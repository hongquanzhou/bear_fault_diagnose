#include <linux/init.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/miscdevice.h>
#include <linux/pwm.h>
#include <linux/spi/spi.h>
#include <linux/of.h>
#include <asm/uaccess.h>
#include <asm/io.h>
#include <linux/gpio.h>
#include <linux/interrupt.h>
#include <linux/delay.h>
#include <linux/mutex.h>
#include <asm/irq.h>
#include <linux/timer.h>
#include <linux/timex.h>
#include <linux/rtc.h>
#include <linux/sched.h>
#include <linux/workqueue.h>

/*******************************************/
/***             数据结构定义             ***/
/*******************************************/
enum STAT
{
    READY,
    RUNNING
};
struct pwm_ctrl
{
    unsigned enable:1;
    unsigned pwm_mode:2;
    unsigned duty_pol:1;
    unsigned inactive_pol:1;
    unsigned output_mode:1;
    unsigned reserved1:2;
    unsigned lp_en:1;
    unsigned clk_sel:1;
    unsigned reserved2:2;
    unsigned prescale:3;
    unsigned reserved3:1;
    unsigned scale:8;
    unsigned rpt:8;
};
struct pwm_register
{
    unsigned int cnt;
    unsigned int period;
    unsigned int duty;
    unsigned int ctrl; 
};

/* read */
struct double_buf
{
    uint8_t buf_read_pos;
    uint32_t read_cnt;
    uint8_t buf_write_pos;
    uint32_t write_cnt;
    unsigned char user_read_buf1[16384];
};

/*******************************************/
/***            变量定义                  ***/
/*******************************************/

// struct pwm_state state;
static struct pwm_register pwm1;
static struct pwm_register *pwm_real;
static struct pwm_state state;

/*  spi_driver  */
static struct spi_device *ad7606 = NULL;
static unsigned char read_buf[2056];
// static unsigned char send_buf[1024];

/*  gpio  */
const unsigned int BUSY = 13;
const unsigned int RST = 219;
static struct mutex m1;
static unsigned int j =0;
/* 申请 GPIO0B5(13) for busy中断 下降沿触发； 申请GPIO7A3(219) for复位 */
unsigned short *p;
// static unsigned int j =0;
unsigned long flag;
static int irq;

/* pwm */
unsigned int *pwm1_mux;
unsigned int *clk,*clk_g7;

/* read */
static struct double_buf df;

/* write file */
static char fileDir[128];
static  loff_t pos = 0;

/* 采集状态 */
static enum STAT sample_status = READY;

static uint8_t status = 1;

/* work_thread */
struct tasklet_struct task_t ; 
struct workqueue_struct *mywork ;
struct work_struct work;
static atomic_t work_cnt = ATOMIC_INIT(0);
static int condition = 0;
DECLARE_WAIT_QUEUE_HEAD(my_queue);

/* read_thread*/
DECLARE_WAIT_QUEUE_HEAD(read_queue);
static uint32_t cnt;

/*******************************************/
/***            函数定义                  ***/
/*******************************************/

static void my_pwm_enable(int cmd)
{
    unsigned int temp;
    temp = pwm_real->ctrl;
    if(cmd)
    {
        temp |= 1 ;
    }  
    else
    {
        temp &=~1;
    }
    pwm_real->ctrl = temp;
}
static void my_pwm_set_plority(int cmd)
{
    unsigned int temp;
    temp = pwm_real->ctrl;
    if(cmd)
    {
        temp |= 1<<3 ;
    }  
    else
    {
        temp &=~(1<<3);
    }
    pwm_real->ctrl = temp;
    printk("ctrl:0x%08x\n",pwm_real->ctrl);
}

