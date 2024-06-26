package SD;

import SD.hsbo.fbg.systemdynamics.exceptions.ModelException;
import SD.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import SD.hsbo.fbg.systemdynamics.functions.IFunction;
import SD.hsbo.fbg.systemdynamics.model.*;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Test class for different types of functions.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 */
public class FunctionTest {

    private final String POPULATION_KEY = "Population";
    private final String BIRTHS_KEY = "Births";
    private final String BIRTH_RATE_KEY = "BirthRate";
    private final String DEATHS_KEY = "Daeths";

    private Model model;
    Stock population;
    Variable birthRate;
    Flow births;
    Flow deaths;

    @Before
    public void prepareValues() {

        model = new Model(1, 12, 1, new EulerCauchyIntegration());
        try {
            population = (Stock) model.createModelEntity(ModelEntityType.STOCK, POPULATION_KEY);
            birthRate = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, BIRTH_RATE_KEY);
            births = (Flow) model.createModelEntity(ModelEntityType.FLOW, BIRTHS_KEY);
            deaths = (Flow) model.createModelEntity(ModelEntityType.FLOW, DEATHS_KEY);
            population.addInputFlows(births);
            population.addOutputFlows(deaths);
            population.setInitialValue(5000);
            population.setChangeRateFunction(
                    () -> births.getCurrentValue()
                            - deaths.getCurrentValue());

            deaths.setInitialValue(100);
            birthRate.setInitialValue(0.1);


        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void algebraicFunctionTest() {
        Converter birthsConverter = model.createConverter(births, birthRate, population);
        birthsConverter.setFunction(new IFunction() {
            @Override
            public double calculateEntityValue() {
                double result = birthRate.getCurrentValue()
                        * population.getCurrentValue();
                return result;
            }
        });
        Simulation simulation = new Simulation(model);
        simulation.run();

        HashMap<String, ModelEntity> entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_KEY).getCurrentValue(), Matchers.closeTo(12412.5, 0.1));
        Assert.assertThat(entities.get(BIRTHS_KEY).getCurrentValue(), Matchers.closeTo(1241.25, 0.1));
        Assert.assertThat(entities.get(BIRTH_RATE_KEY).getCurrentValue(), Matchers.equalTo(0.1));
        Assert.assertThat(entities.get(DEATHS_KEY).getCurrentValue(), Matchers.equalTo(100.));
    }

    @Test
    public void logicFunctionTest() {
        Converter birthsConverter = model.createConverter(births, birthRate, population);
        birthsConverter.setFunction(() -> {
            if (population.getCurrentValue() >= 10000)
                return 0;
            else {
                double result = birthRate.getCurrentValue()
                        * population.getCurrentValue();
                return result;
            }
        });
        Simulation simulation = new Simulation(model);
        simulation.run();
        HashMap<String, ModelEntity> entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_KEY).getCurrentValue(), Matchers.closeTo(10231.8, 0.1));
        Assert.assertThat(entities.get(BIRTHS_KEY).getCurrentValue(), Matchers.equalTo(0.0));
        Assert.assertThat(entities.get(BIRTH_RATE_KEY).getCurrentValue(), Matchers.equalTo(0.1));
        Assert.assertThat(entities.get(DEATHS_KEY).getCurrentValue(), Matchers.equalTo(100.));
    }

    @Test
    public void lookUpTableTest() {
        Converter birthsConverter = model.createConverter(births, birthRate, population);
        birthsConverter.setFunction(new IFunction() {
            @Override
            public double calculateEntityValue() {
                double result = birthRate.getCurrentValue()
                        * population.getCurrentValue();
                return result;
            }
        });
        Converter birthsRateConverter = model.createConverter(birthRate);
        TreeMap<Integer, Double> lookupTable = new TreeMap<Integer, Double>();
        lookupTable.put(1, 0.1);
        lookupTable.put(2, 0.15);
        lookupTable.put(3, 0.2);
        lookupTable.put(4, 0.25);
        lookupTable.put(5, 0.3);
        lookupTable.put(6, 0.35);
        lookupTable.put(7, 0.4);
        lookupTable.put(8, 0.45);
        lookupTable.put(9, 0.4);
        lookupTable.put(10, 0.35);
        lookupTable.put(11, 0.3);
        lookupTable.put(12, 0.25);

        birthsRateConverter.setFunction(new IFunction() {
            @Override
            public double calculateEntityValue() {
                double currentTime = model.getCurrentTime();
                double floorKey = lookupTable.floorKey((int) currentTime);
                double floor = lookupTable.floorEntry((int) currentTime).getValue();
                double ceilingKey = lookupTable.ceilingKey((int) currentTime);
                double ceiling = lookupTable.ceilingEntry((int) currentTime).getValue();
                if (floorKey == ceilingKey)
                    return floor;
                else {
                    double result = floor + (ceiling - floor)
                            / (ceilingKey - floorKey) * (currentTime - ceilingKey);
                    return result;
                }
            }
        });
        Simulation simulation = new Simulation(model);
        simulation.run();

        HashMap<String, ModelEntity> entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_KEY).getCurrentValue(), Matchers.closeTo(76076.1, 0.1));
        Assert.assertThat(entities.get(BIRTHS_KEY).getCurrentValue(), Matchers.closeTo(19019, 0.1));
        Assert.assertThat(entities.get(BIRTH_RATE_KEY).getCurrentValue(), Matchers.equalTo(0.25));
        Assert.assertThat(entities.get(DEATHS_KEY).getCurrentValue(), Matchers.equalTo(100.));
    }
}
