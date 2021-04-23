package bean;

public class OpAndFunc {
    private OP op;
    private ActivationFunc func;

    public OpAndFunc(OP op, ActivationFunc func) {
        this.op = op;
        this.func = func;
    }

    public OP getOp() {
        return op;
    }

    public void setOp(OP op) {
        this.op = op;
    }

    public ActivationFunc getFunc() {
        return func;
    }

    public void setFunc(ActivationFunc func) {
        this.func = func;
    }

    @Override
    public String toString() {
        return "OpAndFunc{" +
                "op=" + op +
                ", func=" + func +
                '}';
    }
}
