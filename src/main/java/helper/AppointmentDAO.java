package helper;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {

    public static int numOfCustomerAppointments(Customer customer) throws SQLException {
        String queryStmt = "SELECT COUNT(customer_id) FROM client_schedule.appointments WHERE customer_id = " + customer.getId();
        JDBC.openConnection();
        Query.querySQL(queryStmt);
        ResultSet result = Query.getResult();
        result.next();
        int numOfAppts = result.getInt(1);
        JDBC.closeConnection();
        return numOfAppts;
    }

    public static ObservableList<Appointment> getAppointmentList(){

    }
}
