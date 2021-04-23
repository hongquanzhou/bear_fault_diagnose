package dao;

import bean.DeviceInfo;
import bean.TrainBearHealthInfo;

import java.util.List;

public interface TrainBearHealthInfoDao {
    public List<TrainBearHealthInfo> getAllDeviceInfoByCondition(TrainBearHealthInfo trainBearHealthInfo);
}
