package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class creates Appointment objects.
 * @author Greg Newby (959900)
 */
public class Appointment {
    /**the id of the appointment*/
    private int Appointment_ID;
    /**the title of the appointment*/
    private String Title;
    /**the description of the appointment*/
    private String Description;
    /**the location of the appointment*/
    private String Location;
    /**the type of the appointment*/
    private String Type;
    /**the date of the appointment*/
    private LocalDate Date;
    /**the start time of the appointment*/
    private LocalDateTime Start;
    /**the end time of the appointment*/
    private LocalDateTime End;
    /**the creation date of the appointment*/
    private LocalDateTime Create_Date;
    /**the creator of the appointment*/
    private String Created_By;
    /**the time the appointment was last updated*/
    private LocalDateTime Last_Update;
    /**the last person to update the appointment*/
    private String Last_Updated_By;
    /**the appointments Customer ID*/
    private int Customer_ID;
    /**the appointments User ID*/
    private int User_ID;
    /**the appointments Contact ID*/
    private int Contact_ID;

    /**This is an Appointment Constructor.
     * This constructor fills all variables
     * @param Appointment_ID the id of the appointment
     * @param Title the title of the appointment
     * @param Description the description of the appointment
     * @param Location the location of the appointment
     * @param Type the type of the appointment
     * @param Start the start of the appointment
     * @param End the end of the appointment
     * @param Create_Date the creation date of the appointment
     * @param Created_By the creator of the appointment
     * @param Last_Update the last update of the appointment
     * @param Last_Updated_By the last person to update the appointment
     * @param Customer_ID the customer ID for the appointment
     * @param User_ID the user ID for the appointment
     * @param Contact_ID the contact ID for the appointment
     */
    public Appointment(int Appointment_ID, String Title, String Description, String Location, String Type, LocalDateTime Start, LocalDateTime End,
                       LocalDateTime Create_Date, String Created_By, LocalDateTime Last_Update, String Last_Updated_By, int Customer_ID, int User_ID, int Contact_ID){
        this.Appointment_ID = Appointment_ID;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.Type = Type;
        this.Date = Start.toLocalDate();
        this.Start = Start;
        this.End = End;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
        this.Customer_ID = Customer_ID;
        this.User_ID = User_ID;
        this.Contact_ID = Contact_ID;
    }

    /**
     * This method sets the Appointment ID.
     * @param appointment_ID the appointment ID
     */
    public void setAppointment_ID(int appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    /**
     * This method sets the appointment title.
     * @param title the appointment title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * This method sets the appointment description.
     * @param description the appointment description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * This method sets the appointment location.
     * @param location the appointment location
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * This method sets the appointment type.
     * @param type the appointment type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * This method sets the appointment start.
     * @param start the appointment start
     */
    public void setStart(LocalDateTime start) {
        Start = start;
    }

    /**
     * This method sets the appointment end.
     * @param end the appointment end
     */
    public void setEnd(LocalDateTime end) {
        End = end;
    }

    /**
     * This method sets the appointment creation date.
     * @param create_Date the appointment creation date
     */
    public void setCreate_Date(LocalDateTime create_Date) {
        Create_Date = create_Date;
    }

    /**
     * This method sets the appointment creator.
     * @param created_By the appointment creator
     */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /**
     * This method sets the appointments last update.
     * @param last_Update the appointments last update
     */
    public void setLast_Update(LocalDateTime last_Update) {
        Last_Update = last_Update;
    }

    /**
     * This method sets the person the appointment was last updated by.
     * @param last_Updated_By the person the appointment was last updated by
     */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /**
     * This method sets the appointment customer ID.
     * @param customer_ID the appointment customer ID
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * This method sets the user ID for the appointment.
     * @param user_ID the user ID for the appointment
     */
    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    /**
     * This method sets the contact ID for the appointment.
     * @param contact_ID the contact ID for the appointment
     */
    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     * This method returns the appointment ID.
     * @return the appointment ID
     */
    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     * This method returns the appointment title.
     * @return the appointment title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * This method returns the appointment description.
     * @return the appointment description
     */
    public String getDescription() {
        return Description;
    }

    /**This method returns the appointment location.
     *
     * @return the appointment location
     */
    public String getLocation() {
        return Location;
    }

    /**
     * This method returns the appointment type.
     * @return the appointment type.
     */
    public String getType() {
        return Type;
    }

    /**
     * This method returns the appointment date.
     * @return the date of the appointment
     */
    public LocalDate getDate() { return Date; }

    /**
     * This method returns the start of the appointment.
     * @return the start of the appointment
     */
    public LocalDateTime getStart() {
        return Start;
    }

    /**
     * This method returns the end of the appointment.
     * @return the end of the appointment
     */
    public LocalDateTime getEnd() {
        return End;
    }

    /**
     * This method returns the day/time the appointment was created.
     * @return the day/time the appointment was created
     */
    public LocalDateTime getCreate_Date() {
        return Create_Date;
    }

    /**
     * This method returns the creator of the appointment.
     * @return the creator of the appointment
     */
    public String getCreated_By() {
        return Created_By;
    }

    /**
     * This method returns the time/date of the last update for the appointment.
     * @return the time/date of the last update for the appointment
     */
    public LocalDateTime getLast_Update() {
        return Last_Update;
    }

    /**
     * This method returns the user who last updated the appointment.
     * @return user who last updated the appointment
     */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /**
     * This method returns the customer of the appointment.
     * @return the customer of the appointment
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * This method returns the user of the appointment.
     * @return the user of the appointment
     */
    public int getUser_ID() {
        return User_ID;
    }

    /**
     * This method returns the contact of the appointment.
     * @return the contact of the appointment
     */
    public int getContact_ID() {
        return Contact_ID;
    }


}
