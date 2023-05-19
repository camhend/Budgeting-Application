package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.scene.image.Image;


public class App extends Application {

    private static Scene scene;

    private ExpenseList expenseList = new ExpenseList();
    private BudgetModel budgetModel = new BudgetModel();

    @Override
    public void start(Stage stage) throws IOException {

        // load most recent month's data to models
        expenseList.loadFromCSV("expenses.csv");
        budgetModel.readCSV("budget.csv");

        FXMLLoader loader = new FXMLLoader(App.class.getResource("MainPage.fxml"));

        scene = new Scene(loader.load(), 900, 615);

        // pass models to MainPageController
        MainPageController controller = loader.getController();
        controller.setModels(expenseList, budgetModel);

        // set icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/appIcon.jpg")));

        // set current size to min
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
    
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
