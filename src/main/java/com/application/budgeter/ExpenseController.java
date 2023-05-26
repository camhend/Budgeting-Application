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
import javafx.css.CssParser;
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
import javafx.stage.Popup;
import java.lang.NumberFormatException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.ArrayList;


public class ExpenseController implements Initializable {
    // page elements
    @FXML private Label expenseTitle; // title of page
    @FXML private AnchorPane expensePage; // page that holds all the elements
    
    // tableview
    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> nameColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, Double> amountColumn;

    // total section
    @FXML private MenuButton totalMenu; // menu for choosing time periods
    @FXML private Label total; // actual number
    @FXML private Label totalTitle; 
    @FXML private Label totalMenuTitle;

    // add expense section
    @FXML private Button addExpenseButton; 
    @FXML private TextField addExpenseField;
    @FXML private MenuButton addCategoryField;
    @FXML private TextField addDateField;
    @FXML private TextField addCostField;

    @FXML private Button saveExpenseButton; 

    // month section
    @FXML MenuButton monthMenu; 
    @FXML Label monthTitle; 

    // edit expense section
    private TextField nameField;
    private MenuButton categoryField;
    private TextField dateField;
    private TextField costField;
    private Label name;
    private Label category;
    private Label date;
    private Label cost;
    private Button finishEditButton;
    private Button closeEditButton;

    // context menu
    ContextMenu editingContextMenu;
    MenuItem deleteMenuItem;
    MenuItem editMenuItem;

    // Total Value popup 
    Popup popup;
    Label totalPopupLabel;

    // Expense & Budget data
    ObservableList<Expense> obsvExpenseList = FXCollections.observableArrayList(); // list of expenses to display in tableview
    BudgetModel budgetModel;
    ExpenseList expenseList; 
    ExpenseModel expenseModel;


    public void setModels(ExpenseModel expenseModel, BudgetModel budgetModel) {
        // pass expenseList to MainPageController
        this.expenseModel = expenseModel;
        this.budgetModel = budgetModel;

        // set default month to newest saved month
        setDefaultMonth();
        setTableData(); 
    } // end setModels method
    

