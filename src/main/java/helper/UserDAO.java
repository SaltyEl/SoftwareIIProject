package helper;

import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.model.User;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

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

}
