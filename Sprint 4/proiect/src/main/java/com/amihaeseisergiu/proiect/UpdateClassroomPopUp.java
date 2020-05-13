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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UpdateClassroomPopUp extends Application {

    Scene scene;
    Classroom shape;
    double x;
    double y;
    DrawingPanel drawingPanel;

    public UpdateClassroomPopUp(DrawingPanel drawingPanel, ExtendedShape shape, double x, double y) {
        this.shape = (Classroom) shape;
        this.x = x;
        this.y = y;
        this.drawingPanel = drawingPanel;
    }

    public HBox adaugareInputuri() {
        HBox grupareInputuri = new HBox();
        Label grupa = new Label("Grupa");
        Label oraStart = new Label("Ora inceput");
        Label oraFinal = new Label("Ora final");
        Label materie = new Label("Materie");
        TextField inputGrupa = new TextField();
        TextField inputOraStart = new TextField();
        TextField inputOraFinal = new TextField();
        TextField inputMaterie = new TextField();
        grupareInputuri.getChildren().add(grupa);
        grupareInputuri.getChildren().add(inputGrupa);
        grupareInputuri.getChildren().add(oraStart);
        grupareInputuri.getChildren().add(inputOraStart);
        grupareInputuri.getChildren().add(oraFinal);
        grupareInputuri.getChildren().add(inputOraFinal);
        grupareInputuri.getChildren().add(materie);
        grupareInputuri.getChildren().add(inputMaterie);
        return grupareInputuri;
    }

    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane();

        ExtendedRectangle rectangle = (ExtendedRectangle) shape;
        Label info = new Label("Information About Classroom");
        info.setStyle("-fx-font: 18 arial;");
        HBox topHBox = new HBox();
        topHBox.getChildren().addAll(info);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(10, 10, 10, 10));

        VBox containerOrar = new VBox();
        Button luniAdd = new Button("+luni");
        Button martiAdd = new Button("+marti");
        Button miercuriAdd = new Button("+miercuri");
        Button joiAdd = new Button("+joi");
        Button vineriAdd = new Button("+vineri");
        Button sambataAdd = new Button("+sambata");
        Button duminicaAdd = new Button("+duminica");

        List<String> zileSaptamana = new ArrayList<>();
        zileSaptamana.add("Luni");
        zileSaptamana.add("Marti");
        zileSaptamana.add("Miercuri");
        zileSaptamana.add("Joi");
        zileSaptamana.add("Vineri");
        zileSaptamana.add("Sambata");
        zileSaptamana.add("Duminica");
        List<VBox> listaVboxuri = new ArrayList<>();
        Label widthLabel = new Label("Width: ");
        Label heightLabel = new Label("Height: ");
        TextField widthField = new TextField(String.valueOf(((ExtendedRectangle) shape).getWidth()));
        TextField heightField = new TextField(String.valueOf(((ExtendedRectangle) shape).getLength()));
        HBox width = new HBox();
        width.getChildren().addAll(widthLabel, widthField);
        HBox height = new HBox();
        height.getChildren().addAll(heightLabel, heightField);
        containerOrar.getChildren().addAll(width, height);
        width.setAlignment(Pos.CENTER);
        height.setAlignment(Pos.CENTER);
        ScrollPane scrollOrar = new ScrollPane();
        containerOrar.setAlignment(Pos.CENTER);
        scrollOrar.setContent(containerOrar);
        scrollOrar.setFitToWidth(true);

        for (int i = 0; i <= 6; i++) {
            VBox zi = new VBox();
            Label ziCurenta = new Label(zileSaptamana.get(i));
            zi.getChildren().add(ziCurenta);
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
            containerOrar.getChildren().add(zi);

        }

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
                        grupareInputuri.getChildren().add(removeLuni);
                        listaVboxuri.get(0).getChildren().add(grupareInputuri);
                        removeLuni.setOnAction(event2 -> {
                            listaVboxuri.get(0).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 1:
                        
                        Button removeMarti = new Button("-");
                        grupareInputuri.getChildren().add(removeMarti);
                        listaVboxuri.get(1).getChildren().add(grupareInputuri);
                        removeMarti.setOnAction(event2 -> {
                            listaVboxuri.get(1).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 2:

                        Button removeMiercuri = new Button("-");
                        grupareInputuri.getChildren().add(removeMiercuri);
                        listaVboxuri.get(2).getChildren().add(grupareInputuri);
                        removeMiercuri.setOnAction(event2 -> {
                            listaVboxuri.get(2).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 3:
                        
                        Button removeJoi = new Button("-");
                        grupareInputuri.getChildren().add(removeJoi);
                        listaVboxuri.get(3).getChildren().add(grupareInputuri);
                        removeJoi.setOnAction(event2 -> {
                            listaVboxuri.get(3).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                        
                    case 4:
                        Button removeVineri = new Button("-");
                        grupareInputuri.getChildren().add(removeVineri);
                        listaVboxuri.get(4).getChildren().add(grupareInputuri);
                        removeVineri.setOnAction(event2 -> {
                            listaVboxuri.get(4).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 5:
                        Button removeSambata = new Button("-");
                        grupareInputuri.getChildren().add(removeSambata);
                        listaVboxuri.get(5).getChildren().add(grupareInputuri);
                        removeSambata.setOnAction(event2 -> {
                            listaVboxuri.get(5).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                    case 6:
                        Button removeDuminica = new Button("-");
                        grupareInputuri.getChildren().add(removeDuminica);
                        listaVboxuri.get(6).getChildren().add(grupareInputuri);
                        removeDuminica.setOnAction(event2 -> {
                            listaVboxuri.get(6).getChildren().remove(grupareInputuri);
                            localLista.remove(input);
                            shape.mapaInputuri.put(entry.getKey(), localLista);
                        });
                        break;
                }

            }
        }

        luniAdd.setOnAction(event -> {
            Button removeLuni = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeLuni);
            listaVboxuri.get(0).getChildren().add(grupareInputuri);
            removeLuni.setOnAction(event2 -> {
                listaVboxuri.get(0).getChildren().remove(grupareInputuri);
            });
        });
        martiAdd.setOnAction(event -> {
            Button removeMarti = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeMarti);
            listaVboxuri.get(1).getChildren().add(grupareInputuri);
            removeMarti.setOnAction(event2 -> {
                listaVboxuri.get(1).getChildren().remove(grupareInputuri);
            });
        });
        miercuriAdd.setOnAction(event -> {
            Button removeMiercuri = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeMiercuri);
            listaVboxuri.get(2).getChildren().add(grupareInputuri);
            removeMiercuri.setOnAction(event2 -> {
                listaVboxuri.get(2).getChildren().remove(grupareInputuri);
            });
        });
        joiAdd.setOnAction(event -> {
            Button removeJoi = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeJoi);
            listaVboxuri.get(3).getChildren().add(grupareInputuri);
            removeJoi.setOnAction(event2 -> {
                listaVboxuri.get(3).getChildren().remove(grupareInputuri);
            });
        });
        vineriAdd.setOnAction(event -> {
            Button removeVineri = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeVineri);
            listaVboxuri.get(4).getChildren().add(grupareInputuri);
            removeVineri.setOnAction(event2 -> {
                listaVboxuri.get(4).getChildren().remove(grupareInputuri);
            });
        });
        sambataAdd.setOnAction(event -> {
            Button removeSambata = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeSambata);
            listaVboxuri.get(5).getChildren().add(grupareInputuri);
            removeSambata.setOnAction(event2 -> {
                listaVboxuri.get(5).getChildren().remove(grupareInputuri);
            });
        });
        duminicaAdd.setOnAction(event -> {
            Button removeDuminica = new Button("-");
            HBox grupareInputuri = adaugareInputuri();
            grupareInputuri.getChildren().add(removeDuminica);
            listaVboxuri.get(6).getChildren().add(grupareInputuri);
            removeDuminica.setOnAction(event2 -> {
                listaVboxuri.get(6).getChildren().remove(grupareInputuri);
            });
        });
        ///////////////////////////////////////////////////////////////////////       
        Label name = new Label("Name:");
        TextField nameField = new TextField(rectangle.getName());
        HBox nameHBox = new HBox();
        nameHBox.setAlignment(Pos.CENTER);
        nameHBox.setSpacing(10);
        nameHBox.getChildren().addAll(name, nameField);
        VBox centerVBox = new VBox();

        centerVBox.getChildren().addAll(nameHBox, scrollOrar);
        centerVBox.setAlignment(Pos.TOP_CENTER);
        centerVBox.setPadding(new Insets(10, 10, 10, 10));
        centerVBox.setSpacing(10);
        centerVBox.setStyle("-fx-border-color: black;\n"
                + "-fx-border-radius: 5;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n");

        nameField.setOnAction(event -> {
            rectangle.setName(nameField.getText());
        });

        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.setSpacing(10);
        bottomHBox.setPadding(new Insets(10, 10, 10, 10));
        Button closeBtn = new Button("Save");
        bottomHBox.getChildren().addAll(closeBtn);
        int btnCount = bottomHBox.getChildren().size();
        closeBtn.prefWidthProperty().bind(bottomHBox.widthProperty().divide(btnCount));
        List<InputSchedule> listaInputuri=new ArrayList<>();
        closeBtn.setOnAction(event -> {
            stage.close();
            rectangle.setName(nameField.getText());
           
            int contor = 0;
            int schimbare=1;
            for (int i = 0; i <= 6; i++) {
                schimbare=0;
                List<InputSchedule> localLista = new ArrayList<>();
                Iterator<Map.Entry<Integer, List<InputSchedule>>> itr = shape.mapaInputuri.entrySet().iterator();
                            while(itr.hasNext()) {
                    Map.Entry<Integer, List<InputSchedule>> entry = itr.next();
                    if(entry.getKey()==i)
                    localLista = entry.getValue();
                }

                for (Node child : listaVboxuri.get(i).getChildren()) {
                    String grupa = null;
                    String oraStart = null;
                    String oraFinal = null;
                    String materie=null;

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
                                        schimbare=1;
                                        
                                    }
                                    contor = 0;
                                }

                            }

                        }

                    }
                }
                if (!localLista.isEmpty() && schimbare==1) {
                  
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

        scene = new Scene(pane, 950, 700);

        pane.setStyle("-fx-background-color: transparent;");

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if(x + 10 + scene.getWidth() > screenBounds.getWidth())
        {
            x = x - ((x + 10 + scene.getWidth()) - screenBounds.getWidth());
        }
        if(y + scene.getHeight() / 2 > screenBounds.getHeight())
        {
            y = screenBounds.getHeight() - (scene.getHeight() / 2) - 70;
        }
        if(y - scene.getHeight() / 2 < 0)
        {
            y = scene.getHeight() / 2;
        }
        stage.setX(x + 10);
        stage.setY(y - scene.getHeight() / 2);
        //stage.initStyle(StageStyle.TRANSPARENT);
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
        Label grupa = new Label("Grupa");
        TextField inputGrupa = new TextField();
        inputGrupa.setText(grp);
        inputOraStart.setText(oStart);
        TextField inputOraFinal = new TextField();
        inputOraFinal.setText(oFinal);
        TextField inputMaterie = new TextField();
        inputMaterie.setText(materieFac);
        grupareInputuri.getChildren().add(grupa);
        grupareInputuri.getChildren().add(inputGrupa);
        grupareInputuri.getChildren().add(oraStart);
        grupareInputuri.getChildren().add(inputOraStart);
        grupareInputuri.getChildren().add(oraFinal);
        grupareInputuri.getChildren().add(inputOraFinal);
        grupareInputuri.getChildren().add(materie);
        grupareInputuri.getChildren().add(inputMaterie);
        return grupareInputuri;
    }
}
