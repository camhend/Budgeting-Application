package com.application.budgeter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;



public class BudgetController implements Initializable {
    
    @FXML AnchorPane budgetPage;
    @FXML private Label budgetTitle;

    // month section
    @FXML MenuButton monthMenu; 
    @FXML Label monthTitle; 

    // Budget Table
    @FXML TableView<Budget> BudgetTable;
    @FXML private TableColumn<Budget, String> category;
    @FXML private TableColumn<Budget,Double> budgetTotal;
    @FXML private TableColumn<Budget,Double> spent;
    @FXML private TableColumn<Budget,Double> remaining;

    // Category text input and button
    @FXML private TextField limitTextField;
    @FXML private TextField categoryTextField;
    @FXML private Button categoryButton;

    // progress bar
    @FXML private ProgressBar SpendingBar;
    @FXML private Label progressTitle;

    // save budget button
    @FXML private Button saveBudgetButton;

    // add month section
    @FXML private Button addMonthButton;
    @FXML private TextField addMonthTextField;
    @FXML private Label addMonthTitle;

    // data models
    BudgetModel budgetModel = new BudgetModel();
    ExpenseModel expenseModel = new ExpenseModel();

    ExpenseList expenseList;
    BudgetList budgetList;

    //* set data models and setup elements that require data models
    public void setModels(ExpenseModel expenseModel, BudgetModel budgetModel) {
        // pass expenseList to MainPageController
        this.expenseModel = expenseModel;
        this.budgetModel = budgetModel;

        setToNewestLists(); // set budgetList and expenseList to most recent dates'

        BudgetTable.setItems(budgetList.getBudgetList()); // set tableview items to budgetList

        updateSpendings();
        setProgressBar();
        setupDeleteMenu();
        setMonthMenuButtons();
    } // end of setModels method


    @Override //* formatting page elements
    public void initialize(URL arg0, ResourceBundle arg1) {
        formatBudgetTable();
        setAnchorPaneConstraints();
    } // end of initialize method



    //*************************/
    // Data Validation methods
    //*************************/

