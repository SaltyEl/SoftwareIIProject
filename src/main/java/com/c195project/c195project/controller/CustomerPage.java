package com.c195project.c195project.controller;

import com.c195project.c195project.Main;
import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.DAO.AppointmentDAO;
import com.c195project.c195project.DAO.CustomerDAO;
import com.c195project.c195project.helpers.HelperFunctions;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CustomerPage implements Initializable {

    public Button logoutButton;
    public TableView<Customer> customerTableView;
    public TableColumn<Customer, Integer> IdCol;
    public TableColumn<Customer, String> nameCol;
    public TableColumn<Customer, String> addressCol;
    public TableColumn<Customer, Integer> divIdCol;
    public TableColumn<Customer, String> phoneCol;
    public TableColumn<Customer, String> postalCodeCol;
    public Button customerAddButton;
    public Button schedulerButton;
    public static Boolean customerIsNotSelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if (LoginPage.loginButtonClicked) {
                Appointment appointment = hasApptWithinFifteen();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                if (appointment != null) {
                    alert.setContentText("You have an appointment within 15 minutes."
                            + "\nAppointment ID: " + appointment.getId()
                            + "\nAppointment Time and Date: " + appointment.getFormattedStartTime());
                } else {
                    alert.setContentText("No appointments in the next 15 minutes");
                }
                alert.showAndWait();
            }
        } catch (SQLException e) {
            HelperFunctions.showError("Error", e.getMessage());
        }
        finally{
            LoginPage.loginButtonClicked = false;
        }

        Task<ObservableList<Customer>> task = new GetAllCustomersTask();
        customerTableView.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException {
        AddUpdateCustomer.addButtonClicked = true;
        HelperFunctions.windowLoader("/com/c195project/c195project/Add-Update-Customer.fxml",
                LoginPage.class, customerAddButton, "Add", 600, 600);
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            AddUpdateCustomer.addButtonClicked = false;
            AddUpdateCustomer.customerToUpdate = customerTableView.getSelectionModel().getSelectedItem();
            HelperFunctions.windowLoader("/com/c195project/c195project/Add-Update-Customer.fxml",
                    LoginPage.class, customerAddButton, "Update", 600, 600);
        }catch(IOException e){
            HelperFunctions.showError("No selection", "Must select a customer to update.");
        }
    }

    public void onSchedulerButtonClick(ActionEvent actionEvent) throws IOException {
        Scheduler.customerSelected = customerTableView.getSelectionModel().getSelectedItem();
        Predicate<Customer> selectionIsEmpty = (customer) -> customer == null;
        customerIsNotSelected = selectionIsEmpty.test(Scheduler.customerSelected);
        HelperFunctions.windowLoader("/com/c195project/c195project/Scheduler.fxml",
                CustomerPage.class, schedulerButton, "Scheduler", 1200, 400);
    }

    public void onLogoutClick (ActionEvent actionEvent) throws IOException {
        HelperFunctions.frenchWindowLoader("/com/c195project/c195project/login-page.fxml",
                CustomerPage.class, logoutButton, "Login", 409, 235);
    }

    public void onCustomerDeleteButtonClick(ActionEvent actionEvent) throws Exception{
        try {
            if(customerTableView.getSelectionModel().getSelectedItem() == null){
                throw new Exception("No customer selected");
            }
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            int numAppts = AppointmentDAO.numOfCustomerAppointments(customer);
            if(numAppts > 0){
                System.out.println("Client has " + numAppts + " appt");
                if(numAppts == 1) {
                    throw new Exception("Customer cannot be deleted. Customer has " + numAppts + " appointment");
                }
                else{
                    throw new Exception("Customer cannot be deleted. Customer has " + numAppts + " appointments");
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    CustomerDAO.deleteCustomer(customer);
                    Task<ObservableList<Customer>> task = new GetAllCustomersTask();
                    customerTableView.itemsProperty().bind(task.valueProperty());
                    new Thread(task).start();
                }
            }
        }catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    private Appointment hasApptWithinFifteen() throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        List<Appointment> userAppointments = AppointmentDAO.getApptListByUserId();
        String currentTime = LocalTime.now().format(dtf);
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.parse(currentTime);

        BiPredicate<LocalTime, LocalTime> isWithin15 = (start, fifteenBefore) -> timeNow.equals(start)
                || timeNow.equals(fifteenBefore) || (timeNow.isAfter(fifteenBefore) && timeNow.isBefore(start));
        Predicate<LocalDate> isSameDate = d -> dateNow.equals(d);

        for (Appointment appointment : userAppointments) {
            LocalTime startTime = appointment.getStartDateTime().toLocalTime();
            LocalTime startTimeMinusFifteen = startTime.minusMinutes(15);
            LocalDate date = appointment.getStartDateTime().toLocalDate();
            if (isWithin15.test(startTime, startTimeMinusFifteen) && isSameDate.test(date)) {
                return appointment;
            }
        }
        return null;
    }
}

class GetAllCustomersTask extends Task {

    @Override
    public ObservableList<Customer> call() throws Exception {
        return CustomerDAO.getCustomerList();
    }
}