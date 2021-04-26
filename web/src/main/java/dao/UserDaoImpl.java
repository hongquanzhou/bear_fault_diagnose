package dao;
//xml存储信息
import bean.User;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import util.JdbcUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UserDaoImpl implements UserDao
{
    static Connection connection = null;
    static{
        try {
            connection = JdbcUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    String file="dom4j.xml";
    public User findByUsername(String username)
    {
        SAXReader reader=new SAXReader();
        try {
            Document document=reader.read(file);
            Element element= (Element) document.selectSingleNode("//user[@username='"+username+"']");
            if (element==null) return null;
            User user=new User();
            String uname=element.attributeValue("username");
            String password=element.attributeValue("password");
            user.setUsername(uname);
            user.setPassword(password);
            return user;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Vector<User> queryUser(User user) {
        Vector<User> ret = new Vector<User>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "select * from t_user where 1=1";
        if(user!=null)
        {
            if(user.getUsername()!=null)
            {
                sql += " and username=\""+user.getUsername()+"\"";
            }
            if(user.getPassword()!=null)
            {
                sql += " and password=\""+user.getPassword()+"\"";
            }
            if(user.getMail()!=null)
            {
                sql+=" and mail=\""+user.getMail() + "\"";
            }
            if(user.getTel()!=null)
            {
                sql+=" and tel=\""+user.getTel() + "\"";
            }
            if(user.getGender()!=null)
            {
                sql+=" and gender=\""+user.getGender() +"\"";
            }
            if(user.getAge()!=null)
            {
                sql+=" and age="+user.getAge();
            }
            if(user.getId()!=null)
            {
                sql+=" and id="+user.getId();
            }
        }
        try{
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())
            {
                User user1 = new User();
                user1.setUsername(rs.getString(1));
                user1.setPassword(rs.getString(2));
                user1.setAge(rs.getInt(3));
                user1.setGender(rs.getString(4));
                user1.setId(rs.getInt(5));
                user1.setTel(rs.getString(6));
                user1.setMail(rs.getString(7));
                ret.add(user1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    public void add(User user)
    {
        SAXReader reader=new SAXReader();
        XMLWriter writer=null;
        try
        {
            Document document=reader.read(file);
            Element rootElement=document.getRootElement();
            Element userElement =rootElement.addElement("user");
            userElement.addAttribute("username",user.getUsername());
            userElement.addAttribute("password",user.getPassword());

            OutputFormat format=new OutputFormat("\t",true);
            format.setTrimText(true);
            writer=new XMLWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"),format);
            writer.write(document);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(User user) throws SQLException {


    }

    public User getAllUserInfo(String username) {
        return null;
    }
}

