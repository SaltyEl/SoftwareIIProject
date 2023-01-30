package helper;

import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public static final String TABLE_CUSTOMERS = "client_schedule.customers";
    public static final String SELECT_ALL_COLUMNS = "SELECT * FROM";
    public static final String COLUMN_ID = "Customer_ID";
    public static final String COLUMN_NAME = "Customer_Name";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_POSTAL_CODE = "Postal_Code";
    public static final String COLUMN_DIVISION_ID = "Division_ID";


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

public static void deleteCustomer(){

}

}
