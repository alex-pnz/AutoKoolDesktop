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
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private BarChart<String,Number> chartEdit;

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
    User user;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        checkBoxNew.getItems().addAll(Role.USER,Role.ADMIN);
        checkBoxEdit.getItems().addAll(Role.USER,Role.ADMIN);

        checkBoxNew.setValue(Role.USER);
        checkBoxEdit.setValue(Role.USER);

        setTableViewData();

        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        buttonEdit.setDisable(true);
        buttonDelete.setDisable(true);



        buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!nameEdit.getText().isBlank()&&!passwordEdit.getText().isBlank()&&user!=null){
                    if (checkIfUserExists(nameEdit.getText())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Студент с таким именем уже существует.");
                        alert.show();
                        return;
                    }
                    DataBaseService.updateRecord(user, nameEdit.getText(), passwordEdit.getText(), checkBoxEdit.getValue());
                    logger.info("User updated");
                }
                setTableViewData();
            }
        });

        buttonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!nameNew.getText().isBlank()&&!passNew.getText().isBlank()){
                    if (checkIfUserExists(nameNew.getText())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Студент с таким именем уже существует.");
                        alert.show();
                        return;
                    }
                    DataBaseService.saveUser(nameNew.getText(), passNew.getText(), checkBoxNew.getValue());
                    logger.info("User saved");
                }
                setTableViewData();
            }
        });

        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (user != null) {
                    DataBaseService.removeUser(user);
                    logger.info("User removed");
                }

                setTableViewData();
            }
        });

    }

    @FXML
    public void handleTableClick(){

        if (observableList.isEmpty()) {
            return;
        }



        if (series1 != null && series2 != null && series3 != null && series4 != null) {
            chartEdit.getData().removeAll(series1,series2,series3,series4);
        }
        user = (User) studentListView.getSelectionModel().getSelectedItem();

        if (user == null){
            user = observableList.get(observableList.size()-1);
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

        chartEdit.getData().addAll(series1,series2,series3,series4);
    }

    @FXML
    public void setTableViewData(){
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

    private boolean checkIfUserExists(String name){
        if (observableList == null || observableList.isEmpty()) return false;
        return observableList.stream().anyMatch(u->u.getName().equals(name));
    }


}