static void my_pwm_config(struct pwm_state* state)
{
    unsigned int temp;
    printk("config\n");
    pwm1.period = state->period;
    pwm1.duty = state->duty_cycle;
    my_pwm_set_plority(1);
    if(state->enabled)
    {
        my_pwm_enable(1);
    }
    else
    {
        my_pwm_enable(0);
    }
    my_pwm_enable(0);
    pwm_real->period = pwm1.period;
    pwm_real->cnt = 0;
    pwm_real->duty = pwm1.duty;
    //连续模式
    temp = pwm_real->ctrl;
    temp |= 1<<1;
    temp &= ~(1<<2); 
    pwm_real->ctrl = temp;
    printk("update status:\n");
    printk("cnt:%d\n",pwm1.cnt);
    printk("period:%d\n",pwm1.period);
    printk("duty:%d\n",pwm1.duty);   
}
static int	sample_probe(struct spi_device *spi)
{
    printk("sample_probe\n");
    printk("spi device:%s\n",spi->modalias);
    ad7606 = spi;
    return 0;
}
static int	sample_remove(struct spi_device *spi)
{
    printk("sample_remove\n");
    printk("spi max speed:%uhz\n",spi->max_speed_hz);
    return 0;
}
static const struct of_device_id spidev_dt_ids[] = {
    { .compatible = "ad7606" },
    {},
};

static struct spi_driver sample_driver = {
    .driver = {
        .name =  "ad7606_driver",
        .owner =    THIS_MODULE,
        .of_match_table = of_match_ptr(spidev_dt_ids),
     },
    .probe = sample_probe,
    .remove = sample_remove,

};

static uint8_t readCondition = 0;
static ssize_t ad7606_read (struct file *filp, char __user * buf, size_t size, loff_t *pos)
{
    int ret;
    unsigned char* p;
    uint8_t temp = df.buf_write_pos;
    if(( temp >3&&df.buf_read_pos==1)||(temp <=3&&df.buf_read_pos==0))
    {
        readCondition = 0;
        wait_event(read_queue,readCondition);
    }
    if(df.buf_read_pos==1)
    {
        p = &df.user_read_buf1[8192];
        df.buf_read_pos = 0;
    }
    else
    {
        p = &df.user_read_buf1[0];
        df.buf_read_pos = 1;
    }
    ret = copy_to_user(buf,p,8192);
    return ret;
}
static ssize_t ad7606_write (struct file *filp, const char __user * buf, size_t size, loff_t *pos)
{ 
    int ret = -1;
    int len;
    printk("ad7606_write\n\n");
    if(!ad7606)
    {
        return -1;
    }
    len = size < 64?size:64;
    ret = copy_from_user(fileDir,buf,len);
    fileDir[len] = '\0';
    printk("fileDir:%s\n",fileDir);
    return ret;
}
static int ad7606_open (struct inode *node, struct file *filp)
{
    printk("ad7606_open\n");
    return 0;
}

static void ad7606_reset(void)
{
    gpio_set_value(RST,1);
    ndelay(60);
    gpio_set_value(RST,0);
    
}
static void start_sample(void)
{
    if(sample_status == READY)
    {
        my_pwm_config(&state);
        printk("j start value:%u\n",j);
        ad7606_reset();
        my_pwm_enable(1);
        sample_status = RUNNING;
        df.buf_read_pos = 0;
        df.buf_write_pos = 0;
        df.read_cnt = 0;
        df.write_cnt = 0;
        cnt = 0;
    }
    else
    {
        printk("is running!please stop!\n");
    }
}
static void stop_sample(void)
{
    if(sample_status == RUNNING)
    {
        my_pwm_enable(0);
        printk("j over value:%u\n",j);
        while(atomic_read(&work_cnt)>0);
        sample_status = READY;
        printk("cnt read:%d,write:%d\n",df.read_cnt,df.write_cnt);
    }
    else
    {
        printk("no running!please start!\n");
    }

}

