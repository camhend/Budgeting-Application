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

public class BudgetController implements Initializable {
    
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
    }
}
