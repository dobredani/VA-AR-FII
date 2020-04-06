/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sergiu
 */
public class UpdateFloorPopUp extends Application {
    
    Scene scene;
    VBox panel;
    Button floor;
    AtomicBoolean popupOpen;
    Label nrFloorsLabel;
    double x;
    double y;
    
    public UpdateFloorPopUp(VBox panel, Button floor, Label nrFloorsLabel, AtomicBoolean popupOpen, double x, double y)
    {
        this.panel = panel;
        this.floor = floor;
        this.nrFloorsLabel = nrFloorsLabel;
        this.popupOpen = popupOpen;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        
        Label info = new Label("Information About " + floor.getText());
        info.setStyle("-fx-font: 18 arial;");

        HBox topHBox = new HBox();
        topHBox.getChildren().addAll(info);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(10, 10, 10, 10));

        Label name = new Label("Name:");
        TextField nameField = new TextField(floor.getText());
        name.setMaxWidth(Double.MAX_VALUE);
        nameField.setMaxWidth(Double.MAX_VALUE);
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

        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.setSpacing(10);
        bottomHBox.setPadding(new Insets(10, 10, 10, 10));
        Button saveBtn = new Button("Save");
        Button deleteBtn = new Button("Delete Floor");
        bottomHBox.getChildren().addAll(saveBtn, deleteBtn);
        
        int btnCount = bottomHBox.getChildren().size();
        saveBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));
        deleteBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));
        
        saveBtn.setOnAction(event -> {
            floor.setText(nameField.getText());
            popupOpen.set(false);
            stage.close();
        });
        
        deleteBtn.setOnAction(event -> {
            panel.getChildren().remove(floor);
            nrFloorsLabel.setText("Floors: " + panel.getChildren().size());
            popupOpen.set(false);
            stage.close();
        });

        pane.setTop(topHBox);
        pane.setCenter(centerVBox);
        pane.setBottom(bottomHBox);
        
        scene = new Scene(pane, 300, 200);
        
        pane.setStyle("-fx-background-color: transparent;");
        
        Bounds bounds = floor.getBoundsInLocal();
        Bounds screenBounds = floor.localToScreen(bounds);
        int x = (int) screenBounds.getMaxX();
        stage.setX(x + 10);
        stage.setY(y - scene.getHeight()/2);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(new Color(0.6, 0.6, 0.6, 0.2));
        stage.setScene(scene);
        stage.show();
    }
    
}
