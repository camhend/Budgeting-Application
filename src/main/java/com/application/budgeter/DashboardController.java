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
        pieChart.setStyle("-fx-background-color: #FFA500;");

        // set transaction table title center
        transactionsTableTitle.setStyle("-fx-alignment: CENTER;");

        // make spendingsRect background blue
        spendingsRect.setStyle("-fx-fill: #0000FF;");
        // center align flatAmountSpent, percentAmountSpent, and daysLeft
        flatAmountSpent.setStyle("-fx-alignment: CENTER;");
        percentAmountSpent.setStyle("-fx-alignment: CENTER;");
        daysLeft.setStyle("-fx-alignment: CENTER;");


    }

    public void setAnchorPaneContraints() {
        // listener for adjusting elements' width when window is resized
        dashboardPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // set width of dashboardTitle
            AnchorPane.setRightAnchor(dashboardTitle, newVal.doubleValue() * .4);
            AnchorPane.setLeftAnchor(dashboardTitle, newVal.doubleValue() * .4 );

            // set width of pieChart
            // 5% left, 60% right
            AnchorPane.setLeftAnchor(pieChart, newVal.doubleValue() * .05);
            AnchorPane.setRightAnchor(pieChart, newVal.doubleValue() * .7);

            // set width of areaChart
            // 5% left, 40% right
            AnchorPane.setLeftAnchor(areaChart, newVal.doubleValue() * .05);
            AnchorPane.setRightAnchor(areaChart, newVal.doubleValue() * .4);

            // set width of spendingsRect
            // 40% left, 40% right
            AnchorPane.setLeftAnchor(spendingsRect, newVal.doubleValue() * .4);
            AnchorPane.setRightAnchor(spendingsRect, newVal.doubleValue() * .4);

            // set width of flatAmountSpent
            // 40% left, 40% right
            AnchorPane.setLeftAnchor(flatAmountSpent, newVal.doubleValue() * .4);
            AnchorPane.setRightAnchor(flatAmountSpent, newVal.doubleValue() * .4);

            // set width of percentAmountSpent
            // 40% left, 40% right
            AnchorPane.setLeftAnchor(percentAmountSpent, newVal.doubleValue() * .4);
            AnchorPane.setRightAnchor(percentAmountSpent, newVal.doubleValue() * .4);

            // set width of daysLeft
            // 40% left, 40% right
            AnchorPane.setLeftAnchor(daysLeft, newVal.doubleValue() * .4);
            AnchorPane.setRightAnchor(daysLeft, newVal.doubleValue() * .4);

            // set width of transactionsTableTitle
            // 55% left, 20% right
            AnchorPane.setLeftAnchor(transactionsTableTitle, newVal.doubleValue() * .65);
            AnchorPane.setRightAnchor(transactionsTableTitle, newVal.doubleValue() * .05);

            // set width of transactionsTable
            // 55% left, 20% right
            AnchorPane.setLeftAnchor(transactionsTable, newVal.doubleValue() * .65);
            AnchorPane.setRightAnchor(transactionsTable, newVal.doubleValue() * .05);
        });


        // listener for adjusting elements' height when window is resized
        dashboardPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // set height of dashboardTitle
            // 3% top, 92% bottom
            AnchorPane.setTopAnchor(dashboardTitle, newVal.doubleValue() * .03);
            AnchorPane.setBottomAnchor(dashboardTitle, newVal.doubleValue() * .92);

            // pieChart
            // 10% top, 55% bottom
            AnchorPane.setTopAnchor(pieChart, newVal.doubleValue() * .1);
            AnchorPane.setBottomAnchor(pieChart, newVal.doubleValue() * .55);

            // areaChart
            // 60% top, 5% bottom
            AnchorPane.setTopAnchor(areaChart, newVal.doubleValue() * .5);
            AnchorPane.setBottomAnchor(areaChart, newVal.doubleValue() * .05);

            // spendingsRect
            // 30% top, 50% bottom
            AnchorPane.setTopAnchor(spendingsRect, newVal.doubleValue() * .3);
            AnchorPane.setBottomAnchor(spendingsRect, newVal.doubleValue() * .5);

            // flatAmountSpent
                // 20% top, 70% bottom
            AnchorPane.setTopAnchor(flatAmountSpent, newVal.doubleValue() * .2);
            AnchorPane.setBottomAnchor(flatAmountSpent, newVal.doubleValue() * .7);

            // percentAmountSpent
                // 25% top, 65% bottom
            AnchorPane.setTopAnchor(percentAmountSpent, newVal.doubleValue() * .25);
            AnchorPane.setBottomAnchor(percentAmountSpent, newVal.doubleValue() * .65);

            // daysLeft
                // 30% top, 60% bottom
            AnchorPane.setTopAnchor(daysLeft, newVal.doubleValue() * .3);
            AnchorPane.setBottomAnchor(daysLeft, newVal.doubleValue() * .6);

            // transactionsTableTitle
            // 10% top 85% bottom
            AnchorPane.setTopAnchor(transactionsTableTitle, newVal.doubleValue() * .07);
            AnchorPane.setBottomAnchor(transactionsTableTitle, newVal.doubleValue() * .88);
            
            // transactionsTable
            // 12% top, 5% bottom
            AnchorPane.setTopAnchor(transactionsTable, newVal.doubleValue() * .12);
            AnchorPane.setBottomAnchor(transactionsTable, newVal.doubleValue() * .05);



        });
    } // end of setAnchorPaneContraints method


    public void formatTable() {
        // set column alignment center
        nameColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        costColumn.setStyle("-fx-alignment: CENTER;");

        // set table column resize policy
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
    } // end of formatTableCells method
} // end of DashboardController class