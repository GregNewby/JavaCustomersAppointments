package model;

import java.time.LocalDateTime;

/**
 * This class creates Users.
 * @author Greg Newby (959900)
 */
public class User {

    /**the ID of the user*/
    private int User_ID;
    /**the name of the user*/
    private String User_Name;
    /**the users password*/
    private String Password;
    /**the date/time the user object was created*/
    private LocalDateTime Create_Date;
    /**the creator of the user object*/
    private String Created_By;
    /**the date/time of the user's last update*/
    private LocalDateTime Last_Update;
    /**the person who last updated the user*/
    private String Last_Updated_By;

    /**
     * This is a constructor for users.
     * This constructor fills all variables.
     * @param User_ID the ID of the user
     * @param User_Name the name of the user
     * @param Password the users password
     * @param Create_Date the date/time the user object was created
     * @param Created_By the creator of the user object
     * @param Last_Update the date/time of the user's last update
     * @param Last_Updated_By the person who last updated the user
     */
    public User(int User_ID, String User_Name, String Password, LocalDateTime Create_Date, String Created_By, LocalDateTime Last_Update, String Last_Updated_By){
        this.User_ID = User_ID;
        this.User_Name = User_Name;
        this.Password = Password;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
    }

    /**
     * This method sets the ID of the user.
     * @param User_ID the ID of the user
     */
    public void setUser_ID(int User_ID){
        this.User_ID = User_ID;
    }

    /**
     * This method sets the name of the user.
     * @param User_Name the name of the user
     */
    public void setUser_Name(String User_Name){
        this.User_Name = User_Name;
    }

    /**
     * This method sets the users password.
     * @param Password the users password
     */
    public void setPassword(String Password){
        this.Password = Password;
    }

    /**
     * This method sets the date/time the user object was created.
     * @param Create_Date the date/time the user object was created
     */
    public void setCreate_Date(LocalDateTime Create_Date) {
        this.Create_Date = Create_Date;
    }

    /**
     * This method sets the creator of the user object.
     * @param Created_By the creator of the user object
     */
    public void setCreated_By(String Created_By){
        this.Created_By = Created_By;
    }

    /**
     * This method sets the date/time of the user's last update.
     * @param Last_Update the date/time of the user's last update
     */
    public void setLast_Update(LocalDateTime Last_Update){
        this.Last_Update = Last_Update;
    }

    /**
     * This method sets the person who last updated the user.
     * @param Last_Updated_By the person who last updated the user
     */
    public void setLast_Updated_By(String Last_Updated_By){
        this.Last_Updated_By = Last_Updated_By;
    }

    /**
     * This method gets the ID of the user.
     * @return the ID of the user
     */
    public int getUser_ID() {
        return User_ID;
    }

    /**
     * This method gets the name of the user.
     * @return the name of the user
     */
    public String getUser_Name() {
        return User_Name;
    }

    /**
     * This method gets the users password.
     * @return the users password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * This method gets the date/time the user object was created.
     * @return the date/time the user object was created
     */
    public LocalDateTime getCreate_Date() {
        return Create_Date;
    }

    /**
     * This method gets the creator of the user object.
     * @return the creator of the user object
     */
    public String getCreated_By() {
        return Created_By;
    }

    /**
     * This method gets the date/time of the user's last update.
     * @return the date/time of the user's last update
     */
    public LocalDateTime getLast_Update() {
        return Last_Update;
    }

    /**
     * This method gets the person who last updated the user.
     * @return the person who last updated the user
     */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /**
     * This method overrides the display for combo boxes so that you see the name of the user.
     * @return the name of the user
     */
    @Override
    public String toString() {
        return (User_Name);
    }

}
