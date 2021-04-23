package util;

import bean.DeviceInfo;
import bean.DeviceInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfoParse {
    public DeviceInfo parse(InputStream inputStream)
    {
        DeviceInfo deviceInfo = new DeviceInfo();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String name = "";
            String value = "";
            int i = 0;
            Map<String, String> name_values = new HashMap<>();
            while ((line = br.readLine()) != null) {
                if (i % 4 == 1) {
                    name = line.substring(38, line.length() - 1);
                }
                if (i % 4 == 3) {
                    value = line;
                    name_values.put(name, value);
                    System.out.println(name + " : " + value);
                }
                i++;
            }

            if (!name_values.get("VehicleNumber").equals("")) {
                deviceInfo.setVehicleNumber(Integer.parseInt(name_values.get("VehicleNumber")));
            } else {
                deviceInfo.setVehicleNumber(-1);
            }

            if (!name_values.get("TrainNumber").equals("")) {
                deviceInfo.setTrainNumber(name_values.get("TrainNumber"));
            } else {
                deviceInfo.setTrainNumber("null");
            }

            if (!name_values.get("Bogie").equals("")) {
                deviceInfo.setBogie(Integer.parseInt(name_values.get("Bogie")));
            } else {
                deviceInfo.setBogie(-1);
            }

            if (!name_values.get("Version").equals("")) {
                deviceInfo.setVersion(name_values.get("Data"));
            } else {
                deviceInfo.setVersion("null");
            }
            if (!name_values.get("Status").equals("")) {
                deviceInfo.setStatusOfDevice(Integer.parseInt(name_values.get("Status")));
            } else {
                deviceInfo.setStatusOfDevice(-1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceInfo;
    }
}
