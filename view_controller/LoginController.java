package view_controller;

import model.DBTables;
import model.User;
import utilities.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This class is the controller for the Login view.
 * This class contains all the methods that are called for the
 * actions and buttons
 * @author Greg Newby (959900)
 */
public class LoginController implements Initializable {

    private Stage stage;
    private DBTables dbTables;
    private User user;
    ResourceBundle rb = ResourceBundle.getBundle("Lang", Locale.forLanguageTag("fr"));

    /**This is a default constructor. */
    public LoginController() throws IOException, FileNotFoundException { }

    /**
     * This controller takes the dbTables as input and sets it as the dbTables for this screen.
     * @param dbTables the dbTable that is passed to this screen
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     * @throws FileNotFoundException this exception is thrown when an attempt to open the file failed
     */
    public LoginController(DBTables dbTables) throws IOException, FileNotFoundException {
        this.dbTables = dbTables;
    }

    /**
     * This method initializes the Login Controller class.
     * The location label is set with the users time zone. Then the other labels on the screen are translated
     * depending on the language setting of the user. A try catch block is used to set the default language to english if not french.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationLbl.setText(String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));

        //LANGUAGE FORMAT
        try {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                passwordLogin.setText(rb.getString("Password"));
                userIDTxt.setText(rb.getString("User") + " " + rb.getString("ID"));
                loginBtn.setText(rb.getString("Log-In"));
                exitBtn.setText(rb.getString("Exit"));
                locationTxt.setText(rb.getString("Location") + ": ");
                programTitle.setText(rb.getString("programTitle"));

            }
        }
        catch (MissingResourceException mrb){
            //USE DEFAULT (ENGLISH)
        }
    }

    @FXML
    private Label locationLbl;

    @FXML
    private Label programTitle;

    @FXML
    private Label locationTxt;

    @FXML
    private Label errorTxt;

    @FXML
    private Label passwordLogin;

    @FXML
    private Label userIDTxt;

    @FXML
    private TextField userTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Button loginBtn;

    @FXML
    private Button exitBtn;

    /**
     * This method is run when the exit button is clicked.
     * The program closes.
     * @param event mouse click
     */
    @FXML
    void onClickExitProgram(MouseEvent event) {
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * This method is run when the Log-In button is clicked.
     * A file login_activity is created within this method that tracks the successful or unsuccessful logins.
     *
     * LOGICAL ERROR: The method first tests the entered username against the usernames in the dbTables. If it is found then it
     * checks the users password to make sure it matches. If they both match the screen is moved to the mainCustomerAppts view
     * and added to the login_activity as a successful login. If not the correct error is
     * displayed and logged into the file as a failed login.
     *
     * @param event mouse click
     * @throws IOException this exception is thrown by failed or interrupted I/O operations
     */
    @FXML
    void onClickGoToMain(MouseEvent event) throws IOException {

        String userName = userTxt.getText();
        String password = passwordTxt.getText();
        int userInt = 0;
        int passInt = 0;

        for (User user : dbTables.getAllUsers()) {
            if (userName.equals(user.getUser_Name())) {
                userInt++;
                if (password.equals(user.getPassword())) {
                    passInt++;
                    this.user = user;
                }
            }
        }


        String filename = "src/login_activity.txt";
        FileWriter fw = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fw);

        if (passInt >= 1){
            outputFile.println("User: " + userName + " |Date: " + LocalDate.now().toString() + " |Time: " + LocalTime.now().toString() + " |Successful: Yes");
        }
        if (passInt == 0) {
            outputFile.println("User: " + userName + " |Date: " + LocalDate.now().toString() + " |Time: " + LocalTime.now().toString() + " |Successful: No");
        }
        outputFile.close();


        if(userInt == 1){
            if(passInt == 1){

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
            else{
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Mot de passe incorrect!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Incorrect password!");
                    alert.showAndWait();
                }
            }
        }
        else{
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("L'ID utilisateur n'existe pas");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("User ID does not exist");
                alert.showAndWait();
            }
        }


    }


}
