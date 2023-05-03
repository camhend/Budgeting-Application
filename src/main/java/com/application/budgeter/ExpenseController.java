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
import java.io.IOException;
import java.io.FileWriter;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.scene.control.SplitPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.binding.Bindings;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;



public class ExpenseController implements Initializable {

    // initialize tableview from fxml
    @FXML private TableView<PlaceholderExpense> expenseTable;
    // initialize table columns from fxml
    @FXML private TableColumn<PlaceholderExpense, String> expenseColumn;
    @FXML private TableColumn<PlaceholderExpense, String> categoryColumn;
    @FXML private TableColumn<PlaceholderExpense, String> dateColumn;
    @FXML private TableColumn<PlaceholderExpense, String> costColumn;

    @FXML private MenuButton totalMenu; // menu for tracking different time periods 
    @FXML private Label total;

    @FXML private Button addExpenseButton; // + button
    @FXML private TextField addExpenseField;
    @FXML private TextField addCategoryField;
    @FXML private TextField addDateField;
    @FXML private TextField addCostField;


    @FXML private Button saveExpenseButton; // saves expense to file

    

    // dummy data (replace with pulling from file)
    ObservableList<PlaceholderExpense> list = FXCollections.observableArrayList(
        new PlaceholderExpense("hotdog", "food", "03/03/2024", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.50"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/04/2023", "$2.25"),
        new PlaceholderExpense("hotdog", "food", "03/03/2023", "$1.50"),
        new PlaceholderExpense("notdog", "food", "03/02/2022", "$2.50")
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

        // add columns to tableview if not already added
        if (!expenseTable.getColumns().contains(expenseColumn)) {
            expenseTable.getColumns().addAll(expenseColumn, categoryColumn, dateColumn, costColumn);
        }

        
        // read expenses.csv file and add to list
        try {
            // create new scanner for file
            Scanner scanner = new Scanner(new File("expenses.csv"));
            // while scanner has next line
            while (scanner.hasNextLine()) {
                // create new scanner for line
                Scanner line = new Scanner(scanner.nextLine());
                // use , as delimiter
                line.useDelimiter(",");
                // add new PlaceholderExpense to list
                list.add(new PlaceholderExpense(line.next(), line.next(), line.next(), line.next()));
                // close line scanner
                line.close();
            }
            // close scanner
            scanner.close();
        // catch if file is not found
        } catch (FileNotFoundException e) {
            // create new file
            try {
                new File("expenses.csv").createNewFile();
            // catch if file cannot be created
            } catch (IOException e2) {
                // error message
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error Creating New Expenses File");
                alert.setContentText("An error occured while creating a new expenses file.");
                alert.showAndWait();
            }
        }

        // add dummy data to tableview
        expenseTable.setItems(list);

        // calcluate all costs from list
        double totalCost = 0;
        // adds all costs from list to totalCost removing the $ sign
        for (PlaceholderExpense expense : list) {
            totalCost += Double.parseDouble(expense.getCost().substring(1));
        }

        // set totalMenu to All Time
        totalMenu.setText("All Time");
        // set total label to totalCost
        total.setText("$" + totalCost);

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
                PlaceholderExpense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
                // remove selected row from list
                list.remove(selectedExpense);
                // update tableview
                expenseTable.setItems(list);
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

    

    // adjust font size depending on length of text
    // if months = 0 then calculate all time
    public String calcluateTotal(int months) {

        double totalCost = 0;

        if (months == 0) {
            // adds all costs from list to totalCost removing the $ sign
            for (PlaceholderExpense expense : list) {
                // if dollar sign 
                if (expense.getCost().charAt(0) == '$') {
                    totalCost += Double.parseDouble(expense.getCost().substring(1));
                }
                else {
                    totalCost += Double.parseDouble(expense.getCost());
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
            for (PlaceholderExpense expense : list) {
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
            // add 2 decimal places to addCostField
            addCostField.setText(String.format("%.2f", Double.parseDouble(addCostField.getText().substring(1))));
            
            // if cost doesn't have $ sign then add it
            if (!addCostField.getText().contains("$")) {
                addCostField.setText("$" + addCostField.getText());
            }

            

            // add data to tableview
            list.add(new PlaceholderExpense(addExpenseField.getText(), addCategoryField.getText(), addDateField.getText(), addCostField.getText()));
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
        // write list to expenses.csv
        try {
            // open file and write each expense to file
            FileWriter csvWriter = new FileWriter("expenses.csv");
            for (PlaceholderExpense expense : list) {
                csvWriter.append(expense.getExpense() + "," + expense.getCategory() + "," + expense.getDate() + "," + expense.getCost() + "\n");
            }
            csvWriter.flush(); // flush data to file
            csvWriter.close(); // close file

            // display success message
            Alert alert = new Alert(AlertType.INFORMATION);

            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Expenses saved to file");
            alert.showAndWait();
        }
        catch (IOException e) {
            // display error message
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error saving expenses to file");
            alert.showAndWait();
        }
    } // end saveExpenses 
    
    
    
} // end ExpenseController class