package com.c195project.c195project.controller;

import com.c195project.c195project.model.Customer;
import helper.AppointmentDAO;
import helper.CustomerDAO;
import helper.HelperFunctions;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<ObservableList<Customer>> task = new GetAllCustomersTask();
        customerTableView.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException {
        AddUpdateCustomer.addButtonClicked = true;
        HelperFunctions.windowLoader("/com/c195project/c195project/Add-Update-Customer.fxml",
                LoginPage.class, customerAddButton, 600, 600);
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            AddUpdateCustomer.addButtonClicked = false;
            AddUpdateCustomer.customerToUpdate = customerTableView.getSelectionModel().getSelectedItem();
            HelperFunctions.windowLoader("/com/c195project/c195project/Add-Update-Customer.fxml",
                    LoginPage.class, customerAddButton, 600, 600);
        }catch(IOException e){
            HelperFunctions.showError("No selection", "Must select a customer to update.");
        }
    }

    public void onSchedulerButtonClick(ActionEvent actionEvent) {
    }

    public void onLogoutClick (ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/login-page.fxml",
                CustomerPage.class, logoutButton,409, 235);
    }

    public void onCustomerDeleteButtonClick(ActionEvent actionEvent) throws Exception{
        try {
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
                System.out.println("Client does not have appt.");
            }
        }catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }
}

class GetAllCustomersTask extends Task {

    @Override
    public ObservableList<Customer> call() throws Exception {
        return CustomerDAO.getCustomerList();
    }
}