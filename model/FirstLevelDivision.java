package model;

import java.time.LocalDateTime;

/**
 * This class creates First Level Division Objects.
 * @author Greg Newby (959900)
 */
public class FirstLevelDivision {

    /**the ID of the First-Level Division*/
    private int Division_ID;
    /**the name of the First-Level Division*/
    private String Division;
    /**the date/time the First-Level Division object was created*/
    private LocalDateTime Create_Date;
    /**the creator of the First-Level Division object*/
    private String Created_By;
    /**the date/time of the last update for the First-Level Division object*/
    private LocalDateTime Last_Update;
    /**the person who last updated the First-Level Division*/
    private String Last_Updated_By;
    /**the country ID for the First-Level Division*/
    private int Country_ID;

    /**
     * This is a constructor for the First-Level Divisions class.
     * This constructor fills all variables
     * @param Division_ID the ID of the First-Level Division
     * @param Division the name of the First-Level Division
     * @param Create_Date the date/time the First-Level Division object was created
     * @param Created_By creator of the First-Level Division object
     * @param Last_Update the date/time of the last update for the First-Level Division object
     * @param Last_Updated_By the person who last updated the First-Level Division
     * @param Country_ID the country ID for the First-Level Division
     */
    public FirstLevelDivision(int Division_ID, String Division, LocalDateTime Create_Date, String Created_By, LocalDateTime Last_Update, String Last_Updated_By, int Country_ID){
        this.Division_ID = Division_ID;
        this.Division = Division;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
        this.Country_ID = Country_ID;
    }

    /**
     * This method sets the ID of the First-Level Division.
     * @param division_ID the ID of the First-Level Division
     */
    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    /**
     * This method sets the name of the First-Level Division.
     * @param division the name of the First-Level Division
     */
    public void setDivision(String division) {
        Division = division;
    }

    /**
     * This method sets the date/time the First-Level Division object was created.
     * @param create_Date the date/time the First-Level Division object was created
     */
    public void setCreate_Date(LocalDateTime create_Date) {
        Create_Date = create_Date;
    }

    /**
     * This method sets the creator of the First-Level Division object.
     * @param created_By the creator of the First-Level Division object
     */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /**
     * This method sets the date/time of the last update for the First-Level Division object.
     * @param last_Update the date/time of the last update for the First-Level Division object
     */
    public void setLast_Update(LocalDateTime last_Update) {
        Last_Update = last_Update;
    }

    /**
     * This method sets the person who last updated the First-Level Division.
     * @param last_Updated_By the person who last updated the First-Level Division
     */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /**
     * This method sets the country ID for the First-Level Division.
     * @param country_ID the country ID for the First-Level Division
     */
    public void setCountry_ID(int country_ID) {
        Country_ID = country_ID;
    }

    /**
     * This method gets the ID of the First-Level Division.
     * @return the ID of the First-Level Division
     */
    public int getDivision_ID() {
        return Division_ID;
    }

    /**
     * This method gets the name of the First-Level Division.
     * @return the name of the First-Level Division
     */
    public String getDivision() {
        return Division;
    }

    /**
     * This method gets the date/time the First-Level Division was created.
     * @return the date/time the First-Level Division was created
     */
    public LocalDateTime getCreate_Date() {
        return Create_Date;
    }

    /**
     * This method gets the creator of the First-Level Division.
     * @return the creator of the First-Level Division
     */
    public String getCreated_By() {
        return Created_By;
    }

    /**
     * This method gets the date/time the First-Level Division was last updated.
     * @return the date/time the First-Level Division was last updated.
     */
    public LocalDateTime getLast_Update() {
        return Last_Update;
    }

    /**
     * This method gets the person that last updated the First-Level Division.
     * @return the person that last updated the First-Level Division
     */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /**
     * This method gets the country ID of the First-Level Division.
     * @return the country ID of the First-Level Division
     */
    public int getCountry_ID() {
        return Country_ID;
    }

    /**
     * This method overrides the display for combo boxes so that you see the name of the First-Level Division.
     * @return the name of the First-Level
     */
    @Override
    public String toString(){
        return(Division);
    }
}
