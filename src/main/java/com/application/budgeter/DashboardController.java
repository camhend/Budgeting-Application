package com.application.budgeter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import java.time.LocalDate;
import javafx.scene.Node;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;


public class DashboardController implements Initializable {
    // page elements
    @FXML private AnchorPane dashboardPage;
    @FXML private Label dashboardTitle;

    // charts
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Double> barChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis amountAxis;

    // central budget data elements
    @FXML private Label flatAmountSpent;
    @FXML private Label percentAmountSpent;
    @FXML private Label daysLeft;
    @FXML private Label transactionsTableTitle;
    
    // month section
    @FXML MenuButton monthMenu; 
    @FXML Label monthTitle; 

    // recent transaction table elements
    @FXML private TableView<Expense> transactionsTable;
    @FXML private TableColumn<Expense, String> nameColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, Double> amountColumn;

    // data models 
    BudgetModel budgetModel = new BudgetModel();
    ExpenseModel expenseModel = new ExpenseModel();

    ExpenseList expenseList;
    BudgetList budgetList;


    //* pass models to controller & set setup elements that require models
    public void setModels(ExpenseModel expenseModel, BudgetModel budgetModel) {
        this.expenseModel = expenseModel;
        this.budgetModel = budgetModel;

        if(!expenseModel.getDateList().isEmpty()) 
            expenseList = getLatestExpenselist();
        else 
            expenseList = new ExpenseList();

        // budgetlist = most recent budgetlist
        ArrayList<String> dates = budgetModel.getDateList();
        String mostRecent = dates.get(dates.size() - 1);
        String year = mostRecent.substring(0,4);
        String month = mostRecent.substring(5);
        if (month.length() == 1) {month = "0" + month;}
        LocalDate date = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // create date object
        budgetList = budgetModel.getBudgetList(date);
        monthMenu.setText(mostRecent); // set monthMenu text to most recent budgetlist

        addPiechart();
        addRecentTransactions();
        formatTable();
        addBarChart();
        addBudgetData();
        setAllMenuButtons();
        setDefaultMonth();
        monthMenu.setText(mostRecent); // set monthMenu text to most recent budgetlist

    } // end of setModels method



