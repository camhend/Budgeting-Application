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




public class MenuController implements Initializable {
    @FXML AnchorPane contentPage; // MainPage splitPane
    @FXML AnchorPane menuPage; // MainPage splitPane

    @FXML private Label menuTitle;

    @FXML private Button dashboardNavButton;
    @FXML private Button budgetNavButton;
    @FXML private Button expenseNavButton;

    @Override
    // create initialize method to set button images anchorpane constraints to center x of splitpane and y/2 of splitpane, y/3 of splitpane, and 2y/3 of splitpane and add listener to reapply constraints if window is resized
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // get the divider position and set it to 0.25 when the window is resized
        // splitPane.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
        //     splitPane.setDividerPositions(0.20);
        // });

        // set menutitle anchorpane constraints to top 10% of splitpane and center x of splitpane
        AnchorPane.setTopAnchor(menuTitle, menuPage.getHeight()*.05 - menuTitle.getHeight()/2);
        AnchorPane.setLeftAnchor(menuTitle, menuPage.getWidth()/2 - menuTitle.getWidth()/2);
        // add listener that continuously rebinds the anchorpane constraints to top 10% of splitpane and center x of splitpane if window is resized
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(menuTitle, menuPage.getHeight()*.05 - menuTitle.getHeight()/2);
        });
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(menuTitle, menuPage.getWidth()/2 - menuTitle.getWidth()/2);
        });
  

        // set anchorpane constraints to center of splitpane
        // bind expensenavbutton to menuPage
        AnchorPane.setTopAnchor(expenseNavButton, menuPage.getHeight()/2 - expenseNavButton.getHeight()/2);
        AnchorPane.setLeftAnchor(expenseNavButton, menuPage.getWidth()/2 - expenseNavButton.getWidth()/2);
        // add listener that continuously rebinds the anchorpane constraints to center of splitpane if window is resized
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(expenseNavButton, menuPage.getHeight()/2 - expenseNavButton.getHeight()/2);
        });
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(expenseNavButton, menuPage.getWidth()/2 - expenseNavButton.getWidth()/2);
        });

        // repeat for budgetNavButton but set it to*.75 of splitpane
        AnchorPane.setTopAnchor(budgetNavButton, menuPage.getHeight()*.75 - budgetNavButton.getHeight()/2);
        AnchorPane.setLeftAnchor(budgetNavButton, menuPage.getWidth()/2 - budgetNavButton.getWidth()/2);
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(budgetNavButton, menuPage.getHeight()*.75 - budgetNavButton.getHeight()/2);
        });
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(budgetNavButton, menuPage.getWidth()/2 - budgetNavButton.getWidth()/2);
        });

        // repeat for dashboardNavButton but set it to*.25 of splitpane
        AnchorPane.setTopAnchor(dashboardNavButton, menuPage.getHeight()*.25 - dashboardNavButton.getHeight()/2);
        AnchorPane.setLeftAnchor(dashboardNavButton, menuPage.getWidth()/2 - dashboardNavButton.getWidth()/2);
        menuPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(dashboardNavButton, menuPage.getHeight()*.25 - dashboardNavButton.getHeight()/2);
        });
        menuPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(dashboardNavButton, menuPage.getWidth()/2 - dashboardNavButton.getWidth()/2);
        });
        // add a listener to multiple size of the dashboardNavButton when the window is resized
        dashboardNavButton.prefWidthProperty().bind(menuPage.widthProperty().multiply(0.4));
        dashboardNavButton.prefHeightProperty().bind(menuPage.heightProperty().multiply(0.2));

        // add a listener to multiple size of the budgetNavButton when the window is resized
        budgetNavButton.prefWidthProperty().bind(menuPage.widthProperty().multiply(0.4));
        budgetNavButton.prefHeightProperty().bind(menuPage.heightProperty().multiply(0.2));

        // add a listener to multiple size of the expenseNavButton when the window is resized
        expenseNavButton.prefWidthProperty().bind(menuPage.widthProperty().multiply(0.4));
        expenseNavButton.prefHeightProperty().bind(menuPage.heightProperty().multiply(0.2));
        
        
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