package bean;

import org.omg.PortableInterceptor.INACTIVE;

import java.sql.Date;
import java.sql.Timestamp;

public class SignalEntity {
    Integer id;
    String date1;
    String trainNumber;
    Date date;
    Integer vehicleNumber;
    Integer bogie;
    Integer axle;
    String pathOfVibrate;

    public void trans()
    {
        if(date1!=null)
        {
            String[] lines = date1.split("-");
            int year = Integer.parseInt(lines[0])-1900;
            int month = Integer.parseInt(lines[1])-1;
            int day = Integer.parseInt(lines[2]);
            this.date = new Date(year,month,day);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(Integer vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Integer getBogie() {
        return bogie;
    }

    public void setBogie(Integer bogie) {
        this.bogie = bogie;
    }

    public Integer getAxle() {
        return axle;
    }

    public void setAxle(Integer axle) {
        this.axle = axle;
    }

    public String getPathOfVibrate() {
        return pathOfVibrate;
    }

    public void setPathOfVibrate(String pathOfVibrate) {
        this.pathOfVibrate = pathOfVibrate;
    }

    @Override
    public String toString() {
        return "SignalEntity{" +
                "trainNumber='" + trainNumber + '\'' +
                ", date='" + date + '\'' +
                ", vehicleNumber=" + vehicleNumber +
                ", bogie=" + bogie +
                ", axle=" + axle +
                ", pathOfVibrate='" + pathOfVibrate + '\'' +
                '}';
    }
}
