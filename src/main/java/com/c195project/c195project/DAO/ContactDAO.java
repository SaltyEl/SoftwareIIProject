package com.c195project.c195project.DAO;

import com.c195project.c195project.model.Contact;
import com.c195project.c195project.helpers.Query;
import com.c195project.c195project.helpers.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {

    public static ObservableList<Contact> getContactList() throws SQLException {
        String stmt = "SELECT * FROM client_schedule.contacts";
        ObservableList<Contact> resultList = FXCollections.observableArrayList();
        JDBC.openConnection();
        Query.querySQL(stmt);
        ResultSet result = Query.getResult();
        while(result.next()){
            Contact contact = new Contact();
            contact.setId(result.getInt("Contact_ID"));
            contact.setName(result.getString("Contact_Name"));
            contact.setEmail(result.getString("email"));
            resultList.add(contact);
        }
        JDBC.closeConnection();
        return resultList;
    }
}
