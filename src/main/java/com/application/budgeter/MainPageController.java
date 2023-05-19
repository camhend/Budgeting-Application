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
import javafx.scene.Node;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Control;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainPageController implements Initializable {

    @FXML private SplitPane mainPage; // overall page
    @FXML private AnchorPane menuPage; // Menu anchorpane
    @FXML private Label menuTitle; // title of menu

    // menu buttons
    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;

    // data models
    ExpenseList expenseList;
    BudgetModel budgetModel;

    // set models when MainPageController is created
    public void setModels(ExpenseList expenseList, BudgetModel budgetModel) {
        this.expenseList = expenseList;
        this.budgetModel = budgetModel;


        // temporary fix to dashboard not getting models on startup
        try {
            switchContentPage("BudgetPage");
            switchContentPage("DashboardPage"); // set default page to dashboard
        } catch (IOException e) {
            System.out.println("Error loading default page");
        }
    }


    // apply page formatting when controler is created
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // get the divider position and set it to 0.20 when the window is resized
        mainPage.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
        });

        addButtonImages();  // add images to menu buttons
        setAnchorConstraints(); // apply anchorpane constraints to page elements
        selectButton(dashboardNavButton); // select dashboard button by default
    } 


    //***********************/
    // Page Switching methods
    //***********************/

    // switches fx:include content page to the page specified by fileName
    public void switchContentPage(String fileName) throws IOException {
        
        // load the new page
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(fileName + ".fxml"));
        AnchorPane newPage = (AnchorPane) loader.load();

        // get the current page
        AnchorPane currentPage = (AnchorPane) mainPage.getItems().get(1);

        // if new page = current page, do nothing
        if (newPage.getId().equals(currentPage.getId())) {
            return;
        }
        else { // else, replace the current page with the new page
            mainPage.getItems().set(1, newPage);
        }

        // add models to controller of new page
        if (fileName.equals("DashboardPage")) {
            DashboardController controller = loader.getController();
            controller.setModels(expenseList, budgetModel);
        } else if (fileName.equals("BudgetPage")) {
            BudgetController controller = loader.getController();
            controller.setModels(expenseList, budgetModel);
        } else if (fileName.equals("ExpensePage")) {
            ExpenseController controller = loader.getController();
            controller.setModels(expenseList, budgetModel);
        }
    }


    // switch page based on menu button clicked and select/unselect buttons

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



    //***********************/
    // Button Styling methods
    //***********************/

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
        // set button images to appropriate images

        ImageView dashboardImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/dashboardButton.jpg")));
        dashboardNavButton.setGraphic(dashboardImageView);
        // fit width and height to button
        dashboardImageView.fitWidthProperty().bind(dashboardNavButton.widthProperty());
        dashboardImageView.fitHeightProperty().bind(dashboardNavButton.heightProperty());

        ImageView budgetImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/budgetButton.jpg")));
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



    //*********************/
    // Page Styling methods
    //*********************/

    // set anchorpane constraints to resize buttons and title when window is resized
    private void setAnchorConstraints() {
        // width listener
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
            setWidthConstraints(menuTitle, newVal, .185, .185);
            setWidthConstraints(dashboardNavButton, newVal, .185, .185);
            setWidthConstraints(budgetNavButton, newVal, .185, .185);
            setWidthConstraints(expenseNavButton, newVal, .185, .185);
        });
        // height listener
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
            setHeightConstraints(menuTitle, newVal, .03, .92);
            setHeightConstraints(dashboardNavButton, newVal, .175, .625);
            setHeightConstraints(expenseNavButton, newVal, .45, .35);
            setHeightConstraints(budgetNavButton, newVal, .725, .075);
        });
    } // end setWidthAnchorConstraints


    private void setWidthConstraints(Node element, Number newVal,  double left, double right) {
        AnchorPane.setLeftAnchor(element, newVal.doubleValue() * left);
        AnchorPane.setRightAnchor(element, newVal.doubleValue() * right);
    } // end setWidthConstraints method


    private void setHeightConstraints(Node element, Number newVal,  double top, double bottom) {
        AnchorPane.setTopAnchor(element, newVal.doubleValue() * top);
        AnchorPane.setBottomAnchor(element, newVal.doubleValue() * bottom);
    } // end setHeightConstraints method
} // end of MainPageController class
