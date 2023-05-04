package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button; 
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.layout.VBox;
// image
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.geometry.Bounds;

import javafx.scene.Node;
import javafx.scene.Parent;



public class MainPageController implements Initializable {

    @FXML private SplitPane mainPage;

    @FXML private AnchorPane menuPage; // MenuPage anchorpane

    @FXML private Label menuTitle;

    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;
    

    // add listener to reapply divider positions if window is resized
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // get the divider position and set it to 0.25 when the window is resized
        mainPage.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
        });

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
            AnchorPane.setTopAnchor(menuTitle, menuPage.getHeight()*.03); 
            AnchorPane.setBottomAnchor(menuTitle, menuPage.getHeight()*.92);

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

    public void switchContentPage(String fileName) throws IOException {
        // load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(fileName + ".fxml"));
        AnchorPane contentPage = (AnchorPane) loader.load();

        // set the content page to the new page
        mainPage.getItems().set(1, contentPage);
        
    }

    @FXML
    private void switchToDashboard() throws IOException {
        switchContentPage("DashboardPage");
        
    }

    @FXML
    private void switchToBudget() throws IOException {
        switchContentPage("BudgetPage");
    }

    @FXML
    private void switchToExpense() throws IOException {
        switchContentPage("ExpensePage");
    }
}
