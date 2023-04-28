package com.application.budgeter;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class ExpenseController implements Initializable {

    // initialize tableview from fxml
    @FXML private TableView<Expense> expenseTable;
    // initialize table columns from fxml
    @FXML private TableColumn<Expense, String> expenseColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, String> dateColumn;
    @FXML private TableColumn<Expense, String> costColumn;

    // dummy data
    ObservableList<Expense> list = FXCollections.observableArrayList(
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
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("notdog", "food", "03/04/2023", "$2.50")
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
    }
}
