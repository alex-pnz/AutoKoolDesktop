module com.fullstackhub.autokool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens com.fullstackhub.autokool to javafx.fxml;
    exports com.fullstackhub.autokool;
    exports com.fullstackhub.autokool.controllers;
    opens com.fullstackhub.autokool.controllers to javafx.fxml;
}