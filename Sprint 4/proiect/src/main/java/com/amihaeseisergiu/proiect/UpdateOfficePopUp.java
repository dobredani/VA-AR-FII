/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

/**
 *
 * @author Sergiu
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateOfficePopUp extends Application {
    
    Scene scene;
    ExtendedShape shape;
    double x;
    double y;
    
    public UpdateOfficePopUp(ExtendedShape shape, double x, double y)
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
            Label info = new Label("Information About Office");
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
            
            HBox bottomHBox = new HBox();
            bottomHBox.setAlignment(Pos.CENTER);
            bottomHBox.setSpacing(10);
            bottomHBox.setPadding(new Insets(10, 10, 10, 10));
            Button closeBtn = new Button("Save");
            bottomHBox.getChildren().addAll(closeBtn);
            int btnCount = bottomHBox.getChildren().size();
            closeBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));
            
            closeBtn.setOnAction(event -> {
                stage.close();
                rectangle.setName(nameField.getText());
            });
            
            pane.setTop(topHBox);
            pane.setCenter(centerVBox);
            pane.setBottom(bottomHBox);
        }
        
        scene = new Scene(pane, 300, 300);
        
        pane.setStyle("-fx-background-color: transparent;");
        
        stage.setX(x + 10);
        stage.setY(y - scene.getHeight() / 2);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(new Color(0.6, 0.6, 0.6, 0.2));
        stage.setScene(scene);
        stage.show();
    }
}
