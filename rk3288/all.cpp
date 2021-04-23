#include "head/Oled.h"
#include "head/Infer.h"
#include "head/Properties.h"
#include "head/Sampler.h"
#include "head/Transfer.h"
#include "head/wireless.h"
#include <pthread.h>
Oled *led;
bool isCon = true;
unsigned char buf2[32];
unsigned char buf3[32];
void* getTimeThread(void* args)
{
    const char *wday[]={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    char buf[32];
    time_t timep;
    struct tm *p;
    time(&timep);
    while(1)
    {   
        p = localtime(&timep);
        sprintf(buf,"%d-%d-%d",(1900+p->tm_year), (1+p->tm_mon),p->tm_mday);
        led->setString(buf,5,0);
        sprintf(buf,"%s %02d:%02d:%02d", wday[p->tm_wday], p->tm_hour, p->tm_min, p->tm_sec);
        led->setString(buf,5,16);
        timep++;
        sleep(1);
    }
}
void* getWifiStatus_thread(void* args)
{
    Transfer* t = (Transfer*)args;
    Properties prop("conf.ini");
    string ip = prop.getProperties("serverIp");
    int port = stoi(prop.getProperties("connectPort"));
    Wireless w(ip,port);
    bool last;
    isCon = w.getStatus();
    last = !isCon;
    while(1)
    {
        isCon = w.getStatus();
        if(isCon!=last)
        {
            last = isCon;
            if(isCon)
            {
                led->setFigure(buf2,16,16,96,48);
                led->setString("transfering  ",5,32);
                t->processOnece();
                led->setString("             ",5,32);
            }
            else
            {
                led->setFigure(buf3,16,16,96,48);
            }
        }
        sleep(2);
    }
    return 0;
}
int main()
{
    char mapToresults[11][20]={"DB    ","DIR   ","DOR@3 ","DOR@6 ","DOR@12","FB    ","FIR   ","FOR@3  ","FOR@6  ","FOR@12","normal"};
    pthread_t tids[2];
    Properties prop("conf.ini");
    string oledFile = prop.getProperties("oledFile");
    string samplerFile = prop.getProperties("samplerFile");
    string serverIp = prop.getProperties("serverIp");
    string serverPort = prop.getProperties("serverPort");
    string modelSaveBase = prop.getProperties("modelSaveBase");
    string modelName = prop.getProperties("modelName");
    string signalSaveBase = prop.getProperties("signalSaveBase");
    string sampleRate = prop.getProperties("sampleRate");
    string logoPic = prop.getProperties("logoPic");
    int oneFileSeconds = stoi(prop.getProperties("oneFileSeconds"));
    Oled oled(oledFile.c_str());
    led = &oled;
    
    FILE* fp_wifi_con = fopen("./pic/wifi_con.bin","rb");
    fread(buf2,1,32,fp_wifi_con);
    fclose(fp_wifi_con);

    FILE* fp_wifi_no_con = fopen("./pic/wifi_no_con.bin","rb");
    fread(buf3,1,32,fp_wifi_no_con);
    fclose(fp_wifi_no_con);
    int lastResult=-1;
    //show logo
    unsigned char buf[1024];
    FILE* logoPicFile = fopen(logoPic.c_str(),"rb");
    fread(buf,1,1024,logoPicFile);
    oled.setFigure(buf,128,64,0,0);
    sleep(1);
    //清屏幕
    memset(buf,'\0',1024);//清屏
    oled.setFigure(buf,128,64,0,0);
    oled.setString("starting",5,48);
    Infer infer(modelSaveBase,modelName);
    Transfer transfer(serverIp,stoi(serverPort),signalSaveBase,modelSaveBase,&infer);
    int ret = pthread_create(&tids[1], NULL, getTimeThread, NULL);
    pthread_create(&tids[0], NULL, getWifiStatus_thread, &transfer);
    Sampler sampler(samplerFile.c_str(),stoi(sampleRate),signalSaveBase);
    sampler.startSample();
    int cnt = 0;
    while(1)
    {
        string res = "result:";
        float* in = sampler.readOne();
        infer.setInput(in);
        int result = infer.infer();
        if(result!=lastResult)
        {
            res += mapToresults[result];
            oled.setString(res.c_str(),5,48);
            lastResult = result;
        }
        
        cnt++;
        if(cnt==oneFileSeconds)
        {
            cnt = 0;
            sampler.stopSample();
            sleep(2);
            sampler.startSample();
        }
        sleep(1);
    }
    sampler.stopSample(); 
    return 0;
}