package dao;

import bean.ModelName;

import java.sql.SQLException;
import java.util.List;

public interface ModelNameDao {
    public ModelName findPathByName(String name) throws SQLException;
    public void addModelName(ModelName modelName);
    public void deleteModelByName(String name);
    public boolean updateModel(ModelName modelName);
    public List<ModelName> getAllModelName();
    public boolean hasName(String name);

}
