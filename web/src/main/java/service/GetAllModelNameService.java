package service;

import dao.ModelNameDao;
import dao.ModelNameDaoImpl;
import bean.ModelName;

import java.util.LinkedList;
import java.util.List;

public class GetAllModelNameService {
    List<ModelName> modelNames = new LinkedList<>();
    public List<ModelName> getAllModelName()
    {
        ModelNameDao modelNameDao = new ModelNameDaoImpl();


        return modelNames;
    }
}
