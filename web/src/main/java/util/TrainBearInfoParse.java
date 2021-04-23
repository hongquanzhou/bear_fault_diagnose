package util;

import bean.TrainBearHealthInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TrainBearInfoParse {
    public TrainBearHealthInfo parse(InputStream inputStream) {
        TrainBearHealthInfo trainBearInfo = new TrainBearHealthInfo();
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
                trainBearInfo.setVehicleNumber(Integer.parseInt(name_values.get("VehicleNumber")));
            } else {
                trainBearInfo.setVehicleNumber(-1);
            }

            if (!name_values.get("TrainNumber").equals("")) {
                trainBearInfo.setTrainNumber(name_values.get("TrainNumber"));
            } else {
                trainBearInfo.setTrainNumber("null");
            }

            if (!name_values.get("Bogie").equals("")) {
                trainBearInfo.setBogie(Integer.parseInt(name_values.get("Bogie")));
            } else {
                trainBearInfo.setBogie(-1);
            }

            if (!name_values.get("Axle").equals("")) {
                trainBearInfo.setAxle(Integer.parseInt(name_values.get("Axle")));
            } else {
                trainBearInfo.setAxle(-1);
            }
            if(name_values.get("Tem")!=null) {
                if (!name_values.get("Tem").equals("")) {
                    trainBearInfo.setStatusOfTem(Integer.parseInt(name_values.get("Tem")));
                } else {
                    trainBearInfo.setStatusOfTem(-1);
                }
            }
            if (!name_values.get("Vib").equals("")) {
                trainBearInfo.setStatusOfVib(Integer.parseInt(name_values.get("Vib")));
            } else {
                trainBearInfo.setStatusOfVib(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainBearInfo;
    }
    
    
    
    
    
}
