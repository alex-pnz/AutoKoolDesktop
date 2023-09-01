package com.fullstackhub.autokool.controllers;

import com.fullstackhub.autokool.models.Question;
import com.fullstackhub.autokool.models.User;
import com.fullstackhub.autokool.models.User.Role;
import com.fullstackhub.autokool.sevices.DataBaseService;
import com.fullstackhub.autokool.sevices.LoginService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

public class AdminController implements Initializable {

    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonNew;

    @FXML
    private Button buttonQuestionEdit;

    @FXML
    private BarChart<String, Number> chartEdit;

    @FXML
    private TextField nameEdit;

    @FXML
    private TextField nameNew;

    @FXML
    private TextField passNew;

    @FXML
    private TextField passwordEdit;

    @FXML
    private CheckBox questionsAnswer1Edit;

    @FXML
    private CheckBox questionsAnswer1New;

    @FXML
    private CheckBox questionsAnswer2Edit;

    @FXML
    private CheckBox questionsAnswer2New;

    @FXML
    private CheckBox questionsAnswer3Edit;

    @FXML
    private CheckBox questionsAnswer3New;

    @FXML
    private Button questionsButtonNew;

    @FXML
    private TableView<Question> questionsListView;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Question, String> answer1Column;
    @FXML
    private TableColumn<Question, String> answer2Column;
    @FXML
    private TableColumn<Question, String> answer3Column;
    @FXML
    private TableColumn<Question, String> option1Column;
    @FXML
    private TableColumn<Question, String> option2Column;
    @FXML
    private TableColumn<Question, String> option3Column;
    @FXML
    private TableColumn<Question, String> imageColumn;
    @FXML
    private ImageView questionImageViewEdit;
    @FXML
    private ImageView questionImageViewNew;
    @FXML
    private TextField questionsOption1Edit;

    @FXML
    private TextField questionsOption1New;

    @FXML
    private TextField questionsOption2Edit;

    @FXML
    private TextField questionsOption2New;

    @FXML
    private TextField questionsOption3Edit;

    @FXML
    private TextField questionsOption3New;

    @FXML
    private TextField questionsQuestionEdit;

    @FXML
    private TextField questionsQuestionNew;

    @FXML
    private Button buttonQuestionDelete;
    @FXML
    private Button buttonQuestionImageEdit;
    @FXML
    private Button buttonQuestionImageNew;
    @FXML
    private Label labelQuestionImageEdit;
    @FXML
    private Label labelQuestionImageNew;

    @FXML
    private TableView<User> studentListView;
    @FXML
    private TableColumn<User, Integer> fail;

    @FXML
    private TableColumn<User, Integer> id;

    @FXML
    private TableColumn<User, Integer> incomplete;

    @FXML
    private TableColumn<User, Integer> pass;

    @FXML
    private TableColumn<User, String> password;

    @FXML
    private TableColumn<User, Role> role;

    @FXML
    private TableColumn<User, Integer> total;

    @FXML
    private TableColumn<User, String> username;

    @FXML
    private ChoiceBox<Role> checkBoxNew;
    @FXML
    private ChoiceBox<Role> checkBoxEdit;

    XYChart.Series series1;
    XYChart.Series series2;
    XYChart.Series series3;
    XYChart.Series series4;
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    ObservableList<User> observableList;
    ObservableList<Question> observableListQuestions;
    User user;
    Question question;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        checkBoxNew.getItems().addAll(Role.USER, Role.ADMIN);
        checkBoxEdit.getItems().addAll(Role.USER, Role.ADMIN);

        checkBoxNew.setValue(Role.USER);
        checkBoxEdit.setValue(Role.USER);

        setTableViewDataQuestion();
        setTableViewDataStudent();

        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        questionsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        buttonEdit.setDisable(true);
        buttonDelete.setDisable(true);

        buttonQuestionEdit.setDisable(true);
        buttonQuestionDelete.setDisable(true);


        buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!nameEdit.getText().isBlank() && !passwordEdit.getText().isBlank() && user != null) {
                    if (checkIfUserExists(nameEdit.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Студент с таким именем уже существует.");
                        alert.show();
                        return;
                    }
                    DataBaseService.updateRecord(user, nameEdit.getText(), passwordEdit.getText(), checkBoxEdit.getValue());
                    logger.info("User updated");
                }
                setTableViewDataStudent();
            }
        });

        buttonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!nameNew.getText().isBlank() && !passNew.getText().isBlank()) {
                    if (checkIfUserExists(nameNew.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Студент с таким именем уже существует.");
                        alert.show();
                        return;
                    }
                    DataBaseService.saveUser(nameNew.getText(), passNew.getText(), checkBoxNew.getValue());
                    logger.info("User saved");
                }
                setTableViewDataStudent();
            }
        });

        questionsButtonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!questionsQuestionNew.getText().isBlank() && !questionsOption1New.getText().isBlank()
                        && !questionsOption2New.getText().isBlank()) {

                    String[] arrTemp = labelQuestionImageNew.getText().split("\\\\");
                    String fileName = arrTemp[arrTemp.length-1];

                    DataBaseService.saveQuestion(questionsQuestionNew.getText(), questionsOption1New.getText(),
                            questionsOption2New.getText(), questionsOption2New.getText(), (questionsAnswer1New.isSelected()?1:0),
                            (questionsAnswer2New.isSelected()?1:0), (questionsAnswer2New.isSelected()?1:0), fileName);

                    String path = String.format("C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", fileName);
                    Image image = questionImageViewNew.getImage();

                    File file = new File(path);

                    try{
                        ImageIO.write(SwingFXUtils.fromFXImage(image,null),"jpg",file);
                    }catch(IOException e){
                        logger.error(e.getMessage());
                    }

                    logger.info("Question saved");

                    setTableViewDataQuestion();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Для сохранения вопроса необходимо заполнить обязательные поля: вопрос, вариант ответа 1 и вариант ответа 2!");
                    alert.show();
                }
            }
        });

        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (user != null) {
                    DataBaseService.removeUser(user);
                    logger.info("User removed");
                }

                setTableViewDataStudent();
            }
        });

        buttonQuestionImageEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения", "*.JPG", "*.jpg"));
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    labelQuestionImageEdit.setText(file.getAbsolutePath());
                    questionImageViewEdit.setImage(new Image(file.getAbsolutePath()));
                    questionImageViewEdit.setVisible(true);
                }
            }
        });

        buttonQuestionDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (question != null) {
                    DataBaseService.removeQuestion(question);

                    if (question.getImage() != null){
                        String path = String.format("C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", question.getImage());
                        File file = new File(path);
                        file.delete();
                    }

                    logger.info("Question removed");
                }

                questionImageViewEdit.setVisible(false);
                buttonQuestionEdit.setDisable(false);
                buttonQuestionDelete.setDisable(false);
                setTableViewDataQuestion();
            }
        });

        buttonQuestionEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!questionsQuestionEdit.getText().isBlank() && !questionsOption1Edit.getText().isBlank()
                        && !questionsOption2Edit.getText().isBlank() && question != null) {

                    String[] arrTemp = labelQuestionImageEdit.getText().split("\\\\");
                    String fileName = arrTemp[arrTemp.length-1];
                    DataBaseService.updateQuestion(questionsQuestionEdit.getText(),questionsOption1Edit.getText(),
                            questionsOption2Edit.getText(),questionsOption3Edit.getText(), (questionsAnswer1Edit.isSelected()?1:0),
                            (questionsAnswer2Edit.isSelected()?1:0), (questionsAnswer3Edit.isSelected()?1:0), fileName,
                            question.getId());


                    String path = String.format("C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", fileName);
                    Image image = questionImageViewEdit.getImage();

                    File file = new File(path);

                    try{
                        ImageIO.write(SwingFXUtils.fromFXImage(image,null),"jpg",file);
                    }catch(IOException e){
                        logger.error(e.getMessage());
                    }

                    logger.info("Question updated");
                }

                setTableViewDataQuestion();
            }
        });

        buttonQuestionImageNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения", "*.JPG", "*.jpg"));
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    labelQuestionImageNew.setText(file.getAbsolutePath());
                    questionImageViewNew.setImage(new Image(file.getAbsolutePath()));
                    questionImageViewNew.setVisible(true);
                }
            }
        });

    }

    @FXML
    public void handleTableClickStudents() {

        if (observableList.isEmpty()) {
            return;
        }

        if (series1 != null && series2 != null && series3 != null && series4 != null) {
            chartEdit.getData().removeAll(series1, series2, series3, series4);
        }
        user = (User) studentListView.getSelectionModel().getSelectedItem();

        if (user == null) {
            user = observableList.get(observableList.size() - 1);
            studentListView.getSelectionModel().selectLast();
        }

        nameEdit.setText(user.getName());
        passwordEdit.setText(user.getPassword());
        buttonEdit.setDisable(false);
        buttonDelete.setDisable(false);


        series1 = new XYChart.Series();
        series1.setName("Успешно");
        series1.getData().add(new XYChart.Data("Успешно", user.getPass()));
        series2 = new XYChart.Series();
        series2.setName("Не удачно");
        series2.getData().add(new XYChart.Data("Не удачно", user.getFail()));
        series3 = new XYChart.Series();
        series3.setName("Не завершил");
        series3.getData().add(new XYChart.Data("Не завершил", user.getIncomplete()));
        series4 = new XYChart.Series();
        series4.setName("Всего");
        series4.getData().add(new XYChart.Data("Всего", user.getTotal()));

        chartEdit.getData().addAll(series1, series2, series3, series4);
    }
    @FXML
    public void handleTableClickQuestions(){
        if (observableListQuestions.isEmpty()) {
            return;
        }

        question = (Question) questionsListView.getSelectionModel().getSelectedItem();

        if (question == null) {
            question = observableListQuestions.get(observableListQuestions.size() - 1);
            studentListView.getSelectionModel().selectLast();
        }

        questionsQuestionEdit.setText(question.getQuestion());
        questionsOption1Edit.setText(question.getOption1());
        questionsOption2Edit.setText(question.getOption2());
        questionsAnswer1Edit.setSelected(question.getAnswer1().equals("1"));
        questionsAnswer2Edit.setSelected(question.getAnswer2().equals("1"));

        if(question.getOption3()!=null) {
            questionsOption3Edit.setText(question.getOption3());
            questionsAnswer3Edit.setSelected(question.getAnswer3().equals("1"));
        }

        if (question.getImage() != null) {

            String path = String.format("file:///C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", question.getImage());
            Image image = new Image(path);

            questionImageViewEdit.setImage(image);
            questionImageViewEdit.setVisible(true);

        } else{
            questionImageViewEdit.setVisible(false);
        }

        buttonQuestionEdit.setDisable(false);
        buttonQuestionDelete.setDisable(false);

    }

    @FXML
    public void setTableViewDataStudent() {
        studentListView.getItems().clear();

        observableList = FXCollections.observableArrayList();
        List<User> listTemp = DataBaseService.getAllUsers();

        if (listTemp != null) {
            observableList.addAll(listTemp);
        }


        id.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        username.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
        password.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
        role.setCellValueFactory(cellData -> cellData.getValue().getRoleProperty());
        pass.setCellValueFactory(cellData -> cellData.getValue().getPassProperty().asObject());
        fail.setCellValueFactory(cellData -> cellData.getValue().getFailProperty().asObject());
        incomplete.setCellValueFactory(cellData -> cellData.getValue().getIncompleteProperty().asObject());
        total.setCellValueFactory(cellData -> cellData.getValue().getTotalProperty().asObject());
        studentListView.setItems(observableList);
    }

    private boolean checkIfUserExists(String name) {
        if (observableList == null || observableList.isEmpty()) return false;
        return observableList.stream().anyMatch(u -> u.getName().equals(name));
    }

    @FXML
    public void setTableViewDataQuestion() {
        questionsListView.getItems().clear();

        observableListQuestions = FXCollections.observableArrayList();
        List<Question> listTemp = DataBaseService.getAllQuestions();

        if (listTemp != null) {
            observableListQuestions.addAll(listTemp);
        }

        questionColumn.setCellValueFactory(cellData -> cellData.getValue().getQuestionProperty());
        option1Column.setCellValueFactory(cellData -> cellData.getValue().getOption1Property());
        option2Column.setCellValueFactory(cellData -> cellData.getValue().getOption2Property());
        option3Column.setCellValueFactory(cellData -> cellData.getValue().getOption3Property());
        answer1Column.setCellValueFactory(cellData -> cellData.getValue().getAnswer1Property());
        answer2Column.setCellValueFactory(cellData -> cellData.getValue().getAnswer2Property());
        answer3Column.setCellValueFactory(cellData -> cellData.getValue().getAnswer3Property());
        imageColumn.setCellValueFactory(cellData -> cellData.getValue().getImageProperty());

        questionsListView.setItems(observableListQuestions);
    }

}
