package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.HelloApplication;
import com.fullstackhub.autokool.controllers.AdminController;
import com.fullstackhub.autokool.controllers.UserController;
import com.fullstackhub.autokool.models.User;

import java.io.IOException;
import java.sql.*;

import com.fullstackhub.autokool.utils.FXUtils;
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

import static com.fullstackhub.autokool.utils.Constants.*;

public class LoginService {

    private final FXUtils fxUtils;

    public LoginService(FXUtils fxUtils){
        this.fxUtils = fxUtils;
    }

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public boolean logUserIn(ActionEvent actionEvent, User user){
        String sqlSelectAllPersons = "SELECT * FROM users WHERE username = ?";
        String name = user.getName();

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, DB_USER, DB_PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllPersons)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.isBeforeFirst()){
                    if (resultSet.next()){
                        int retrievedId = resultSet.getInt("id");
                        String retrievedUsername = resultSet.getString("username");
                        String retrievedPassword = resultSet.getString("password");
                        String retrievedRole = resultSet.getString("role");

                        if (retrievedUsername.equals(user.getName()) && retrievedPassword.equals(user.getPassword())) {
                            logger.info("Correct password!");
                            user.setId(retrievedId);
                            user.setRole((retrievedRole.equals("USER"))? User.Role.USER: User.Role.ADMIN);
                            try{
                                fxUtils.showScreen(actionEvent, user);
                            } catch (IOException e) {
                                logger.error("IOException FXMLLoader - {}" ,e.getMessage());
                                return false;
                            }
                            return true;
                        } else {
                            fxUtils.showAlert("Wrong password!");
                        }
                    }
                } else {
                    fxUtils.showAlert("User not found!");
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException - {}", e.getMessage());
        }
        return false;
    }

}
