package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class creates a DBTables object.
 * The DBTables object holds the list of Customers, Appointments, Contacts, Countries, Users, and First-Level Divisions of the Database.
 * @author Greg Newby (959900)
 */
public class DBTables {

    /**the list of customers in the database*/
    private static ObservableList<Customer>            allCustomers = FXCollections.observableArrayList();
    /**the list of appointments in the database*/
    private static ObservableList<Appointment>         allAppointments = FXCollections.observableArrayList();
    /**the list of contacts in the database*/
    private static ObservableList<Contact>             allContacts = FXCollections.observableArrayList();
    /**the list of countries in the database*/
    private static ObservableList<Country>             allCountries = FXCollections.observableArrayList();
    /**the list of users in the database*/
    private static ObservableList<User>                allUsers = FXCollections.observableArrayList();
    /**the list of First-Level Divisions in the database*/
    private static ObservableList<FirstLevelDivision>  allFirstLevelDivisions = FXCollections.observableArrayList();

    /**
     * This method gets the full list of customers.
     * @return observable list of all customers
     */
    public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }

    /**
     * This method gets the full list of appointments.
     * @return observable list of all appointments
     */
    public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }

    /**
     * This method gets the full list of contacts.
     * @return observable list of all contacts
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /**
     * This method gets the full list of countries.
     * @return observable list of all countries
     */
    public static ObservableList<Country> getAllCountries(){
        return allCountries;
    }

    /**
     * This method gets the full list of users.
     * @return observable list of users
     */
    public static ObservableList<User> getAllUsers(){
        return allUsers;
    }

    /**
     * This method gets the full lists of First-Level Divisions.
     * @return observable list of First-Level Divisions
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions(){
        return allFirstLevelDivisions;
    }

    /**
     * This method adds a customer to the list of DBTables customers.
     * @param newCustomer the customer to add to the list of customers
     */
    public static void addCustomer(Customer newCustomer){
        allCustomers.add(newCustomer);
    }

    /**
     * This method adds an appointment to the list of DBTables appointments.
     * @param newAppointment the appointment to add to the list of appointments
     */
    public static void addAppointment(Appointment newAppointment){
        allAppointments.add(newAppointment);
    }

    /**
     * This method adds a contact to the list of DBTables contacts.
     * @param newContact the contact to add to the list of contacts
     */
    public static void addContact(Contact newContact){
        allContacts.add(newContact);
    }

    /**
     * This method adds a country to the list of DBTables countries.
     * @param newCountry the country to add to the list of countries
     */
    public static void addCountry(Country newCountry){
        allCountries.add(newCountry);
    }

    /**
     * This method adds a user to the list of DBTables users.
     * @param newUser the user to add to the list of users
     */
    public static void addUser(User newUser){
        allUsers.add(newUser);
    }

    /**
     * This method adds a First-Level Division to the list of DBTables First-Level Divisions.
     * @param newFirstLevelDivision the First-Level Division to be added to the list of First-Level Divisions
     */
    public static void addFirstLevelDivision(FirstLevelDivision newFirstLevelDivision){
        allFirstLevelDivisions.add(newFirstLevelDivision);
    }

    /**
     * This method replaces a customer in the list of DBTables customers.
     * @param index the index of the customer in the DBTables customers
     * @param newCustomer the new customer that will replace the old customer
     */
    public static void updateCustomer(int index, Customer newCustomer){
        allCustomers.set(index, newCustomer);
    }

    /**
     * This method deletes a customer from the list of DBTables customers.
     * @param selectedCustomer the selected customer to be deleted
     * @return True if the customer is deleted. False if the customer is not deleted
     */
    public static boolean deleteCustomer(Customer selectedCustomer){
        if (allCustomers.remove(selectedCustomer)){
            return true;
        }
        return false;
    }

    /**
     * This method replaces an appointment in the list of DBTables appointments.
     * @param index the index of the appointment in the DBTables appointments
     * @param newAppointment the new appointment to replace the old appointment
     */
    public static void updateAppointment(int index, Appointment newAppointment){
        allAppointments.set(index, newAppointment);
    }

    /**
     * This method deletes an appointment from the list of DBTables appointments.
     * @param selectedAppointment the selected appointment to be deleted
     * @return True if the appointment is deleted. False if the appointment is not deleted
     */
    public static boolean deleteAppointment(Appointment selectedAppointment){
        if(allAppointments.remove(selectedAppointment)){
            return true;
        }
        return false;
    }

    /**
     * This method looks up a customer that contains a portion of the string given.
     * This method goes through the list of customers in DBTables and adds that customer to an observable list (matchingCustomers)
     * if the customer name contains the given string. It then returns the matchingCustomer list.
     * @param customerName the string to see if it is contained in the list of customer names
     * @return the observable list of matching customers
     */
    public static ObservableList<Customer> lookUpCustomer(String customerName){
        ObservableList<Customer> matchingCustomers = FXCollections.observableArrayList();
        for(Customer customer : allCustomers){
            if(customer.getCustomer_Name().contains(customerName)){
                matchingCustomers.add(customer);
            }
        }
        return matchingCustomers;
    }

}
