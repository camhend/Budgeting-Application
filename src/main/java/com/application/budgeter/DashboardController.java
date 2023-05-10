package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import java.time.LocalDate;


public class DashboardController implements Initializable {
    @FXML private AnchorPane dashboardPage;
    @FXML private Label dashboardTitle;

    @FXML private PieChart pieChart;
    @FXML private AreaChart areaChart;

    @FXML private Rectangle spendingsRect;
    @FXML private Label flatAmountSpent;
    @FXML private Label percentAmountSpent;
    @FXML private Label daysLeft;
    @FXML private Label transactionsTableTitle;

    @FXML private TableView<Expense> transactionsTable;
    @FXML private TableColumn<Expense, String> nameColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, Double> costColumn;


    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        
    }
}