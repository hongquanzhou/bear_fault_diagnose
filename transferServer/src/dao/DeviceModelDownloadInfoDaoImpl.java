package dao;

import bean.DeviceModelDownloadInfo;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DeviceModelDownloadInfoDaoImpl implements DeviceModelDownloadInfoDao{
    static Connection connection = null;
    static{
        try {
            connection = JdbcUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean addTask(DeviceModelDownloadInfo d) throws SQLException {
        PreparedStatement ps = null;
        String sql = "insert into t_device_download (device_id,model_over_id,task_name,submit_date,user_id) values(?,?,?,?,?)";
        ps = connection.prepareStatement(sql);
        if(d!=null)
        {
            ps.setInt(1,d.getDeviceId());
            ps.setInt(2,d.getModelOverId());
            ps.setString(3,d.getTaskName());
            ps.setTimestamp(4,new java.sql.Timestamp(d.getSubmitDate().getTime()));
            ps.setInt(5,d.getUserId());
        }
        return ps.execute();

    }
    public boolean deleteTask(DeviceModelDownloadInfo d) throws SQLException {
        PreparedStatement ps = null;
        String sql = "delete from t_device_download "+" where id="+ d.getId();
        ps = connection.prepareStatement(sql);
        return ps.execute();
    }
    public boolean updateTask(DeviceModelDownloadInfo d) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "update t_device_download set";
        int flag = 0;
        if(d.getDeviceId()!=null)
        {
            flag = 1;
            sql+=" device_id="+d.getDeviceId();
        }
        if(d.getModelOverId()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" model_over_id="+d.getModelOverId();
        }
        if(d.getTaskName()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" task_name=\""+d.getTaskName()+"\"";
        }
        if(d.getSubmitDate()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" submit_date=\""+ new java.sql.Timestamp(d.getSubmitDate().getTime())+"\"";
        }
        if(d.getDownloadDate()!=null)
        {
            if(flag == 1)
            {
                sql += ",";
            }
            flag = 1;
            sql+=" download_date=\""+ new java.sql.Timestamp(d.getDownloadDate().getTime())+"\"";
        }
        if(d.getStatus()!=null)
        {
            if(flag == 1) {
                sql += ",";
            }
            sql+=" status=\""+d.getStatus()+"\"";
        }
        sql += " where id="+d.getId();
        ps = connection.prepareStatement(sql);
        return  ps.execute();
    }
    public Vector<DeviceModelDownloadInfo> queryTask(DeviceModelDownloadInfo d) throws SQLException {
        Vector<DeviceModelDownloadInfo> ret = new Vector<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "select * from t_device_download where 1=1";
        if(d!=null)
        {
            if(d.getId()!=null)
            {
                sql += " and id="+d.getId();
            }
            if(d.getDeviceId()!=null)
            {
                sql += " and device_id=\""+d.getDeviceId()+"\"";
            }
            if(d.getModelOverId()!=null)
            {
                sql+=" and model_over_id="+d.getModelOverId();
            }
            if(d.getTaskName()!=null)
            {
                sql+=" and task_name="+d.getModelOverId();
            }
            if(d.getSubmitDate()!=null)
            {
                sql+=" and submit_date="+new java.sql.Date(d.getSubmitDate().getTime());
            }
            if(d.getDownloadDate()!=null)
            {
                sql+=" and download_date="+new java.sql.Date(d.getDownloadDate().getTime());
            }
            if(d.getUserId()!=null)
            {
                sql+=" and user_id=\""+d.getUserId();
            }
            if(d.getStatus()!=null)
            {
                sql+=" and status= \""+d.getStatus()+"\"";
            }
        }
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next())
        {
            DeviceModelDownloadInfo d1 = new DeviceModelDownloadInfo();
            d1.setId(rs.getInt(1));
            d1.setDeviceId(rs.getInt(2));
            d1.setModelOverId(rs.getInt(3));
            d1.setSubmitDate(rs.getTimestamp(4));
            d1.setDownloadDate(rs.getTimestamp(5));
            d1.setUserId(rs.getInt(6));
            d1.setStatus(rs.getString(7));
            d1.setTaskName(rs.getString(8));
            ret.add(d1);
        }
        return ret;
    }
}
