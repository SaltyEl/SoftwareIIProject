package com.c195project.c195project.helpers;

import com.c195project.c195project.DAO.AppointmentDAO;
import com.c195project.c195project.DAO.DivisionDAO;
import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Country;
import com.c195project.c195project.model.Division;
import com.c195project.c195project.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class consists of methods which can assist throughout the implementation of this program.
 *
 * @author Blake Ramsey
 */
public class HelperFunctions {

    /**
     * Shows Error Pop-up when called.
     *
     * @param title Title of error box.
     * @param message Message to be displayed about error that is being handled.
     */
    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method directs user to the next appropriate window.<br><br> Improvements: I have altered
     * the method such that it can be used statically across the program. In Software I project I was
     * not able to figure this out.
     *
     * @param fxmlDoc The location of the window that should be loaded.
     * @param classname The class from which the method is called.
     * @param buttonClicked The button that is clicked.
     * @param title Title of the window to be loaded.
     * @param width The width of window to be loaded.
     * @param height The height of window to be loaded.
     * @throws IOException
     */
    public static void windowLoader(String fxmlDoc, Class classname, Button buttonClicked, String title, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(classname.getResource(fxmlDoc));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = (Stage) buttonClicked.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is the same as the windowLoader, except it accounts for Locale and will adapt language accordingly.
     *
     * @param fxmlDoc The location of the window that should be loaded.
     * @param classname The class from which the method is called.
     * @param buttonClicked The button that is clicked.
     * @param title Title of the window to be loaded.
     * @param width The width of window to be loaded.
     * @param height The height of window to be loaded.
     * @throws IOException
     */
    public static void frenchWindowLoader(String fxmlDoc, Class classname, Button buttonClicked, String title, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(classname.getResource(fxmlDoc));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = (Stage) buttonClicked.getScene().getWindow();
        if(Locale.getDefault().getLanguage().equals("fr")) {
            stage.setTitle(LoginPage.convertWordToFrenchCA("Login"));
        }else{
            stage.setTitle("Login");
        }
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method utilizes the DivisionDAO.getDivisionsList() method to obtain a list of Divisions, and then filters
     * that list by Country. Lambda justification is ease of use and method not needed else where.
     *
     * @param country The Country to filter Divisions by.
     * @return Returns ObservableList of type Division.
     */
    public static ObservableList<Division> filterDivisionByCountry(Country country){
        ObservableList<Division> divisionList = DivisionDAO.getDivisionsList();
        ObservableList<Division> filteredList =
                divisionList.stream()
                        .filter(s -> s.getCountryId() == country.getId())
                        .sorted(Comparator.comparing(Division::getDivisionName))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
        return filteredList;
    }

    /**
     * This method converts user local time and date into any other timezone as specified by the ZoneID argument.
     *
     * @param timeToConvert The LocalDateTime object entered by the user.
     * @param zoneId The ZoneID used to convert the users LocalDateTime object
     * @return The LocalDateTime after conversion using ZoneID.
     */
    public static LocalDateTime convertLocalTime(LocalDateTime timeToConvert, ZoneId zoneId){
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime zonedTime = timeToConvert.atZone(localZone);
        return zonedTime.withZoneSameInstant(zoneId).toLocalDateTime();
    }

    /**
     * A method to determine whether or not the time entered by user is within business hours. This method also uses the date to determine
     * if the use has entered a meeting start and end time that would run overnight.  Used Lambda expressions to define Predicate and BiPredicate
     * variables.
     *
     * @param startDateTime The LocalDateTime object representing the start of the meeting.
     * @param endDateTime The LocalDateTime object representing the end of the meeting.
     * @return Returns True if times are within business hours, and false otherwise.
     */
    public static Boolean businessIsOpen(LocalDateTime startDateTime, LocalDateTime endDateTime){
        ZoneId eastern = ZoneId.of("America/New_York");
        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(22, 0);
        LocalTime startTimeInEastern = convertLocalTime(startDateTime, eastern).toLocalTime();
        LocalTime endTimeInEastern = convertLocalTime(endDateTime, eastern).toLocalTime();

        Predicate<LocalTime> isDuringBusinessHours = (t -> (t.isAfter(open) && t.isBefore(close)) || (t.equals(open)) || (t.equals(close)));
        BiPredicate<LocalDate, LocalDate> isNotOverNight = LocalDate::equals;

        return (isDuringBusinessHours.test(startTimeInEastern)
                && (isDuringBusinessHours.test(endTimeInEastern))
                && (isNotOverNight.test(startDateTime.toLocalDate(), endDateTime.toLocalDate())));
    }

    /**
     * This method determines if there is appointment overlap when creating or updating an appointment, and if so returns true. Lambdas
     * are used to define filters within a stream, which improves readability.
     *
     * @param appointment The appointment to check against all other customer appointments.
     * @return Returns true if there is appointment overlap, and false if there is no overlap.
     * @throws SQLException
     */
    public static boolean doAppointmentsOverlap(Appointment appointment) throws SQLException {
        LocalTime start = appointment.getStartDateTime().toLocalTime();
        LocalTime end = appointment.getEndDateTime().toLocalTime();
        LocalDate date = appointment.getStartDateTime().toLocalDate();

        System.out.println("ID:" + appointment.getId());

        List<Appointment> apptList = AppointmentDAO.getAppointmentList();
        List<Appointment> filteredList = apptList.stream()
                .filter(a -> a.getStartDateTime().toLocalDate().equals(date))
                .filter(a -> a.getCustomerID() == appointment.getCustomerID())
                .filter(a -> a.getId() != appointment.getId()).toList();

        for(Appointment a : filteredList){
            System.out.println(a.toString());
            LocalTime compareStart = a.getStartDateTime().toLocalTime();
            LocalTime compareEnd = a.getEndDateTime().toLocalTime();
            System.out.println("Comparison start: " + compareStart);
            System.out.println("Comparison end: " + compareEnd);
            boolean startIsDuringAppointment = start.isAfter(compareStart) && start.isBefore(compareEnd);
            boolean endIsDuringAppointment = end.isAfter(compareStart) && end.isBefore(compareEnd);
            boolean apptBetweenStartAndEnd = compareStart.isBefore(end) && compareEnd.isBefore(end)
                    && compareEnd.isAfter(start) && compareStart.isAfter(start);
            boolean startsAreSame = compareStart.equals(start);
            boolean endsAreSame = compareEnd.equals(end);
            if(startIsDuringAppointment || endIsDuringAppointment || apptBetweenStartAndEnd || startsAreSame || endsAreSame){
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to update login_activity.txt on user login attempts and whether they were successful or not. This method
     * updates txt file with Username, date, time and success / failure of login attempt.
     *
     * @param userName The User attempting to log in.
     * @param loginSuccess The boolean representing success / failure of attempt.
     * @throws IOException
     */
    public static void trackUserLoginAttempts(String userName, boolean loginSuccess) throws IOException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime timeDateUTC = HelperFunctions.convertLocalTime(LocalDateTime.now(), ZoneId.of("UTC"));
        String timeUTC = timeDateUTC.toLocalTime().format(timeFormat);
        String dateUTC = timeDateUTC.toLocalDate().format(dateFormat);

        //Login Attempt by USERNAME on DATE at TIME - SUCCESS / FAILURE;
        StringBuilder sb = new StringBuilder();
        sb.append("Login Attempt by '" + userName + "' on " + dateUTC + " at " + timeUTC + " UTC - ");
        if(loginSuccess){
            sb.append("SUCCESS");
        }
        else{
            sb.append("FAILURE");
        }
        String loginAttemptString = sb.toString();
        String fileName = "login_activity.txt";
        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter fileOutput = new PrintWriter(fileWriter);
        fileOutput.println(loginAttemptString);
        fileOutput.close();
    }
}
