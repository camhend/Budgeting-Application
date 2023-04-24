package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;

public class BudgetController {
    @FXML
    private void switchToDashboard() throws IOException {
        App.setRoot("DashboardPage");
    }

    @FXML
    private void switchToExpense() throws IOException {
        App.setRoot("ExpensePage");
    }
}
