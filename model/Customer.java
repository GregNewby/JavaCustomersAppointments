package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * This class creates Customer Objects.
 * @author Greg Newby (959900)
 */
public class Customer {

    /**the ID of the customer*/
    private int Customer_ID;
    /**the name of the customer*/
    private String Customer_Name;
    /**the address of the customer*/
    private String Address;
    /**the Postal Code of the customer*/
    private String Postal_Code;
    /**the phone number of the customer*/
    private String Phone;
    /**the creation date/time of the customer object*/
    private LocalDateTime Create_Date;
    /**the creator of the customer object*/
    private String Created_By;
    /**the date/time of the customers last update*/
    private LocalDateTime Last_Update;
    /**the person to last update the customer object*/
    private String Last_Updated_By;
    /**the division ID of the customer*/
    private int Division_ID;
    /**an observable list of associated appointments*/
    private ObservableList<Appointment> associatedAppts = FXCollections.observableArrayList();


    /**
     * This is a customer constructor.
     * This constructor fills all variables for a customer
     * @param Customer_ID the ID of the customer
     * @param Customer_Name the name of the customer
     * @param Address the address of the customer
     * @param Postal_Code the Postal Code of the customer
     * @param Phone the phone number of the customer
     * @param Create_Date the creation date/time of the customer object
     * @param Created_By the creator of the customer object
     * @param Last_Update the date/time of the customers last update
     * @param Last_Updated_By the person to last update the customer object
     * @param Division_ID the division ID of the customer
     */
    public Customer(int Customer_ID, String Customer_Name, String Address, String Postal_Code, String Phone,
                    LocalDateTime Create_Date, String Created_By, LocalDateTime Last_Update, String Last_Updated_By, int Division_ID){
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Address = Address;
        this.Postal_Code = Postal_Code;
        this.Phone = Phone;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
        this.Division_ID = Division_ID;
    }

    /**
     * This method fills the associated appointments from an observable list of appointments.
     * @param associatedAppts an observable list of appointments
     */
    public void setAssociatedAppts(ObservableList<Appointment> associatedAppts) {
        this.associatedAppts = associatedAppts;
    }

    /**
     * This method add an appointment from the observable list of associated appointments.
     * @param appointment
     */
    public void addAssociatedAppt(Appointment appointment){
        associatedAppts.add(appointment);
    }

    /**
     * This method deletes an appointment from the observable list of associated appointments.
     * @param appointment
     * @return
     */
    public boolean deleteAssociatedAppt(Appointment appointment){
        if(associatedAppts.remove(appointment)){
            return true;
        }
        return false;
    }

    /**
     * This method sets the ID of the customer.
     * @param customer_ID the ID of the customer
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * This method sets the name of the customer.
     * @param customer_Name the name of the customer
     */
    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    /**
     * This method sets the address of the customer.
     * @param address the address of the customer
     */
    public void setAddress(String address) {
        Address = address;
    }

    /**
     * This method sets the Postal Code of the customer.
     * @param postal_Code the Postal Code of the customer
     */
    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    /**
     * This method sets the phone number of the customer.
     * @param phone the phone number of the customer
     */
    public void setPhone(String phone) {
        Phone = phone;
    }

    /**
     * This method sets the creation date/time of the customer object.
     * @param create_Date the creation date/time of the customer object
     */
    public void setCreate_Date(LocalDateTime create_Date) {
        Create_Date = create_Date;
    }

    /**
     * This method sets the creator of the customer object.
     * @param created_By the creator of the customer object
     */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /**
     * This method sets the date/time of the customers last update.
     * @param last_Update the date/time of the customers last update
     */
    public void setLast_Update(LocalDateTime last_Update) {
        Last_Update = last_Update;
    }

    /**
     * This method sets the person to last update the customer object.
     * @param last_Updated_By the person to last update the customer object
     */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /**
     * This method sets the division ID of the customer
     * @param division_ID the division ID of the customer
     */
    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    /**
     * This method gets the ID of the customer.
     * @return the ID of the customer
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * This method gets the name of the customer.
     * @return the name of the customer
     */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    /**This method gets the address of the customer.
     *
     * @return the address of the customer
     */
    public String getAddress() {
        return Address;
    }

    /**
     * This method gets the Postal Code of the customer.
     * @return the Postal Code of the customer
     */
    public String getPostal_Code() {
        return Postal_Code;
    }

    /**
     * This method gets the phone number of the customer.
     * @return the phone number of the customer
     */
    public String getPhone() {
        return Phone;
    }

    /**
     * This method gets the creation date/time of the customer object.
     * @return the creation date/time of the customer object
     */
    public LocalDateTime getCreate_Date() {
        return Create_Date;
    }

    /**
     * This method gets the creator of the customer object.
     * @return the creator of the customer object
     */
    public String getCreated_By() {
        return Created_By;
    }

    /**
     * This method gets the date/time of the customers last update.
     * @return the date/time of the customers last update
     */
    public LocalDateTime getLast_Update() {
        return Last_Update;
    }

    /**
     * This method gets the person to last update the customer object.
     * @return the person to last update the customer object
     */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /**
     * This method gets the division ID of the customer
     * @return the division ID of the customer
     */
    public int getDivision_ID() {
        return Division_ID;
    }

    /**
     * This method gets the observable list of associated appointments
     * @return the observable list of associated appointments
     */
    public ObservableList<Appointment> getAssociatedAppts() {
        return associatedAppts;
    }

    /**
     * This method overrides the display for combo boxes so that you see the name of the customer.
     * @return the name of the customer
     */
    @Override
    public String toString() {
        return (Customer_Name);
    }

}
