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
int Transfer::processOnece()
{
    int client_sockfd;
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
    len = readline(client_sockfd,buf,BUFSIZ);
    if(len<0)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s",buf);
    if(strcmp(buf,"give me your info\n")!=0)
    {
        printf("info erro,over\n");
        close(client_sockfd);
        return 0;
    }
    //send info;
    sprintf(buf,"{id:1,trainNumber:\"G520\",vehicleNumber:12345,bogie:1}\n");
    len = send(client_sockfd,buf,strlen(buf),0); 
    printf("send info:%s",buf);

    //确认有无模型需要下载
    len = readline(client_sockfd,buf,BUFSIZ);
    if(len<0)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s",buf);
    if(strcmp(buf,"no model to download\n")==0)
    {
        std::cout<<"no model to download"<<endl;
    }
    else
    {
        std::cout<<"downloading model..."<<endl;
        //模型下载
        sprintf(buf,"ok\n");
        len = send(client_sockfd,buf,strlen(buf),0); 
        printf("send info:%s",buf);
        //获取文件名
        char fileName[128];
        memcpy(fileName,(modelBase+"/").c_str(),modelBase.length()+1);
        len = readline(client_sockfd,buf,128);
        if(len<0)
        {
            close(client_sockfd);
            return -1;
        }
        buf[len-1] = '\0';
        strcat(fileName,buf);
        //获取文件大小
        len = readline(client_sockfd,buf,BUFSIZ);
        if(len<0)
        {
            close(client_sockfd);
            return -1;
        }
        printf("filesize:%s",buf);
        int length = parseInt(buf);
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
    len = readline(client_sockfd,buf,BUFSIZ);
    if(len<0)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s\n",buf);
    if(strcmp(buf,"Do you have signal to save\n")==0)
    {
        vector<char*>*  fileNameVector = getFileList(signalBase.c_str());
        if(fileNameVector->size()==0)  //无信号上传
        {
            std::cout<<"no signal upload"<<endl;
            sprintf(buf,"no\n");
            len = send(client_sockfd,buf,strlen(buf),0); 
        }
        else
        {
            std::cout<<"have signal upload"<<endl;
            std::cout<<"signal uploading..."<<endl;
            sprintf(buf,"yes\n");
            len = send(client_sockfd,buf,strlen(buf),0); 
            //数据数目
            int num;
            int cnt;
            int length;
           
            num = fileNameVector->size();
            unsigned char readbuf[128];
            sprintf(buf,"%d\n",num);
            len = send(client_sockfd,buf,strlen(buf),0); 
            for(int i=0;i<num;i++)
            {
                //发送文件名
                strcpy(buf,(*fileNameVector)[i]);
                strcat(buf,"\n");
                send(client_sockfd,buf,strlen(buf),0);
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
                send(client_sockfd,buf,strlen(buf),0);
                //发送文件数据
                FILE* signalfp = fopen(fullName,"r");
                while((length = fread(readbuf,1,128,signalfp))>0)
                {
                    send(client_sockfd,readbuf,length,0);
                }
                std::cout<<"one file ok\n";
                remove(fullName);
                fclose(signalfp);
            }   
            std::cout<<"signal send over"<<endl;      
        }
    }
    else
    {
        printf("no data save!over\n");
    }
    len = readline(client_sockfd,buf,BUFSIZ);
    if(len<0)
    {
        close(client_sockfd);
        return -1;
    }
    printf("get msg:%s",buf);
    close(client_sockfd);
    return 0;
}
