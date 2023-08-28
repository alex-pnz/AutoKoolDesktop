package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.models.User;

import java.sql.*;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    public static void logUserIn(User user) {
        String sqlSelectAllPersons = "SELECT * FROM users WHERE username = ?";
        String connectionUrl = "jdbc:mysql://localhost:3306/autokool";
        String name = user.getName();

        try (Connection connection = DriverManager.getConnection(connectionUrl, "root", "qwer1234");
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllPersons)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.isBeforeFirst()){
                    while (resultSet.next()){
                        String retrievedUsername = resultSet.getString("username");
                        String retrievedPassword = resultSet.getString("password");

                        if (retrievedUsername.equals(user.getName()) && retrievedPassword.equals(user.getPassword())) {
                            logger.info("Correct password!");
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
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
