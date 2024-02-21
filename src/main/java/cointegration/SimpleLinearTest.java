package cointegration;

import java.util.ArrayList;
import java.util.List;

public class SimpleLinearTest {

    public static void main(String[] args){
        double[][] data1 = linearScatters();
        double[] x=new double[]{1,2,3,4,5,6,7,8,9,10};
        double[] y=new double[]{10,11,23,45,45,55,32,67,78,90};
        double[][]data2=new double[10][];
        for(int i=0;i<10;i++){
            data2[i]=new double[2];
            data2[i][0]=x[i];
            data2[i][1]=y[i];
        }
        SimpleLinearRegression re = new SimpleLinearRegression();
        re.addData(data2);
        System.out.println("R方为"+ re.getRSquared());
        System.out.println("调整R方为"+ re.getAdjRSquared());
        System.out.println(re.getFunction());
        System.out.println("标准误为：" + re.getStdErrors()[0]);
        System.out.println("f值：" + re.getFValue() );
        System.out.println("f检验P值:" + StatisticsUtil.getPValue(re.getFValue(), 1, data2.length - 2));
    }

    public static double[][] linearScatters() {
        List<double[]> data = new ArrayList<>();
        for (double x = 0; x <= 10; x += 0.1) {
            double y = 1.5 * x + 0.5;
            y += Math.random() * 60 - 2; // 随机数
            double[] xy = {x, y};
            data.add(xy);
        }
        return data.stream().toArray(double[][]::new);
    }

}
