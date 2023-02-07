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
    public Button updateTimeButton;
    public Button countButton;
    public TextField monthOrTypeTextField;
    public Label countLabel;
    private Month thisMonth;

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
                LoginPage.class, appointmentAddButton, "Add", 600 , 600);

    }

    public void onAppointmentUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        AddUpdateAppointment.addButtonClicked = false;
        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select appointment to update.");
            }
            AddUpdateAppointment.appointmentSelected = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();
            HelperFunctions.windowLoader("/com/c195project/c195project/AddUpdateAppointment.fxml",
                    LoginPage.class, appointmentAddButton, "Update", 600, 600);
        }
        catch(Exception e){
            HelperFunctions.showError("Appointment", e.getMessage());
        }
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
                LoginPage.class, backButton, "Customer", 777, 402);
    }

    public void onWeekClick(ActionEvent actionEvent) throws SQLException {
        monthRadioButton.setText("Month");
        weekRadioButton.setText("Week - Next 7 Days");
        LocalDate startDateMinusOne = LocalDate.now().minusDays(1);
        LocalDate endDatePlusOne = LocalDate.now().plusDays(8);

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

    public void onMonthClick(ActionEvent actionEvent) throws SQLException {
        weekRadioButton.setText("Week");
        this.thisMonth = LocalDate.now().getMonth();
        monthRadioButton.setText("Month - " + thisMonth.toString());
        LocalDate startDateMinusOne = LocalDate.now().minusDays(1);


        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        if(CustomerPage.customerIsNotSelected){
            appointmentList = AppointmentDAO.getAppointmentList();
        }
        else{
            appointmentList = AppointmentDAO.getApptListByCustomerID(customerSelected);
        }

        Predicate<Appointment> isSameMonth = a -> thisMonth.equals(a.getStartDateTime().getMonth());
        Predicate<Appointment> isOnOrAfterCurrentDate = a -> a.getStartDateTime().toLocalDate().isAfter(startDateMinusOne);

        ObservableList<Appointment> filteredList = appointmentList.stream()
                .filter(isSameMonth)
                .filter(isOnOrAfterCurrentDate)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

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

    public void onUpdateTimeClick(ActionEvent actionEvent) {
        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select appointment for time adjustment.");
            }
            UpdateAppointmentTime.appointmentSelected = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();
            HelperFunctions.windowLoader("/com/c195project/c195project/TimeChange.fxml",
                    Scheduler.class, updateTimeButton, "Time Change", 248, 152);
        }catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    public void onCountButtonClick(ActionEvent actionEvent) throws SQLException {
        if(monthOrTypeTextField.getText().matches("^(1[0-2]|[0-9]|0[0-9])$")){
            Integer monthNumber = Integer.parseInt(monthOrTypeTextField.getText());
            String monthNumberToString = monthNumber.toString();
            if(monthNumberToString.startsWith("0")){
                String simplifiedNum = monthNumberToString.substring(1);
                monthNumber = Integer.parseInt(simplifiedNum);
            }
            countLabel.setText(String.valueOf(AppointmentDAO.countAppointmentsByMonth(monthNumber)));
            return;
        }
        String userInput = monthOrTypeTextField.getText();
        Month[] allMonths = Month.values();
        for(Month month : allMonths){
            if(month.toString().equals(userInput.toUpperCase())){
                int monthNum = month.getValue();
                countLabel.setText(String.valueOf(AppointmentDAO.countAppointmentsByMonth(monthNum)));
                return;
            }
        }
        int typeCount = AppointmentDAO.countAppointmentsByType(userInput);
        countLabel.setText(String.valueOf(typeCount));
    }
}
