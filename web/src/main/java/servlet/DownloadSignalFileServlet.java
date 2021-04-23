package servlet;

import bean.SignalEntity;
import bean.signal_path;
import com.google.gson.Gson;
import com.jmatio.io.MatFileReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DownloadSignalFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties prop = new Properties();
        prop.load(DownLoadSignalDataServlet.class.getClassLoader().getResourceAsStream("conf.properties"));
        String basePath = prop.getProperty("dataSavePath");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + ":DownloadSignalData");
        Reader reader = req.getReader();
        String line = ((BufferedReader) reader).readLine();
        String fileName = line.split("=")[1];
        File file = new File(basePath + fileName);
        FileInputStream fis = null;
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition","attachment;filename=" + fileName);
        resp.setContentLength((int) file.length());
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[128];
            int cnt = 0;
            int sum = 0;
            while ((cnt = fis.read(buf)) > 0) {
                resp.getOutputStream().write(buf, 0, cnt);
                sum += cnt;
            }
            System.out.println(sum);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
            fis.close();
        }
    }
}
