package dao;

import bean.DeviceInfo;
import bean.TrainBearHealthInfo;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class TrainBearHealthInfoDaoImpl implements TrainBearHealthInfoDao {
    @Override
    public List<TrainBearHealthInfo> getAllDeviceInfoByCondition(TrainBearHealthInfo trainBearHealthInfo) {
        List<TrainBearHealthInfo> trainBearHealthInfos = new LinkedList<>();
        try{
            try {
                Connection connection = null;
                ResultSet rs = null;
                PreparedStatement ps = null;
                connection = JdbcUtils.getConnection();
                String sql = "select * from t_train_bear_info where 1=1";
                if (trainBearHealthInfo.getTrainNumber() != "null") {
                    sql += " and train_number=\"" + trainBearHealthInfo.getTrainNumber()+"\"";
                }
                if (trainBearHealthInfo.getVehicleNumber() != -1) {
                    sql += " and vehicle_number=" + trainBearHealthInfo.getVehicleNumber();
                }
                if (trainBearHealthInfo.getBogie() != -1)
                {
                    sql += " and bogie="+trainBearHealthInfo.getBogie();
                }
                if (trainBearHealthInfo.getAxle() != -1) {
                    sql += " and axle=" + trainBearHealthInfo.getAxle();
                }
                if(trainBearHealthInfo.getStatusOfVib()!=0)
                {
                    sql += " and status_of_vib="+trainBearHealthInfo.getStatusOfVib();
                }
                System.out.println(sql);
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next())
                {
                    TrainBearHealthInfo deviceInfo1 = new TrainBearHealthInfo();
                    deviceInfo1.setTrainNumber(rs.getString(1));
                    deviceInfo1.setVehicleNumber(rs.getInt(2));
                    deviceInfo1.setBogie(rs.getInt(3));
                    deviceInfo1.setAxle(rs.getInt(4));
                    deviceInfo1.setStatusOfTem(rs.getInt(5));
                    deviceInfo1.setStatusOfVib(rs.getInt(6));
                    trainBearHealthInfos.add(deviceInfo1);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return trainBearHealthInfos;
    }
}
