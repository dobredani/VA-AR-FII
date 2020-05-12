/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

/**
 * @author Alex
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControlPanel extends VBox {

    ComboBox comboBox;
    Button eraserBtn;
    boolean erase;
    MainFrame frame;
    List<Integer> existingFloors = new ArrayList<>();
    Building building;
    boolean initiateFloor;

    public ControlPanel(MainFrame frame, Building building, boolean state) {
        this.frame = frame;
        this.building = building;

        eraserBtn = new Button("Eraser: Off");
        eraserBtn.setSkin(new FadeButtonSkin(eraserBtn));
        eraserBtn.setStyle("-fx-background-color: #ffff00;");
        comboBox = new ComboBox();
        comboBox.setStyle("-fx-background-color: transparent;"
                + "-fx-border-color: #ffff00;"
                + "-fx-border-width: 3;"
        );

        ScrollPane addFloorScrollPane = new ScrollPane();
        Label nrFloorsLabel = new Label("Floors: 0");
        VBox addFloorVBox = new VBox();
        Button addFloorBtn = new Button("Add Floor");
        addFloorBtn.setMaxWidth(Double.MAX_VALUE);
        addFloorBtn.setSkin(new FadeButtonSkin(addFloorBtn));
        addFloorBtn.setStyle("-fx-background-color: #ffff00;");
        addFloorScrollPane.setContent(addFloorVBox);
        addFloorScrollPane.setStyle("-fx-background-color:transparent;");
        addFloorScrollPane.setFitToWidth(true);
        addFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        addFloorVBox.setAlignment(Pos.TOP_CENTER);
        addFloorVBox.setSpacing(10);
        addFloorVBox.setPadding(new Insets(5, 5, 5, 5));
        addFloorVBox.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");


        comboBox.setItems(FXCollections.observableArrayList(new String[]{"Class Room", "Hall Way", "Office", "Bathroom", "Stairs", "Elevator"}));
        comboBox.getSelectionModel().select(0);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        eraserBtn.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(eraserBtn, comboBox, nrFloorsLabel, addFloorBtn, addFloorScrollPane);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(10);
        
        addFloorBtn.setOnAction(event -> {
            AtomicBoolean popupOpen = new AtomicBoolean(false);
            int level = 0;
            while (existingFloors.contains(level)) {
                level++;
            }
            existingFloors.add(level);
            Button newFloorBtn = new Button("Floor " + level);
            Floor floor = new Floor(level);
            frame.building.getFloors().put(level, floor);
            newFloorBtn.setMaxWidth(Double.MAX_VALUE);
            newFloorBtn.setStyle("-fx-background-color: #ff6600;");
            newFloorBtn.setSkin(new FadeButtonSkin(newFloorBtn));
            addFloorVBox.getChildren().add(newFloorBtn);
            nrFloorsLabel.setText("Floors: " + (addFloorVBox.getChildren().size()));

            if (addFloorVBox.getChildren().size() >= 1) {
                frame.drawingPanel.getCanvas().setVisible(true);
                frame.drawingPanel.setStyle("-fx-background-color: white");
            } else {
                frame.drawingPanel.getCanvas().setVisible(false);
                frame.drawingPanel.setStyle("");
            }

            CustomAnimation.animateInFromRightWithBounceSmall(addFloorVBox.getWidth(), newFloorBtn);
            newFloorBtn.setOnMousePressed(event2 -> {
                if (event2.isPrimaryButtonDown()) {
                    String nrFloor = newFloorBtn.getText().substring(6, 7);
                    Floor f = frame.building.getFloors().get(Integer.parseInt(nrFloor));
                    frame.drawingPanel.setGraph(f.getGraph());
                    frame.drawingPanel.setShapes(f.getShapes());
                    frame.drawingPanel.deleteAllShapes();
                    frame.drawingPanel.drawAll();
                } else if (event2.isSecondaryButtonDown() && !popupOpen.get()) {
                    popupOpen.set(true);
                    UpdateFloorPopUp popup = new UpdateFloorPopUp(frame, Integer.parseInt(newFloorBtn.getText().substring(6, 7)), addFloorVBox, newFloorBtn, nrFloorsLabel, popupOpen, event2.getScreenX(), event2.getScreenY());
                    popup.start(new Stage());
                }
            });
            
            if(!initiateFloor)
            {
                initiateFloor = true;
                Platform.runLater(() -> {
                    Floor f = frame.building.getFloors().get(0);
                    frame.drawingPanel.setGraph(f.getGraph());
                    frame.drawingPanel.setShapes(f.getShapes());
                    frame.drawingPanel.deleteAllShapes();
                    frame.drawingPanel.drawAll();
                });
            }
        });
        
        if(state && !initiateFloor)
        {
            Platform.runLater(() -> {
                addFloorBtn.fire();
            });
        }
        
        for(int i = 0; i < building.getFloors().size(); i++)
        {
            AtomicBoolean popupOpen = new AtomicBoolean(false);
            existingFloors.add(i);
            Button newFloorBtn = new Button("Floor " + i);
            newFloorBtn.setMaxWidth(Double.MAX_VALUE);
            addFloorVBox.getChildren().add(newFloorBtn);
            nrFloorsLabel.setText("Floors: " + (addFloorVBox.getChildren().size()));

            if (addFloorVBox.getChildren().size() >= 1) {
                frame.drawingPanel.getCanvas().setVisible(true);
                frame.drawingPanel.setStyle("-fx-background-color: white");
            } else {
                frame.drawingPanel.getCanvas().setVisible(false);
                frame.drawingPanel.setStyle("");
            }

            newFloorBtn.setOnMousePressed(event2 -> {
                if (event2.isPrimaryButtonDown()) {
                    String nrFloor = newFloorBtn.getText().substring(6, 7);
                    Floor f = frame.building.getFloors().get(Integer.parseInt(nrFloor));
                    frame.drawingPanel.setGraph(f.getGraph());
                    frame.drawingPanel.setShapes(f.getShapes());
                    frame.drawingPanel.deleteAllShapes();
                    frame.drawingPanel.drawAll();
                } else if (event2.isSecondaryButtonDown() && !popupOpen.get()) {
                    popupOpen.set(true);
                    UpdateFloorPopUp popup = new UpdateFloorPopUp(frame, Integer.parseInt(newFloorBtn.getText().substring(6, 7)), addFloorVBox, newFloorBtn, nrFloorsLabel, popupOpen, event2.getScreenX(), event2.getScreenY());
                    popup.start(new Stage());
                }
            });
            
            if(i == 0)
            {
                Platform.runLater(() -> {
                    
                    Floor f = frame.building.getFloors().get(0);
                    frame.drawingPanel.setGraph(f.getGraph());
                    frame.drawingPanel.setShapes(f.getShapes());
                    frame.drawingPanel.deleteAllShapes();
                    frame.drawingPanel.drawAll();
                });
            }
        }

        eraserBtn.setOnAction(event -> {
            if (erase) {
                erase = false;
                eraserBtn.setText("Eraser: Off");
            } else {
                erase = true;
                eraserBtn.setText("Eraser: On");
            }
        });
        
        Platform.runLater(() -> {
            CustomAnimation.animateTypeWriterText(nrFloorsLabel, "Floors: " + addFloorVBox.getChildren().size());
        });
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox comboBox) {
        this.comboBox = comboBox;
    }
}