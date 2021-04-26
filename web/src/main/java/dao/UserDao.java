package dao;

import bean.User;

import java.sql.SQLException;
import java.util.Vector;

public interface UserDao
{
    public User findByUsername(String username);
    public Vector<User> queryUser(User user);
    public void add(User user);
    public void update(User user) throws SQLException;
    public User getAllUserInfo(String username);
}
