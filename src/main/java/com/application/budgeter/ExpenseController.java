package com.application.budgeter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.format.DateTimeParseException;



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


    // editPage.fxml elements

    @FXML private AnchorPane EditPage; // page that holds all the elements
    
    @FXML private TextField expensePopupField;
    @FXML private TextField categoryPopupField;
    @FXML private TextField datePopupField;
    @FXML private TextField costPopupField;

    @FXML private Button finishEditButton;
    @FXML private Button closeEditButton;

    
    ExpenseList expenseList = new ExpenseList(); // list of expenses

    ObservableList<Expense> obsvExpenseList = FXCollections.observableArrayList(); // list of expenses to display in tableview
    
    // add expenses to expenseList and display in tableview

    


    // initialize method (runs when ExpenseController is created)
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {

        // load EditPage.fxml
        

        // setup page
        setAnchorPaneConstraints(); 
        formatTableCells(); 
        formatTableColumns();
        expenseTable.setPlaceholder(new Label("No Expenses Added")); 
       
        // get expenselist (expense model) 
        
        loadExpenses(); // load expenses from file?

        // add expenses to observable list
        for (Expense expense : expenseList) {
            obsvExpenseList.add(expense);
        }

        // add observable list to tableview
        expenseTable.setItems(obsvExpenseList);

    
        // calc all time total
        totalMenu.setText("All Time");
        updateTotal();


        // delete and edit menu items
        ContextMenu editingContextMenu = new ContextMenu();
        expenseTable.setContextMenu(editingContextMenu); // set context menu to tableview

        // create menu items
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        // add menu item to context menu
        editingContextMenu.getItems().addAll(deleteMenuItem, editMenuItem);

        // listeners to handle each menu item
        deleteListener(deleteMenuItem); 
        editListener(editMenuItem);
    } // end initialize method


    private void deleteListener(MenuItem deleteMenuItem) {
        // set event handler for when delete menu item is clicked
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // get selected row
                Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
                // remove selected row from list
                expenseList.remove(selectedExpense);
                // remove selected row from observable list
                obsvExpenseList.remove(selectedExpense);
                // update tableview
                expenseTable.setItems(obsvExpenseList);
                // update total
                updateTotal();
            }
        });
    } // end deleteListener method


    private void editListener(MenuItem editMenuItem) {
        // set event handler for when edit menu item is clicked
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // get selected row
                Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
                // create popup 
                Popup popup = new Popup();
                // add
                popup.getContent().add(EditPage);
                // show
                popup.show(expensePage.getScene().getWindow());
                
                

                // // set text for textfields to selected expense (convert toString)
                // expensePopupField.setText(selectedExpense.getName());
                // categoryPopupField.setText(selectedExpense.getCategory());
                // // dd/mm/yyyy date 
                // datePopupField.setText(selectedExpense.getLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                // // convert double amount to 2 decimal format with $ sign
                // costPopupField.setText(String.format("$%.2f", Double.toString(selectedExpense.getAmount())));
                
                // // Show the popup
                // popup.show(expensePage.getScene().getWindow());

                // // disable expensePage's root
                // expensePage.getScene().getRoot().setDisable(true);

                // // when close button is clicked
                // closeEditButton.setOnAction(new EventHandler<ActionEvent>() {
                //     @Override
                //     public void handle(ActionEvent event) {
                //         // close popup
                //         popup.hide();
                //         // undisable expensePage root
                //         expensePage.setDisable(false);
                //     }
                // });

                // // when finish button is clicked
                // finishEditButton.setOnAction(new EventHandler<ActionEvent>() {
                //     @Override
                //     public void handle(ActionEvent event) {
                //         // if none of the fields are empty, or if date is not valid, or if cost is not a number
                //         if (expensePopupField.getText().equals("") || categoryPopupField.getText().equals("") || datePopupField.getText().equals("") || costPopupField.getText().equals("")) {
                //             // alert user with javafx alert
                //             Alert alert = new Alert(AlertType.WARNING);
                //             alert.setTitle("Empty Fields");
                //             alert.setHeaderText("Empty Fields");
                //             alert.setContentText("Please fill out all fields");
                //             alert.showAndWait();
                //             return;
                //         }
                //         else if (!datePopupField.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
                //             // alert user with javafx alert
                //             Alert alert = new Alert(AlertType.WARNING);
                //             alert.setTitle("Invalid Date");
                //             alert.setHeaderText("Invalid Date");
                //             alert.setContentText("Please enter a valid date (mm/dd/yyyy)");
                //             alert.showAndWait();
                //             return;
                //         }
                //         else if (!costPopupField.getText().matches("\\d+(\\.\\d+)?")) {
                //             // alert user with javafx alert
                //             Alert alert = new Alert(AlertType.WARNING);
                //             alert.setTitle("Invalid Cost");
                //             alert.setHeaderText("Invalid Cost");
                //             alert.setContentText("Please enter a valid cost");
                //             alert.showAndWait();
                //             return;
                //         }
                //         else {

                        
                //         // create expense
                //         String name = expensePopupField.getText();
                //         String category = categoryPopupField.getText();
                //         LocalDate date = LocalDate.parse(datePopupField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                //         double cost = Double.parseDouble(costPopupField.getText().substring(1));
            
                //         Expense newExpense = new Expense(name, category, date, cost);

                //         // close popup
                //         popup.hide();
                //         // undisable expensePage root
                //         expensePage.setDisable(false);

                //         // get new expense

                //         // edit expenselist
                //         expenseList.edit(selectedExpense, newExpense);

                //         // get new expense index
                //         int index = expenseList.getIndex(newExpense);
                //         // update observable list
                //         obsvExpenseList.remove(selectedExpense);
                //         obsvExpenseList.add(index, newExpense);

                //         updateTotal();
                //         }
                //     }
                // });
                

                // Expense newExpense;

                // // edit expenselist
                // expenseList.edit(selectedExpense, newExpense);

                // // get new expense index
                // int index = expenseList.getIndex(newExpense);
                // // update observable list
                // obsvExpenseList.remove(selectedExpense);
                // obsvExpenseList.add(index, newExpense);

                // updateTotal();
            }
        });
    } // end editListener method



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


    private void formatTableCells() {
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

        // align columns center
        expenseColumn.setStyle("-fx-alignment: CENTER;");
        categoryColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        costColumn.setStyle("-fx-alignment: CENTER;");
    } // end of formatTableCells method



    private void formatTableColumns() {
        // set cell value factory for each column
        expenseColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("localDate"));
        costColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));

        // set tableview resize policy to it will not resize columns past the width of the tableview
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // add columns to tableview if not already added
        if (!expenseTable.getColumns().contains(expenseColumn)) {
            // create list of columns to add
            List<TableColumn<Expense, ?>> columns = Arrays.asList(expenseColumn, categoryColumn, dateColumn, costColumn);
            expenseTable.getColumns().addAll(columns);
        }
    } // end of formatTableColumns method

    

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
        // read menu button text and update total accordingly
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
    } // end updateTotal method
    

    
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
            // check if date is valid
            try {
                // parse 
                LocalDate date = LocalDate.parse(addDateField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }
            catch (DateTimeParseException e) {
                // display error message
                Alert alert = new Alert(AlertType.ERROR);

                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter a valid date (mm/dd/yyyy)");
                alert.showAndWait();
                return;
            }
            
            // if addCostField has $ sign remove it
            if (addCostField.getText().charAt(0) == '$') {
                addCostField.setText(addCostField.getText().substring(1));
            }
            // add 2 decimal places to addCostField and add $ sign
            addCostField.setText(String.format("$%.2f", Double.parseDouble(addCostField.getText())));

            // add data to tableview
            String name = addExpenseField.getText();
            String category = addCategoryField.getText().toLowerCase(); // convert category to lowercase (easier to search)
            LocalDate date = LocalDate.parse(addDateField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy")); 
            double cost = Double.parseDouble(addCostField.getText().substring(1));
            Expense newExpense = new Expense(name, category, date, cost);
            
            expenseList.add(newExpense);
            obsvExpenseList.add(newExpense);

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