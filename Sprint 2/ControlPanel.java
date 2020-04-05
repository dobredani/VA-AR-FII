/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

/**
 *
 * @author Alex
 */
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControlPanel extends VBox {
    
    ComboBox comboBox;
    Button eraser;
    List<VBox> comboBoxItems;
    boolean erase;
    MainFrame frame;
    
    public ControlPanel(MainFrame frame)
    {
        this.frame = frame;
        
        eraser = new Button("Eraser: Off");
        comboBox = new ComboBox();
        comboBoxItems = new ArrayList<>();
        VBox item1 = new VBox();
        item1.setSpacing(10);
        item1.setAlignment(Pos.TOP_CENTER);
        Label item1Label = new Label("Rectangle");
        item1Label.setTextFill(Color.BLACK);
        item1.getChildren().addAll(item1Label, new Rectangle(0, 0));
        comboBoxItems.add(item1);
        
        comboBox.setItems(FXCollections.observableArrayList(comboBoxItems));
        comboBox.setVisibleRowCount(2);
        comboBox.setValue("Select Shape");
        comboBox.setMaxWidth(Double.MAX_VALUE);
        eraser.setMaxWidth(Double.MAX_VALUE);
        
        this.getChildren().addAll(eraser, comboBox);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10,10,10,10));
        this.setSpacing(10);
        
        eraser.setOnAction(event -> {
            if(erase)
            {
                erase = false;
                eraser.setText("Eraser: Off");
            }
            else
            {
                erase = true;
                eraser.setText("Eraser: On");
            }
        });
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public List<VBox> getComboBoxItems() {
        return comboBoxItems;
    }

    public void setComboBoxItems(List<VBox> comboBoxItems) {
        this.comboBoxItems = comboBoxItems;
    }
    
}
