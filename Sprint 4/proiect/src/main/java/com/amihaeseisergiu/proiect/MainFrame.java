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

    public MainFrame(Stage stage, Building build) {
        this.stage = stage;
        this.building = build;

        BorderPane pane = new BorderPane();
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
        controlPanel = new ControlPanel(this, building);
        pane.setTop(configPanel);
        pane.setLeft(controlPanel);
        pane.setCenter(drawingPanel);
        pane.setBottom(savePanel);

        scene = new Scene(pane, 800, 600);
    }

    public void init() {
        stage.setScene(scene);
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
