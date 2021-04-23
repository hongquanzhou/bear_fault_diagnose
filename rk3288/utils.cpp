#include "head/utils.h"

int getFileSize(const char *filename)
{
	printf("file:%s\n",filename);
    FILE* fp = fopen(filename,"r");
    if(!fp) return -1;
    fseek(fp,0,SEEK_END);
    int size = ftell(fp);
    fclose(fp);
    return size;
}
ssize_t readn(int fd, void *buf, size_t count)
{
	size_t nleft=count;//剩余字节数
	ssize_t nread;//已经接受的字节数
	char *bufp = (char*)buf;//

	while(nleft>0)
	{
		if((nread=read(fd, bufp, nleft))<0)
		{
			if(errno==EINTR)//信号中断
			{
				continue;
			}
			return -1;//否则出错
		}
		else if(nread==0)//表示对方关闭传送
		{
			return count-nleft;//返回已读字节数
		}
		bufp+=nread;//进行指针偏移
		nleft -= nread;
	} 
	return count;
}
ssize_t recv_peek(int sockfd, void *buf,size_t len)
{
	while(1)
	{
		//recv函数只用于套接口
		//recv函数读取后，不将数据在缓冲区清除
		int ret= recv(sockfd, buf, len, MSG_PEEK);
		if(ret == -1 && errno == EINTR)
			continue;
		return ret;
	}
}

ssize_t readline(int sockfd, void *buf, size_t maxline)
{
	int ret;//设置窥探返回值
	int nread;//设置已窥探字符数
	char *bufp = (char*)buf;//缓存buf
	char *buf1 = (char*)buf;
	int nleft = maxline;//设置maxline为包最大长度，nleft为剩余需读取字符数
	while(1)
	{
		ret=recv_peek(sockfd, bufp, nleft);
		if(ret<0)
		{
			return ret;
		}
		else if(ret==0)
		{
			return ret;//对方终止了传送
		}

		nread=ret;
		int i;//检测有没有‘\n’字符，有则读取
		for(i=0; i<nread;i++)
		{
			if(bufp[i]=='\n')
			{
				ret= readn(sockfd,bufp, i+1);
				if(ret != i+1)//已经窥探到有i+1字符，如果没有则错误
					return -1;
				buf1[ret] = '\0';
				return ret;
			}
		}
		//如果没有读到‘\n’，则将消息读入，直到最大包
		nleft -= nread;
		ret= readn(sockfd, bufp, nread);
		if(ret != nread)//已经窥探到有nread个字符，如果readn函数不能读取这么多，则错误
		{
			exit(EXIT_FAILURE);
		}
		bufp += nread;
	}
	return -1;
}

int parseInt(const char*size) //含有\n
{
    int len = strlen(size)-2;
    int ret = 0;
    for(int i=0;i<len;i++)
    {
        ret = ret*10 + size[i]-'0';
    }
    return ret;
}
vector<char*>* getFileList(const char *baseDir)
{
    vector<char*>* ret = new vector<char*>();
    DIR* dir = opendir(baseDir);
    struct dirent *file;
    while((file=readdir(dir))!=NULL)
    {
        char *buf = (char*)malloc(128);
        strcpy(buf,file->d_name);
		int len = strlen(buf);
        if((strcmp(buf,".")!=0)&&(strcmp(buf,"..")!=0)&&buf[len-1]!='g')
        { 
            ret->push_back(buf);
        }
    }
    closedir(dir);
    return ret;
}
