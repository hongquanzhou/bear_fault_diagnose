package bean;

public class DeviceInfo {
    Integer id;
    String trainNumber;
    Integer vehicleNumber;
    Integer bogie;
    Integer statusOfDevice;
    String version;

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

    public Integer getStatusOfDevice() {
        return statusOfDevice;
    }

    public void setStatusOfDevice(Integer statusOfDevice) {
        this.statusOfDevice = statusOfDevice;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "id=" + id +
                ", trainNumber='" + trainNumber + '\'' +
                ", vehicleNumber=" + vehicleNumber +
                ", bogie=" + bogie +
                ", statusOfDevice=" + statusOfDevice +
                ", version='" + version + '\'' +
                '}';
    }
}
