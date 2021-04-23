package dao;

import bean.User;

import java.sql.SQLException;

public interface UserDao
{
    public User findByUsername(String username);
    public void add(User user);
    public void update(User user) throws SQLException;
    public User getAllUserInfo(String username);
}
