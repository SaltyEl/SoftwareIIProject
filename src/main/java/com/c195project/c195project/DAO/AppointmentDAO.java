package com.c195project.c195project.DAO;

import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.controller.UpdateAppointmentTime;
import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.helpers.JDBC;
import com.c195project.c195project.helpers.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class AppointmentDAO {

    public static final String TABLE_APPOINTMENTS = "client_schedule.appointments";

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

    public static ObservableList<Appointment> getApptListByCustomerID(Customer customer) throws SQLException {
        ObservableList<Appointment> listToFilter = getAppointmentList();
        ObservableList<Appointment> resultList = listToFilter.stream()
                .filter(a -> a.getCustomerID() == customer.getId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        return resultList;
    }

    public static void addAppointment(Appointment appointment) throws SQLException {
        //INSERT INTO appointments VALUES(1, 'title', 'description', 'location',
        // 'Planning Session', '2020-05-28 12:00:00',
        // '2020-05-28 13:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 3);
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

    public static void updateAppointmentTime(Appointment appointment) throws SQLException {
        String stmt = "UPDATE client_schedule.appointments SET start='" + Timestamp.valueOf(appointment.getStartDateTime())
                + "', end='" + Timestamp.valueOf(appointment.getEndDateTime())  + "', last_update=NOW(), last_updated_by='"
                + LoginPage.currentUser + "' WHERE appointment_id=" + appointment.getId();

        JDBC.openConnection();
        Query.querySQL(stmt);
        JDBC.closeConnection();
    }
}
