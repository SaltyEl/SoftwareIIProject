package com.c195project.c195project.controller;

import com.c195project.c195project.model.Country;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.model.Division;
import com.c195project.c195project.DAO.CountryDAO;
import com.c195project.c195project.DAO.CustomerDAO;
import com.c195project.c195project.DAO.DivisionDAO;
import com.c195project.c195project.helpers.HelperFunctions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUpdateCustomer implements Initializable {
    public Button cancelButton;
    public TextField customerIDTxt;
    public static Boolean addButtonClicked;
    public Label AddUpdateTitleLabel;
    public TextField addCustomerNameTxt;
    public TextField customerAddressTxt;
    public TextField postalCodeTxt;
    public TextField phoneNumberTxt;
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> divisionComboBox;
    public Button addPartSaveBtn;
    public static Customer customerToUpdate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(addButtonClicked) {
            AddUpdateTitleLabel.setText("Add Customer");
            try {
                countryComboBox.setItems(CountryDAO.getCountryList());
                divisionComboBox.setItems(DivisionDAO.getDivisionsList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            AddUpdateTitleLabel.setText("Update Customer");
            customerIDTxt.setText(String.valueOf(customerToUpdate.getId()));
            addCustomerNameTxt.setText(customerToUpdate.getName());
            customerAddressTxt.setText(customerToUpdate.getAddress());
            postalCodeTxt.setText(customerToUpdate.getPostalCode());
            phoneNumberTxt.setText(customerToUpdate.getPhoneNumber());
            try {
                ObservableList<Country> countryList = CountryDAO.getCountryList();
                countryComboBox.setItems(countryList);
                countryComboBox.getSelectionModel().select(CountryDAO.getCountryFromCustomerID(customerToUpdate.getId()) - 1);

                Country countrySelected = countryComboBox.getSelectionModel().getSelectedItem();
                ObservableList<Division> divisionList = HelperFunctions.filterDivisionByCountry(countrySelected);
                divisionComboBox.setItems(divisionList);

                int index = -1;
                for(int i=0; i < divisionList.size();i++){
                    if(customerToUpdate.getDivisionId() == divisionList.get(i).getId()){
                        index = i;
                        break;
                    }
                }
                divisionComboBox.getSelectionModel().select(index);
            }catch(SQLException e){
                System.out.println("Error: " + e.getMessage());
            }

        }
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        try {
            if (addButtonClicked) {
                Customer newCustomer = new Customer();
                newCustomer.setId(CustomerDAO.findLastCustomerId() + 1);
                newCustomer.setName(addCustomerNameTxt.getText());
                newCustomer.setPostalCode(postalCodeTxt.getText());
                newCustomer.setPhoneNumber(phoneNumberTxt.getText());
                newCustomer.setAddress(customerAddressTxt.getText());
                newCustomer.setDivisionId(divisionComboBox.getSelectionModel().getSelectedItem().getId());
                if(newCustomer.getName().isEmpty() || newCustomer.getAddress().isEmpty()
                || newCustomer.getPhoneNumber().isEmpty() || newCustomer.getPostalCode().isEmpty()
                || divisionComboBox.getSelectionModel().isEmpty() || countryComboBox.getSelectionModel().isEmpty()){
                    throw new IOException("All fields must be completed.");
                }
                CustomerDAO.insertCustomer(newCustomer);
                HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                        LoginPage.class, addPartSaveBtn, "Customer", 777, 402);
            }
            else{
                if( divisionComboBox.getSelectionModel().isEmpty()){
                    throw new Exception("Please select a division.");
                }
                customerToUpdate.setDivisionId(divisionComboBox.getSelectionModel().getSelectedItem().getId());
                customerToUpdate.setName(addCustomerNameTxt.getText());
                customerToUpdate.setPostalCode(postalCodeTxt.getText());
                customerToUpdate.setPhoneNumber(phoneNumberTxt.getText());
                customerToUpdate.setAddress(customerAddressTxt.getText());
                if(customerToUpdate.getName().isEmpty() || customerToUpdate.getAddress().isEmpty()
                        || customerToUpdate.getPhoneNumber().isEmpty() || customerToUpdate.getPostalCode().isEmpty()){
                    throw new Exception("All fields must be completed.");
                }
                CustomerDAO.updateCustomer(customerToUpdate);
                HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                        LoginPage.class, cancelButton, "Customer", 777, 402);
            }
        }catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                LoginPage.class, cancelButton, "Customer", 777, 402);
    }

    public void onComboBoxClick(ActionEvent actionEvent) throws SQLException {
        Country country = countryComboBox.getSelectionModel().getSelectedItem();
        divisionComboBox.setItems(HelperFunctions.filterDivisionByCountry(country));
    }
}