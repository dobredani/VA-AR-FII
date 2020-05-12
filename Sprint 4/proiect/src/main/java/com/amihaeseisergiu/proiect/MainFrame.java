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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainFrame {

    Stage stage;
    Scene scene;
    DrawingPanel drawingPanel;
    ConfigPanel configPanel;
    ControlPanel controlPanel;
    SavePanel savePanel;
    Building building;

    public MainFrame(Stage stage, Building build, boolean state) {
        this.stage = stage;
        this.building = build;
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: linear-gradient(#4facfe, #00f2fe)");
        
        configPanel = new ConfigPanel(this);
        drawingPanel = new DrawingPanel(this);
        if (build.floors != null) {
            build.floors.entrySet().forEach((floor) -> {
            for(ExtendedShape s : floor.getValue().getShapes()) {
                drawingPanel.getIds().add(((ExtendedRectangle)s).getId());
            }
            });
        }
        savePanel = new SavePanel(this);
        controlPanel = new ControlPanel(this, building, state);
        pane.setTop(configPanel);
        pane.setLeft(controlPanel);
        pane.setCenter(drawingPanel);
        pane.setBottom(savePanel);
        
        drawingPanel.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        configPanel.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        
        controlPanel.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        
        savePanel.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");

        BorderPane.setMargin(drawingPanel, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(configPanel, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(controlPanel, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(savePanel, new Insets(5, 5, 5, 5));
        
        scene = new Scene(pane, 800, 600);
        
        CustomAnimation.animateInFromLeftWithBounceSmall(scene.getWidth(), controlPanel);
        CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), drawingPanel);
        CustomAnimation.animateInFromTopWithBounceSmall(scene.getHeight(), configPanel);
        CustomAnimation.animateInFromBottomWithBounceSmall(scene.getHeight(), savePanel);
    }

    public void init() {
        stage.setScene(scene);
        //stage.setAlwaysOnTop(true);
        //stage.setFullScreen(true);
        stage.show();
        
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void setDrawingPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public ConfigPanel getConfigPanel() {
        return configPanel;
    }

    public void setConfigPanel(ConfigPanel configPanel) {
        this.configPanel = configPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

}
