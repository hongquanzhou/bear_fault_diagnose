#include<stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main()
{
	char buf[100];
	int fd = open("/dev/ssd1306_oled",O_RDWR);
	while(1)
	{
		gets(buf);
		write(fd,buf,strlen(buf));
	}
	close(fd);
	return 0;
}
