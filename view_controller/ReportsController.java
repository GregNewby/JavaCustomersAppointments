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
import model.Appointment;
import model.Contact;
import model.DBTables;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class is the controller for the reports view.
 * This class contains all the methods that are called for the
 * combo boxes tableviews and buttons
 * @author Greg Newby (959900)
 */
public class ReportsController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    int numberOfSelectedType;
    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));
    private ObservableList<Month> monthObservableList = FXCollections.observableArrayList();
    private ObservableList<Appointment> monthsAppts = FXCollections.observableArrayList();
    private ObservableList<Appointment> selectedContactAppts = FXCollections.observableArrayList();
    private ObservableList<String> typeOfApptsList = FXCollections.observableArrayList();


    /**This is a default constructor. */
    public ReportsController(){}

    /**
     * This constructor take a dbTables and a user and saves them for this screens
     * dbTables and user.
     * @param dbTables the dbTables passed to this screen
     * @param user the user passed to this screen
     */
    public ReportsController(DBTables dbTables, User user) {
        this.dbTables = dbTables;
        this.user = user;
    }

    /**
     * This method initializes the ReportsController class.
     * This method uses a try catch block to handle if the language of the user is in french or english.
     * The method then calls 3 functions to generate the month, type and contact combo box.
     * Then it sets the visible box for total customers.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //LANGUAGE FORMAT
        try {
            if (Locale.getDefault().getLanguage().equals("fr")) {


                apptStartTime.setText(rb.getString("StartTime"));
                apptEndTime.setText(rb.getString("EndTime"));
                apptId.setText(rb.getString("AppointmentID"));
                apptTitle.setText(rb.getString("Title"));
                apptDescription.setText(rb.getString("Description"));
                apptType.setText(rb.getString("Type"));
                custID.setText(rb.getString("custId"));
                monthComboBox.setPromptText(rb.getString("Month"));
                apptTypeComboBox.setPromptText(rb.getString("Type"));
                contactComboBox.setPromptText(rb.getString("SelectAContact"));
                mainBtn.setText(rb.getString("Back"));
                reportsLbl.setText(rb.getString("Reports"));
                contactLbl.setText(rb.getString("contactLbl") + ":");
                customerLbl.setText(rb.getString("customerLbl") + ":");
                numberOfApptsTxt.setText(rb.getString("numberOfApptsTxt"));
                helpNote.setText("(" + rb.getString("helpNote") + ")....................");

            }
        }
        catch (MissingResourceException mrb){
            //USE DEFAULT (ENGLISH)
        }

        generateContactComboBoxes();
        generateTypeComboBoxes();
        generateMonthComboBoxes();
        numberOfCustomers.setText(String.valueOf(dbTables.getAllCustomers().size()));

    }

    /**
     * This method generates the type combo box.
     * The method goes through all appointment types and adds it to the observable list typeOfApptsList if it is not already
     * in the list.
     */
    private void generateTypeComboBoxes(){
        for(Appointment appointment : dbTables.getAllAppointments()){
            if(!typeOfApptsList.contains(appointment.getType())){
                typeOfApptsList.add(appointment.getType());
            }
        }

        apptTypeComboBox.setItems(typeOfApptsList);
        apptTypeComboBox.setVisibleRowCount(7);


    }

    /**
     * This method generates the Month combo box.
     * A for Loop is used to add all 12 months to the monthObservableList
     */
    private void generateMonthComboBoxes(){

        for(int i = 1; i < 13 ; i++) {
            monthObservableList.add(Month.of(i));
        }
        monthComboBox.setItems(monthObservableList);
    }

    /**
     * This method generates the contact combo box.
     */
    private void generateContactComboBoxes(){
        contactComboBox.setItems(dbTables.getAllContacts());
        contactComboBox.setVisibleRowCount(7);
    }

    /**
     * This method generates the schedule table for the selected contact.
     */
    public void generateScheduleTable(){
        apptStartTime.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptEndTime.setCellValueFactory(new PropertyValueFactory<>("End"));
        apptId.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        custID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));

        contactScheduleTbl.setItems(selectedContactAppts);
    }

    /**
     * This method adds all appointments of a certain month to a list.
     * The method gets the value of the month combo box and adds all appointment in that month to the monthsAppts observable list.
     */
    private void getMonthlyAppts(){

        for(Appointment appointment : dbTables.getAllAppointments()){
            if (appointment.getDate().getMonth() == monthComboBox.getValue()){
                monthsAppts.add(appointment);
            }
        }
    }

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private Label customerLbl;

    @FXML
    private Label numberOfApptsTxt;

    @FXML
    private Label reportsLbl;

    @FXML
    private Label helpNote;

    @FXML
    private Label typeLbl;

    @FXML
    private Label contactLbl;

    @FXML
    private TableView<Appointment> contactScheduleTbl;

    @FXML
    private TableColumn<Appointment, Integer> apptId;

    @FXML
    private TableColumn<Appointment, String> apptTitle;

    @FXML
    private TableColumn<Appointment, String> apptType;

    @FXML
    private TableColumn<Appointment, String> apptDescription;

    @FXML
    private TableColumn<Appointment, LocalDateTime> apptStartTime;

    @FXML
    private TableColumn<Appointment, LocalDateTime> apptEndTime;

    @FXML
    private TableColumn<Appointment, Integer> custID;

    @FXML
    private Button mainBtn;

    @FXML
    private TextField numberOfTypeAppts;

    @FXML
    private TextField numberOfCustomers;

    @FXML
    private ComboBox<String> apptTypeComboBox;

    @FXML
    private ComboBox<Month> monthComboBox;


    /**
     * This method is called when the month is selected from the combo box.
     * The method calls get monthly appointments which adds the appointments of the selected month to a list
     * @param event month selection
     */
    @FXML
    void onActionShowMonth(ActionEvent event) {
        monthsAppts.clear();
        getMonthlyAppts();

    }

    /**
     * This method displays the number of appointments for the selected type.
     * Once the type is selected the method searches through all appointments in the dbTables and adds the number of
     * appointments with the selected type.
     * @param event selecting a type
     */
    @FXML
    void onShowTypeNumber(ActionEvent event) {
        String selectedType = apptTypeComboBox.getSelectionModel().getSelectedItem();
        int numberOfSelected = 0;

        for(Appointment appointment : monthsAppts){
            if(appointment.getType().equals(selectedType)){
                numberOfSelected++;
            }
        }
        numberOfSelectedType = numberOfSelected;
        numberOfTypeAppts.setText(String.valueOf(numberOfSelectedType));
    }

    /**
     * This method fills the schedule table view once a contact is selected. First the method clears the observable list.
     * The method goes through all appointments in dbTables and if the contact matches the selected contact it is added to the observable
     * list selectedContactAppts. The method then orders the observable list in order by time of appointments. Then runs the generateScheduleTable.
     * @param event selecting a contact
     */
    @FXML
    void onShowAppts(ActionEvent event){
        //CLEAR SELECTED CONTACT APPT LIST
        selectedContactAppts.clear();

        if(!contactComboBox.getSelectionModel().isEmpty()) {
            //ADD CONTACTS APPOINTMENTS TO OBSERVABLE LIST
            for (Appointment appointment : dbTables.getAllAppointments()) {
                if (contactComboBox.getSelectionModel().getSelectedItem().getContact_ID() == appointment.getContact_ID()) {
                    selectedContactAppts.add(appointment);
                }
            }

            //SORT ELEMENTS IN ORDER BY START TIME
            for (int i = 0; i < selectedContactAppts.size(); i++) {
                for (int j = i+1 ; j < selectedContactAppts.size(); j++) {
                    if (selectedContactAppts.get(i).getStart().isAfter(selectedContactAppts.get(j).getStart())) {
                        Appointment tempAppt = selectedContactAppts.get(i);
                        selectedContactAppts.set(i,selectedContactAppts.get(j));
                        selectedContactAppts.set(j,tempAppt);
                    }
                }
            }

            generateScheduleTable();

        }

    }

    /**
     * This method is called when the back button is clicked.
     * The method returns to the mainCustomersAppts view passing this screens dbTables and user.
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void goToMainView(MouseEvent event) throws IOException {

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
