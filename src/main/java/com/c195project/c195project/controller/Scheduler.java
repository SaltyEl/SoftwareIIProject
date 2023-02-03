package com.c195project.c195project.controller;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import helper.AppointmentDAO;
import helper.CustomerDAO;
import helper.HelperFunctions;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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

    }

    public void onAppointmentUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        AddUpdateAppointment.addButtonClicked = false;
        AddUpdateAppointment.appointmentSelected = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();
        HelperFunctions.windowLoader("/com/c195project/c195project/AddUpdateAppointment.fxml",
                LoginPage.class, appointmentAddButton, 600 , 600);
    }

    public void onAppointmentDeleteButtonClick(ActionEvent actionEvent) throws Exception {
        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("No appointment selected");
            }
            Appointment appointment = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppointment(appointment.getId());

                ObservableList<Appointment> appointmentList = AppointmentDAO.getAppointmentList();
                appointmentTableView.setItems(appointmentList);
            }
        }
        catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    public void onBackClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                LoginPage.class, backButton, 632, 402);
    }

}
