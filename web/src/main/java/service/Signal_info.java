package service;

import bean.SignalEntity;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Signal_info {
    public List<SignalEntity> getallUser() throws SQLException {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        conn = JdbcUtils.getConnection();
        List<SignalEntity> list=new ArrayList<SignalEntity>();
        String sql="select * from mysignal";
        try {
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()) {
                SignalEntity user=new SignalEntity();
                user.setTrainNumber(rs.getString("train_number"));
                user.setDate(rs.getDate("date"));
                user.setVehicleNumber(rs.getInt("vehicle_number"));
                user.setBogie(rs.getInt("bogie"));
                user.setAxle(rs.getInt("axle"));
                user.setPathOfVibrate(rs.getString("path_of_vibrate"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
