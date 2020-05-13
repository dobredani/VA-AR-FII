/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

/**
 *
 * @author Alex
 */
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Sergiu
 */
public class ConfigPanel extends HBox {

    MainFrame mainFrame;
    Label widthLabel;
    Label heightLabel;
    TextField widthTextField;
    TextField heightTextField;
    Slider widthSlider;
    Slider heightSlider;
    ColorPicker colorPicker;

    public ConfigPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        widthLabel = new Label("Width:");
        widthLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        heightLabel = new Label("Heigth:");
        heightLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        widthSlider = new Slider(1, 200, 0.1);
        widthSlider.setStyle("-fx-control-inner-background: #03b6fc;");
        heightSlider = new Slider(1, 200, 0.1);
        heightSlider.setStyle("-fx-control-inner-background: #03b6fc;");
        widthSlider.setMaxWidth(50);
        heightSlider.setMaxWidth(50);
        widthTextField = new TextField("50");
        widthTextField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        heightTextField = new TextField("50");
        heightTextField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        widthTextField.setMaxWidth(50);
        heightTextField.setMaxWidth(50);
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        this.getChildren().addAll(widthLabel, widthTextField, widthSlider, heightLabel, heightTextField, heightSlider, colorPicker);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(10);

        widthSlider.valueProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            widthTextField.setText(String.valueOf(newValue.intValue()));
        });

        widthSlider.valueProperty().addListener((obs, oldval, newVal) -> widthSlider.setValue(Math.round(newVal.intValue())));

        heightSlider.valueProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            heightTextField.setText(String.valueOf(newValue.intValue()));
        });

        heightSlider.valueProperty().addListener((obs, oldval, newVal) -> heightSlider.setValue(Math.round(newVal.intValue())));

        widthTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                widthTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (!widthTextField.getText().isBlank() && Integer.valueOf(widthTextField.getText()) % 2 != 0) {
                widthTextField.setText(String.valueOf(Integer.valueOf(widthTextField.getText()) + 1));
            }
            if (Integer.valueOf(widthTextField.getText()) < 50) {
                widthTextField.setText(String.valueOf(Integer.valueOf("50")));
            }
            if (!widthTextField.getText().isBlank() && (widthTextField.getText().length() > 3 || Integer.valueOf(widthTextField.getText()) > 200)) {
                String s = widthTextField.getText().substring(0, widthTextField.getText().length() - 1);
                widthTextField.setText(s);
            }
            if (!widthTextField.getText().isBlank() && widthTextField.getText().equals("0")) {
                widthTextField.setText("1");
            }
            mainFrame.controlPanel.comboBox.setMaxWidth(Double.MAX_VALUE);
        });

        heightTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                heightTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (!heightTextField.getText().isBlank() && Integer.valueOf(heightTextField.getText()) % 2 != 0) {
                heightTextField.setText(String.valueOf(Integer.valueOf(heightTextField.getText()) + 1));
            }
            if (Integer.valueOf(heightTextField.getText()) < 50) {
                heightTextField.setText(String.valueOf(Integer.valueOf("50")));
            }
            if (!heightTextField.getText().isBlank() && (heightTextField.getText().length() > 3 || Integer.valueOf(heightTextField.getText()) > 200)) {
                String s = heightTextField.getText().substring(0, heightTextField.getText().length() - 1);
                heightTextField.setText(s);
            }
            if (!heightTextField.getText().isBlank() && heightTextField.getText().equals("0")) {
                heightTextField.setText("1");
            }
        });

    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public Label getWidthLabel() {
        return widthLabel;
    }

    public void setWidthLabel(Label widthLabel) {
        this.widthLabel = widthLabel;
    }

    public Label getHeightLabel() {
        return heightLabel;
    }

    public void setHeightLabel(Label heightLabel) {
        this.heightLabel = heightLabel;
    }

    public TextField getWidthTextField() {
        return widthTextField;
    }

    public void setWidthTextField(TextField widthTextField) {
        this.widthTextField = widthTextField;
    }

    public TextField getHeightTextField() {
        return heightTextField;
    }

    public void setHeightTextField(TextField heightTextField) {
        this.heightTextField = heightTextField;
    }

    public Slider getWidthSlider() {
        return widthSlider;
    }

    public void setWidthSlider(Slider widthSlider) {
        this.widthSlider = widthSlider;
    }

    public Slider getHeightSlider() {
        return heightSlider;
    }

    public void setHeightSlider(Slider heightSlider) {
        this.heightSlider = heightSlider;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

}
