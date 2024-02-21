package dro.stat;

import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.opc.PackageRelationship;

/**
 * This is a data class file, which wraps the results of a Granger-Causality computation for easy
 * access.
 * @author MD
 * @version 1.0
 * @since 26/04/15
 */
public class GrangerCausalIndicator {

    // The result of the hypothesis test compared to the critical value
    private double pValue;
    // The critical value used to compare the hypotheses tests
    private double criticalValue;
    // The lag window size used in the Granger computation
    // aka: the amount of incorporated historical information
    private int lagSize;
    // 联合回归方程的参数结果
    private double[] h1Parameters;



    /**
     * Creates a new GrangerCausalIndicator object.
     * @param a_pValue The computed pValue.
     * @param aCriticalValue The critical value used in the F-Test to check for statistical
     *                       evidence.
     * @param aLagSize The lag window size used for computing the Granger-Causality value.
     */
    public GrangerCausalIndicator(double a_pValue, double aCriticalValue, int aLagSize) {
        this.pValue=a_pValue;
        this.criticalValue=aCriticalValue;
        this.lagSize=aLagSize;
    }

    public GrangerCausalIndicator(double a_pValue, double aCriticalValue, int aLagSize,double[] h1Parameters) {
        this.pValue=a_pValue;
        this.criticalValue=aCriticalValue;
        this.lagSize=aLagSize;
        this.h1Parameters=h1Parameters;
    }

    /**
     * @return The computed pValue of the Granger-Causality computation
     */
    public double getPValue() {
        return pValue;
    }

    /**
     * @return The critical value used to compare the hypotheses results.
     */
    public double getCriticalValue() {
        return criticalValue;
    }

    /**
     * @return The lag window size used for the Granger-Computation.
     */
    public int getLagSize() {
        return lagSize;
    }

    /**
     * @return 返回联合回归的参数结果
     */
    public double[] getH1Parameters(){return h1Parameters; }

    @Override
    public String toString() {
        return "Granger-Causality";
    }
}
