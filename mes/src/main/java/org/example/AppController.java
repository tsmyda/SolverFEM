package org.example;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController{
    @FXML
    private LineChart<Number, Number> plot;

    @FXML
    private TextField fieldN;

    @FXML
    public void buttonAction(){
        try{
            System.out.println("Hello world!");
        }
        catch(NumberFormatException exception){exception.printStackTrace();}
    }

}