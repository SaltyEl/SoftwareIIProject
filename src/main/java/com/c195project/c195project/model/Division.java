package com.c195project.c195project.model;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
