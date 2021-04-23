package service;

import bean.SignalEntity;
import dao.SignalEntityDao;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class DownloadData {
    SignalEntityDao signalEntityDao = null;
    public  void DownloadData(SignalEntityDao signalEntityDao)
    {
        this.signalEntityDao = signalEntityDao;
    }
    public void work(List<SignalEntity> signalEntities, OutputStream out)
    {
        Iterator<SignalEntity> it = signalEntities.iterator();
        while(it.hasNext())
        {
            SignalEntity signalEntity = signalEntityDao.QuerySignalEntity(it.next()).get(0);
        }
    }
}
