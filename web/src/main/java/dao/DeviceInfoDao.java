package dao;

import bean.DeviceInfo;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public interface DeviceInfoDao {
    public List<DeviceInfo> getAllDeviceInfoByCondition(DeviceInfo deviceInfo);
    public boolean add(DeviceInfo deviceInfo) throws SQLException;
    public boolean delete(DeviceInfo deviceInfo) throws SQLException;
    public boolean update(DeviceInfo deviceInfo) throws SQLException;
    public Vector<DeviceInfo> query(DeviceInfo deviceInfo) throws SQLException;
}