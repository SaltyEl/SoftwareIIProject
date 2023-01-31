package com.c195project.c195project.controller;

import helper.AppointmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Scheduler implements Initializable {
    public Button appointmentAddButton;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;
    public TableView appointmentTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentTableView.setItems(AppointmentDAO.getAppointmentList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onAppointmentAddButtonClick(ActionEvent actionEvent) {
    }

    public void onAppointmentUpdateButtonClick(ActionEvent actionEvent) {
    }

    public void onAppointmentDeleteButtonClick(ActionEvent actionEvent) {
    }

    public void onBackClick(ActionEvent actionEvent) {
    }

}
