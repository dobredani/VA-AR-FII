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
    Building building;
    
    public MainFrame(Stage stage)
    {
        this.stage = stage;
        building = new Building();
        BorderPane pane = new BorderPane();
        configPanel = new ConfigPanel(this);
        controlPanel = new ControlPanel(this, building);
        drawingPanel = new DrawingPanel(this);
        
        pane.setTop(configPanel);
        pane.setLeft(controlPanel);
        pane.setCenter(drawingPanel);
        
        scene = new Scene(pane, 800, 600);
    }
    
    public void init()
    {
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