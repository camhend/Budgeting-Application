module com.application.budgeter {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.application.budgeter to javafx.fxml;
    exports com.application.budgeter;
}
