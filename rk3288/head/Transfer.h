#include <stdio.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <unistd.h>
#include <dirent.h>
#include "utils.h"
#include "Infer.h"
using namespace std;
class Transfer
{
    private:
        string serverIp;
        string signalBase;
        string modelBase;
        int serverPort;
        int len;
        int sin_size;
        int client_sockfd;
        struct sockaddr_in remote_addr;
        Infer *infer;
        char buf[BUFSIZ];
    public:
        Transfer(string ip,int port,string signal,string model,Infer* in);
        int transmit(const char* buf);
        int receive(char* buf);
        ~Transfer();
        int processOnece();
};