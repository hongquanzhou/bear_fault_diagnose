package bean;

import com.google.gson.Gson;

import java.util.Vector;

public class Model {
    public Vector<Vector<Integer>> layersSize = new Vector<Vector<Integer>>();  //CNN Depth|Height|Width|filter Height|filter Width   Dense  FCNN
    public Vector<OpAndFunc>  ops = new Vector<OpAndFunc>();    // (Max-Pool Convolution  flatten  Normal)|activation(relu sigmoid)

    public Model() {
        Vector<Integer> v1 = new Vector<Integer>(5,8);
        v1.add(1);v1.add(2); v1.add(3);v1.add(4); v1.add(5);
        layersSize.add(v1);
        layersSize.add(v1);
        OpAndFunc op1 = new OpAndFunc(OP.convolution,ActivationFunc.relu);
        OpAndFunc op2 = new OpAndFunc(OP.max_pool,ActivationFunc.relu);
        OpAndFunc op3 = new OpAndFunc(OP.convolution,ActivationFunc.relu);
        OpAndFunc op4 = new OpAndFunc(OP.max_pool,ActivationFunc.relu);
        ops.add(op1);
        ops.add(op2);
        ops.add(op3);
        ops.add(op4);

    }

    @Override
    public String toString() {
        return "Model{" +
                "layersSize=" + layersSize +
                ", ops=" + ops +
                '}';
    }

    public static void main(String[] args) {
        //String s = "{\"layersSize\":[[1,2,3,4,5],[1,2,3,4,5]],\"ops\":[{\"op\":\"convolution\",\"func\":\"relu\"},{\"op\":\"max_pool\",\"func\":\"relu\"},{\"op\":\"convolution\",\"func\":\"relu\"},{\"op\":\"max_pool\",\"func\":\"relu\"}]}";
        String s = "{\"layersSize\":[[8,128,128,8,8],[8,64,64,16,16],[24,48,48,8,8],[24,16,16,8,8],[256],[128]],\"ops\":[{\"op\":\"max_pool\",\"func\":\"relu\"},{\"op\":\"convolution\",\"func\":\"relu\"},{\"op\":\"max_pool\",\"func\":\"relu\"},{\"op\":\"dense\",\"func\":\"relu\"},{\"op\":\"dense\",\"func\":\"relu\"}]}";
        Model model = new Gson().fromJson(s,Model.class);
        System.out.println(model);
        System.out.println(new Gson().toJson(model));

    }

};