package com.amihaeseisergiu.proiect;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
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
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainMenu {

    Stage stage;
    Scene scene;

    public MainMenu(Stage stage) {
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
        leftVBox.setStyle("-fx-border-insets: 5;"
                + "-fx-border-width: 3;"
                + "-fx-border-radius: 5;");

        VBox centerVBox = new VBox();
        centerVBox.setPadding(new Insets(5, 5, 5, 5));
        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setSpacing(10);
        centerVBox.setStyle("-fx-border-insets: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 1;");
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
            buildingHBox.setStyle("-fx-border-color: black;"
                    + "-fx-border-insets: 5;"
                    + "-fx-border-width: 1;"
                    + "-fx-border-radius: 1;");

            TextField buildName = new TextField();
            HBox leftBox = new HBox(buildName);
            leftBox.setAlignment(Pos.CENTER_LEFT);
            leftBox.setPadding(new Insets(5, 5, 5, 5));
            leftBox.setSpacing(10);
            HBox.setHgrow(leftBox, Priority.ALWAYS);

            Button editBtn = new Button("Edit");
            editBtn.setStyle("-fx-background-color: white;");
            Button deleteBtn = new Button("X");
            deleteBtn.setStyle("-fx-background-color: rgb(240,128,128);");
            HBox rightBox = new HBox(editBtn, deleteBtn);
            rightBox.setAlignment(Pos.CENTER_RIGHT);
            rightBox.setPadding(new Insets(5, 5, 5, 5));
            rightBox.setSpacing(10);
            HBox.setHgrow(rightBox, Priority.ALWAYS);

            buildingHBox.getChildren().addAll(leftBox, rightBox);
            centerScrollPaneVBox.getChildren().add(buildingHBox);

            editBtn.setOnAction(ev -> {

                if (!buildName.getText().isEmpty()) {
                    Building build = new Building();
                    build.name = buildName.getText();
                    MainFrame mainFrame = new MainFrame(stage, build, true);
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

            if (selectedDirectory != null) {
                File[] files = fileFinder(selectedDirectory.getAbsolutePath());

                centerScrollPaneVBox.getChildren().clear();

                for (File f : files) {
                    
                    JSONParser jsonParser = new JSONParser();
                    try (FileReader reader = new FileReader(f.getAbsolutePath()))
                    {
                        JSONObject obj = (JSONObject) jsonParser.parse(reader);
                        
                        Building build = new Building();
                        build.name = obj.get("name").toString();
                        
                        JSONArray floors = (JSONArray) obj.get("floors");
                        
                        
                        for(Object level : floors)
                        {
                            
                            Floor floor = new Floor(Integer.valueOf(((JSONObject) level).get("level").toString()));
                            Graph graph = new Graph();
                            floor.setGraph(graph);
                            
                            JSONArray waypoints = (JSONArray) ((JSONObject) level).get("waypoints");
                            
                            for(Object waypoint : waypoints)
                            {
                                String shapeType = ((JSONObject) waypoint).get("shapeType").toString();
                                ExtendedShape shape;
                                switch (shapeType) {
                                    case "Classroom":
                                        shape = new Classroom(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        break;
                                    case "Bathroom":
                                        shape = new Bathroom(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        break;
                                    case "Elevator":
                                        shape = new Elevator(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        break;
                                    case "Hallway":
                                        shape = new Hallway(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        graph.hallway = (Hallway) shape;
                                        break;
                                    case "Office":
                                        shape = new Office(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        break;
                                    case "Stairs":
                                        shape = new Stairs(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        break;
                                    default:
                                        shape = new Stairs(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                        break;
                                }
                                
                                shape.setColor(((JSONObject) waypoint).get("color").toString());
                                shape.setStartPoint(new Point(shape.getCenterPoint().getX() + Double.valueOf(((JSONObject) waypoint).get("width").toString()) / 2, shape.getCenterPoint().getY() + Double.valueOf(((JSONObject) waypoint).get("length").toString()) / 2));
                                ((ExtendedRectangle) shape).setWidth(Double.valueOf(((JSONObject) waypoint).get("width").toString()));
                                ((ExtendedRectangle) shape).setLength(Double.valueOf(((JSONObject) waypoint).get("length").toString()));
                                ((ExtendedRectangle) shape).setName(((JSONObject) waypoint).get("name").toString());
                                ((ExtendedRectangle) shape).setType(((JSONObject) waypoint).get("type").toString());
                                ((ExtendedRectangle) shape).setShapeType(((JSONObject) waypoint).get("shapeType").toString());
                                ((ExtendedRectangle) shape).setId(Integer.valueOf(((JSONObject) waypoint).get("markerId").toString()));
                                Rectangle rectangle = new Rectangle();
                                rectangle.setBounds((int)shape.getCenterPoint().getX(), (int)shape.getCenterPoint().getY(),(int) ((ExtendedRectangle) shape).getWidth(), (int) ((ExtendedRectangle) shape).getLength());
                                ((ExtendedRectangle) shape).setRectangle(rectangle);
                                
                                floor.getShapes().add(shape);
                                if(graph.graph.isEmpty())
                                {
                                    graph.addInitialShape(shape);
                                }
                                else {
                                    addShapeToGraph((ExtendedRectangle) shape, floor.getShapes(), graph);
                                    graph.setOrder();
                                }
                                
                            }
                            
                            build.getFloors().put(floor.getLevel(), floor);
                        }
                        
                        HBox buildingHBox = new HBox();
                        buildingHBox.setPadding(new Insets(5, 5, 5, 5));
                        buildingHBox.setAlignment(Pos.CENTER);
                        buildingHBox.setSpacing(10);
                        buildingHBox.setStyle("-fx-border-color: black;"
                                + "-fx-border-insets: 5;"
                                + "-fx-border-width: 1;"
                                + "-fx-border-radius: 1;");
                        TextField buildName = new TextField(f.getName().replaceFirst("[.][^.]+$", ""));
                        HBox leftBox = new HBox(buildName);
                        leftBox.setAlignment(Pos.CENTER_LEFT);
                        leftBox.setPadding(new Insets(5, 5, 5, 5));
                        leftBox.setSpacing(10);
                        HBox.setHgrow(leftBox, Priority.ALWAYS);
                        Button editBtn = new Button("Edit");
                        editBtn.setStyle("-fx-background-color: white;");
                        Button exportBtn = new Button("Export");
                        exportBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
                        Button deleteBtn = new Button("X");
                        deleteBtn.setStyle("-fx-background-color: rgb(240,128,128);");
                        HBox rightBox = new HBox(editBtn, exportBtn, deleteBtn);
                        rightBox.setAlignment(Pos.CENTER_RIGHT);
                        rightBox.setPadding(new Insets(5, 5, 5, 5));
                        rightBox.setSpacing(10);
                        HBox.setHgrow(rightBox, Priority.ALWAYS);
                        buildingHBox.getChildren().addAll(leftBox, rightBox);
                        centerScrollPaneVBox.getChildren().add(buildingHBox);
                        editBtn.setOnAction(ev -> {

                            build.name = buildName.getText();
                            MainFrame mainFrame = new MainFrame(stage, build, false);
                            mainFrame.init();
                        });
                        exportBtn.setOnAction(ev -> {
                            System.out.println(build.toJson().toJSONString());
                            String response = executePost("http://localhost:5000/building", build.toJson().toJSONString());
                            System.out.println(response);
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
                    } catch (IOException | ParseException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            
        });

        scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.setFullScreen(true);
        stage.show();
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public File[] fileFinder(String dirName) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".json");
            }

        });

    }
    
    public void addShapeToGraph(ExtendedRectangle r, List<ExtendedShape> shapes, Graph graph) {
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
}
