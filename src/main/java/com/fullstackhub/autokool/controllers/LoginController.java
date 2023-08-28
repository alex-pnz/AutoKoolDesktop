package com.fullstackhub.autokool.controllers;

import com.fullstackhub.autokool.models.User;
import com.fullstackhub.autokool.sevices.LoginService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button buttonLogin;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonLogin.setDisable(true);

        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                User user = new User(textFieldUsername.getText(),textFieldPassword.getText());
                LoginService.logUserIn(user);
            }
        });

    }

    public void onKeyReleaseListener() {
        String s1 = textFieldUsername.getText();
        String s2 = textFieldPassword.getText();

        boolean check = (s1.isEmpty() || s1.trim().isEmpty()) || (s2.isEmpty() || s2.trim().isEmpty());
        buttonLogin.setDisable(check);

    }

    public void printAll() {

        String sqlSelectAllPersons = "SELECT * FROM users";
        String connectionUrl = "jdbc:mysql://localhost:3306/autokool";

        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "qwer1234");
             PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons);
             ResultSet rs = ps.executeQuery()) {



            while (rs.next()) {
                long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                System.out.println(id+" "+username + " " + password);
                // do something with the extracted data...
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

}
