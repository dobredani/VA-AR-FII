package com.amihaeseisergiu.proiect;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;

public class DrawingPanel extends HBox {
    
    Canvas canvas;
    
    public DrawingPanel()
    {
        canvas = new Canvas();
        this.getChildren().addAll(canvas);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: white;"
                + "-fx-border-color: black;");
    }
}
