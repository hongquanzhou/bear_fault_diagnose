package bean;

import java.util.Date;
public class DeviceModelDownloadInfo {
    Integer id;
    Integer deviceId;
    Integer modelOverId;
    Date submitDate;
    Date downloadDate;
    Integer userId;
    String status;

    public DeviceModelDownloadInfo() {
        this.id = null;
        this.deviceId = null;
        this.modelOverId = null;
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
                ", submitDate=" + submitDate +
                ", downloadDate=" + downloadDate +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
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

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
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

