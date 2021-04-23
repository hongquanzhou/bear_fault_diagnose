package servlet;

import dao.ModelNameDao;
import dao.ModelNameDaoImpl;
import bean.ModelName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class QueryModelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QueryModel");

        List<ModelName> modelNames = null;
        ModelNameDao modelNameDao = new ModelNameDaoImpl();
        modelNames = modelNameDao.getAllModelName();
        Writer writer = resp.getWriter();
        Print(writer,modelNames);

    }
    private void Print(Writer writer,List<ModelName> modelNames)
    {
        Iterator<ModelName> it = modelNames.iterator();
        ModelName modelName= null;
        ((PrintWriter) writer).print("[");
        while(it.hasNext())
        {
            modelName = it.next();
            //((PrintWriter) writer).print("{"+"\""+modelName.getName()+"\""+":"+"\""+modelName.getPath()+"\""+"}");
            ((PrintWriter) writer).print("{\"name\":\""+modelName.getName()+"\",\"path\":\""+modelName.getPath()+"\"}");
            if(it.hasNext())
            {
                ((PrintWriter) writer).print(",");
            }
        }
        ((PrintWriter) writer).print("]");
    }
}
