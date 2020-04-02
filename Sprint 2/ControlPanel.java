package com.amihaeseisergiu.proiect;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ControlPanel extends VBox {
    
    Label text;
    TextField textField;
    
    public ControlPanel()
    {
        text = new Label("Test");
        textField = new TextField();
        
        this.getChildren().addAll(text, textField);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10,10,10,10));
        this.setStyle("-fx-border-color: black;");
    }
}
