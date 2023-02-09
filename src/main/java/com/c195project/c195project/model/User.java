package com.c195project.c195project.model;

/**
 * The class to create and manipulate Users throughout the program.
 *
 * @author Blake Ramsey
 */
public class User {
    private int userId;
    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return getUserName();
    }

    /**
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId the userID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return the password to set
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
