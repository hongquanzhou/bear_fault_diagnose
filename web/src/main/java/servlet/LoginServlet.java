package servlet;

import bean.User;
import redis.clients.jedis.*;
import service.UserException;
import service.UserService;
import util.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Properties;

public class LoginServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        System.out.println("Login!");
        /*
         * 获取表单数据，将用户名在数据库中查找
         * 1.成功，比较得到的密码和表单中的密码是否一样，一样则成功，不一样则返回密码错误信息
         * 2.失败，返回用户名错误的信息
         */
        User form=CommonUtils.toBean(request.getParameterMap(),User.class);
        UserService userService=new UserService();
        try {
            User user= userService.login(form);
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(expireTime);
            String SESSIONID = session.getId();
            jedis.setex(SESSIONID,expireTime,user.getUsername());
            System.out.println("redis add:" + SESSIONID +":"+user.getUsername());
            response.sendRedirect(request.getContextPath()+"/user/welcome.jsp");
        } catch (UserException e) {
            request.setAttribute("user",form);
            request.setAttribute("msg",new String(e.getMessage().getBytes("iso-8859-1"),"utf-8"));
            System.out.println("path:"+request.getContextPath());
            request.getRequestDispatcher( "/user/login.jsp").forward(request,response);
        }
    }
}
