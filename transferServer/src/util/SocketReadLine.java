package util;

import java.io.BufferedInputStream;
import java.io.IOException;

public class SocketReadLine {
    byte[] buf = new byte[128];
    BufferedInputStream bin = null;
    Integer pos1 = 0;
    Integer pos2 = 0;
    public SocketReadLine(BufferedInputStream bin) {
        this.bin = bin;
    }
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        Integer len = 0;
        while(true)
        {
            if(pos1 == pos2)
            {
                len = bin.read(buf,pos2,128- pos2);
                pos2 += len;
                if(pos2 == 128)
                {
                    pos2 = 0;
                }
            }
            else
            {
                if(pos2 < pos1)
                {
                    len = 128;
                }
                len += pos2 - pos1;
            }
            for(int i=0;i<len;i++)
            {
                char c = (char)buf[pos1];
                if(c == '\n')
                {
                    pos1 += 1;
                    String line = sb.toString();
                    if(pos1 ==128)
                    {
                        pos1 = 0;
                    }
                    return line;
                }
                else
                {
                    sb.append(c);
                }
                pos1 ++;
                if(pos1 ==128)
                {
                    pos1 = 0;
                }
            }
        }
    }
    public void read(byte[] ret,int length) throws IOException {
        Integer len = 0;
        Integer sum = 0;
        while (true) {
            if (pos1 == pos2) {
                len = bin.read(buf, pos2, 128 - pos2);
                pos2 += len;
                if (pos2 == 128) {
                    pos2 = 0;
                }
            } else {
                if (pos2 < pos1) {
                    len = 128;
                }
                len += pos2 - pos1;
            }
            for (int i = 0; i < len; i++) {
                ret[sum] = buf[pos1];
                pos1++;
                sum++;
                if (pos1 == 128) {
                    pos1 = 0;
                }
                if(sum==length)
                {
                    return;
                }
            }
        }
    }
}
