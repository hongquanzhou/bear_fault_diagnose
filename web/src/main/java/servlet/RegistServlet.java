package servlet;

import bean.User;
import service.UserException;
import service.UserService;
import util.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RegistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        System.out.println("Rigist!");
        UserService userService=new UserService();
        User form=CommonUtils.toBean(request.getParameterMap(),User.class);
        System.out.println(request.getSession().getAttribute("username"));
        Map<String,String> errors=new HashMap<>();
        String username=form.getUsername();
        String password=form.getPassword();
        String verifyCode=form.getVerifyCode();
        String sessionVerifyCode=(String) request.getSession().getAttribute("session_vcode");
        if (verifyCode==null||verifyCode.trim().isEmpty())
        {
            errors.put("verifyCode","verifyCode empty!");
        }else if (verifyCode.length()!=4)
        {
            errors.put("verifyCode","verifyCode should 4");
        }else if (!sessionVerifyCode.equalsIgnoreCase(verifyCode))
        {
            errors.put("verifyCode","verifyCode error");
        }

        if (errors!=null&&errors.size()>0)
        {
            request.setAttribute("errors",errors);
            request.setAttribute("user",form);
            request.getRequestDispatcher("/user/regist.jsp").forward(request,response);
            return;
        }

        System.out.println("form:"+form);
        try {
            userService.regist(form);
            response.getWriter().println("<h1>regist success!</h1><a href="+request.getContextPath()+"/user/login.jsp"+">click here to login!</a>");
        } catch (UserException e) {
            request.setAttribute("user",form);
            request.setAttribute("msg",e.getMessage());
            request.getRequestDispatcher("/user/regist.jsp").forward(request,response);
        }
    }


}
