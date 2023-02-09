package com.c195project.c195project.model;

/**
 * The class to create and manipulate Contacts throughout the program.
 *
 * @author Blake Ramsey
 */
public class Contact {
    private int id;
    private String name;
    private String email;

    @Override
    public String toString() {
        return getName();
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

    /**
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
