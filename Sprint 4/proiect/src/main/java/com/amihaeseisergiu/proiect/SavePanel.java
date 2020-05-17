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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
                String buildingNames = executeGet("http://localhost:5000/building");
                String response = "";
                JSONParser buildingParser = new JSONParser();
                boolean ok = false;
                JSONArray buildings = (JSONArray) buildingParser.parse(buildingNames);

                Bounds bounds = frame.pane.localToScreen(frame.pane.getBoundsInLocal());
                int x = (int) bounds.getMinX() + (int) frame.pane.getWidth() / 2;
                int y = (int) bounds.getMinY() + (int) frame.pane.getHeight() / 2;
                JSONParser responseParser = new JSONParser();
                for (Object objct : buildings) {
                    if (objct.toString().equals(frame.building.name)) {
                        ok = true;
                        response = executeHTTPRequest("http://localhost:5000/building", frame.building.toJson().toJSONString(), "PUT");
                        JSONObject obj = (JSONObject) responseParser.parse(response);
                        int statusCode = Integer.valueOf(obj.get("Code").toString());
                        String responseText = obj.get("Response").toString();
                        if (responseText.contains("The building is not connected")) {
                            ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "The building is not connected");
                            errorPopUp.start(new Stage());
                        } else if (statusCode == 200) {
                            MainMenu menu = new MainMenu(frame.stage);
                        } else if (statusCode != 200) {
                            ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "An error has occured");
                            errorPopUp.start(new Stage());
                        }
                    }
                }
                if (ok == false) {
                    response = executeHTTPRequest("http://localhost:5000/building", frame.building.toJson().toJSONString(), "POST");
                    JSONObject obj = (JSONObject) responseParser.parse(response);
                    int statusCode = Integer.valueOf(obj.get("Code").toString());
                    String responseText = obj.get("Response").toString();
                    if (responseText.contains("The building is not connected")) {
                        ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "The building is not connected");
                        errorPopUp.start(new Stage());
                    } else if (statusCode == 200) {
                        MainMenu menu = new MainMenu(frame.stage);
                    } else if (statusCode != 200) {
                        ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "An error has occured");
                        errorPopUp.start(new Stage());
                    }
                }
            } catch (ConnectException ex) {
                Bounds bounds = frame.pane.localToScreen(frame.pane.getBoundsInLocal());
                int x = (int) bounds.getMinX() + (int) frame.pane.getWidth() / 2;
                int y = (int) bounds.getMinY() + (int) frame.pane.getHeight() / 2;
                ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "Connection to server has failed");
                errorPopUp.start(new Stage());
            } catch (ParseException ex) {
                Bounds bounds = frame.pane.localToScreen(frame.pane.getBoundsInLocal());
                int x = (int) bounds.getMinX() + (int) frame.pane.getWidth() / 2;
                int y = (int) bounds.getMinY() + (int) frame.pane.getHeight() / 2;
                ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "A parsing error has occured");
                errorPopUp.start(new Stage());
            } catch (Exception ex) {
                Bounds bounds = frame.pane.localToScreen(frame.pane.getBoundsInLocal());
                int x = (int) bounds.getMinX() + (int) frame.pane.getWidth() / 2;
                int y = (int) bounds.getMinY() + (int) frame.pane.getHeight() / 2;
                ErrorPopUp errorPopUp = new ErrorPopUp(x, y, "An error has occured");
                errorPopUp.start(new Stage());
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
            int statusCode = connection.getResponseCode();
            InputStream is;
            if (statusCode > 200 && statusCode <= 500) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("Code", statusCode);
            jsonResponse.put("Response", response.toString());
            return jsonResponse.toString();
            //Get Response  
            //return response.toString();
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