static void read_a_sample(void)
{
    int i = 0;
    for(i=0;i<100;i++)
    {
        spi_read(ad7606,read_buf,16);
        udelay(50);
    }  
    printk("read a sample\n");
}
static void set_hz(int num)
{
    printk("sethz:%d\n",num);
    state.period = (unsigned int)(10*743*10000/num);
    state.duty_cycle = (unsigned int)(5*743*10000/num);
}
enum CMD_AD7606
{
    CMD_SET_HZ=3,
    CMD_START_SAMPLE,
    CMD_STOP_SAMPLE,
    CMD_READ_A_SMPLE,
};
static long ad7606_unlocked_ioctl (struct file *filp, unsigned int cmd, unsigned long arg)
{
    switch(cmd)
    {   
        case CMD_SET_HZ:set_hz(arg);break;
        case CMD_START_SAMPLE:start_sample();break;
        case CMD_STOP_SAMPLE:stop_sample();break;
        case CMD_READ_A_SMPLE:read_a_sample();break;
        default:break;
    }
    return 0;
}
static int ad7606_release (struct inode *node, struct file *filp)
{
    if(sample_status == RUNNING)
    {
        stop_sample();
    }
    return 0;
}
struct file_operations ad7606_fops = {
    .owner = THIS_MODULE,
    .open = ad7606_open,
    .release = ad7606_release,
    .write = ad7606_write,
    .read = ad7606_read,
    .unlocked_ioctl = ad7606_unlocked_ioctl,
};
struct miscdevice ad7606_dev =
{  
    .minor  =   MISC_DYNAMIC_MINOR,
    .fops   =   &ad7606_fops,
    .name   =   "ad7606_sampler",
};

int pwm_init(void)
{
    unsigned long base = 0xff680030;
    unsigned long iomux = 0xff770078;
    unsigned long clk_base = 0xff760188;
    unsigned int temp;
    pwm_real = (struct pwm_register*)ioremap(base,16);
    pwm1_mux = (unsigned int*)ioremap(iomux,4);
    clk = (unsigned int*)ioremap(clk_base,4);
    //复用引脚设置  2位为1
    temp = pwm1_mux[0];
    temp |= (1<<12); 
    temp |= (1<<13); 
    temp &=~(1<<14);
    temp |=(1<<(12+16));//需要使能高16位对应位
    temp |=(1<<(13+16));//需要使能高16位对应位
    temp |=(1<<(14+16));
    pwm1_mux[0] = temp;
    printk("pwm_mux:0x%08x\n",pwm1_mux[0]);
    //pwm 时钟使能：0位为0
    temp = clk[0];
    temp &=~(1<<0);
    temp |=(1<<16);
    clk[0] = temp;
    printk("clk:0x%08x\n",clk[0]);
    //连续模式  x01x
    temp = pwm_real->ctrl;
    temp |= 1<<1;
    temp &= ~(1<<2); 
    pwm_real->ctrl = temp; 
    printk("ctrl:%08x\n",pwm_real->ctrl);
    state.period = 10000;
    state.duty_cycle = 5000;
    my_pwm_config(&state);
    misc_register(&ad7606_dev);
    printk("init\n");
    return 0;
}
void pwm_exit(void)
{   
    misc_deregister(&ad7606_dev);
    iounmap(pwm_real);
    iounmap(pwm1_mux);
    iounmap(clk);
}


//tasklet
static void mytasklet(unsigned long u)
{
    spi_read(ad7606,read_buf,16);
}
DECLARE_TASKLET(tasklet_spi_read,mytasklet,0);
// work_queue

unsigned int Pos = 0; 
static void do_work(struct work_struct *work)
{ 
    spi_read(ad7606,&read_buf[Pos],16); 
    if(Pos<2040)
    {
        Pos += 8;
    }
    else  
    {   
        pos += 2048;
        Pos = 0;
        memcpy(&df.user_read_buf1[df.buf_write_pos*2048],read_buf,2048);
        df.buf_write_pos++;
        cnt++;
        if(cnt%4==0&&cnt>0&&readCondition==0)
        {
            readCondition = 1;
            wake_up(&read_queue);
        }
        if(df.buf_write_pos==8) 
        {
            df.buf_write_pos = 0;
            df.write_cnt++;
        }
    }  
}


