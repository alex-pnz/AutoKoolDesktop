package com.fullstackhub.autokool.controllers;

import com.fullstackhub.autokool.models.User;
import com.fullstackhub.autokool.sevices.LoginService;
import com.fullstackhub.autokool.utils.FXUtils;
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

    private LoginService loginService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonLogin.setVisible(false);
        loginService = new LoginService(new FXUtils());

        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                User user = new User(textFieldUsername.getText(),textFieldPassword.getText());
                loginService.logUserIn(actionEvent, user);
            }
        });

    }

    public void onKeyReleaseListener() {
        String s1 = textFieldUsername.getText();
        String s2 = textFieldPassword.getText();

        boolean check = (s1.isEmpty() || s1.trim().isEmpty()) || (s2.isEmpty() || s2.trim().isEmpty());
        buttonLogin.setVisible(!check);

    }

}
