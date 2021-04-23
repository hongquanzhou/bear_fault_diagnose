package bean;

import java.sql.Timestamp;

public class TaskInfo {
    Integer id;
    String modelName;
    String sourceName;
    String preParameter;
    String taskName;
    Timestamp createTime;

    LossFunction lossFunction;
    Optimizer learnRateUpdateStrategy; //改为Optimizer
    Float learnRate;

    Float momentum;
    Float accumulator;
    Float beta_1;
    Float beta_2;
    Float epsilon;
    Float rho;
    Integer batchSize;
    Integer iterNum;

    String parameter;
    String status;
    Integer process;
    Float testAcc;

    Metric metric;

    public Float getMomentum() {
        return momentum;
    }

    public void setMomentum(Float momentum) {
        this.momentum = momentum;
    }

    public Float getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(Float accumulator) {
        this.accumulator = accumulator;
    }

    public Float getBeta_1() {
        return beta_1;
    }

    public void setBeta_1(Float beta_1) {
        this.beta_1 = beta_1;
    }

    public Float getBeta_2() {
        return beta_2;
    }

    public void setBeta_2(Float beta_2) {
        this.beta_2 = beta_2;
    }

    public Float getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(Float epsilon) {
        this.epsilon = epsilon;
    }

    public Float getRho() {
        return rho;
    }

    public void setRho(Float rho) {
        this.rho = rho;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public void setLossFunction(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
    }

    public void setLearnRateUpdateStrategy(Optimizer learnRateUpdateStrategy) {
        this.learnRateUpdateStrategy = learnRateUpdateStrategy;
    }

    public Float getTestAcc() {
        return testAcc;
    }

    public void setTestAcc(Float testAcc) {
        this.testAcc = testAcc;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "id=" + id +
                ", modelName='" + modelName + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", preParameter='" + preParameter + '\'' +
                ", taskName='" + taskName + '\'' +
                ", createTime=" + createTime +
                ", lossFunction=" + lossFunction +
                ", learnRateUpdateStrategy=" + learnRateUpdateStrategy +
                ", learnRate=" + learnRate +
                ", momentum=" + momentum +
                ", accumulator=" + accumulator +
                ", beta_1=" + beta_1 +
                ", beta_2=" + beta_2 +
                ", epsilon=" + epsilon +
                ", rho=" + rho +
                ", batchSize=" + batchSize +
                ", iterNum=" + iterNum +
                ", parameter='" + parameter + '\'' +
                ", status='" + status + '\'' +
                ", process=" + process +
                ", testAcc=" + testAcc +
                ", metric=" + metric +
                '}';
    }

    public LossFunction getLossFunction() {
        return lossFunction;
    }

    public void setLossFunction(String lossFunction) {
        LossFunction los = LossFunction.valueOf(lossFunction);
        this.lossFunction = los;
    }

    public Optimizer getLearnRateUpdateStrategy() {
        return learnRateUpdateStrategy;
    }

    public void setLearnRateUpdateStrategy(String learnRateUpdateStrategy) {
        this.learnRateUpdateStrategy = Optimizer.valueOf(learnRateUpdateStrategy);
    }

    public Float getLearnRate() {
        return learnRate;
    }

    public void setLearnRate(Float learnRate) {
        this.learnRate = learnRate;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getIterNum() {
        return iterNum;
    }

    public void setIterNum(Integer iterNum) {
        this.iterNum = iterNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getPreParameter() {
        return preParameter;
    }

    public void setPreParameter(String preParameter) {
        this.preParameter = preParameter;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public TaskInfo() {
        this.id = null;
        this.modelName = null;
        this.sourceName = null;
        this.preParameter = null;
        this.taskName = null;
        this.createTime = null;
        this.parameter = null;
        this.status = null;
        this.process = null;
    }
}
