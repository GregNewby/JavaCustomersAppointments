package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;
import utilities.DBConnection;
import utilities.DBQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This class is the controller for the mod appointments view.
 * This class contains all the methods that are called for the
 * combo boxes and buttons
 * @author Greg Newby (959900)
 */
public class ModApptController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    private Appointment selectedAppointment;
    private int selectedApptIndex;
    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));

    /**This is a default constuctor. */
    public ModApptController() { }

    /**
     * This is a constructor takes the DBTable, selectedApptIndex and the current user as input
     * and saves them as this screens current DBTable, selectedApptIndex and user.
     * @param dbTables the DBTable that is passed to this screen
     * @param user the user that is passed to this screen
     * @param selectedApptIndex the selected appointment index that is passed to this screen
     */
    public ModApptController(DBTables dbTables,User user,int selectedApptIndex){
        this.dbTables = dbTables;
        this.user = user;
        this.selectedApptIndex = selectedApptIndex;
        this.selectedAppointment = dbTables.getAllAppointments().get(selectedApptIndex);
    }

    /**
     *This method initializes the ModApptController.
     * 3 methods are called to generate the customer, contact, and user combo boxes. The text fields are filled with the
     * selected appointments data and the labels are translated depending on the users language settings.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateCustomerComboBoxes();
        generateContactComboBoxes();
        generateUserComboBoxes();

        if (Locale.getDefault().getLanguage().equals("fr")) {
            modApptLbl.setText(rb.getString("ModifyAppointment"));
            apptIDLbl.setText(rb.getString("AppointmentID"));
            apptTitleLbl.setText(rb.getString("Title"));
            apptDescriptionLbl.setText(rb.getString("Description"));
            apptLocationLbl.setText(rb.getString("Location"));
            apptTypeLbl.setText(rb.getString("Type"));
            apptStartLbl.setText(rb.getString("StartTime"));
            apptEndLbl.setText(rb.getString("EndTime"));
            apptContactLbl.setText(rb.getString("contactID"));
            apptCustLbl.setText(rb.getString("Customer"));
            apptUserLbl.setText(rb.getString("userID"));
            dateOfApptLbl.setText(rb.getString("DateOfAppointment"));
            modApptSaveBtn.setText(rb.getString("Save"));
            modApptCancelBtn.setText(rb.getString("Cancel"));

        }

        modApptId.setText(String.valueOf(selectedAppointment.getAppointment_ID()));
        modApptTitle.setText(selectedAppointment.getTitle());
        modApptDescription.setText(selectedAppointment.getDescription());
        modApptLocation.setText(selectedAppointment.getLocation());
        modApptType.setText(selectedAppointment.getType());
        modApptStart.setText(selectedAppointment.getStart().toLocalTime().toString());
        modApptEnd.setText(selectedAppointment.getEnd().toLocalTime().toString());
        dateOfAppointment.setValue(selectedAppointment.getStart().toLocalDate());

    }

    /**
     * This method generates the contact combo box.
     */
    private void generateContactComboBoxes(){
        modApptContact.setItems(dbTables.getAllContacts());
        modApptContact.setVisibleRowCount(7);

        for(Contact contact : dbTables.getAllContacts()){
            if(selectedAppointment.getContact_ID() == contact.getContact_ID()){
                modApptContact.setValue(contact);
            }
        }
    }

    /**
     * This method generates the customer combo box.
     */
    private void generateCustomerComboBoxes(){
        modApptCustomer.setItems(dbTables.getAllCustomers());
        modApptCustomer.setVisibleRowCount(7);

        for(Customer customer : dbTables.getAllCustomers()){
            if(selectedAppointment.getCustomer_ID()== customer.getCustomer_ID()){
                modApptCustomer.setValue(customer);
            }
        }
    }

    /**
     * This method generates the user combo box.
     */
    private void generateUserComboBoxes(){
        modApptUser.setItems(dbTables.getAllUsers());
        modApptUser.setVisibleRowCount(7);

        for(User user : dbTables.getAllUsers()){
            if(selectedAppointment.getUser_ID() == user.getUser_ID()){
                modApptUser.setValue(user);
            }
        }
    }

    /**
     * This method refills the associated appointments.
     * This method is called when an appointment is added or deleted.
     */
    private void refillAssociatedAppointments() {
        //first clear all associated appts
        for(Customer customer : dbTables.getAllCustomers()){
            customer.getAssociatedAppts().clear();
        }
        //then reload the associate appts
        for (Appointment appointment : dbTables.getAllAppointments()) {
            for (Customer customer : dbTables.getAllCustomers()) {
                if (appointment.getCustomer_ID() == customer.getCustomer_ID()) {
                    customer.addAssociatedAppt(appointment);
                }
            }
        }
    }

    @FXML
    private Label dateOfApptLbl;

    @FXML
    private Label apptUserLbl;

    @FXML
    private Label modApptLbl;

    @FXML
    private Label apptIDLbl;

    @FXML
    private Label apptTitleLbl;

    @FXML
    private Label apptDescriptionLbl;

    @FXML
    private Label apptLocationLbl;

    @FXML
    private Label apptTypeLbl;

    @FXML
    private Label apptStartLbl;

    @FXML
    private Label apptEndLbl;

    @FXML
    private Label apptContactLbl;

    @FXML
    private Label apptCustLbl;

    @FXML
    private TextField modApptId;

    @FXML
    private TextField modApptTitle;

    @FXML
    private TextField modApptDescription;

    @FXML
    private TextField modApptLocation;

    @FXML
    private TextField modApptType;

    @FXML
    private TextField modApptStart;

    @FXML
    private TextField modApptEnd;

    @FXML
    private DatePicker dateOfAppointment;

    @FXML
    private ComboBox<Contact> modApptContact;

    @FXML
    private ComboBox<Customer> modApptCustomer;

    @FXML
    private ComboBox<User> modApptUser;

    @FXML
    private Button modApptCancelBtn;

    @FXML
    private Button modApptSaveBtn;

    @FXML
    void onContactComboBox(ActionEvent event) { }

    @FXML
    void onCustComboBox(ActionEvent event) { }

    @FXML
    void onUserComboBox(ActionEvent event) { }


    /**
     * This method is called when the save button is clicked.
     * The method reads all information in the text fields, date picker and combo boxes, and then replaces the appointment in the database.
     * If the database is updated the appointment is also replaced in the DBTables object. This method also translates the errors based on the
     * users language settings.
     *
     * LOGICAL ERROR: The appointment start time must be before the end time and in between business hours of 8am-10pm EST.
     * The appointments must not overlap. If these things are not true an error
     * messages is displayed.
     *
     * RUNTIME ERROR: Errors are displayed if all data is not filled. DateTimeParseExceptions and NullPointerExceptions are handled by
     * try catch blocks for the date picker and combo boxes.
     *
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     * @throws SQLException this exception is thrown when reaching out to the database
     */
    @FXML
    void onClickModAppt(MouseEvent event) throws IOException, SQLException {
        try {
            String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?," +
                    "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            Connection conn = DBConnection.getConnection();
            DBQuery.setPreparedStatement(conn, updateStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            int appointmentID = Integer.parseInt(modApptId.getText());
            String title = modApptTitle.getText();
            String description = modApptDescription.getText();
            String location = modApptLocation.getText();
            String type = modApptType.getText();
            LocalDate date = dateOfAppointment.getValue();
            LocalTime start = LocalTime.parse(modApptStart.getText());
            LocalTime end = LocalTime.parse(modApptEnd.getText());
            LocalDateTime startDateTime = LocalDateTime.of(date, start);
            LocalDateTime endDateTime = LocalDateTime.of(date, end);
            LocalDateTime createDate = LocalDateTime.now();
            String createdBy = user.getUser_Name();
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = user.getUser_Name();
            int customerID = modApptCustomer.getSelectionModel().getSelectedItem().getCustomer_ID();
            int userID = modApptUser.getSelectionModel().getSelectedItem().getUser_ID();
            int contactID = modApptContact.getSelectionModel().getSelectedItem().getContact_ID();


            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(startDateTime));
            ps.setTimestamp(6, Timestamp.valueOf(endDateTime));
            ps.setTimestamp(7, Timestamp.valueOf(lastUpdate));
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, customerID);
            ps.setInt(10, userID);
            ps.setInt(11, contactID);
            ps.setInt(12, appointmentID);

            //Hours of operation conversions
            ZonedDateTime openTimeEST = ZonedDateTime.of(date, LocalTime.of(8, 00), ZoneId.of("America/New_York"));
            ZonedDateTime closeTimeEST = ZonedDateTime.of(date, LocalTime.of(22, 00), ZoneId.of("America/New_York"));
            ZonedDateTime openTimeLocal = openTimeEST.withZoneSameInstant(ZoneId.of(TimeZone.getDefault().getID()));
            ZonedDateTime closeTimeLocal = closeTimeEST.withZoneSameInstant(ZoneId.of(TimeZone.getDefault().getID()));
            LocalTime openTime = openTimeLocal.toLocalTime();
            LocalTime closeTime = closeTimeLocal.toLocalTime();

            if (Locale.getDefault().getLanguage().equals("fr")){
                //ERRORS IN ENGLISH
                if(title.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser le titre vide!");
                    alert.showAndWait();
                }
                if(description.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser la description vide!");
                    alert.showAndWait();
                }
                if(location.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser l'emplacement vide!");
                    alert.showAndWait();
                }
                if(type.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser le type vide!");
                    alert.showAndWait();
                }
            }
            else{
                //ERRORS IN ENGLISH
                if(title.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave title empty!");
                    alert.showAndWait();
                }
                if(description.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave description empty!");
                    alert.showAndWait();
                }
                if(location.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave location empty!");
                    alert.showAndWait();
                }
                if(type.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave type empty!");
                    alert.showAndWait();
                }
            }


            if (start.compareTo(end)<= 0) {
                if ((start.compareTo(openTime) >= 0 && start.compareTo(closeTime) <= 0) && (end.compareTo(openTime) >= 0 && end.compareTo(closeTime) <= 0)) {
                    int overlapFlag = 0;
                    int thisApptFlag = 0;
                    for(Appointment appointment : dbTables.getAllAppointments()) {
                        if ((startDateTime.isBefore(appointment.getStart()) && endDateTime.isBefore(appointment.getStart())) || startDateTime.isAfter(appointment.getEnd())) {
                        }
                        else{
                            //IF THE APPOINTMENT IS OVERLAPPING AND IS NOT THE SAME AS THE SELECTED APPOINTMENT, FLAG THE APPOINTMENT AS OVERLAPPING
                            if(appointment.getAppointment_ID() != selectedAppointment.getAppointment_ID()){
                                overlapFlag++;
                            }
                            //IF IT IS THE SAME APPOINTMENT DO NOTHING
                            else {
                            }
                        }
                    }

                    if(overlapFlag != 0){
                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERREUR");
                            alert.setContentText("Les rendez-vous ne peuvent pas se chevaucher!");
                            alert.showAndWait();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Appointments cannot overlap!");
                            alert.showAndWait();
                        }
                    }

                    if(!title.isEmpty() && !description.isEmpty() && !location.isEmpty() && !type.isEmpty() && overlapFlag == 0) {
                        ps.execute();
                    }

                    if (ps.getUpdateCount() > 0) {
                        dbTables.updateAppointment(selectedApptIndex, new Appointment(appointmentID, title, description, location, type, startDateTime,
                                endDateTime, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID));

                        refillAssociatedAppointments();

                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            System.out.println(ps.getUpdateCount() + " ligne(s) affectée(s)! et dbTables mis à jour");
                        }
                        else{
                            System.out.println(ps.getUpdateCount() + " row(s) affected! and dbTables updated");
                        }

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/mainCustomersAppts.fxml"));
                        view_controller.MainCustomersApptsController controller = new view_controller.MainCustomersApptsController(dbTables, user);
                        loader.setController(controller);
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        stage.setTitle("Greg_Newby C195");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    } else {
                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            System.out.println("aucun changement");
                        }
                        else {
                            System.out.println("No change");
                        }
                    }

                }
                else{
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERREUR");
                        alert.setContentText("Le rendez-vous doit être dans les heures ouvrables de\n " +
                                "8am-10pm EST (" + openTime + "-" + closeTime + " " + String.valueOf(ZoneId.of(TimeZone.getDefault().getID())) + ")!");
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Appointment must be within business hours of\n " +
                                "8am-10pm EST (" + openTime + "-" + closeTime + " " + String.valueOf(ZoneId.of(TimeZone.getDefault().getID())) + ")!");
                        alert.showAndWait();
                    }
                }
            }
            else{
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("L'heure de début doit être antérieure à l'heure de fin!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Start time must be before end time!");
                    alert.showAndWait();
                }
            }
        }
        catch (DateTimeParseException dtpe){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Vous devez sélectionner une date pour le rendez-vous\n plus une heure de début ET de fin au format HH:mm!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must select a date for the appointment\n plus a start AND end time in HH:mm format!");
                alert.showAndWait();
            }
        }
        catch (NullPointerException npe){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Vous devez sélectionner un contact, un client et un utilisateur!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must select a Contact, Customer, and User!");
                alert.showAndWait();
            }
        }

    }

    /**
     * This method is called when the cancel button is clicked.
     * The method returns to the mainCustomersAppts view passing this screens dbTables and user.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickGoToMainView(MouseEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/mainCustomersAppts.fxml"));
        view_controller.MainCustomersApptsController controller = new view_controller.MainCustomersApptsController(dbTables,user);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Greg_Newby C195");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
