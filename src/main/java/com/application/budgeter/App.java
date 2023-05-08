package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;

// TODO
    // polish and bug test front end
        // make draft of integration
    // seperate tasks in initialize method into seperate methods
    // delete method
    // edit method
    // merge sort implementation
    // finish integration

// known bugs
    // when resizing window, the divider position becomes moveable
    // slight page size difference between expense page and other pages (cuz of anchorpane constraints??)


public class App extends Application {

    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {


        scene = new Scene(loadFXML("MainPage"), 900, 615);

        // set icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/appIcon.jpg")));

        // set current size to min
        stage.setMinWidth(900);
        stage.setMinHeight(600);
    
        stage.setScene(scene);
        stage.show();
    }
    

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        String path = App.class.getResource(fxml + ".fxml").toString();
        System.out.println("Loading FXML from: " + path);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
