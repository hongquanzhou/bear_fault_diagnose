package bean;

public class TrainBearHealthInfo {
    String trainNumber;
    int vehicleNumber;
    int bogie;
    int Axle;
    int statusOfTem;
    int statusOfVib;

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public int getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(int vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getBogie() {
        return bogie;
    }

    public void setBogie(int bogie) {
        this.bogie = bogie;
    }

    public int getAxle() {
        return Axle;
    }

    public void setAxle(int axle) {
        Axle = axle;
    }

    public int getStatusOfTem() {
        return statusOfTem;
    }

    public void setStatusOfTem(int statusOfTem) {
        this.statusOfTem = statusOfTem;
    }

    public int getStatusOfVib() {
        return statusOfVib;
    }

    public void setStatusOfVib(int statusOfVib) {
        this.statusOfVib = statusOfVib;
    }

    @Override
    public String toString() {
        return "TrainBearHealthInfo{" +
                "trainNumber='" + trainNumber + '\'' +
                ", vehicleNumber=" + vehicleNumber +
                ", bogie=" + bogie +
                ", Axle=" + Axle +
                ", statusOfTem=" + statusOfTem +
                ", statusOfVib=" + statusOfVib +
                '}';
    }
}
