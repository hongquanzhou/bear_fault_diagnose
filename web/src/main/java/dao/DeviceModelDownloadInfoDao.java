package dao;

import bean.DeviceModelDownloadInfo;

import java.sql.SQLException;
import java.util.Vector;

public interface DeviceModelDownloadInfoDao {
    public boolean addTask(DeviceModelDownloadInfo d) throws SQLException;

    public boolean deleteTask(DeviceModelDownloadInfo d) throws SQLException;

    public boolean updateTask(DeviceModelDownloadInfo d) throws SQLException;

    public Vector<DeviceModelDownloadInfo> queryTask(DeviceModelDownloadInfo d) throws SQLException;
}
