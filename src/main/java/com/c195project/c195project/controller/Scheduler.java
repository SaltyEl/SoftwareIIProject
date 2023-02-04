package com.c195project.c195project.controller;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.DAO.AppointmentDAO;
import com.c195project.c195project.helpers.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Scheduler implements Initializable {
    public Button appointmentAddButton;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;
    public TableView appointmentTableView;
    public static Customer customerSelected;
    public Button backButton;
    public Label adjustableLabel;
    public TableColumn startCol;
    public RadioButton weekRadioButton;
    public RadioButton monthRadioButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(CustomerPage.customerIsNotSelected){
            adjustableLabel.setText("All Customer Appointments");
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

                Alert deleteInfo = new Alert(Alert.AlertType.INFORMATION);
                deleteInfo.setTitle("Appointment Deleted");
                deleteInfo.setContentText("Appointment Deleted -\n" + "ID: " + appointment.getId() +
                       "\nType: " + appointment.getType());
                deleteInfo.showAndWait();
            }
        }
        catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    public void onBackClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                LoginPage.class, backButton, 777, 402);
    }

    public void onWeekClick(ActionEvent actionEvent) throws SQLException {
        monthRadioButton.setText("Month");
        weekRadioButton.setText("Week - Next 7 Days");
        filterByTime(8);
    }

    public void onMonthClick(ActionEvent actionEvent) throws SQLException {
        weekRadioButton.setText("Week");
        monthRadioButton.setText("Month - Next 30 Days");
        filterByTime(31);
    }

    private void filterByTime(int addDays) throws SQLException {
        LocalDate startDateMinusOne = LocalDate.now().minusDays(1);
        LocalDate endDatePlusOne = LocalDate.now().plusDays(addDays);

        System.out.println(startDateMinusOne + "\n" + endDatePlusOne);

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        if(CustomerPage.customerIsNotSelected){
            appointmentList = AppointmentDAO.getAppointmentList();
        }
        else{
            appointmentList = AppointmentDAO.getApptListByCustomerID(customerSelected);
        }

        Predicate<Appointment> isBeforeEndAndAfterStart = a -> a.getStartDateTime().toLocalDate().isBefore(endDatePlusOne)
                && a.getStartDateTime().toLocalDate().isAfter(startDateMinusOne);

        ObservableList<Appointment> filteredList = appointmentList.stream()
                .filter(isBeforeEndAndAfterStart)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        System.out.println(filteredList);

        appointmentTableView.setItems(filteredList);
    }

    public void onAllClicked(ActionEvent actionEvent) {
        weekRadioButton.setText("Week");
        monthRadioButton.setText("Month");
        if(CustomerPage.customerIsNotSelected){
            adjustableLabel.setText("All Customer Appointments");
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
}
