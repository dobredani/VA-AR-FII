package com.amihaeseisergiu.proiect;

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
    
    public MainFrame(Stage stage)
    {
        this.stage = stage;
        
        BorderPane pane = new BorderPane();
        configPanel = new ConfigPanel();
        controlPanel = new ControlPanel();
        drawingPanel = new DrawingPanel();
        
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
}
