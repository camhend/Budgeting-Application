package com.application.budgeter;

import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class BudgetController implements Initializable {
    
    @FXML AnchorPane budgetPage;
    @FXML private Label budgetTitle;

    // month section
    @FXML MenuButton monthMenu; 
    @FXML Label monthTitle; 

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

    // save
    @FXML private Button saveBudgetButton;

    BudgetModel budgetModel = new BudgetModel();
    ExpenseList expenseList;
    ExpenseModel expenseModel = new ExpenseModel();


    public void setModels(ExpenseModel expenseModel, BudgetModel budgetModel) {
        // pass expenseList to MainPageController
        this.expenseModel = expenseModel;
        this.budgetModel = budgetModel;

        LocalDate today = LocalDate.now();
        expenseList = expenseModel.getExpenseList(today); // get expenseList for current month
        
        BudgetTable.setItems(budgetModel.getBudgetList());
        updateSpending();
        setProgressBar();

        // delete menubutton context menu in BudgetTable
        ContextMenu contextMenu = new ContextMenu();
        BudgetTable.setContextMenu(contextMenu); // set context menu to tableview

        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(deleteMenuItem);
        deleteListener(deleteMenuItem); 
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        category.setCellValueFactory(new PropertyValueFactory<Budget, String>("category"));
        total.setCellValueFactory(new PropertyValueFactory<Budget, Double>("total"));
        spent.setCellValueFactory(new PropertyValueFactory<Budget, Double>("spent"));
        remaining.setCellValueFactory(new PropertyValueFactory<Budget, Double>("remaining"));
        formatCurrencyColumns();

        // formatting page elements
        setAnchorPaneConstraints();
        BudgetTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void submit(ActionEvent event) {
        try {
            String categoryName = categoryTextField.getText();
            double categoryLimit = Double.parseDouble(limitTextField.getText());
            budgetModel.addBudget(categoryName, 0, categoryLimit);
            categoryTextField.clear();
            limitTextField.clear();
        }
        catch (Exception e) {
            System.out.print(e);
        }
    } // end submit

    private void deleteListener(MenuItem deleteMenuItem) {
        deleteMenuItem.setOnAction((ActionEvent event) -> {
            
            if (expenseList != null) {
                // if category is in expenseList, send alert and return
                for (Expense expense : expenseList) {
                    if (expense.getCategory().equals(BudgetTable.getSelectionModel().getSelectedItem().category)) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Cannot delete category that has expenses");
                        alert.setContentText("Please delete expenses in this category first");
                        alert.showAndWait();
                        return;
                    }
                }
            }
            // get selected row
            Budget selectedBudget = BudgetTable.getSelectionModel().getSelectedItem();
            // remove selected row from the data
            budgetModel.deleteBudget(selectedBudget);
            
        });
    }


    private void updateSpending() {
        // for each category, check category spending in expenseList
        for (Budget budget : budgetModel.getBudgetList()) {
            double newSpent = expenseList.getCategorySpending(budget.category);
            if (newSpent != -1) {
                budgetModel.editBudgetSpent(budget, newSpent);
            }
        }
    }

    public void saveBudget() {
        budgetModel.writeCSV("budget.csv");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText("Budget saved");
        alert.setContentText("Your budget has been saved");
        alert.showAndWait();
    }


    private void setProgressBar() {
        double totalSpent = 0;
        double totalBudget = 0;
        for (Budget budget : budgetModel.getBudgetList()) {
           totalSpent += budget.spent;
           totalBudget += budget.total;
        }
        SpendingBar.setProgress(totalSpent/totalBudget);

        // format totalSpent and totalBudget to 2 decimal places 
        String spent =  String.format("%.2f", totalSpent);
        String total = String.format("%.2f", totalBudget);

        progressTitle.setText("Spent: $" + spent + " / $" + total + " (" + (int)(totalSpent/totalBudget * 100) + "%)");
    }
    
    

    private void formatCurrencyColumns() {
        // set cell factory for 
        remaining.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });

        spent.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });

        total.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
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
            
            setWidthConstraints(monthMenu, newVal, .1, .79);
            setWidthConstraints(monthTitle, newVal, .1, .79);

            // save button at right 10% of window
            setWidthConstraints(saveBudgetButton, newVal, .8, .1);   
        });

        // height property listener
        budgetPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeightConstraints(BudgetTable, newVal, 0.40, 0.05);
            setHeightConstraints(SpendingBar, newVal, 0.20, 0.77);
            setHeightConstraints(budgetTitle, newVal, 0.03, 0.92);
            setHeightConstraints(progressTitle, newVal, 0.15, 0.80);
            AnchorPane.setTopAnchor(limitTextField, newVal.doubleValue() * .30);
            AnchorPane.setTopAnchor(categoryTextField, newVal.doubleValue() * .30);
            AnchorPane.setTopAnchor(categoryButton, newVal.doubleValue() * .30);

            AnchorPane.setTopAnchor(monthMenu, newVal.doubleValue() * .1);
            AnchorPane.setTopAnchor(monthTitle, newVal.doubleValue() * .075);

            AnchorPane.setBottomAnchor(saveBudgetButton, newVal.doubleValue() * .72); 
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