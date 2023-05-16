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

    @FXML private SplitPane mainPage; // overall page

    @FXML private AnchorPane menuPage; // Menu anchorpane

    @FXML private Label menuTitle; // title of menu

    // menu buttons
    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;


    // add listener to reapply divider positions if window is resized
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {

        // set menuPage color to gray
        menuPage.setStyle("-fx-background-color: #ECEAEA;");

        addButtonImages(); 

        // get the divider position and set it to 0.20 when the window is resized
        mainPage.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
        });

        setAnchorConstraints();
        
        selectButton(dashboardNavButton); // default page is dashboard
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
        selectButton(dashboardNavButton);
        unselectButton(budgetNavButton);
        unselectButton(expenseNavButton);
    }

    @FXML
    private void switchToBudget() throws IOException {
        switchContentPage("BudgetPage");
        selectButton(budgetNavButton);
        unselectButton(dashboardNavButton);
        unselectButton(expenseNavButton);
    }

    @FXML
    private void switchToExpense() throws IOException {
        switchContentPage("ExpensePage");
        selectButton(expenseNavButton);
        unselectButton(dashboardNavButton);
        unselectButton(budgetNavButton);
    }


    private void selectButton(Button button) {
        // set button to glow
        button.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, #505177, 10, 0.5, 0, 0);");
    }

    private void unselectButton(Button button) {
        // set button to unselected
        button.setStyle("-fx-background-color: #FFFFFF;");
    }


    @FXML
    private void addButtonImages() {
        // set button images to DashboardButton.jpg, BudgetButton.jpg, and ExpenseButton.jpg set image to fit budgetNavButton

        ImageView dashboardImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/dashboardButton.jpg")));
        dashboardNavButton.setGraphic(dashboardImageView);
        // fit width and height to button
        dashboardImageView.fitWidthProperty().bind(dashboardNavButton.widthProperty());
        dashboardImageView.fitHeightProperty().bind(dashboardNavButton.heightProperty());

        ImageView budgetImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/budgetButton1.jpg")));
        budgetNavButton.setGraphic(budgetImageView);
        // fit width and height to button
        budgetImageView.fitWidthProperty().bind(budgetNavButton.widthProperty());
        budgetImageView.fitHeightProperty().bind(budgetNavButton.heightProperty());

        ImageView expenseImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/expenseButton.jpg")));
        expenseNavButton.setGraphic(expenseImageView);
        // fit width and height to button
        expenseImageView.fitWidthProperty().bind(expenseNavButton.widthProperty());
        expenseImageView.fitHeightProperty().bind(expenseNavButton.heightProperty());
    } // end addButtonImages


    // set anchorpane constraints to resize buttons and title when window is resized
    private void setAnchorConstraints() {

        //width
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
        
            mainPage.setDividerPositions(0.20);

            // set left and right button constraints to 18.5 - 81.5%
            AnchorPane.setLeftAnchor(menuTitle, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(menuTitle, menuPage.getWidth()*.185);

            AnchorPane.setLeftAnchor(dashboardNavButton, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(dashboardNavButton, menuPage.getWidth()*.185);

            AnchorPane.setLeftAnchor(expenseNavButton, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(expenseNavButton, menuPage.getWidth()*.185);

            AnchorPane.setLeftAnchor(budgetNavButton, menuPage.getWidth()*.185);
            AnchorPane.setRightAnchor(budgetNavButton, menuPage.getWidth()*.185);
        });

        // height
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
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
        });
    } // end setWidthAnchorConstraints



    // set anchorpane constraints for menu buttons and title
    private void setHeightAnchorConstraints() {
        
    } // end of setAnchorConstraints method 
} // end of MainPageController class
