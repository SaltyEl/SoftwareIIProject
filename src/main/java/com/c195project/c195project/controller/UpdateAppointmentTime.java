package com.c195project.c195project.controller;

import com.c195project.c195project.DAO.AppointmentDAO;
import com.c195project.c195project.helpers.HelperFunctions;
import com.c195project.c195project.model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class UpdateAppointmentTime implements Initializable {

    public static Appointment appointmentSelected;
    public TextField startTimeTextBox;
    public TextField endTimeTextBox;
    public Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeTextBox.setText(appointmentSelected.getStartDateTime().toLocalTime().toString());
        endTimeTextBox.setText(appointmentSelected.getEndDateTime().toLocalTime().toString());
    }

    public void onSaveClick(ActionEvent actionEvent) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime startDateTime = appointmentSelected.getStartDateTime();
            LocalDateTime endDateTime = appointmentSelected.getEndDateTime();

            LocalDate startDate = startDateTime.toLocalDate();
            LocalDate endDate = endDateTime.toLocalDate();

            if (startTimeTextBox.getText().isEmpty() || endTimeTextBox.getText().isEmpty()) {
                //throw new Exception("Please fill out times.");
            }
            LocalTime startTime = LocalTime.parse(startTimeTextBox.getText(), dtf);
            LocalTime endTime = LocalTime.parse(endTimeTextBox.getText(), dtf);

            appointmentSelected.setStartDateTime(HelperFunctions.convertLocalToUTC(LocalDateTime.of(startDate, startTime)));
            appointmentSelected.setEndDateTime(HelperFunctions.convertLocalToUTC(LocalDateTime.of(endDate, endTime)));
            AppointmentDAO.updateAppointmentTime(appointmentSelected);
            HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                    UpdateAppointmentTime.class, backButton, "Scheduler", 1200, 400);
        }catch(DateTimeParseException dtpe){
            HelperFunctions.showError("Error", "Please enter time format of HH:mm");
        }
        catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }

    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                UpdateAppointmentTime.class, backButton, "Scheduler", 1200, 400);
    }
}
