package dao;


import bean.Data;
import bean.DataSource;
import bean.ModelName;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JdbcDataSourceImpl implements DataSourceDao {
    static Connection connection = null;
    static {
        try {
            connection = JdbcUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
    }
    @Override
    public void add(DataSource dataSource) {

    }

    @Override
    public void remove(DataSource dataSource) {

    }

    @Override
    public void update(DataSource dataSource) {

    }

    @Override
    public List<DataSource> Query(DataSource dataSource) throws SQLException {
        List<DataSource> ret = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_data_source WHERE 1=1";
        if(dataSource!=null)
        {
            if(dataSource.getName()!=null)
            {
                sql += " and name=\""+ dataSource.getName()+"\"";
            }
            if(dataSource.getPath()!=null)
            {
                sql += " and path=\""+ dataSource.getPath()+"\"";
            }
        }
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next())
        {
            DataSource  dataSource1 = new DataSource();
            dataSource1.setName(rs.getString(1));
            dataSource1.setPath(rs.getString(2));
            ret.add(dataSource1);
        }
        return ret;
    }
}
