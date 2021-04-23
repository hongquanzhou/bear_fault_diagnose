package servlet;

import bean.Mode;
import bean.Status;
import bean.Task;
import bean.TaskInfo;
import com.google.gson.Gson;
import service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControlTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":ControlTask");
        Gson gson = new Gson();
        Reader reader = req.getReader();
        String line = null;
        TaskInfo taskInfo = null;
        Mode mode = null;
        Status ret = null;
        TaskService taskService = new TaskService();
        line=((BufferedReader) reader).readLine();
        //System.out.println("task:"+ line);
        taskInfo = new Gson().fromJson(line,TaskInfo.class);
        line=((BufferedReader) reader).readLine();
        //System.out.println("mode:"+line);
        mode = new Gson().fromJson(line,Mode.class);
        taskService.control(taskInfo,mode);
        Writer writer = resp.getWriter();
        writer.write("success");
    }
}
