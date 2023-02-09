package com.c195project.c195project.controller;

import com.c195project.c195project.DAO.ContactDAO;
import com.c195project.c195project.model.Appointment;
import com.c195project.c195project.model.Contact;
import com.c195project.c195project.model.Customer;
import com.c195project.c195project.DAO.AppointmentDAO;
import com.c195project.c195project.helpers.HelperFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The controller for interaction between Appointment.java, AppointmentDAO.java and Scheduler.fxml.
 *
 * @author Blake Ramsey
 */
public class Scheduler implements Initializable {
    public Button appointmentAddButton;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;
    public TableView appointmentTableView;
    public static Customer customerSelected;
    public Button backButton;
    public Label adjustableLabel;
    public TableColumn startCol;
    public RadioButton weekRadioButton;
    public RadioButton monthRadioButton;
    public Button updateTimeButton;
    public Button countButton;
    public TextField monthOrTypeTextField;
    public Label countLabel;
    public ComboBox contactComboBox;
    public Button showAllButton;
    public ToggleGroup appointmentFilter;
    public RadioButton showAllRadioButton;
    private Month thisMonth;

    /**
     * This method initializes the Scheduler controller after root element has been processed.
     *
     * @param url Resolves relative paths for root object, or null if location is not known.
     * <br>
     * @param resourceBundle Resources used to localize root object, or null if root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(CustomerPage.customerIsNotSelected){
            adjustableLabel.setText("All Customer Appointments");
            try {
                appointmentTableView.setItems(AppointmentDAO.getAppointmentList());
                contactComboBox.setItems(ContactDAO.getContactList());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            adjustableLabel.setText("Appointments for: " + customerSelected.getName());
            try {
                appointmentTableView.setItems(AppointmentDAO.getApptListByCustomerID(customerSelected));
                contactComboBox.setItems(ContactDAO.getContactList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * This method uses the windowLoader method in order to direct the user to the Add Appointment window. This method
     * assigns 'true' to the addButtonClicked boolean.
     *
     * @param actionEvent The actionEvent is clicking on the Add Appointment Button.
     * @throws IOException
     */
    public void onAppointmentAddButtonClick(ActionEvent actionEvent) throws IOException {
        AddUpdateAppointment.addButtonClicked = true;
        HelperFunctions.windowLoader("/com/c195project/c195project/AddUpdateAppointment.fxml",
                LoginPage.class, appointmentAddButton, "Add", 600 , 600);

    }

