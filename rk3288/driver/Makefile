all:
	insmod ssd1306_device.ko
	insmod ssd1306_driver.ko
	insmod ad7606.ko
cp:
	cp ./oled/ssd1306_device.ko ./
	cp ./oled/ssd1306_driver.ko ./
	cp ./sample/ad7606.ko ./

uninstall:
	rmmod ssd1306_device.ko
	rmmod ssd1306_driver.ko
	rmmod ad7606.ko
