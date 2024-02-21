import cointegration.SimpleLinearRegression;
import org.apache.commons.math3.special.Beta;

public class Regressiontest {

    public static void main(String[] args){
        double[][] data=new double[5][];
        data[0]=new double[]{1,6};
        data[1]=new double[]{2,3};
        data[2]=new double[]{3,6};
        data[3]=new double[]{4,8};
        data[4]=new double[]{5,10};
        SimpleLinearRegression StrengthXY=new SimpleLinearRegression();
        StrengthXY.addData(data);

        double n = 1;
        double m = 3;
        double pvalue=1-(Beta.regularizedBeta(n * StrengthXY.getFValue()/ (m + n * StrengthXY.getFValue()), 0.5D * n, 0.5D * m));

        System.out.println("data");
    }

}
