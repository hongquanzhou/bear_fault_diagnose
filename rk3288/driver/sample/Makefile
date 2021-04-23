ifneq ($(KERNELRELEASE),)
obj-m := ad7606.o
 
else
KDIR := /mnt/Kernel/firefly-rk3288_kernel
all:
	#rmmod ad7606
	$(MAKE) -C $(KDIR) M=$(PWD) modules
	#insmod ad7606.ko
	rm -rf .*.cmd *.o *.mod.c .tmp_versions  *.symvers *.order
clean:
	rm -rf .*.cmd *.o *.mod.c .tmp_versions  *.symvers *.order
endif