    // setup page elements
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        setAnchorPaneConstraints(); // set resize constraints for anchorpane
        formatTable(); // format tableview 
        setContextMenu();  // set context menu for tableview (delete/edit)
        createEditMenu(); // setup edit menu
        setTotalPopup(); // setup total popup (shows total when hovering over total label)
    } // end initialize method



    //***********************/
    // Data Validation methods 
    //***********************/

    private double convertToDouble(String cost) {
        cost = cost.replace("$", "");
        cost = String.format("%.2f", Double.parseDouble(cost));
        return Double.parseDouble(cost);
    } // end convertToDouble method


    public boolean isValidDate(String date) {
        // if date is mm/dd/yyyy format regex
        if (!date.matches("^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$")) {
            return false;
        }
        
        // check if date is in mm/dd/yyyy format
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        
        // check if text field date is equal to parsed date 
        // LocalDate.format automatically readjusts fake dates (e.g. 02/30/2020) to real dates (e.g. 02/29/2020)
        if (!(date.equals(formattedDate))) {
            return false;
        }
        return true;
    } // end isValidDate method


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


    private boolean fieldsAreEmpty(String name, String category, String date, String cost) {
        // if any fields are empty
        if (name.equals("") || category.equals("") || name.equals("") || cost.equals("")) {
            return true;
        }
        return false;
    } // end fieldsAreEmpty method


    private boolean areValidAddExpenses(String name, String category, String date, String amount, Popup owner) {
        // if any fields are empty
        if (fieldsAreEmpty(name, category, date, amount)) {
            sendAlert("Error", "Error", "Please fill out all fields", owner);
            return false;
        }
        else if (!isValidCost(amount)) {
            sendAlert("Error", "Error", "Please enter a positive valid cost (e.g. $1.50)", owner);
            return false;
        }
        else if (!isValidDate(date)) {
            sendAlert("Error", "Error", "Please enter a valid date (mm/dd/yyyy)", owner);
            return false;
        }
        // contains comams
        else if (name.contains(",") || category.contains(",")) {
            sendAlert("Error", "Error", "Please do not use commas", owner);
            return false;
        }
        // category not selected
        else if (category.equals("Category...")) {
            sendAlert("Error", "Error", "Please Select a Category", owner);
            return false;
        }
        return true;
    } // end validateAddExpense method



    //*************************/
    // Data Manipulation methods
    //*************************/

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

                // create edit popup layout
                AnchorPane layout = new AnchorPane();
                layout.setPrefSize(300, 300);

                // fill text fields with selected expense's data
                nameField.setText(selectedExpense.getName());
                categoryField.setText(selectedExpense.getCategory());
                dateField.setText(selectedExpense.getLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                costField.setText(String.format("$%.2f", selectedExpense.getAmount()));

                layout.getChildren().addAll(name, category, date, cost, nameField, categoryField, dateField, costField, finishEditButton, closeEditButton);

                Popup popup = new Popup();
                popup.getContent().add(layout);


                // get window center coordinates
                double x = expensePage.getScene().getWindow().getX();
                double y = expensePage.getScene().getWindow().getY();
                x = x + expensePage.getScene().getRoot().getLayoutBounds().getWidth() / 2;
                y = y + expensePage.getScene().getRoot().getLayoutBounds().getHeight() / 2;

                // Show the popup in the center of the expensePage
                popup.show(expensePage.getScene().getWindow(), x - layout.getPrefWidth() / 2, y - layout.getPrefHeight() / 2);
                expensePage.getScene().getRoot().setDisable(true); // disable main window

                // popup position event handler
                centerEditPopup(popup, layout);
                
                // reenable main window when popup is closed
                closeEditButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        popup.hide();
                        expensePage.getScene().getRoot().setDisable(false);
                    }
                });

                // close popup when escape is pressed
                popup.addEventFilter(KeyEvent.KEY_PRESSED, eventTwo -> {
                    if (eventTwo.getCode() == KeyCode.ESCAPE) {
                        popup.hide();
                        expensePage.getScene().getRoot().setDisable(false);
                    }
                });

                finishEditButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        boolean validFields = areValidAddExpenses(nameField.getText(), categoryField.getText(), dateField.getText(), costField.getText(), popup);
                        if (validFields) {
                            // create new expense object
                            String name = nameField.getText();
                            String category = categoryField.getText();
                            LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                            double cost = convertToDouble(costField.getText());
                            Expense editedExpense = new Expense(name, category, date, cost);

                            // edit expense and update observable list
                            expenseModel.edit(selectedExpense, editedExpense);
                            obsvExpenseList.clear();
                            for (Expense expense : expenseList) {
                                obsvExpenseList.add(expense);
                            }

                            // update tableview
                            expenseTable.refresh();
                            updateTotal();

                            // close popup
                            popup.hide();
                            expensePage.getScene().getRoot().setDisable(false);

                            sendAlert("Success", "Success", "Expense successfully edited", null);
                        }
                    }
                }); // end finishEditButton listener
            } // end handle method
        }); // end editButton listener
    } // end editListener method
    

    // add data from text fields to tableview
    public void addExpense() {
        boolean validFields = areValidAddExpenses(addExpenseField.getText(), addCategoryField.getText(), addDateField.getText(), addCostField.getText(), null);
        if (validFields) {
            // create new expense
            String name = addExpenseField.getText();
            String category = addCategoryField.getText(); // convert category to lowercase (easier to search)
            LocalDate date = LocalDate.parse(addDateField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy")); 
            double cost = convertToDouble(addCostField.getText());
            Expense newExpense = new Expense(name, category, date, cost);
            
            // add expense and update observable list
            expenseModel.add(newExpense); 
            obsvExpenseList.clear();
            for (Expense expense : expenseList) {
                obsvExpenseList.add(expense);
            }

            // add expense to tableview and clear textfields
            expenseTable.refresh();
            addExpenseField.clear();
            addCategoryField.setText("Category...");
            addDateField.clear();
            addCostField.clear();
            updateTotal();

            sendAlert("Success", "Success", "Expense successfully added", null);
        }
    } // end addExpense method


    private void updateMonth() {
        // get expenselist of selected month
        String year = monthMenu.getText().substring(0, 4); 
        String month = monthMenu.getText().substring(5); 
        if (month.length() == 1) {month = "0" + month;}
        LocalDate date = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // create date object
        expenseList = expenseModel.getExpenseList(date); // get expense list for month

        // clear and update observable list and tableview
        obsvExpenseList.clear(); 
        for (Expense expense : expenseList) { 
            obsvExpenseList.add(expense); 
        }
        expenseTable.refresh();
        updateTotal();

        // set month menu with dates of found files
        ArrayList<String> dateList = expenseModel.getDateList();
        monthMenu.getItems().clear();
        setMenuButton(monthMenu, dateList);
    } // end updateMonth method


    public void updateTotal() {
        // if total menu = all
        if (totalMenu.getText().equals("All")) {
            total.setText(String.format("$%.2f", expenseList.getTotalSpending()));
        }
        // else find total for selected category
        else { 
            if (expenseList.getCategorySpending(totalMenu.getText()) == -1) { // if category not found
                total.setText("$0.00"); // set total to 0
            }
            else { // else set total to category total
                total.setText(String.format("$%.2f", expenseList.getCategorySpending(totalMenu.getText())));
            }
        }
    } // end updateTotal method



    //***************/
    // File IO method
    //***************/

    // save data from tableview to file (NOTE add paramter for file to save to)
    public void saveExpenses() {
        expenseModel.saveAll(true);
        sendAlert("Save", "Save", "Saved All Expenses", null);

        // refresh monthMenu items
        ArrayList<String> dateList = expenseModel.getDateList();
        monthMenu.getItems().clear();
        setMenuButton(monthMenu, dateList);
    } // end saveExpenses method
    


    //************************/
    // Front End Design Methods
    //************************/

    
        //******************/
        // Tableview Methods
        //******************/


    private void setTableData() {
        // if no month selected (no file saved)
        if (monthMenu.getText().equals("Month")) { // if no month selected
            expenseList = new ExpenseList();
        }
        else { // else get latest month
            String year = monthMenu.getText().substring(0, 4); 
            String month = monthMenu.getText().substring(5); 
            if (month.length() == 1) {month = "0" + month;}
            LocalDate date = LocalDate.parse(month + "/01/" + year, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // create date object
            expenseList = expenseModel.getExpenseList(date); // get expense list for month
        }
        
        // update observable list and tableview
        obsvExpenseList = FXCollections.observableArrayList();
        expenseTable.setItems(obsvExpenseList);
        for (Expense expense : expenseList) {
            obsvExpenseList.add(expense);
        }
        setAllMenuButtons();
        // calc and set total
        totalMenu.setText("All");
        updateTotal();
    } // end setTableData method


    private void formatTable() {
        formatTableCells();
        formatTableColumns();
        expenseTable.setPlaceholder(new Label("No Expenses Added")); 
    } // end formatTable method


    private void formatTableCells() {
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
    } // end of formatTableCells method


    private void formatTableColumns() {
        // set each expense field to its respective column
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("localDate"));
        amountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("amount"));

        // set tableview resize policy to it will not resize columns past the width of the tableview
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // add columns to tableview if not already added
        if (!expenseTable.getColumns().contains(nameColumn)) {
            // create list of columns to add
            List<TableColumn<Expense, ?>> columns = Arrays.asList(nameColumn, categoryColumn, dateColumn, amountColumn);
            expenseTable.getColumns().addAll(columns);
        }
    } // end of formatTableColumns method



        //*******************/
        // MenuButton Methods
        //*******************/

    private void setMenuButton(MenuButton menuButton, ArrayList<String> items) {
        for (String item : items) {
            MenuItem menuItem = new MenuItem(item);
            menuButton.getItems().add(menuItem);
            menuItem.setOnAction(this::changeMenuButton);
        }
    } // end of setMenuButton method


    private void setAllMenuButtons() {
        // add all as default option for totalmenu
        MenuItem all = new MenuItem("All");
        totalMenu.getItems().add(all);
        all.setOnAction(this::changeMenuButton);
        
        // set category buttons with categories from budgetmodel
        ArrayList<String> categoryList = budgetModel.getCategoryList();
        setMenuButton(totalMenu, categoryList);
        setMenuButton(addCategoryField, categoryList);
        setMenuButton(categoryField, categoryList);

        // set month menu with dates of found files
        ArrayList<String> dateList = expenseModel.getDateList();
        setMenuButton(monthMenu, dateList);
    } // end setMenuItems method


    // changes menu button text to selected menu item
    public void changeMenuButton(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        MenuButton menuButton = (MenuButton) menuItem.getParentPopup().getOwnerNode();
        menuButton.setText(menuItem.getText());
        // update tableview based on menu button clicked
        if (menuButton == monthMenu) {
            updateMonth();
        }
        else if (menuButton == totalMenu) {
            updateTotal();
        }
    } // end changeMenuButton method


    private void setDefaultMonth() {
        if(expenseModel.getDateList().isEmpty()) {
            return;
        }
        // get newest date
        ArrayList<String> dates = expenseModel.getDateList();
        String newestDate = dates.get(dates.size() - 1);

        // set month menu to newest date and update tableview
        monthMenu.setText(newestDate); 
        updateMonth(); 
        monthMenu.getItems().clear();
    } // end setDefaultMonth method



        //*******************/
        // Edit Popup Methods
        //*******************/

    private void createEditMenu() {

        nameField = new TextField();
        categoryField = new MenuButton();
        dateField = new TextField();
        costField = new TextField();
        name = new Label("Name:");
        category = new Label("Category:");
        date = new Label("Date:");
        cost = new Label("Cost:");
        finishEditButton = new Button("Finish");
        closeEditButton = new Button("Close");

        // place each label 
        name.setLayoutX(100);
        name.setLayoutY(50);
        category.setLayoutX(100);
        category.setLayoutY(100);
        date.setLayoutX(100);
        date.setLayoutY(150);
        cost.setLayoutX(100);
        cost.setLayoutY(200);

        // place each textfield
        nameField.setLayoutX(200);
        nameField.setLayoutY(50);
        categoryField.setLayoutX(200);
        categoryField.setLayoutY(100);
        categoryField.setPrefWidth(150);
        dateField.setLayoutX(200);
        dateField.setLayoutY(150);
        costField.setLayoutX(200);
        costField.setLayoutY(200);

        // Create a button to finish editing
        finishEditButton.setLayoutX(100);
        finishEditButton.setLayoutY(300);

        // close
        closeEditButton.setLayoutX(300);
        closeEditButton.setLayoutY(300);
    } // end of createEditMenu method


    private void centerEditPopup(Popup popup, AnchorPane layout) {
        // popup position event handlers
        expensePage.getScene().getWindow().xProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldX, Number newX) {
                popup.setX(newX.doubleValue() + expensePage.getScene().getRoot().getLayoutBounds().getWidth() / 2 - layout.getPrefWidth() / 2);
            }
        });
        expensePage.getScene().getWindow().yProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldY, Number newY) {
                popup.setY(newY.doubleValue() + expensePage.getScene().getRoot().getLayoutBounds().getHeight() / 2 - layout.getPrefHeight() / 2);
            }
        });
        expensePage.getScene().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) {
                popup.setX(expensePage.getScene().getWindow().getX() + expensePage.getScene().getRoot().getLayoutBounds().getWidth() / 2 - layout.getPrefWidth() / 2);
            }
        });
        expensePage.getScene().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldHeight, Number newHeight) {
                popup.setY(expensePage.getScene().getWindow().getY() + expensePage.getScene().getRoot().getLayoutBounds().getHeight() / 2 - layout.getPrefHeight() / 2);
            }
        });
    } // end of centerEditPopup method
 


        //******************************/
        // AnchorPane Constraint Methods
        //******************************/

    private void setAnchorPaneConstraints() {
        // listener for adjusting elements' width when window is resized
        expensePage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidthConstraints(expenseTitle, newVal, 0.4, 0.4);
            setWidthConstraints(monthMenu, newVal, .1, .79);
            setWidthConstraints(monthTitle, newVal, .1, .79);
            setWidthConstraints(total, newVal, .8, .1);
            total.setPrefWidth(newVal.doubleValue() * .1);

            // runs after total is resized
            Platform.runLater(() -> {
                setWidthConstraints(totalTitle, newVal, .8, .1);
                totalTitle.setPrefWidth(newVal.doubleValue() * .1);

                // set totalMenu next to total label
                AnchorPane.setRightAnchor(totalMenu, total.getWidth() + newVal.doubleValue() * .1);  // total menu next to total label
                // set totalMenuTitle on top of totalMenu (centered)
                AnchorPane.setRightAnchor(totalMenuTitle, total.getWidth()+totalMenuTitle.getWidth()/2 + newVal.doubleValue() * .1);
            });

            setWidthConstraints(expenseTable, newVal, .1, .1);

            // add buttons above tableview 10% of window each
            setWidthConstraints(addExpenseField, newVal, .1, .8); 
            setWidthConstraints(addCategoryField, newVal, .2, .7);
            setWidthConstraints(addDateField, newVal, .3, .6);
            setWidthConstraints(addCostField, newVal, .4, .5);
            setWidthConstraints(addExpenseButton, newVal, .5, .46);

            // save button at right 10% of window
            setWidthConstraints(saveExpenseButton, newVal, .8, .1);   
        });

        // listener for adjusting elements' height when window is resized
        expensePage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeightConstraints(expenseTitle, newVal, .03, 0.92);

            AnchorPane.setTopAnchor(monthMenu, newVal.doubleValue() * .1);
            AnchorPane.setTopAnchor(monthTitle, newVal.doubleValue() * .075);

            AnchorPane.setTopAnchor(total, newVal.doubleValue() * .1); // total cost at top 10% of window
            AnchorPane.setTopAnchor(totalTitle, newVal.doubleValue() * .075); // total title at top 10% of window
            AnchorPane.setTopAnchor(totalMenu, newVal.doubleValue() * .1); // total menu at top 10% of window
            AnchorPane.setTopAnchor(totalMenuTitle, newVal.doubleValue() * .075);
            
            setHeightConstraints(expenseTable, newVal, .32, .05);

            AnchorPane.setBottomAnchor(addExpenseField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addCategoryField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addDateField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addCostField, newVal.doubleValue() * .72);
            AnchorPane.setBottomAnchor(addExpenseButton, newVal.doubleValue() * .72);

            AnchorPane.setBottomAnchor(saveExpenseButton, newVal.doubleValue() * .72); 
        });
    } // end setAnchorPaneConstraints method

    private void setWidthConstraints(Node element, Number newVal,  double left, double right) {
        AnchorPane.setLeftAnchor(element, newVal.doubleValue() * left);
        AnchorPane.setRightAnchor(element, newVal.doubleValue() * right);
    } // end setWidthConstraints method

    private void setHeightConstraints(Node element, Number newVal,  double top, double bottom) {
        AnchorPane.setTopAnchor(element, newVal.doubleValue() * top);
        AnchorPane.setBottomAnchor(element, newVal.doubleValue() * bottom);
    } // end setHeightConstraints method



        //******/
        // Other
        //******/

    private void setContextMenu() {
        // create & set context menu to tableview
        editingContextMenu = new ContextMenu();
        deleteMenuItem = new MenuItem("Delete");
        editMenuItem = new MenuItem("Edit");
        editingContextMenu.getItems().addAll(deleteMenuItem, editMenuItem);
        expenseTable.setContextMenu(editingContextMenu);

        // listeners to handle each menu item
        deleteListener(deleteMenuItem); 
        editListener(editMenuItem);
    } // end setContextMenu method


    private void setTotalPopup() {
        // popup total value when mouse hovers over total
        popup = new Popup();
        totalPopupLabel = new Label(total.getText());
        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(200, 200, 200), null, null);
        Background background = new Background(backgroundFill);
        totalPopupLabel.setBackground(background);
        popup.getContent().add(totalPopupLabel);

        // hover over total to show popup
        total.setOnMouseEntered(event -> {
            totalPopupLabel.setText(total.getText());
            double x = event.getScreenX();
            double y = event.getScreenY();
            popup.show(total, x, y);
        });
        total.setOnMouseExited(event -> {
            popup.hide();
        });
    } // end setTotalPopup method


    private void sendAlert(String title, String header, String content, Popup owner) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        if (owner != null) {alert.initOwner(owner);}
        alert.showAndWait();
    } // end sendAlert method
} // end ExpenseController class