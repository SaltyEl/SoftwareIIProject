package com.c195project.c195project.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The class to create and manipulate Countries throughout the program.
 *
 * @author Blake Ramsey
 */
public class Country {
    private int id;
    private String name;

    @Override
    public String toString() {
        return (getName());
    }

    public Country() {
    }

    public Country(int id, String name){
        this.id = id;
        this.name = name;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
