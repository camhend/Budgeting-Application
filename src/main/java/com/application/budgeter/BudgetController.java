package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;

public class BudgetController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}