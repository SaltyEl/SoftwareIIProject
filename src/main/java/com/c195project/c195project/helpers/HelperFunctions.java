package com.c195project.c195project.helpers;

import com.c195project.c195project.DAO.AppointmentDAO;
import com.c195project.c195project.DAO.DivisionDAO;
import com.c195project.c195project.controller.LoginPage;
import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Country;
import com.c195project.c195project.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static void windowLoader(String fxmlDoc, Class classname, Button buttonClicked, String title, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(classname.getResource(fxmlDoc));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = (Stage) buttonClicked.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

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

    public static ObservableList<Division> filterDivisionByCountry(Country country){
        ObservableList<Division> divisionList = DivisionDAO.getDivisionsList();
        ObservableList<Division> filteredList =
                divisionList.stream()
                        .filter(s -> s.getCountryId() == country.getId())
                        .sorted(Comparator.comparing(Division::getDivisionName))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
        return filteredList;
    }

    public static LocalDateTime convertLocalTime(LocalDateTime timeToConvert, ZoneId zoneId){
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime zonedTime = timeToConvert.atZone(localZone);
        return zonedTime.withZoneSameInstant(zoneId).toLocalDateTime();
    }

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

    public static boolean doAppointmentsOverlap(Appointment appointment) throws SQLException {
        LocalTime start = appointment.getStartDateTime().toLocalTime();
        LocalTime end = appointment.getEndDateTime().toLocalTime();
        LocalDate date = appointment.getStartDateTime().toLocalDate();

        List<Appointment> apptList = AppointmentDAO.getAppointmentList();
        List<Appointment> filteredList = apptList.stream()
                .filter(a -> a.getStartDateTime().toLocalDate().equals(date))
                .filter(a -> a.getCustomerID() == appointment.getCustomerID())
                .filter(a -> a.getId() != appointment.getId()).toList();

        for(Appointment a : filteredList){
            LocalTime compareStart = a.getStartDateTime().toLocalTime();
            LocalTime compareEnd = a.getEndDateTime().toLocalTime();
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
}
