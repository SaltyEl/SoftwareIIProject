package com.c195project.c195project.controller;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Contact;
import com.c195project.c195project.model.Customer;
import helper.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddUpdateAppointment implements Initializable {
    public static Boolean addButtonClicked;
    public ComboBox contactNameComboBox;
    public TextField appointmentIDTxt;
    public TextField addTitleTxt;
    public TextField addDescriptionTxt;
    public TextField addLocationTxt;
    public TextField addTypeTxt;
    public TextField startDateTimeTxt;
    public TextField endDateTimeTxt;
    public TextField userIDTxt;
    public TextField customerIDTxt;
    public Button addApptSaveButton;
    public Label AddUpdateApptTitleLbl;
    public static Appointment appointmentSelected;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

    public Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(addButtonClicked){
            AddUpdateApptTitleLbl.setText("Add Appointment");
            try {
                contactNameComboBox.setItems(ContactDAO.getContactList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            AddUpdateApptTitleLbl.setText("Update Appointment");
            addTitleTxt.setText(appointmentSelected.getTitle());
            addDescriptionTxt.setText(appointmentSelected.getDescription());
            addTypeTxt.setText(appointmentSelected.getType());
            addLocationTxt.setText(appointmentSelected.getLocation());
            startDateTimeTxt.setText(appointmentSelected.getFormattedStartTime());
            endDateTimeTxt.setText(appointmentSelected.getFormattedEndTime());
            userIDTxt.setText(String.valueOf(appointmentSelected.getUserID()));
            customerIDTxt.setText(String.valueOf(appointmentSelected.getCustomerID()));
            try {
                contactNameComboBox.setItems(ContactDAO.getContactList());
                contactNameComboBox.getSelectionModel().select(appointmentSelected.getContactID() - 1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        if (addTitleTxt.getText().isEmpty() || addDescriptionTxt.getText().isEmpty()
                || addTitleTxt.getText().isEmpty() || addLocationTxt.getText().isEmpty()
                || contactNameComboBox.getSelectionModel().isEmpty() || startDateTimeTxt.getText().isEmpty()
                || endDateTimeTxt.getText().isEmpty() || userIDTxt.getText().isEmpty()
                || customerIDTxt.getText().isEmpty() || addTypeTxt.getText().isEmpty()) {
            throw new IOException("All fields must be completed.");
        }
        try {
            if (addButtonClicked) {
                Appointment appointment = new Appointment();
                Appointment newAppointment = createAppointmentFromUserInfo(appointment);
                AppointmentDAO.addAppointment(newAppointment);
                HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                        AddUpdateAppointment.class, addApptSaveButton, 1200, 400);
            }
            else{
                Appointment updatedAppointment = createAppointmentFromUserInfo(appointmentSelected);
                AppointmentDAO.updateAppointment(updatedAppointment);
                HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                        AddUpdateAppointment.class, addApptSaveButton, 1200, 400);
            }
        } catch (IOException | SQLException e) {
            HelperFunctions.showError("Error", e.getMessage());
        }
        catch(DateTimeParseException dtpe){
            HelperFunctions.showError("Error", "Date and time must be filled out as prompted.");
        }
    }

        public void onCancelClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                LoginPage.class, cancelButton, 1200, 400);
    }

    private Appointment createAppointmentFromUserInfo(Appointment appointment) throws SQLException, IOException {
        if(appointment.getId() == 0) {
            appointment.setId(AppointmentDAO.findLastAppointmentID() + 1);
        }
        appointment.setTitle(addTitleTxt.getText());
        appointment.setDescription(addDescriptionTxt.getText());
        appointment.setType(addTypeTxt.getText());
        appointment.setLocation(addLocationTxt.getText());
        LocalDateTime timeEntered= LocalDateTime.parse(startDateTimeTxt.getText(), dtf);
        appointment.setStartDateTime(HelperFunctions.convertLocalToUTC(timeEntered));
        LocalDateTime endTimeEntered = LocalDateTime.parse(endDateTimeTxt.getText(), dtf);
        appointment.setEndDateTime(HelperFunctions.convertLocalToUTC(endTimeEntered));
        Integer customerID = Integer.valueOf(customerIDTxt.getText());
        if(!CustomerDAO.containsCustomerID(customerID)){
            throw new IOException("This customer does not exist");
        }
        appointment.setCustomerID(customerID);
        Integer userID = Integer.valueOf(userIDTxt.getText());
        if(!UserDAO.containsUserId(userID)){
            throw new IOException("This user does not exist");
        }
        appointment.setUserID(userID);
        Contact contact = (Contact) contactNameComboBox.getSelectionModel().getSelectedItem();
        appointment.setContactID(contact.getId());

        return appointment;
    }

}