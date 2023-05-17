package com.application.budgeter;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.Node;



public class BudgetController implements Initializable {
    
    @FXML AnchorPane budgetPage;
    @FXML private Label budgetTitle;

    // Budget Table
    @FXML TableView<Budget> BudgetTable;
    @FXML private TableColumn<Budget, String> category;
    @FXML private TableColumn<Budget,Double> total;
    @FXML private TableColumn<Budget,Double> spent;
    @FXML private TableColumn<Budget,Double> remaining;

    // Category text input and button
    @FXML private TextField limitTextField;
    @FXML private TextField categoryTextField;
    @FXML private Button categoryButton;

    // progress bar
    @FXML private ProgressBar SpendingBar;
    @FXML private Label progressTitle;

    BudgetModel budgetModel = new BudgetModel();
    ExpenseList expenseList = new ExpenseList();

    public void SetModels(ExpenseList expenseList, BudgetModel budgetModel) {
        // pass expenseList to MainPageController
        this.expenseList = expenseList;
        this.budgetModel = budgetModel;
    }

    public void submit(ActionEvent event) {
        try {
            String categoryName = categoryTextField.getText();
            double categoryLimit = Double.parseDouble(limitTextField.getText());
            Budget budgetCategory = new Budget(categoryName, 0, categoryLimit);
            BudgetTable.getItems().add(budgetCategory);
        }
        catch (Exception e) {
            System.out.print(e);
        }
    }

    ObservableList<Budget> budgetlist = FXCollections.observableArrayList(
        (new Budget("Food", 300.00, 600.00)),
        (new Budget("Transportation", 160.00, 300.00)),
        (new Budget("Housing", 1000.00, 1000.00)),
        (new Budget("Utillities", 156.00, 225.00)),
        (new Budget("Clothing", 138.00, 200.00)),
        (new Budget("Recreation", 65.00, 150.00))
    );

    public void setProgressBar() {
        double totalSpent = 0;
        double totalBudget = 0;
        for (Budget budget : budgetlist) {
           totalSpent += budget.spent;
           totalBudget += budget.total;
        }
        SpendingBar.setProgress(totalSpent/totalBudget);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        category.setCellValueFactory(new PropertyValueFactory<Budget, String>("category"));
        total.setCellValueFactory(new PropertyValueFactory<Budget, Double>("total"));
        spent.setCellValueFactory(new PropertyValueFactory<Budget, Double>("spent"));
        remaining.setCellValueFactory(new PropertyValueFactory<Budget, Double>("remaining"));
        BudgetTable.setItems(budgetlist);
        setProgressBar();

        // formatting page elements
        setAnchorPaneConstraints();
        BudgetTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    private void setAnchorPaneConstraints() {
        // width property listener
        budgetPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthConstraints(budgetTitle, newVal, 0.40, 0.40); 
            setWidthConstraints(BudgetTable, newVal, 0.10, 0.10); 
            setWidthConstraints(SpendingBar, newVal, 0.30, 0.30); 
            setWidthConstraints(progressTitle, newVal, 0.40, 0.40); 
            setWidthConstraints(categoryTextField, newVal, 0.275, 0.55);  
            setWidthConstraints(limitTextField, newVal, 0.475, 0.35); 
            setWidthConstraints(categoryButton, newVal, 0.675, 0.275); 
        });

        // height property listener
        budgetPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeightConstraints(BudgetTable, newVal, 0.40, 0.10);
            setHeightConstraints(SpendingBar, newVal, 0.20, 0.77);
            setHeightConstraints(budgetTitle, newVal, 0.03, 0.92);
            setHeightConstraints(progressTitle, newVal, 0.15, 0.80);
            AnchorPane.setTopAnchor(limitTextField, newVal.doubleValue() * .30);
            AnchorPane.setTopAnchor(categoryTextField, newVal.doubleValue() * .30);
            AnchorPane.setTopAnchor(categoryButton, newVal.doubleValue() * .30);
        });
    }

    private void setWidthConstraints(Node element, Number newVal,  double left, double right) {
        AnchorPane.setLeftAnchor(element, newVal.doubleValue() * left);
        AnchorPane.setRightAnchor(element, newVal.doubleValue() * right);
    }

    private void setHeightConstraints(Node element, Number newVal,  double top, double bottom) {
        AnchorPane.setTopAnchor(element, newVal.doubleValue() * top);
        AnchorPane.setBottomAnchor(element, newVal.doubleValue() * bottom);
    }
} // end of budget controller class