package cointegration;

import JSci.maths.statistics.FDistribution;
import JSci.maths.statistics.TDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cointegration.AnalysisConstants.PEARSON_ID;
import static cointegration.AnalysisConstants.SPEARMAN_ID;


public class StatisticsUtil {
    private final  StatisticsUtil stat = new StatisticsUtil();

    private static final double  NO_VALUE = Double.NaN;

    private StatisticsUtil() {
    }

    public StatisticsUtil getInstance() {

        return stat;

    }

    /**
     * 计算数据均值
     * @param values 双精度数组
     * @return 平均值
     */
    public static double computeMean(final double[] values) {
        if (testNull(values)) {
            return NO_VALUE;
        }
        Mean mean = new Mean();
        return mean.evaluate(values);
    }

    /**
     * 计算数据标准差
     * @param values 双精度数组
     * @return 标准差
     */
    public static double computeStandardDeviation(final double[] values) {
        if (testNull(values)) {
            return NO_VALUE;
        }
        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(values);
    }

    /**
     * 获取数据最大值
     * @param values 双精度数组
     * @return 最大值
     */
    public static double getMaxValue(final double[] values) {
        if (testNull(values)) {
            return NO_VALUE;
        }
        Arrays.sort(values);
        return values[values.length - 1];
    }


    /**
     * 获取数据最小值
     * @param values 双精度数组
     * @return 最小值
     */
    public static double getMinValue(final double[] values) {
        if (testNull(values)) {
            return NO_VALUE;
        }
        Arrays.sort(values);
        return values[0];
    }

    /**
     * 获取数据最大值和最小值
     * @param values - 双精度数组
     * @return map<String, Double>
     */
    public static Map<String, Double> getMaxAndMin(final double[] values) {
        Map<String, Double> result = new HashMap<>(16);
        result.put("max", NO_VALUE);
        result.put("min", NO_VALUE);
        if (testNull(values)) {
            return result;
        }
        Arrays.sort(values);
        result.clear();
        result.put("max", values[values.length - 1]);
        result.put("min", values[0]);
        return result;
    }

    /**
     * 计算两个数据之间的相关系数
     * @param xArray -- 双精度数组
     * @param yArray -- 双精度数组
     * @param methodId -- 相关系数方法
     * @return double
     */
    public static double correlation(final double[] xArray, final double[] yArray, String methodId) {

        double value = NO_VALUE;
        if (xArray.length != yArray.length) {
            throw new DimensionMismatchException(xArray.length, yArray.length);
        } else if (xArray.length < 2) {
            return value;
        } else {
            if (PEARSON_ID.equals(methodId)) {
                PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
                value = pearsonsCorrelation.correlation(xArray, yArray);
            } else if (SPEARMAN_ID.equals(methodId)) {
                SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
                value = spearmansCorrelation.correlation(xArray, yArray);
            }

        }
        return value;
    }

    /**
     * 计算两个数据之间的相关系数
     * @param xArray -- 双精度数组
     * @param yArray -- 双精度数组
     * @return double
     */
    public static double correlation(final double[] xArray, final double[] yArray) {
        return correlation(xArray, yArray, PEARSON_ID);
    }


    /**
     * 计算显著性p值算法
     * @param rValue 相关系数
     * @param n 分析数据的个数
     * @param methodId 使用的方法
     * @return p-value
     */
    public static double getPValue(final double rValue, final int n, String methodId) {

        if (n < 2) {
            return NO_VALUE;
        }
        double pValue = NO_VALUE;
        double tValue = ((rValue * Math.sqrt(n-2)) / (Math.sqrt(1 - (rValue * rValue))));
        int free= n-2;
        TDistribution td=new TDistribution(free);
        if (PEARSON_ID.equals(methodId)) {

            double cumulative = td.cumulative(tValue);
            if(tValue>0) {
                pValue=(1-cumulative)*2;
            }else {
                pValue=cumulative*2;
            }

        } else if (SPEARMAN_ID.equals(methodId)) {
            if (n > 500) {
                tValue = (rValue * Math.sqrt(n-1));
            }

            double cumulative = td.cumulative(tValue);
            if(tValue>0) {
                pValue=(1-cumulative)*2;
            }else {
                pValue=cumulative*2;
            }

        }

        return pValue;
    }

    /**
     * T检验 - 计算显著性p值算法
     * @param rValue 相关系数
     * @param n 分析数据的个数
     * @return p-value
     */
    public static double getPValue(final double rValue, final int n) {
        return getPValue(rValue, n, PEARSON_ID);
    }

    /**
     * 判空
     * @param values 双精度数组
     * @return boolean
     */
    private static boolean testNull(final double[] values) {
        return values == null;
    }

    /**
     * F检验 - 计算显著性p值算法
     * @param fValue f检验值 (ESS/K)/(RSS/(n-k-1))
     * @param dgrP X的自由度 即为自变量的个数
     * @param dgrQ Y的自由度 样本数 - 自变量个数 -1
     * @return P-Value
     */
    public static double getPValue(double fValue, int dgrP, int dgrQ) {

        FDistribution fd=new FDistribution(dgrP, dgrQ);

        double cumulative = fd.cumulative(fValue);

        return (1-cumulative);
    }
}
