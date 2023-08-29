package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.models.Question;
import com.fullstackhub.autokool.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private static final String sqlSelectAllPersons = "SELECT * FROM results WHERE user_id = ?";
    private static final String sqlSelectRandomQuestions = "SELECT * FROM questions ORDER BY rand() LIMIT ";
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/autokool";

    private static final String DB_USERNAME = "root";
    private static final String DB_PASS = "qwer1234";


    public static boolean findResults(User user) {

        List<Integer> list = null;
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USERNAME, DB_PASS);
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

    public static List<Question> getQuestions(int n){

        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USERNAME, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectRandomQuestions+n)) {

            List<Question> list = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        list.add(new Question(
                                resultSet.getString("question"),
                                resultSet.getString("option1"),
                                resultSet.getString("option2"),
                                resultSet.getString("option3"),
                                resultSet.getString("answer1"),
                                resultSet.getString("answer2"),
                                resultSet.getString("answer3"),
                                resultSet.getString("image")
                        ));
                    }
                }

            }
            logger.info(list.get(0).toString());
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static boolean saveResults(User user, int result){
        return false;
    }

}