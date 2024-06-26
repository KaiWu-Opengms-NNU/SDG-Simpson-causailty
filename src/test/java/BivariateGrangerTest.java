import dro.stat.GrangerCausality;
import dro.stat.GrangerCausalityStrategy_Bivariate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Class that provides all unit tests for the default Granger-Causality methods and the specific
 * bivariate Granger-Methods.
 *
 * @author MD
 * @version 1.0
 * @since 02/05/15
 */
public class BivariateGrangerTest {

    // missing functions under test in GrangerCausalityStrategy_Bivariate class:
    // - apply

    // missing functions under test in GrangerCausality class:
    // - checkDataSizeConstraints
    // - createLaggedSide

    /**
     * Tests to verify the 'apply' method.
     */
    @Test
    public void testApplyMethod() {
        // Not a test yet!
        double[] y = new double[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        double[] x = new double[]{ 80, 70, 60, 50, 40, 30, 20, 10, 0 };
        GrangerCausalityStrategy_Bivariate big = new BivariateGrangerImpl(2);
        System.out.println(big.apply(y, x).getPValue());
        System.out.println(big.apply(y, x).getCriticalValue());
    }

    /**
     * Tests the creation of the lagged matrix for the Granger model.
     */
    @Test
    public void testCreateLaggedSide() {

    }

    /**
     * Tests whether the method correctly prevents the computation of Granger-Causality, if the
     * given data sets are invalid (if their size doesn't adhere to predefined constraints).
     */
    @Test
    public void testDataSizeConstraints() {
        GrangerCausalityStrategy_Bivariate big = new BivariateGrangerImpl(3);
        // T1) Expect an exception, if the number of predictors is greater than the remaining data
        //     size for the lagged matrix creation
        //     lag size = 3, data size = 9, variables = 2, predictors = 6
        double[] y = new double[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        double[] x = new double[]{ 80, 70, 60, 50, 40, 30, 20, 10, 0 };
        try {
           big.apply(y, x);
            Assert.fail();
        } catch (Exception e) { /* Expected exception */ }
    }

    /**
     * Tests the maximum lag size possible for a given number of observations and the number of
     * variables in the universe (this method is not limited to bivariate analysis).
     */
    @Test
    public void testMaxLagSize() {
        // T1) Expect an exception, if the user tries to compute the maximum lag with parameters
        //     smaller than 1
        try {
            GrangerCausality.getMaximumLagSize(0, 5);
            Assert.fail();
        } catch ( Exception ex ) { /* Expected result */ }
        try {
            GrangerCausality.getMaximumLagSize(1, 0);
            Assert.fail();
        } catch ( Exception ex ) { /* Expected result */ }

        // T2) Expect an exception, if a minimum lag size of 1 can't be satisfied for the given
        //     parameters. E.g., if the universe contains 3 variables, at least 4 observations are
        //     required to satisfy the linear Granger system of equations for a lag size of 1
        try {
            GrangerCausality.getMaximumLagSize(3, 3);
            Assert.fail();
        } catch (Exception ex) { /* Expected result */ }

        // T3) Test the maximum lag size for one example, e.g., if we have 5 variables and
        //     50 observations, we expect a maximum lag size of 8
        int maxLag = GrangerCausality.getMaximumLagSize(50, 5);
        Assert.assertEquals(8, maxLag, 0);
        int test=GrangerCausality.getMaximumLagSize(10,2);
        System.out.println(test);
    }

    /**
     * Tests the square sum method.
     */
    @Test
    public void testSqrSumMethod() {
        // T1) Square sum of an empty array is .0
        BivariateGrangerImpl big = new BivariateGrangerImpl(22);
        double sqrSum = big.sqrSum(new double[]{});
        Assert.assertEquals(.0, sqrSum, .0);

        // T2) Square sum of an example has to be correct
        double[] testArr = new double[] { 1, 2, 3, 4, 5 };
        sqrSum = big.sqrSum(testArr);
        Assert.assertEquals(55, sqrSum, .0);
    }


    /**
     * Tests the strip method that cuts off l given elements from an array.
     */
    @Test
    public void testStripMethod() {
        // T1) Can't strip an array of l elements, if the array length is smaller than l
        //     => expect an exception that states this
        final int lagSize = 5;
        BivariateGrangerImpl big = new BivariateGrangerImpl(lagSize);
        double[] testArr = new double[] { .0, -2.1, Math.PI, 22.2 };
        try {
            big.strip(testArr);
            Assert.fail();
        } catch (Exception e) { /* Nothing to do here */ }

        // T2) Stripping of an array should always create a new array (object)
        testArr = new double[] { 0, 0, 0, 0, 0 };
        double[] stripped = big.strip(testArr);
        Assert.assertFalse(stripped.equals(testArr));
        BivariateGrangerImpl big0 = new BivariateGrangerImpl(0);
        stripped = big0.strip(testArr);
        Assert.assertFalse(stripped.equals(testArr));

        // T3) Striping l elements from array with length n, leaves a new array of length n-l
        testArr = new double[] { .0, -2.1, Math.PI, 22.2, -343434 };
        BivariateGrangerImpl big3 = new BivariateGrangerImpl(3);
        stripped = big3.strip(testArr);
        Assert.assertArrayEquals(stripped, new double[]{ 22.2, -343434 }, .0);
    }


    /**
     * Private test class that extends the bivariate Granger-Causality class to gain access to all
     * protected methods.
     */
    private class BivariateGrangerImpl extends GrangerCausalityStrategy_Bivariate {
        public BivariateGrangerImpl(int aLagSize) { super(aLagSize); }
        @Override
        protected double[] strip(double[] a) { return super.strip(a); }
        @Override
        protected double sqrSum(double[] a) { return super.sqrSum(a); }
    }
}
