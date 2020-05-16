/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateClassroomPopUp extends Application {

    Scene scene;
    Classroom shape;
    double x;
    double y;
    DrawingPanel drawingPanel;
    List<Hallway> hallways;

    public UpdateClassroomPopUp(DrawingPanel drawingPanel, ExtendedShape shape, double x, double y, List<Hallway> hallways) {
        this.shape = (Classroom) shape;
        this.x = x;
        this.y = y;
        this.drawingPanel = drawingPanel;
        this.hallways = hallways;
    }

    public HBox adaugareInputuri() {
        HBox grupareInputuri = new HBox();
        Label grupa = new Label("Grupa");
        Label oraStart = new Label("Ora inceput");
        Label oraFinal = new Label("Ora final");
        Label materie = new Label("Materie");
        TextField inputGrupa = new TextField();
        inputGrupa.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputGrupa.setMaxWidth(30);
        TextField inputOraStart = new TextField();
        inputOraStart.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputOraStart.setMaxWidth(30);
        TextField inputOraFinal = new TextField();
        inputOraFinal.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputOraFinal.setMaxWidth(30);
        TextField inputMaterie = new TextField();
        inputMaterie.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputMaterie.setMaxWidth(100);
        grupareInputuri.getChildren().add(grupa);
        grupareInputuri.getChildren().add(inputGrupa);
        grupareInputuri.getChildren().add(oraStart);
        grupareInputuri.getChildren().add(inputOraStart);
        grupareInputuri.getChildren().add(oraFinal);
        grupareInputuri.getChildren().add(inputOraFinal);
        grupareInputuri.getChildren().add(materie);
        grupareInputuri.getChildren().add(inputMaterie);
        grupareInputuri.setPadding(new Insets(5, 5, 5, 5));
        grupareInputuri.setSpacing(10);
        grupareInputuri.setAlignment(Pos.CENTER);
        return grupareInputuri;
    }

    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: linear-gradient(#4facfe, #00f2fe)");

        ExtendedRectangle rectangle = (ExtendedRectangle) shape;
        Label info = new Label("Information About Classroom");
        info.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
        HBox topHBox = new HBox();
        topHBox.getChildren().addAll(info);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(10, 10, 10, 10));
        topHBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");

        VBox containerOrar = new VBox();
        Button luniAdd = new Button("Monday");
        luniAdd.setStyle("-fx-background-color: #ffff00;");
        luniAdd.setSkin(new FadeButtonSkin(luniAdd));
        luniAdd.setMaxWidth(Double.MAX_VALUE);
        Button martiAdd = new Button("Tuesday");
        martiAdd.setStyle("-fx-background-color: #ffff00;");
        martiAdd.setSkin(new FadeButtonSkin(martiAdd));
        martiAdd.setMaxWidth(Double.MAX_VALUE);
        Button miercuriAdd = new Button("Wednesday");
        miercuriAdd.setStyle("-fx-background-color: #ffff00;");
        miercuriAdd.setSkin(new FadeButtonSkin(miercuriAdd));
        miercuriAdd.setMaxWidth(Double.MAX_VALUE);
        Button joiAdd = new Button("Thursday");
        joiAdd.setStyle("-fx-background-color: #ffff00;");
        joiAdd.setSkin(new FadeButtonSkin(joiAdd));
        joiAdd.setMaxWidth(Double.MAX_VALUE);
        Button vineriAdd = new Button("Friday");
        vineriAdd.setStyle("-fx-background-color: #ffff00;");
        vineriAdd.setSkin(new FadeButtonSkin(vineriAdd));
        vineriAdd.setMaxWidth(Double.MAX_VALUE);
        Button sambataAdd = new Button("Saturday");
        sambataAdd.setStyle("-fx-background-color: #ffff00;");
        sambataAdd.setSkin(new FadeButtonSkin(sambataAdd));
        sambataAdd.setMaxWidth(Double.MAX_VALUE);
        Button duminicaAdd = new Button("Sunday");
        duminicaAdd.setStyle("-fx-background-color: #ffff00;");
        duminicaAdd.setSkin(new FadeButtonSkin(duminicaAdd));
        duminicaAdd.setMaxWidth(Double.MAX_VALUE);

        List<VBox> listaVboxuri = new ArrayList<>();
        ScrollPane scrollOrar = new ScrollPane();
        containerOrar.setAlignment(Pos.CENTER);
        containerOrar.setPadding(new Insets(5, 5, 5, 5));
        containerOrar.setSpacing(10);
        scrollOrar.setContent(containerOrar);
        scrollOrar.setFitToWidth(true);
        scrollOrar.setPadding(new Insets(5, 5, 5, 5));
        scrollOrar.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollOrar.setStyle("-fx-background-color: transparent;");
        if (stage.isShowing()) {
            Platform.runLater(() -> {
                scrollOrar.lookup(".viewport").setStyle("-fx-background-color: transparent;");
            });
        } else {
            stage.setOnShown(e -> scrollOrar.lookup(".viewport").setStyle("-fx-background-color: transparent;"));
        }

        for (int i = 0; i <= 6; i++) {
            VBox zi = new VBox();
            zi.setAlignment(Pos.CENTER);
            switch (i) {
                case 0:
                    zi.getChildren().add(luniAdd);
                    break;
                case 1:
                    zi.getChildren().add(martiAdd);
                    break;
                case 2:
                    zi.getChildren().add(miercuriAdd);
                    break;
                case 3:
                    zi.getChildren().add(joiAdd);
                    break;

                case 4:
                    zi.getChildren().add(vineriAdd);
                    break;
                case 5:
                    zi.getChildren().add(sambataAdd);
                    break;
                case 6:
                    zi.getChildren().add(duminicaAdd);
                    break;

            }
            listaVboxuri.add(zi);
            listaVboxuri.get(i).setAlignment(Pos.TOP_CENTER);
            listaVboxuri.get(i).setPadding(new Insets(5, 5, 5, 5));
            listaVboxuri.get(i).setSpacing(10);
            listaVboxuri.get(i).setStyle("-fx-border-color: black;"
                    + "-fx-background-color: white;"
                    + "-fx-background-radius: 5;"
                    + "-fx-border-width: 1;"
                    + "-fx-border-style: dashed;"
                    + "-fx-border-radius: 5;");
            containerOrar.getChildren().add(zi);

        }
        List<String> hallwaysNames = new ArrayList<>();
        for (Hallway h : hallways) {
            hallwaysNames.add(h.getName());
        }
        ObservableList<String> optionsHallways = FXCollections.observableArrayList(hallwaysNames);
        final ComboBox comboBoxHallways = new ComboBox(optionsHallways);
        comboBoxHallways.setMaxWidth(100);
        comboBoxHallways.setStyle("-fx-background-color: transparent;"
                + "-fx-border-color: #ffff00;"
                + "-fx-border-width: 3;"
        );
        Iterator<Map.Entry<Integer, List<InputSchedule>>> iter = shape.mapaInputuri.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, List<InputSchedule>> entry = iter.next();
            System.out.println("Key = " + entry.getKey());
            List<InputSchedule> localLista = entry.getValue();
            for (InputSchedule input : localLista) {
                HBox grupareInputuri = setareInputuri(input.getGrupa(), input.getOraStart(), input.getOraFinal(), input.getMaterie());
                switch (entry.getKey()) {
                    case 0:

                        Button removeLuni = new Button("-");
                        removeLuni.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeLuni.setSkin(new FadeButtonSkin(removeLuni));
                        grupareInputuri.getChildren().add(removeLuni);
                        listaVboxuri.get(0).getChildren().add(1, grupareInputuri);
                        removeLuni.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(0).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 1:

                        Button removeMarti = new Button("-");
                        removeMarti.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeMarti.setSkin(new FadeButtonSkin(removeMarti));
                        grupareInputuri.getChildren().add(removeMarti);
                        listaVboxuri.get(1).getChildren().add(1, grupareInputuri);
                        removeMarti.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(1).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 2:

                        Button removeMiercuri = new Button("-");
                        removeMiercuri.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeMiercuri.setSkin(new FadeButtonSkin(removeMiercuri));
                        grupareInputuri.getChildren().add(removeMiercuri);
                        listaVboxuri.get(2).getChildren().add(1, grupareInputuri);
                        removeMiercuri.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(2).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 3:

                        Button removeJoi = new Button("-");
                        removeJoi.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeJoi.setSkin(new FadeButtonSkin(removeJoi));
                        grupareInputuri.getChildren().add(removeJoi);
                        listaVboxuri.get(3).getChildren().add(1, grupareInputuri);
                        removeJoi.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(3).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;

                    case 4:
                        Button removeVineri = new Button("-");
                        removeVineri.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeVineri.setSkin(new FadeButtonSkin(removeVineri));
                        grupareInputuri.getChildren().add(removeVineri);
                        listaVboxuri.get(4).getChildren().add(1, grupareInputuri);
                        removeVineri.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(4).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 5:
                        Button removeSambata = new Button("-");
                        removeSambata.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeSambata.setSkin(new FadeButtonSkin(removeSambata));
                        grupareInputuri.getChildren().add(removeSambata);
                        listaVboxuri.get(5).getChildren().add(1, grupareInputuri);
                        removeSambata.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(5).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 6:
                        Button removeDuminica = new Button("-");
                        removeDuminica.setStyle("-fx-background-color: rgb(240,128,128);");
                        removeDuminica.setSkin(new FadeButtonSkin(removeDuminica));
                        grupareInputuri.getChildren().add(removeDuminica);
                        listaVboxuri.get(6).getChildren().add(1, grupareInputuri);
                        removeDuminica.setOnAction(event2 -> {
                            CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(6).getChildren());
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                }

            }
        }

        luniAdd.setOnAction(event -> {
            Button removeLuni = new Button("-");
            removeLuni.setStyle("-fx-background-color: rgb(240,128,128);");
            removeLuni.setSkin(new FadeButtonSkin(removeLuni));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeLuni);
            listaVboxuri.get(0).getChildren().add(1, grupareInputuri);
            removeLuni.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(0).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        martiAdd.setOnAction(event -> {
            Button removeMarti = new Button("-");
            removeMarti.setStyle("-fx-background-color: rgb(240,128,128);");
            removeMarti.setSkin(new FadeButtonSkin(removeMarti));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeMarti);
            listaVboxuri.get(1).getChildren().add(1, grupareInputuri);
            removeMarti.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(1).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        miercuriAdd.setOnAction(event -> {
            Button removeMiercuri = new Button("-");
            removeMiercuri.setStyle("-fx-background-color: rgb(240,128,128);");
            removeMiercuri.setSkin(new FadeButtonSkin(removeMiercuri));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeMiercuri);
            listaVboxuri.get(2).getChildren().add(1, grupareInputuri);
            removeMiercuri.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(2).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        joiAdd.setOnAction(event -> {
            Button removeJoi = new Button("-");
            removeJoi.setStyle("-fx-background-color: rgb(240,128,128);");
            removeJoi.setSkin(new FadeButtonSkin(removeJoi));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeJoi);
            listaVboxuri.get(3).getChildren().add(1, grupareInputuri);
            removeJoi.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(3).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        vineriAdd.setOnAction(event -> {
            Button removeVineri = new Button("-");
            removeVineri.setStyle("-fx-background-color: rgb(240,128,128);");
            removeVineri.setSkin(new FadeButtonSkin(removeVineri));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeVineri);
            listaVboxuri.get(4).getChildren().add(1, grupareInputuri);
            removeVineri.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(4).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        sambataAdd.setOnAction(event -> {
            Button removeSambata = new Button("-");
            removeSambata.setStyle("-fx-background-color: rgb(240,128,128);");
            removeSambata.setSkin(new FadeButtonSkin(removeSambata));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeSambata);
            listaVboxuri.get(5).getChildren().add(1, grupareInputuri);
            removeSambata.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(5).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        duminicaAdd.setOnAction(event -> {
            Button removeDuminica = new Button("-");
            removeDuminica.setStyle("-fx-background-color: rgb(240,128,128);");
            removeDuminica.setSkin(new FadeButtonSkin(removeDuminica));
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeDuminica);
            listaVboxuri.get(6).getChildren().add(1, grupareInputuri);
            removeDuminica.setOnAction(event2 -> {
                CustomAnimation.animateOutToLeftAndRemove(scene.getWidth(), grupareInputuri, listaVboxuri.get(6).getChildren());
            });

            CustomAnimation.animateInFromRightWithBounceSmall(scene.getWidth(), grupareInputuri);
        });
        ///////////////////////////////////////////////////////////////////////       
        Label name = new Label("Name:");
        TextField nameField = new TextField(rectangle.getName());
        nameField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        HBox nameHBox = new HBox();
        nameHBox.setAlignment(Pos.CENTER);
        nameHBox.setSpacing(10);
        nameHBox.getChildren().addAll(name, nameField);
        VBox centerVBox = new VBox();
        centerVBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        
        HBox hallwayHBox = new HBox();
        Label hallwayLabel = new Label("Opens to ");
        hallwayHBox.getChildren().addAll(hallwayLabel,comboBoxHallways);
        hallwayHBox.setAlignment(Pos.CENTER);
        
        Label widthLabel = new Label("Width: ");
        Label heightLabel = new Label("Height: ");
        TextField widthField = new TextField(String.valueOf(((ExtendedRectangle) shape).getWidth()));
        widthField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        TextField heightField = new TextField(String.valueOf(((ExtendedRectangle) shape).getLength()));
        heightField.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        HBox width = new HBox();
        width.setSpacing(10);
        width.getChildren().addAll(widthLabel, widthField);
        HBox height = new HBox();
        height.setSpacing(10);
        height.getChildren().addAll(heightLabel, heightField);
        width.setAlignment(Pos.CENTER);
        height.setAlignment(Pos.CENTER);

        centerVBox.getChildren().addAll(nameHBox, width, height, hallwayHBox, scrollOrar);
        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setPadding(new Insets(10, 10, 10, 10));
        centerVBox.setSpacing(10);

        nameField.setOnAction(event -> {
            rectangle.setName(nameField.getText());
        });

        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.setSpacing(10);
        bottomHBox.setPadding(new Insets(10, 10, 10, 10));
        bottomHBox.setStyle("-fx-border-color: black;"
                + "-fx-background-color: white;"
                + "-fx-background-radius: 5;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: dashed;"
                + "-fx-border-radius: 5;");
        Button closeBtn = new Button("Save");
        closeBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
        closeBtn.setSkin(new FadeButtonSkin(closeBtn));

        bottomHBox.getChildren().addAll(closeBtn);
        int btnCount = bottomHBox.getChildren().size();
        closeBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));
        closeBtn.setOnAction(event -> {
            stage.close();
            rectangle.setName(nameField.getText());
            if (comboBoxHallways.getValue() != null) {
                for (Hallway h : hallways) {
                    if (h.getName().equals(comboBoxHallways.getValue())) {
                        ((ExtendedRectangle) shape).setHallway(h);
                    }
                }
            }
            int contor = 0;
            int schimbare = 1;
            for (int i = 0; i <= 6; i++) {
                schimbare = 0;
                List<InputSchedule> localLista = new ArrayList<>();
                Iterator<Map.Entry<Integer, List<InputSchedule>>> itr = shape.mapaInputuri.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<Integer, List<InputSchedule>> entry = itr.next();
                    if (entry.getKey() == i) {
                        localLista = entry.getValue();
                    }
                }

                for (Node child : listaVboxuri.get(i).getChildren()) {
                    String grupa = null;
                    String oraStart = null;
                    String oraFinal = null;
                    String materie = null;

                    if (child instanceof HBox) {
                        for (Node child1 : ((HBox) child).getChildren()) {
                            if (contor == 0) {
                                if (child1 instanceof TextField) {
                                    grupa = (((TextField) child1).getText());

                                    contor++;

                                }
                            } else if (contor == 1) {
                                if (child1 instanceof TextField) {
                                    oraStart = (((TextField) child1).getText());

                                    contor++;

                                }
                            } else if (contor == 2) {
                                if (child1 instanceof TextField) {
                                    oraFinal = (((TextField) child1).getText());

                                    contor++;

                                }
                            } else if (contor == 3) {
                                if (child1 instanceof TextField) {
                                    materie = (((TextField) child1).getText());

                                    InputSchedule grupareInputuri = new InputSchedule(grupa, oraStart, oraFinal, materie);
                                    int ok = 0;
                                    if (!localLista.isEmpty()) {
                                        for (InputSchedule input : localLista) {
                                            if ((input.equals(grupareInputuri))) {
                                                ok = 1;
                                                break;

                                            }
                                        }

                                    }
                                    if (ok == 0 || localLista.isEmpty()) {
                                        localLista.add(grupareInputuri);
                                        schimbare = 1;

                                    }
                                    contor = 0;
                                }

                            }

                        }

                    }
                }
                if (!localLista.isEmpty() && schimbare == 1) {

                    System.out.println(i);
                    shape.mapaInputuri.put(i, localLista);
                }
            }

            // cod bagat de mine
            drawingPanel.deleteShape(rectangle);
            double initialWidth = rectangle.getWidth();
            double initialHeight = rectangle.getLength();
            rectangle.setLength(Double.valueOf(heightField.getText()));
            rectangle.setWidth(Double.valueOf(widthField.getText()));
            rectangle.getRectangle().setSize((int) rectangle.getWidth(), (int) rectangle.getLength());
            if (drawingPanel.checkCollision(rectangle, 0) == true || rectangle.getLength() < 50 || rectangle.getWidth() < 50) {
                rectangle.setLength(initialHeight);
                rectangle.setWidth(initialWidth);
                rectangle.getRectangle().setSize((int) rectangle.getWidth(), (int) rectangle.getLength());
                drawingPanel.drawAll();
            } else {
                drawingPanel.deleteShapeFromGraph(rectangle);
                drawingPanel.getShapes().add(rectangle);
                drawingPanel.addShapeToGraph(rectangle);
                drawingPanel.setOrder();
                drawingPanel.drawAll();
                drawingPanel.getIds().add(rectangle.getId());
            }
            rectangle.setStartPoint(new Point(rectangle.getCenterPoint().getX() + rectangle.getWidth() / 2, rectangle.getCenterPoint().getY() + rectangle.getLength() / 2));
            rectangle.getRectangle().setBounds((int) rectangle.getCenterPoint().getX(), (int) rectangle.getCenterPoint().getY(), (int) rectangle.getWidth(), (int) rectangle.getLength());
            // cod bagat de mine
        });

        pane.setTop(topHBox);
        pane.setCenter(centerVBox);
        pane.setBottom(bottomHBox);

        BorderPane.setMargin(topHBox, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(centerVBox, new Insets(5, 5, 5, 5));
        BorderPane.setMargin(bottomHBox, new Insets(5, 5, 5, 5));

        scene = new Scene(pane, 600, 600);

        CustomAnimation.animateInFromLeftWithBounceSmall(scene.getWidth(), centerVBox);
        CustomAnimation.animateInFromTopWithBounceSmall(scene.getHeight(), topHBox);
        CustomAnimation.animateInFromBottomWithBounceSmall(scene.getHeight(), bottomHBox);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if (x + 10 + scene.getWidth() > screenBounds.getWidth()) {
            x = x - ((x + 10 + scene.getWidth()) - screenBounds.getWidth());
        }
        if (y + scene.getHeight() / 2 > screenBounds.getHeight()) {
            y = screenBounds.getHeight() - (scene.getHeight() / 2) - 70;
        }
        if (y - scene.getHeight() / 2 < 0) {
            y = scene.getHeight() / 2;
        }
        stage.setX(x + 10);
        stage.setY(y - scene.getHeight() / 2);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(new Color(0.6, 0.6, 0.6, 0.2));
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    public HBox setareInputuri(String grp, String oStart, String oFinal, String materieFac) {
        HBox grupareInputuri = new HBox();
        Label oraStart = new Label("Ora inceput");
        Label oraFinal = new Label("Ora final");
        Label materie = new Label("Materie");
        TextField inputOraStart = new TextField();
        inputOraStart.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputOraStart.setMaxWidth(30);
        Label grupa = new Label("Grupa");
        TextField inputGrupa = new TextField();
        inputGrupa.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputGrupa.setMaxWidth(30);
        inputGrupa.setText(grp);
        inputOraStart.setText(oStart);
        TextField inputOraFinal = new TextField();
        inputOraFinal.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputOraFinal.setMaxWidth(30);
        inputOraFinal.setText(oFinal);
        TextField inputMaterie = new TextField();
        inputMaterie.setStyle("-fx-background-color: transparent; -fx-border-color: #0099ff; -fx-border-width: 0 0 1 0;");
        inputMaterie.setMaxWidth(100);
        inputMaterie.setText(materieFac);
        grupareInputuri.getChildren().add(grupa);
        grupareInputuri.getChildren().add(inputGrupa);
        grupareInputuri.getChildren().add(oraStart);
        grupareInputuri.getChildren().add(inputOraStart);
        grupareInputuri.getChildren().add(oraFinal);
        grupareInputuri.getChildren().add(inputOraFinal);
        grupareInputuri.getChildren().add(materie);
        grupareInputuri.getChildren().add(inputMaterie);
        grupareInputuri.setPadding(new Insets(5, 5, 5, 5));
        grupareInputuri.setSpacing(10);
        grupareInputuri.setAlignment(Pos.CENTER);
        return grupareInputuri;
    }
}
