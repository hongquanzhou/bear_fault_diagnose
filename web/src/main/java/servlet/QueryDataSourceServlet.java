package servlet;

import com.google.gson.Gson;
import dao.DataSourceDao;
import dao.JdbcDataSourceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryDataSourceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+":QueryDataSource");
        Writer writer = resp.getWriter();
        DataSourceDao dataSourceDao = new JdbcDataSourceImpl();
        try {
            writer.write(new Gson().toJson(dataSourceDao.Query(null)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
