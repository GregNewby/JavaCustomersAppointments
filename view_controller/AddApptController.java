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
import main.IDinterface;
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
 * This class is the controller for the add appointments view.
 * This class contains all the methods that are called for the
 * combo boxes and buttons
 * @author Greg Newby (959900)
 */
public class AddApptController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));

    /**this is the lambda function used for generating the next appointment*/
    public IDinterface nextAppointmentID = n -> n + 1;

    /**This is a default constructor. */
    public AddApptController() { }

    /**
     * This is a constructor takes the DBTable and the current user as input
     * and saves them as this screens current DBTable and user.
     * @param dbTables the DBTable that is passed to this screen
     * @param user the user that is passed to this screen
     */
    public AddApptController(DBTables dbTables, User user){
        this.dbTables = dbTables;
        this.user = user;
    }

    /**
     * This method initializes the AddApptController class.
     * The method calls 4 functions to generates the appointment ID, contact
     * combo box, customer combo box, and user combo box. It also translates the
     * screen into the correct language depending on the users language settings.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateAppointmentID();
        generateContactComboBoxes();
        generateCustomerComboBoxes();
        generateUserComboBoxes();

        if (Locale.getDefault().getLanguage().equals("fr")) {
            addAppointmentLbl.setText(rb.getString("AddAppointment"));
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
            addApptSaveBtn.setText(rb.getString("Save"));
            addApptCancelBtn.setText(rb.getString("Cancel"));
        }
    }

    /**
     * This method generates the contact combo box.
     */
    private void generateContactComboBoxes(){
        addApptContact.setItems(dbTables.getAllContacts());
        addApptContact.setVisibleRowCount(7);
    }

    /**
     * This method generates the customer combo box.
     */
    private void generateCustomerComboBoxes(){
        addApptCustomer.setItems(dbTables.getAllCustomers());
        addApptCustomer.setVisibleRowCount(7);
    }

    /**
     * This method generates the user combo box.
     */
    private void generateUserComboBoxes(){
        addApptUser.setItems(dbTables.getAllUsers());
        addApptUser.setVisibleRowCount(7);
    }

    /**
     * This method generates the appointment ID.
     * The method checks for the last appointment ID and uses the LAMBDA
     * function to calculate the next number.
     */
    private void generateAppointmentID(){
        int largestAppointmentID = 0;
        for(Appointment appointment : dbTables.getAllAppointments()) {
            if (appointment.getAppointment_ID() > 0 && appointment.getAppointment_ID() > largestAppointmentID){
                largestAppointmentID = appointment.getAppointment_ID();
            }
        }

        String largestAppointmentIDstring = String.valueOf(nextAppointmentID.calculateNextID(largestAppointmentID));
        addApptId.setText(largestAppointmentIDstring);
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
    private Label addAppointmentLbl;

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
    private Label apptUserLbl;

    @FXML
    private Label dateOfApptLbl;

    @FXML
    private TextField addApptId;

    @FXML
    private TextField addApptTitle;

    @FXML
    private TextField addApptDescription;

    @FXML
    private TextField addApptLocation;

    @FXML
    private DatePicker dateOfAppointment;

    @FXML
    private TextField addApptType;

    @FXML
    private TextField addApptStart;

    @FXML
    private TextField addApptEnd;

    @FXML
    private ComboBox<Contact> addApptContact;

    @FXML
    private ComboBox<Customer> addApptCustomer;

    @FXML
    private ComboBox<User> addApptUser;

    @FXML
    private Button addApptCancelBtn;

    @FXML
    private Button addApptSaveBtn;

    @FXML
    void onContactComboBox(ActionEvent event) {

    }

    @FXML
    void onCustComboBox(ActionEvent event) {

    }

    @FXML
    void onUserComboBox(ActionEvent event) {

    }

    /**
     * This method is called when the save button is clicked.
     * The method reads all information in the text fields, date picker and combo boxes, and then adds the appointment to the database.
     * If the database is updated the appointment is also added to the DBTables object. This method also translates the errors based on the
     * users language settings.
     *
     * LOGICAL ERROR: The appointment start time must be before the end time and in between business hours of 8am-10pm EST.
     * The appointments cannot overlap. If these things are not true an error
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
    void onClickAddApptToList(MouseEvent event) throws IOException, SQLException {
        try {
            String insertStatement = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, " +
                    "Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Connection conn = DBConnection.getConnection();
            DBQuery.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();


            int appointmentID = Integer.parseInt(addApptId.getText());
            String title = addApptTitle.getText();
            String description = addApptDescription.getText();
            String location = addApptLocation.getText();
            String type = addApptType.getText();
            LocalDate date = dateOfAppointment.getValue();
            LocalTime start = LocalTime.parse(addApptStart.getText());
            LocalTime end = LocalTime.parse(addApptEnd.getText());
            LocalDateTime startDateTime = LocalDateTime.of(date, start);
            LocalDateTime endDateTime = LocalDateTime.of(date, end);
            LocalDateTime createDate = LocalDateTime.now();
            String createdBy = user.getUser_Name();
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = user.getUser_Name();
            int customerID = addApptCustomer.getSelectionModel().getSelectedItem().getCustomer_ID();
            int userID = addApptUser.getSelectionModel().getSelectedItem().getUser_ID();
            int contactID = addApptContact.getSelectionModel().getSelectedItem().getContact_ID();


            ps.setInt(1, appointmentID);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, location);
            ps.setString(5, type);
            ps.setTimestamp(6, Timestamp.valueOf(startDateTime));
            ps.setTimestamp(7, Timestamp.valueOf(endDateTime));
            ps.setTimestamp(8, Timestamp.valueOf(createDate));
            ps.setString(9, createdBy);
            ps.setTimestamp(10, Timestamp.valueOf(lastUpdate));
            ps.setString(11, lastUpdatedBy);
            ps.setInt(12, customerID);
            ps.setInt(13, userID);
            ps.setInt(14, contactID);

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
                    for(Appointment appointment : dbTables.getAllAppointments()) {
                        if ((startDateTime.isBefore(appointment.getStart()) && endDateTime.isBefore(appointment.getStart())) || startDateTime.isAfter(appointment.getEnd())) {
                        }
                        else{
                            overlapFlag++;
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

                    if (!title.isEmpty() && !description.isEmpty() && !location.isEmpty() && !type.isEmpty() && overlapFlag == 0) {
                        ps.execute();
                    }

                    if (ps.getUpdateCount() > 0) {
                        dbTables.addAppointment(new Appointment(appointmentID, title, description, location, type, startDateTime,
                                endDateTime, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID));

                        refillAssociatedAppointments();

                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            System.out.println(ps.getUpdateCount() + " ligne(s) affectée(s)! et dbTables mis à jour");
                        } else {
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

                    }
                    else {
                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            System.out.println("aucun changement");
                        } else {
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
        catch(DateTimeParseException dtpe){
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
        catch(NullPointerException npe){
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