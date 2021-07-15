package view_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.NameInterface;
import model.Appointment;
import model.Customer;
import model.DBTables;
import model.User;
import utilities.DBConnection;
import utilities.DBQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the main customers appointments view.
 * This class contains all the methods that are called for the
 * searches, tableviews and buttons
 * @author Greg Newby (959900)
 */
public class MainCustomersApptsController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    private ObservableList<Customer> searchCustomerList = FXCollections.observableArrayList();
    private ObservableList<Appointment> weeksAppts = FXCollections.observableArrayList();
    private ObservableList<Appointment> monthsAppts = FXCollections.observableArrayList();

    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));

    /**This is the First lambda function used to display the user logged in*/
    public NameInterface name = n -> n + " is logged in!";

    /**This is a default constructor. */
    public MainCustomersApptsController(){ }

    /**
     * This constructors takes a dbTables object as a parameter and sets it as this screens dbTables.
     * @param dbTables the dbTables passed to this controller
     */
    public MainCustomersApptsController(DBTables dbTables){
        this.dbTables = dbTables;
    }

    /**
     * This constructor takes a dbTables and user and sets them as this screens dbTables and user.
     * @param dbTables the dbTables passed to this controller
     * @param user the user passed to this controller
     */
    public MainCustomersApptsController(DBTables dbTables, User user){
        this.dbTables = dbTables;
        this.user = user;

        getMonthlyWeeklyAppts();

    }

    /**
     * This method initializes the MainCusomersApptsController.
     * This method uses the first LAMBDA function
     * The method first checks for upcoming appointments and then displays the view filling customers and appointments.
     * The method translates all labels to the appropriate language of the users settings.
     * The method also displays the username of the user logged in at the top of the screen, and initializes
     * the appointment tableview to show all appointments.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //LANGUAGE FORMAT
        try {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                logoutBtn.setText(rb.getString("Log-Out"));
                reportsBtn.setText(rb.getString("Reports"));
                welcomeTxt.setText(rb.getString("Welcome") + ",");
                customersTxt.setText(rb.getString("Customers"));
                apptsTxt.setText(rb.getString("Appointments"));
                searchCustBar.setPromptText(rb.getString("Search"));
                addCustBtn.setText(rb.getString("Add"));
                addApptBtn.setText(rb.getString("Add"));
                modApptBtn.setText(rb.getString("Modify"));
                modCustBtn.setText(rb.getString("Modify"));
                deleteApptBtn.setText(rb.getString("Delete"));
                deleteCustBtn.setText(rb.getString("Delete"));
                allRadio.setText(rb.getString("All"));
                monthlyRadio.setText(rb.getString("Monthly"));
                weeklyRadio.setText(rb.getString("Weekly"));
                custId.setText(rb.getString("custId"));
                custName.setText(rb.getString("CustomerName"));
                custFLD.setText(rb.getString("First-LevelDivision"));
                custAddress.setText(rb.getString("Address"));
                custPostalCode.setText(rb.getString("PostalCode"));
                custPhone.setText(rb.getString("Phone#"));
                apptStartTime.setText(rb.getString("StartTime"));
                apptEndTime.setText(rb.getString("EndTime"));
                apptId.setText(rb.getString("AppointmentID"));
                apptTitle.setText(rb.getString("Title"));
                apptDesc.setText(rb.getString("Description"));
                apptLocation.setText(rb.getString("Location"));
                apptContact.setText(rb.getString("Contact"));
                apptType.setText(rb.getString("Type"));
                apptCustId.setText(rb.getString("custId"));

            }
        }
        catch (MissingResourceException mrb){
            //USE DEFAULT (ENGLISH)
        }

        LoginId.setText(name.getName(user.getUser_Name()));
        allRadio.fire();
        generateCustomerTbl();
        generateAppointmentTbl();
        checkUpcomingAppointments();
    }

    /**
     * This method fills 2 observable list for monthsAppts and weeksAppts.
     */
    private void getMonthlyWeeklyAppts(){
        //Current date and day of the week
        LocalDate todaysDate = LocalDate.now();
        int year = todaysDate.getYear();
        int month = todaysDate.getMonthValue();
        int dayOfWeek = todaysDate.getDayOfWeek().getValue();// monday = 1, tuesday = 2,..., sunday = 7

        //Get dates for start and end of current month and week
        LocalDate beginningOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = LocalDate.of(year,month, todaysDate.lengthOfMonth());
        LocalDate startOfWeek = todaysDate;
        LocalDate endOfWeek = todaysDate;

        while(startOfWeek.getDayOfWeek().getValue() != 1){
            startOfWeek = startOfWeek.minusDays(1);
        }

        while(endOfWeek.getDayOfWeek().getValue() != 7){
            endOfWeek = endOfWeek.plusDays(1);
        }

        //Fill Observable list for current month and current week
        for(Appointment appointment : dbTables.getAllAppointments()){
            if (appointment.getDate().compareTo(beginningOfMonth) >= 0 && appointment.getDate().compareTo(endOfMonth) <= 0){
                monthsAppts.add(appointment);
            }
            if(appointment.getDate().compareTo(startOfWeek) >= 0 && appointment.getDate().compareTo(endOfWeek) <= 0){
                weeksAppts.add(appointment);
            }
        }
    }

    /**
     * This method checks if there is an appointment within the next 15 minutes.
     * An alert is displayed with the appropriate message with the date and time of the appointment.*/
    private void checkUpcomingAppointments(){
        int noApptTrigger = 0;
        for(int i = 0; i < 1; i++ ) {
            for (Appointment appointment : DBTables.getAllAppointments()) {
                if (user.getUser_ID() == appointment.getUser_ID()) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime startTime = appointment.getStart();
                    long minutesBeforeAppointment = ChronoUnit.MINUTES.between(currentDateTime, startTime);
                    long timeGap = (minutesBeforeAppointment + 1); //add 1 to fix the estimation

                    if (timeGap > 0 && timeGap <= 15 ) {
                        noApptTrigger++;
                        if(Locale.getDefault().getLanguage().equals("fr")){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Rendez-vous à venir!");
                            alert.setContentText("Vous avez un rendez-vous (" + appointment.getTitle() + ") en\n" +
                                    " " + timeGap + " minute(s) le " + appointment.getStart() + "!");
                            alert.showAndWait();
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Upcoming Appointment!");
                            alert.setContentText("You have an appointment (" + appointment.getTitle() + ") in\n" +
                                    " " + timeGap + " minute(s) on " + appointment.getStart() + "!");
                            alert.showAndWait();
                        }
                    }
                }
            }
            if(noApptTrigger == 0){
                if (Locale.getDefault().getLanguage().equals("fr")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pas de rendez-vous à venir");
                    alert.setContentText("Il n'y a pas de rendez-vous immédiat.");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Upcoming Appointments");
                    alert.setContentText("There are no immediate appointments.");
                    alert.showAndWait();
                }
            }
        }
    }


    /**
     * This method generates the customer table.
     */
    private void generateCustomerTbl(){
        custId.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        custName.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        custAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        custFLD.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));
        custPostalCode.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
        custPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));

        custTbl.setItems(dbTables.getAllCustomers());
        custTbl.refresh();
    }

    /**
     * This method generates the appointments table.
     */
    private void generateAppointmentTbl(){
        apptStartTime.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptEndTime.setCellValueFactory(new PropertyValueFactory<>("End"));
        apptId.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        apptContact.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptCustId.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));

        if(allRadio.isSelected()) {
            apptTbl.setItems(dbTables.getAllAppointments());
        }
        if(weeklyRadio.isSelected()){
            apptTbl.setItems(weeksAppts);
        }
        if(monthlyRadio.isSelected()){
            apptTbl.setItems(monthsAppts);
        }
        apptTbl.refresh();
    }

    /**
     * This method refills the associated appointments.
     * This method is called whenever an appointment is added, modified or deleted
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
    private Label LoginId;

    @FXML
    private Button addCustBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button modCustBtn;

    @FXML
    private Button deleteCustBtn;

    @FXML
    private TextField searchCustBar;

    @FXML
    private Label welcomeTxt;

    @FXML
    private Label customersTxt;

    @FXML
    private Label apptsTxt;

    @FXML
    private TableView<Customer> custTbl;

    @FXML
    private TableColumn<Customer, Integer> custId;

    @FXML
    private TableColumn<Customer, String> custName;

    @FXML
    private TableColumn<Customer, String> custAddress;

    @FXML
    private TableColumn<Customer, Integer> custFLD;

    @FXML
    private TableColumn<Customer, String> custPostalCode;

    @FXML
    private TableColumn<Customer, String> custPhone;

    @FXML
    private Button addApptBtn;

    @FXML
    private Button modApptBtn;

    @FXML
    private Button deleteApptBtn;

    @FXML
    private TableView<Appointment> apptTbl;

    @FXML
    private TableColumn<Appointment, LocalDateTime> apptStartTime;

    @FXML
    private TableColumn<Appointment, LocalDateTime> apptEndTime;

    @FXML
    private TableColumn<Appointment, Integer> apptId;

    @FXML
    private TableColumn<Appointment, String> apptTitle;

    @FXML
    private TableColumn<Appointment, String> apptDesc;

    @FXML
    private TableColumn<Appointment, String> apptLocation;

    @FXML
    private TableColumn<Appointment, Integer> apptContact;

    @FXML
    private TableColumn<Appointment, String> apptType;

    @FXML
    private TableColumn<Appointment, Integer> apptCustId;

    @FXML
    private RadioButton monthlyRadio;

    @FXML
    private ToggleGroup monthlyWeekly;

    @FXML
    private RadioButton weeklyRadio;

    @FXML
    private RadioButton allRadio;

    @FXML
    private Label monthWeek;

    @FXML
    private Button logoutBtn;

    /**
     * This method takes you back to the login view.
     * This logs the user out and does not pass a user to the controller
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickLogOut(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/loginView.fxml"));
        view_controller.LoginController controller = new view_controller.LoginController(dbTables);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Greg_Newby C195");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method searches for customers that contain the string in the search parts bar.
     * @param event pressing enter
     */
    @FXML
    void onActionCustSearch(ActionEvent event) {

        searchCustomerList.setAll(DBTables.lookUpCustomer(searchCustBar.getText()));

        try {

            if (searchCustomerList.size() == 0) {
                int searchID = Integer.parseInt(searchCustBar.getText());

                for (Customer customer : DBTables.getAllCustomers()) {
                    if (customer.getCustomer_ID() == searchID) {
                        searchCustomerList.add(customer);
                    }
                }
            }


            custTbl.setItems(searchCustomerList);
            custTbl.refresh();
            searchCustBar.clear();
        }
        catch (NumberFormatException nfe) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setContentText("Le produit est introuvable! " + nfe.getMessage());
                error.showAndWait();
            }
            else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("Product cannot be found! " + nfe.getMessage());
                error.showAndWait();
            }
        }
    }

    /**
     * This method deletes an appointment.
     * The method presents a confirmation alert before deleting.
     * The method deletes the appointment and refills associated appointments and
     * then displays a message of the deleted appointment.
     *
     * RUNTIME ERROR: This method must have a selected appointment to delete
     *
     * @param event mouse click
     * @throws SQLException this exception is thrown when reaching out to the database
     */
    @FXML
    void onClickDeleteAppt(MouseEvent event) throws SQLException {
        if(apptTbl.getSelectionModel().getSelectedItem() != null) {
            //Are you sure you want to delete this item?
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete Appointment " + apptTbl.getSelectionModel().getSelectedItem().getAppointment_ID() + " " +
                    "\nfor " + apptTbl.getSelectionModel().getSelectedItem().getType() + " on " + apptTbl.getSelectionModel().getSelectedItem().getStart() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){

                Appointment deletedAppointment = apptTbl.getSelectionModel().getSelectedItem();

                String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
                Connection conn = DBConnection.getConnection();
                DBQuery.setPreparedStatement(conn, deleteStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();

                ps.setInt(1,deletedAppointment.getAppointment_ID());

                ps.execute();

                if(ps.getUpdateCount() > 0) {
                    dbTables.deleteAppointment(deletedAppointment);
                    monthsAppts.remove(deletedAppointment);
                    weeksAppts.remove(deletedAppointment);
                    for (Customer customer : dbTables.getAllCustomers()) {
                        if (customer.getAssociatedAppts().contains(deletedAppointment)) {
                            customer.deleteAssociatedAppt(deletedAppointment);
                        }
                    }
                    generateAppointmentTbl();

                    refillAssociatedAppointments();

                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        System.out.println(ps.getUpdateCount() + " ligne(s) affectée(s)! et dbTables mis à jour");
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Rendez-vous Supprimé!");
                        alert2.setContentText("Le rendez-vous " + deletedAppointment.getAppointment_ID() + " pour\n" +
                                " " + deletedAppointment.getType() + " a été supprimé!");
                        alert2.showAndWait();
                    }
                    else{
                        System.out.println(ps.getUpdateCount() + " row(s) affected! and dbTables updated");
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Appointment Deleted!");
                        alert2.setContentText("Appointment " + deletedAppointment.getAppointment_ID() + " for\n" +
                                " " + deletedAppointment.getType() + " has been deleted!");
                        alert2.showAndWait();
                    }
                }
            }
        }
        else{
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ATTENTION");
                alert.setContentText("Vous devez choisir un rendez-vous à supprimer!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must choose an Appointment to delete!");
                alert.showAndWait();
            }
        }
    }

    /**
     * This method deletes a customer.
     * The customer must not have any associated appointments.
     * The method presents a confirmation alert before deleting.
     * The method deletes the customer and refills associated appointments.
     *
     * LOGICAL ERROR: The customer must not have any associated appointments.
     * RUNTIME ERROR: This method must have a selected customer to delete.
     *
     * @param event mouse click
     * @throws SQLException this exception is thrown when reaching out to the database
     */
    @FXML
    void onClickDeleteCust(MouseEvent event) throws SQLException {

        if(custTbl.getSelectionModel().getSelectedItem() != null){
            if(custTbl.getSelectionModel().getSelectedItem().getAssociatedAppts().isEmpty()){
                //Are you sure you want to delete this item?
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Customer?");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){

                    Customer deletedCustomer = custTbl.getSelectionModel().getSelectedItem();

                    String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ?";
                    Connection conn = DBConnection.getConnection();
                    DBQuery.setPreparedStatement(conn, deleteStatement);
                    PreparedStatement ps = DBQuery.getPreparedStatement();

                    ps.setInt(1,deletedCustomer.getCustomer_ID());

                    ps.execute();

                    if(ps.getUpdateCount() > 0) {
                        dbTables.deleteCustomer(deletedCustomer);
                        generateCustomerTbl();

                        refillAssociatedAppointments();

                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            System.out.println(ps.getUpdateCount() + " ligne(s) affectée(s)! et dbTables mis à jour");
                        }
                        else{
                            System.out.println(ps.getUpdateCount() + " row(s) affected! and dbTables updated");
                        }
                    }
                }
            }
            else{
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ATTENTION");
                    alert.setContentText("Vous ne pouvez pas supprimer un client avec un rendez-vous.\n " +
                            "Vous devez d'abord supprimer le rendez-vous." );
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setContentText("You cannot delete a customer with an appointment.\n " +
                            "You must first delete the appointment.");
                    alert.showAndWait();
                }
            }
        }
        else {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ATTENTION");
                alert.setContentText("Vous devez choisir un client à supprimer!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must choose a Customer to delete!");
                alert.showAndWait();
            }
        }
    }

    /**
     * This method goes to the add appointment view.
     * It passes the current dbTables, user.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickGoToAddAppt(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/addApptView.fxml"));
        view_controller.AddApptController controller = new view_controller.AddApptController(dbTables,user);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Greg_Newby C195");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * This method goes to the add customer view.
     * It passes the current dbTables and user.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickGoToAddCust(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/addCustView.fxml"));
        view_controller.AddCustController controller = new view_controller.AddCustController(dbTables, user);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Greg_Newby C195");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * This method goes to the mod appointment view.
     * It passes the current dbTables, user and selectedAppointmentIndex .
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickGoToModAppt(MouseEvent event) throws IOException {
        try {
            int selectedApptIndex = apptTbl.getSelectionModel().getSelectedIndex();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/modApptView.fxml"));
            view_controller.ModApptController controller = new view_controller.ModApptController(dbTables, user, selectedApptIndex);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Greg_Newby C195");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch(IndexOutOfBoundsException iob){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ATTENTION");
                alert.setContentText("Vous devez choisir un Rendez-vous à modifier!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must choose an Appointment to modify!");
                alert.showAndWait();
            }
        }

    }

    /**
     * This method goes to the mod customers view.
     * It passes the current dbTables, user and the selectedCustomerIndex.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickGoToModCust(MouseEvent event) throws IOException {
        try {
            int selectedCustomerIndex = custTbl.getSelectionModel().getSelectedIndex();


            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/modCustView.fxml"));
            view_controller.ModCustController controller = new view_controller.ModCustController(dbTables, user, selectedCustomerIndex);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Greg_Newby C195");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IndexOutOfBoundsException iob) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ATTENTION");
                alert.setContentText("Vous devez choisir un Client à modifier!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must choose a Customer to modify!");
                alert.showAndWait();
            }
        }

    }

    /**
     * This method goes to the reports view.
     * It passes the current dbTables and user.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onGoToReports(MouseEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/reportsView.fxml"));
        view_controller.ReportsController controller = new view_controller.ReportsController(dbTables, user);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Greg_Newby C195");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method generates the appointments based on the radial button clicked
     * @param event radial button change
     */
    @FXML
    void radioClicked(MouseEvent event){
        generateAppointmentTbl();
    }
}
