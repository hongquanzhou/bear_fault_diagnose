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

public class DownLoadSignalDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties prop = new Properties();
        prop.load(DownLoadSignalDataServlet.class.getClassLoader().getResourceAsStream("conf.properties"));
        String basePath = prop.getProperty("dataSavePath");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + ":DownloadSignalData");
        String path = parse(req.getInputStream());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(basePath + path));
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
    private String parse(InputStream in) throws IOException {
        String ret = null;
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        String[] lines = new String[2];
        while((line=br.readLine()) != null)
        {
            lines[0]=line;
        }
        signal_path s = null;
        s = gson.fromJson(lines[0],signal_path.class);
        if(s != null) ret = s.getPath();
        System.out.println(ret);
        return ret;
    }
}
