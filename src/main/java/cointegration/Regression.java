package cointegration;

public class Regression {
    private double rSquared;

    private double adjRSquared;

    private double[] parameters;

    private double[] stdErrors;

    private double fValue;

    private boolean hasData;

    private Integer dfDependent;

    private Integer dfIndependent;

    public Integer getDfDependent() {
        return dfDependent;
    }

    public void setDfDependent(Integer dfDependent) {
        this.dfDependent = dfDependent;
    }

    public Integer getDfIndependent() {
        return dfIndependent;
    }

    public void setDfIndependent(Integer dfIndependent) {
        this.dfIndependent = dfIndependent;
    }

    public Regression() {
    }

    public double getRSquared() {
        return rSquared;
    }

    public void setRSquared(double rSquared) {
        this.rSquared = rSquared;
    }

    public double getAdjRSquared() {
        return adjRSquared;
    }

    public void setAdjRSquared(double adjRSquared) {
        this.adjRSquared = adjRSquared;
    }

    public double[] getParameters() {
        return parameters;
    }

    public void setParameters(double[] parameters) {
        this.parameters = parameters;
    }

    public double[] getStdErrors() {
        return stdErrors;
    }

    public void setStdErrors(double[] stdErrors) {
        this.stdErrors = stdErrors;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public double getFValue() {
        return fValue;
    }

    public void setFValue(double fValue) {
        this.fValue = fValue;
    }
}
