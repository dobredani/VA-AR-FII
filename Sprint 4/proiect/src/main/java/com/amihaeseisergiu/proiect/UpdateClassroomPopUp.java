/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateClassroomPopUp extends Application {

    Scene scene;
    Classroom shape;
    double x;
    double y;
    

    public UpdateClassroomPopUp(ExtendedShape shape, double x, double y) {
        this.shape = (Classroom) shape;
        this.x = x;
        this.y = y;
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

        VBox luni = new VBox();
        luni.getChildren().add(new Label("luni"));
        luni.setAlignment(Pos.CENTER);
        luni.getChildren().add(luniAdd);
        containerOrar.getChildren().add(luni);

        VBox marti = new VBox();
        marti.getChildren().add(new Label("marti"));
        marti.setAlignment(Pos.CENTER);
        marti.getChildren().add(martiAdd);
        containerOrar.getChildren().add(marti);

        VBox miercuri = new VBox();
        miercuri.getChildren().add(new Label("miercuri"));
        miercuri.setAlignment(Pos.CENTER);
        miercuri.getChildren().add(miercuriAdd);
        containerOrar.getChildren().add(miercuri);

        VBox joi = new VBox();
        joi.getChildren().add(new Label("joi"));
        joi.setAlignment(Pos.CENTER);
        joi.getChildren().add(joiAdd);
        containerOrar.getChildren().add(joi);

        VBox vineri = new VBox();
        vineri.getChildren().add(new Label("vineri"));
        vineri.setAlignment(Pos.CENTER);
        vineri.getChildren().add(vineriAdd);
        containerOrar.getChildren().add(vineri);

        VBox sambata = new VBox();
        sambata.getChildren().add(new Label("sambata"));
        sambata.setAlignment(Pos.CENTER);
        sambata.getChildren().add(sambataAdd);
        containerOrar.getChildren().add(sambata);

        VBox duminica = new VBox();
        duminica.getChildren().add(new Label("duminica"));
        duminica.setAlignment(Pos.CENTER);
        duminica.getChildren().add(duminicaAdd);
        containerOrar.getChildren().add(duminica);

       
        ScrollPane scrollOrar = new ScrollPane();
        containerOrar.setAlignment(Pos.CENTER);
        scrollOrar.setContent(containerOrar);
        scrollOrar.setFitToWidth(true);
       
       for(int i=0;i<shape.listaMaterieLuni.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartLuni.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitLuni.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieLuni.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeLuni = new Button("-");
            grupareInputuri.getChildren().add(removeLuni);

            luni.getChildren().add(grupareInputuri);
            removeLuni.setOnAction(event2 -> {
                luni.getChildren().remove(grupareInputuri);
                shape.listaOraStartLuni.remove(index);
                shape.listaOraSfarsitLuni.remove(index);
                shape.listaMaterieLuni.remove(index);
            });
        }
        for(int i=0;i<shape.listaMaterieMarti.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartMarti.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitMarti.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieMarti.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeMarti = new Button("-");
            grupareInputuri.getChildren().add(removeMarti);

            marti.getChildren().add(grupareInputuri);
            removeMarti.setOnAction(event2 -> {
                marti.getChildren().remove(grupareInputuri);
                shape.listaOraStartMarti.remove(index);
                shape.listaOraSfarsitMarti.remove(index);
                shape.listaMaterieMarti.remove(index);
            });
        } 
        for(int i=0;i<shape.listaMaterieMiercuri.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartMiercuri.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitMiercuri.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieMiercuri.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeMiercuri = new Button("-");
            grupareInputuri.getChildren().add(removeMiercuri);

            miercuri.getChildren().add(grupareInputuri);
            removeMiercuri.setOnAction(event2 -> {
                miercuri.getChildren().remove(grupareInputuri);
                shape.listaOraStartMiercuri.remove(index);
                shape.listaOraSfarsitMiercuri.remove(index);
                shape.listaMaterieMiercuri.remove(index);
            });
        }
        for(int i=0;i<shape.listaMaterieJoi.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartJoi.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitJoi.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieJoi.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeJoi = new Button("-");
            grupareInputuri.getChildren().add(removeJoi);

            joi.getChildren().add(grupareInputuri);
            removeJoi.setOnAction(event2 -> {
                joi.getChildren().remove(grupareInputuri);
                shape.listaOraStartJoi.remove(index);
                shape.listaOraSfarsitJoi.remove(index);
                shape.listaMaterieJoi.remove(index);
            });
        } 
        for(int i=0;i<shape.listaMaterieVineri.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartVineri.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitVineri.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieVineri.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeVineri = new Button("-");
            grupareInputuri.getChildren().add(removeVineri);

            vineri.getChildren().add(grupareInputuri);
            removeVineri.setOnAction(event2 -> {
                vineri.getChildren().remove(grupareInputuri);
                shape.listaOraStartVineri.remove(index);
                shape.listaOraSfarsitVineri.remove(index);
                shape.listaMaterieVineri.remove(index);
            });
        } 
        for(int i=0;i<shape.listaMaterieSambata.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartSambata.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitSambata.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieSambata.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeSambata = new Button("-");
            grupareInputuri.getChildren().add(removeSambata);

            sambata.getChildren().add(grupareInputuri);
            removeSambata.setOnAction(event2 -> {
                sambata.getChildren().remove(grupareInputuri);
                shape.listaOraStartSambata.remove(index);
                shape.listaOraSfarsitSambata.remove(index);
                shape.listaMaterieSambata.remove(index);
            });
        } 
        for(int i=0;i<shape.listaMaterieDuminica.size();i++)
        {   final int index=i;
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            inputOraStart.setText(shape.listaOraStartDuminica.get(i));
            TextField inputOraFinal = new TextField();
            inputOraFinal.setText(shape.listaOraSfarsitDuminica.get(i));
            TextField inputMaterie = new TextField();
            inputMaterie.setText(shape.listaMaterieDuminica.get(i));
            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeDuminica = new Button("-");
            grupareInputuri.getChildren().add(removeDuminica);

            duminica.getChildren().add(grupareInputuri);
            removeDuminica.setOnAction(event2 -> {
                duminica.getChildren().remove(grupareInputuri);
                shape.listaOraStartDuminica.remove(index);
                shape.listaOraSfarsitDuminica.remove(index);
                shape.listaMaterieDuminica.remove(index);
            });
        } 
        luniAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeLuni = new Button("-");
            grupareInputuri.getChildren().add(removeLuni);

            luni.getChildren().add(grupareInputuri);

            removeLuni.setOnAction(event2 -> {
                luni.getChildren().remove(grupareInputuri);
            });
        });

        ///////////////////////////////////////////////////////////////////////
        martiAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeMarti = new Button("-");
            grupareInputuri.getChildren().add(removeMarti);

            marti.getChildren().add(grupareInputuri);

            removeMarti.setOnAction(event2 -> {
                marti.getChildren().remove(grupareInputuri);
            });
        });
        /////////////////////////////////////////////////////////////////////
        miercuriAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeMiercuri = new Button("-");
            grupareInputuri.getChildren().add(removeMiercuri);

            miercuri.getChildren().add(grupareInputuri);

            removeMiercuri.setOnAction(event2 -> {
                miercuri.getChildren().remove(grupareInputuri);
            });
        });
        //////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        joiAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeJoi = new Button("-");
            grupareInputuri.getChildren().add(removeJoi);

            joi.getChildren().add(grupareInputuri);

            removeJoi.setOnAction(event2 -> {
                joi.getChildren().remove(grupareInputuri);
            });
        });
        ///////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////
        vineriAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeVineri = new Button("-");
            grupareInputuri.getChildren().add(removeVineri);

            vineri.getChildren().add(grupareInputuri);

            removeVineri.setOnAction(event2 -> {
                vineri.getChildren().remove(grupareInputuri);
            });
        });

        //////////////////////////////////////////////////////////////////////////
        sambataAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeSambata = new Button("-");
            grupareInputuri.getChildren().add(removeSambata);

            sambata.getChildren().add(grupareInputuri);

            removeSambata.setOnAction(event2 -> {
                sambata.getChildren().remove(grupareInputuri);
            });
        });

        //////////////////////////////////////////////////////////////////////////
        duminicaAdd.setOnAction(event -> {
            HBox grupareInputuri = new HBox();
            Label oraStart = new Label("Ora inceput");
            Label oraFinal = new Label("Ora final");
            Label materie = new Label("Materie");
            TextField inputOraStart = new TextField();
            TextField inputOraFinal = new TextField();
            TextField inputMaterie = new TextField();

            grupareInputuri.getChildren().add(oraStart);
            grupareInputuri.getChildren().add(inputOraStart);
            grupareInputuri.getChildren().add(oraFinal);
            grupareInputuri.getChildren().add(inputOraFinal);
            grupareInputuri.getChildren().add(materie);
            grupareInputuri.getChildren().add(inputMaterie);

            Button removeDuminica = new Button("-");
            grupareInputuri.getChildren().add(removeDuminica);

            duminica.getChildren().add(grupareInputuri);

            removeDuminica.setOnAction(event2 -> {
                duminica.getChildren().remove(grupareInputuri);
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

        closeBtn.setOnAction(event -> {
            stage.close();
            rectangle.setName(nameField.getText());
            int contor=1;
            int figuriAdaugateAnterior=0;
            for (Node child : luni.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaLuni*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartLuni.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitLuni.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieLuni.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
            contor=1;
            figuriAdaugateAnterior=0;
            for (Node child : marti.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaMarti*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartMarti.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitMarti.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieMarti.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
            contor=1;
            figuriAdaugateAnterior=0;
            for (Node child : miercuri.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaMiercuri*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartMiercuri.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitMiercuri.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieMiercuri.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
             contor=1;
            figuriAdaugateAnterior=0;
            for (Node child : joi.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaJoi*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartJoi.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitJoi.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieJoi.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
            contor=1;
            figuriAdaugateAnterior=0;
            for (Node child : vineri.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaVineri*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartVineri.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitVineri.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieVineri.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
            contor=1;
            figuriAdaugateAnterior=0;
            for (Node child : sambata.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaSambata*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartSambata.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitSambata.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieSambata.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
            contor=1;
            figuriAdaugateAnterior=0;
            for (Node child : duminica.getChildren()) {
                if (child instanceof HBox) 
                    for (Node child1 : ((HBox) child).getChildren()) {
                        if (child1 instanceof TextField)
                        figuriAdaugateAnterior++;
                        if(figuriAdaugateAnterior>shape.marimeListaDuminica*3)
                        {if(contor==1)
                        {if (child1 instanceof TextField) {
                            shape.listaOraStartDuminica.add(((TextField) child1).getText());
                            contor++;
                        }}
                        else
                        if(contor==2)
                        {if (child1 instanceof TextField) {
                            shape.listaOraSfarsitDuminica.add(((TextField) child1).getText());
                            contor++;
                         
                        }}
                        else
                        {if(contor==3)
                        if (child1 instanceof TextField) {
                            shape.listaMaterieDuminica.add(((TextField) child1).getText());
                            contor=1;
                        }
                        }}
                    }
                
            
        }
         
            shape.marimeListaLuni=shape.listaMaterieLuni.size();
            shape.marimeListaMarti=shape.listaMaterieMarti.size();
            shape.marimeListaMiercuri=shape.listaMaterieMiercuri.size();
             shape.marimeListaJoi=shape.listaMaterieJoi.size();
             shape.marimeListaVineri=shape.listaMaterieVineri.size();
              shape.marimeListaSambata=shape.listaMaterieSambata.size();
               shape.marimeListaDuminica=shape.listaMaterieDuminica.size();
            //oraStart->getText()
        });
        
        pane.setTop(topHBox);
        pane.setCenter(centerVBox);
        pane.setBottom(bottomHBox);

        scene = new Scene(pane, 700, 700);

        pane.setStyle("-fx-background-color: transparent;");
     
        stage.setX(x + 10);
        stage.setY(y - scene.getHeight() / 2);
        //stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(new Color(0.6, 0.6, 0.6, 0.2));
        stage.setScene(scene);
        stage.show();
    }
}
