#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/slab.h>
#include <linux/jiffies.h>
#include <linux/i2c.h>
#include <linux/mutex.h>
#include <linux/fs.h>
#include <asm/uaccess.h>
#include <linux/types.h>
#include <linux/errno.h>
#include <linux/mm.h>
#include <linux/sched.h>
#include <linux/cdev.h>
#include <linux/device.h>
#include <linux/io.h>

#define CHRMEM_SIZE 0x1000
#define MEM_CLEAR   0x1

#define SSD1306_CMD    0
#define SSD1306_DAT    1

#define SSD1306_WIDTH    128
#define SSD1306_HEIGHT   64


static struct i2c_board_info ssd1306_client2=
{ 
        .type="ssd1306",
        .addr=0x3c,
};

struct i2c_client *ssd1306_client;
struct i2c_adapter *adapter;
static int ssd1306_module_init(void)
{
    printk("ssd1306 device enter\n");
    adapter = i2c_get_adapter(4);
    ssd1306_client = i2c_new_device(adapter, &ssd1306_client2);
    return 0;
}

static void ssd1306_module_exit(void)
{
    printk("ssd1306 device exit\n");
}

module_init(ssd1306_module_init);
module_exit(ssd1306_module_exit);

MODULE_LICENSE("GPL");
