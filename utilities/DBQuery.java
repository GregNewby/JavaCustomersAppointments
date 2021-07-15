package utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used any time a query is done on the database.
 * This class handles both regular statement objects and prepared statement objects.
 * If the .execute() function returns true the statement is a SELECT statement.
 * If the .execute() function returns false the statement is a INSERT, UPDATE, or DELETE statement.
 * @author Greg Newby (959900)
 */
public class DBQuery {

    /**a regular statement variable to hold the query*/
    private static Statement statement;
    /**a prepared statement variable to hold the query*/
    private static PreparedStatement preparedStatement;

    /**
     * This method sets a statement.
     * This method is good for Raw SQL statements but if you use variables you must enclose them in quotes which
     * can make things messy.
     * @param conn the connection to the database
     */
    public static void setStatement(Connection conn)
    {
        try
        {
            statement = conn.createStatement();
        }
        catch (SQLException e)
        {
        }
    }

    /**
     * This method sets a prepared statement.
     * This method is better for using variables in the SQL Statements. You also do not have to worry
     * about special characters in the variables.
     * @param conn the connection to the database
     * @param sqlStatement the prepared SQL statement that the query will use
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement)
    {
        try
        {
            preparedStatement = conn.prepareStatement(sqlStatement);
        }
        catch (SQLException e)
        {
        }
    }


    /**
     * This method returns the statement.
     * @return returns the statement
     */
    public static Statement getStatement()
    {
        return statement;
    }

    /**
     * This method returns the prepared statement.
     * @return returns the prepared statement
     */
    public static PreparedStatement getPreparedStatement()
    {
        return preparedStatement;
    }

}
