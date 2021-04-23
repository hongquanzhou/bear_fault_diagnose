#include<iostream>
#include<string>
#include "head/Oled.h"
unsigned char buf1[1024];

int main()
{
    unsigned char buf1[1024];
    unsigned char buf2[32];
    unsigned char buf3[32];
    char c;
    Oled oled("/dev/ssd1306_oled");

    FILE* fp_logo = fopen("./pic/mece.bin","rb");
    fread(buf1,1,1024,fp_logo);
    fclose(fp_logo);

    FILE* fp_wifi_con = fopen("./pic/wifi_con.bin","rb");
    fread(buf2,1,32,fp_wifi_con);
    fclose(fp_wifi_con);

    FILE* fp_wifi_no_con = fopen("./pic/wifi_no_con.bin","rb");
    fread(buf3,1,32,fp_wifi_no_con);
    fclose(fp_wifi_no_con);


    oled.setFigure(buf1,128,64,0,0);
    oled.setFigure(buf2,16,16,96,48);
    oled.setFigure(buf3,16,16,112,48);
    std::cin>>c;
    oled.setString("");
    return 0;
}