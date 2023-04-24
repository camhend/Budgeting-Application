package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    private void switchToBudget() throws IOException {
        App.setRoot("BudgetPage");
    }

    @FXML
    private void switchToExpense() throws IOException {
        App.setRoot("ExpensePage");
    }
}