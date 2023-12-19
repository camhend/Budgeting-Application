module com.application.budgeter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.application.budgeter to javafx.fxml;
    exports com.application.budgeter;
}
