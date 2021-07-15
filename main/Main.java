package main;

import model.*;
import utilities.DBConnection;
import utilities.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view_controller.LoginController;

import java.io.FileNotFoundException;
import java.io.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**This application is a GUI-based scheduling desktop app for a consulting organization with office hours of 8am-10pm Eastern Standard Time.
 *
 * The purpose of this application is to interface with a database that uses tables of Customers, Appointments, Users, Contacts, Countries and First-Level-Divisions.
 * This application allows you to manage current appointments and customers with the capability to add, modify and delete. The application has language capabilities for
 * english and french.
 *
 * JavaDocs are located in src\files\Javadocs\index
 * README.txt located in src\files
 * login activity file located in src\login_activity.txt *
 *
 * @author Greg Newby (959900)
 */
public class Main extends Application {

    DBTables dbTables = new DBTables();

    /** This method fills the dbTables object with the tables from the database.
     You must first make a connection the the database before calling this method
     */
    private void fillDBTables() throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn); //creates statement Object
        Statement statement = DBQuery.getStatement(); //fetches statement

        // Fills Country Data
        String selectCountriesStatement = "SELECT * FROM countries";
        statement.execute(selectCountriesStatement);
        ResultSet rs = statement.getResultSet(); //gets a ResultSet Object

        while(rs.next()){
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            dbTables.addCountry(new Country(countryID,countryName,createDate,createdBy,lastUpdate,lastUpdatedBy));

        }



        //Fills User Data
        String selectUsersStatement = "SELECT * FROM users";
        statement.execute(selectUsersStatement);
        ResultSet rs2 = statement.getResultSet(); //gets a ResultSet Object

        while(rs2.next()){
            int userID = rs2.getInt("User_ID");
            String userName = rs2.getString("User_Name");
            String password = rs2.getString("Password");
            LocalDateTime createDate = rs2.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs2.getString("Created_By");
            LocalDateTime lastUpdate = rs2.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs2.getString("Last_Updated_By");

            dbTables.addUser(new User(userID,userName,password,createDate,createdBy,lastUpdate,lastUpdatedBy));

        }



        // Fills First-Level-Division Data
        String selectFirstLevelDivisionStatement = "SELECT * FROM first_level_divisions";
        statement.execute(selectFirstLevelDivisionStatement);
        ResultSet rs4 = statement.getResultSet(); //gets a ResultSet Object

        while(rs4.next()){
            int divisionID = rs4.getInt("Division_ID");
            String division = rs4.getString("Division");
            LocalDateTime createDate = rs4.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs4.getString("Created_By");
            LocalDateTime lastUpdate = rs4.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs4.getString("Last_Updated_By");
            int countryID = rs4.getInt("COUNTRY_ID");

            dbTables.addFirstLevelDivision(new FirstLevelDivision(divisionID,division,createDate,createdBy,lastUpdate,lastUpdatedBy,countryID));

        }



        // Fills Appointment Data
        String selectAppointmentsStatement = "SELECT * FROM appointments";
        statement.execute(selectAppointmentsStatement);
        ResultSet rs5 = statement.getResultSet(); //gets a ResultSet Object

        while(rs5.next()){
            int appointmentID = rs5.getInt("Appointment_ID");
            String title = rs5.getString("Title");
            String description = rs5.getString("Description");
            String location = rs5.getString("Location");
            String type = rs5.getString("Type");
            LocalDateTime start = rs5.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs5.getTimestamp("End").toLocalDateTime();
            LocalDateTime createDate = rs5.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs5.getString("Created_By");
            LocalDateTime lastUpdate = rs5.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs5.getString("Last_Updated_By");
            int customerID = rs5.getInt("Customer_ID");
            int userID = rs5.getInt("User_ID");
            int contactID = rs5.getInt("Contact_ID");

            dbTables.addAppointment(new Appointment(appointmentID,title,description,location,type,start,end,createDate,createdBy,lastUpdate,lastUpdatedBy,customerID,userID,contactID));

        }


        //Fills Contact Data
        String selectContactsStatement = "SELECT * FROM contacts";
        statement.execute(selectContactsStatement);
        ResultSet rs6 = statement.getResultSet();

        while(rs6.next()){
            int contactID = rs6.getInt("Contact_ID");
            String contactName = rs6.getString("Contact_Name");
            String email = rs6.getString("Email");

            dbTables.addContact(new Contact(contactID,contactName,email));

        }


        // Fills Customer Data
        String selectCustomersStatement = "SELECT * FROM customers";
        statement.execute(selectCustomersStatement);
        ResultSet rs3 = statement.getResultSet(); //gets a ResultSet Object

        while(rs3.next()){
            int customerID = rs3.getInt("Customer_ID");
            String customerName = rs3.getString("Customer_Name");
            String address = rs3.getString("Address");
            String postalCode = rs3.getString("Postal_Code");
            String phone = rs3.getString("Phone");
            LocalDateTime createDate = rs3.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy =  rs3.getString("Created_By");
            LocalDateTime lastUpdate = rs3.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs3.getString("Last_Updated_By");
            int divisionID = rs3.getInt("Division_ID");

            dbTables.addCustomer(new Customer(customerID,customerName,address,postalCode,phone,createDate,createdBy,lastUpdate,lastUpdatedBy,divisionID));

        }
        // add all associated Appointments
        for(Appointment appointment : dbTables.getAllAppointments()){
            for(Customer customer : dbTables.getAllCustomers()){
                if(appointment.getCustomer_ID() == customer.getCustomer_ID()){
                    customer.addAssociatedAppt(appointment);
                }
            }
        }

    }

    /**This is the start method.
     * This method brings up the first screen the user will see.
     *
     * @param primaryStage this is the primary stage.
     * @throws Exception this exception is thrown because we are changing screens.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //First fill/load the dbTables from the database
        fillDBTables();

        // Then bring up the first screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/loginView.fxml"));
        view_controller.LoginController controller = new view_controller.LoginController(dbTables);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Greg_Newby C195");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**This is the main method.
     * This method starts the JavaFX application.
     * It makes a connection to the database and closes the connection when the program is closed.
     *
     * @param args
     * @throws SQLException this exception is thrown when reaching out to the database
     * @throws FileNotFoundException this exception is thrown when the Resource bundle cannot be found.
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        Connection conn = DBConnection.startConnection();


        launch(args);


        DBConnection.closeConnection();
    }
}











