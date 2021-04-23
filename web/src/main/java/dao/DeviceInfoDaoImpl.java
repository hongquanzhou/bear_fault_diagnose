package dao;

import bean.DeviceInfo;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class DeviceInfoDaoImpl implements DeviceInfoDao{
    static Connection connection = null;
    static{
        try {
            connection = JdbcUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DeviceInfo> getAllDeviceInfoByCondition(DeviceInfo deviceInfo)
    {
        List<DeviceInfo> deviceInfos = new LinkedList<>();
        try {
            Connection connection = null;
            ResultSet rs = null;
            PreparedStatement ps = null;
            connection = JdbcUtils.getConnection();
            String sql = "select * from t_device_info where 1=1";
            if (deviceInfo.getTrainNumber() != "null") {
                sql += " and train_number=\"" + deviceInfo.getTrainNumber()+"\"";
            }
            if (deviceInfo.getVehicleNumber() != -1) {
                sql += " and vehicle_number=" + deviceInfo.getVehicleNumber();
            }
            if (deviceInfo.getBogie() != -1)
            {
                sql += " and bogie="+deviceInfo.getBogie();
            }
            if (deviceInfo.getVersion() != "null") {
                sql += " and version=\"" + deviceInfo.getVersion()+"\"";
            }
            if(deviceInfo.getStatusOfDevice()!=0)
            {
                sql += " and status_of_device="+deviceInfo.getStatusOfDevice();
            }
            System.out.println(sql);
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())
            {
                DeviceInfo deviceInfo1 = new DeviceInfo();
                deviceInfo1.setId(rs.getInt(1));
                deviceInfo1.setTrainNumber(rs.getString(2));
                deviceInfo1.setVehicleNumber(rs.getInt(3));
                deviceInfo1.setBogie(rs.getInt(4));
                deviceInfo1.setVersion(rs.getString(5));
                deviceInfo1.setStatusOfDevice(rs.getInt(6));
                deviceInfos.add(deviceInfo1);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return deviceInfos;
    }

    @Override
    public boolean add(DeviceInfo deviceInfo) throws SQLException {
        PreparedStatement ps = null;
        String sql = "insert into t_device_info (id,train_number,vehicle_number,bogie) values(?,?,?,?)";
        ps = connection.prepareStatement(sql);
        if(deviceInfo!=null)
        {
            ps.setInt(1,deviceInfo.getId());
            ps.setString(2,deviceInfo.getTrainNumber());
            ps.setInt(3,deviceInfo.getVehicleNumber());
            ps.setInt(4,deviceInfo.getBogie());
        }
        return ps.execute();
    }

    @Override
    public boolean delete(DeviceInfo deviceInfo) throws SQLException {
        PreparedStatement ps = null;
        String sql = "delete from t_device_info "+" where id="+deviceInfo.getId();
        ps = connection.prepareStatement(sql);
        return ps.execute();

    }

    @Override
    public boolean update(DeviceInfo deviceInfo) throws SQLException {
        PreparedStatement ps = null;
        String sql = "update t_device_info set";
        if(deviceInfo.getTrainNumber()!=null)
        {
            sql+=" train_number=\""+deviceInfo.getTrainNumber()+"\"";
        }
        if(deviceInfo.getVehicleNumber()!=null)
        {
            sql+=" vehicle_number="+deviceInfo.getVehicleNumber();
        }
        if(deviceInfo.getBogie()!=null)
        {
            sql+=" bogie="+deviceInfo.getBogie();
        }
        if(deviceInfo.getVersion()!=null)
        {
            sql+=" version=\""+deviceInfo.getVersion()+"\"";
        }
        if(deviceInfo.getStatusOfDevice()!=null)
        {
            sql+=" status_of_device="+deviceInfo.getStatusOfDevice();
        }
        sql += " where id="+deviceInfo.getId();
        ps = connection.prepareStatement(sql);
        return  ps.execute();
    }

    @Override
    public Vector<DeviceInfo> query(DeviceInfo deviceInfo) throws SQLException {
        Vector<DeviceInfo> deviceInfos = new Vector<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "select * from t_device_info where 1=1";
        if(deviceInfo!=null)
        {
            if(deviceInfo.getId()!=null)
            {
                sql += " and id="+deviceInfo.getId();
            }
            if(deviceInfo.getTrainNumber()!=null)
            {
                sql += " and train_number=\""+deviceInfo.getTrainNumber()+"\"";
            }
            if(deviceInfo.getVehicleNumber()!=null)
            {
                sql+=" and vehicle_number="+deviceInfo.getVehicleNumber();
            }
            if(deviceInfo.getBogie()!=null)
            {
                sql+=" and bogie="+deviceInfo.getBogie();
            }
            if(deviceInfo.getVersion()!=null)
            {
                sql+=" and version=\""+deviceInfo.getVersion()+"\"";
            }
            if(deviceInfo.getStatusOfDevice()!=null)
            {
                sql+=" and status_of_device"+deviceInfo.getStatusOfDevice();
            }
        }
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next())
        {
            DeviceInfo deviceInfo1 = new DeviceInfo();
            deviceInfo1.setId(rs.getInt(1));
            deviceInfo1.setTrainNumber(rs.getString(2));
            deviceInfo1.setVehicleNumber(rs.getInt(3));
            deviceInfo1.setBogie(rs.getInt(4));
            deviceInfo1.setVersion(rs.getString(5));
            deviceInfo1.setStatusOfDevice(rs.getInt(6));
            deviceInfos.add(deviceInfo1);
        }
        return deviceInfos;
    }
}
