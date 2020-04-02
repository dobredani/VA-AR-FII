package com.amihaeseisergiu.proiect;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PopUpFrame extends Application {

    Scene scene;
    
    @Override
    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        
        scene = new Scene(pane, 600, 400);
        
        stage.setScene(scene);
        stage.show();
    }
    
}
