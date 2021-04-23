package dao;

import bean.User;
import util.JdbcUtils;

import java.sql.*;

public class JdbcUserDaoImpl implements UserDao
{

    @Override
    public User findByUsername(String username) {
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        User user=new User();
        try {
            connection = JdbcUtils.getConnection();
            String sql="SELECT * FROM t_user WHERE username=?";
            ps=connection.prepareStatement(sql);
            ps.setString(1,username);
            rs=ps.executeQuery();
            if (rs==null) return null;
            if (rs.next())
            {
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                return user;
            }else
            {
                return null;
            }

        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }finally {
            try
            {
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
                if (connection!=null) connection.close();
            }catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void add(User user) {
        Connection connection=null;
        PreparedStatement ps=null;
        System.out.println(user);
        try {
            connection = JdbcUtils.getConnection();
            String sql="insert into t_user (username,password) values(?,?)";
            ps=connection.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());
            ps.execute();
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }finally {
            try
            {
                if (ps!=null) ps.close();
                if (connection!=null) connection.close();
            }catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void update(User user) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = JdbcUtils.getConnection();
        String sql = "update t_user set";
        int flag = 0;
        if(user.getUsername()!=null)
        {
            flag = 1;
            sql+=" username=\""+ user.getUsername()+"\"";
        }
        if(user.getAge()!=null)
        {
            if(flag==1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" age="+ user.getAge();
        }
        if(user.getGender()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" gender=\""+user.getGender()+"\"";
        }
        if(user.getPassword()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" password=\""+user.getPassword()+"\"";
        }
        if(user.getTel()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" tel=\""+user.getTel() + "\"";
        }
        if(user.getMail()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" mail=\""+user.getMail()+"\"";
        }
        sql += " where id="+user.getId();
        ps = connection.prepareStatement(sql);
        ps.execute();
    }

    @Override
    public User getAllUserInfo(String username) {
        User user = new User();
        try {
            Connection connection = null;
            ResultSet rs = null;
            PreparedStatement ps = null;
            connection = JdbcUtils.getConnection();
            String sql = "select * from t_user where username = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            if(rs.next())
            {
                user.setUsername(rs.getString("username"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setTel(rs.getString("tel"));
                user.setMail(rs.getString("mail"));
                user.setId(rs.getInt("id"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }
}
