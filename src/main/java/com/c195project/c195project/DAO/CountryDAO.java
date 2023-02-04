package com.c195project.c195project.DAO;

import com.c195project.c195project.model.Country;
import com.c195project.c195project.helpers.JDBC;
import com.c195project.c195project.helpers.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {

    public static final String COLUMN_COUNTRY_NAME = "country";
    public static final String COLUMN_COUNTRY_ID = "country_id";
    public static final String TABLE_COUNTRIES = "client_schedule.countries";

    public static ObservableList<Country> getCountryList() throws SQLException {
        try{
            JDBC.openConnection();
            String queryString = "SELECT " + COLUMN_COUNTRY_ID+ ", " +
                     COLUMN_COUNTRY_NAME + " FROM " + TABLE_COUNTRIES;
            Query.querySQL(queryString);
            ResultSet result = Query.getResult();
            ObservableList<Country> resultsList = FXCollections.observableArrayList();
            while(result.next()) {
                Country country = new Country();
                country.setId(result.getInt(COLUMN_COUNTRY_ID));
                country.setName(result.getString(COLUMN_COUNTRY_NAME));
                resultsList.add(country);
            }
            return resultsList;

        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        finally{
            JDBC.closeConnection();
        }
        return null;
    }

    public static int getCountryFromCustomerID(int customerID) throws SQLException {
        String sqlStmt = "SELECT countries.country_id FROM client_schedule.countries, client_schedule.customers, client_schedule.first_level_divisions " +
                "WHERE customers.division_id = first_level_divisions.division_id " +
                "AND first_level_divisions.country_id = countries.country_id AND customers.customer_id = " + customerID;
        JDBC.openConnection();
        Query.querySQL(sqlStmt);
        ResultSet result = Query.getResult();
        result.next();
        int countryID = result.getInt("country_id");
        JDBC.closeConnection();
        return countryID;
    }

    public static String getCountryFromDivisionID(int divisionID) throws SQLException{
        /*SELECT countries.country
        FROM client_schedule.first_level_divisions, client_schedule.countries
        WHERE first_level_divisions.country_id = countries.country_id
        AND first_level_divisions.division_id = 1;*/

        String sqlStmt = "SELECT countries.country " +
                "FROM client_schedule.first_level_divisions, client_schedule.countries " +
                "WHERE first_level_divisions.country_id = countries.country_id " +
                "AND first_level_divisions.division_id =" + divisionID;

        JDBC.openConnection();
        Query.querySQL(sqlStmt);
        ResultSet result = Query.getResult();
        result.next();
        String country = result.getString("country");
        JDBC.closeConnection();
        return country;
    }
}
