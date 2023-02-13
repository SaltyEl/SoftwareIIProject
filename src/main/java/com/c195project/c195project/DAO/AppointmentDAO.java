package com.c195project.c195project.DAO;

import com.c195project.c195project.controller.CustomerPage;
import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.controller.Scheduler;
import com.c195project.c195project.controller.UpdateAppointmentTime;
import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Contact;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.helpers.JDBC;
import com.c195project.c195project.helpers.Query;
import com.c195project.c195project.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Data Access Object Class which manages the CRUD operations between the MySQL server and Java Application for Appointments.
 *
 * @author Blake Ramsey
 */
public class AppointmentDAO {

    public static final String TABLE_APPOINTMENTS = "client_schedule.appointments";

    /**
     * This method accesses the appointments table via a database query and returns the number of customer appointments for a specific customer
     *
     * @param customer The Customer object whose appointments should be searched for.
     * @return Returns an int representing the number of appointments for that customer.
     * @throws SQLException
     */
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

    /**
     * This method accesses the appointments table via a database query and gets an appointment list for display and
     * manipulation.
     *
     * @return Returns an ObservableList of Appointments.
     * @throws SQLException
     */
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
            LocalDateTime startTime = result.getTimestamp("Start").toLocalDateTime();
            nextAppointment.setStartDateTime(startTime);
            LocalDateTime endTime = result.getTimestamp("End").toLocalDateTime();
            nextAppointment.setEndDateTime(endTime);
            nextAppointment.setContactID(result.getInt("Contact_ID"));
            nextAppointment.setUserID(result.getInt("User_ID"));
            nextAppointment.setCustomerID(result.getInt("Customer_ID"));
            resultList.add(nextAppointment);
        }
        JDBC.closeConnection();
        return resultList;
    }

    /**
     * This method utilizes the getAppointmentList() method to fetch all appointments, and then filters these appointments
     * to show only appointments associated with a specified Customer ID. Lambda justification is readability and method only
     * needed within this method.
     *
     * @param customer The Customer object whose appointments should be searched for.
     * @return Returns an ObservableList of Appointments.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getApptListByCustomerID(Customer customer) throws SQLException {
        ObservableList<Appointment> listToFilter = getAppointmentList();
        ObservableList<Appointment> resultList = listToFilter.stream()
                .filter(a -> a.getCustomerID() == customer.getId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        return resultList;
    }

    /**
     * This method takes a Contact object as an argument and returns an ObservableList of Appointments, filtered by
     * Contact ID. List is further filtered by Customer appointments if Customer is selected prior to
     * entering the Scheduler window. This method utilizes the getAppointmentList() method to acquire a full appointment list from the
     * database. Lambda justification is readability and method only needed within this method.
     *
     * @param contact The contact object used to filter the appointment list.
     * @return Returns an ObservableList of Appointments.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getApptListByContactID(Contact contact) throws SQLException {
        if(CustomerPage.customerIsNotSelected){
            return getAppointmentList().stream()
                    .filter(a -> a.getContactID() == contact.getId())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        else{
            Customer customer = Scheduler.customerSelected;
            return getApptListByCustomerID(customer).stream()
                    .filter(a -> a.getContactID() == contact.getId())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

    }

    /**
     * This method queries the database and retrieves a list of Appointments by User ID.
     *
     * @return Returns a list of Appointments.
     * @throws SQLException
     */
    public static List<Appointment> getApptListByUserId() throws SQLException {
        User currentUser = UserDAO.getUser();
        String stmt = "SELECT * FROM client_schedule.appointments WHERE user_id=" + currentUser.getUserId();
        JDBC.openConnection();
        Query.querySQL(stmt);
        ResultSet result = Query.getResult();
        List<Appointment> resultList = new ArrayList<>();
        while(result.next()){
            int id = result.getInt("appointment_id");
            LocalDateTime start = result.getTimestamp("start").toLocalDateTime();
            Appointment appointment = new Appointment(id, start);
            resultList.add(appointment);
        }
        JDBC.closeConnection();
        return resultList;
    }

    /**
     * This method uses an insert statement to add an Appointment to the Appointments table in the database.
     *
     * @param appointment The appointment to be added.
     * @throws SQLException
     */
    public static void addAppointment(Appointment appointment) throws SQLException {
        String insertStmt = "INSERT INTO " + TABLE_APPOINTMENTS + " VALUES(" +
                appointment.getId() + ", " + "'" + appointment.getTitle() + "', '"
                + appointment.getDescription() + "', '" + appointment.getLocation()
                + "', '" + appointment.getType() + "', '" + Timestamp.valueOf(appointment.getStartDateTime())
                + "', '" + Timestamp.valueOf(appointment.getEndDateTime()) + "', NOW(), '"
                + LoginPage.currentUser + "', NOW(), '" + LoginPage.currentUser + "', " +
                appointment.getCustomerID() + ", " + appointment.getUserID() + ", " +
                appointment.getContactID() + ")";
        JDBC.openConnection();
        Query.querySQL(insertStmt);
        JDBC.closeConnection();
    }

    /**
     * This method uses an Update statement to update an existing appointment within the database.
     *
     * @param appointment The appointment to be updated.
     * @throws SQLException
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        String updateStmt = "UPDATE " + TABLE_APPOINTMENTS + " SET title='" + appointment.getTitle()
                + "', description='" + appointment.getDescription() +"', location='" + appointment.getLocation()
                + "', type='" + appointment.getType() + "', start='" + Timestamp.valueOf(appointment.getStartDateTime())
                + "', end='" + Timestamp.valueOf(appointment.getEndDateTime()) + "', last_update=NOW(), last_updated_by='"
                + LoginPage.currentUser + "', customer_id=" + appointment.getCustomerID() + ", user_id="
                + appointment.getUserID() + ", contact_id=" + appointment.getContactID()
                +" WHERE appointment_id=" + appointment.getId();
        System.out.println(updateStmt);
        JDBC.openConnection();
        Query.querySQL(updateStmt);
        JDBC.closeConnection();
    }
    public static void deleteAppointment(int id) throws SQLException {
        String deleteStatement = "DELETE FROM " + TABLE_APPOINTMENTS + " WHERE appointment_id = " + id;
        JDBC.openConnection();
        Query.querySQL(deleteStatement);
        JDBC.closeConnection();
    }

    /**
     * This method queries the database and returns the highest numbered appointment ID.
     *
     * @return Returns an int, the highest numbered appointment ID in the database at that time.
     */
    public static int findLastAppointmentID() {
        JDBC.openConnection();
        try {
            //SELECT * FROM client_schedule.customers ORDER BY Customer_ID DESC LIMIT 1;
            String sqlQuery = CustomerDAO.SELECT_ALL_COLUMNS + " " + TABLE_APPOINTMENTS +
                    " ORDER BY appointment_id DESC LIMIT 1";
            Query.querySQL(sqlQuery);
            ResultSet result = Query.getResult();
            if (result.next()) {
                return result.getInt("appointment_id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            JDBC.closeConnection();
        }
        return -1;
    }

    /**
     *This method updates the appointment time within the database.
     *
     * @param appointment The appointment to be updated.
     * @throws SQLException
     */
    public static void updateAppointmentTime(Appointment appointment) throws SQLException {
        String stmt = "UPDATE client_schedule.appointments SET start='" + Timestamp.valueOf(appointment.getStartDateTime())
                + "', end='" + Timestamp.valueOf(appointment.getEndDateTime())  + "', last_update=NOW(), last_updated_by='"
                + LoginPage.currentUser + "' WHERE appointment_id=" + appointment.getId();

        JDBC.openConnection();
        Query.querySQL(stmt);
        JDBC.closeConnection();
    }

    /**
     * This method queries the database and returns and int representing the number of appointments by Month.
     *
     * @param monthNum The month in which appointments should be counted.
     * @return Returns an int representing the number of appointments.
     * @throws SQLException
     */
    public static int countAppointmentsByMonth(int monthNum) throws SQLException {
        String stmt = "SELECT COUNT(*) FROM client_schedule.appointments WHERE MONTH(start) = " + monthNum;
        JDBC.openConnection();
        Query.querySQL(stmt);
        ResultSet result = Query.getResult();
        result.next();
        int numMonths = result.getInt(1);
        JDBC.closeConnection();
        return numMonths;
    }

    /**
     * This method queries the database and returns an int representing the number of appointments by Type.
     *
     * @param userQuery The Type of appointment to be queried in the database.
     * @return Returns the number of appointments.
     * @throws SQLException
     */
    public static int countAppointmentsByType(String userQuery) throws SQLException{
        String stmt = "SELECT COUNT(*) FROM client_schedule.appointments WHERE type = '" + userQuery + "'";
        JDBC.openConnection();
        Query.querySQL(stmt);
        ResultSet result = Query.getResult();
        result.next();
        int count = result.getInt(1);
        JDBC.closeConnection();
        return count;
    }

    /**
     * This method queries the database and returns an int representing the number of appointments by Type AND Month.
     *
     * @param userQuery The Type of appointment to be queried in the database.
     * @param monthNum The month in which appointments should be counted.
     * @return Returns the number of appointments.
     * @throws SQLException
     */
    public static int countAppointmentsByTypeAndMonth(String userQuery, int monthNum) throws SQLException{
        String stmt = "SELECT COUNT(*) FROM client_schedule.appointments WHERE type = '" + userQuery
                + "' AND MONTH(start) = " + monthNum;
        JDBC.openConnection();
        Query.querySQL(stmt);
        ResultSet result = Query.getResult();
        result.next();
        int count = result.getInt(1);
        JDBC.closeConnection();
        return count;
    }

}
