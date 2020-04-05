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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopUpFrame extends Application {

    Scene scene;
    ExtendedShape shape;
    double x;
    double y;
    
    public PopUpFrame(ExtendedShape shape, double x, double y)
    {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        
        if(shape instanceof ExtendedRectangle)
        {
            ExtendedRectangle rectangle = (ExtendedRectangle) shape;
            Label info = new Label("Information About Rectangle");
            info.setStyle("-fx-font: 18 arial;");
            
            HBox topHBox = new HBox();
            topHBox.getChildren().addAll(info);
            topHBox.setAlignment(Pos.CENTER);
            topHBox.setPadding(new Insets(10, 10, 10, 10));
            
            Label name = new Label("Name:");
            TextField nameField = new TextField(rectangle.getName());
            HBox nameHBox = new HBox();
            nameHBox.setAlignment(Pos.CENTER);
            nameHBox.setSpacing(10);
            nameHBox.getChildren().addAll(name, nameField);
            VBox centerVBox = new VBox();
            centerVBox.getChildren().addAll(nameHBox);
            centerVBox.setAlignment(Pos.TOP_CENTER);
            centerVBox.setPadding(new Insets(10, 10, 10, 10));
            centerVBox.setSpacing(10);
            centerVBox.setStyle("-fx-border-color: black;\n" +
                   "-fx-border-radius: 5;\n" +
                   "-fx-border-insets: 5;\n" +
                   "-fx-border-width: 3;\n");
            
            nameField.setOnAction(event -> {
                rectangle.setName(nameField.getText());
            });
            
            pane.setTop(topHBox);
            pane.setCenter(centerVBox);
        }
        
        scene = new Scene(pane, 300, 300);
        
        stage.setX(x + 20);
        stage.setY(y - scene.getHeight()/2);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
    }
    
}