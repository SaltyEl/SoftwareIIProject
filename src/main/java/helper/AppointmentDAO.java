package helper;

import com.c195project.c195project.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {

    public static int numOfCustomerAppointments(Customer customer) throws SQLException {
        String queryStmt = "SELECT COUNT(customer_id) FROM client_schedule.appointments WHERE customer_id = " + customer.getId();
        JDBC.openConnection();
        Query.querySQL(queryStmt);
        ResultSet result = Query.getResult();
        result.next();
        JDBC.closeConnection();
        return result.getInt(1);
    }
}
