#include "head/wireless.h"
Wireless::~Wireless()
{

}
Wireless::Wireless(std::string ip,int port)
{
	NET_PORT = port;
	NET_IP = ip;
}
bool Wireless::getStatus()
{
	if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) 
	{
		perror("Could not create simple datagram socket");
		return false;
	}
	in_len = sizeof(struct sockaddr_in);
    /*设置默认服务器的信息*/
	servaddr.sin_family = PF_INET;
	servaddr.sin_port = htons(NET_PORT);
	servaddr.sin_addr.s_addr = inet_addr(NET_IP.c_str());
	memset(servaddr.sin_zero,0,sizeof(servaddr.sin_zero));
	int syncnt = 1; // 用于检测环境是否ok，提高灵敏度 1+2 = 3 秒
    setsockopt(sockfd, IPPROTO_TCP, TCP_SYNCNT, &syncnt, sizeof(syncnt));
	/*connect 函数 检测连接*/
	if(connect(sockfd,(struct sockaddr* )&servaddr,in_len) < 0 ) 
	{   
		close(sockfd);
	    return false; 
	}else{   
		close(sockfd);
		return true;
	}  
}