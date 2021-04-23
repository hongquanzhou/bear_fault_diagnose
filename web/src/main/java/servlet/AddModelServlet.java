package servlet;

import dao.ModelNameDao;
import dao.ModelNameDaoImpl;
import bean.ModelName;
import util.FormdataParse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddModelServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        try {
            URL real = AddModelServlet.class.getResource("/");
            String realPath = real.getPath();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(new Date())+":AddModel");
            //start parse
            FormdataParse formdataParse = new FormdataParse(request.getInputStream());
            formdataParse.Parse();
            //Parse Over!Start add file!
            String name = formdataParse.names_values.get("name");
            String path = realPath + "../../resource/model/";
            BufferedWriter bw = new BufferedWriter(new PrintWriter(path + name + ".svg"));
            bw.write(formdataParse.names_values.get("svg"));
            bw.close();
            BufferedWriter bw2 = new BufferedWriter(new PrintWriter(path + name));

            //Start add ModelName to db
            bw2.write(formdataParse.names_values.get("Network"));
            bw2.close();
            ModelNameDao modelNameDao = new ModelNameDaoImpl();
            ModelName modelName = new ModelName();
            modelName.setName(name);
            modelName.setPath("../resource/model/" + name+".svg");
            modelNameDao.addModelName(modelName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
