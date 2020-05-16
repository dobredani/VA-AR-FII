/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import static com.amihaeseisergiu.proiect.MainMenu.executeGet;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Sergiu
 */
public class SavePanel extends HBox {

    MainFrame frame;
    Label editingFloorLabel = new Label("Editing Floor 0");

    public SavePanel(MainFrame frame) {
        this.frame = frame;

        this.setPadding(new Insets(5, 5, 5, 5));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        Button mainMenuBtn = new Button("Main Menu");
        mainMenuBtn.setStyle("-fx-background-color: #0099ff;");
        mainMenuBtn.setSkin(new FadeButtonSkin(mainMenuBtn));
        HBox leftBox = new HBox(mainMenuBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setPadding(new Insets(5, 5, 5, 5));
        leftBox.setSpacing(10);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        editingFloorLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
        HBox middleBox = new HBox(editingFloorLabel);
        middleBox.setAlignment(Pos.CENTER);
        middleBox.setPadding(new Insets(5, 5, 5, 5));
        middleBox.setSpacing(10);
        HBox.setHgrow(middleBox, Priority.ALWAYS);

        Button saveBtn = new Button("Save Building");
        saveBtn.setStyle("-fx-background-color: rgb(86, 205, 110);");
        saveBtn.setSkin(new FadeButtonSkin(saveBtn));
        HBox rightBox = new HBox(saveBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setPadding(new Insets(5, 5, 5, 5));
        rightBox.setSpacing(10);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        this.getChildren().addAll(leftBox, middleBox, rightBox);

        mainMenuBtn.setOnAction(e -> {
            MainMenu menu = new MainMenu(frame.stage);
        });

        saveBtn.setOnAction(e -> {
            try {
                System.out.println(frame.building.toJson().toJSONString());
                String buildingNames = executeGet("http://localhost:5000/building");
                String response = "";
                JSONParser buildingParser = new JSONParser();
                boolean ok = false;
                JSONArray buildings = (JSONArray) buildingParser.parse(buildingNames);

                for (Object objct : buildings) {
                    if (objct.toString().equals(frame.building.name)) {
                        ok = true;
                        response = executeHTTPRequest("http://localhost:5000/building", frame.building.toJson().toJSONString(), "PUT");
                    }
                }
                if (ok == false) {
                    response = executeHTTPRequest("http://localhost:5000/building", frame.building.toJson().toJSONString(), "POST");
                }
                System.out.println(response);
                System.out.println("Succes");
                MainMenu menu = new MainMenu(frame.stage);
            } catch (Exception ex) {
                Logger.getLogger(SavePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public String executeHTTPRequest(String targetURL, String urlParameters, String method) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
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
}
