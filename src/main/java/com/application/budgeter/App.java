package com.application.budgeter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;
import java.io.File;
import java.time.LocalDate;


public class App extends Application {

    private static Scene scene; 

    // data models
    private ExpenseModel expenseModel = new ExpenseModel();
    private BudgetModel budgetModel = new BudgetModel();

    @Override
    public void start(Stage stage) throws IOException {

        // create budgetdata file for current month if no files exist
        createCurrentMonthFiles();

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

        // load stylesheet
        String styleSheetPath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\application\\budgeter\\stylesheet.css";   
        styleSheetPath = styleSheetPath.replace(" ", "%20"); 
        System.out.println("Stylesheet loaded: " + styleSheetPath);
        scene.getStylesheets().add("file:///" + styleSheetPath.replace("\\", "/"));

        stage.show();

        // print working directory (last dir should be \Budgeter)
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }

    private void createCurrentMonthFiles() {
        // budget data directory is empty, create yyyy-mm.csv file for current month
        String projectRootPath = System.getProperty("user.dir") + "\\budgetdata";
        File directory = new File(projectRootPath);

        // check if no files in budgetdata directory
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files.length == 0) {
                // create yyyy-mm.csv file for current month
                LocalDate currentDate = LocalDate.now();
                String currentMonth = "";
                if (currentDate.getMonthValue() > 9) {
                    currentMonth = currentDate.getYear() + "-" + currentDate.getMonthValue();
                } else {
                    currentMonth = currentDate.getYear() + "-0" + currentDate.getMonthValue();
                }
                String filename = currentMonth + ".csv";
                File newFile = new File(projectRootPath + "\\" + filename);
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error: Could not create file");
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(); 
    }
}
