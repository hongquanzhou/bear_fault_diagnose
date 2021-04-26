#include "head/Sampler.h"
void* readSaveThread(void*arg)
{
    Sampler* p = (Sampler*)arg;
    p->readSave();
}
Sampler::Sampler(const char* file,int num,string fileDir)
{
    ok = 0;
    fd = open(file,O_RDWR);
    setHz(num);
    setFileDir(fileDir);
    state = STOPED;
}
void Sampler::setHz(int num)
{
    hz = num;
}
void Sampler::setFileDir(string file)
{
    this->fileDir = file;
    // write(fd,fileDir.c_str(),fileDir.size());
}
void Sampler::startSample()
{
    if(state==STOPED)
    {
        //create file
        time_t timep;
        struct tm *p;
        time(&timep);
        p = localtime(&timep);
        sprintf(fileName,(fileDir+"/%d_%02d_%02d_%02d_%02d_%02d_sampling").c_str(),p->tm_year+1900,p->tm_mon+1,p->tm_mday,p->tm_hour,p->tm_min,p->tm_sec);
        fp = fopen(fileName,"w+");
        
        //start sample
        ioctl(fd,CMD_SET_HZ,hz);
        ioctl(fd,CMD_START_SAMPLE,0);
        state = RUNNING;
        // 文件读取保存线程
        pthread_t t;
        pthread_create(&t,0,readSaveThread,this);
    }
    else
    {
        std::cout<<"sampler is running,please stop it"<<endl;
    }

}
void Sampler::stopSample()
{
    if(state==RUNNING)
    {
        ioctl(fd,CMD_STOP_SAMPLE,0);
        state = STOPED;
        //save file
        while(__sync_fetch_and_add(&ok,0));
        fclose(fp);
        fp = NULL;
        //rename 
        char newFileName[256];
        memcpy(newFileName,fileName,256);
        int len = strlen(fileName);
        newFileName[len-9] = '\0';
        rename(fileName, newFileName);
    }
    else
    {
        std::cout<<"sampler is stopped,please start it"<<endl;
    }

}
void Sampler::readSave()
{
    // pthread_attr_t attr;
    // pthread_attr_init(&attr);
    // pthread_attr_setschedpolicy(&attr, SCHED_RR);
    int cnt = 0;
    while(state==RUNNING)
    {
        
        if(fd>0)
        {
            read(fd,read_buf,8192);
        }
        cnt++;
        if(cnt==10)
        {
            memcpy(read_buf1,read_buf,8192);
            cnt=0;
        }
        if(fp!=NULL)
        {
            __sync_fetch_and_add(&ok, 1);
            if(fwrite(read_buf,1,8192,fp)<0)
            {
                return;
            }
            __sync_fetch_and_sub(&ok, 1);
        }
    }
}
float* Sampler::readOne()
{
    unsigned char temp;
    short *p = (short*)read_buf1;
    for(int i=0;i<4096;i++)
    {
        temp = read_buf1[2*i];
        read_buf1[2*i] = read_buf1[2*i+1];
        read_buf1[2*i+1] = temp;
    }
    int j = 0;
    for(int i=0;i<4096;i++)
    {
        if(i%4!=3)
        {
            float value = p[i]*5.0*1000.0/32768.0;
            float_buf[j] = value;
            j++;
        }
    }
    //各通道归一化
    for(int i=0;i<3;i++)
    {
        int maxValue = -5000;
        int minValue = 5000;
        for(int j=0;j<1024;j++)
        {
            if(maxValue<float_buf[3*j+i])
            {
                maxValue = float_buf[3*j+i];
            }
            if(minValue>float_buf[3*j+i])
            {
                minValue = float_buf[3*j+i];
            }
        }
        int dis = maxValue-minValue;
        for(int j=0;j<1024;j++)
        {
            float_buf[3*j+i] = (float_buf[3*j+i] - minValue)/dis;
        }
    }
    return float_buf;
   
}
Sampler::~Sampler()
{
    if(state==RUNNING)
    {
        stopSample();
    }
    close(fd);
}
