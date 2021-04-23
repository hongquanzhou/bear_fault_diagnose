package servlet;

import bean.Msg;
import com.google.gson.Gson;
import service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class GetCurveDataServlet  extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":GetCurveData");
        String line = req.getReader().readLine();
        Msg msg = new Gson().fromJson(line, Msg.class);
        TaskService taskService = new TaskService();
        try {
            line = taskService.getData(msg.taskId,msg.pos);
            resp.getWriter().write(line);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
