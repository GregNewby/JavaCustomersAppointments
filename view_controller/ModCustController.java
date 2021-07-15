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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the mod customer view.
 * This class contains all the methods that are called for the
 * combo boxes and buttons
 * @author Greg Newby (959900)
 */
public class ModCustController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    private Customer selectedCustomer;
    private int selectedCustomerIndex;
    private ObservableList<FirstLevelDivision> divisionsInSelectedCountry = FXCollections.observableArrayList();

    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));


    /**This is a default constructor. */
    public ModCustController() { }

    /**
     * This is a constructor takes the DBTable, selectedCustomerIndex and the current user as input
     * and saves them as this screens current DBTable, selectedCustomerIndex and user.
     * @param dbTables the DBTable that is passed to this screen
     * @param user the user that is passed to this screen
     * @param selectedCustomerIndex the selected customer index that is passed to this screen
     */
    public ModCustController(DBTables dbTables, User user, int selectedCustomerIndex){
        this.dbTables = dbTables;
        this.user = user;
        this.selectedCustomerIndex = selectedCustomerIndex;
        this.selectedCustomer = dbTables.getAllCustomers().get(selectedCustomerIndex);
    }

    /**
     * This method initializes the ModCustomerController class.
     * The method calls 2 functions to generate the AsocAppointmentTbl, and generate the
     * the country combo box. The function sets the values for the country and First-Level Division combo boxes.
     * This function also translates the labels based upon the language
     * settings of the users computer.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        generateCountryComboBoxes();
        generateAsocAppointmentsTbl();
        modCustId.setText(String.valueOf(selectedCustomer.getCustomer_ID()));
        modCustName.setText(selectedCustomer.getCustomer_Name());
        modCustAddress.setText(selectedCustomer.getAddress());
        modCustPostal.setText(selectedCustomer.getPostal_Code());
        modCustPhone.setText(selectedCustomer.getPhone());
        modCustCreatedBy.setText(selectedCustomer.getCreated_By());

        for(FirstLevelDivision division : dbTables.getAllFirstLevelDivisions()){
            if(division.getDivision_ID() == selectedCustomer.getDivision_ID()){
                modCustState.setValue(division);
            for(Country country : dbTables.getAllCountries()){
                if(division.getCountry_ID() == country.getCounty_ID()){
                    modCustCountry.setValue(country);
                }
            }
            }
        }

        if (Locale.getDefault().getLanguage().equals("fr")) {
            modCustLbl.setText(rb.getString("addCustomer"));
            custIDLbl.setText(rb.getString("custId"));
            custNameLbl.setText(rb.getString("CustomerName"));
            custAddrLbl.setText(rb.getString("Address"));
            custCountryLbl.setText(rb.getString("Country"));
            custStateLbl.setText(rb.getString("First-LevelDivision"));
            custPostalLbl.setText(rb.getString("PostalCode"));
            custPhoneLbl.setText(rb.getString("Phone#"));
            custCreatorLbl.setText(rb.getString("CreatedBy"));
            modCustSaveBtn.setText(rb.getString("Save"));
            modCustCancelBtn.setText(rb.getString("Cancel"));
            custApptsLbl.setText(rb.getString("CustomerAppointments"));
            deleteApptBtn.setText(rb.getString("Delete") + " " + rb.getString("appointment"));
        }

    }

    /**
     * This method generates the country combo box.
     */
    private void generateCountryComboBoxes(){
        modCustCountry.setItems(dbTables.getAllCountries());
        modCustCountry.setVisibleRowCount(7);
    }

    /**
     * This method generates the Divisions combo box.
     * The method goes through all divisions that match the selected country code and add them to the observable list
     * divisionsInSelectedCountry. Then display divisionsInSelectedCountry in combo box.
     */
    private void generateDivisionComboBox(){
        divisionsInSelectedCountry.clear();

        for(FirstLevelDivision division : dbTables.getAllFirstLevelDivisions()){
            if(division.getCountry_ID() == modCustCountry.getSelectionModel().getSelectedItem().getCounty_ID()){
                divisionsInSelectedCountry.add(division);
            }
        }

        modCustState.setItems(divisionsInSelectedCountry);
        modCustState.setVisibleRowCount(7);
    }

    /**
     * This method generates the associated appointments table.
     */
    private void generateAsocAppointmentsTbl(){

        apptStartTime.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptId.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));

        asocApptTbl.setItems(selectedCustomer.getAssociatedAppts());
        asocApptTbl.refresh();
    }

    /**
     * This method refills the associated Appointments.
     * This method is called when the customer is replaced and when associated appointments are modified.
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
    private Label modCustLbl;

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
    private Label custApptsLbl;

    @FXML
    private TextField modCustId;

    @FXML
    private TextField modCustName;

    @FXML
    private TextField modCustAddress;

    @FXML
    private ComboBox<Country> modCustCountry;

    @FXML
    private ComboBox<FirstLevelDivision> modCustState;

    @FXML
    private TextField modCustPostal;

    @FXML
    private TextField modCustPhone;

    @FXML
    private TextField modCustCreatedBy;

    @FXML
    private Button modCustCancelBtn;

    @FXML
    private Button modCustSaveBtn;

    @FXML
    private Button deleteApptBtn;

    @FXML
    private TableView<Appointment> asocApptTbl;

    @FXML
    private TableColumn<Appointment, LocalDateTime> apptStartTime;

    @FXML
    private TableColumn<Appointment, Integer> apptId;

    @FXML
    private TableColumn<Appointment, String> apptTitle;

    /**
     * This method generates the division combo box when a country is selected.
     * @param event selecting a country
     */
    @FXML
    void onCountryComboBox(ActionEvent event) {
        modCustState.setValue(null);
        generateDivisionComboBox();
    }

    @FXML
    void onFLDComboBox(ActionEvent event) {

    }

    /**
     * This method deletes the selected appointment.
     * If the appointment is deleted from the database it is then removed from dbTables.
     * Associated appointment is also regenerated.
     *
     * RUNTIME ERROR: the user must select an appointment to be deleted or else an error is thrown in the appropriate language.
     *
     * @param event mouse click
     * @throws SQLException this exception is thrown when reaching out to the database
     */
    @FXML
    void onClickRemoveAsocAppt(MouseEvent event) throws SQLException {
        if(asocApptTbl.getSelectionModel().getSelectedItem() != null) {
            //Are you sure you want to delete this item?
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete Appointment " + asocApptTbl.getSelectionModel().getSelectedItem().getAppointment_ID() + " " +
                    "\nfor " + asocApptTbl.getSelectionModel().getSelectedItem().getType() + " on " + asocApptTbl.getSelectionModel().getSelectedItem().getStart() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){

                Appointment deletedAppointment = asocApptTbl.getSelectionModel().getSelectedItem();

                String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
                Connection conn = DBConnection.getConnection();
                DBQuery.setPreparedStatement(conn, deleteStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();

                ps.setInt(1,deletedAppointment.getAppointment_ID());

                ps.execute();

                if(ps.getUpdateCount() > 0) {
                    selectedCustomer.deleteAssociatedAppt(deletedAppointment);
                    dbTables.deleteAppointment(deletedAppointment);
                    generateAsocAppointmentsTbl();
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
     * This method is called when the save button is clicked.
     * The method reads all information in the text fields and combo boxes, and then replaces the customer in the database.
     * If the database is updated the customer is also replaced in the DBTables object. This method also translates the errors based on the
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

            String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";


            Connection conn = DBConnection.getConnection();
            DBQuery.setPreparedStatement(conn, updateStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            int customerID = Integer.parseInt(modCustId.getText());
            String customerName = modCustName.getText();
            String address = modCustAddress.getText();
            String postalCode = modCustPostal.getText();
            String phone = modCustPhone.getText();
            LocalDateTime createDate = selectedCustomer.getCreate_Date();
            String createdBy = modCustCreatedBy.getText();
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = user.getUser_Name();
            int divisionID = modCustState.getSelectionModel().getSelectedItem().getDivision_ID();


            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setString(5, createdBy);
            ps.setTimestamp(6, Timestamp.valueOf(lastUpdate));
            ps.setString(7, lastUpdatedBy);
            ps.setInt(8, divisionID);
            ps.setInt(9, customerID);

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
                dbTables.updateCustomer(selectedCustomerIndex, new Customer(customerID, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID));

                if (Locale.getDefault().getLanguage().equals("fr")) {
                    System.out.println(ps.getUpdateCount() + " ligne(s) affectée(s)! et dbTables mis à jour");
                }
                else{
                    System.out.println(ps.getUpdateCount() + " row(s) affected! and dbTables updated");
                }
                refillAssociatedAppointments();

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
