package dao;

import bean.TaskInfo;

import java.sql.SQLException;
import java.util.Vector;

public interface TaskInfoDao {
    public void add(TaskInfo taskInfo) throws SQLException;
    public void remove(TaskInfo taskInfo) throws SQLException;
    public void update(TaskInfo taskInfo) throws SQLException;
    public Vector<TaskInfo> query(TaskInfo taskInfo) throws SQLException;
}
