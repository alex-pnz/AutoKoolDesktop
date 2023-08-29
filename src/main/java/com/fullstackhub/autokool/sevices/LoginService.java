package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.HelloApplication;
import com.fullstackhub.autokool.controllers.UserController;
import com.fullstackhub.autokool.models.User;

import java.io.IOException;
import java.sql.*;

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

public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public static boolean logUserIn(ActionEvent actionEvent, User user) {
        String sqlSelectAllPersons = "SELECT * FROM users WHERE username = ?";
        String connectionUrl = "jdbc:mysql://localhost:3306/autokool";
        String name = user.getName();

        try (Connection connection = DriverManager.getConnection(connectionUrl, "root", "qwer1234");
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllPersons)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.isBeforeFirst()){
                    while (resultSet.next()){
                        int retrievedId = resultSet.getInt("id");
                        String retrievedUsername = resultSet.getString("username");
                        String retrievedPassword = resultSet.getString("password");

                        if (retrievedUsername.equals(user.getName()) && retrievedPassword.equals(user.getPassword())) {
                            logger.info("Correct password!");
                            user.setId(retrievedId);
                            try{
                                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
                                Parent root = fxmlLoader.load();

                                UserController userController = fxmlLoader.getController();
                                userController.setUserData(user);

                                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                stage.setTitle("Пользователь N" + retrievedId+ ": " + retrievedUsername);
                                stage.setScene(new Scene(root, 1000, 600));
                                stage.show();
                                stage.getScene().setFill(Color.TRANSPARENT);
                                stage.centerOnScreen();
                                stage.setResizable(false);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }


                            return true;
                        } else {
                            logger.info("Wrong password!");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Wrong password!");
                            alert.show();
                        }
                    }

                } else {
                    logger.info("User is not found");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("User not found!");
                    alert.show();
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
