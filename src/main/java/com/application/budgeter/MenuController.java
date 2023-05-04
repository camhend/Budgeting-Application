package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button; 
import javafx.scene.control.SplitPane.Divider;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
// image
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.geometry.Bounds;

import javafx.scene.Node;





public class MenuController implements Initializable {
    @FXML AnchorPane contentPage; // MainPage splitPane
    @FXML AnchorPane menuPage; // MainPage splitPane

    @FXML private Label menuTitle;

    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;

    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // set button images to DashboardButton.jpg, BudgetButton.jpg, and ExpenseButton.jpg set image to fit budgetNavButton



        // listener to resize buttons and title width when window is resized
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // 18.5 - 81.5%

            AnchorPane.setLeftAnchor(menuTitle, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(menuTitle, menuPage.getWidth()*.185);

            AnchorPane.setLeftAnchor(dashboardNavButton, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(dashboardNavButton, menuPage.getWidth()*.185);

            AnchorPane.setLeftAnchor(expenseNavButton, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(expenseNavButton, menuPage.getWidth()*.185);

            AnchorPane.setLeftAnchor(budgetNavButton, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(budgetNavButton, menuPage.getWidth()*.185);
        });
        
        // listener to resize buttons and title height when window is resized
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // measurements in percentage of height bottom to top

            // anchorpane title takes 90 - 95%
            AnchorPane.setTopAnchor(menuTitle, menuPage.getHeight()*.05); 
            AnchorPane.setBottomAnchor(menuTitle, menuPage.getHeight()*.9);

            // anchorpane dashboard 62.5 - 82.5%
            AnchorPane.setTopAnchor(dashboardNavButton, menuPage.getHeight()*.175); 
            AnchorPane.setBottomAnchor(dashboardNavButton, menuPage.getHeight()*.625);
            

            // anchorpane expense 35 - 55%
            AnchorPane.setTopAnchor(expenseNavButton, menuPage.getHeight()*.45);
            AnchorPane.setBottomAnchor(expenseNavButton, menuPage.getHeight()*.35); 

            // anchorpane budget 7.5 - 27.5%
            AnchorPane.setTopAnchor(budgetNavButton, menuPage.getHeight()*.725); 
            AnchorPane.setBottomAnchor(budgetNavButton, menuPage.getHeight()*.075);

            

            



        });
        

        
    }
    


    @FXML
    private void switchToDashboard() throws IOException {
        App.setRoot("DashboardPage");
    }

    @FXML
    private void switchToBudget() throws IOException {
        App.setRoot("BudgetPage");
    }

    @FXML
    private void switchToExpense() throws IOException {
        App.setRoot("ExpensePage");
    }
}