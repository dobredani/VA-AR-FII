package com.amihaeseisergiu.proiect;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage)
    {
        MainFrame mainFrame = new MainFrame(stage);
        mainFrame.init();
    }

    public static void main(String[] args) {
        launch();
    }

}