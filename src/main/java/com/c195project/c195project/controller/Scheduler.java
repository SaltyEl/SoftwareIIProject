package com.c195project.c195project.controller;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import helper.AppointmentDAO;
import helper.HelperFunctions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;

public class Scheduler implements Initializable {
    public Button appointmentAddButton;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;
    public TableView appointmentTableView;
    public static Customer customerSelected;
    public Button backButton;
    public Label adjustableLabel;
    public TableColumn startCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(CustomerPage.customerIsSelected){
            adjustableLabel.setText("All Appointments");
            try {
                appointmentTableView.setItems(AppointmentDAO.getAppointmentList());
                
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            adjustableLabel.setText("Appointments for: " + customerSelected.getName());
            try {
                appointmentTableView.setItems(AppointmentDAO.getApptListByCustomerID(customerSelected));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void onAppointmentAddButtonClick(ActionEvent actionEvent) throws IOException {
        AddUpdateAppointment.addButtonClicked = true;
        HelperFunctions.windowLoader("/com/c195project/c195project/AddUpdateAppointment.fxml",
                LoginPage.class, appointmentAddButton, 600 , 600);

        /*ZoneId compZone = ZoneId.of("America/New_York");
        ZoneId dbZone = ZoneId.of("Europe/London");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        LocalDateTime UTC = now.atZone(compZone).withZoneSameInstant(dbZone).toLocalDateTime();
        System.out.println(UTC);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        String time = "06-01-2020 13:00";
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println(startTime);
        ZoneOffset offsetOneHour = ZoneOffset.ofHours(-2);
        LocalDateTime oneHourOffset = startTime.minusHours(1);
        System.out.println(oneHourOffset);*/

    }

    public void onAppointmentUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        AddUpdateAppointment.addButtonClicked = false;
        HelperFunctions.windowLoader("/com/c195project/c195project/AddUpdateAppointment.fxml",
                LoginPage.class, appointmentAddButton, 600 , 600);
    }

    public void onAppointmentDeleteButtonClick(ActionEvent actionEvent) {
    }

    public void onBackClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                LoginPage.class, backButton, 632, 402);
    }

}
