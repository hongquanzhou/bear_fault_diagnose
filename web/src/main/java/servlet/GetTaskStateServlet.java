package servlet;

import bean.TrainState;
import com.google.gson.Gson;
import service.GetStateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTaskStateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":GetState");
        GetStateService getStateService = new GetStateService();
        TrainState state = getStateService.GetState();
        Writer writer = resp.getWriter();
        Gson gson = new Gson();
        String s = gson.toJson(state);
        ((PrintWriter) writer).print(s);
    }
}
