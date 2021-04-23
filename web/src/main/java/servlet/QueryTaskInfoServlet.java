package servlet;

import bean.TaskInfo;
import com.google.gson.Gson;
import dao.JdbcTaskInfoDaoImpl;
import dao.TaskInfoDao;
import service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryTaskInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QurtyTaskInfo");
        String line = null;
        TaskInfo taskInfo = null;
        TaskInfoDao taskInfoDao = new JdbcTaskInfoDaoImpl();
        while((line = req.getReader().readLine())!=null)
        {
            taskInfo = new Gson().fromJson(line,TaskInfo.class);
        }
       // System.out.println(taskInfo);
        Writer writer = resp.getWriter();
        try {
            writer.write(new Gson().toJson(taskInfoDao.query(taskInfo)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
