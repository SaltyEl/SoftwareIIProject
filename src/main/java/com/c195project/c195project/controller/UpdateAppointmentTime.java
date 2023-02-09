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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.function.BiPredicate;

/**
 * The controller for interaction between Scheduler.java, AppointmentDAO.java and TimeChange.fxml.
 *
 * @author Blake Ramsey
 */
public class UpdateAppointmentTime implements Initializable {

    public static Appointment appointmentSelected;
    public TextField startTimeTextBox;
    public TextField endTimeTextBox;
    public Button backButton;
    public Button saveButton;

    /**
     * This method initializes the UpdateAppointmentTime controller after root element has been processed.
     *
     * @param url Resolves relative paths for root object, or null if location is not known.
     * <br>
     * @param resourceBundle Resources used to localize root object, or null if root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeTextBox.setText(appointmentSelected.getStartDateTime().toLocalTime().toString());
        endTimeTextBox.setText(appointmentSelected.getEndDateTime().toLocalTime().toString());
    }

    /**
     * This method saves the updated times of the appointment which was selected in previous window. If text fields are empty,
     * exception is thrown. If not within business hours, exception is thrown. If end time is before start time, exception is
     * thrown. After save takes place, user is directed back to Scheduler page.
     *
     * @param actionEvent The actionEvent is clicking on the saveButton.
     */
    public void onSaveClick(ActionEvent actionEvent) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime startDateTime = appointmentSelected.getStartDateTime();
            LocalDateTime endDateTime = appointmentSelected.getEndDateTime();

            LocalDate startDate = startDateTime.toLocalDate();
            LocalDate endDate = endDateTime.toLocalDate();

            if (startTimeTextBox.getText().isEmpty() || endTimeTextBox.getText().isEmpty()) {
                throw new Exception("Please fill out times.");
            }
            LocalTime startTime = LocalTime.parse(startTimeTextBox.getText(), dtf);
            LocalTime endTime = LocalTime.parse(endTimeTextBox.getText(), dtf);

            LocalDateTime adjustedStartDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime adjustedEndDateTime = LocalDateTime.of(endDate, endTime);
            boolean withinBusinessHours = HelperFunctions.businessIsOpen(adjustedStartDateTime, adjustedEndDateTime);
            if(!withinBusinessHours){
                throw new Exception("This is not within business hours.");
            }
            BiPredicate<LocalTime, LocalTime> isStartBeforeEnd = LocalTime::isBefore;
            if(!isStartBeforeEnd.test(adjustedStartDateTime.toLocalTime(), adjustedEndDateTime.toLocalTime())){
                throw new Exception("End time must be after start time");
            }

            appointmentSelected.setStartDateTime(adjustedStartDateTime);
            appointmentSelected.setEndDateTime(adjustedEndDateTime);
            boolean overlap = HelperFunctions.doAppointmentsOverlap(appointmentSelected);
            if(overlap){
                throw new Exception("Customer appointments cannot overlap");
            }

            ZoneId UTC = ZoneId.of("UTC");
            appointmentSelected.setStartDateTime(HelperFunctions.convertLocalTime(adjustedStartDateTime, UTC));
            appointmentSelected.setEndDateTime(HelperFunctions.convertLocalTime(adjustedEndDateTime, UTC));
            AppointmentDAO.updateAppointmentTime(appointmentSelected);
            HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                    UpdateAppointmentTime.class, saveButton, "Scheduler", 1200, 400);
        }catch(DateTimeParseException dtpe){
            HelperFunctions.showError("Error", "Please enter time format of HH:mm");
        }
        catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }

    }

    /**
     * This method utilizes the windoLoader method to send user back to Scheduler window.
     *
     * @param actionEvent actionEvent is clicking the backButton.
     * @throws IOException
     */
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                UpdateAppointmentTime.class, backButton, "Scheduler", 1200, 400);
    }
}
