package servlet;

import bean.DeviceInfo;
import bean.DeviceModelDownloadInfo;
import com.google.gson.Gson;
import dao.*;
import util.FormdataParse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmitDownloadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        URL real = AddModelServlet.class.getResource("/");
        Writer writer = new FileWriter(real.getPath() + "../../resource/log/downloadLog",true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":SubmitDownloadTask");
        DeviceModelDownloadInfoDao dd = new DeviceModelDownloadInfoDaoImpl();
        DeviceModelDownloadInfo d = new DeviceModelDownloadInfo();
        d.setSubmitDate(new Date());
        d.setUserId(0);
        FormdataParse formdataParse = new FormdataParse(req.getInputStream());
        formdataParse.Parse();
        Integer id = Integer.parseInt(formdataParse.names_values.get("modelInfoId"));
        d.setModelOverId(id);
        d.setStatus("No");
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
