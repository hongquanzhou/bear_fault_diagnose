package servlet;

import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class GetUserNameServlet extends HttpServlet {
    private static Jedis jedis = null;
    private static Integer expireTime;
    static {
        Properties prop = new Properties();
        try {
            prop.load(GetUserNameServlet.class.getClassLoader().getResourceAsStream("conf.properties"));
            String host = prop.getProperty("JedisIp");
            String port = prop.getProperty("JedisPort");
            String auth = prop.getProperty("JedisAuth");
            expireTime = Integer.parseInt(prop.getProperty("JedisExpire"));
            jedis = new Jedis(host,Integer.parseInt(port));
            jedis.auth(auth);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(new Date()) + ":GetUserName");
            HttpSession session = req.getSession();
            Writer writer = resp.getWriter();
            if (session.isNew()) {
                ((PrintWriter) writer).print("no,relogin");
            } else {
                if(jedis.exists(session.getId()))
                {
                    String user = jedis.get(session.getId());
                    jedis.expire(session.getId(),expireTime);
                    ((PrintWriter) writer).print(user);
                }
                else
                {
                    ((PrintWriter) writer).print("no,relogin");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