    @Override //* setup page when page is loaded
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        setAnchorPaneContraints();
    } // end of initialize method


    //* get latest filename and convert to local date and get expense list
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
    } // end of getLatestExpenselist method



    //********************/
    // PAGE SETUP METHODS
    //********************/

    //* add budget data to pie chart
    private void addPiechart() {
        // get observable list with category names and spent amounts from budget
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Budget budget : budgetList.getBudgetList()) {
            pieChartData.add(new PieChart.Data(budget.getCategory(), budget.getSpent()));
        }

        // set pie chart data
        pieChart.setData(pieChartData);
        pieChart.setTitle("");
    } // end of addPiechart method


    //* add most recent 20 transactions to table
    private void addRecentTransactions() {
        if (expenseList == null) 
            return;

        // get up to last 20 transactions
        ObservableList<Expense> recentTransactions = FXCollections.observableArrayList();
        int numTransactions = expenseList.size();
        if (numTransactions > 20) 
            numTransactions = 20;
        for (int i = 0; i < numTransactions; i++) 
            recentTransactions.add(expenseList.get(i));

        // set table data
        transactionsTable.setItems(recentTransactions);
    } // end of addRecentTransactions method


    //* add budget data to pie chart
    private void addBarChart() {
        // get observable list from budgetmodel
        ObservableList<XYChart.Series<String, Double>> barChartData = FXCollections.observableArrayList();

        // add category and spent to series
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        for (Budget budget : budgetList.getBudgetList()) 
            series.getData().add(new XYChart.Data<>(budget.getCategory(), budget.getSpent()));
        barChartData.add(series);

        // set pie chart data
        barChart.setData(barChartData);
        barChart.setLegendVisible(false);
        barChart.setTitle("Spendings");
    } // end of addBarChart method


    //* add budget data to central budget summary
    private void addBudgetData() {

        // get total spent and total budget
        double totalSpent = 0;
        double totalBudget = 0;
        for (Budget budget : budgetList.getBudgetList()) {
           totalSpent += budget.spent;
           totalBudget += budget.total;
        }

        // format totalSpent and totalBudget to 2 decimal places 
        String spent =  "$" + String.format("%.2f", totalSpent);
        String total = "$" + String.format("%.2f", totalBudget);

        // calculate percent spent
        double percentAmount = (totalSpent / totalBudget) * 100;
        String percentAmountString = "$" + String.format("%.2f", percentAmount) + "%";

        // calculate days left in month
        LocalDate currentDate = LocalDate.now();
        String daysLeft = Integer.toString(currentDate.lengthOfMonth() - currentDate.getDayOfMonth());

        // set labels
        flatAmountSpent.setText(spent + " / " + total + " Spent");
        percentAmountSpent.setText(percentAmountString + " Spent");
        this.daysLeft.setText(daysLeft + " Days Left");
    } // end of addBudgetData method



    //************************/
    // Front End Design Methods
    //************************/

    //* set anchorpane constraints to resize buttons and title when window is resized
    private void setAnchorPaneContraints() {
        // width constraints
        dashboardPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthConstraints(dashboardTitle, newVal, .4, .4); // dashboard title center 20% of page
            setWidthConstraints(pieChart, newVal, .05, .6); // pie chart right 35% of page
            setWidthConstraints(barChart, newVal, .05, .4); // bar chart bottom right 55% of page
            setWidthConstraints(flatAmountSpent, newVal, .4, .4); // flat amount spent center 20% of page
            setWidthConstraints(percentAmountSpent, newVal, .4, .4); // percent amount spent center 20% of page
            setWidthConstraints(daysLeft, newVal, .4, .4); // days left center 20% of page
                setWidthConstraints(transactionsTableTitle, newVal, .65, .05); // transactions table title left 30% of page
            setWidthConstraints(transactionsTable, newVal, .65, .05); // transactions table left 30% of page

            setWidthConstraints(monthMenu, newVal, .1, .79); // month menu left 11% of page
            setWidthConstraints(monthTitle, newVal, .1, .79); // month title left 11% of page
        });

        // height constraints
        dashboardPage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeightConstraints(dashboardTitle, newVal, .03, .92); // dashboard title top 5% of page
            setHeightConstraints(pieChart, newVal, .1, .55); // pie chart top 35% of page
            setHeightConstraints(barChart, newVal, .5, .05); // bar chart bottom 45% of page
            setHeightConstraints(flatAmountSpent, newVal, .17, .73); // flat amount spent top-middle 10% of page
            setHeightConstraints(percentAmountSpent, newVal, .25, .65); // percent amount spent top-middle 10% of page
            setHeightConstraints(daysLeft, newVal, .33, .57); // days left top-middle 10% of page
            setHeightConstraints(transactionsTableTitle, newVal, .07, .88); // transactions table title top 5% of page
            setHeightConstraints(transactionsTable, newVal, .12, .05); // transactions table takes most of page height

            AnchorPane.setTopAnchor(monthMenu, newVal.doubleValue() * .1); // month menu top 10% of page
            AnchorPane.setTopAnchor(monthTitle, newVal.doubleValue() * .075); // month title above month menu
        });
    } // end of setAnchorPaneContraints method


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


    //* Apply formatting to table and set table columns to expense fields
    private void formatTable() {
        // set table columns
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("localDate"));
        amountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));

        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // set table column resize policy

        transactionsTable.setPlaceholder(new Label("No Transactions Yet")); // set placeholder text if no transactions

        // add $ and 2 decimal places to amount column
        amountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else 
                    setText(String.format("$%.2f", item));
            }
        });

        // format date column to MM/dd/yyyy
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) 
                    setText(null);
                else 
                    setText(item.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        });
    } // end of formatTable method


    //* give a menubutton an arraylist of items
    private void setMenuButton(MenuButton menuButton, ArrayList<String> items) {
        for (String item : items) {
            MenuItem menuItem = new MenuItem(item);
            menuButton.getItems().add(menuItem);
            menuItem.setOnAction(this::changeMenuButton);
        }
    } // end of setMenuButton method


    //* set menu items for month menu
    private void setAllMenuButtons() {
        ArrayList<String> dateList = expenseModel.getDateList();
        setMenuButton(monthMenu, dateList);
    } // end setMenuItems method


    //* changes menu button text to selected menu item
    public void changeMenuButton(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        MenuButton menuButton = (MenuButton) menuItem.getParentPopup().getOwnerNode();
        menuButton.setText(menuItem.getText());

        // update month if month menu button is changed
        if (menuButton == monthMenu) {
            updateMonth();
        }
    } // end changeMenuButton method


    //* change expense list to selected month
    private void updateMonth() {
        // get expenselist of selected month
        String year = monthMenu.getText().substring(0, 4); 
        String month = monthMenu.getText().substring(5); 
        if (month.length() == 1) {month = "0" + month;}
        LocalDate date = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // create date object
        expenseList = expenseModel.getExpenseList(date); // get expense list for month

        addRecentTransactions();

        // set month menu with dates of found files
        ArrayList<String> dateList = expenseModel.getDateList();
        monthMenu.getItems().clear();
        setMenuButton(monthMenu, dateList);
    } // end updateMonth method


    //* set month to default month (newest month)
    private void setDefaultMonth() {
        if(expenseModel.getDateList().isEmpty()) {
            return;
        }
        // get newest date
        ArrayList<String> dates = expenseModel.getDateList();
        String newestDate = dates.get(dates.size() - 1);

        // set month menu to newest date and update tableview
        monthMenu.setText(newestDate); 
        monthMenu.getItems().clear();
    } // end setDefaultMonth method
} // end of DashboardController class