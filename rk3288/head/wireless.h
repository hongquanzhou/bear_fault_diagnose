#ifndef WIRELESS_H
#define WIRELESS_H
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
    #include <netinet/tcp.h>
    #include <string>
    class Wireless
    {
        private:
            int sockfd;
            int in_len=0;
  	        struct sockaddr_in servaddr;
            int NET_PORT;
            std::string NET_IP;
        public:
            Wireless(std::string ip,int port);
            bool getStatus();
            ~Wireless();
    };
#endif