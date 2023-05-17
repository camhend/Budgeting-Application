package com.application.budgeter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import java.time.LocalDate;
import javafx.scene.Node;


public class DashboardController implements Initializable {
    // page elements
    @FXML private AnchorPane dashboardPage;
    @FXML private Label dashboardTitle;

    // charts
    @FXML private PieChart pieChart;
    @FXML private AreaChart<String, Double> areaChart;

    // center budget data elements
    @FXML private Rectangle spendingsRect;
    @FXML private Label flatAmountSpent;
    @FXML private Label percentAmountSpent;
    @FXML private Label daysLeft;
    @FXML private Label transactionsTableTitle;

    // recent transaction table elements
    @FXML private TableView<Expense> transactionsTable;
    @FXML private TableColumn<Expense, String> nameColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, Double> costColumn;

    ExpenseList expenseList = new ExpenseList();
    BudgetModel budgetModel = new BudgetModel();

    public void SetModels(ExpenseList expenseList, BudgetModel budgetModel) {
        // pass expenseList to MainPageController
        this.expenseList = expenseList;
        this.budgetModel = budgetModel;
    }



    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        setAnchorPaneContraints();
        formatTable();
        // set pie chart background to orange
        pieChart.setStyle("-fx-background-color: #CEC9BF;");



 




    }

    public void setAnchorPaneContraints() {
        // listener for adjusting elements' width when window is resized
        dashboardPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthConstraints(dashboardTitle, newVal, .4, .4);
            setWidthConstraints(pieChart, newVal, .05, .6);
            setWidthConstraints(areaChart, newVal, .05, .4);
            setWidthConstraints(spendingsRect, newVal, .4, .4);
            setWidthConstraints(flatAmountSpent, newVal, .4, .4);
            setWidthConstraints(percentAmountSpent, newVal, .4, .4);
            setWidthConstraints(daysLeft, newVal, .4, .4);
            setWidthConstraints(transactionsTableTitle, newVal, .65, .05);
            setWidthConstraints(transactionsTable, newVal, .65, .05);
        });

        // listener for adjusting elements' height when window is resized
        dashboardPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeightConstraints(dashboardTitle, newVal, .03, .92);
            setHeightConstraints(pieChart, newVal, .1, .55);
            setHeightConstraints(areaChart, newVal, .5, .05);
            setHeightConstraints(spendingsRect, newVal, .3, .5);
            setHeightConstraints(flatAmountSpent, newVal, .2, .7);
            setHeightConstraints(percentAmountSpent, newVal, .25, .65);
            setHeightConstraints(daysLeft, newVal, .3, .6);
            setHeightConstraints(transactionsTableTitle, newVal, .07, .88);
            setHeightConstraints(transactionsTable, newVal, .12, .05);
        });
    } // end of setAnchorPaneContraints method


    private void setWidthConstraints(Node element, Number newVal,  double left, double right) {
        AnchorPane.setLeftAnchor(element, newVal.doubleValue() * left);
        AnchorPane.setRightAnchor(element, newVal.doubleValue() * right);
    } // end setWidthConstraints method


    private void setHeightConstraints(Node element, Number newVal,  double top, double bottom) {
        AnchorPane.setTopAnchor(element, newVal.doubleValue() * top);
        AnchorPane.setBottomAnchor(element, newVal.doubleValue() * bottom);
    } // end setHeightConstraints method


    public void formatTable() {
        // set table column resize policy
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
    } // end of formatTableCells method
} // end of DashboardController class