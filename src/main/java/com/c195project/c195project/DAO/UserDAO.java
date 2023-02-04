package com.c195project.c195project.DAO;

import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.helpers.JDBC;
import com.c195project.c195project.helpers.Query;
import com.c195project.c195project.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * This method connects to database and finds a user (if they exist) within the client_schedule.users
     * table. If user is found, then a User object is created and returned.
     *
     * @return Returns User object or null if User object does not exist.
     * @throws SQLException
     */
 public static User findUser() throws SQLException {
     try {
         JDBC.openConnection();
         String sqlStatement = "SELECT * FROM client_schedule.users WHERE User_Name = '" + LoginPage.getUserName()
                 + "'";
         Query.querySQL(sqlStatement);
         User userResult;
         ResultSet result = Query.getResult();
         if (result.next()) {
             int userId = result.getInt("User_ID");
             String userName = result.getString("User_Name");
             String password = result.getString("Password");
             userResult = new User(userId, userName, password);
             return userResult;
         } else{
             //Alerts.showError("User","No matching user found");
         }
     }catch(SQLException e){
         System.out.println("Error: " + e.getMessage());
     }
     finally{
         JDBC.closeConnection();
     }
     return null;
 }

    public static Boolean containsUserId(Integer userID) throws SQLException {
        String stmt = "SELECT user_id FROM client_schedule.users ORDER BY user_id";
        JDBC.openConnection();
        Query.querySQL(stmt);
        ResultSet result = Query.getResult();
        while(result.next()){
            Integer integer = result.getInt(1);
            if(integer.equals(userID)){
                JDBC.closeConnection();
                return true;
            }
        }
        JDBC.closeConnection();
        return false;
    }

}
