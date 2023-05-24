package com.application.budgeter;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import java.lang.reflect.Array;
import java.time.LocalDate;
import javafx.scene.Node;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.SplitPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


public class DashboardController implements Initializable {
    // page elements
    @FXML private AnchorPane dashboardPage;
    @FXML private Label dashboardTitle;

    // charts
    @FXML private PieChart pieChart;

    @FXML private BarChart<String, Double> barChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis amountAxis;

    // center budget data elements
    @FXML private Label flatAmountSpent;
    @FXML private Label percentAmountSpent;
    @FXML private Label daysLeft;
    @FXML private Label transactionsTableTitle;

    // recent transaction table elements
    @FXML private TableView<Expense> transactionsTable;
    @FXML private TableColumn<Expense, String> nameColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, Double> amountColumn;

    BudgetModel budgetModel = new BudgetModel();
    ExpenseList expenseList;
    ExpenseModel expenseModel = new ExpenseModel();


    public void setModels(ExpenseModel expenseModel, BudgetModel budgetModel) {
        // pass expenseList to MainPageController
        this.expenseModel = expenseModel;
        this.budgetModel = budgetModel;

        if(!expenseModel.getDateList().isEmpty()) {
            expenseList = getLatestExpenselist();
        }

        addPiechart();
        addRecentTransactions();
        formatTable();
        addBarChart();
        addBudgetData();
    } // end of setModels method



    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        setAnchorPaneContraints();
    } // end of initialize method


    private ExpenseList getLatestExpenselist() {
        // get latest date from dateList
        ArrayList<String> dateList = expenseModel.getDateList();
        String date = dateList.get(dateList.size() - 1);

        // convert date to LocalDate object
        String year = date.substring(0, 4); 
        String month = date.substring(5); 
        if (month.length() == 1) {month = "0" + month;}
        LocalDate newestDate = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        // return expense list for month
        return expenseModel.getExpenseList(newestDate); 
    }



    //**********************/
    // format page elements
    //**********************/

    private void addPiechart() {
        // make observable list with category names and spent amounts from budget
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Budget budget : budgetModel.getBudgetList()) {
            pieChartData.add(new PieChart.Data(budget.getCategory(), budget.getSpent()));
        }

        // set pie chart data
        pieChart.setData(pieChartData);
        pieChart.setTitle("Spendings");
    } // end of addPiechart method


    private void addRecentTransactions() {
        // make observable list with category names and spent amounts from budget
        ObservableList<Expense> recentTransactions = FXCollections.observableArrayList();
        // get last 20 transactions, but if there are less than 20, get all of them
        int numTransactions = expenseList.size();
        if (numTransactions > 20) {
            numTransactions = 20;
        }

        for (int i = 0; i < numTransactions; i++) {
            recentTransactions.add(expenseList.get(i));
        }

        // set pie chart data
        transactionsTable.setItems(recentTransactions);
    } // end of addRecentTransactions method


    private void addBarChart() {
        // make observable list with category names and spent amounts from budget
        ObservableList<XYChart.Series<String, Double>> barChartData = FXCollections.observableArrayList();
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (Budget budget : budgetModel.getBudgetList()) {
            series.getData().add(new XYChart.Data<>(budget.getCategory(), budget.getSpent()));
        }

        barChartData.add(series);

        // set pie chart data
        barChart.setData(barChartData);
        barChart.setLegendVisible(false);
        barChart.setTitle("Spendings");
    } // end of addBarChart method


    private void addBudgetData() {
        double totalSpent = 0;
        double totalBudget = 0;
        for (Budget budget : budgetModel.getBudgetList()) {
           totalSpent += budget.spent;
           totalBudget += budget.total;
        }
        // format totalSpent and totalBudget to 2 decimal places 
        String spent =  "$" + String.format("%.2f", totalSpent);
        String total = "$" + String.format("%.2f", totalBudget);


        double percentAmount = (totalSpent / totalBudget) * 100;
        String percentAmountString = "$" + String.format("%.2f", percentAmount) + "%";

        // days left = days in month - current day
        LocalDate currentDate = LocalDate.now();
        String daysLeft = Integer.toString(currentDate.lengthOfMonth() - currentDate.getDayOfMonth());


        flatAmountSpent.setText(spent + " / " + total + " Spent");
        percentAmountSpent.setText(percentAmountString + " Spent");
        this.daysLeft.setText(daysLeft + " Days Left");
    } // end of addBudgetData method



    //************************/
    // Front End Design Methods
    //************************/
    
    public void setAnchorPaneContraints() {
        // listener for adjusting elements' width when window is resized
        dashboardPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthConstraints(dashboardTitle, newVal, .4, .4);
            setWidthConstraints(pieChart, newVal, .05, .6);
            setWidthConstraints(barChart, newVal, .05, .4);
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
            setHeightConstraints(barChart, newVal, .5, .05);
            setHeightConstraints(flatAmountSpent, newVal, .17, .73);
            setHeightConstraints(percentAmountSpent, newVal, .25, .65);
            setHeightConstraints(daysLeft, newVal, .33, .57);
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
        // set table columns
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("localDate"));
        amountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));

        // set table column resize policy
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // format data in table

        // set cell factory for cost column to format cost to currency (adds $ and .00 to end of cost)
        amountColumn.setCellFactory(column -> new TableCell<>() {
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

        // set cell factory for date column to format date to MM/dd/yyyy
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                }
            }
        });
    } // end of formatTable method
} // end of DashboardController class