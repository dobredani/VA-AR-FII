package com.amihaeseisergiu.proiect;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainMenu {

    Stage stage;
    Scene scene;
    boolean created;

    public MainMenu(Stage stage) {
        this.stage = stage;

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: linear-gradient(#4facfe, #00f2fe)");

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 5, 5, 5));
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setSpacing(10);
        topHBox.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");

        Label topLabel = new Label("Building Creator");
        topLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        topHBox.getChildren().addAll(topLabel);

        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(5, 5, 5, 5));
        leftVBox.setAlignment(Pos.TOP_CENTER);
        leftVBox.setSpacing(10);

        Button createNewBuilding = new Button("Create New Building");
        createNewBuilding.setSkin(new FadeButtonSkin(createNewBuilding));
        createNewBuilding.setStyle("-fx-background-color: #ffff00;");
        createNewBuilding.setMaxWidth(Double.MAX_VALUE);
        Button selectFolder = new Button("Load Buildings");
        selectFolder.setStyle("-fx-background-color: #ffff00;");
        selectFolder.setSkin(new FadeButtonSkin(selectFolder));
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        leftVBox.getChildren().addAll(createNewBuilding, selectFolder);
        leftVBox.setStyle("-fx-background-radius: 5;"
                + "-fx-background-color: white;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");

        VBox centerVBox = new VBox();
        centerVBox.setPadding(new Insets(5, 5, 5, 5));
        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setSpacing(10);
        centerVBox.setStyle("fx-background-color: transparent");
        ScrollPane centerScrollPane = new ScrollPane();
        centerScrollPane.setFitToWidth(true);
        centerScrollPane.setStyle("-fx-background-color: transparent;");
        centerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox centerScrollPaneVBox = new VBox();
        centerScrollPaneVBox.setPadding(new Insets(5, 5, 5, 5));
        centerScrollPaneVBox.setAlignment(Pos.TOP_CENTER);
        centerScrollPaneVBox.setSpacing(10);
        centerScrollPaneVBox.setStyle("-fx-background-color:transparent;");
        centerScrollPane.setContent(centerScrollPaneVBox);
        if (stage.isShowing()) {
            Platform.runLater(() -> {
                centerScrollPane.lookup(".viewport").setStyle("-fx-background-color: transparent;");
            });
        } else {
            stage.setOnShown(e -> centerScrollPane.lookup(".viewport").setStyle("-fx-background-color: transparent;"));
        }

        centerVBox.getChildren().addAll(centerScrollPane);

        pane.setCenter(centerVBox);
        pane.setTop(topHBox);
        pane.setLeft(leftVBox);

        createNewBuilding.setOnAction(e -> {

            if (!created) {
                created = true;
                HBox buildingHBox = new HBox();
                buildingHBox.setPadding(new Insets(5, 5, 5, 5));
                buildingHBox.setAlignment(Pos.CENTER);
                buildingHBox.setSpacing(10);
                buildingHBox.setStyle("-fx-border-color: black;"
                        + "-fx-background-color: white;"
                        + "-fx-background-radius: 5;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-radius: 5;");

                TextField buildName = new TextField();
                buildName.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
                Label nameLabel = new Label("Building Name:");
                HBox leftBox = new HBox(nameLabel, buildName);
                leftBox.setAlignment(Pos.CENTER_LEFT);
                leftBox.setPadding(new Insets(5, 5, 5, 5));
                leftBox.setSpacing(10);
                HBox.setHgrow(leftBox, Priority.ALWAYS);

                Button editBtn = new Button("Edit");
                editBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
                editBtn.setSkin(new FadeButtonSkin(editBtn));
                Button deleteBtn = new Button("X");
                deleteBtn.setSkin(new FadeButtonSkin(deleteBtn));
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
                    created = false;
                    CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), buildingHBox, centerScrollPaneVBox.getChildren());
                });

                CustomAnimation.animateInFromRightWithBounce(scene.getWidth(), buildingHBox);

            }
        }
        );

        selectFolder.setOnAction(e
                -> {
            ProgressIndicator progress = new ProgressIndicator();
            progress.setMaxSize(40, 40);
            progress.setStyle("-fx-progress-color: green");
            centerVBox.getChildren().add(progress);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String buildingNames = executeGet("http://localhost:5000/building");
                        JSONParser buildingParser = new JSONParser();
                        JSONArray buildings = (JSONArray) buildingParser.parse(buildingNames);

                        for (Object objct : buildings) {
                            String jsonBuilding = executeGet("http://localhost:5000/building/" + objct.toString().replace(" ", "%20"));
                            JSONParser jsonParser = new JSONParser();
                            JSONObject obj = (JSONObject) jsonParser.parse(jsonBuilding);

                            Building build = new Building();
                            build.name = obj.get("name").toString();

                            JSONArray floors = (JSONArray) obj.get("floors");

                            for (Object level : floors) {

                                Floor floor = new Floor(Integer.valueOf(((JSONObject) level).get("level").toString()));
                                Graph graph = new Graph();
                                floor.setGraph(graph);

                                JSONArray waypoints = (JSONArray) ((JSONObject) level).get("waypoints");

                                for (Object waypoint : waypoints) {
                                    String shapeType = ((JSONObject) waypoint).get("shapeType").toString();
                                    ExtendedShape shape;
                                    switch (shapeType) {
                                        case "Classroom":
                                            shape = new Classroom(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                            for (int i = 0; i <= 6; i++) {
                                                List<InputSchedule> listaZile = new ArrayList<>();
                                                for (Object s : (JSONArray) ((JSONObject) waypoint).get("schedule")) {
                                                    if (((JSONObject) s).get("dayOfWeek").toString().equals("Luni") && i == 0) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    } else if (((JSONObject) s).get("dayOfWeek").toString().equals("Marti") && i == 1) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    } else if (((JSONObject) s).get("dayOfWeek").toString().equals("Miercuri") && i == 2) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    } else if (((JSONObject) s).get("dayOfWeek").toString().equals("Joi") && i == 3) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    } else if (((JSONObject) s).get("dayOfWeek").toString().equals("Vineri") && i == 4) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    } else if (((JSONObject) s).get("dayOfWeek").toString().equals("Sambata") && i == 5) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    } else if (((JSONObject) s).get("dayOfWeek").toString().equals("Duminica") && i == 6) {
                                                        InputSchedule input = new InputSchedule(((JSONObject) s).get("group").toString(), ((JSONObject) s).get("startTime").toString(), ((JSONObject) s).get("finishTime").toString(), ((JSONObject) s).get("course").toString());
                                                        listaZile.add(input);
                                                    }

                                                }
                                                ((Classroom) shape).mapaInputuri.put(i, listaZile);
                                            }
                                            break;
                                        case "Bathroom":
                                            shape = new Bathroom(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                            break;
                                        case "Elevator":
                                            shape = new Elevator(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                            break;
                                        case "Office":
                                            shape = new Office(new Point(Double.valueOf(((JSONObject) waypoint).get("x").toString()), Double.valueOf(((JSONObject) waypoint).get("y").toString())));
                                            JSONArray profs = (JSONArray) ((JSONObject) waypoint).get("professors");
                                            for (Object p : profs) {
                                                ((Office) shape).getProfessors().add(p.toString());
                                            }
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
                                    if (!(shape instanceof Bathroom)) {
                                        ((ExtendedRectangle) shape).setType(((JSONObject) waypoint).get("type").toString());
                                    }
                                    ((ExtendedRectangle) shape).setShapeType(((JSONObject) waypoint).get("shapeType").toString());
                                    ((ExtendedRectangle) shape).setId(Integer.valueOf(((JSONObject) waypoint).get("markerId").toString()));
                                    Rectangle rectangle = new Rectangle();
                                    rectangle.setBounds((int) shape.getCenterPoint().getX(), (int) shape.getCenterPoint().getY(), (int) ((ExtendedRectangle) shape).getWidth(), (int) ((ExtendedRectangle) shape).getLength());
                                    ((ExtendedRectangle) shape).setRectangle(rectangle);

                                    floor.getShapes().add(shape);
                                    if (graph.graph.isEmpty()) {
                                        graph.addInitialShape(shape);
                                    } else {
                                        addShapeToGraph((ExtendedRectangle) shape, floor.getShapes(), graph);
                                        graph.setOrder();
                                    }

                                }

                                JSONArray hallways = (JSONArray) ((JSONObject) level).get("hallways");

                                for (Object hallway : hallways) {

                                    String shapeType = ((JSONObject) hallway).get("shapeType").toString();
                                    ExtendedShape shape = new Hallway(new Point(Double.valueOf(((JSONObject) hallway).get("x").toString()), Double.valueOf(((JSONObject) hallway).get("y").toString())));

                                    shape.setColor(((JSONObject) hallway).get("color").toString());
                                    shape.setStartPoint(new Point(shape.getCenterPoint().getX() + Double.valueOf(((JSONObject) hallway).get("width").toString()) / 2, shape.getCenterPoint().getY() + Double.valueOf(((JSONObject) hallway).get("length").toString()) / 2));
                                    ((ExtendedRectangle) shape).setWidth(Double.valueOf(((JSONObject) hallway).get("width").toString()));
                                    ((ExtendedRectangle) shape).setLength(Double.valueOf(((JSONObject) hallway).get("length").toString()));
                                    ((ExtendedRectangle) shape).setName(((JSONObject) hallway).get("name").toString());
                                    ((ExtendedRectangle) shape).setShapeType(((JSONObject) hallway).get("shapeType").toString());
                                    ((ExtendedRectangle) shape).setId(Integer.valueOf(((JSONObject) hallway).get("markerId").toString()));
                                    Rectangle rectangle = new Rectangle();
                                    rectangle.setBounds((int) shape.getCenterPoint().getX(), (int) shape.getCenterPoint().getY(), (int) ((ExtendedRectangle) shape).getWidth(), (int) ((ExtendedRectangle) shape).getLength());
                                    ((ExtendedRectangle) shape).setRectangle(rectangle);

                                    floor.getShapes().add(shape);
                                    if (graph.graph.isEmpty()) {
                                        graph.addInitialShape(shape);
                                    } else {
                                        addShapeToGraph((ExtendedRectangle) shape, floor.getShapes(), graph);
                                        graph.setOrder();
                                    }
                                }
                                build.getFloors().put(floor.getLevel(), floor);
                            }

                            Platform.runLater(() -> {
                                HBox buildingHBox = new HBox();
                                buildingHBox.setPadding(new Insets(5, 5, 5, 5));
                                buildingHBox.setAlignment(Pos.CENTER);
                                buildingHBox.setSpacing(10);
                                buildingHBox.setStyle("-fx-border-color: black;"
                                        + "-fx-background-color: white;"
                                        + "-fx-background-radius: 5;"
                                        + "-fx-border-width: 1;"
                                        + "-fx-border-style: dashed;"
                                        + "-fx-border-radius: 5;");
                                TextField buildName = new TextField(objct.toString().replaceFirst("[.][^.]+$", ""));
                                buildName.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
                                Label nameLabel = new Label("Building Name:");
                                HBox leftBox = new HBox(nameLabel, buildName);
                                leftBox.setAlignment(Pos.CENTER_LEFT);
                                leftBox.setPadding(new Insets(5, 5, 5, 5));
                                leftBox.setSpacing(10);
                                HBox.setHgrow(leftBox, Priority.ALWAYS);
                                Button editBtn = new Button("Edit");
                                editBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
                                editBtn.setSkin(new FadeButtonSkin(editBtn));
                                Button deleteBtn = new Button("X");
                                deleteBtn.setStyle("-fx-background-color: rgb(240,128,128);");
                                deleteBtn.setSkin(new FadeButtonSkin(deleteBtn));
                                HBox rightBox = new HBox(editBtn, deleteBtn);
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

                                deleteBtn.setOnAction(ev -> {
                                    try {
                                        String response = executeDelete("http://localhost:5000/building/" + buildName.getText().replace(" ", "%20"));
                                        CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), buildingHBox, centerScrollPaneVBox.getChildren());
                                    } catch (ConnectException ex) {
                                        Platform.runLater(() -> {
                                            Bounds bounds = pane.localToScreen(pane.getBoundsInLocal());
                                            int x = (int) bounds.getMinX() + (int) pane.getWidth() / 2;
                                            int y = (int) bounds.getMinY() + (int) pane.getHeight() / 2;
                                            ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "Connection to server has failed");
                                            errorPopUp.start(new Stage());
                                        });
                                    } catch (Exception ex) {
                                        Platform.runLater(() -> {
                                            Bounds bounds = pane.localToScreen(pane.getBoundsInLocal());
                                            int x = (int) bounds.getMinX() + (int) pane.getWidth() / 2;
                                            int y = (int) bounds.getMinY() + (int) pane.getHeight() / 2;
                                            ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "An error has occured");
                                            errorPopUp.start(new Stage());
                                        });
                                    }
                                });

                                CustomAnimation.animateInFromRightWithBounce(scene.getWidth(), buildingHBox);
                            });

                        }
                    } catch (ConnectException ex) {
                        Platform.runLater(() -> {
                            Bounds bounds = pane.localToScreen(pane.getBoundsInLocal());
                            int x = (int) bounds.getMinX() + (int) pane.getWidth() / 2;
                            int y = (int) bounds.getMinY() + (int) pane.getHeight() / 2;
                            ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "Connection to server has failed");
                            errorPopUp.start(new Stage());
                        });
                    } catch (Exception ex) {
                        Platform.runLater(() -> {
                            Bounds bounds = pane.localToScreen(pane.getBoundsInLocal());
                            int x = (int) bounds.getMinX() + (int) pane.getWidth() / 2;
                            int y = (int) bounds.getMinY() + (int) pane.getHeight() / 2;
                            ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "An error has occured");
                            errorPopUp.start(new Stage());
                        });
                    }

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            centerVBox.getChildren().remove(progress);
                        }
                    });
                }
            }).start();
            /*
            
             */
        }
        );

        BorderPane.setMargin(leftVBox,
                new Insets(5, 5, 5, 5));
        BorderPane.setMargin(topHBox,
                new Insets(5, 5, 5, 5));
        BorderPane.setMargin(centerVBox,
                new Insets(5, 5, 5, 5));

        scene = new Scene(pane, 800, 600);

        CustomAnimation.animateInFromLeft(scene.getWidth(), leftVBox);
        CustomAnimation.animateInFromTop(scene.getHeight(), topHBox);
        CustomAnimation.animateTypeWriterText(topLabel,
                "Building Creator");

        stage.setScene(scene);

        stage.setTitle(
                "Building Creator");
        //stage.setAlwaysOnTop(true);
        //stage.setFullScreen(true);
        stage.show();
    }

    public static String executeDelete(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public static String executeGet(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
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
