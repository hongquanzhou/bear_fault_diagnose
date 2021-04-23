package service;

import bean.*;
import com.google.gson.Gson;
import dao.JdbcTaskInfoDaoImpl;
import dao.TaskDao;
import dao.TaskInfoDao;

import javax.jws.WebParam;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Vector;

public class TaskService {
    static TaskInfoDao taskInfoDao = new JdbcTaskInfoDaoImpl();
    static TaskServer taskServer = null;
    public static void connect(){
        System.out.println("connect");
        Properties prop = new Properties();
        try {
            prop.load(TaskServer.class.getClassLoader().getResourceAsStream("conf.properties"));
            String IP = prop.getProperty("TaskServerIP");
            String port = prop.getProperty("TaskServerPort");
            taskServer = new TaskServer(IP,Integer.parseInt(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void submit(TaskInfo taskInfo){
        if(taskServer==null) connect();
        try {
            taskInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            taskInfoDao.add(taskInfo);
            Msg msg = new Msg();
            msg.No = "submit";
            msg.model = taskInfo.getModelName();
            msg.data = taskInfo.getSourceName();
            msg.parameter = taskInfo.getPreParameter();
            msg.createTime = taskInfo.getCreateTime();
            taskInfo.setCreateTime(null);
            msg.taskId = taskInfoDao.query(taskInfo).elementAt(0).getId();
            taskServer.send(new Gson().toJson(msg));
        }
        catch (IOException e)
        {
            taskServer = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public Status query(TaskInfo taskInfo) {
        if (taskServer == null) connect();
        try {
            Vector<TaskInfo> taskInfos = taskInfoDao.query(taskInfo);
            if (taskInfos.size() < 0) return null;
            TaskInfo taskInfo1 = taskInfos.elementAt(0);
            String str = "";
            //System.out.println(taskInfo1);
            str += "{" + "state:\"" + taskInfo1.getStatus() + "\",process:" + taskInfo1.getProcess() + "}";
            return new Gson().fromJson(str, Status.class);
        } catch (SQLException e) {
            return null;
        }
    }
    public void control(TaskInfo taskInfo, Mode mode) {
        if(taskServer==null) connect();
        try{
            Msg msg = new Msg();
            msg.No = "control";
            msg.createTime = taskInfoDao.query(taskInfo).elementAt(0).getCreateTime();
            msg.mode = mode;
            msg.taskId = taskInfoDao.query(taskInfo).elementAt(0).getId();
            taskServer.send(new Gson().toJson(msg));
        }
        catch (IOException e)
        {
            taskServer = null;
        }
        catch (SQLException e)
        {

        }
    }
    public String getData(Integer id,Integer pos) throws IOException, SQLException {
        if(taskServer==null) connect();
        try{
            Msg msg = new Msg();
            msg.No = "GetData";
            msg.taskId = id;
            msg.pos = pos;
            return taskServer.getData(new Gson().toJson(msg));
        }
        catch (IOException e)
        {
            taskServer = null;
            return null;
        }
    }
}
