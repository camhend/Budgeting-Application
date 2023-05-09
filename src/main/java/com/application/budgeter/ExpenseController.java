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
import javafx.stage.Window;




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

    
    ExpenseList expenseList = new ExpenseList(); // list of expenses

    ObservableList<Expense> obsvExpenseList = FXCollections.observableArrayList(); // list of expenses to display in tableview
    
    // add expenses to expenseList and display in tableview

    


    // initialize method (runs when ExpenseController is created)
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {


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
                // Create a label with a message
                Label name = new Label("Name:");
                // place at x = 10 y = 10
                name.setLayoutX(100);
                name.setLayoutY(100);
                Label category = new Label("Category:");
                category.setLayoutX(100);
                category.setLayoutY(150);
                Label date = new Label("Date:");
                date.setLayoutX(100);
                date.setLayoutY(200);
                Label cost = new Label("Cost:");
                cost.setLayoutX(100);
                cost.setLayoutY(250);

                // Create a text field each for name, category, date, and cost
                TextField nameField = new TextField();
                nameField.setLayoutX(200);
                nameField.setLayoutY(100);
                TextField categoryField = new TextField();
                categoryField.setLayoutX(200);
                categoryField.setLayoutY(150);
                TextField dateField = new TextField();
                dateField.setLayoutX(200);
                dateField.setLayoutY(200);
                TextField costField = new TextField();
                costField.setLayoutX(200);
                costField.setLayoutY(250);

                // fill text fields with selected expense's data
                nameField.setText(selectedExpense.getName());
                categoryField.setText(selectedExpense.getCategory());
                // mm/dd/yyyy
                dateField.setText(selectedExpense.getLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                // 2 decimal places and add dollar sign
                costField.setText(String.format("$%.2f", selectedExpense.getAmount()));

                // Create a button to finish editing
                Button finishEditButton = new Button("Finish");
                finishEditButton.setLayoutX(100);
                finishEditButton.setLayoutY(300);

                // close
                Button closeEditButton = new Button("Close");
                closeEditButton.setLayoutX(200);
                closeEditButton.setLayoutY(300);



                // Create a VBox layout to hold the label
                AnchorPane layout = new AnchorPane();
                layout.getChildren().addAll(name, category, date, cost, nameField, categoryField, dateField, costField, finishEditButton, closeEditButton);

                // Create a popup and set its content to the layout
                Popup popup = new Popup();
                popup.getContent().add(layout);
                // make height and width of popup
                layout.setPrefHeight(300);
                layout.setPrefWidth(300);

                


                // Show the popup above the button
                popup.show(expensePage.getScene().getWindow());
                // disable main window
                expensePage.getScene().getRoot().setDisable(true);

                // on clicking close
                closeEditButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        popup.hide();
                        expensePage.getScene().getRoot().setDisable(false);
                    }
                });

                finishEditButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // check if textfields are empty and correct data types
                        if (nameField.getText().isEmpty() || categoryField.getText().isEmpty() || dateField.getText().isEmpty() || costField.getText().isEmpty()) {
                            // create alert
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Empty Fields");
                            alert.setContentText("Please fill in all fields");
                            alert.showAndWait();
                        }
                        // else if costfield is a number or is a number + $
                        else if (!(costField.getText().matches("[0-9]+(\\.[0-9][0-9]?)?") || costField.getText().matches("\\$[0-9]+(\\.[0-9][0-9]?)?"))) {
                            // create alert
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Cost");
                            alert.setContentText("Please enter a valid cost");
                            alert.showAndWait();
                        } 
                        // else if date is not mm/dd/yyyy 
                        else if (!dateField.getText().matches("^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$")) {
                            // create alert
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Date");
                            alert.setContentText("Please enter a valid date");
                            alert.showAndWait();
                        }
                        else {
                            String readDate = dateField.getText();
                            LocalDate parsedDate = LocalDate.parse(readDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                            String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                            // if date in text field is equal to date parsed in LocalDate mm/dd/yyyy
                            if (!(readDate.equals(formattedDate))) {
                                // display error message
                                Alert alert = new Alert(AlertType.ERROR);

                                alert.setTitle("Error");
                                alert.setHeaderText("Error");
                                alert.setContentText("Please Enter a Date from the Gregorian Calendar");
                                alert.showAndWait();
                                return;
                            }

                            // if costfield start with $, remove it
                            if (costField.getText().startsWith("$")) {
                                costField.setText(costField.getText().substring(1));
                            }

                            // get edited expense
                            Expense editedExpense = new Expense(nameField.getText(), categoryField.getText(), LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy")), Double.parseDouble(costField.getText()));
                            // edit main expense list
                            expenseList.edit(selectedExpense, editedExpense);
                            // get index of edited expense
                            int index = expenseList.getIndex(editedExpense);

                            // remove old expense from observable list
                            obsvExpenseList.remove(selectedExpense);
                            // add edited expense to observable list at index
                            obsvExpenseList.add(index, editedExpense);

                            // update tableview
                            expenseTable.setItems(obsvExpenseList);
                            // update total
                            updateTotal();
                            // close popup
                            popup.hide();
                            expensePage.getScene().getRoot().setDisable(false);
                        }
                    }
                });
                
                // outline
                    // get expense row
                    // create popup
                    // add textfields and button to popup
                    // handle close and finish button
                        // check if textfields are empty and correct data types
                    // add edited expense to expenseList
                    // get index new of edited expense
                    // remove old expense from observable list
                    // add edited expense to observable list at index
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
            String readDate = addDateField.getText();
            LocalDate parsedDate = LocalDate.parse(readDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            // if date in tet field is equal to date parsed in LocalDate mm/dd/yyyy
            if (!(readDate.equals(formattedDate))) {
                // display error message
                Alert alert = new Alert(AlertType.ERROR);

                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please Enter a Date from the Gregorian Calendar");
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
                // get amount with 2 decimals
                csvWriter.append(String.format("%.2f", expense.getAmount()));
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