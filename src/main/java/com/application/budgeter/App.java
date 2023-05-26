package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;


public class App extends Application {

    private static Scene scene; 

    // data models
    private ExpenseModel expenseModel = new ExpenseModel();
    private BudgetModel budgetModel = new BudgetModel();

    @Override
    public void start(Stage stage) throws IOException {
        // read csv files
        budgetModel.readCSV("budget.csv");

        // load MainPage.fxml
        FXMLLoader loader = new FXMLLoader(App.class.getResource("MainPage.fxml"));
        scene = new Scene(loader.load(), 900, 615);

        // pass models to MainPageController
        MainPageController controller = loader.getController();
        controller.setModels(expenseModel, budgetModel);

        // format stage
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/appIcon.jpg")));
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        // set stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); 
    }
}
