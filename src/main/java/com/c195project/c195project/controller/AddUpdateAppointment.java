package com.c195project.c195project.controller;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Contact;
import com.c195project.c195project.model.Customer;
import helper.AppointmentDAO;
import helper.ContactDAO;
import helper.CustomerDAO;
import helper.HelperFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(addButtonClicked){
            try {
                contactNameComboBox.setItems(ContactDAO.getContactList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{

        }
    }

    public Button cancelButton;
    public Button addPartSaveBtn;

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            if (addButtonClicked) {
                if (addTitleTxt.getText().isEmpty() || addDescriptionTxt.getText().isEmpty()
                        || addTitleTxt.getText().isEmpty() || addLocationTxt.getText().isEmpty()
                        || contactNameComboBox.getSelectionModel().isEmpty() || startDateTimeTxt.getText().isEmpty()
                        || endDateTimeTxt.getText().isEmpty() || userIDTxt.getText().isEmpty()
                        || customerIDTxt.getText().isEmpty() || addTypeTxt.getText().isEmpty()) {
                    throw new IOException("All fields must be completed.");
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
                Appointment appointment = new Appointment();
                appointment.setId(AppointmentDAO.findLastAppointmentID() + 1);
                appointment.setTitle(addTitleTxt.getText());
                appointment.setDescription(addDescriptionTxt.getText());
                appointment.setType(addTypeTxt.getText());
                appointment.setLocation(addLocationTxt.getText());
                appointment.setStartDateTime(LocalDateTime.parse(startDateTimeTxt.getText(), dtf));
                appointment.setEndDateTime(LocalDateTime.parse(endDateTimeTxt.getText(), dtf));
                appointment.setCustomerID(Integer.parseInt(customerIDTxt.getText()));
                appointment.setUserID(Integer.parseInt(userIDTxt.getText()));
                Contact contact = (Contact) contactNameComboBox.getSelectionModel().getSelectedItem();
                appointment.setContactID(contact.getId());
                System.out.println(appointment.getContactID());
                AppointmentDAO.addAppointment(appointment);
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
        HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                LoginPage.class, cancelButton, 632, 402);
    }

}
