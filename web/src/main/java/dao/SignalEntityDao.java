package dao;

import bean.SignalEntity;

import java.util.List;

public interface SignalEntityDao {
    public boolean addSingnalEntity(SignalEntity signalEntity);
    public boolean deleteSingnalEntity(SignalEntity signalEntity);
    public List<SignalEntity> QuerySignalEntity(SignalEntity signalEntity);
}
