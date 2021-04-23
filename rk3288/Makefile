all.o:all.cpp
	g++ --std=c++11  -I /mnt/tensorflow2.0/tensorflow -I /mnt/tensorflow2.0/tensorflow/tensorflow/lite/tools/make/downloads/flatbuffers/include  -c all.cpp -o all.o

Oled.o:Oled.cpp
	g++ -c Oled.cpp

Infer.o:Infer.cpp
	g++ --std=c++11  -I /mnt/tensorflow2.0/tensorflow -I /mnt/tensorflow2.0/tensorflow/tensorflow/lite/tools/make/downloads/flatbuffers/include  -c Infer.cpp

Properties.o:Properties.cpp
	g++ -c Properties.cpp

Sampler.o:Sampler.cpp
	g++ -c    Sampler.cpp

Transfer2.o:Transfer2.cpp
	g++ -I /mnt/tensorflow2.0/tensorflow -I /mnt/tensorflow2.0/tensorflow/tensorflow/lite/tools/make/downloads/flatbuffers/include -I /mnt/Kernel/firefly-rk3288_kernel/include  -c Transfer2.cpp

utils.o:utils.cpp
	g++ -c utils.cpp

Wireless.o:Wireless.cpp
	g++ -c Wireless.cpp

all:Oled.o  Properties.o Sampler.o utils.o Transfer2.o all.o  Infer.o Wireless.o
	g++ --std=c++11 -march=armv7-a -mfpu=neon-vfpv4 -funsafe-math-optimizations -ftree-vectorize -o all  all.o Infer.o /usr/lib/tensorflow-lite/rpi_armv7l/obj/tensorflow/lite/tools/evaluation/utils.o \
	 /usr/lib/tensorflow-lite/rpi_armv7l/obj/tensorflow/lite/tools/command_line_flags.o \
	 /usr/lib/tensorflow-lite/rpi_armv7l/obj/tensorflow/lite/tools/tool_params.o \
	 /usr/lib/tensorflow-lite/libtensorflow-lite.a -Wl,--no-export-dynamic -Wl,--exclude-libs,ALL -Wl,--gc-sections -Wl,--as-needed \
	 -latomic -lstdc++ -lpthread -lm -ldl \
	 Oled.o   Properties.o Sampler.o  Transfer2.o utils.o Wireless.o

clean:
	rm *.o