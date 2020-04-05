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