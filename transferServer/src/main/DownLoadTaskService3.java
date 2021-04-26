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
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

public class DownLoadTaskService3 {
    public DownLoadTaskService3(Socket s)
    {
        device = s;
    }
    private Socket device = null;
    static DeviceModelDownloadInfoDao deviceModelDownloadInfoDao = null;
    static {
        deviceModelDownloadInfoDao = new DeviceModelDownloadInfoDaoImpl();
    }
    //第一步，和device完成配对，鉴权
    public boolean Pair()
    {
        return true;
    }
    public void process() throws IOException, SQLException {
        Properties prop = new Properties();
        prop.load(DownLoadTaskService3.class.getClassLoader().getResourceAsStream("conf.properties"));
        String modelSavePath = prop.getProperty("modelSavePath");
        String dataSavePath = prop.getProperty("dataSavePath");
        BufferedInputStream bin1 = new BufferedInputStream(device.getInputStream());
        SocketReadLine socketReadLine = new SocketReadLine(bin1);
        BufferedOutputStream bout = new BufferedOutputStream(device.getOutputStream());
        System.out.println("Pair stage");
        if(Pair()!=true){ return ; }
        System.out.println("get info stage");
        String line = null;
        DeviceInfo deviceInfo = null;
        bout.write("give me your info\n".getBytes());
        bout.flush();
        byte[] buf = new byte[128];
        line =  socketReadLine.readLine();
        deviceInfo = new Gson().fromJson(line, DeviceInfo.class);
        DeviceModelDownloadInfo modelDownloadInfo = new DeviceModelDownloadInfo();
        modelDownloadInfo.setDeviceId(deviceInfo.getId());
        modelDownloadInfo.setStatus("No");
        Vector<DeviceModelDownloadInfo> rs = deviceModelDownloadInfoDao.queryTask(modelDownloadInfo);
        System.out.println("download model stage");
        if(rs.size()==0)
        {
            bout.write("no model to download\n".getBytes());
            bout.flush();
        }
        else
        {
            line = "";
            bout.write("you have a model to download\n".getBytes());
            bout.flush();
            socketReadLine.readLine();
            line = new String(buf);
            System.out.println(line);
            Integer modelId = rs.elementAt(0).getModelOverId();
            //传输model
            //获取模型路径
            String filename = "CNN_quanti.tflite";
            rs.elementAt(0);
            String path = modelSavePath + filename;
            //发送文件名
            bout.write((filename+"\n").getBytes());
            bout.flush();
            //发送文件大小
            Integer fileSize =(int) new File(path).length();
            System.out.println("filesize:"+fileSize);
            bout.write(( fileSize +"\n").getBytes());
            bout.flush();
            //发送模型文件
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(path)));
            BufferedOutputStream socketout = new BufferedOutputStream(device.getOutputStream());
            Integer len;
            Integer sum = 0;
            while((len = bin.read(buf,0,128))!=-1)
            {
                socketout.write(buf,0,len);
                sum += len;
            }
            System.out.println("sum:"+sum);
            socketout.flush();
            //改数据库 状态已下载
            rs.elementAt(0).setDownloadDate(new Timestamp(new Date().getTime()));
            rs.elementAt(0).setStatus("Yes");
            deviceModelDownloadInfoDao.updateTask(rs.elementAt(0));
        }
        //signal upload stage
        System.out.println("signal upload stage");
        bout.write("Do you have signal to save\n".getBytes());
        bout.flush();
        line = socketReadLine.readLine();
        if(line.equals("yes"))
        {
            //传输signal
            System.out.println("start save data");
            BufferedInputStream sin = new BufferedInputStream(device.getInputStream());
            line = socketReadLine.readLine();
            Integer fileNum = Integer.parseInt(line);
            //System.out.println("fileNum:"+fileNum);
            for(int i=0;i<fileNum;i++)
            {
                String fileName = socketReadLine.readLine();
                System.out.println("fileName:"+fileName);
                String fileSizes = socketReadLine.readLine();
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
        bout.write("bye\n".getBytes());
        bout.flush();
        bout.close();
        bin1.close();
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
}
