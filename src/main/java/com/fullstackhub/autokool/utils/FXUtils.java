package com.fullstackhub.autokool.utils;

import com.fullstackhub.autokool.HelloApplication;
import com.fullstackhub.autokool.controllers.UserController;
import com.fullstackhub.autokool.models.User;
import com.fullstackhub.autokool.sevices.LoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FXUtils {

    private static final Logger logger = LoggerFactory.getLogger(FXUtils.class);

    public void showScreen (ActionEvent actionEvent, User user) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(
                user.getRole().equals(User.Role.USER) ? "user-view.fxml":"admin-view.fxml"));
        Parent root = fxmlLoader.load();

        if (user.getRole().equals(User.Role.USER)) {
            UserController userController = fxmlLoader.getController();
            userController.setUserData(user);
        }

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(user.getRole().equals(User.Role.USER) ? "Пользователь " + user.getId() + ": " + user.getName() :"Autokool: Admin Dashboard");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
        stage.getScene().setFill(Color.TRANSPARENT);

        stage.centerOnScreen();
        stage.setResizable(false);

    }

    public void showAlert(String message){
        logger.info(message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

}
