package cointegration;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class MultipleLinearRegression extends Regression {
    private final OLSMultipleLinearRegression ols;

    public MultipleLinearRegression() {
        ols = new OLSMultipleLinearRegression();
    }

    public void addData(double[][] xArray, double[] yArray, int numberOfIndependent) {

        int dfDependent = yArray.length - numberOfIndependent - 1;

        ols.newSampleData(yArray, xArray);

        this.setParameters(ols.estimateRegressionParameters());
        this.setStdErrors(ols.estimateRegressionParametersStandardErrors());
        this.setRSquared(ols.calculateRSquared());
        this.setAdjRSquared(ols.calculateAdjustedRSquared());
        this.setDfDependent(dfDependent);
        this.setDfIndependent(numberOfIndependent);

        this.setFValue(((ols.calculateTotalSumOfSquares() - ols.calculateResidualSumOfSquares()) / numberOfIndependent) / (ols.calculateResidualSumOfSquares() / dfDependent));
        this.setHasData(true);

    }

    public String getFunction() {

        if (!this.isHasData()) {
            return "未构造数据";
        }

        final double[] parameters = this.getParameters();
        StringBuilder function = new StringBuilder("y =  ");
        for (int i = 0; i < parameters.length; i++) {
            function.append(parameters[i]).append("x").append(i);
            if (i != (parameters.length -1)) {
                function.append(" + ");
            }

        }
        return function.toString();
    }
}
