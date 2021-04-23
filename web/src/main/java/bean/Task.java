package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.sql.Date;

public class Task {
    Integer createTime;
    //Model model;
    String model;
    //Data data;
    String data;
    //Parameter parameter;
    Integer parameter;

    public Task(Integer createTime, String model, String data, Integer parameter) {
        this.createTime = createTime;
        this.model = model;
        this.data = data;
        this.parameter = parameter;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getParameter() {
        return parameter;
    }

    @Override
    public String toString() {
        return "Task{" +
                "createTime=" + createTime +
                ", model='" + model + '\'' +
                ", data=" + data +
                ", parameter=" + parameter +
                '}';
    }

    public void setParameter(Integer parameter) {
        this.parameter = parameter;
    }
}
