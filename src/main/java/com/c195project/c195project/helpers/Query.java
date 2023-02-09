package com.c195project.c195project.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class provides methods for executing SELECT, UPDATE, DELETE and INSERT methods.
 */
public class Query {
    private static String query;
    private static Statement stmt;
    private static ResultSet result;

    /**
     * This method executes a query on the database with which it is connected.
     *
     * @param q A string representing the SQL statement to be executed.
     * @throws SQLException
     */
    public static void querySQL(String q) throws SQLException {
        query = q;
        try{
            stmt = JDBC.connection.createStatement();
            if(query.toLowerCase().startsWith("select")){
                result = stmt.executeQuery(q);
            }
            if(query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update") ||
                    query.toLowerCase().startsWith("delete")){
                stmt.executeUpdate(q);
            }
        }
        catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     *
     * @return the result.
     */
    public static ResultSet getResult() {
        return result;
    }
}
