package cointegration;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultipleLinearTest {

    public static void main(String[] args){
        //double[][] x = randomX3();
        double[][] x=new double[6][];
        x[0]=new double[]{1,3,12};
        x[1]=new double[]{1,4,23};
        x[2]=new double[]{2,5,19};
        x[3]=new double[]{3,6,25};
        x[4]=new double[]{4,9,28};
        x[5]=new double[]{5,8,30};
        //double[] y = randomY3(x);
        double[] y=new double[]{100,234,389,456,501,603};
        MultipleLinearRegression mul = new MultipleLinearRegression();
        mul.addData(x, y, 2);

        System.out.println(mul.getFunction());
        System.out.println("标准误： "  + mul.getStdErrors()[0]);
        System.out.println("R方 : " + mul.getRSquared());
        System.out.println("调整后R方 :" + mul.getAdjRSquared());
        System.out.println("f值：" + mul.getFValue()) ;
        System.out.println("f检验P值:" + StatisticsUtil.getPValue(mul.getFValue(), mul.getDfIndependent(), mul.getDfDependent()));
    }

    public static double[][] randomX3() {
        List<double[]> data = new ArrayList<>();
        for (double i = 0; i < 10; i += 0.1) {
            double x1 = i;
            double x2 = Math.sqrt(i);
            data.add(new double[]{x1, x2});
        }
        return data.stream().toArray(double[][]::new);
    }

    public static double[] randomY3(double[][] arr) {
        if (arr != null && arr.length > 0) {
            int len = arr.length;
            double[] y = new double[len];
            for (int i = 0; i < len; i++) {
                double[] x = arr[i];
                // 构造数据
                y[i] = functionConstructorY3(x);
            }
            return y;
        }
        return null;
    }

    public static double functionConstructorY3(double[] x) {
        double x1 = x[0];
        double x2 = x[1];
        return 20 + 2 * x1 + 3 * x2 + Math.random() * 30;
    }
}
