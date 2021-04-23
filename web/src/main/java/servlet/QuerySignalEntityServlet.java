package servlet;

import bean.SignalEntity;
import com.google.gson.Gson;
import dao.SignalEntityDao;
import dao.SignalEntityDaoImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuerySignalEntityServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QurtySignalEntity");

        List<SignalEntity> signalEntities = null;
        Reader reader = req.getReader();
        String line = ((BufferedReader) reader).readLine();
        System.out.println(line);
        SignalEntity signalEntity = new Gson().fromJson(line,SignalEntity.class);
        signalEntity.trans();
        System.out.println(signalEntity);
        SignalEntityDao signalEntityDao = new SignalEntityDaoImp();
        signalEntities = signalEntityDao.QuerySignalEntity(signalEntity);
        processRes(resp.getWriter(),signalEntities);

    }


    private boolean processRes(Writer writer,List<SignalEntity> signalEntities)
    {
        Iterator<SignalEntity> it = signalEntities.iterator();
        SignalEntity signalEntity = null;
        ((PrintWriter) writer).print("[");
        while(it.hasNext())
        {
            signalEntity = it.next();
            ((PrintWriter) writer).print("{\"id\":\""+signalEntity.getId()+"\",\"data\":\""+ signalEntity.getDate()+"\",\"trainNumber\":\""+signalEntity.getTrainNumber()
                    +"\",\"vehicleNumber\":\""+signalEntity.getVehicleNumber()+"\",\"bogie\":\""+signalEntity.getBogie()+"\",\"axle\":\""+signalEntity.getAxle()
                    +"\",\"pathOfVibrate\":\""+signalEntity.getPathOfVibrate()+"\"}");
            if(it.hasNext())
            {
                ((PrintWriter) writer).print(",");
            }
        }
        ((PrintWriter) writer).print("]");
        return true;
    }
}
