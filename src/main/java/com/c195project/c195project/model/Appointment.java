package com.c195project.c195project.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class allows for Appointments to be created and manipulated throughout the program.
 *
 * @author Blake Ramsey
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private String formattedStartTime;
    private LocalDateTime endDateTime;
    private String formattedEndTime;
    private int customerID;
    private int userID;
    private int contactID;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

    public Appointment() {
        this.id = 0;
    }
    public Appointment(int id, LocalDateTime startTime){
        this.id = id;
        this.startDateTime = startTime;
        this.formattedStartTime = startDateTime.format(dtf);
    }

    @Override
    public String toString(){
        return String.valueOf(getId());
    }

    /**
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id the id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type the type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return the startDateTime
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     *
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        this.formattedStartTime = startDateTime.format(dtf);
    }

    /**
     *
     * @return the endDateTime
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     *
     * @return the formattedStartTime
     */
    public String getFormattedStartTime(){
        return formattedStartTime;
    }

    /**
     *
     * @return the formattedEndTime
     */
    public String getFormattedEndTime(){
        return formattedEndTime;
    }

    /**
     *
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        this.formattedEndTime = endDateTime.format(dtf);
    }

    /**
     *
     * @return the customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @param customerID the customerID to set.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     *
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     *
     * @return the contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID the contactID to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
