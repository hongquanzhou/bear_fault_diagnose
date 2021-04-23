package servlet;

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

public class QueryTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QueryTask");
        Gson gson = new Gson();
        Reader reader = req.getReader();
        String line = null;
        TaskInfo taskInfo = null;
        Status ret = null;
        TaskService taskService = new TaskService();
        while((line=((BufferedReader) reader).readLine())!=null)
        {
            //System.out.println(line);
            taskInfo = parseMsg(line);
            //System.out.println(taskInfo);
        }
        ret = taskService.query(taskInfo);
        //System.out.println("QueryTaskServlet:"+ ret);
        Writer writer = resp.getWriter();
        writer.write(new Gson().toJson(ret));
    }
    private TaskInfo parseMsg(String msg)
    {
        return new Gson().fromJson(msg,TaskInfo.class);
    }
}
