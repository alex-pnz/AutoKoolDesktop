package com.fullstackhub.autokool.controllers;

import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    }

    public void onKeyReleaseListener() {
        String s1 = textFieldUsername.getText();
        String s2 = textFieldPassword.getText();

        boolean check = (s1.isEmpty() || s1.trim().isEmpty()) || (s2.isEmpty() || s2.trim().isEmpty());
        buttonLogin.setDisable(check);

    }

}
