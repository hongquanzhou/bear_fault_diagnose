ifneq ($(KERNELRELEASE),)
obj-m := ssd1306_device.o
obj-m += ssd1306_driver.o
else
KDIR := /mnt/Kernel/linux-4.4.154
all:
	$(MAKE) -C $(KDIR) M=$(PWD) modules
clean:
	rm -rf .*.cmd *.o *.mod.c .tmp_versions
endif

uninstall:
	rmmod ssd1306_device.ko
	rmmod ssd1306_driver.ko

install:
	insmod ssd1306_device.ko
	insmod ssd1306_driver.ko
