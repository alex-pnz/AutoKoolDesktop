package com.fullstackhub.autokool.controllers;

import com.fullstackhub.autokool.models.Question;
import com.fullstackhub.autokool.models.User;
import com.fullstackhub.autokool.sevices.DataBaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/** Controller class for view - user-view.fxml
 * @author Alexandr Niculita <alex_penza@list.ru>
 * @version 1.0     */

public class UserController implements Initializable {

    @FXML
    private Button userButton;

    @FXML
    private PieChart userMainChart;

    @FXML
    private Button userMainExam;

    @FXML
    private Label userMainId;

    @FXML
    private Label userMainName;

    @FXML
    private Label userMainSuccess;

    @FXML
    private Label userMainTotal;

    @FXML
    private Label userMainUnsuccess;

    @FXML
    private Label userMainUncompleted;

    @FXML
    private Button examPaneCheckButton;

    @FXML
    private Button examPaneEndButton;

    @FXML
    private ImageView examPaneImage;

    @FXML
    private Button examPaneNextButton;

    @FXML
    private Label examPaneNumber;

    @FXML
    private CheckBox examPaneOption1;

    @FXML
    private CheckBox examPaneOption2;

    @FXML
    private CheckBox examPaneOption3;

    @FXML
    private Label examPanePassFail;

    @FXML
    private Text examPaneText;

    @FXML
    private Pane mainUserExamPane;

    private User user;
    private List<Question> questionList = new ArrayList<>();

    private static int counter = 0;
    private static int questionsFromDB = 7;
    private static int numberOfQuestionsToPass = 5;
    private Question currentQuestion;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        /** Quits app */
        userButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        /** Starts exam */
        userMainExam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                counter = 0;
                userButton.setDisable(true);
                userMainExam.setDisable(true);
                questionList = DataBaseService.getQuestions(questionsFromDB);

                setTextAndImage();
                mainUserExamPane.setVisible(true);
            }
        });

        /** Exits exam with no result checked */
        examPaneEndButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainUserExamPane.setVisible(false);
                Alert alert = null;
                if (DataBaseService.saveResults(user,2)){
                    logger.info("Экзамен завершен досрочно!");
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Экзамен завершен досрочно!");
                    alert.show();
                } else {
                    logger.error("Что то не так с базой данных! Результат не был записан.");
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Что то не так с базой данных! Результат не был записан.");
                    alert.show();
                }

                userButton.setDisable(false);
                userMainExam.setDisable(false);
                setUserData(user);

            }
        });

        /** Checks logic pass/fail for a question */
        examPaneCheckButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (!examPaneOption1.isSelected()&&!examPaneOption2.isSelected()&&!examPaneOption3.isSelected()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Выберите один или несколько ответов.");
                    alert.show();
                    return;
                }

                String check1 = examPaneOption1.isSelected()?"1":"0";
                String check2 = examPaneOption2.isSelected()?"1":"0";

                boolean result = check1.equals(currentQuestion.getAnswer1())&&check2.equals(currentQuestion.getAnswer2());

                if(currentQuestion.getOption3() != null) {
                    String check3 = examPaneOption3.isSelected()?"1":"0";
                    result = result&&check3.equals(currentQuestion.getAnswer3());
                }

                currentQuestion.setResult(result);

                if (result) {
                    examPanePassFail.setText("Правильно");
                    examPanePassFail.setTextFill(Color.GREEN);
                } else {
                    examPanePassFail.setText("He правильно");
                    examPanePassFail.setTextFill(Color.RED);
                }


                if (counter == questionsFromDB-1) {
                    long totalPassed = questionList.stream().filter(Question::getResult).count();
                    int endResult = (totalPassed >= numberOfQuestionsToPass)?1:0;
                    String message = (totalPassed >= numberOfQuestionsToPass)?"Поздравляем! Экзамен завершен успешно!":
                            "К сожалению, экзамен не сдан! Попробуйте еще раз!";

                    mainUserExamPane.setVisible(false);
                    Alert alert = null;
                    if (DataBaseService.saveResults(user,endResult)){
                        logger.info(message);
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(message);
                        alert.show();
                    } else {
                        logger.error("Что то не так с базой данных! Результат не был записан.");
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Что то не так с базой данных! Результат не был записан.");
                        alert.show();
                    }

                    userButton.setDisable(false);
                    userMainExam.setDisable(false);
                    setUserData(user);
                    return;
                }

                examPanePassFail.setVisible(true);
                examPaneCheckButton.setDisable(true);
                examPaneNextButton.setDisable(false);

            }
        });

        /** Proceeding to next question */
        examPaneNextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                counter++;
                setTextAndImage();
            }
        });


    }

    public boolean setUserData(User user) {
        this.user = user;
        userMainName.setText(user.getName());
        userMainId.setText(user.getId()+"");

        if (DataBaseService.findResults(user)) {
            List<Integer> resultList = user.getResultList();

            long fail = resultList.stream().filter(i -> i == 0).count();
            userMainUnsuccess.setText(fail+"");
            long pass = resultList.stream().filter(i -> i == 1).count();
            userMainSuccess.setText(pass+"");
            long uncompleted = resultList.stream().filter(i -> i == 2).count();
            userMainUncompleted.setText(uncompleted+"");
            userMainTotal.setText((fail + pass + uncompleted) + "");

            ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                    new PieChart.Data("Сдал", pass),
                    new PieChart.Data("Не сдал", fail),
                    new PieChart.Data("Не завершил", uncompleted)
            );

            userMainChart.setData(list);
            return true;
        }

        return false;
    }

    /** Setting text and images*/
    private void setTextAndImage(){
        if (questionList != null) {
            currentQuestion = questionList.get(counter);
            logger.info(currentQuestion.toString());

            // Setting text and images
            examPaneNumber.setText((counter+1) + "");
            examPaneText.setText(currentQuestion.getQuestion());

            if (currentQuestion.getImage() == null) {
                examPaneImage.setVisible(false);
            } else {
                String path = String.format("file:///C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s",currentQuestion.getImage());
                Image image = new Image(path);

                examPaneImage.setImage(image);
                examPaneImage.setVisible(true);
            }

            examPaneOption1.setText(currentQuestion.getOption1());
            examPaneOption1.setSelected(false);
            examPaneOption2.setText(currentQuestion.getOption2());
            examPaneOption2.setSelected(false);

            if(currentQuestion.getOption3() == null) {
                examPaneOption3.setVisible(false);
            } else {
                examPaneOption3.setText(currentQuestion.getOption3());
                examPaneOption3.setVisible(true);
                examPaneOption3.setSelected(false);
            }


            examPanePassFail.setVisible(false);
            examPaneNextButton.setDisable(true);
            examPaneCheckButton.setDisable(false);


        } else {
            logger.error("Can't get exam questions from DB!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't get exam questions from DataBase!");
            alert.show();
        }
    }
}
