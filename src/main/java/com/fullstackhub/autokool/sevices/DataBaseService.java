package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.models.Question;
import com.fullstackhub.autokool.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.fullstackhub.autokool.utils.Constants.DB_PASS;
import static com.fullstackhub.autokool.utils.Constants.DB_USER;

public class DataBaseService {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseService.class);
    private static final String sqlSelectAllResults = "SELECT * FROM results WHERE user_id = ?";
    private static final String sqlSelectRandomQuestions = "SELECT * FROM questions ORDER BY rand() LIMIT ";
    private static final String sqlSelectAllQuestions = "SELECT * FROM questions;";
    private static final String sqlGetAllUsersWithStats = """
            SELECT * FROM users LEFT JOIN (SELECT user_id AS x, COUNT(result) AS total,
            	(SELECT COUNT(result) FROM results WHERE result = 0 AND user_id = x) AS fail,
            	(SELECT COUNT(result) FROM results WHERE result = 1 AND user_id = x) AS pass,
                (SELECT COUNT(result) FROM results WHERE result = 2 AND user_id = x) AS incomplete
             FROM results GROUP BY user_id) AS res_out ON users.id = res_out.x;
            """;
    private static final String sqlUpdateUser = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?;";
    private static final String sqlUpdateQuestion = "UPDATE questions SET question=?,option1=?,option2=?,option3=?,answer1=?,answer2=?,answer3=?,image=? WHERE idquestions = ?;";
    private static final String sqlSaveUser = "INSERT INTO users (username, password, role) VALUES (?, ?, ?);";
    private static final String sqlSaveQuestion = "INSERT INTO questions (question,option1,option2,option3,answer1,answer2,answer3,image) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String sqlDeleteUser = "DELETE FROM users WHERE id = ?;";
    private static final String sqlDeleteQuestion = "DELETE FROM questions WHERE idquestions = ?;";
    private static final String sqlSaveResult = "INSERT INTO results (user_id, result) VALUES (?, ?)";
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/autokool";

    public static boolean findResults(User user) {

        List<Integer> list = null;
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllResults)) {

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
            logger.error(e.getMessage());
        }

        return false;
    }

    public static List<User> getAllUsers(){

        List<User> list = null;

        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGetAllUsersWithStats);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            list = new ArrayList<>();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    list.add(new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role"),
                            resultSet.getInt("total"),
                            resultSet.getInt("fail"),
                            resultSet.getInt("pass"),
                            resultSet.getInt("incomplete")
                    ));
                }
            }
            return list;

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public static boolean saveUser(String newName, String newPassword, User.Role role) {
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveUser)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newPassword);
            preparedStatement.setString(3, role.name());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage()+ " Unable to save user to DB");
        }
        return false;
    }

    public static boolean saveQuestion(String newQuestion, String newOption1, String newOption2,
                                       String newOption3, int newAnswer1, int newAnswer2,
                                       int newAnswer3, String newImage) {
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveQuestion)) {

            preparedStatement.setString(1, newQuestion);
            preparedStatement.setString(2, newOption1);
            preparedStatement.setString(3, newOption2);
            preparedStatement.setString(4, newOption3);
            preparedStatement.setInt(5, newAnswer1);
            preparedStatement.setInt(6, newAnswer2);
            preparedStatement.setInt(7, newAnswer3);
            preparedStatement.setString(8, newImage);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage()+ " Unable to save question to DB");
        }
        return false;
    }

    public static boolean removeUser(User user){
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUser)) {
            preparedStatement.setString(1, user.getId() + "");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage()+ " Unable to delete user from DB");
        }
        return false;
    }

    public static boolean removeQuestion(Question question){
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteQuestion)) {
            preparedStatement.setString(1, question.getId() + "");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage()+ " Unable to delete question from DB");
        }
        return false;
    }

    public static boolean updateQuestion(String newQuestion, String newOption1, String newOption2,
                                       String newOption3, int newAnswer1, int newAnswer2,
                                       int newAnswer3, String newImage, int idquestions) {
        logger.info(idquestions +" ID ");
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateQuestion)) {

            preparedStatement.setString(1, newQuestion);
            preparedStatement.setString(2, newOption1);
            preparedStatement.setString(3, newOption2);
            preparedStatement.setString(4, newOption3);
            preparedStatement.setInt(5, newAnswer1);
            preparedStatement.setInt(6, newAnswer2);
            preparedStatement.setInt(7, newAnswer3);
            preparedStatement.setString(8, newImage);
            preparedStatement.setInt(9, idquestions);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage()+ " Unable to save user to DB");
        }

        return false;
    }

    public static List<Question> getQuestions(int n){

        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectRandomQuestions+n)) {

            List<Question> list = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        list.add(new Question(
                                resultSet.getInt("idquestions"),
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
            logger.error(e.getMessage());
        }

        return null;
    }

    public static List<Question> getAllQuestions(){

        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllQuestions)) {

            List<Question> list = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        list.add(new Question(
                                resultSet.getInt("idquestions"),
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
            logger.error(e.getMessage());
        }

        return null;
    }


    public static boolean saveResults(User user, int result){
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveResult)) {
            preparedStatement.setString(1, user.getId() + "");
            preparedStatement.setString(2, result + "");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage() + " Unable to save results to DB");
        }


        return false;
    }

    public static boolean updateRecord(User user, String newName, String newPassword, User.Role role) {
        try(Connection connection = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateUser)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newPassword);
            preparedStatement.setString(3, role.name());
            preparedStatement.setString(4, user.getId() + "");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage()+ " Unable to update user to DB");
        }
        return false;
    }
}
