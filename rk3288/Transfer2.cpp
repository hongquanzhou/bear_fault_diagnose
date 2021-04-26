#include "head/Transfer.h"

Transfer::Transfer(string ip,int port,string signal,string model,Infer*in)
{
    serverIp = ip;
    serverPort = port;
    signalBase = signal;
    modelBase = model;
    infer = in;
    std::cout<<serverIp<<" "<<port<<" "<<signalBase<<" "<<modelBase<<endl;
}
Transfer::~Transfer()
{
    
}
enum TransState
{
    START,
    GETINFO,
    MODEL,
    SIGNAL,
    END
};
int times = 5;
int Transfer::transmit(const char* buf)
{
    printf("send:%s",buf);
    int len = 0;
    int flag = 1;
    char recv[64];
    for(int i=0;i<times&&flag;i++)
    {
        
        len = send(client_sockfd,buf,strlen(buf),0);
        if(len == -1)
            return -1;
        printf("sended:%s",buf);
        len = readline(client_sockfd,recv,BUFSIZ);
        if(len == -1)
            return -1;
        printf("received:%s",recv);

        if(strcmp(buf,recv)==0)
        {
            flag = 0;
            sprintf(recv,"yes\n");
        }
        else
        {
            sprintf(recv,"no\n");
        }
        len = send(client_sockfd,recv,strlen(recv),0);
        if(len == -1)
            return -1;
        printf("sended ACK:%s",recv);
    }
    printf("over\n\n");
    if(flag==0)
    {
        return 0;
    }
    else
    {
        return -1;
    }
}
int Transfer::receive(char* buf)
{
    
    char recv1[32];
    int len = 0;
    int flag = 1;
    for(int i=0;i<times&&flag;i++)
    {
        len = readline(client_sockfd,buf,BUFSIZ);
        if(len == -1)
            return -1;
        printf("received:%s",buf);
        
        len = send(client_sockfd,buf,strlen(buf),0);
        if(len == -1)
            return -1;
        printf("sended:%s",buf);

        len = readline(client_sockfd,recv1,BUFSIZ);
        if(len == -1)
            return -1;
        printf("recvived ack:%s",recv1);
        if(strcmp(recv1,"yes\n")==0)
        {
            flag = 0;
        }
        
    }
    printf("over\n\n");
    if(flag==0)
    {
        buf[strlen(buf)-1] = '\0';
        return 0;
    }
    else
    {
        return -1;
    }

}
int Transfer::processOnece()
{
    memset(&remote_addr,0,sizeof(remote_addr));
    remote_addr.sin_family = AF_INET;
    remote_addr.sin_addr.s_addr = inet_addr(serverIp.c_str());
    remote_addr.sin_port = htons(serverPort);
    if((client_sockfd = socket(PF_INET,SOCK_STREAM,0))<0)
    {
        perror("socket error");
        return 1;
    }
    sin_size = sizeof(struct sockaddr_in);
    if(connect(client_sockfd,(struct sockaddr*)&remote_addr,sizeof(sockaddr_in))<0)
    {
        perror("connect error");
        return 1;
    }
    printf("connected to server\n");
    if(receive(buf)==-1)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s",buf);
    if(strcmp(buf,"give me your info")!=0)
    {
        printf("info erro,over\n");
        close(client_sockfd);
        return 0;
    }
    //send info;
    sprintf(buf,"{id:1,trainNumber:\"G520\",vehicleNumber:12345,bogie:1}\n");
    if(transmit(buf)==-1)
    {
        close(client_sockfd);
        return -1;
    }
   
    //确认有无模型需要下载
    if(receive(buf)==-1)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s",buf);
    if(strcmp(buf,"no model to download")==0)
    {
        std::cout<<"no model to download"<<endl;
    }
    else
    {
        std::cout<<"downloading model..."<<endl;
        //模型下载
        sprintf(buf,"ok\n");
        if(transmit(buf)==-1)
        {
            close(client_sockfd);
            return -1;
        }
        //获取文件名
        char fileName[128];
        memcpy(fileName,(modelBase+"/").c_str(),modelBase.length()+2);
        if(receive(buf)==-1)
        {
            close(client_sockfd);
            return -1;
        }
        strcat(fileName,buf);
        printf("fileName:%s\n",fileName);
        //获取文件大小
        if(receive(buf)==-1)
        {
            close(client_sockfd);
            return -1;
        }
        printf("filesize:%s\n",buf);
        int length = stoi(buf);
        std::cout<<"size:"<<length<<endl;
        //写文件
        FILE* modelFd = fopen(fileName,"w+");
        while(length > 0)
        {
            len = min(length,BUFSIZ);
            len = recv(client_sockfd,buf,len,0);
            fwrite(buf,1,len,modelFd);
            length = length - len;
        }
        fclose(modelFd);
        infer->updateModel();
        std::cout<<"model download success"<<endl;
    }
    //数据上传阶段
    cout<<"signal updload stage"<<endl;
    if(receive(buf)==-1)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s\n",buf);
    if(strcmp(buf,"Do you have signal to save")==0)
    {
        vector<char*>*  fileNameVector = getFileList(signalBase.c_str());
        if(fileNameVector->size()==0)  //无信号上传
        {
            std::cout<<"no signal upload"<<endl;
            sprintf(buf,"no\n");
            if(transmit(buf)==-1)
            {
                close(client_sockfd);
                return -1;
            }
        }
        else
        {
            std::cout<<"have signal upload"<<endl;
            std::cout<<"signal uploading..."<<endl;
            sprintf(buf,"YES\n");
            if(transmit(buf)==-1)
            {
                close(client_sockfd);
                return -1;
            }
            //数据数目
            int num;
            int cnt;
            int length;
            num = fileNameVector->size();
            unsigned char readbuf[128];
            sprintf(buf,"%d\n",num);
            if(transmit(buf)==-1)
            {
                close(client_sockfd);
                return -1;
            }
            for(int i=0;i<num;i++)
            {
                //发送文件名
                strcpy(buf,(*fileNameVector)[i]);
                strcat(buf,"\n");
                if(transmit(buf)==-1)
                {
                    close(client_sockfd);
                    return -1;
                }
                //发送文件大小
                char fullName[128];
                memcpy(fullName,signalBase.c_str(),signalBase.length());
                fullName[signalBase.length()] = '/';
                fullName[signalBase.length()+1] = '\0';
                strcat(fullName,(*fileNameVector)[i]);
                printf("fullname:%s\n",fullName);
                int fileSize = getFileSize(fullName);
                printf("filesize:%d\n",fileSize);
                sprintf(buf,"%d\n",fileSize);
                if(transmit(buf)==-1)
                {
                    close(client_sockfd);
                    return -1;
                }
                //发送文件数据
                FILE* signalfp = fopen(fullName,"r");
                while(fileSize>0)
                {
                    length = fileSize>128?128:fileSize;
                    length = fread(readbuf,1,length,signalfp);
                    send(client_sockfd,readbuf,length,0);
                    fileSize -= length;
                }
                std::cout<<"one file ok\n";
                remove(fullName);
                fclose(signalfp);
            }   
            std::cout<<"signal send over"<<endl;   
            for(int i=0;i<fileNameVector->size();i++)
            {
                free((*fileNameVector)[i]);
            }
            delete fileNameVector;   
        }
    }
    else
    {
        printf("erro!over\n");
        close(client_sockfd);
        return -1;
    }
    if(receive(buf)==-1)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s\n",buf);
    printf("over\n");
    close(client_sockfd);
    return 0;
}
