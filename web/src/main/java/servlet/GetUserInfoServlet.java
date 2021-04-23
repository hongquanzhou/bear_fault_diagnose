package servlet;

import bean.User;
import com.google.gson.Gson;
import dao.JdbcUserDaoImpl;
import dao.UserDao;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class GetUserInfoServlet extends HttpServlet {
    private static Jedis jedis = null;
    private static Integer expireTime;
    static {
        Properties prop = new Properties();
        try {
            prop.load(LoginServlet.class.getClassLoader().getResourceAsStream("conf.properties"));
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + ":GetUserInfo");
        User user = null;
        String sessionID = req.getSession().getId();
        Writer writer = resp.getWriter();
        if(jedis.exists(sessionID))
        {
            String username = jedis.get(sessionID);
            UserDao userDao = new JdbcUserDaoImpl();
            user = userDao.getAllUserInfo(username);
            ((PrintWriter) writer).print(new Gson().toJson(user));
        }
        else
        {
            ((PrintWriter) writer).print("no");
        }
    }
}
