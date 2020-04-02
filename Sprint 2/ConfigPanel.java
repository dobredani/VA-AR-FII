/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author Sergiu
 */
public class ConfigPanel extends HBox{
    
    Label text;
    TextField textField;
    
    public ConfigPanel()
    {
        text = new Label("Test");
        textField = new TextField();
        this.getChildren().addAll(text, textField);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10,10,10,10));
        this.setStyle("-fx-border-color: black;");
    }
}
