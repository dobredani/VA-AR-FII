/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DrawingPanel extends HBox {

    Canvas canvas;
    private List<ExtendedShape> shapes;
    private GraphicsContext gc;
    private MainFrame mainFrame;
    private UpdateShapePopUp popUpFrame;
    private boolean wasInsideShape = false;
    private boolean wasInsideWouldBeShape = false;
    private ExtendedShape currentShape;
    private int type = 0;
    boolean isInCenter = false;

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

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
        this.setStyle("-fx-background-color: white");
        this.setAlignment(Pos.CENTER);
        resetCanvas();
    }

    public void setMainFrame(MainFrame mf) {
        this.mainFrame = mf;
    }

    public boolean checkCollision(ExtendedRectangle r) {
        for (ExtendedShape shape : shapes) {
            if (shape instanceof ExtendedRectangle) {
                if (((ExtendedRectangle) shape).getRectangle().getLayoutBounds().intersects(r.getRectangle().getLayoutBounds())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initRectangle(ExtendedRectangle r, double x, double y, double h, double w) {
        r.getRectangle().setHeight(h);
        r.getRectangle().setWidth(w);
        r.getRectangle().setX(x);
        r.getRectangle().setY(y);
    }

    public void draw(double x, double y) {
        if (mainFrame.getControlPanel().getComboBox().getValue() instanceof VBox) {
            if (((VBox) mainFrame.getControlPanel().getComboBox().getValue()).getChildren().get(1) instanceof javafx.scene.shape.Rectangle) {
                drawRectangle(x, y);
            }
        }
    }

    public void drawRectangle(double x, double y) {
        if (!mainFrame.getConfigPanel().getWidthTextField().getText().isBlank() && !mainFrame.getConfigPanel().getHeightTextField().getText().isBlank()) {
            ExtendedRectangle r = new ExtendedRectangle(new Point(x, y));
            initRectangle(r, x, y, Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) - 1, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) - 1);
//            setDragListeners(r.getRectangle());
            if (checkCollision(r) == false) {
                gc.beginPath();
                gc.setFill(mainFrame.getConfigPanel().getColorPicker().getValue());
                gc.setStroke(mainFrame.getConfigPanel().getColorPicker().getValue());
                gc.rect(x, y, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                gc.fill();
                gc.stroke();
                r.setLength(Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                r.setWidth(Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()));
                r.setColor(mainFrame.getConfigPanel().getColorPicker().getValue().toString());
                shapes.add(r);
            }
        }
    }

    public void drawRectangle(ExtendedShape s) {
        gc.beginPath();
        gc.setFill(Color.valueOf(s.getColor()));
        gc.setStroke(Color.valueOf(s.getColor()));
        gc.rect(s.getCenterPoint().getX(), s.getCenterPoint().getY(), ((ExtendedRectangle) s).getWidth(), ((ExtendedRectangle) s).getLength());
        gc.fill();
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
                    r = new ExtendedRectangle(new Point(x, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                } else {
                    gc.rect(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText())));
                }
            } else if (t == 2) {
                if (checkCenter == false) {
                    gc.rect(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(x, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                } else {
                    gc.rect(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength(), Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(s.getStartPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) / 2, s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()));
                }
            } else if (t == 3) {
                if (checkCenter == false) {
                    gc.rect(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), y));
                } else {
                    gc.rect(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                }
            } else if (t == 4) {
                if (checkCenter == false) {
                    gc.rect(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), y));
                } else {
                    gc.rect(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
                    r = new ExtendedRectangle(new Point(s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth(), s.getStartPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) / 2));
                }
            }
        }
        initRectangle(r, r.getCenterPoint().getX(), r.getCenterPoint().getY(), Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) - 1, Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) - 1);
        //setDragListeners(r.getRectangle());
        if (checkCollision(r) == false) {
            gc.stroke();
            gc.fill();
            r.setLength(Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()));
            r.setWidth(Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()));
            r.setColor(mainFrame.getConfigPanel().getColorPicker().getValue().toString());
            shapes.add(r);
        }
    }

    public void drawAll() {
        for (ExtendedShape s : shapes) {
            if (s instanceof ExtendedRectangle) {
                drawRectangle(s);
            }
        }
    }

    public void deleteShape(ExtendedShape s) {
        shapes.remove(s);
    }

    public void delete(double x, double y) {
        if (shapes != null) {
            for (ExtendedShape s : shapes) {
                if (s instanceof ExtendedRectangle) {
                    if (isMouseInRectangle(s, x, y)) {
                        deleteShape(s);
                        resetCanvas();
                        drawAll();
                        break;
                    }
                }
            }
        }
    }

    public void resetCanvas() {
        this.getChildren().remove(canvas);
        this.canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(e -> {
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
                            popUpFrame = new UpdateShapePopUp(shape, e.getScreenX(), e.getScreenY());
                            Stage stage = new Stage();
                            popUpFrame.start(stage);
                        }
                    }
                }
            }
        });
        mainFrame.getStage().show();
        canvas.setOnMouseMoved(e -> {
            if (shapes != null) {
                boolean ok = false;
                for (ExtendedShape s : shapes) {
                    if (s instanceof ExtendedRectangle) {
                        if (isMouseInRectangle(s, e.getX(), e.getY())) {
                            ok = true;
                            if (wasInsideShape == true) {
                                resetCanvas();
                                drawAll();
                            }
                            wasInsideShape = true;
                            gc.beginPath();
                            gc.setStroke(Color.ORANGE);
                            gc.rect(s.getCenterPoint().getX() - 3, s.getCenterPoint().getY() - 3, ((ExtendedRectangle) s).getWidth() + 6, ((ExtendedRectangle) s).getLength() + 6);
                            gc.stroke();
                            break;
                        }
                    }
                }
                if (ok == false && wasInsideShape == true) {
                    wasInsideShape = false;
                    resetCanvas();
                    drawAll();
                }
                if (mainFrame.getControlPanel().erase == false) {
                    boolean ok2 = false;
                    if (ok == false) {
                        boolean hasFoundShapeCenter = false;
                        for (ExtendedShape s : shapes) {
                            if (hasFoundShapeCenter == false) {
                                if (s instanceof ExtendedRectangle) {
                                    if (((e.getX() >= s.getCenterPoint().getX() && e.getX() <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() <= s.getCenterPoint().getX()) && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) && e.getY() > s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) - 20 && e.getY() < s.getCenterPoint().getY() - Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) + 20) {
                                        type = 1;
                                        if (wasInsideWouldBeShape == true) {
                                            resetCanvas();
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
                                    if (((e.getX() >= s.getCenterPoint().getX() && e.getX() <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) <= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) || (e.getX() <= s.getCenterPoint().getX()) && e.getX() + Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) >= s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth()) && e.getY() > s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength() - 20 && e.getY() < s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength() + 20) {
                                        type = 2;
                                        if (wasInsideWouldBeShape == true) {
                                            resetCanvas();
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
                                    if ((e.getX() < s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) + 20 && e.getX() > s.getCenterPoint().getX() - Double.parseDouble(mainFrame.getConfigPanel().getWidthTextField().getText()) - 20) && (((e.getY() >= s.getCenterPoint().getY()) && e.getY() <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || ((e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY()) && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || (e.getY() < s.getCenterPoint().getY() && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()))) {
                                        type = 3;
                                        if (wasInsideWouldBeShape == true) {
                                            resetCanvas();
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
                                    if ((e.getX() < s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth() + 20 && e.getX() > s.getCenterPoint().getX() + ((ExtendedRectangle) s).getWidth() - 20) && (((e.getY() >= s.getCenterPoint().getY()) && e.getY() <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || ((e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY()) && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) <= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()) || (e.getY() <= s.getCenterPoint().getY() && e.getY() + Double.parseDouble(mainFrame.getConfigPanel().getHeightTextField().getText()) >= s.getCenterPoint().getY() + ((ExtendedRectangle) s).getLength()))) {
                                        type = 4;
                                        if (wasInsideWouldBeShape == true) {
                                            resetCanvas();
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
                        resetCanvas();
                        drawAll();
                    }
                }
            }
        });
    }

    public void draw(ExtendedShape s, double x, double y, int t, boolean checkCenter) {
        if (mainFrame.getControlPanel().getComboBox().getValue() instanceof VBox) {
            if (((VBox) mainFrame.getControlPanel().getComboBox().getValue()).getChildren().get(1) instanceof javafx.scene.shape.Rectangle) {
                drawRectangle(s, x, y, t, checkCenter);
            }
        }
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

    /* public void setDragListeners(final Shape block) {
        final Point dragDelta = new Point();

        block.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.setX(block.getLayoutX() - mouseEvent.getSceneX());
                dragDelta.setY(block.getLayoutY() - mouseEvent.getSceneY());
                block.setCursor(Cursor.NONE);
            }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                block.setCursor(Cursor.HAND);
            }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                block.setLayoutX(mouseEvent.getSceneX() + dragDelta.getX());
                block.setLayoutY(mouseEvent.getSceneY() + dragDelta.getY());
     //           checkShapeIntersection(block);
            }
        });
    }
    /*private void checkShapeIntersection(Shape block) {
    boolean collisionDetected = false;
    for (ExtendedShape static_bloc : shapes) {
      if ( ((ExtendedRectangle)static_bloc).getRectangle() != block) {
        static_bloc.setFill(Color.GREEN);

        Shape intersect = Shape.intersect(block, static_bloc);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
          collisionDetected = true;
        }
      }
    }

    if (collisionDetected) {
      block.setFill(Color.BLUE);
    } else {
      block.setFill(Color.GREEN);
    }
  }*/
}
