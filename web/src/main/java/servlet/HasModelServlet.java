package servlet;

import dao.ModelNameDao;
import dao.ModelNameDaoImpl;
import util.FormdataParse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HasModelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":HasModel");
        FormdataParse fm = new FormdataParse(req.getInputStream());
        fm.Parse();
        String name = fm.getByKey("name");
        ModelNameDao modelNameDao = new ModelNameDaoImpl();
        Writer writer = resp.getWriter();
        if(!modelNameDao.hasName(name))
        {
            writer.append("ok");
        }
        else
        {
            writer.append("no");
        }
    }
}
