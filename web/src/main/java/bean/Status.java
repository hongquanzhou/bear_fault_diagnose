package bean;

public class Status {
    public State state;
    public int process;

    @Override
    public String toString() {
        return "Status{" +
                "state=" + state +
                ", process=" + process +
                '}';
    }
}
