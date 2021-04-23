package servlet;


import bean.TaskInfo;
import com.google.gson.Gson;
import dao.JdbcTaskInfoDaoImpl;
import dao.TaskInfoDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckTaskNameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":CheckTaskName");
        String line = req.getReader().readLine();
        TaskInfo taskInfo = new Gson().fromJson(line,TaskInfo.class);
        TaskInfoDao taskInfoDao = new JdbcTaskInfoDaoImpl();
        try {
            Writer writer = resp.getWriter();
            if(taskInfoDao.query(taskInfo).size()==0)
            {
                writer.append("ok");
            }
            else
            {
                writer.append("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
