package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseService {

    private static final String sqlSelectAllPersons = "SELECT * FROM results WHERE user_id = ?";
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/autokool";


    public static boolean findResults(User user) {

        List<Integer> list = null;
        try(Connection connection = DriverManager.getConnection(connectionUrl, "root", "qwer1234");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllPersons)) {

            preparedStatement.setString(1, user.getId() + "");
            list = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        list.add(resultSet.getInt("result"));
                    }
                }

            }
            user.setResultList(list);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

}
