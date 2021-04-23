package dao;

import bean.SignalEntity;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class SignalEntityDaoImp implements SignalEntityDao {
    @Override
    public boolean addSingnalEntity(SignalEntity signalEntity) {
        try{
            Connection connection = null;
            PreparedStatement ps = null;
            connection = JdbcUtils.getConnection();
            String sql = "insert into t_signal (date,train_number,vehicle_number,bogie,axle," +
                    "path_of_vibrate) values(?,?,?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setDate(1,signalEntity.getDate());
            ps.setString(2,signalEntity.getTrainNumber());
            ps.setInt(3,signalEntity.getVehicleNumber() );
            ps.setInt(4,signalEntity.getBogie());
            ps.setInt(5,signalEntity.getAxle());
            ps.setString(6,signalEntity.getPathOfVibrate());
            System.out.println(ps.executeUpdate()+"rows update");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteSingnalEntity(SignalEntity signalEntity) {
        return false;
    }

    @Override
    public List<SignalEntity> QuerySignalEntity(SignalEntity signalEntity) {
        List<SignalEntity> signalEntities = new LinkedList<>();
        try{
            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            connection = JdbcUtils.getConnection();
            String sql = "select * from t_signal where 1=1 ";
            if(signalEntity.getDate()!=null)
            {
                sql += " and date=\""+signalEntity.getDate()+"\"";
            }
            if(signalEntity.getTrainNumber()!=null)
            {
                sql += " and train_number=\""+signalEntity.getTrainNumber()+"\"";
            }
            if(signalEntity.getVehicleNumber()!=null)
            {
                sql += " and vehicle_number="+signalEntity.getVehicleNumber();
            }
            if((signalEntity.getBogie()!=null))
            {
                sql += " and bogie="+signalEntity.getBogie();
            }
            if(signalEntity.getAxle()!=null)
            {
                sql += " and axle="+signalEntity.getAxle();
            }
            System.out.println("sql:" + sql);
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next())
            {
                SignalEntity signalEntity1 = new SignalEntity();
                signalEntity1.setAxle(rs.getInt("axle"));
                signalEntity1.setBogie(rs.getInt("bogie"));
                signalEntity1.setDate(rs.getDate("date"));
                signalEntity1.setPathOfVibrate(rs.getString("path_of_vibrate"));
                signalEntity1.setTrainNumber(rs.getString("train_number"));
                signalEntity1.setVehicleNumber(rs.getInt("vehicle_number"));
                signalEntity1.setId(rs.getInt("id"));
                System.out.println(signalEntity1);
                signalEntities.add(signalEntity1);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return signalEntities;

    }
}
