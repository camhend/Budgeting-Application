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
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.io.FileWriter;
import javafx.scene.control.ContextMenu;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import java.io.BufferedReader;
import java.io.FileReader;



public class ExpenseController implements Initializable {

    @FXML private Label expenseTitle; // title of page

    @FXML private AnchorPane expensePage; // page that holds all the elements
    
    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> expenseColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, Double> costColumn;

    @FXML private MenuButton totalMenu; // menu for tracking different time periods 

    @FXML private Label total; // total amount of money spent in time period
    @FXML private Label totalTitle; // title of total section

    @FXML private Button addExpenseButton; // + button
    @FXML private TextField addExpenseField;
    @FXML private TextField addCategoryField;
    @FXML private TextField addDateField;
    @FXML private TextField addCostField;

    @FXML private Button saveExpenseButton; // saves expense to file

    
    ExpenseList expenses = new ExpenseList(); // list of expenses

    ObservableList<Expense> expenseList = FXCollections.observableArrayList(); // list of expenses to display in tableview
    
    // add expenses to expenseList and display in tableview

    


    // initialize method (runs when ExpenseController is created)
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // load expenses from file
        loadExpenses();

        setAnchorPaneConstraints(); // set constraints for anchor pane
        
        expenseTable.setPlaceholder(new Label("No Expenses Added")); // set placeholder for tableview

