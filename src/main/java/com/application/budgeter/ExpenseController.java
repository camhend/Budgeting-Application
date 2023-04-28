package com.application.budgeter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExpenseController implements Initializable {

    // initialize tableview from fxml
    @FXML private TableView<Expense> expenseTable;
    // initialize table columns from fxml
    @FXML private TableColumn<Expense, String> expenseColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, String> dateColumn;
    @FXML private TableColumn<Expense, String> costColumn;

    @FXML private MenuButton totalMenu;
    @FXML private Label total;

    @FXML private Button addExpenseButton;

    @FXML private Button saveExpenseButton;

    @FXML private TextField addExpenseField;
    @FXML private TextField addCategoryField;
    @FXML private TextField addDateField;
    @FXML private TextField addCostField;


    // dummy data (replace with pulling from file)
    ObservableList<Expense> list = FXCollections.observableArrayList(
        new Expense("hotdog", "food", "03/03/2024", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.25"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/02/2022", "$2.50")
    );



    // initialize method (runs when ExpenseController is created)
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // set cell value factory for each column
        expenseColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("expense"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("date"));
        costColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cost"));

        // set tableview resize policy to it will not resize columns past the width of the tableview
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // align columns center
        expenseColumn.setStyle("-fx-alignment: CENTER;");
        categoryColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        costColumn.setStyle("-fx-alignment: CENTER;");

        // add columns to tableview
        expenseTable.getColumns().addAll(expenseColumn, categoryColumn, dateColumn, costColumn);

        // add dummy data to tableview
        expenseTable.setItems(list);

        // calcluate all costs from list
        double totalCost = 0;
        // adds all costs from list to totalCost removing the $ sign
        for (Expense expense : list) {
            totalCost += Double.parseDouble(expense.getCost().substring(1));
        }

        // set totalMenu to All Time
        totalMenu.setText("All Time");
        // set total label to totalCost
        total.setText("$" + totalCost);
    } // end initialize

    

    // adjust font size depending on length of text
    // if months = 0 then calculate all time
    public String calcluateTotal(int months) {

        double totalCost = 0;

        if (months == 0) {
            // adds all costs from list to totalCost removing the $ sign
            for (Expense expense : list) {
                totalCost += Double.parseDouble(expense.getCost().substring(1));
            }
        }
        else // if months != 0 then calculate past months
        {
            // get current date
            LocalDate currentDate = LocalDate.now();
            // get date from months ago
            LocalDate pastDate = currentDate.minusMonths(months);
            // 

            // adds all costs from list to totalCost removing the $ sign
            for (Expense expense : list) {
                // convert expense date to localDate
                LocalDate expenseDate = LocalDate.parse(expense.getDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if (expenseDate.isAfter(pastDate) && expenseDate.isBefore(currentDate) || expenseDate.isEqual(currentDate))
                {
                    totalCost += Double.parseDouble(expense.getCost().substring(1));
                }
            }
        }

        return String.format("%.2f", totalCost);
    } // end calcluateTotal method



    // changes menu button text to selected menu item
    public void changeMenuButton(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        totalMenu.setText(menuItem.getText());
        switch (menuItem.getText()) {
            case "Past Month":
                total.setText("$" + calcluateTotal(1));
                break;
            case "Past 3 Months":
                total.setText("$" + calcluateTotal(3));
                break;
            case "Past 6 Months":
                total.setText("$" + calcluateTotal(6));
                break;
            case "Past 12 Months":
                total.setText("$" + calcluateTotal(12));
                break;
            case "All Time":
                total.setText("$" + calcluateTotal(0)); // 0 means all time
                break;
        }
    } // end changeMenuButton method


    public void updateTotal() {
        // update total button
        switch (totalMenu.getText()) {
            case "Past Month":
                total.setText("$" + calcluateTotal(1));
                break;
            case "Past 3 Months":
                total.setText("$" + calcluateTotal(3));
                break;
            case "Past 6 Months":
                total.setText("$" + calcluateTotal(6));
                break;
            case "Past 12 Months":
                total.setText("$" + calcluateTotal(12));
                break;
            case "All Time":
                total.setText("$" + calcluateTotal(0)); // 0 means all time
                break;
        }
    }
    

    
    // add data from text fields to tableview
    public void addExpense() {
        // if any fields are empty
        if (addExpenseField.getText().isEmpty() || addCategoryField.getText().isEmpty() || addDateField.getText().isEmpty() || addCostField.getText().isEmpty()) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill out all fields");
            alert.showAndWait();
        }
        // if cost is valid format with $
        else if (!addCostField.getText().matches("^\\$?[0-9]+(\\.[0-9]{1,2})?$")) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please enter a valid cost (ex. $1.50)");
            alert.showAndWait();
        }
        // if date is valid mm/dd/yyyy
        else if (!addDateField.getText().matches("^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$")) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please enter a valid date (mm/dd/yyyy)");
            alert.showAndWait();
        }
        // else add data to tableview
        else {
            // add data to tableview
            list.add(new Expense(addExpenseField.getText(), addCategoryField.getText(), addDateField.getText(), addCostField.getText()));
            // clear text fields
            addExpenseField.clear();
            addCategoryField.clear();
            addDateField.clear();
            addCostField.clear();

            updateTotal();
        }
    } // end addExpense method



    // save data from tableview to file
    public void saveExpenses() {

    } // end saveExpenses method
} // end ExpenseController class