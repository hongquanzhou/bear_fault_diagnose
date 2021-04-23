package servlet;

import bean.TrainBearHealthInfo;
import bean.TrainBearHealthInfo;
import dao.TrainBearHealthInfoDaoImpl;
import util.TrainBearInfoParse;

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

public class QueryTrainBearHealthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QueryTrainBearInfo");
        TrainBearHealthInfo trainBearHealthInfo = new TrainBearInfoParse().parse(req.getInputStream());

        System.out.println(trainBearHealthInfo);

        List<TrainBearHealthInfo> trainBearHealthInfos = new TrainBearHealthInfoDaoImpl().getAllDeviceInfoByCondition(trainBearHealthInfo);
        processRes(resp.getWriter(),trainBearHealthInfos);
    }
    private boolean processRes(Writer writer, List<TrainBearHealthInfo> trainBearHealthInfos)
    {
        Iterator<TrainBearHealthInfo> it = trainBearHealthInfos.iterator();
        TrainBearHealthInfo trainBearHealthInfo = null;
        ((PrintWriter) writer).print("[");
        while(it.hasNext())
        {
            trainBearHealthInfo = it.next();
            ((PrintWriter) writer).print("{\"trainNumber\":\""+trainBearHealthInfo.getTrainNumber()
                    +"\",\"vehicleNumber\":\""+trainBearHealthInfo.getVehicleNumber()+"\",\"bogie\":\""+trainBearHealthInfo.getBogie()+"\",\"axle\":\""
                    +trainBearHealthInfo.getAxle()+"\",\"vib\":\""+trainBearHealthInfo.getStatusOfVib()+"\",\"tem\":\""+trainBearHealthInfo.getStatusOfTem()+"\"}");
            if(it.hasNext())
            {
                ((PrintWriter) writer).print(",");
            }
        }
        ((PrintWriter) writer).print("]");
        return true;
    }
}
