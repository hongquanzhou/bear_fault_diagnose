#include "head/Oled.h"
    
Oled::Oled(const char* deviceFile)
{
    pthread_t tid;
    //pthread_mutex_init(&lock, NULL); 
    this->filename = deviceFile;
    fd = open(filename.c_str(),O_RDWR);
    memset(this->buf,'\0',1024);
    //刷新线程
    status = true;
}
void Oled::drawPoint(int chXpos, int chYpos, int chPoint)
{
    int chPos, chBx; 
    unsigned char chTemp = 0;

    if (chXpos > 127 || chYpos > 63) {
        return;
    }
    chPos = 7 - chYpos / 8; //
    chBx = chYpos % 8;
    chTemp = 1 << (7 - chBx);

    if (chPoint) {
        this->buf[chXpos][chPos] |= chTemp;

    } else {
        this->buf[chXpos][chPos] &= ~chTemp;
    }
}
void Oled::displayChar(int chXpos, int chYpos, char chChr, int chSize, int chMode)
{
    int i, j;
    int chYpos0 = chYpos;
    unsigned char chTemp;
    chChr = chChr - ' ';
    for (i = 0; i < chSize; i ++) {
        if (chMode) {
            chTemp = c_chFont1608[chChr][i];
        } else {
            chTemp = ~c_chFont1608[chChr][i];
        }

        for (j = 0; j < 8; j ++) {
            if (chTemp & 0x80) {
                this->drawPoint(chXpos, chYpos, 1);
            } else {
                this->drawPoint(chXpos, chYpos, 0);
            }
            chTemp <<= 1;
            chYpos ++;

            if ((chYpos - chYpos0) == chSize) {
                chYpos = chYpos0;
                chXpos ++;
                break;
            }
        }
    }
}
void Oled::displayString(int chXpos, int chYpos, const char *pchString, int chSize, int chMode)
{
    while (*pchString != '\0') {
        if (chXpos > (128 - chSize / 2)) {
            chXpos = 0;
            chYpos += chSize;
            if (chYpos > (64 - chSize)) {
                chYpos = chXpos = 0;
                memset(this->buf,'\0',128*8);
            }
        }
        displayChar(chXpos, chYpos, *pchString, chSize, chMode);
        chXpos += chSize / 2;
        pchString ++;
    }
}
void Oled::draw()
{
    setMode(MODE_POINT);
    if(fd>0){
        write(fd,this->buf,1024);
    }
   
}
void Oled::setString(const char* str,int x,int y)
{ 
    displayString(x,y,str,16,1);
}
void Oled::setFigure(const unsigned char *bu,int width,int height,int x,int y)
{
    unsigned char temp,temp2;
    int h = height/8;
    int y_pos,mod;
    for(int i=0;i<width;i++)
    {
        y_pos = y;
        for(int j=0;j<h;j++)
        {
            temp = bu[i*h+j];
            for(int k=0;k<8;k++)
            {
                mod = y_pos%8;
                temp2 = 1<<mod;
                if(temp&1<<k)
                {                
                    this->buf[x][y_pos/8]|= temp2;
                }
                else
                {
                    this->buf[x][y_pos/8]&= ~temp2;
                }
                y_pos++;
                if(y_pos>63)
                    break;
            }
        }
        x++;
        if(x>127) break;
    }
}
//初始化
void Oled::init()
{
    ioctl(this->fd,CMD_INIT,0);
}
//休眠
void Oled::sleep()
{
    ioctl(this->fd,CMD_SLEEP,0);
}
//唤醒
void Oled::awake()
{
    ioctl(this->fd,CMD_AWAKE,0);
}
void Oled::setMode(enum MODE m)
{
    ioctl(this->fd,CMD_SET_MODE,m);
}
void Oled::refresh()
{
    ioctl(this->fd,CMD_REFRESH_GRAM,0);
}
Oled::~Oled()
{
    //pthread_mutex_destroy(&lock); 
    status = false;
    close(fd);
}