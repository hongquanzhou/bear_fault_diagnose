package dao;

import bean.Task;

import java.sql.SQLException;

public interface TaskDao {
    public void add(Task task) throws SQLException;
    public Integer findCreateTime(Task task) throws SQLException;
    public void removeTask(Task task);
}
