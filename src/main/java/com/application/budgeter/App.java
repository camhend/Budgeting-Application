package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;

// TODO
    // seperate observable list for tableview 
        // delete method
        // edit method
        // add method
        // total method
    // merge sort implementation
    // high res button images
        // different images when clicked on

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
