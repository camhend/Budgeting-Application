package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

// TODO
    // resizing
        // anchor constraints
    // change pages
        // use onAction in buttons on pages
        // make sure to use fx:controller in each fxml file
    // make use of controllers
    // push to team
    // read and add fx:id to fxml elements
    // add images to menu buttons


public class App extends Application {

    private static Scene scene;


    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("DashboardPage"), 900, 615);

        // set current size to min
        stage.setMinWidth(900);
        stage.setMinHeight(615);
    
        stage.setScene(scene);
        stage.show();
    }
    

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
