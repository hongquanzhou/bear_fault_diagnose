package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignalShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + ":SignalShow");
        Print(req.getInputStream());
        req.getRequestDispatcher("../signal_show.jsp");
    }
    private void Print(InputStream in) throws IOException {
        System.out.println("Parse");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while((line=br.readLine())!=null)
        {
            System.out.println(line);
        }


    }

}
