/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class DrawingPanel extends HBox {

    private Canvas canvas;
    private List<ExtendedShape> shapes;
    private GraphicsContext gc;
    private MainFrame mainFrame;
    private boolean wasInsideShape = false;
    private boolean wasInsideWouldBeShape = false;
    private ExtendedShape currentShape;
    private ExtendedShape initialShape = null;
    private int type = 0;
    boolean isInCenter = false;
    boolean isShapeBeingDragged = false;
    private Graph graph = new Graph();
    private double mouseX;
    private double mouseY;
    private double initialX = 0;
    private double initialY = 0;
    private boolean isMouseInCenter = false;
    private ExtendedShape draggedShape = null;
    private List<Integer> ids;

    public double areaOfTriangle(Point x, Point y, Point z) {
        double returnValue = x.getX() * (y.getY() - z.getY()) + y.getX() * (z.getY() - x.getY()) + z.getX() * (x.getY() - y.getY());
        if (returnValue < 0) {
            return -returnValue;
        } else {
            return returnValue;
        }
    }

    public DrawingPanel(MainFrame mf) {
        mainFrame = mf;
        shapes = new ArrayList<>();
        this.setAlignment(Pos.CENTER);
        setCanvas();
        canvas.setVisible(false);
        ids = new ArrayList<>();
        List<Integer> hallways = new ArrayList<>();
        List<Integer> classrooms = new ArrayList<>();
        List<Integer> bathrooms = new ArrayList<>();
        List<Integer> stairs = new ArrayList<>();
        List<Integer> elevator = new ArrayList<>();

        AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

        this.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.5;
                if (event.getDeltaY() <= 0) {

                    zoomFactor = 1 / zoomFactor;
                }
                zoomOperator.zoom(canvas, zoomFactor, event.getSceneX(), event.getSceneY());
            }
        });
    }

    public void setMainFrame(MainFrame mf) {
        this.mainFrame = mf;
    }

    public boolean checkCollision(ExtendedRectangle r, int add) {
        r.getRectangle().setSize((int) r.getRectangle().getWidth() + add, (int) r.getRectangle().getHeight() + add);
        for (ExtendedShape shape : shapes) {
            if (r != shape) {
                ((ExtendedRectangle) shape).getRectangle().setSize((int) ((ExtendedRectangle) shape).getRectangle().getWidth() + add, (int) ((ExtendedRectangle) shape).getRectangle().getHeight() + add);
                if (shape instanceof ExtendedRectangle) {
                    if (((ExtendedRectangle) shape).getRectangle().getBounds().intersects(r.getRectangle().getBounds())) {
                        r.getRectangle().setSize((int) r.getRectangle().getWidth() - add, (int) r.getRectangle().getHeight() - add);
                        ((ExtendedRectangle) shape).getRectangle().setSize((int) ((ExtendedRectangle) shape).getRectangle().getWidth() - add, (int) ((ExtendedRectangle) shape).getRectangle().getHeight() - add);
                        return true;
                    }
                }
                ((ExtendedRectangle) shape).getRectangle().setSize((int) ((ExtendedRectangle) shape).getRectangle().getWidth() - add, (int) ((ExtendedRectangle) shape).getRectangle().getHeight() - add);
            }
        }
        r.getRectangle().setSize((int) r.getRectangle().getWidth() - add, (int) r.getRectangle().getHeight() - add);
        return false;
    }

    public void initRectangle(ExtendedRectangle r, double x, double y, double h, double w) {
        r.getRectangle().setBounds((int) x, (int) y, (int) w, (int) h);
    }

    public void draw(double x, double y) {

        drawRectangle(x, y);
    }

    public void drawRectangle(double x, double y) {
        if (!mainFrame.getConfigPanel().getWidthTextField().getText().isBlank() && !mainFrame.getConfigPanel().getHeightTextField().getText().isBlank()) {
            ExtendedRectangle r = new ExtendedRectangle(new Point(x, y));
            if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                r = new Classroom(new Point(x, y));
            } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                r = new Hallway(new Point(x, y));
            } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                r = new Stairs(new Point(x, y));
            } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                r = new Elevator(new Point(x, y));
            }

            initRectangle(r, x, y, Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()));
            if (checkCollision(r, 0) == false) {
                gc.beginPath();
                gc.setFill(mainFrame.getConfigPanel().getColorPicker().getValue());
                gc.setStroke(mainFrame.getConfigPanel().getColorPicker().getValue());
                gc.rect(x, y, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                // gc.fill();
                gc.stroke();
                r.setLength(Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                r.setWidth(Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()));
                r.setColor(mainFrame.getConfigPanel().getColorPicker().getValue().toString());
                shapes.add(r);
                setId(r);
                r.setName(String.valueOf(r.getId()));
                gc.fillText(r.getName(), r.getStartPoint().getX() - r.getName().length() * 2 - 3, r.getStartPoint().getY() - 1, r.getWidth());
                if (initialShape == null) {
                    initialShape = r;
                    graph.addInitialShape(initialShape);
                } else {
                    addShapeToGraph(r);
                    setOrder();
                }
                System.out.println(graph);
            }
        }
    }

    public void addShapeToGraph(ExtendedRectangle r) {
        r.getRectangle().setSize((int) r.getRectangle().getWidth() + 1, (int) r.getRectangle().getHeight() + 1);
        for (ExtendedShape sh : shapes) {
            if (sh != r) {
                boolean ok = false;
                if (sh instanceof ExtendedRectangle) {
                    ((ExtendedRectangle) sh).getRectangle().setSize((int) ((ExtendedRectangle) sh).getRectangle().getWidth() + 1, (int) ((ExtendedRectangle) sh).getRectangle().getHeight() + 1);

                    if (((ExtendedRectangle) sh).getRectangle().getBounds().intersects(r.getRectangle().getBounds())) {
                        String rToS = null;
                        String sToR = null;
                        if (sh.getCenterPoint().getX() == r.getCenterPoint().getX() + r.getWidth()) {
                            if (sh.getCenterPoint().getY() + ((ExtendedRectangle) sh).getLength() != r.getCenterPoint().getY() && r.getCenterPoint().getY() + r.getLength() != ((ExtendedRectangle) sh).getCenterPoint().getY() && r.getCenterPoint().getY() + r.getLength() != sh.getCenterPoint().getY() && sh.getCenterPoint().getY() + ((ExtendedRectangle) sh).getLength() != r.getCenterPoint().getY()) {
                                rToS = "Left";
                                sToR = "Right";
                                ok = true;
                            }
                        } else if (sh.getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() == r.getCenterPoint().getX()) {
                            if (sh.getCenterPoint().getY() + ((ExtendedRectangle) sh).getLength() != r.getCenterPoint().getY() && r.getCenterPoint().getY() + r.getLength() != ((ExtendedRectangle) sh).getCenterPoint().getY() && r.getCenterPoint().getY() + r.getLength() != sh.getCenterPoint().getY() && sh.getCenterPoint().getY() + ((ExtendedRectangle) sh).getLength() != r.getCenterPoint().getY()) {
                                rToS = "Right";
                                sToR = "Left";
                                ok = true;
                            }
                        } else if (sh.getCenterPoint().getY() - r.getLength() == r.getCenterPoint().getY()) {
                            if (sh.getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() != r.centerPoint.getX() && r.getCenterPoint().getX() + r.getWidth() != sh.getCenterPoint().getX() && r.getCenterPoint().getX() + r.getWidth() != sh.centerPoint.getX() && sh.getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() != r.getCenterPoint().getX()) {
                                rToS = "Up";
                                sToR = "Down";
                                ok = true;
                            }
                        } else if (sh.getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() != r.centerPoint.getX() && r.getCenterPoint().getX() + r.getWidth() != sh.getCenterPoint().getX() && r.getCenterPoint().getX() + r.getWidth() != sh.centerPoint.getX() && sh.getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() != r.getCenterPoint().getX()) {
                            rToS = "Down";
                            sToR = "Up";
                            ok = true;
                        }
                        if (ok == true) {
                            Pair<ExtendedShape, String> p1 = new Pair(sh, sToR);
                            Pair<ExtendedShape, String> p2 = new Pair(r, rToS);
                            graph.addShape(r, p1);
                            graph.addShape(sh, p2);
                        }
                    }
                    ((ExtendedRectangle) sh).getRectangle().setSize((int) ((ExtendedRectangle) sh).getRectangle().getWidth() - 1, (int) ((ExtendedRectangle) sh).getRectangle().getHeight() - 1);
                }
            }
        }
        r.getRectangle().setSize((int) r.getRectangle().getWidth() - 1, (int) r.getRectangle().getHeight() - 1);
    }

    public void drawRectangle(ExtendedShape s) {
        gc.beginPath();
        gc.setFill(Color.valueOf(s.getColor()));
        gc.setStroke(Color.valueOf(s.getColor()));
        gc.rect(s.getCenterPoint().getX(), s.getCenterPoint().getY(), ((ExtendedRectangle) s).getWidth(), ((ExtendedRectangle) s).getLength());
        // gc.fill();
        gc.fillText(((ExtendedRectangle) s).getName(), ((ExtendedRectangle) s).getStartPoint().getX() - ((ExtendedRectangle) s).getName().length() * 2 - 3, ((ExtendedRectangle) s).getStartPoint().getY() - 1, ((ExtendedRectangle) s).getWidth());
        gc.stroke();
    }

    public void drawRectangle(ExtendedShape s, double x, double y, int t, boolean checkCenter) {
        gc.beginPath();
        gc.setFill(mainFrame.getConfigPanel().getColorPicker().getValue());
        gc.setStroke(mainFrame.getConfigPanel().getColorPicker().getValue());
        ExtendedRectangle r = null;
        if (s instanceof ExtendedRectangle) {
            if (t == 1) {
                if (checkCenter == false) {
                    gc.rect(x, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(x, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(x, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(x, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(x, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    }
                } else {
                    gc.rect(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Classroom(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                    }
                }
            } else if (t == 2) {
                if (checkCenter == false) {
                    gc.rect(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    }
                } else {
                    gc.rect(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                    }
                }
            } else if (t == 3) {
                if (checkCenter == false) {
                    gc.rect(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y));
                    }
                } else {
                    gc.rect(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    }
                }
            } else if (t == 4) {
                if (checkCenter == false) {
                    gc.rect(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y));
                    }
                } else {
                    gc.rect(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Class Room")) {
                        r = new Classroom(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Hall Way")) {
                        r = new Hallway(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Stairs")) {
                        r = new Stairs(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    } else if (mainFrame.controlPanel.comboBox.getSelectionModel().getSelectedItem().equals("Elevator")) {
                        r = new Elevator(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                    }
                }
            }
        }
        initRectangle(r, r.getCenterPoint().getX(), r.getCenterPoint().getY(), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()));
        if (checkCollision(r, 0) == false) {
            gc.stroke();
            // gc.fill();
            r.setLength(Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
            r.setWidth(Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()));
            r.setColor(mainFrame.getConfigPanel().getColorPicker().getValue().toString());
            shapes.add(r);
            setId(r);
            r.setName(String.valueOf(r.getId()));
            gc.fillText(r.getName(), r.getStartPoint().getX() - r.getName().length() * 2 - 3, r.getStartPoint().getY() - 1, r.getWidth());
            addShapeToGraph(r);
            setOrder();
            System.out.println(graph);
        }
    }

    public void drawAll() {
        for (ExtendedShape s : shapes) {
            if (s instanceof ExtendedRectangle) {
                drawRectangle(s);
            }
        }
    }

    public void deleteShapeFromGraph(ExtendedShape s) {
        shapes.remove(s);
        ids.remove((Integer)(((ExtendedRectangle) s).getId()));
        graph.deleteShapeFromGraph(s);
        System.out.println(graph);
    }

    public void delete(double x, double y) {
        if (shapes != null) {
            for (ExtendedShape s : shapes) {
                if (s instanceof ExtendedRectangle) {
                    if (isMouseInRectangle(s, x, y)) {
                        deleteShapeFromGraph(s);
                        deleteShape(((ExtendedRectangle) s));
                        drawAll();
                        break;
                    }
                }
            }
        }
    }

    public void setCanvas() {
        this.getChildren().remove(canvas);
        this.canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
            if (e.isPrimaryButtonDown()) {
                if (mainFrame.controlPanel.erase) {
                    delete(e.getX(), e.getY());
                } else {
                    if (wasInsideWouldBeShape == false) {
                        draw(e.getX(), e.getY());
                    } else {
                        draw(currentShape, e.getX(), e.getY(), type, isInCenter);
                    }
                }
            } else if (e.isSecondaryButtonDown()) {
                for (ExtendedShape shape : shapes) {
                    if (shape instanceof ExtendedRectangle) {
                        if (isMouseInRectangle(shape, e.getX(), e.getY())) {
                            Bounds bounds = canvas.localToScreen(canvas.getBoundsInLocal());
                            int x = (int) bounds.getMinX() + (int) ((ExtendedRectangle) shape).getRectangle().getX() + (int) ((ExtendedRectangle) shape).getRectangle().getWidth();
                            int y = (int) bounds.getMinY() + (int) ((ExtendedRectangle) shape).getRectangle().getY() + (int) ((ExtendedRectangle) shape).getLength() / 2;
                            if (((ExtendedRectangle) shape).type.equals("Classroom")) {
                                UpdateClassroomPopUp popUpFrame = new UpdateClassroomPopUp(shape, x, y);
                                popUpFrame.start(new Stage());
                            } else if (((ExtendedRectangle) shape).type.equals("Stairs")) {
                                UpdateStairsPopUp popUpFrame = new UpdateStairsPopUp(shape, x, y);
                                popUpFrame.start(new Stage());
                            } else if (((ExtendedRectangle) shape).type.equals("Hallway")) {
                                UpdateHallwayPopUp popUpFrame = new UpdateHallwayPopUp(shape, x, y);
                                popUpFrame.start(new Stage());
                            } else if (((ExtendedRectangle) shape).type.equals("Elevator")) {
                                UpdateElevatorPopUp popUpFrame = new UpdateElevatorPopUp(shape, x, y);
                                popUpFrame.start(new Stage());
                            }
                        }
                    }
                }
            }
        });
        mainFrame.getStage().show();
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (initialX == 0 && initialY == 0) {
                    initialX = mouseX;
                    initialY = mouseY;
                }
                event.setDragDetect(false);
                ExtendedRectangle castedShape = null;
                if (isShapeBeingDragged == false) {
                    for (ExtendedShape s : shapes) {
                        if (isMouseInRectangle(s, event.getX(), event.getY())) {
                            isShapeBeingDragged = true;
                            draggedShape = s;
                            castedShape = ((ExtendedRectangle) draggedShape);
                        }
                    }
                } else {
                    if (!isMouseInRectangle(draggedShape, event.getX(), event.getY())) {
                        castedShape = null;
                    } else {
                        castedShape = ((ExtendedRectangle) draggedShape);
                    }
                }
                if (castedShape != null) {
                    double beforeHeight = castedShape.getLength();
                    double beforeWidth = castedShape.getWidth();
                    double beforeX = castedShape.getCenterPoint().getX();
                    double beforeY = castedShape.getCenterPoint().getY();
                    if (isMouseInCenter == false) {
                        if (getRectangleHalf(draggedShape, event.getX(), event.getY()) == false) {
                            castedShape.setLength(castedShape.getLength() + (event.getY() - initialY));
                            castedShape.setWidth(castedShape.getWidth() + (event.getX() - initialX));
                            castedShape.getRectangle().setSize((int) castedShape.getWidth(), (int) castedShape.getLength());
                        } else {
                            castedShape.setLength(castedShape.getLength() - (event.getY() - initialY));
                            castedShape.setWidth(castedShape.getWidth() - (event.getX() - initialX));
                            castedShape.getCenterPoint().setX(castedShape.getCenterPoint().getX() + (event.getX() - initialX));
                            castedShape.getCenterPoint().setY(castedShape.getCenterPoint().getY() + (event.getY() - initialY));
                            castedShape.getStartPoint().setX(castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2);
                            castedShape.getStartPoint().setY(castedShape.getCenterPoint().getY() + castedShape.getLength() / 2);
                            castedShape.getRectangle().setBounds((int) castedShape.getCenterPoint().getX(), (int) castedShape.getCenterPoint().getY(), (int) castedShape.getWidth(), (int) castedShape.getLength());
                        }
                        initialX = event.getX();
                        initialY = event.getY();
                    } else {
                        castedShape.getCenterPoint().setX(castedShape.getCenterPoint().getX() + (event.getX() - initialX));
                        castedShape.getCenterPoint().setY(castedShape.getCenterPoint().getY() + (event.getY() - initialY));
                        castedShape.getRectangle().setLocation((int) castedShape.getCenterPoint().getX(), (int) castedShape.getCenterPoint().getY());
                        castedShape.getStartPoint().setX(castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2);
                        castedShape.getStartPoint().setY(castedShape.getCenterPoint().getY() + castedShape.getLength() / 2);
                        initialX = event.getX();
                        initialY = event.getY();
                    }
                    if (checkCollision(castedShape, 0) == true || castedShape.getWidth() < 50 || castedShape.getLength() < 50) {
                        castedShape.setLength(beforeHeight);
                        castedShape.setWidth(beforeWidth);
                        castedShape.getCenterPoint().setX(beforeX);
                        castedShape.getCenterPoint().setY(beforeY);
                        castedShape.getStartPoint().setX(castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2);
                        castedShape.getStartPoint().setY(castedShape.getCenterPoint().getY() + castedShape.getLength() / 2);
                        castedShape.getRectangle().setBounds((int) castedShape.getCenterPoint().getX(), (int) castedShape.getCenterPoint().getY(), (int) castedShape.getWidth(), (int) castedShape.getLength());
                    }
                    ExtendedRectangle oldShape = new ExtendedRectangle(new Point(beforeX, beforeY));
                    oldShape.setLength(beforeHeight);
                    oldShape.setWidth(beforeWidth);
                    deleteShape(oldShape);
                    deleteShapeFromGraph(draggedShape);
                    shapes.add(draggedShape);
                    addShapeToGraph(castedShape);
                    setOrder();
                    setId((ExtendedRectangle) draggedShape);
                    drawAll();
                    System.out.println(graph);
                }
            }
        });
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                draggedShape = null;
                isShapeBeingDragged = false;
                isMouseInCenter = false;
                initialX = 0;
                initialY = 0;
                event.setDragDetect(true);
            }
        });
        canvas.setOnMouseMoved(e -> {
            if (isShapeBeingDragged == false) {
                if (shapes != null) {
                    boolean ok = false;
                    for (ExtendedShape s : shapes) {
                        if (s instanceof ExtendedRectangle) {
                            if (isMouseInRectangle(s, e.getX(), e.getY())) {
                                ok = true;
                                if (wasInsideShape == true) {
                                    deleteAllShapes();
                                    drawAll();
                                }
                                wasInsideShape = true;
                                gc.beginPath();
                                if (e.getX() < s.getStartPoint().getX() + 15 && e.getX() > s.getStartPoint().getX() - 15 && e.getY() < s.getStartPoint().getY() + 15 && e.getY() > s.getStartPoint().getY() - 15) {
                                    gc.setStroke(Color.BLUE);
                                    isMouseInCenter = true;
                                } else {
                                    gc.setStroke(Color.ORANGE);
                                    isMouseInCenter = false;
                                }

                                gc.rect(s.getCenterPoint().getX() - 3, s.getCenterPoint().getY() - 3, ((ExtendedRectangle) s).getWidth() + 6, ((ExtendedRectangle) s).getLength() + 6);
                                gc.stroke();
                                break;
                            }
                        }
                    }
                    if (ok == false && wasInsideShape == true) {
                        wasInsideShape = false;
                        deleteAllShapes();
                        drawAll();
                    }
                    if (mainFrame.getControlPanel().erase == false) {
                        boolean ok2 = false;
                        if (ok == false) {
                            boolean hasFoundShapeCenter = false;
                            for (ExtendedShape s : shapes) {
                                if (hasFoundShapeCenter == false) {
                                    if (s instanceof ExtendedRectangle) {
                                        if (!mainFrame.getConfigPanel().getWidthTextField().getText().isEmpty() && ((e.getX() >= s.getCenterPoint().getX() && e.getX() <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() <= s.getCenterPoint().getX()) && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) && e.getY() > s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) - 20 && e.getY() < s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) + 20) {
                                            type = 1;
                                            if (wasInsideWouldBeShape == true) {
                                                deleteAllShapes();
                                                drawAll();
                                            }
                                            wasInsideWouldBeShape = true;
                                            currentShape = s;
                                            ok2 = true;
                                            gc.beginPath();
                                            Point cp = new Point(e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2);
                                            if (cp.getX() < s.getStartPoint().getX() + 3 && cp.getX() > s.getStartPoint().getX() - 3) {
                                                hasFoundShapeCenter = true;
                                                gc.setStroke(Color.BLUE);
                                                isInCenter = true;
                                            } else {
                                                isInCenter = false;
                                                gc.setStroke(Color.ORANGE);
                                            }
                                            gc.rect(e.getX(), s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                                            gc.stroke();
                                            gc.setStroke(Color.ORANGE);
                                        }
                                        if (!mainFrame.getConfigPanel().getWidthTextField().getText().isEmpty() && !mainFrame.getConfigPanel().getHeightTextField().getText().isEmpty() && ((e.getX() >= s.getCenterPoint().getX() && e.getX() <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() <= s.getCenterPoint().getX()) && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) && e.getY() > s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength() - 20 && e.getY() < s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength() + 20) {
                                            type = 2;
                                            if (wasInsideWouldBeShape == true) {
                                                deleteAllShapes();
                                                drawAll();
                                            }
                                            wasInsideWouldBeShape = true;
                                            currentShape = s;
                                            ok2 = true;
                                            gc.beginPath();
                                            Point cp = new Point(e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2);
                                            if (cp.getX() < s.getStartPoint().getX() + 3 && cp.getX() > s.getStartPoint().getX() - 3) {
                                                gc.setStroke(Color.BLUE);
                                                hasFoundShapeCenter = true;
                                                isInCenter = true;
                                            } else {
                                                isInCenter = false;
                                                gc.setStroke(Color.ORANGE);
                                            }
                                            gc.rect(e.getX(), s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                                            gc.stroke();
                                        }
                                        if (!mainFrame.getConfigPanel().getWidthTextField().getText().isEmpty() && !mainFrame.getConfigPanel().getHeightTextField().getText().isEmpty() && (e.getX() < s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) + 20 && e.getX() > s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) - 20) && (((e.getY() >= s.getCenterPoint().getY()) && e.getY() <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || ((e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY()) && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || (e.getY() < s.getCenterPoint().getY() && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()))) {
                                            type = 3;
                                            if (wasInsideWouldBeShape == true) {
                                                deleteAllShapes();
                                                drawAll();
                                            }
                                            wasInsideWouldBeShape = true;
                                            currentShape = s;
                                            ok2 = true;
                                            gc.beginPath();
                                            Point cp = new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2);
                                            if (cp.getY() < s.getStartPoint().getY() + 3 && cp.getY() > s.getStartPoint().getY() - 3) {
                                                gc.setStroke(Color.BLUE);
                                                hasFoundShapeCenter = true;
                                                isInCenter = true;
                                            } else {
                                                isInCenter = false;
                                                gc.setStroke(Color.ORANGE);
                                            }
                                            gc.rect(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), e.getY(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                                            //gc.strokeLine(this.getLayoutX(),this.getLayoutY(),this.getLayoutX() + 300,this.getLayoutY());
                                            gc.stroke();
                                        }
                                        if (!mainFrame.getConfigPanel().getWidthTextField().getText().isEmpty() && !mainFrame.getConfigPanel().getHeightTextField().getText().isEmpty() && (e.getX() < s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth() + 20 && e.getX() > s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth() - 20) && (((e.getY() >= s.getCenterPoint().getY()) && e.getY() <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || ((e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY()) && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || (e.getY() <= s.getCenterPoint().getY() && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()))) {
                                            type = 4;
                                            if (wasInsideWouldBeShape == true) {
                                                deleteAllShapes();
                                                drawAll();
                                            }
                                            wasInsideWouldBeShape = true;
                                            currentShape = s;
                                            ok2 = true;
                                            gc.beginPath();
                                            Point cp = new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2);
                                            if (cp.getY() < s.getStartPoint().getY() + 3 && cp.getY() > s.getStartPoint().getY() - 3) {
                                                hasFoundShapeCenter = true;
                                                gc.setStroke(Color.BLUE);
                                                isInCenter = true;
                                            } else {
                                                isInCenter = false;
                                                gc.setStroke(Color.ORANGE);
                                            }
                                            gc.rect(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), e.getY(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                                            gc.stroke();
                                        }
                                    }
                                }
                            }
                        }
                        if (ok2 == false && wasInsideWouldBeShape == true) {
                            wasInsideWouldBeShape = false;
                            deleteAllShapes();
                            drawAll();
                        }
                    }
                }
            }
        });
    }

    public void draw(ExtendedShape s, double x, double y, int t, boolean checkCenter) {

        drawRectangle(s, x, y, t, checkCenter);
    }

    public void deleteShape(ExtendedRectangle r) {
        gc.beginPath();
        gc.setFill(Color.rgb(255, 255, 255));
        gc.setStroke(Color.rgb(255, 255, 255));
        gc.rect(r.getCenterPoint().getX() - 6, r.getCenterPoint().getY() - 6, r.getWidth() + 12, r.getLength() + 12);
        gc.fill();
        gc.stroke();
    }

    public void deleteAllShapes() {
        gc.beginPath();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.rect(0, 0, this.getWidth(), this.getHeight());
        gc.stroke();
        gc.fill();
    }

    public boolean isMouseInRectangle(ExtendedShape s, double x, double y) {
        Point center = new Point();
        center.setX((((ExtendedRectangle) s).getCenterPoint().getX()) + ((ExtendedRectangle) s).getWidth() / 2);
        center.setY((((ExtendedRectangle) s).getCenterPoint().getY()) + ((ExtendedRectangle) s).getLength() / 2);
        Point x1 = new Point(center.getX() - ((ExtendedRectangle) s).getWidth() / 2, center.getY() - ((ExtendedRectangle) s).getLength() / 2);
        Point x2 = new Point(center.getX() + ((ExtendedRectangle) s).getWidth() / 2, center.getY() - ((ExtendedRectangle) s).getLength() / 2);
        Point x3 = new Point(center.getX() + ((ExtendedRectangle) s).getWidth() / 2, center.getY() + ((ExtendedRectangle) s).getLength() / 2);
        Point x4 = new Point(center.getX() - ((ExtendedRectangle) s).getWidth() / 2, center.getY() + ((ExtendedRectangle) s).getLength() / 2);
        double rectangleArea = ((ExtendedRectangle) s).getLength() * ((ExtendedRectangle) s).getWidth();
        Point p = new Point(x, y);
        if (areaOfTriangle(x1, x2, p) + areaOfTriangle(x2, x3, p) + areaOfTriangle(x3, x4, p) + areaOfTriangle(x4, x1, p) == rectangleArea * 2 && x > s.getCenterPoint().getX() + 3 && x < s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth() - 3 && y > s.getCenterPoint().getY() + 3 && y < s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength() - 3) {
            return true;
        }
        return false;
    }

    public boolean getRectangleHalf(ExtendedShape s, double x, double y) {
        Point center = new Point();
        center.setX((((ExtendedRectangle) s).getCenterPoint().getX()) + ((ExtendedRectangle) s).getWidth() / 2);
        center.setY((((ExtendedRectangle) s).getCenterPoint().getY()) + ((ExtendedRectangle) s).getLength() / 2);
        Point x1 = new Point(center.getX() - ((ExtendedRectangle) s).getWidth() / 2, center.getY() - ((ExtendedRectangle) s).getLength() / 2);
        Point x2 = new Point(center.getX() + ((ExtendedRectangle) s).getWidth() / 2, center.getY() - ((ExtendedRectangle) s).getLength() / 2);
        Point x3 = new Point(center.getX() + ((ExtendedRectangle) s).getWidth() / 2, center.getY() + ((ExtendedRectangle) s).getLength() / 2);
        Point x4 = new Point(center.getX() - ((ExtendedRectangle) s).getWidth() / 2, center.getY() + ((ExtendedRectangle) s).getLength() / 2);
        double firstHalfArea = areaOfTriangle(x1, x2, x4);
        //double secondHalfArea = areaOfTriangle(x2, x3, x4);
        Point p = new Point(x, y);
        if (areaOfTriangle(p, x1, x2) + areaOfTriangle(x2, x4, p) + areaOfTriangle(x4, x1, p) == firstHalfArea) {
            return true;
        } else {
            return false;
        }
    }

    public void setId(ExtendedRectangle r) {
        Collections.sort(ids);
        int i = 1;
        if (ids.isEmpty()) {
            r.setId(i);
            ids.add(i);
        } else {
            for (Integer id : ids) {
                if (id != i) {
                    break;
                }
                i++;
            }
            r.setId(i);
            ids.add(i);
        }
        System.out.println(ids);
    }

    public void setOrder() {
        graph.setOrder();
    }

    public List<ExtendedShape> getShapes() {
        return shapes;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setShapes(List<ExtendedShape> shapes) {
        this.shapes = shapes;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

}