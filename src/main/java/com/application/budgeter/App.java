package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
// tableview
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

// TODO
    // resizing
        // anchorpane constraints
        // replace vbox with anchorpane
        // figure out how to lock aspect ratio
    // make use of controllers 
    // read and add fx:id to fxml elements
    // make drop down total time button change text
    // make add expense button add expense to tableview
    // save button???

// problems
    // button images not showing in Menu.fxml
        // possibly because Menu.fxml is being imported into the other pages instead of being loaded by the main app
    // bug occuring saying duplicate columns in tableview

// done / fixed
    // make menu section a single file and use it in each page (done)
        // bugfix: font from imported file needs to loaded by each page
    // change pages (done)
        // use onAction in buttons on pages
        // make sure to use fx:controller in each fxml file
    // add images to menu buttons (done)



public class App extends Application {

    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("DashboardPage"), 900, 615);

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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
