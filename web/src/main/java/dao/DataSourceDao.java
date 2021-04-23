package dao;
import bean.DataSource;

import java.sql.SQLException;
import java.util.List;

public interface DataSourceDao {
    public void add(DataSource dataSource);
    public void remove(DataSource dataSource);
    public void update(DataSource dataSource);
    public List<DataSource> Query(DataSource dataSource) throws SQLException;

}
