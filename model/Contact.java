package model;
/**
 * This class creates Contact Objects.
 * @author Greg Newby (959900)
 */
public class Contact {

    /**the ID of the contact*/
    private int Contact_ID;
    /**the name of the contact*/
    private String Contact_Name;
    /**the email for the contact*/
    private String Email;

    /**This is a contact constructor.
     * This constructor fills all variables
     * @param Contact_ID the ID of the contact
     * @param Contact_Name the name of the contact
     * @param Email the email for the contact
     */
    public Contact(int Contact_ID, String Contact_Name, String Email){
        this.Contact_ID = Contact_ID;
        this.Contact_Name = Contact_Name;
        this.Email = Email;
    }

    /**
     * This method sets the ID of the contact.
     * @param contact_ID the ID of the contact
     */
    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     * This method sets the name of the contact.
     * @param contact_Name the name of the contact
     */
    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    /**
     * This method sets the email of the contact.
     * @param email the email for the contact
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     * This method returns the ID of the contact.
     * @return the ID of the contact
     */
    public int getContact_ID() {
        return Contact_ID;
    }

    /**
     * This method returns the contact's name.
     * @return the name of the contact
     */
    public String getContact_Name() {
        return Contact_Name;
    }

    /**
     * This method returns the email for the contact.
     * @return the email for the contact
     */
    public String getEmail() {
        return Email;
    }

    /**
     * This method overrides the display for combo boxes so that you see the name of the contact.
     * @return the name of the contact
     */
    @Override
    public String toString() {
        return (Contact_Name);
    }

}
