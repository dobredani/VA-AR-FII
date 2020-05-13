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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sergiu
 */
public class UpdateStairsPopUp extends Application {

    Scene scene;
    List<ExtendedShape> stairs = new ArrayList<>();
    ExtendedShape shape;
    double x;
    double y;
    DrawingPanel drawingPanel;

    public UpdateStairsPopUp(DrawingPanel drawingPanel, List<ExtendedShape> stairs, ExtendedShape shape, double x, double y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.stairs = stairs;
        this.drawingPanel = drawingPanel;
    }

    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();

        if (shape instanceof ExtendedRectangle) {
            ExtendedRectangle rectangle = (ExtendedRectangle) shape;
            Label info = new Label("Information About Stairs");
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

            List<String> stairsNames = new ArrayList<>();

            for (ExtendedShape s : stairs) {
                if (!(stairsNames.contains(((ExtendedRectangle) s).getName()))) {
                    stairsNames.add(((ExtendedRectangle) s).getName());
                }
            }

            ObservableList<String> options = FXCollections.observableArrayList(stairsNames);
            final ComboBox comboBox = new ComboBox(options);
            VBox centerVBox = new VBox();
            centerVBox.getChildren().addAll(nameHBox, comboBox);
            
            Label widthLabel = new Label("Width: ");
            Label heightLabel = new Label("Height: ");
            TextField widthField = new TextField(String.valueOf(((ExtendedRectangle)shape).getWidth()));
            TextField heightField = new TextField(String.valueOf(((ExtendedRectangle)shape).getLength()));
            HBox width = new HBox();
            width.getChildren().addAll(widthLabel, widthField);
            HBox height = new HBox();
            height.getChildren().addAll(heightLabel, heightField);
            centerVBox.getChildren().addAll(width, height);
            width.setAlignment(Pos.CENTER);
            height.setAlignment(Pos.CENTER);
            
            centerVBox.setAlignment(Pos.TOP_CENTER);
            centerVBox.setPadding(new Insets(10, 10, 10, 10));
            centerVBox.setSpacing(10);
            centerVBox.setStyle("-fx-border-color: black;\n"
                    + "-fx-border-radius: 5;\n"
                    + "-fx-border-insets: 5;\n"
                    + "-fx-border-width: 3;\n");

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
                if (comboBox.getValue() != null) {
                    rectangle.setName(comboBox.getValue().toString());
                } else {
                    rectangle.setName(nameField.getText());
                }
                
                drawingPanel.deleteShape(rectangle);
                double initialWidth = rectangle.getWidth();
                double initialHeight = rectangle.getLength();
                rectangle.setLength(Double.valueOf(heightField.getText()));
                rectangle.setWidth(Double.valueOf(widthField.getText()));
                rectangle.getRectangle().setSize((int) rectangle.getWidth(), (int) rectangle.getLength());
                if (drawingPanel.checkCollision(rectangle, 0) == true || rectangle.getLength() < 50 || rectangle.getWidth() < 50) {
                    rectangle.setLength(initialHeight);
                    rectangle.setWidth(initialWidth);
                    rectangle.getRectangle().setSize((int)rectangle.getWidth(), (int)rectangle.getLength());
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
        }

        scene = new Scene(pane, 300, 300);

        pane.setStyle("-fx-background-color: transparent;");

        Rectangle2D screenBoundsTest = Screen.getPrimary().getBounds();
        if(x + 10 + scene.getWidth() > screenBoundsTest.getWidth())
        {
            x = x - ((x + 10 + scene.getWidth()) - screenBoundsTest.getWidth());
        }
        if(y + scene.getHeight() / 2 > screenBoundsTest.getHeight())
        {
            y = screenBoundsTest.getHeight() - (scene.getHeight() / 2) - 50;
        }
        if(y - scene.getHeight() / 2 < 0)
        {
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
