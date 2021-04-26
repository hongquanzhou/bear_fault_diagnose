package dao;

import bean.Metric;
import bean.Task;
import bean.TaskInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class JdbcTaskInfoDaoImpl implements TaskInfoDao {
    static Connection connection = null;
    static{
        try {
            connection = JdbcUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void add(TaskInfo taskInfo) throws SQLException {
        PreparedStatement ps = null;
        String sql = "insert into t_task_info (model_name,source_name,pre_parameter,task_name,create_time," +
                "loss_function,learn_rate_update_strategy,learn_rate,batch_size,iter_num,metric,momentum,accumulator,beta_1,beta_2,epsilon,rho) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        ps = connection.prepareStatement(sql);
        if(taskInfo!=null)
        {
            ps.setString(1,taskInfo.getModelName());
            ps.setString(2,taskInfo.getSourceName());
            ps.setString(3,taskInfo.getPreParameter());
            ps.setString(4,taskInfo.getTaskName());
            ps.setTimestamp(5,taskInfo.getCreateTime());
            ps.setString(6,taskInfo.getLossFunction().name());
            ps.setString(7,taskInfo.getLearnRateUpdateStrategy().name());
            ps.setFloat(8,taskInfo.getLearnRate());
            ps.setInt(9,taskInfo.getBatchSize());
            ps.setInt(10,taskInfo.getIterNum());
            ps.setString(11,taskInfo.getMetric().name());
            ps.setFloat(12,taskInfo.getMomentum());
            ps.setFloat(13,taskInfo.getAccumulator());
            ps.setFloat(14,taskInfo.getBeta_1());
            ps.setFloat(15,taskInfo.getBeta_2());
            ps.setFloat(16,taskInfo.getEpsilon());
            ps.setFloat(17,taskInfo.getRho());
        }
        System.out.println("HH");
        Boolean ret = ps.execute();
        if(ret){
            System.out.println("ok");
        }
        else{
           System.out.println("add error");
       }
    }

    @Override
    public void remove(TaskInfo taskInfo) throws SQLException {
        PreparedStatement ps = null;
        String sql = "delete from t_task_info "+" where create_time="+taskInfo.getCreateTime();
        ps = connection.prepareStatement(sql);
        ps.execute();
    }

    @Override
    public void update(TaskInfo taskInfo) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "update t_task_info set parameter=\""+taskInfo.getParameter()+"\" where create_time="+taskInfo.getCreateTime();
        ps = connection.prepareStatement(sql);
        ps.execute();
    }

    @Override
    public Vector<TaskInfo> query(TaskInfo taskInfo) throws SQLException {
        Vector<TaskInfo> ret = new Vector<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "select * from t_task_info where 1=1";
        if(taskInfo!=null)
        {
            if(taskInfo.getModelName()!=null)
            {
                sql += " and model_name=\""+taskInfo.getModelName()+"\"";
            }
            if(taskInfo.getSourceName()!=null)
            {
                sql += " and source_name=\""+taskInfo.getSourceName()+"\"";
            }
            if(taskInfo.getParameter()!=null)
            {
                sql+=" and pre_parameter=\""+taskInfo.getPreParameter() + "\"";
            }
            if(taskInfo.getTaskName()!=null)
            {
                sql+=" and task_name=\""+taskInfo.getTaskName() + "\"";
            }
            if(taskInfo.getCreateTime()!=null)
            {
                sql+=" and create_time=\""+taskInfo.getCreateTime() +"\"";
            }
            if(taskInfo.getParameter()!=null)
            {
                sql+=" and parameter=\""+taskInfo.getParameter() + "\"";
            }
            if(taskInfo.getId()!=null)
            {
                sql+=" and id="+ taskInfo.getId();
            }
        }
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next())
        {
            TaskInfo taskInfo1 = new TaskInfo();
            taskInfo1.setId(rs.getInt(1));
            taskInfo1.setModelName(rs.getString(2));
            taskInfo1.setSourceName(rs.getString(3));
            taskInfo1.setPreParameter(rs.getString(4));
            taskInfo1.setTaskName(rs.getString(5));
            taskInfo1.setCreateTime(rs.getTimestamp(6));
            taskInfo1.setParameter(rs.getString(7));
            taskInfo1.setStatus(rs.getString(8));
            taskInfo1.setProcess(rs.getInt(9));
            taskInfo1.setLossFunction(rs.getString(10));
            taskInfo1.setLearnRateUpdateStrategy(rs.getString(11));
            taskInfo1.setLearnRate(rs.getFloat(12));
            taskInfo1.setBatchSize(rs.getInt(13));
            taskInfo1.setIterNum(rs.getInt(14));
            taskInfo1.setTestAcc(rs.getFloat(15));
            taskInfo1.setMetric(Metric.valueOf(rs.getString(16)));
            taskInfo1.setMomentum(rs.getFloat(17));
            taskInfo1.setAccumulator(rs.getFloat(18));
            taskInfo1.setBeta_1(rs.getFloat(19));
            taskInfo1.setBeta_2(rs.getFloat(20));
            taskInfo1.setEpsilon(rs.getFloat(21));
            taskInfo1.setRho(rs.getFloat(22));
            ret.add(taskInfo1);
        }
        return ret;
    }
}
