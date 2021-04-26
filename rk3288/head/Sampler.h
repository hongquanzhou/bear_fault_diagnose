#ifndef SAMPLER_H
    #define SAMPLER_H
    #include <stdio.h>
    #include <stdlib.h>
    #include <sys/stat.h>
    #include <sys/types.h>
    #include <sys/ioctl.h>
    #include <unistd.h>
    #include <fcntl.h>
    #include <string>
    #include <string.h>
    #include <iostream>
    // #define  __KERNEL__
    // #define __LINUX_ARM_ARCH__ 7
    // #include <asm/atomic.h>
    using namespace std;
    
    enum CMD_AD7606
    {
        CMD_SET_HZ=3,
        CMD_START_SAMPLE=4,
        CMD_STOP_SAMPLE=5, 
    };
    enum AD7606_STATE
    {
        RUNNING,STOPED
    };
    class Sampler
    {
        public:
            int fd; //采集设备
            int fp; //采集数据保存文件
            int hz;
            enum AD7606_STATE state;
            string fileDir;
            unsigned char read_buf[8192];
            unsigned char read_buf1[8192];//推断保存
            char fileName[256];
            int onefileSeconds;
        public:
            float float_buf[4096];
            Sampler(const char* file,int num,string fileDir,int Seconds);
            void setHz(int num);
            void setFileDir(string fileDir);
            float* readOne(); 
            void readSave();  
            void startSample();     
            void stopSample();       
            ~Sampler();
    };
#endif