package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;


public class MainPageController implements Initializable {

    @FXML private SplitPane mainPage;

    @FXML private AnchorPane menuPage; // MenuPage anchorpane

    @FXML private Label menuTitle; 

    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;
    


    // add listener to reapply divider positions if window is resized
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {

        addButtonImages(); 

        // get the divider position and set it to 0.20 when the window is resized
        mainPage.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
        });

        // listener to resize buttons and title width when window is resized
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthAnchorConstraints();
        });
        
        // listener to resize buttons and title height when window is resized
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeightAnchorConstraints();
        });
    } 



    // switches fx:include content page to the page specified by fileName
    public void switchContentPage(String fileName) throws IOException {
        // load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(fileName + ".fxml"));
        AnchorPane newPage = (AnchorPane) loader.load();
        // detect current source page
        AnchorPane currentPage = (AnchorPane) mainPage.getItems().get(1);

        // if content page = source page, do nothing
        if (newPage.getId().equals(currentPage.getId())) {
            return;
        }

        // set the content page to the loaded fxml file
        mainPage.getItems().set(1, newPage);
    }



    // switch pages methods accessed by menu buttons

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



    @FXML
    private void addButtonImages() {
        // set button images to DashboardButton.jpg, BudgetButton.jpg, and ExpenseButton.jpg set image to fit budgetNavButton

        ImageView dashboardImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/DashboardButton.jpg")));
        dashboardNavButton.setGraphic(dashboardImageView);
        // fit width and height to button
        dashboardImageView.fitWidthProperty().bind(dashboardNavButton.widthProperty());
        dashboardImageView.fitHeightProperty().bind(dashboardNavButton.heightProperty());

        ImageView budgetImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/BudgetButton.jpg")));
        budgetNavButton.setGraphic(budgetImageView);
        // fit width and height to button
        budgetImageView.fitWidthProperty().bind(budgetNavButton.widthProperty());
        budgetImageView.fitHeightProperty().bind(budgetNavButton.heightProperty());

        ImageView expenseImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/ExpenseButton.jpg")));
        expenseNavButton.setGraphic(expenseImageView);
        // fit width and height to button
        expenseImageView.fitWidthProperty().bind(expenseNavButton.widthProperty());
        expenseImageView.fitHeightProperty().bind(expenseNavButton.heightProperty());
    } // end addButtonImages


    // set anchorpane constraints for menu buttons and title
    private void setWidthAnchorConstraints() {
        mainPage.setDividerPositions(0.20);
        // 18.5 - 81.5%

        AnchorPane.setLeftAnchor(menuTitle, menuPage.getWidth()*.185);
        AnchorPane.setRightAnchor(menuTitle, menuPage.getWidth()*.185);

        AnchorPane.setLeftAnchor(dashboardNavButton, menuPage.getWidth()*.185);
        AnchorPane.setRightAnchor(dashboardNavButton, menuPage.getWidth()*.185);

        AnchorPane.setLeftAnchor(expenseNavButton, menuPage.getWidth()*.185);
        AnchorPane.setRightAnchor(expenseNavButton, menuPage.getWidth()*.185);

        AnchorPane.setLeftAnchor(budgetNavButton, menuPage.getWidth()*.185);
        AnchorPane.setRightAnchor(budgetNavButton, menuPage.getWidth()*.185);
    } // end setWidthAnchorConstraints



    // set anchorpane constraints for menu buttons and title
    private void setHeightAnchorConstraints() {
        mainPage.setDividerPositions(0.20);
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
    } // end of setAnchorConstraints method 
} // end of MainPageController class
