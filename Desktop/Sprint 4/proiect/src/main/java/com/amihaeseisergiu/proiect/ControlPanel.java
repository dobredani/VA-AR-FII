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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;

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
        nrFloorsLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
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

        comboBox.setItems(FXCollections.observableArrayList(new String[]{"Class Room", "Hall Way", "Bathroom", "Office", "Stairs", "Elevator"}));
        comboBox.getSelectionModel().select(0);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        eraserBtn.setMaxWidth(Double.MAX_VALUE);
     /*   Button graph = new Button("Graph");
        graph.setOnAction(e->{
            System.out.println(frame.drawingPanel.getGraph());
        });*/
        Button undo = new Button("Undo");
        undo.setSkin(new FadeButtonSkin(undo));
        undo.setStyle("-fx-background-color: #ffff00;");
        undo.setMaxWidth(Double.MAX_VALUE);
        undo.setOnAction(e -> {
            if (frame.drawingPanel.getLastActions().size() != 0) {
                Pair<String, ExtendedShape> pair = frame.drawingPanel.getLastActions().get(frame.drawingPanel.getLastActions().size() - 1);
                if (pair.getKey().equals("Add")) {
                    frame.drawingPanel.getShapes().remove(pair.getValue());
                    frame.drawingPanel.deleteShapeFromGraph(((ExtendedRectangle)pair.getValue()));
                    frame.drawingPanel.setOrder();
                } else {
                    if (frame.drawingPanel.checkCollision(((ExtendedRectangle) pair.getValue()), 0) == false) {
                        frame.drawingPanel.getShapes().add(pair.getValue());
                        frame.drawingPanel.addShapeToGraph(((ExtendedRectangle)pair.getValue()));
                        frame.drawingPanel.setId((ExtendedRectangle) pair.getValue());
                        String name;
                        if(((ExtendedRectangle)pair.getValue()).getShapeType().split(" ")[0].equals("Classroom")) {
                            name = "Class";
                        } else if(((ExtendedRectangle)pair.getValue()).getShapeType().split(" ")[0].equals("Bathroom")) {
                            name = "Bath";
                        } else {
                            name = ((ExtendedRectangle)pair.getValue()).getShapeType().split(" ")[0];
                        }
                        ((ExtendedRectangle)pair.getValue()).setName(name + " " + ((ExtendedRectangle)pair.getValue()).getId());
                        frame.drawingPanel.setOrder();
                    }
                }
                frame.drawingPanel.getLastActions().remove(frame.drawingPanel.getLastActions().size() - 1);
                frame.drawingPanel.deleteAllShapes();
                frame.drawingPanel.drawAll();
            }
        });
        this.getChildren().addAll(eraserBtn, undo, comboBox, nrFloorsLabel, addFloorBtn, addFloorScrollPane);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(10);

        addFloorBtn.setOnAction(event -> {

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
            addFloorVBox.getChildren().add(0, newFloorBtn);
            addFloorScrollPane.setVisible(true);
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
                    String nrFloor = newFloorBtn.getText().split(" ")[1];
                    frame.savePanel.editingFloorLabel.setText("Editing Floor " + nrFloor);
                    Floor f = frame.building.getFloors().get(Integer.parseInt(nrFloor));
                    frame.drawingPanel.setGraph(f.getGraph());
                    frame.drawingPanel.setShapes(f.getShapes());
                    frame.drawingPanel.deleteAllShapes();
                    frame.drawingPanel.drawAll();
                } else if (event2.isSecondaryButtonDown()) {
                    int index = Integer.valueOf(newFloorBtn.getText().split(" ")[1]);
                    frame.drawingPanel.removeIds(frame.building.getFloors().get(index).getShapes());
                    int size = addFloorVBox.getChildren().size() - 1;
                    CustomAnimation.animateOutToLeftAndRemove(frame.scene.getWidth(), newFloorBtn, addFloorVBox.getChildren());
                    frame.building.getFloors().remove(index);
                    existingFloors.remove((Integer) index);
                    nrFloorsLabel.setText("Floors: " + size);
                    if (existingFloors.size() > 0) {
                        int nrFloor = existingFloors.get(0);
                        frame.savePanel.editingFloorLabel.setText("Editing Floor " + nrFloor);
                        Floor f = frame.building.getFloors().get(nrFloor);
                        frame.drawingPanel.setGraph(f.getGraph());
                        frame.drawingPanel.setShapes(f.getShapes());
                        frame.drawingPanel.deleteAllShapes();
                        frame.drawingPanel.drawAll();
                    } else {
                        frame.drawingPanel.deleteAllShapes();
                    }

                    if (frame.building.getFloors().size() >= 1) {
                        Platform.runLater(() -> {
                            frame.drawingPanel.getCanvas().setVisible(true);
                            frame.drawingPanel.setStyle("-fx-background-color: white");
                        });

                    }

                    if (size == 0) {
                        addFloorScrollPane.setVisible(false);
                        Platform.runLater(() -> {
                            frame.savePanel.editingFloorLabel.setText("");
                            frame.drawingPanel.getCanvas().setVisible(false);
                            frame.drawingPanel.setStyle("");
                        });
                    }
                }
            });

            if (!initiateFloor || existingFloors.size() == 1) {
                initiateFloor = true;
                Platform.runLater(() -> {
                    frame.savePanel.editingFloorLabel.setText("Editing Floor 0");
                    Floor f = frame.building.getFloors().get(0);
                    frame.drawingPanel.setGraph(f.getGraph());
                    frame.drawingPanel.setShapes(f.getShapes());
                    frame.drawingPanel.deleteAllShapes();
                    frame.drawingPanel.drawAll();
                });
            }
        });

        if (state && !initiateFloor) {
            Platform.runLater(() -> {
                addFloorBtn.fire();
            });
        }
        if (!state) {
            Platform.runLater(() -> {
                for (int i = 0; i < building.getFloors().size(); i++) {

                    existingFloors.add(i);
                    Button newFloorBtn = new Button("Floor " + i);
                    newFloorBtn.setMaxWidth(Double.MAX_VALUE);
                    newFloorBtn.setStyle("-fx-background-color: #ff6600;");
                    newFloorBtn.setSkin(new FadeButtonSkin(newFloorBtn));
                    addFloorVBox.getChildren().add(0, newFloorBtn);
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
                            String nrFloor = newFloorBtn.getText().split(" ")[1];
                            frame.savePanel.editingFloorLabel.setText("Editing Floor " + nrFloor);
                            Floor f = frame.building.getFloors().get(Integer.parseInt(nrFloor));
                            frame.drawingPanel.setGraph(f.getGraph());
                            frame.drawingPanel.setShapes(f.getShapes());
                            frame.drawingPanel.deleteAllShapes();
                            frame.drawingPanel.drawAll();
                        } else if (event2.isSecondaryButtonDown()) {
                            int index = Integer.valueOf(newFloorBtn.getText().split(" ")[1]);
                            frame.drawingPanel.removeIds(frame.building.getFloors().get(index).getShapes());
                            int size = addFloorVBox.getChildren().size() - 1;
                            CustomAnimation.animateOutToLeftAndRemove(frame.scene.getWidth(), newFloorBtn, addFloorVBox.getChildren());
                            frame.building.getFloors().remove(index);
                            existingFloors.remove((Integer) index);
                            nrFloorsLabel.setText("Floors: " + size);
                            if (existingFloors.size() > 0) {
                                int nrFloor = existingFloors.get(0);
                                frame.savePanel.editingFloorLabel.setText("Editing Floor " + nrFloor);
                                Floor f = frame.building.getFloors().get(nrFloor);
                                frame.drawingPanel.setGraph(f.getGraph());
                                frame.drawingPanel.setShapes(f.getShapes());
                                frame.drawingPanel.deleteAllShapes();
                                frame.drawingPanel.drawAll();
                            } else {
                                frame.drawingPanel.deleteAllShapes();
                            }

                            if (frame.building.getFloors().size() >= 1) {
                                Platform.runLater(() -> {
                                    frame.drawingPanel.getCanvas().setVisible(true);
                                    frame.drawingPanel.setStyle("-fx-background-color: white");
                                });

                            }

                            if (size == 0) {
                                addFloorScrollPane.setVisible(false);
                                Platform.runLater(() -> {
                                    frame.savePanel.editingFloorLabel.setText("");
                                    frame.drawingPanel.getCanvas().setVisible(false);
                                    frame.drawingPanel.setStyle("");
                                });
                            }
                        }
                    });

                    if (i == 0) {
                        Platform.runLater(() -> {

                            Floor f = frame.building.getFloors().get(0);
                            frame.drawingPanel.setGraph(f.getGraph());
                            frame.drawingPanel.setShapes(f.getShapes());
                            frame.drawingPanel.deleteAllShapes();
                            frame.drawingPanel.drawAll();
                        });
                    }
                }
            });
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

    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox comboBox) {
        this.comboBox = comboBox;
    }
}
