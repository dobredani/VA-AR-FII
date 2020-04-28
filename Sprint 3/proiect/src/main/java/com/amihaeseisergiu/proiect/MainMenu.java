package com.amihaeseisergiu.proiect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainMenu {
    
    Stage stage;
    Scene scene;
    
    public MainMenu(Stage stage)
    {
        this.stage = stage;
        
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 5, 5, 5));
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setSpacing(10);
        
        Label topLabel = new Label("Building Creator");
        topLabel.setStyle("-fx-font-size: 20;");
        topHBox.getChildren().addAll(topLabel);
        
        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(5, 5, 5, 5));
        leftVBox.setAlignment(Pos.TOP_CENTER);
        leftVBox.setSpacing(10);
        
        Button createNewBuilding = new Button("Create New Building");
        createNewBuilding.setMaxWidth(Double.MAX_VALUE);
        leftVBox.getChildren().addAll(createNewBuilding);
        leftVBox.setStyle("-fx-border-color: black;" +
        "-fx-border-insets: 5;" +
        "-fx-border-width: 3;" +
        "-fx-border-radius: 5;");
        
        VBox centerVBox = new VBox();
        centerVBox.setPadding(new Insets(5, 5, 5, 5));
        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setSpacing(10);
        centerVBox.setStyle("-fx-border-color: black;" +
        "-fx-border-insets: 5;" +
        "-fx-border-width: 3;" +
        "-fx-border-radius: 5;");
        ScrollPane centerScrollPane = new ScrollPane();
        centerScrollPane.setFitToWidth(true);
        centerScrollPane.setStyle("-fx-background-color:transparent;");
        
        VBox centerScrollPaneVBox = new VBox();
        centerScrollPaneVBox.setPadding(new Insets(5, 5, 5, 5));
        centerScrollPaneVBox.setAlignment(Pos.TOP_CENTER);
        centerScrollPaneVBox.setSpacing(10);
        centerScrollPane.setContent(centerScrollPaneVBox);
        
        Button selectFolder = new Button("Load Buildings");
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        centerVBox.getChildren().addAll(selectFolder);
        centerVBox.getChildren().addAll(centerScrollPane);
        
        pane.setCenter(centerVBox);
        pane.setTop(topHBox);
        pane.setLeft(leftVBox);
        
        createNewBuilding.setOnAction(e -> {
            
            HBox buildingHBox = new HBox();
            buildingHBox.setPadding(new Insets(5, 5, 5, 5));
            buildingHBox.setAlignment(Pos.CENTER);
            buildingHBox.setSpacing(10);
            buildingHBox.setStyle("-fx-border-color: black;" +
            "-fx-border-insets: 5;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 5;");

            TextField buildName = new TextField();
            HBox leftBox = new HBox(buildName);
            leftBox.setAlignment(Pos.CENTER_LEFT);
            leftBox.setPadding(new Insets(5, 5, 5, 5));
            leftBox.setSpacing(10);
            HBox.setHgrow(leftBox, Priority.ALWAYS);

            Button editBtn = new Button("Edit");
            Button deleteBtn = new Button("x");
            HBox rightBox = new HBox(editBtn, deleteBtn);
            rightBox.setAlignment(Pos.CENTER_RIGHT);
            rightBox.setPadding(new Insets(5, 5, 5, 5));
            rightBox.setSpacing(10);
            HBox.setHgrow(rightBox, Priority.ALWAYS);

            buildingHBox.getChildren().addAll(leftBox, rightBox);
            centerScrollPaneVBox.getChildren().add(buildingHBox);

            editBtn.setOnAction(ev -> {
                
                if(!buildName.getText().isEmpty())
                {
                    Building build = new Building();
                    build.name = buildName.getText();
                    MainFrame mainFrame = new MainFrame(stage, build);
                    mainFrame.init();
                }
            });

            deleteBtn.setOnAction(ev -> {
                
                centerScrollPaneVBox.getChildren().remove(buildingHBox);
            });
            
        });
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:/"));
        
        selectFolder.setOnAction(e -> {
            File selectedDirectory = directoryChooser.showDialog(stage);

            if(selectedDirectory != null)
            {
                File[] files = fileFinder(selectedDirectory.getAbsolutePath());
                for(File f : files)
                {
                    System.out.println(f.getAbsolutePath());
                }
                
                centerScrollPaneVBox.getChildren().clear();
                
                for (File f : files)
                {
                    FileInputStream fileInputStream;
                    try {
                        fileInputStream = new FileInputStream(f.getAbsolutePath());
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                        Building build = (Building) objectInputStream.readObject();
                        objectInputStream.close();
                        
                        HBox buildingHBox = new HBox();
                        buildingHBox.setPadding(new Insets(5, 5, 5, 5));
                        buildingHBox.setAlignment(Pos.CENTER);
                        buildingHBox.setSpacing(10);
                        buildingHBox.setStyle("-fx-border-color: black;" +
                        "-fx-border-insets: 5;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 5;");

                        TextField buildName = new TextField(f.getName().replaceFirst("[.][^.]+$", ""));
                        HBox leftBox = new HBox(buildName);
                        leftBox.setAlignment(Pos.CENTER_LEFT);
                        leftBox.setPadding(new Insets(5, 5, 5, 5));
                        leftBox.setSpacing(10);
                        HBox.setHgrow(leftBox, Priority.ALWAYS);

                        Button editBtn = new Button("Edit");
                        Button exportBtn = new Button("Export");
                        Button deleteBtn = new Button("x");
                        HBox rightBox = new HBox(editBtn, exportBtn, deleteBtn);
                        rightBox.setAlignment(Pos.CENTER_RIGHT);
                        rightBox.setPadding(new Insets(5, 5, 5, 5));
                        rightBox.setSpacing(10);
                        HBox.setHgrow(rightBox, Priority.ALWAYS);

                        buildingHBox.getChildren().addAll(leftBox, rightBox);
                        centerScrollPaneVBox.getChildren().add(buildingHBox);

                        editBtn.setOnAction(ev -> {

                            build.name = buildName.getText();
                            MainFrame mainFrame = new MainFrame(stage, build);
                            mainFrame.init();
                        });

                        exportBtn.setOnAction(ev -> {
                            PrintWriter pw = null;
                            try {
                                pw = new PrintWriter("building.json");
                                pw.write(build.toJson().toJSONString());

                                pw.flush();
                                pw.close();
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        });

                        deleteBtn.setOnAction(ev -> {

                            centerScrollPaneVBox.getChildren().remove(buildingHBox);
                            f.delete();
                        });

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
        
        scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    public File[] fileFinder(String dirName)
    {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter()
        { 
            @Override
            public boolean accept(File dir, String filename)
            {
                return filename.endsWith(".building");
            }
            
        });

    }
}
