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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmitTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":SubmitTask");
        Gson gson = new Gson();
        String line = null;
        TaskInfo taskInfo = null;
        while((line=req.getReader().readLine())!=null)
        {
            System.out.println("line:"+line);
            taskInfo = gson.fromJson(line,TaskInfo.class);
            System.out.println("taskinfo:"+taskInfo);
        }
        TaskService taskService = new TaskService();
        taskService.submit(taskInfo);
        System.out.println(taskInfo);
        resp.getWriter().write("ok");
    }
}
