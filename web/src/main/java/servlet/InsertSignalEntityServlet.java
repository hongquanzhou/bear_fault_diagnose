package servlet;

import bean.SignalEntity;
import com.google.gson.Gson;
import dao.SignalEntityDao;
import dao.SignalEntityDaoImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertSignalEntityServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":InsertSignalEntity");
        String line = req.getReader().readLine();
        SignalEntity signalEntity = new Gson().fromJson(line,SignalEntity.class);
        System.out.println("insert:"+signalEntity);
        SignalEntityDao signalEntityDao = new SignalEntityDaoImp();
        signalEntityDao.addSingnalEntity(signalEntity);
        resp.getWriter().print("ok!add success");
    }

}
