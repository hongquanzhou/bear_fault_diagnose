package bean;


import java.sql.Timestamp;
import java.util.Date;

public class Msg {
        public String No = null;
        public String model = null;
        public String data = null;
        public String parameter = null;
        public Timestamp createTime = null;
        public Mode mode = null;
        public Integer taskId = null;
        public Integer pos = null;

        public String getNo() {
                return No;
        }

        public void setNo(String no) {
                No = no;
        }

        public String getModel() {
                return model;
        }

        public void setModel(String model) {
                this.model = model;
        }

        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
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

        public Mode getMode() {
                return mode;
        }

        public void setMode(Mode mode) {
                this.mode = mode;
        }

        public Integer getTaskId() {
                return taskId;
        }

        public void setTaskId(Integer taskId) {
                this.taskId = taskId;
        }

        public Integer getPos() {
                return pos;
        }

        public void setPos(Integer pos) {
                this.pos = pos;
        }

        @Override
        public String toString() {
                return "Msg{" +
                        "No='" + No + '\'' +
                        ", model='" + model + '\'' +
                        ", data='" + data + '\'' +
                        ", parameter='" + parameter + '\'' +
                        ", createTime=" + createTime +
                        ", mode=" + mode +
                        ", taskId=" + taskId +
                        ", pos=" + pos +
                        '}';
        }
}
