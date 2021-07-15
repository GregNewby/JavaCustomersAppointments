package model;

import java.time.LocalDateTime;

/**
 * This class creates Country Objects.
 * @author Greg Newby (959900)
 */
public class Country {

    /**the ID of the country*/
    private int County_ID;
    /**the name of the country*/
    private String Country;
    /**the date/time that the county object was created*/
    private LocalDateTime Create_Date;
    /**the person who created the country object*/
    private String Created_By;
    /**the time/date the country object was last updated*/
    private LocalDateTime Last_Update;
    /**the person who last updated the country object*/
    private String Last_Updated_By;

    /**
     * This is a country constructor.
     * This constructor fills all the variables
     * @param Country_ID the ID of the country
     * @param Country the name of the country
     * @param Create_Date the date/time that the county object was created
     * @param Created_By the person who created the country object
     * @param Last_Update the time/date the country object was last updated
     * @param Last_Updated_By the person who last updated the country object
     */
    public Country(int Country_ID, String Country, LocalDateTime Create_Date,
                   String Created_By, LocalDateTime Last_Update, String Last_Updated_By){
        this.County_ID = Country_ID;
        this.Country = Country;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this. Last_Updated_By = Last_Updated_By;
    }

    /**
     *This method sets the country's ID.
     * @param county_ID the ID of the country
     */
    public void setCounty_ID(int county_ID) {
        County_ID = county_ID;
    }

    /**
     * This method sets the country name.
     * @param country the name of the country
     */
    public void setCountry(String country) {
        Country = country;
    }

    /**
     *This method sets the date/time that the country object was created.
     * @param create_Date the date/time that the county object was created
     */
    public void setCreate_Date(LocalDateTime create_Date) {
        Create_Date = create_Date;
    }

    /**
     * This method sets who created the country object.
     * @param created_By the person who created the country object
     */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /**
     * This method sets the time/date the country object was last updated.
     * @param last_Update the time/date the country object was last updated
     */
    public void setLast_Update(LocalDateTime last_Update) {
        Last_Update = last_Update;
    }

    /**
     * This method sets the person who last updated the country object.
     * @param last_Updated_By the person who last updated the country object
     */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /**
     * This method gets the ID of the country.
     * @return the ID of the country
     */
    public int getCounty_ID() {
        return County_ID;
    }

    /**
     * This method gets the name of the country.
     * @return the name of the country
     */
    public String getCountry() {
        return Country;
    }

    /**
     * This method gets the date/time that the county object was created.
     * @return the date/time that the county object was created
     */
    public LocalDateTime getCreate_Date() {
        return Create_Date;
    }

    /**
     * This method gets the person who created the country object.
     * @return the person who created the country object
     */
    public String getCreated_By() {
        return Created_By;
    }

    /**
     * This method gets the time/date the country object was last updated.
     * @return the time/date the country object was last updated
     */
    public LocalDateTime getLast_Update() {
        return Last_Update;
    }

    /**
     * This method gets the person who last updated the country object.
     * @return the person who last updated the country object
     */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /**
     * This method overrides the display for combo boxes so that you see the name of the country.
     * @return the name of the country
     */
    @Override
    public String toString(){
        return(Country);
    }
}
