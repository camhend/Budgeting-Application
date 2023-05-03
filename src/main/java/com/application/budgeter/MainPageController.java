package com.application.budgeter;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class MainPageController implements Initializable{

    @FXML SplitPane splitPane;

    // add listener to reapply divider positions if window is resized
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // get the divider position and set it to 0.25 when the window is resized
        splitPane.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            splitPane.setDividerPositions(0.20);
        });
    } 
}