    //* return true if cost is a valid number (e.g. 10.00, 10, $10.00, $10)
    public boolean isValidCost(String cost) {
        // check if cost is an integer or double
        try {
            // if cost without $ sign is an integer
            double newCost = Double.parseDouble(cost.replace("$", ""));
            if (newCost < 0) { // if cost is negative
                return false;
            }
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    } // end isValidCost method



    //*************************/
    // Data Manipulation methods
    //*************************/

    //* add new month file
    public void addMonth(ActionEvent event) {
        // errpr checking addMonthTextField
        // not empty, characters 1-4 are numbers, character 5 is a dash, characters 6-7 are numbers (01-12)
        if (addMonthTextField.getText().equals("") || addMonthTextField.getText().length() != 7 || 
            !Character.isDigit(addMonthTextField.getText().charAt(0)) || !Character.isDigit(addMonthTextField.getText().charAt(1)) ||
            !Character.isDigit(addMonthTextField.getText().charAt(2)) || !Character.isDigit(addMonthTextField.getText().charAt(3)) ||
            addMonthTextField.getText().charAt(4) != '-' ||
            !Character.isDigit(addMonthTextField.getText().charAt(5)) || !Character.isDigit(addMonthTextField.getText().charAt(6)) ||
            Integer.parseInt(addMonthTextField.getText().substring(5)) > 12 || Integer.parseInt(addMonthTextField.getText().substring(5)) < 1) {
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please enter a valid YYYY-MM date (e.g. 2023-01)");
            alert.showAndWait();
            return;
        }
        
        // get date from addMonthTextField
        String year = addMonthTextField.getText().substring(0,4);
        String month = addMonthTextField.getText().substring(5);

        ArrayList<String> currentMonths = budgetModel.getDateList(); // get list of current months

        if (currentMonths.contains(addMonthTextField.getText())) { // if month already exists

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Month already exists");
            alert.setContentText("Please enter a month that does not already exist");
            alert.showAndWait();
            return;
        }
        
        LocalDate date = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // create date object

        budgetModel.getBudgetList(date); // get budgetList for date

        
        setMonthMenuButtons();

        monthMenu.setText(addMonthTextField.getText()); // set monthMenu text to new month
        updateMonth();

        addMonthTextField.clear(); // clear addMonthTextField

        // alert month created
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Month Created");
        alert.setHeaderText("Month Created");
        alert.setContentText("Month " + addMonthTextField.getText() + " created");
        alert.showAndWait();
    } // end addMonth method


    //* add budget to budgetlist 
    public void submit(ActionEvent event) {
        if (isValidCost(limitTextField.getText()) && !categoryTextField.getText().equals("")) {
            String categoryName = categoryTextField.getText();
            double categoryLimit = Double.parseDouble(limitTextField.getText());

            Budget newBudget = new Budget(categoryName, 0, categoryLimit);

            budgetList.add(newBudget);

            categoryTextField.clear();
            limitTextField.clear();
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please enter a valid Category Name and Dollar Cost limit");
            alert.showAndWait();
        }
    } // end submit method


    //* listener for deleting budget from budgetlist
    private void deleteListener(MenuItem deleteMenuItem) {
        deleteMenuItem.setOnAction((ActionEvent event) -> {

            // get selected row
            Budget selectedBudget = BudgetTable.getSelectionModel().getSelectedItem();

            if (selectedBudget == null) { return; } // if no row is selected, return
            
            // make sure category does not have expenses in expenseList
            if (expenseList != null) {

                ExpenseList fileExpenseList = new ExpenseList();
                String filename = monthMenu.getText() + ".csv";
                fileExpenseList.loadFromCSV(filename); // load expenseList from file

                // if category is in file expenseList, send alert and return
                for (Expense expense : fileExpenseList) {
                    if (expense.getCategory().equals(BudgetTable.getSelectionModel().getSelectedItem().category)) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Cannot delete category that has expenses");
                        alert.setContentText("Please delete expenses in this category first (And Save)");
                        alert.showAndWait();
                        return;
                    }
                }

                // if category is in current expenseList, send alert and return
                for (Expense expense : expenseList) {
                    if (expense.getCategory().equals(BudgetTable.getSelectionModel().getSelectedItem().category)) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Cannot delete category that has expenses");
                        alert.setContentText("Please delete expenses in this category first (And Save)");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            // remove selected row from the data
            budgetList.remove(selectedBudget);
        });
    } // end deleteListener method

    //* update data when month is changed
    private void updateMonth() {
        // get date from menu button
        String date = monthMenu.getText();
        String year = date.substring(0,4);
        String month = date.substring(5);
        if (month.length() == 1) {month = "0" + month;}

        // get budgetList and expenseList from budgetModel and expenseModel
        budgetList = budgetModel.getBudgetList(LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        expenseList = expenseModel.getExpenseList(LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        // update table
        BudgetTable.setItems(budgetList.getBudgetList());

        updateSpendings();
        setProgressBar();
    } // end updateMonth method
    

    //* update spending list record based on expenses in expenselist
    private void updateSpendings() {
        // get budgetlist's according expenseList
        expenseList = expenseModel.getExpenseList(budgetList.getMonthYear());

        for (Budget budget : budgetList.getBudgetList()) {
            // get total spent for each budget category
            double totalSpent = expenseList.getCategorySpending(budget.getCategory());
            if (totalSpent == -1) { totalSpent = 0;}

            budget.setSpent(totalSpent);
        }
    } // end of updateSpendings method


    //* set budgetlist and expenselist to newest set available
    private void setToNewestLists() {
        // create MM/dd/yyyy date string from most recent date in budgetModel
        ArrayList<String> dates = budgetModel.getDateList();
        String mostRecent = dates.get(dates.size() - 1);
        String year = mostRecent.substring(0,4);
        String month = mostRecent.substring(5);
        if (month.length() == 1) {month = "0" + month;}

        // get most recent date
        LocalDate date = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // create date object
        
        // set budgetList and expenseList to most recent dates'
        budgetList = budgetModel.getBudgetList(date);
        expenseList = expenseModel.getExpenseList(date); 

        // set month title to most recent date
        monthMenu.setText(mostRecent);
    } // end of setMostRecentLists method



    //***************/
    // File I/O method
    //***************/

    //* write budget to csv file
    public void saveBudget() {
        budgetModel.saveAll();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText("Budget saved");
        alert.setContentText("Your budget has been saved");
        alert.showAndWait();
    } // end saveBudget method

    

    //*********************/
    // Page Design Methods
    //*********************/


        //******************/
        // Tableview Methods
        //******************/

    //* apply formatting to table
    private void formatBudgetTable() {
        formatCurrencyColumns();

        BudgetTable.setPlaceholder(new Label("No Categories Added"));

        BudgetTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        category.setCellValueFactory(new PropertyValueFactory<Budget, String>("category"));
        budgetTotal.setCellValueFactory(new PropertyValueFactory<Budget, Double>("total"));
        spent.setCellValueFactory(new PropertyValueFactory<Budget, Double>("spent"));
        remaining.setCellValueFactory(new PropertyValueFactory<Budget, Double>("remaining"));
    } // end formatBudgetTable


    //* format all currency columns
    private void formatCurrencyColumns() {
        formatCurrencyColumn(remaining);
        formatCurrencyColumn(spent);
        formatCurrencyColumn(budgetTotal);
    } // end formatCurrencyColumns method


    //* add dollar sign and 2 decimal places to given column
    private void formatCurrencyColumn(TableColumn<Budget, Double> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else 
                    setText(String.format("$%.2f", item));
            }
        });
    } // end formatCurrencyColumn method


        //******************/
        // MenuButton Methods
        //******************/

    private void setMonthMenuButtons() {
        monthMenu.getItems().clear(); // clear menuitems

        // get list of dates from budgetModel
        ArrayList<String> dates = budgetModel.getDateList();

        // create menuitems for each date
        for (String date : dates) {
            MenuItem menuItem = new MenuItem(date);
            monthMenu.getItems().add(menuItem);
            menuItem.setOnAction(this::changeMonthMenuButton);
        }
    } // end setMonthMenuButtons method


    //* changes menu button text to selected menu item
    public void changeMonthMenuButton(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        MenuButton menuButton = (MenuButton) menuItem.getParentPopup().getOwnerNode();
        menuButton.setText(menuItem.getText());
        updateMonth();
    } // end changeMenuButton method


        //******************/
        // Anchorpane Methods
        //******************/

    //* set anchorpane constraints for all elements when window is resized
    private void setAnchorPaneConstraints() {
        // width property listener
        budgetPage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthConstraints(budgetTitle, newVal, 0.40, 0.40); 
            setWidthConstraints(BudgetTable, newVal, 0.10, 0.10); 
            setWidthConstraints(SpendingBar, newVal, 0.30, 0.30); 
            setWidthConstraints(progressTitle, newVal, 0.30, 0.30); 
            setWidthConstraints(categoryTextField, newVal, 0.275, 0.55);  
            setWidthConstraints(limitTextField, newVal, 0.475, 0.35); 
            setWidthConstraints(categoryButton, newVal, 0.675, 0.275);
            
            setWidthConstraints(monthMenu, newVal, .1, .79);
            setWidthConstraints(monthTitle, newVal, .1, .79);

            // save button at right 10% of window
            setWidthConstraints(saveBudgetButton, newVal, .8, .1);   

            setWidthConstraints(addMonthTitle, newVal, .75, .1);
            setWidthConstraints(addMonthButton, newVal, .85, .1);
            setWidthConstraints(addMonthTextField, newVal, .75, .15);
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

            AnchorPane.setTopAnchor(saveBudgetButton, newVal.doubleValue() * .3); 

            AnchorPane.setTopAnchor(addMonthTitle, newVal.doubleValue() * .075);
            AnchorPane.setTopAnchor(addMonthButton, newVal.doubleValue() * .1);
            AnchorPane.setTopAnchor(addMonthTextField, newVal.doubleValue() * .1);
        });
    } // end setAnchorPaneConstraints method


    // set left and right anchor constraints
    private void setWidthConstraints(Node element, Number newVal,  double left, double right) {
        AnchorPane.setLeftAnchor(element, newVal.doubleValue() * left);
        AnchorPane.setRightAnchor(element, newVal.doubleValue() * right);
    } // end setWidthConstraints method


    // set top and bottom anchor constraints
    private void setHeightConstraints(Node element, Number newVal,  double top, double bottom) {
        AnchorPane.setTopAnchor(element, newVal.doubleValue() * top);
        AnchorPane.setBottomAnchor(element, newVal.doubleValue() * bottom);
    } // end setHeightConstraints method

    
        //***************/
        // Other Methods
        //***************/

    //* set progress bar and title to percentage of budget spent
    private void setProgressBar() {
        double totalSpent = 0;
        double totalBudget = 0;
        for (Budget budget : budgetList.getBudgetList()) {
           totalBudget += budget.total;
           double categorySpent = expenseList.getCategorySpending(budget.category);
           if (categorySpent == -1) { categorySpent = 0; }
           totalSpent += categorySpent;
        }

        SpendingBar.setProgress(totalSpent/totalBudget);

        // format totalSpent and totalBudget to 2 decimal places 
        String spent =  String.format("%.2f", totalSpent);
        String total = String.format("%.2f", totalBudget);

        double percentSpent = (totalSpent / totalBudget) * 100;

        // Round percentSpent to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedPercentSpent = decimalFormat.format(percentSpent);

        // prevent NaN error
        if (totalBudget == 0) {formattedPercentSpent = "0";}


        progressTitle.setText("Spent: $" + spent + " / $" + total + " (" + formattedPercentSpent + "%)");
    } // end setProgressBar method


    private void setupDeleteMenu() {
        // delete menubutton context menu in BudgetTable
        ContextMenu contextMenu = new ContextMenu();
        BudgetTable.setContextMenu(contextMenu); // set context menu to tableview

        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(deleteMenuItem);
        deleteListener(deleteMenuItem); 
    } // end of setupDeleteMenu method
} // end of budget controller class