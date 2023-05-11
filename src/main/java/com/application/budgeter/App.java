package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;

// TODO
    // text shortening or wrap text when too long
    // write tests for expensecontroller 
    // merge sort implementation
    // high res button images
        // different images when clicked on
    // searching total for x time period
    // search total for x category x time period
    // expenselist paramter
    // NORMALIZE naming
       // expense -> name
       // category -> category
       // localDate -> date
       // amount -> cost

// known bugs
    // when resizing window, the divider position becomes moveable
    // slight page size difference between expense page and other pages (cuz of anchorpane constraints??)

// QOL
    // auto place mm/dd/yyyy indicator in date textfield
    // info markers for each textfield
    // are you sure? before closing window
    // proportional popup page size
    // less janky popup page repositioning
    // make add work for years under 4 digits

// test
    // data manipulation methods from controller


public class App extends Application {

    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {


        scene = new Scene(loadFXML("MainPage"), 900, 615);
        

        // set icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/appIcon.jpg")));

        // set current size to min
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
    
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
