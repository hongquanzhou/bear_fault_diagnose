package servlet;

import bean.User;
import com.google.gson.Gson;
import dao.JdbcUserDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateUserInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + ":UpdateUserInfo");
        Reader reader = req.getReader();
        String line = ((BufferedReader) reader).readLine();
        User user = new Gson().fromJson(line, User.class);
        System.out.println(user);
        UserDao userDao = new JdbcUserDaoImpl();
        try {
            userDao.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Writer writer = resp.getWriter();
        writer.write("apply success");
    }
}
