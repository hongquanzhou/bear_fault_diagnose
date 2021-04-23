package dao;

import bean.ModelName;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ModelNameDaoImpl implements ModelNameDao{
    @Override
    public ModelName findPathByName(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ModelName modelName = new ModelName();
        connection = JdbcUtils.getConnection();
        String sql = "SELECT * FROM t_model_name WHERE name=?";
        ps = connection.prepareStatement(sql);
        ps.setString(1,name);
        rs = ps.executeQuery();
        if(rs == null) return null;
        if(rs.next())
        {
            modelName.setName(name);
            modelName.setPath(rs.getString("path"));
            return modelName;
        }
        else
        {
            return null;
        }

    }

    @Override
    public void addModelName(ModelName modelName) {
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            connection = JdbcUtils.getConnection();
            String sql = "insert into t_model_name (name,path) values(?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1,modelName.getName());
            ps.setString(2,modelName.getPath());
            ps.executeUpdate();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteModelByName(String name) {
        //待完成
    }

    @Override
    public boolean updateModel(ModelName modelName) {
        return false;
    }

    @Override
    public List<ModelName> getAllModelName() {
        List<ModelName> modelNames = new LinkedList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = JdbcUtils.getConnection();
            String sql = "select * from t_model_name";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())
            {
                ModelName modelName = new ModelName();
                modelName.setName(rs.getString("name"));
                modelName.setPath(rs.getString("path"));
                if(!modelNames.add(modelName))
                {
                    System.out.println("erro");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return modelNames;
    }

    @Override
    public boolean hasName(String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            connection = JdbcUtils.getConnection();
            String sql = "select name from t_model_name where name=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,name);
            rs = ps.executeQuery();
            if(rs == null) return false;
            if(rs.next())
            {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
