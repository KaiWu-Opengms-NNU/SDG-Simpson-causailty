package cointegration;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class SimpleLinearRegression extends Regression{

    private final SimpleRegression regression;

    public SimpleLinearRegression() {
        this.regression = new SimpleRegression();
    }


    public void addData(final double[][] data) {

        regression.addData(data); // 数据集

        this.setHasData(true);

        RegressionResults results = regression.regress();

        this.setParameters(results.getParameterEstimates());
        this.setRSquared(results.getRSquared());
        this.setAdjRSquared(results.getAdjustedRSquared());
        this.setStdErrors(results.getStdErrorOfEstimates());

        this.setFValue((results.getRegressionSumSquares()) / (results.getErrorSumSquares()  / (data.length -2)) );

        //Pvalue

    }


    public String getFunction() {

        if (!this.isHasData()) {
            return "未构造数据";
        }

        double b0 = this.getParameters()[0];
        double b1 = this.getParameters()[1];

        return "f(x) =" +
                (b0 >= 0 ? " " : " - ") +
                Math.abs(b0) +
                (b1 > 0 ? " + " : " - ") +
                Math.abs(b1) +
                "x";
    }

}
