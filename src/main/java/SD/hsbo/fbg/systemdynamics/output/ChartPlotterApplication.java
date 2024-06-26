package SD.hsbo.fbg.systemdynamics.output;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles the chart plotting.
 *
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class ChartPlotterApplication extends Application {

    private static ArrayList<Series> series;
    private static double width = 800;
    private static double height = 600;

    @Override
    public void start(Stage stage) {

        // create root group
        Group root = new Group();

        // create scene
        Scene scene = new Scene(root, width, height);

        //creating the chart
        LineChart lineChart = this.createLineChart("System Dynamics Chart");
        lineChart.getData().addAll(series);

        BorderPane borderPane = new BorderPane();
        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(lineChart);

        // add border pane to root
        root.getChildren().add(borderPane);

        // save to file
        ChartPlotterApplication.saveToFile(scene);
        // stop application
        Platform.exit();
    }

    /**
     * Method to add series to the chart.
     *
     * @param modelEntityNames list of model entity names.
     */
    public static void addSeries(List<String> modelEntityNames) {
        ChartPlotterApplication.series = new ArrayList<>();
        for (String modelEntityName : modelEntityNames) {
            Series s = new Series();
            s.setName(modelEntityName);
            ChartPlotterApplication.series.add(s);
        }
    }

    /**
     * Method to add values to the chart series.
     *
     * @param modelEntityValues list of model entity values.
     * @param currentTime       current model time.
     */
    public static void addValues(List<String> modelEntityValues, double currentTime) {
        for (int i = 0; i < modelEntityValues.size(); i++) {
            String valueString = modelEntityValues.get(i);
            NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
            Number number;
            try {
                number = format.parse(valueString);
                double value = number.doubleValue();
                ChartPlotterApplication.series.get(i).getData().add(new XYChart.Data(currentTime, value));
            } catch (ParseException ex) {
                Logger.getLogger(ChartViewerApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Method to create a LineChart.
     *
     * @param title line chart title
     * @return line chart
     */
    private LineChart createLineChart(String title) {
        //defining the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Timestep");
        yAxis.setLabel("Value");
        //creating the chart
        LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);

        // disable chart animation
        lineChart.setAnimated(false);

        lineChart.setTitle(title);
        return lineChart;
    }

    /**
     * Set scene width and height.
     *
     * @param width  width.
     * @param height height.
     */
    public static void setSize(double width, double height) {
        ChartPlotterApplication.width = width;
        ChartPlotterApplication.height = height;
    }

    /**
     * Method to save the chart as an image.
     *
     * @param scene Scene
     */
    private static void saveToFile(Scene scene) {
        WritableImage image = scene.snapshot(null);
        File outputFile = new File("chart.png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
