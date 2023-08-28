module com.fullstackhub.autokool {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.fullstackhub.autokool to javafx.fxml;
    exports com.fullstackhub.autokool;
}