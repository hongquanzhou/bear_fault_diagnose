package servlet;

import bean.SignalEntity;
import service.Signal_info;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SignalServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        Signal_info service = new Signal_info();
        List<SignalEntity> list = null;
        try {
            list = service.getallUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将查询出来的用户信息保存到session对象中
        HttpSession session = request.getSession();
        session.setAttribute("userlist", list);
        request.getRequestDispatcher("/user/signal_analysis.").forward(request,response);

    }
}
