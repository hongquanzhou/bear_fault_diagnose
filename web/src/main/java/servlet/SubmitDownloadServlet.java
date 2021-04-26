package servlet;

import bean.DeviceInfo;
import bean.DeviceModelDownloadInfo;
import bean.TaskInfo;
import bean.User;
import com.google.gson.Gson;
import dao.*;
import redis.clients.jedis.Jedis;
import util.FormdataParse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

public class SubmitDownloadServlet extends HttpServlet {
    private static Jedis jedis = null;
    private static Integer expireTime;
    static {
        Properties prop = new Properties();
        try {
            prop.load(GetUserNameServlet.class.getClassLoader().getResourceAsStream("conf.properties"));
            String host = prop.getProperty("JedisIp");
            String port = prop.getProperty("JedisPort");
            String auth = prop.getProperty("JedisAuth");
            expireTime = Integer.parseInt(prop.getProperty("JedisExpire"));
            jedis = new Jedis(host,Integer.parseInt(port));
            jedis.auth(auth);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        URL real = AddModelServlet.class.getResource("/");
        Writer writer = new FileWriter(real.getPath() + "../../resource/log/downloadLog",true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":SubmitDownloadTask");
        DeviceModelDownloadInfoDao dd = new DeviceModelDownloadInfoDaoImpl();
        DeviceModelDownloadInfo d = new DeviceModelDownloadInfo();
        TaskInfoDao taskInfoDao = new JdbcTaskInfoDaoImpl();
        TaskInfo taskInfo = new TaskInfo();
        d.setSubmitDate(new Timestamp(new Date().getTime()));
        // 准备userID
        HttpSession session = req.getSession();
        String userName = jedis.get(session.getId());
        jedis.expire(session.getId(),expireTime);
        User user1 = new User();
        user1.setUsername(userName);
        UserDao userDao = new UserDaoImpl();
        user1 = userDao.queryUser(user1).elementAt(0);
        d.setUserId(user1.getId());
        FormdataParse formdataParse = new FormdataParse(req.getInputStream());
        formdataParse.Parse();
        Integer id = Integer.parseInt(formdataParse.names_values.get("modelInfoId"));
        d.setModelOverId(id);
        d.setStatus("No");
        // 获取TaskName
        Integer num = Integer.parseInt(formdataParse.names_values.get("deviceInfoNum"));
        for(int i=0;i<num;i++)
        {
            String line = formdataParse.names_values.get("deviceInfo"+i);
            String[] lines = line.split(" ");
            DeviceInfoDao deviceInfoDao = new DeviceInfoDaoImpl();
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setTrainNumber(lines[0]);
            deviceInfo.setVehicleNumber(Integer.parseInt(lines[1]));
            deviceInfo.setBogie(Integer.parseInt(lines[2]));

            try {
                deviceInfo = deviceInfoDao.query(deviceInfo).elementAt(0);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                taskInfo.setId(id);
                taskInfo = taskInfoDao.query(taskInfo).elementAt(0);
                System.out.println(taskInfo);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            d.setTaskName(taskInfo.getTaskName());
            d.setDeviceId(deviceInfo.getId());
            try {
                dd.addTask(d);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            writer.append(new Date() + " submit " + d + "\n");
            writer.close();
        }
        resp.getWriter().write("submit success");
    }
}
