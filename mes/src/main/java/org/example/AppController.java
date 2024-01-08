package org.example;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController{
    public CheckBox showSymbols;
    @FXML
    private LineChart<Number, Number> plot;

    @FXML
    private TextField pointsNumberField;

    private Calculator calculator = new Calculator();
    @FXML
    public void buttonAction(){
        try{
            Integer pointsNumber = Integer.parseInt(pointsNumberField.getText());
            calculate(pointsNumber);
        }
        catch(NumberFormatException exception){exception.printStackTrace();}
    }

    private void calculate(int n) {
        double[] vectorW = this.calculator.solve(n);
        double h=3.0/n;
        XYChart.Series<Number, Number> data = new XYChart.Series<>();
        data.getData().add(new XYChart.Data<>(0.0, 5.0));
        data.getData().add(new XYChart.Data<>(3.0, 4.0));
        for (int i = 0; i < vectorW.length; i++) {
            double x=h*(i+1);
            double y=5-x/3+vectorW[i];
            data.getData().add(new XYChart.Data<>(x, y));
        }
        if (!this.plot.getData().isEmpty()) {
            this.plot.getData().remove(0);
        }
        this.plot.setCreateSymbols(showSymbols.isSelected());
        this.plot.getData().add(0,data);
    }

}