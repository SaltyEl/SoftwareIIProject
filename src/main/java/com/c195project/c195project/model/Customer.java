package com.c195project.c195project.model;

import com.c195project.c195project.DAO.CountryDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;

/**
 * The class to create and manipulate Customers throughout the program.
 *
 * @author Blake Ramsey
 */
public class Customer {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty address;
    private SimpleStringProperty postalCode;
    private SimpleStringProperty phoneNumber;
    private SimpleIntegerProperty divisionId;
    private SimpleStringProperty country;

    public Customer() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.postalCode = new SimpleStringProperty();
        this.phoneNumber = new SimpleStringProperty();
        this.divisionId = new SimpleIntegerProperty();
        this.country = new SimpleStringProperty();
    }

    /**
     *
     * @return the country
     */
    public String getCountry() {
        return country.get();
    }

    /**
     * This method sets the country for each Customer by calling the CountryDAO.getCountryFromDivisionID() method.
     *
     * @param divisionID The division ID
     * @throws SQLException
     */
    public void setCountry(int divisionID) throws SQLException {
        this.country.set(CountryDAO.getCountryFromDivisionID(getDivisionId()));
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return id.get();
    }

    /**
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     *
     * @return the address
     */
    public String getAddress() {
        return address.get();
    }

    /**
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address.set(address);
    }

    /**
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode.get();
    }

    /**
     *
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    /**
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    /**
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    /**
     *
     * @return the division ID
     */
    public int getDivisionId() {
        return divisionId.get();
    }

    /**
     *
     * @param divisionId the divisionID to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId.set(divisionId);
    }
}
