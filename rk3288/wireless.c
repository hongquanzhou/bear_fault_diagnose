#include <sys/types.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <linux/wireless.h>
#include <string.h> 
#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#define NET_PORT 53
#define NET_IP "8.8.8.8" //谷歌DNS
int main()
{
    int sockfd;
    struct iwreq wreq;
    struct iw_statistics stats;
    char buffer[32];
    memset(buffer,0,32);
    memset(&stats,0,sizeof(stats));
    memset(&wreq,0,sizeof(wreq));
    strcpy(wreq.ifr_ifrn.ifrn_name,"wlan0");
    if((sockfd=socket(AF_INET,SOCK_DGRAM,0))==-1)
    {
        perror("Could not create simple datagram socket");
		exit(EXIT_FAILURE);
    }
    wreq.u.essid.pointer = buffer;
    wreq.u.essid.length = 32;
    if (ioctl(sockfd,SIOCGIWESSID, &wreq) == -1) {
	 	perror("IOCTL SIOCGIWESSID Failed,error");
	 	exit(2);
	}else {	
	 	//printf("IOCTL SIOCGIWESSID Successfull\n");
		printf("The essid is:%s\n",wreq.u.essid.pointer);        //network name
	}
	close(sockfd);
    return 0;
}