#ifndef UTILS_H
    #define UTILS_H
    // #include <stdio.h>
    // #include <sys/types.h>
    // #include <netinet/in.h>
    // #include <arpa/inet.h>
    // #include <fstream>
    #include <iostream>
    #include <vector>
    #include <sys/socket.h>
    #include <string.h>
    #include <unistd.h>
    #include <dirent.h>
    using namespace std;
    int getFileSize(const char *filename);
    vector<char*>* getFileList(const char *baseDir);
    ssize_t readn(int fd, void *buf, size_t count);
    ssize_t recv_peek(int sockfd, void *buf,size_t len);
    ssize_t readline(int sockfd, void *buf, size_t maxline);
    int parseInt(const char*size);
#endif