        // set cell factory for cost column to format cost to currency (adds $ and .00 to end of cost)
        costColumn.setCellFactory(column -> new TableCell<>() {
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
        

        

        // set cell value factory for each column
        expenseColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("localDate"));
        costColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));

        // set tableview resize policy to it will not resize columns past the width of the tableview
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // align columns center
        expenseColumn.setStyle("-fx-alignment: CENTER;");
        categoryColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        costColumn.setStyle("-fx-alignment: CENTER;");

        // add columns to tableview if not already added
        if (!expenseTable.getColumns().contains(expenseColumn)) {
            // create list of columns to add
            List<TableColumn<Expense, ?>> columns = Arrays.asList(expenseColumn, categoryColumn, dateColumn, costColumn);
            expenseTable.getColumns().addAll(columns);
        }


        // add list to tableview
        expenseTable.setItems(expenseList);

        // calc all time total
        totalMenu.setText("All Time");
        updateTotal();

        // right click to delete row
        // context menu for deleting row
        ContextMenu editingContextMenu = new ContextMenu();
        // menu item for deleting row
        MenuItem deleteMenuItem = new MenuItem("Delete");
        // edit menu item 
        MenuItem editMenuItem = new MenuItem("Edit");
        // add menu item to context menu
        editingContextMenu.getItems().addAll(deleteMenuItem, editMenuItem);

        // set context menu to tableview
        expenseTable.setContextMenu(editingContextMenu);

        // set event handler for when delete menu item is clicked
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // get selected row
                Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
                // remove selected row from list
                expenseList.remove(selectedExpense);
                // update tableview
                expenseTable.setItems(expenseList);
                // update total
                updateTotal();
            }
        });

        // set event handler for when edit menu item is clicked
        // make tableview editable
        // when value is changed check if valid
            // all fields must be filled
            // date is mm/dd/yyyy
            // cost is a number (no letters) add $ sign if not added, make sure 2 decimal format
        

    } // end initialize method



    private void setAnchorPaneConstraints() {
        // listener for adjusting elements' width when window is resized
        expensePage.widthProperty().addListener((obs, oldVal, newVal) -> {
            
            // page title centered
            AnchorPane.setLeftAnchor(expenseTitle, newVal.doubleValue() * .40); 
            AnchorPane.setRightAnchor(expenseTitle, newVal.doubleValue() * .40); 

            // total takes up 10% of window width 
            AnchorPane.setRightAnchor(total, newVal.doubleValue() * .1); 
            AnchorPane.setLeftAnchor(total, newVal.doubleValue() * .8); 
            total.setPrefWidth(newVal.doubleValue() * .1);

            AnchorPane.setLeftAnchor(totalTitle, newVal.doubleValue() * 0.8);
            AnchorPane.setRightAnchor(totalTitle, newVal.doubleValue() * 0.1);  
            totalTitle.setPrefWidth(newVal.doubleValue() * .1);
            AnchorPane.setRightAnchor(totalMenu, total.getWidth() + newVal.doubleValue() * .1);  // total menu next to total label
            // runs after total is resized
            Platform.runLater(() -> {
                AnchorPane.setLeftAnchor(totalTitle, newVal.doubleValue() * 0.8);
                AnchorPane.setRightAnchor(totalTitle, newVal.doubleValue() * 0.1);  
                totalTitle.setPrefWidth(newVal.doubleValue() * .1);
                AnchorPane.setRightAnchor(totalMenu, total.getWidth() + newVal.doubleValue() * .1);  // total menu next to total label
            });

            // tableview takes up 80% of window width centered
            AnchorPane.setLeftAnchor(expenseTable, newVal.doubleValue() * .1);
            AnchorPane.setRightAnchor(expenseTable, newVal.doubleValue() * .1);

            // add buttons above tableview 10% of window each 
            AnchorPane.setLeftAnchor(addExpenseField, newVal.doubleValue() * .1);
            AnchorPane.setRightAnchor(addExpenseField, newVal.doubleValue() * .8);

            AnchorPane.setLeftAnchor(addCategoryField, newVal.doubleValue() * .2);
            AnchorPane.setRightAnchor(addCategoryField, newVal.doubleValue() * .7);

            AnchorPane.setLeftAnchor(addDateField, newVal.doubleValue() * .3);
            AnchorPane.setRightAnchor(addDateField, newVal.doubleValue() * .6);

            AnchorPane.setLeftAnchor(addCostField, newVal.doubleValue() * .4);
            AnchorPane.setRightAnchor(addCostField, newVal.doubleValue() * .5);

            AnchorPane.setLeftAnchor(addExpenseButton, newVal.doubleValue() * .5);
            AnchorPane.setRightAnchor(addExpenseButton, newVal.doubleValue() * .46);

            // save button at right 10% of window
            AnchorPane.setLeftAnchor(saveExpenseButton, newVal.doubleValue() * .8);
            AnchorPane.setRightAnchor(saveExpenseButton, newVal.doubleValue() * .1);    

            
        });

        // listener for adjusting elements' height when window is resized
        expensePage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // page title at top 5% of window
            AnchorPane.setTopAnchor(expenseTitle, newVal.doubleValue() * .03); 
            AnchorPane.setBottomAnchor(expenseTitle, newVal.doubleValue() * .92); 

            AnchorPane.setTopAnchor(total, newVal.doubleValue() * .1); // total cost at top 10% of window
            AnchorPane.setTopAnchor(totalTitle, newVal.doubleValue() * .075); // total title at top 10% of window
            AnchorPane.setTopAnchor(totalMenu, newVal.doubleValue() * .1); // total menu at top 10% of window
            
            // tableview anchorpane takes up 70% of window height
            AnchorPane.setBottomAnchor(expenseTable, 0.0);
            AnchorPane.setTopAnchor(expenseTable, newVal.doubleValue() * .3);

            // add buttons above tableview
            AnchorPane.setBottomAnchor(addExpenseField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addCategoryField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addDateField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addCostField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addExpenseButton, newVal.doubleValue() * .72);

            // save button above tableview
            AnchorPane.setBottomAnchor(saveExpenseButton, newVal.doubleValue() * .72); 
        });
    }

    

    // adjust font size depending on length of text
    // if months = 0 then calculate all time
    public String calcluateTotal(int months) {

        double totalCost = 0;

        if (months == 0) {
            // adds all costs from list to totalCost removing the $ sign
            for (Expense expense : expenseList) {
                // if dollar sign is in front of cost then remove it
                if (Double.toString(expense.getAmount()).charAt(0) == '$') {
                    
                }
                else {
                    totalCost += expense.getAmount();
                }
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
            for (Expense expense : expenseList) {
                // convert expense date to localDate
                LocalDate expenseDate = expense.getLocalDate();
                if (expenseDate.isAfter(pastDate) && expenseDate.isBefore(currentDate) || expenseDate.isEqual(currentDate))
                {
                    totalCost += expense.getAmount();
                }
            }
        }

        return String.format("%.2f", totalCost);
    } // end calcluateTotal method
 


    // changes menu button text to selected menu item
    public void changeMenuButton(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        totalMenu.setText(menuItem.getText());
        updateTotal();
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
        // if date is valid mm/dd/yyyy
        else if (!addDateField.getText().matches("^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$")) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please enter a valid date (mm/dd/yyyy)");
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
        
        // else add data to tableview
        else {
            // if addCostField has $ sign remove it
            if (addCostField.getText().charAt(0) == '$') {
                addCostField.setText(addCostField.getText().substring(1));
            }

            // add 2 decimal places to addCostField
            addCostField.setText(String.format("%.2f", Double.parseDouble(addCostField.getText())));
            
            // add $ back to addCostField
            if (!addCostField.getText().contains("$")) {
                addCostField.setText("$" + addCostField.getText());
            }

            

            // add data to tableview
            // convert data to string
            String name = addExpenseField.getText();
            String category = addCategoryField.getText();
            LocalDate date = LocalDate.parse(addDateField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy")); 
            double cost = Double.parseDouble(addCostField.getText().substring(1));
            Expense newExpense = new Expense(name, category, date, cost);
            
            expenseList.add(newExpense);
            // add expense to tableview
            expenseTable.refresh();

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
        // write data from expenseList to expenses.csv

        try {
            FileWriter csvWriter = new FileWriter("expenses.csv");


            // write data to csv file
            for (Expense expense : expenseList) {
                csvWriter.append(expense.getName());
                csvWriter.append(",");
                csvWriter.append(expense.getCategory());
                csvWriter.append(",");
                csvWriter.append(expense.getLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                csvWriter.append(",");
                csvWriter.append(String.valueOf(expense.getAmount()));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();

            // display success message
            Alert alert = new Alert(AlertType.INFORMATION);

            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Expenses have been saved");
            alert.showAndWait();
        }
        catch (IOException e) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error saving expenses");
            alert.showAndWait();
        }
        
    } // end saveExpenses 

    public void loadExpenses() {
        // read data from expenses.csv to expenseList
        try {
            // clear expenseList
            expenseList.clear();

            // read data from csv file
            BufferedReader csvReader = new BufferedReader(new FileReader("expenses.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String name = data[0];
                String category = data[1];
                LocalDate date = LocalDate.parse(data[2], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                double cost = Double.parseDouble(data[3]);
                Expense newExpense = new Expense(name, category, date, cost);
                expenseList.add(newExpense);
            }
            csvReader.close();
        }
        catch (IOException e) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error loading expenses");
            alert.showAndWait();
        }
    } // end loadExpenses method
} // end ExpenseController class