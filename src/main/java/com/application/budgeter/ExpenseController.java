package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;

public class ExpenseController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
