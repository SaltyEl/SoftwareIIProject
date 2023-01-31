package helper;

import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import javafx.collections.FXCollections;
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

    public static ObservableList<Appointment> getAppointmentList() throws SQLException {
        String queryStmt = "SELECT * FROM client_schedule.appointments";
        JDBC.openConnection();
        Query.querySQL(queryStmt);
        ResultSet result = Query.getResult();
        ObservableList<Appointment> resultList = FXCollections.observableArrayList();
        while(result.next()){
            Appointment nextAppointment = new Appointment();
            nextAppointment.setId(result.getInt("Appointment_ID"));
            nextAppointment.setTitle(result.getString("Title"));
            nextAppointment.setDescription(result.getString("Description"));
            nextAppointment.setLocation(result.getString("Location"));
            nextAppointment.setType(result.getString("Type"));
            nextAppointment.setStartDateTime(result.getString("Start"));
            nextAppointment.setEndDateTime(result.getString("End"));
            nextAppointment.setContactID(result.getInt("Contact_ID"));
            nextAppointment.setUserID(result.getInt("User_ID"));
            nextAppointment.setCustomerID(result.getInt("Customer_ID"));
            resultList.add(nextAppointment);
        }
        JDBC.closeConnection();
        return resultList;
    }
}
