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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


public class MainPageController implements Initializable {

    // main page elements
    @FXML private SplitPane mainPage;
    @FXML private AnchorPane menuPage;
    @FXML private Label menuTitle;

    // menu buttons
    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;

    // data models
    BudgetModel budgetModel = new BudgetModel();
    ExpenseModel expenseModel = new ExpenseModel();


    //* pass models to controller
    public void setModels(ExpenseModel expenseModel, BudgetModel budgetModel) {
        this.expenseModel = expenseModel;
        this.budgetModel = budgetModel;

        // temporary fix to dashboard not getting models on startup
        try {
            switchContentPage("BudgetPage");
            switchContentPage("DashboardPage"); // set default page to dashboard
        } catch (IOException e) {
            System.out.println("Error loading default page");
        }

        // stop splitpane divider from moving
        mainPage.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
        });
    } // end of setModels method


    //* apply page formatting when controller is created
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        addButtonImages();  // add images to menu buttons
        setAnchorConstraints(); // apply anchorpane constraints to page elements
        selectButton(dashboardNavButton); // select dashboard button by default
    } // end of initialize method


    //***********************/
    // Page Switching methods
    //***********************/

    //* switches main pane in splitpane to new page from fileName
    public void switchContentPage(String fileName) throws IOException {
        
        // load the new and current page
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(fileName + ".fxml"));
        AnchorPane newPage = (AnchorPane) loader.load();
        AnchorPane currentPage = (AnchorPane) mainPage.getItems().get(1);

        // switch pages if new page is not the current page
        if (newPage.getId().equals(currentPage.getId())) 
            return;
        else 
            mainPage.getItems().set(1, newPage);

        // give models to controller of new page
        if (fileName.equals("DashboardPage")) {
            DashboardController controller = loader.getController();
            controller.setModels(expenseModel, budgetModel);
        } else if (fileName.equals("BudgetPage")) {
            BudgetController controller = loader.getController();
            controller.setModels(expenseModel, budgetModel);
        } else if (fileName.equals("ExpensePage")) {
            ExpenseController controller = loader.getController();
            controller.setModels(expenseModel, budgetModel);
        }

        // stop splitpane divider from moving
        mainPage.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20);
        });
    } // end of switchContentPage method


    //* switch page, and select the button that was clicked 

    @FXML
    private void switchToDashboard() throws IOException {
        switchContentPage("DashboardPage");
        selectButton(dashboardNavButton);
        unselectButton(budgetNavButton);
        unselectButton(expenseNavButton);
    } // end of switchToDashboard method

    @FXML
    private void switchToBudget() throws IOException {
        switchContentPage("BudgetPage");
        selectButton(budgetNavButton);
        unselectButton(dashboardNavButton);
        unselectButton(expenseNavButton);
    } // end of switchToBudget method

    @FXML
    private void switchToExpense() throws IOException {
        switchContentPage("ExpensePage");
        selectButton(expenseNavButton);
        unselectButton(dashboardNavButton);
        unselectButton(budgetNavButton);
    } // end of switchToExpense method



    //***********************/
    // Button Styling methods
    //***********************/

    //* set button to glow
    private void selectButton(Button button) { 
        button.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, #505177, 10, 0.5, 0, 0);");
    } // end selectButton

    //* set button to unselected
    private void unselectButton(Button button) {
        button.setStyle("-fx-background-color: #FFFFFF;");
    } // end unselectButton


    //* set button images to appropriate images
    private void addButtonImages() {
        // set images for buttons
        ImageView dashboardImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/dashboardButton.jpg")));
        dashboardNavButton.setGraphic(dashboardImageView);

        ImageView budgetImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/budgetButton.jpg")));
        budgetNavButton.setGraphic(budgetImageView);

        ImageView expenseImageView = new ImageView(new Image(getClass().getResourceAsStream("/com/application/budgeter/images/expenseButton.jpg")));
        expenseNavButton.setGraphic(expenseImageView);

        
        // fit width and height to button
        dashboardImageView.fitWidthProperty().bind(dashboardNavButton.widthProperty());
        dashboardImageView.fitHeightProperty().bind(dashboardNavButton.heightProperty());

        budgetImageView.fitWidthProperty().bind(budgetNavButton.widthProperty());
        budgetImageView.fitHeightProperty().bind(budgetNavButton.heightProperty());

        expenseImageView.fitWidthProperty().bind(expenseNavButton.widthProperty());
        expenseImageView.fitHeightProperty().bind(expenseNavButton.heightProperty());
    } // end addButtonImages



    //*********************/
    // Page Design methods
    //*********************/

    //* set anchorpane constraints to resize buttons and title when window is resized
    private void setAnchorConstraints() {
        // width listener
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20); // set divider position to 20:80 ratio
            setWidthConstraints(menuTitle, newVal, .185, .185); // menu title center 63% of width
            setWidthConstraints(dashboardNavButton, newVal, .185, .185); // dashboard button center 63% of width 
            setWidthConstraints(budgetNavButton, newVal, .185, .185); // budget button center 63% of width
            setWidthConstraints(expenseNavButton, newVal, .185, .185); // expense button center 63% of width
        });
        // height listener
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            mainPage.setDividerPositions(0.20); // set divider position to 20:80 ratio
            setHeightConstraints(menuTitle, newVal, .03, .92); // menu title top 5% of menu pane height

            // menu buttons equally spaced vertically
            setHeightConstraints(dashboardNavButton, newVal, .175, .625); 
            setHeightConstraints(expenseNavButton, newVal, .45, .35);
            setHeightConstraints(budgetNavButton, newVal, .725, .075);
        });
    } // end setWidthAnchorConstraints


    //* set left and right anchor constraints
    private void setWidthConstraints(Node element, Number newVal,  double left, double right) {
        AnchorPane.setLeftAnchor(element, newVal.doubleValue() * left);
        AnchorPane.setRightAnchor(element, newVal.doubleValue() * right);
    } // end setWidthConstraints method


    //* set top and bottom anchor constraints
    private void setHeightConstraints(Node element, Number newVal,  double top, double bottom) {
        AnchorPane.setTopAnchor(element, newVal.doubleValue() * top);
        AnchorPane.setBottomAnchor(element, newVal.doubleValue() * bottom);
    } // end setHeightConstraints method
} // end of MainPageController class
