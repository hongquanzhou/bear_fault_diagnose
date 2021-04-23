package main;

import bean.DeviceInfo;
import bean.DeviceModelDownloadInfo;
import bean.SignalEntity;
import com.google.gson.Gson;
import dao.DeviceModelDownloadInfoDao;
import dao.DeviceModelDownloadInfoDaoImpl;
import dao.SignalEntityDao;
import dao.SignalEntityDaoImp;
import util.SocketReadLine;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

public class DownLoadTaskService2 {
    public DownLoadTaskService2(Socket s) throws IOException {
        device = s;
    }
    private Socket device = null;
    SocketReadLine socketReadLine = null;
    BufferedOutputStream writer = null;
    static DeviceModelDownloadInfoDao deviceModelDownloadInfoDao = null;
    static {
        deviceModelDownloadInfoDao = new DeviceModelDownloadInfoDaoImpl();
    }
    public static int times = 5;
    private int transmit(String s) throws IOException {
        System.out.println("send:"+s);
        boolean flag = true;
        String recv;
        for(int i=0;i<times&&flag;i++)
        {

            writer.write((s+"\n").getBytes());
            writer.flush();
            System.out.println("sended:"+ s);

            recv = socketReadLine.readLine();
            System.out.println("recved:"+ recv);

            if(s.equals(recv))
            {
                flag = false;
                writer.write("yes\n".getBytes());
                writer.flush();
                System.out.println("sended ACK:yes");
            }
            else
            {
                writer.write("no\n".getBytes());
                writer.flush();
                System.out.println("sended ACK:no");
            }
        }
        System.out.println("over\n");
        if(flag==false)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
    private int receive(char[] s) throws IOException {
        Arrays.fill(s,'\0');
        String recv = null;
        boolean flag = true;
        for(int i=0;i<times&&flag;i++)
        {

            recv = socketReadLine.readLine();
            System.out.println("received:"+recv);
            writer.write((recv+"\n").getBytes());
            writer.flush();
            System.out.println("sended:"+recv);
            for(int j=0;j<recv.length();j++)
            {
                s[j] = recv.charAt(j);
            }
            s[recv.length()] = '\0';
            recv = socketReadLine.readLine();
            System.out.println("received ACK:"+ recv);
            if(recv.equals("yes"))
            {
                flag = false;
            }
        }
        System.out.println("over\n");
        if(flag == false)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
    String array2Str(char[] buf)
    {
        StringBuilder ret = new StringBuilder();
        int i=0;
        while(buf[i]!='\0')
        {
            ret.append(buf[i]);
            i++;
        }
        return  ret.toString();
    }
    //第一步，和device完成配对，鉴权
    public boolean Pair()
    {
        return true;
    }
    public void process() throws IOException, SQLException {
        Properties prop = new Properties();
        prop.load(DownLoadTaskService.class.getClassLoader().getResourceAsStream("conf.properties"));
        String modelSavePath = prop.getProperty("modelSavePath");
        String dataSavePath = prop.getProperty("dataSavePath");
        BufferedInputStream bin = new BufferedInputStream(device.getInputStream());
        socketReadLine = new SocketReadLine(bin);
        BufferedOutputStream bout = new BufferedOutputStream(device.getOutputStream());
        writer = bout;
        System.out.println("Pair stage");
        if(Pair()!=true){ return ; }
        System.out.println("get info stage");
        String line;
        char[] buf = new char[128];
        DeviceInfo deviceInfo = null;
        if(transmit("give me your info")!=0)
        {
            device.close();
            return ;
        }
        if(receive(buf)!=0)
        {
            device.close();
            return ;
        }
        line = array2Str(buf);
        deviceInfo = new Gson().fromJson(line, DeviceInfo.class);
        DeviceModelDownloadInfo modelDownloadInfo = new DeviceModelDownloadInfo();
        modelDownloadInfo.setDeviceId(deviceInfo.getId());
        modelDownloadInfo.setStatus("No");
        Vector<DeviceModelDownloadInfo> rs = deviceModelDownloadInfoDao.queryTask(modelDownloadInfo);
        System.out.println("download model stage");
        if(rs.size()==0)
        {
            if(transmit("no model to download")!=0)
            {
                device.close();
                return ;
            }
        }
        else
        {
            if(transmit("you have a model to download")!=0)
            {
                device.close();
                return ;
            }
            if(receive(buf)!=0)
            {
                device.close();
                return ;
            }
            line = array2Str(buf);
            System.out.println(line);
            Integer modelId = rs.elementAt(0).getModelOverId();
            //传输model
            //获取模型路径
            String filename = "CNN_quanti.tflite";
            rs.elementAt(0);
            String path = modelSavePath + filename;
            //发送文件名
            if(transmit(filename)!=0)
            {
                device.close();
                return ;
            }
            //发送文件大小
            Integer fileSize =(int) new File(path).length();
            System.out.println("filesize:"+fileSize);
            if(transmit(fileSize.toString())!=0)
            {
                device.close();
                return ;
            }
            //发送模型文件
            BufferedInputStream bin1 = new BufferedInputStream(new FileInputStream(new File(path)));
            BufferedOutputStream socketout = new BufferedOutputStream(device.getOutputStream());
            Integer len;
            Integer sum = 0;
            byte[] buf1 = new byte[128];
            while((len = bin1.read(buf1,0,128))!=-1)
            {
                socketout.write(buf1,0,len);
                sum += len;
            }
            System.out.println("sum:"+sum);
            socketout.flush();
            //改数据库 状态已下载
            rs.elementAt(0).setDownloadDate(new Date());
            rs.elementAt(0).setStatus("Yes");
            deviceModelDownloadInfoDao.updateTask(rs.elementAt(0));
        }
        //signal upload stage
        System.out.println("signal upload stage");
        if(transmit("Do you have signal to save")!=0)
        {
            device.close();
            return ;
        }

        if(receive(buf)!=0)
        {
            device.close();
            return ;
        }
        line = array2Str(buf);
//        line = "YES";
        System.out.println("line:"+line);
        if(line.equals("YES"))
        {
            //传输signal fileNum
            System.out.println("start save data");
            if(receive(buf)!=0)
            {
                device.close();
                return ;
            }
            Integer fileNum = Integer.parseInt(array2Str(buf));
            //System.out.println("fileNum:"+fileNum);
            for(int i=0;i<fileNum;i++)
            {
                if(receive(buf)!=0)
                {
                    device.close();
                    return ;
                }
                String fileName = array2Str(buf);
                System.out.println("fileName:"+fileName);
                if(receive(buf)!=0)
                {
                    device.close();
                    return ;
                }
                String fileSizes = array2Str(buf);
                Integer fileSize = Integer.parseInt(fileSizes);
                System.out.println("fileSize:"+fileSize);
                BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(new File(dataSavePath + fileName)));
                byte[] buf1 = new byte[128];
                while(fileSize>0)
                {
                    Integer recvlen = Math.min(128,fileSize);
                    socketReadLine.read(buf1,recvlen);
                    fout.write(buf1,0,recvlen);
                    fileSize = fileSize - recvlen;
                }
                fout.close();
                //改数据库
                SignalEntity signalEntity = new SignalEntity();
                signalEntity.setTrainNumber(deviceInfo.getTrainNumber());
                signalEntity.setVehicleNumber(deviceInfo.getVehicleNumber());
                signalEntity.setBogie(deviceInfo.getBogie());
                signalEntity.setDate(new java.sql.Date(parse(fileName).getTime()));
                signalEntity.setAxle(0);
                signalEntity.setPathOfVibrate(fileName);
                SignalEntityDao signalEntityDao = new SignalEntityDaoImp();
                signalEntityDao.addSingnalEntity(signalEntity);
            }
            System.out.println("signal upload over");
        }
        if(transmit("byebye")!=0)
        {
            device.close();
            return;
        }
        bout.close();
        bin.close();
        device.close();
        System.out.println("over");
    }

    private Date parse(String fileName)
    {
        String[] date = fileName.split("_");
        int year = Integer.parseInt(date[0]) - 1900;
        int month = Integer.parseInt(date[1]) - 1;
        int day = Integer.parseInt(date[2]);
        int hour = Integer.parseInt(date[3]);
        int min = Integer.parseInt(date[4]);
        int sec = Integer.parseInt(date[5]);
        Date ret = new Date(year,month,day,hour,min,sec);
        return  ret;
    }
    public boolean SubmitTask(DeviceModelDownloadInfo d) throws SQLException {
        return deviceModelDownloadInfoDao.addTask(d);
    }
    public  boolean DeleteTask(DeviceModelDownloadInfo d) throws SQLException {
        return deviceModelDownloadInfoDao.deleteTask(d);
    }
    public boolean updateTask(DeviceModelDownloadInfo d) throws SQLException {
        return deviceModelDownloadInfoDao.updateTask(d);
    }
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("downloadServer start");
        ServerSocket serverSocket = new ServerSocket(1080);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket server = null;
                try{
                    server = new ServerSocket(1081);
                    while(true)
                    {
                        Socket s = server.accept();
                        s.close();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
        Socket s = null;
        while(true)
        {
            s = serverSocket.accept();
            DownLoadTaskService2 dt = new DownLoadTaskService2(s);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dt.process();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

