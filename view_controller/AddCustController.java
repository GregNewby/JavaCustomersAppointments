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
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is the controller for the add customer view.
 * This class contains all the methods that are called for the
 * combo boxes and buttons
 * @author Greg Newby (959900)
 */
public class AddCustController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    private ObservableList<FirstLevelDivision> divisionsInSelectedCountry = FXCollections.observableArrayList();
    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));

    /**this is the lambda function used for generating the next customer*/
    public IDinterface nextCustomerID = n -> n + 1;

    /**This is a default constructor. */
    public AddCustController(){ }

    /**
     * This constructor takes a DBTable and user as input
     * and saves them as this screens current DBTable and user.
     * @param dbTables the DBTable that is passed to this screen
     * @param user the user that is passed to this screen
     */
    public AddCustController(DBTables dbTables,User user){

        this.dbTables = dbTables;
        this.user = user;

    }

    /**
     * This method initializes the AddCustomerController class.
     * The method calls 2 functions to generate the customer ID, and generate the
     * the country combo box. This function also translates the labels based upon the language
     * settings of the users computer.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateCustomerID();
        generateCountryComboBoxes();

        if (Locale.getDefault().getLanguage().equals("fr")) {
            addCustLbl.setText(rb.getString("addCustomer"));
            custIDLbl.setText(rb.getString("custId"));
            custNameLbl.setText(rb.getString("CustomerName"));
            custAddrLbl.setText(rb.getString("Address"));
            custCountryLbl.setText(rb.getString("Country"));
            custStateLbl.setText(rb.getString("First-LevelDivision"));
            custPostalLbl.setText(rb.getString("PostalCode"));
            custPhoneLbl.setText(rb.getString("Phone#"));
            custCreatorLbl.setText(rb.getString("CreatedBy"));
            addCustSaveBtn.setText(rb.getString("Save"));
            addCustCancelBtn.setText(rb.getString("Cancel"));
        }
    }

    /**
     * This method generates the customer ID.
     * The method checks for the last customer ID and uses the LAMBDA
     * function to calculate the next number.
     */
    private void generateCustomerID(){
        int largestCustomerID = 0;
        for(Customer customer : dbTables.getAllCustomers()) {
            if (customer.getCustomer_ID() > 0 && customer.getCustomer_ID() >largestCustomerID){
                largestCustomerID = customer.getCustomer_ID();
            }
        }

        String largestCustomerIDstring = String.valueOf(nextCustomerID.calculateNextID(largestCustomerID));
        addCustId.setText(largestCustomerIDstring);
    }

    /**
     * This method generates the country combo box.
     */
    private void generateCountryComboBoxes(){
        addCustCountry.setItems(dbTables.getAllCountries());
        addCustCountry.setVisibleRowCount(7);
    }

    /**
     * This method generates the First-Level Division combo box.
     * The divisionInSelectedCountry observable list is first cleared.
     * Once the country is selected the divisions that match the country are added to divisionsInSelectedCountry
     * observable list and then displayed.
     */
    private void generateDivisionComboBox(){
        divisionsInSelectedCountry.clear();

        for(FirstLevelDivision division : dbTables.getAllFirstLevelDivisions()){
            if(division.getCountry_ID() == addCustCountry.getSelectionModel().getSelectedItem().getCounty_ID()){
                divisionsInSelectedCountry.add(division);
            }
        }

        addCustState.setItems(divisionsInSelectedCountry);
        addCustState.setVisibleRowCount(7);
    }

    /**
     * This method refills the associated Appointments.
     * This method is called when a new customer is created.
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
    private Label addCustLbl;

    @FXML
    private Label custIDLbl;

    @FXML
    private Label custNameLbl;

    @FXML
    private Label custAddrLbl;

    @FXML
    private Label custCountryLbl;

    @FXML
    private Label custStateLbl;

    @FXML
    private Label custPostalLbl;

    @FXML
    private Label custPhoneLbl;

    @FXML
    private Label custCreatorLbl;

    @FXML
    private TextField addCustId;

    @FXML
    private TextField addCustName;

    @FXML
    private TextField addCustAddress;

    @FXML
    private ComboBox<Country> addCustCountry;

    @FXML
    private ComboBox<FirstLevelDivision> addCustState;

    @FXML
    private TextField addCustPostal;

    @FXML
    private TextField addCustPhone;

    @FXML
    private TextField addCustCreatedBy;

    @FXML
    private Button addCustCancelBtn;

    @FXML
    private Button addCustSaveBtn;

    /**
     * This method calls the function to generate First-Level Divisions when the country is selected.
     * @param event when the country is selected
     */
    @FXML
    void onCountryComboBox(ActionEvent event) {

        generateDivisionComboBox();
    }

    @FXML
    void onFLDComboBox(ActionEvent event) {
    }

    /**
     * This method is called when the save button is clicked.
     * The method reads all information in the text fields and combo boxes, and then adds the customer to the database.
     * If the database is updated the customer is also added to the DBTables object. This method also translates the errors based on the
     * users language settings.
     *
     * RUNTIME ERROR: Errors are displayed if all data is not filled. NullPointerExceptions are handled by
     * try catch blocks for the combo boxes.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     * @throws SQLException this exception is thrown when reaching out to the database
     */
    @FXML
    void onClickAddCustToList(MouseEvent event) throws IOException, SQLException {
        try {

            String insertStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) Values(?,?,?,?,?,?,?,?,?,?)";


            Connection conn = DBConnection.getConnection();
            DBQuery.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            int customerID = Integer.parseInt(addCustId.getText());
            String customerName = addCustName.getText();
            String address = addCustAddress.getText();
            String postalCode = addCustPostal.getText();
            String phone = addCustPhone.getText();
            LocalDateTime createDate = LocalDateTime.now();
            String createdBy = addCustCreatedBy.getText();
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = user.getUser_Name();
            int divisionID = addCustState.getSelectionModel().getSelectedItem().getDivision_ID();


            ps.setInt(1, customerID);
            ps.setString(2, customerName);
            ps.setString(3, address);
            ps.setString(4, postalCode);
            ps.setString(5, phone);
            ps.setTimestamp(6, Timestamp.valueOf(createDate));
            ps.setString(7, createdBy);
            ps.setTimestamp(8, Timestamp.valueOf(lastUpdate));
            ps.setString(9, lastUpdatedBy);
            ps.setInt(10, divisionID);

            if(customerName.isEmpty()){
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser le nom du client vide!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave customer name empty!");
                    alert.showAndWait();
                }
            }
            if(address.isEmpty()){
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser l'adresse vide!");
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave address empty!");
                    alert.showAndWait();
                }
            }
            if(postalCode.isEmpty()){
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser le code postal vide!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave postal code empty!");
                    alert.showAndWait();
                }
            }
            if(phone.isEmpty()){
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser le téléphone vide!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave phone empty!");
                    alert.showAndWait();
                }
            }
            if(createdBy.isEmpty()){
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Vous ne pouvez pas laisser 'créé par' vide!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("You cannot leave 'created by' empty!");
                    alert.showAndWait();
                }
            }

            if(!customerName.isEmpty() && !address.isEmpty() && !postalCode.isEmpty() && !phone.isEmpty() && !createdBy.isEmpty()) {
                ps.execute();
            }

            if (ps.getUpdateCount() > 0) {
                dbTables.addCustomer(new Customer(customerID, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID));

                refillAssociatedAppointments();

                if (Locale.getDefault().getLanguage().equals("fr")) {
                    System.out.println(ps.getUpdateCount() + " ligne(s) affectée(s)! et dbTables mis à jour");
                }
                else{
                    System.out.println(ps.getUpdateCount() + " row(s) affected! and dbTables updated");
                }

                //Go Back to Main screen after update
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
        catch (NullPointerException npe){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Vous devez sélectionner un pays et un état/une région !");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You must select a Country and State/Region!");
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
