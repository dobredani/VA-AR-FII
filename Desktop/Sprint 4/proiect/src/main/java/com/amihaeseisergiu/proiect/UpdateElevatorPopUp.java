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

public class UpdateElevatorPopUp extends Application {

    Scene scene;
    ExtendedShape shape;
    double x;
    double y;
    DrawingPanel drawingPanel;
    List<Hallway> hallways;
    List<ExtendedShape> elevators;

    public UpdateElevatorPopUp(DrawingPanel drawingPanel, List<ExtendedShape> elevators, ExtendedShape shape, double x, double y, List<Hallway> hallways) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.drawingPanel = drawingPanel;
        this.hallways = hallways;
        this.elevators = elevators;
    }

    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: linear-gradient(#4facfe, #00f2fe)");

        ExtendedRectangle rectangle = (ExtendedRectangle) shape;
        Label info = new Label("Information About Elevator");
        info.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));

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

        Label name = new Label("Name:");
        TextField nameField = new TextField(rectangle.getName());
        nameField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        HBox nameHBox = new HBox();
        nameHBox.setAlignment(Pos.CENTER);
        nameHBox.setSpacing(10);
        nameHBox.getChildren().addAll(name, nameField);
        VBox centerVBox = new VBox();
        centerVBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        centerVBox.getChildren().addAll(nameHBox);

        List<String> elevatorNames = new ArrayList<>();

        for (ExtendedShape s : elevators) {
            if (!(elevatorNames.contains(((ExtendedRectangle) s).getName()))) {
                elevatorNames.add(((ExtendedRectangle) s).getName());
            }
        }

        List<String> hallwaysNames = new ArrayList<>();
        for (Hallway h : hallways) {
            hallwaysNames.add(h.getName());
        }
        ObservableList<String> optionsHallways = FXCollections.observableArrayList(hallwaysNames);
        ObservableList<String> optionsElevators = FXCollections.observableArrayList(elevatorNames);
        final ComboBox comboBoxHallways = new ComboBox(optionsHallways);
        final ComboBox comboBoxElevators = new ComboBox(optionsElevators);
        comboBoxHallways.setMaxWidth(100);
        comboBoxHallways.setStyle("-fx-background-color: transparent;"
                + "-fx-border-color: #ffff00;"
                + "-fx-border-width: 3;"
        );
        comboBoxElevators.setMaxWidth(100);
        comboBoxElevators.setStyle("-fx-background-color: transparent;"
                + "-fx-border-color: #ffff00;"
                + "-fx-border-width: 3;"
        );
        HBox hallwayHBox = new HBox();
        Label hallwayLabel = new Label("Opens to ");
        hallwayHBox.getChildren().addAll(hallwayLabel, comboBoxHallways);
        hallwayHBox.setAlignment(Pos.CENTER);

        Label widthLabel = new Label("Width: ");
        Label heightLabel = new Label("Height: ");
        TextField widthField = new TextField(String.valueOf(((ExtendedRectangle) shape).getWidth()));
        widthField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        TextField heightField = new TextField(String.valueOf(((ExtendedRectangle) shape).getLength()));
        heightField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        HBox width = new HBox();
        width.setAlignment(Pos.CENTER);
        width.setSpacing(10);
        width.getChildren().addAll(widthLabel, widthField);
        HBox height = new HBox();
        height.setAlignment(Pos.CENTER);
        height.setSpacing(10);
        height.getChildren().addAll(heightLabel, heightField);
        centerVBox.getChildren().addAll(comboBoxElevators, hallwayHBox, width, height);
        width.setAlignment(Pos.CENTER);
        height.setAlignment(Pos.CENTER);

        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setPadding(new Insets(10, 10, 10, 10));
        centerVBox.setSpacing(10);

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
        Button closeBtn = new Button("Save");
        closeBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
        closeBtn.setSkin(new FadeButtonSkin(closeBtn));
        bottomHBox.getChildren().addAll(closeBtn);
        int btnCount = bottomHBox.getChildren().size();
        closeBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));

        closeBtn.setOnAction(event -> {
            stage.close();
            if (comboBoxElevators.getValue() != null) {
                boolean ok = true;
                for (ExtendedShape shape : drawingPanel.getShapes()) {
                    if (((ExtendedRectangle) shape).getName().equals(comboBoxElevators.getValue().toString())) {
                        ok = false;
                    }
                }
                if (ok == true) {
                    rectangle.setName(comboBoxElevators.getValue().toString());
                }
            } else {
                boolean ok = true;
                for (ExtendedShape shape : drawingPanel.getShapes()) {
                    if (((ExtendedRectangle) shape).getName().equals(nameField.getText())) {
                        ok = false;
                    }
                }
                if (ok == true && !nameField.getText().isBlank()) {
                    rectangle.setName(nameField.getText());
                }
            }
            if (comboBoxHallways.getValue() != null) {
                for (Hallway h : hallways) {
                    if (h.getName().equals(comboBoxHallways.getValue())) {
                        ((ExtendedRectangle) shape).setHallway(h);
                        drawingPanel.setOrder();
                    }
                }
            }
            drawingPanel.deleteShape(rectangle);
            double initialWidth = rectangle.getWidth();
            double initialHeight = rectangle.getLength();
            rectangle.setLength(Double.valueOf(heightField.getText()));
            rectangle.setWidth(Double.valueOf(widthField.getText()));
            rectangle.setLength((int) (rectangle.getLength()));
            rectangle.setWidth((int) (rectangle.getWidth()));
            rectangle.getRectangle().setSize((int) rectangle.getWidth(), (int) rectangle.getLength());
            if (drawingPanel.checkCollision(rectangle, 0) == true || rectangle.getLength() < 50 || rectangle.getWidth() < 50) {
                rectangle.setLength(initialHeight);
                rectangle.setWidth(initialWidth);
                rectangle.getRectangle().setSize((int) rectangle.getWidth(), (int) rectangle.getLength());
                drawingPanel.drawAll();
            } else {
                drawingPanel.deleteShapeFromGraph(rectangle);
                drawingPanel.getShapes().add(rectangle);
                drawingPanel.addShapeToGraph(rectangle);
                drawingPanel.setOrder();
                drawingPanel.drawAll();
                drawingPanel.getIds().add(rectangle.getId());
            }
            rectangle.setStartPoint(new Point(rectangle.getCenterPoint().getX() + rectangle.getWidth() / 2, rectangle.getCenterPoint().getY() + rectangle.getLength() / 2));
            rectangle.getRectangle().setBounds((int) rectangle.getCenterPoint().getX(), (int) rectangle.getCenterPoint().getY(), (int) rectangle.getWidth(), (int) rectangle.getLength());
        });

        pane.setTop(topHBox);
        pane.setCenter(centerVBox);
        pane.setBottom(bottomHBox);

        BorderPane.setMargin(topHBox, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(centerVBox, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(bottomHBox, new Insets(5, 5, 5, 5));

        scene = new Scene(pane, 300, 400);

        CustomAnimation.animateInFromLeftWithBounceSmall(scene.getWidth(), centerVBox);
        CustomAnimation.animateInFromTopWithBounceSmall(scene.getHeight(), topHBox);
        CustomAnimation.animateInFromBottomWithBounceSmall(scene.getHeight(), bottomHBox);

        Rectangle2D screenBoundsTest = Screen.getPrimary().getBounds();
        if (x + 10 + scene.getWidth() > screenBoundsTest.getWidth()) {
            x = x - ((x + 10 + scene.getWidth()) - screenBoundsTest.getWidth());
        }
        if (y + scene.getHeight() / 2 > screenBoundsTest.getHeight()) {
            y = screenBoundsTest.getHeight() - (scene.getHeight() / 2) - 50;
        }
        if (y - scene.getHeight() / 2 < 0) {
            y = scene.getHeight() / 2;
        }
        stage.setX(x + 10);
        stage.setY(y - scene.getHeight() / 2);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(new Color(0.6, 0.6, 0.6, 0.2));
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
