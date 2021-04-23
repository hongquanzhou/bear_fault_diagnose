package servlet;

import bean.DeviceInfo;
import bean.DeviceInfo;
import dao.DeviceInfoDao;
import dao.DeviceInfoDaoImpl;
import util.DeviceInfoParse;

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

import static jdk.nashorn.internal.objects.Global.print;

public class QueryDeviceInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QueryDeviceInfo");
        DeviceInfo deviceInfo = new DeviceInfoParse().parse(req.getInputStream());

        System.out.println(deviceInfo);

        DeviceInfoDao deviceInfoDao = new DeviceInfoDaoImpl();
        List<DeviceInfo> deviceInfoList = deviceInfoDao.getAllDeviceInfoByCondition(deviceInfo);
        
        processRes(resp.getWriter(),deviceInfoList);
        
        
    }
    private boolean processRes(Writer writer,List<DeviceInfo> deviceInfoList)
    {
        Iterator<DeviceInfo> it = deviceInfoList.iterator();
        DeviceInfo deviceInfo = null;
        ((PrintWriter) writer).print("[");
        while(it.hasNext())
        {
            deviceInfo = it.next();
            ((PrintWriter) writer).print("{\"trainNumber\":\""+deviceInfo.getTrainNumber()
                    +"\",\"vehicleNumber\":\""+deviceInfo.getVehicleNumber()+"\",\"bogie\":\""+deviceInfo.getBogie()+"\",\"version\":\""
                    +deviceInfo.getVersion()+"\",\"status\":\""+deviceInfo.getStatusOfDevice()+"\"}");
            if(it.hasNext())
            {
                ((PrintWriter) writer).print(",");
            }
        }
        ((PrintWriter) writer).print("]");
        return true;
    }
}
