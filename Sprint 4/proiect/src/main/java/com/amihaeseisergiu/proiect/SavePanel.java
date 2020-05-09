/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Sergiu
 */
public class SavePanel extends HBox {
    
    MainFrame frame;
    
    public SavePanel(MainFrame frame)
    {
        this.frame = frame;
        
        this.setPadding(new Insets(5, 5, 5, 5));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        
        Button saveBtn = new Button("Save Building");
        this.getChildren().addAll(saveBtn);
   
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:/"));
        
        saveBtn.setOnAction(e -> {
            
            File selectedDirectory = directoryChooser.showDialog(frame.stage);

            if(selectedDirectory != null)
            {
                try {
                    
                    try (FileOutputStream fileOutputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/" + frame.building.name + ".json")) {
                        byte[] strToBytes = frame.building.toJson().toJSONString().getBytes();
                        fileOutputStream.write(strToBytes);
                    }
                    MainMenu menu = new MainMenu(frame.stage);
                        
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SavePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SavePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
}
