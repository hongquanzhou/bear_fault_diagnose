package main;


import bean.DeviceInfo;
import com.google.gson.Gson;

public class test {
    public static void main(String[] args) {
        DeviceInfo deviceInfo = new Gson().fromJson("{id:1,trainNumber:\"G520\",vehicleNumber:12345,bogie:1}",DeviceInfo.class);
        String s = "{id:1,trainNumber:\"G520\",vehicleNumber:12345,bogie:1}";
        System.out.println(s.length());
        System.out.println(deviceInfo);
    }
}
