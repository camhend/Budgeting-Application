package com.application.budgeter;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class ExpenseController implements Initializable {

    // initialize tableview from fxml
    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> expenseColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, String> dateColumn;
    @FXML private TableColumn<Expense, String> costColumn;

    // dummy data
    ObservableList<Expense> list = FXCollections.observableArrayList(
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50"),
        new Expense("hotdog", "food", "03/03/2023", "$1.50"),
        new Expense("thotdog", "food", "03/04/2023", "$2.50")
    );

    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // set cell value factory to tableview columns
        expenseColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("expense"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("date"));
        costColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cost"));

        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expenseTable.getColumns().addAll(expenseColumn, categoryColumn, dateColumn, costColumn);

        // align columns center
        expenseColumn.setStyle("-fx-alignment: CENTER;");
        categoryColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        costColumn.setStyle("-fx-alignment: CENTER;");

        // add list to tableview
        expenseTable.setItems(list);
    }

    // Getters for the TableView and TableColumn instances
    public TableView<Expense> getExpenseTable() {
        return expenseTable;
    }

    public TableColumn<Expense, String> getExpenseColumn() {
        return expenseColumn;
    }

    public TableColumn<Expense, String> getCategoryColumn() {
        return categoryColumn;
    }

    public TableColumn<Expense, String> getDateColumn() {
        return dateColumn;
    }

    public TableColumn<Expense, String> getCostColumn() {
        return costColumn;
    }
}
