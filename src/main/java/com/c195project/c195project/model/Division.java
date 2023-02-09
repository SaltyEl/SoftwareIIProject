package com.c195project.c195project.model;

/**
 * The class to create and manipulate Divisions throughout the program.
 *
 * @author Blake Ramsey
 */
public class Division {
    private int id;
    private String divisionName;
    private int countryId;

    public Division() {
    }

    public Division(String divisionName) {
        this.divisionName = divisionName;
    }

    @Override
    public String toString() {
        return getDivisionName();
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return the divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     *
     * @param divisionName the division name to set.
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     *
     * @return the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     *
     * @param countryId The country ID to set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
