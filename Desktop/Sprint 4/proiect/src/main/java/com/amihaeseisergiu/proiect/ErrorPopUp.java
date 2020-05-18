/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorPopUp extends Application {

    Scene scene;
    double x;
    double y;
    String errorMessage;

    public ErrorPopUp(double x, double y, String errorMessage) {
        this.x = x;
        this.y = y;
        this.errorMessage = errorMessage;
    }

    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: linear-gradient(#4facfe, #00f2fe)");
        Label info = new Label("Error");
        info.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));

        Label errorLabel = new Label(errorMessage);
        errorLabel.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        
        VBox centerVBox = new VBox(errorLabel);
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setPadding(new Insets(5, 5, 5, 5));
        centerVBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        
        HBox topHBox = new HBox();
        topHBox.getChildren().addAll(info);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(10, 10, 10, 10));
        topHBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");

        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.setSpacing(10);
        bottomHBox.setPadding(new Insets(10, 10, 10, 10));
        bottomHBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        Button closeBtn = new Button("Confirm");
        closeBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
        closeBtn.setSkin(new FadeButtonSkin(closeBtn));
        bottomHBox.getChildren().addAll(closeBtn);
        int btnCount = bottomHBox.getChildren().size();
        closeBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));

        closeBtn.setOnAction(event -> {
            stage.close();
        });

        pane.setTop(topHBox);
        pane.setBottom(bottomHBox);
        pane.setCenter(centerVBox);
        
        BorderPane.setMargin(topHBox, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(bottomHBox, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(centerVBox, new Insets(5, 5, 5, 5));

        scene = new Scene(pane, 300, 170);

        CustomAnimation.animateInFromTopWithBounceSmall(scene.getHeight(), topHBox);
        CustomAnimation.animateInFromBottomWithBounceSmall(scene.getHeight(), bottomHBox);
        CustomAnimation.animateInFromLeftWithBounceSmall(scene.getHeight(), centerVBox);

        Rectangle2D screenBoundsTest = Screen.getPrimary().getBounds();
        if (x + scene.getWidth() / 2 > screenBoundsTest.getWidth()) {
            x = x - ((x + scene.getWidth() / 2) - screenBoundsTest.getWidth());
        }
        
        if (x - scene.getWidth() / 2 < 0) {
            x = scene.getWidth() / 2;
        }
        
        if (y + scene.getHeight() / 2 > screenBoundsTest.getHeight()) {
            y = screenBoundsTest.getHeight() - (scene.getHeight() / 2) - 50;
        }
        if (y - scene.getHeight() / 2 < 0) {
            y = scene.getHeight() / 2;
        }
        stage.setX(x - scene.getWidth() / 2);
        stage.setY(y - scene.getHeight() / 2);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(new Color(0.6, 0.6, 0.6, 0.2));
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
