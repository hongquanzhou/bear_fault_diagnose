package bean;

import java.sql.Timestamp;
import java.util.Date;
public class DeviceModelDownloadInfo {
    Integer id;
    Integer deviceId;
    Integer modelOverId;
    String taskName;
    Timestamp submitDate;
    Timestamp downloadDate;
    Integer userId;
    String status;

    public DeviceModelDownloadInfo() {
        this.id = null;
        this.deviceId = null;
        this.modelOverId = null;
        this.taskName = null;
        this.submitDate = null;
        this.downloadDate = null;
        this.userId = null;
        this.status = null;
    }


    @Override
    public String toString() {
        return "DeviceModelDownloadInfo{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", modelOverId=" + modelOverId +
                ", taskName='" + taskName + '\'' +
                ", submitDate=" + submitDate +
                ", downloadDate=" + downloadDate +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getModelOverId() {
        return modelOverId;
    }

    public void setModelOverId(Integer modelOverId) {
        this.modelOverId = modelOverId;
    }


    public Timestamp getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Timestamp submitDate) {
        this.submitDate = submitDate;
    }

    public Timestamp getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Timestamp downloadDate) {
        this.downloadDate = downloadDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

