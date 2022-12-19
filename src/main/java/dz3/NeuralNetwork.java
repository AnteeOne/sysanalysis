package dz3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class NeuralNetwork {

    private static final double[] x1 = new double[100];
    private static final double[] x2 = new double[100];
    private static final double[] y = new double[100];

    static {
        readTestData100();
    }

    private double w11, w12, w21, w22, v11, v12, v13, v21, v22, v23, w1, w2, w3;
    private double e;

    public NeuralNetwork(double w11, double w12, double w21, double w22, double v11, double v12, double v13, double v21, double v22, double v23, double w1, double w2, double w3) {
        this.w11 = w11;
        this.w12 = w12;
        this.w21 = w21;
        this.w22 = w22;
        this.v11 = v11;
        this.v12 = v12;
        this.v13 = v13;
        this.v21 = v21;
        this.v22 = v22;
        this.v23 = v23;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    public NeuralNetwork(DataModel model) throws NumberFormatException {
        init(model);
    }

    public NeuralNetwork() {
    }

    private static void readTestData100() {
        List<String> lstData;
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/antee/Documents/Projects/Kotlin/KFU/sysanalysis/src/main/java/dz3/test_data_100.csv"))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(";");
                x1[i] = Double.parseDouble(s[0]);
                x2[i] = Double.parseDouble(s[1]);
                y[i] = Double.parseDouble(s[2]);
                //System.out.println(x1[i] +";" + x2[i] +";" + y[i]);
                i++;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataModel dm = new DataModel("0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "0.1", "");
        System.out.println(dm);
        NeuralNetwork nn = new NeuralNetwork(dm);
        //nn.readTestData100();
        System.out.println(nn.e());
    }

    public void init(DataModel model) throws NumberFormatException {
        this.w11 = Double.parseDouble(model.getW11());
        this.w12 = Double.parseDouble(model.getW12());
        this.w21 = Double.parseDouble(model.getW21());
        this.w22 = Double.parseDouble(model.getW22());
        this.v11 = Double.parseDouble(model.getV11());
        this.v12 = Double.parseDouble(model.getV12());
        this.v13 = Double.parseDouble(model.getV13());
        this.v21 = Double.parseDouble(model.getV21());
        this.v22 = Double.parseDouble(model.getV22());
        this.v23 = Double.parseDouble(model.getV23());
        this.w1 = Double.parseDouble(model.getW1());
        this.w2 = Double.parseDouble(model.getW2());
        this.w3 = Double.parseDouble(model.getW3());
    }

    private double f(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double g(double x1, double x2) {
        double h11 = f(x1 * w11 + x2 * w21);
        double h12 = f(x1 * w12 + x2 * w22);
        return f(f(h11 * v11 + h12 * v21) * w1 + f(h11 * v12 + h12 * v22) * w2 + f(h11 * v13 + h12 * v23));
    }

    public double e() {
        double res = 0;
        for (int i = 0; i < 100; i++) {
            double yt = g(x1[i], x2[i]);
            res += (yt - y[i]) * (yt - y[i]);
        }
        return res;
    }

}