static irqreturn_t irq_handler(int irq,void *id)
{   
    // schedule_work(&work);
    // queue_work_on(WORK_CPU_UNBOUND, system_highpri_wq,&work);
    atomic_inc(&work_cnt);
    condition = 1;
    wake_up_interruptible(&my_queue);
    j++;
    return IRQ_HANDLED;
}

static int ad7606_worker_fn(void *worker_ptr)
{
    while(status)
    {
        if( atomic_read(&work_cnt)>0)
        {
            atomic_dec(&work_cnt);
            do_work(NULL);
        }
        else
        {
            condition = 0;
            wait_event_interruptible(my_queue,condition);
        }
    }
    return 0;
}
struct task_struct *task = NULL;
static int spi_init(void)
{
    int ret;
    struct task_struct *task=NULL;
    // struct sched_param param = { .sched_priority = 1 };
    struct sched_attr attr;
    memset(&attr, 0, sizeof(attr));
    attr.size = sizeof(attr);
    attr.sched_policy = SCHED_DEADLINE; 
    attr.sched_period = 100*1000;
    attr.sched_runtime = 80*1000;
    attr.sched_deadline = 90*1000;

    //添加驱动
    ret = spi_register_driver(&sample_driver);
    if(ret!=0)
    {
        printk("add spi driver failed\n");
        return -1;
    }
    printk("add spi driver success\n");
    
    mywork = create_workqueue("my work");
    INIT_WORK(&work,do_work);
    queue_work(mywork,&work);
    // 实时工作线程
	task = kthread_run(ad7606_worker_fn,NULL, "ad7606");
    
	if (IS_ERR(task)) {
		printk("failed to create message pump task\n");
		return 0;
	}
	// sched_setscheduler(task, SCHED_FIFO, &param);
    sched_setattr(task,&attr);
    return 0;
}
static void spi_exit(void)   
{
    spi_unregister_driver(&sample_driver);
    destroy_workqueue(mywork);
}

static int gpio_init(void)
{
    int ret;
    ret = gpio_request(BUSY, NULL);
    if(ret < 0)
    {
        printk("gpio Busy request failed\n");
        return -1;
    }
    ret = gpio_request(RST, NULL);
    if(ret < 0)
    {
        printk("gpio RST request failed\n");
        goto err0;
    }
    //设置BUSY
    irq = gpio_to_irq(BUSY);
    if(irq < 0)
    {
        printk("busy get irq failed\n");
    }
    printk("irq:%d\n",irq);
    gpio_direction_input(BUSY);
    ret = request_irq(irq,irq_handler,IRQF_TRIGGER_FALLING,"busy",NULL);
   // irq_set_affinity(irq,cpumask_of(1));
   // INIT_WORK(&mywq,do_work);
    if(ret<0)
    {
        printk("busy request irq failed!\n");
        goto err1;
    }
    //设置RST >50ns 脉冲上升沿复位
    gpio_direction_output(RST,0);
    mutex_init(&m1);
    return 0;
err1:
    gpio_free(RST);

err0:
    gpio_free(BUSY);
    return -1;

}
static void gpio_exit(void)
{
    free_irq(irq,NULL);
    gpio_free(RST);
    gpio_free(BUSY);

}
static int ad7606_init(void)
{
    pwm_init();
    spi_init();
    gpio_init();
    printk("success\n");
    return 0;
}
static void ad7606_exit(void)
{
    gpio_exit();
    spi_exit();
    pwm_exit();
    status = 0;
    condition = 1;
    wake_up_interruptible(&my_queue);
    printk("exit\n");
}

module_init(ad7606_init);
module_exit(ad7606_exit);
MODULE_LICENSE("GPL");
MODULE_AUTHOR("hq");
MODULE_DESCRIPTION("AD7606 sample driver!");
