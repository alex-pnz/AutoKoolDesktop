package com.fullstackhub.autokool.controllers;

import com.fullstackhub.autokool.models.User;
import com.fullstackhub.autokool.sevices.DataBaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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


    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    public void setUserData(User user) {
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

        }





    }
}