    /**
     * This method uses the windowLoader method in order to direct the user to the Update Appointment window. This method
     * assigns 'false' to the addButtonClicked boolean. This method assigns an Appointment object to appointmentSelected,
     * and passes this to the next window. If no appointment is selected, error is thrown.
     *
     * @param actionEvent The actionEvent is clicking on the Update Appointment Button.
     * @throws IOException
     */
    public void onAppointmentUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        AddUpdateAppointment.addButtonClicked = false;
        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select appointment to update.");
            }
            AddUpdateAppointment.appointmentSelected = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();
            HelperFunctions.windowLoader("/com/c195project/c195project/AddUpdateAppointment.fxml",
                    LoginPage.class, appointmentAddButton, "Update", 600, 600);
        }
        catch(Exception e){
            HelperFunctions.showError("Appointment", e.getMessage());
        }
    }

    /**
     * This method is to delete selected appointment. Confirmation alert pops up to make sure appointment should be deleted. Information
     * alert populates after appointment is deleted to inform of deletion details.
     *
     * @param actionEvent The actionEvent is the Delete button being clicked.
     * @throws Exception
     */
    public void onAppointmentDeleteButtonClick(ActionEvent actionEvent) throws Exception {
        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("No appointment selected");
            }
            Appointment appointment = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppointment(appointment.getId());

                ObservableList<Appointment> appointmentList = AppointmentDAO.getAppointmentList();
                appointmentTableView.setItems(appointmentList);

                Alert deleteInfo = new Alert(Alert.AlertType.INFORMATION);
                deleteInfo.setTitle("Appointment Deleted");
                deleteInfo.setContentText("Appointment Deleted -\n" + "ID: " + appointment.getId() +
                       "\nType: " + appointment.getType());
                deleteInfo.showAndWait();
            }
        }
        catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    /**
     * This method uses the windowLoader method to take the user back to the Customer page.
     *
     * @param actionEvent The actionEvent is the Back button being clicked.
     * @throws IOException
     */
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                LoginPage.class, backButton, "Customer", 777, 402);
    }

    /**
     * This method filters appointments by week (the next 7 days). For example, if clicked on a Thursday, it would
     * show appointments through the following Thursday. This method accounts for if specific customer appointments are showing, and
     * filters accordingly.
     *
     * @param actionEvent The actionEvent is the weekRadioButton being clicked.
     * @throws SQLException
     */
    public void onWeekClick(ActionEvent actionEvent) throws SQLException {
        contactComboBox.getSelectionModel().clearSelection();
        monthRadioButton.setText("Month");
        weekRadioButton.setText("Week - Next 7 Days");
        LocalDate startDateMinusOne = LocalDate.now().minusDays(1);
        LocalDate endDatePlusOne = LocalDate.now().plusDays(8);

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        if(CustomerPage.customerIsNotSelected){
            appointmentList = AppointmentDAO.getAppointmentList();
        }
        else{
            appointmentList = AppointmentDAO.getApptListByCustomerID(customerSelected);
        }

        Predicate<Appointment> isBeforeEndAndAfterStart = a -> a.getStartDateTime().toLocalDate().isBefore(endDatePlusOne)
                && a.getStartDateTime().toLocalDate().isAfter(startDateMinusOne);

        ObservableList<Appointment> filteredList = appointmentList.stream()
                .filter(isBeforeEndAndAfterStart)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        System.out.println(filteredList);

        appointmentTableView.setItems(filteredList);
    }

    /**
     * This method filters appointments by month, and shows any remaining appointments for that month.
     * This method accounts for if specific customer appointments are showing, and
     * filters accordingly.
     *
     * @param actionEvent The actionEvent is the monthRadioButton being clicked.
     * @throws SQLException
     */
    public void onMonthClick(ActionEvent actionEvent) throws SQLException {
        contactComboBox.getSelectionModel().clearSelection();
        weekRadioButton.setText("Week");
        this.thisMonth = LocalDate.now().getMonth();
        monthRadioButton.setText("Remainder of Month - " + thisMonth.toString());
        LocalDate startDateMinusOne = LocalDate.now().minusDays(1);


        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        if(CustomerPage.customerIsNotSelected){
            appointmentList = AppointmentDAO.getAppointmentList();
        }
        else{
            appointmentList = AppointmentDAO.getApptListByCustomerID(customerSelected);
        }

        Predicate<Appointment> isSameMonth = a -> thisMonth.equals(a.getStartDateTime().getMonth());
        Predicate<Appointment> isOnOrAfterCurrentDate = a -> a.getStartDateTime().toLocalDate().isAfter(startDateMinusOne);

        ObservableList<Appointment> filteredList = appointmentList.stream()
                .filter(isSameMonth)
                .filter(isOnOrAfterCurrentDate)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        appointmentTableView.setItems(filteredList);
    }

    /**
     * This method shows all appointments.
     * This method accounts for if specific customer appointments are showing, and
     * filters accordingly.
     *
     * @param actionEvent The actionEvent is the showAllRadioButton being clicked.
     */
    public void onAllClicked(ActionEvent actionEvent) {
        weekRadioButton.setText("Week");
        monthRadioButton.setText("Month");
        if(CustomerPage.customerIsNotSelected){
            adjustableLabel.setText("All Customer Appointments");
            try {
                appointmentTableView.setItems(AppointmentDAO.getAppointmentList());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            adjustableLabel.setText("Appointments for: " + customerSelected.getName());
            try {
                appointmentTableView.setItems(AppointmentDAO.getApptListByCustomerID(customerSelected));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * This method utilizes the windowLoader method in order to send the user to the UpdateAppointmentTime window. If appointment
     * selection is made then an appropriate error is thrown.
     *
     * @param actionEvent The actionEvent is the updateTimeButton being clicked.
     */
    public void onUpdateTimeClick(ActionEvent actionEvent) {
        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select appointment for time adjustment.");
            }
            UpdateAppointmentTime.appointmentSelected = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();
            HelperFunctions.windowLoader("/com/c195project/c195project/TimeChange.fxml",
                    Scheduler.class, updateTimeButton, "Time Change", 248, 152);
        }catch(Exception e){
            HelperFunctions.showError("Error", e.getMessage());
        }
    }

    /**
     * This method returns a count of total appointments (not filtered by customer) which contain a specified type or fall inside
     * a specified month. The user enters the month (i.e. Jan / 01 / 1) or the type of appointment in order to filter results.
     *
     * @param actionEvent The actionEvent is the countButton being clicked.
     * @throws SQLException
     */
    public void onCountButtonClick(ActionEvent actionEvent) throws SQLException {
        if(monthOrTypeTextField.getText().matches("^(1[0-2]|[0-9]|0[0-9])$")){
            Integer monthNumber = Integer.parseInt(monthOrTypeTextField.getText());
            String monthNumberToString = monthNumber.toString();
            if(monthNumberToString.startsWith("0")){
                String simplifiedNum = monthNumberToString.substring(1);
                monthNumber = Integer.parseInt(simplifiedNum);
            }
            countLabel.setText(String.valueOf(AppointmentDAO.countAppointmentsByMonth(monthNumber)));
            return;
        }
        String userInput = monthOrTypeTextField.getText();
        Month[] allMonths = Month.values();
        for(Month month : allMonths){
            if(month.toString().equals(userInput.toUpperCase())){
                int monthNum = month.getValue();
                countLabel.setText(String.valueOf(AppointmentDAO.countAppointmentsByMonth(monthNum)));
                return;
            }
        }
        int typeCount = AppointmentDAO.countAppointmentsByType(userInput);
        countLabel.setText(String.valueOf(typeCount));
    }

    /**
     * This method uses a combobox in order to filter appointments by Contact. This method additionally filters by contact for each
     * customer selected.
     *
     * @param actionEvent The actionEvent is a contactComboBox selection being made.
     * @throws SQLException
     */
    public void onContactClick(ActionEvent actionEvent) throws SQLException {
        Contact contact = (Contact) contactComboBox.getSelectionModel().getSelectedItem();
        if(contact == null){
            return;
        }
        appointmentFilter.selectToggle(showAllRadioButton);
        weekRadioButton.setText("Week");
        monthRadioButton.setText("Month");
        adjustableLabel.setText("Appointments for " + contact.toString());
        appointmentTableView.setItems(AppointmentDAO.getApptListByContactID(contact));

    }

    /**
     * This method shows all appointments or all appointments for selected customer.
     *
     * @param actionEvent The actionEvent is the showAllButton being clicked.
     * @throws SQLException
     */
    public void onShowAllButtonClick(ActionEvent actionEvent) throws SQLException {
        appointmentFilter.selectToggle(showAllRadioButton);
        weekRadioButton.setText("Week");
        monthRadioButton.setText("Month");
        contactComboBox.getSelectionModel().clearSelection();
        if(CustomerPage.customerIsNotSelected){
            adjustableLabel.setText("All Customer Appointments");
            appointmentTableView.setItems(AppointmentDAO.getAppointmentList());
        }
        else{
            adjustableLabel.setText("Appointments for: " + customerSelected.getName());
            appointmentTableView.setItems(AppointmentDAO.getApptListByCustomerID(customerSelected));
        }
    }
}
