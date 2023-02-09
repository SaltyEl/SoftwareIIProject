package com.c195project.c195project.DAO;

import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.helpers.JDBC;
import com.c195project.c195project.helpers.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Data Access Object Class which manages the CRUD operations between the MySQL server and Java Application for Customer.
 *
 * @author Blake Ramsey
 */
public class CustomerDAO {

    public static final String TABLE_CUSTOMERS = "client_schedule.customers";
    public static final String SELECT_ALL_COLUMNS = "SELECT * FROM";
    public static final String COLUMN_ID = "Customer_ID";
    public static final String COLUMN_NAME = "Customer_Name";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_POSTAL_CODE = "Postal_Code";
    public static final String COLUMN_DIVISION_ID = "Division_ID";

    /**
     * This method queries the database and returns a list of all Customers.
     *
     * @return Returns ObservableList of type Customer.
     * @throws SQLException
     */
    public static ObservableList<Customer> getCustomerList() throws SQLException{
    JDBC.openConnection();
    try{
        String getCustomerString = SELECT_ALL_COLUMNS + " " + TABLE_CUSTOMERS;
        Query.querySQL(getCustomerString);
        ObservableList<Customer> resultsList = FXCollections.observableArrayList();
        ResultSet result = Query.getResult();
        while(result.next()){
            Customer newCustomer = new Customer();
            newCustomer.setId(result.getInt(COLUMN_ID));
            newCustomer.setName(result.getString(COLUMN_NAME));
            newCustomer.setAddress(result.getString(COLUMN_ADDRESS));
            newCustomer.setPhoneNumber(result.getString(COLUMN_PHONE));
            newCustomer.setPostalCode(result.getString(COLUMN_POSTAL_CODE));
            newCustomer.setDivisionId(result.getInt(COLUMN_DIVISION_ID));
            newCustomer.setCountry(newCustomer.getDivisionId());
            resultsList.add(newCustomer);
        }
        return resultsList;
    }
    catch(SQLException e){
        System.out.println("Error: " + e.getMessage());
    }
    finally{
        JDBC.closeConnection();
    }

    return null;
}

    /**
     * This method queries the database and determine if customer ID provided as argument is contained within the Customer table.
     *
     * @param customerID The customerID to be searched for.
     * @return Return true if found, false if not found.
     * @throws SQLException
     */
    public static Boolean containsCustomerID(Integer customerID) throws SQLException {
    String stmt = "SELECT customer_id FROM client_schedule.customers ORDER BY customer_id";
    JDBC.openConnection();
    Query.querySQL(stmt);
    ResultSet result = Query.getResult();
    while(result.next()){
        Integer integer = result.getInt(1);
        if(integer.equals(customerID)){
            JDBC.closeConnection();
            return true;
        }
    }
    JDBC.closeConnection();
    return false;
}

    /**
     * This method queries the database and finds the greatest Customer ID.
     *
     * @return Returns an int representing the greatest Customer ID.
     */
    public static int findLastCustomerId(){
    JDBC.openConnection();
    try{
        //SELECT * FROM client_schedule.customers ORDER BY Customer_ID DESC LIMIT 1;
        String sqlQuery = SELECT_ALL_COLUMNS + " " + TABLE_CUSTOMERS +
                " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        Query.querySQL(sqlQuery);
        ResultSet result = Query.getResult();
        if(result.next()) {
            return result.getInt("Customer_ID");
        }
        else{
            return 0;
        }
    }catch(SQLException e){
        System.out.println("Error: " + e.getMessage());
    }
    finally{
        JDBC.closeConnection();
    }
    return -1;
}

    /**
     * This method accesses the database and inserts a Customer using information provided in argument.
     *
     * @param customer The Customer object to be inserted into the database.
     * @throws SQLException
     */
    public static void insertCustomer(Customer customer) throws SQLException {
    //INSERT INTO customers VALUES(1, 'Daddy Warbucks', '1919 Boardwalk',
    // '01291', '869-908-1875', NOW(), 'script', NOW(), 'script', 29);
    String insertStmt = "INSERT INTO " + TABLE_CUSTOMERS + " VALUES(" +
            customer.getId() + ", " + "'" + customer.getName() + "', '"
            + customer.getAddress() + "', '" + customer.getPostalCode()
            + "', '" + customer.getPhoneNumber() + "', NOW(), '" + LoginPage.currentUser
            + "', NOW(), '" + LoginPage.currentUser + "', " + customer.getDivisionId() + ")";
    System.out.println(insertStmt);
    JDBC.openConnection();
    Query.querySQL(insertStmt);
    JDBC.closeConnection();
}

    /**
     * This method utilizes the Update statement to update the database with the customer object provided as an argument.
     *
     * @param customer The Customer object to be updated.
     * @throws SQLException
     */
    public static void updateCustomer(Customer customer) throws SQLException {
    String updateStmt = "UPDATE " + TABLE_CUSTOMERS + " SET customer_name='" + customer.getName()
            + "', address='" + customer.getAddress() +"', postal_code='" + customer.getPostalCode()
            + "', phone='" + customer.getPhoneNumber() + "', last_update=NOW(), last_updated_by='"
            + LoginPage.currentUser + "', division_id=" + customer.getDivisionId() + " WHERE customer_id="
            + customer.getId();
    JDBC.openConnection();
    Query.querySQL(updateStmt);
    JDBC.closeConnection();
}

    /**
     * This method utilizes the Delete statement to delete the specified Customer from the database.
     *
     * @param customer Customer to be deleted.
     * @throws SQLException
     */
    public static void deleteCustomer(Customer customer) throws SQLException {
    String deleteStatement = "DELETE FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_ID
            + " = " + customer.getId();
    JDBC.openConnection();
    Query.querySQL(deleteStatement);
    JDBC.closeConnection();
}

}
