package utilities;

//version 8.0.22
//JDK 11.0.9 win 64x

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to connect to a database.
 * @author Greg Newby (959900)
 */
public class DBConnection {

    // JDBC URL Parts
    private static final String dbName = "dbName";
    private static final String protocol = "protocol";
    private static final String vendor = ":vendor:";
    private static final String ipaddr = "//wgudb.ucertify.com/" + dbName;

    // JDBC URL
    private static final String jdbcURL = protocol + vendor + ipaddr ;

    //Connection and Driver Interface Reference
    private static Connection conn = null;
    private static final String mySQLJDBCDriver = "com.mysql.cj.jdbc.Driver";

    //Username and Password
    private static String  username = "username";
    private static String password = "password";

    /**
     * This method starts a connection to the database.
     * This method is usually only called in the beginning of the program.
     * @return the connection object
     */
    public static Connection startConnection()
    {
        try
        {
            Class.forName(mySQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successfull!");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * This method closes the connection to the database.
     * This method is usually called before the program closes.
     */
    public static void closeConnection()
    {
        try
        {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch (SQLException e)
        {
           //do nothing// System.out.println(e.getMessage());
        }
    }

    /**
     * This method starts a connection to the database.
     * This method is usually only called in the beginning of the program.
     * This method is similar to the other start connection but uses a data source.
     * @return the connection object
     */
    public static Connection startConnectionDataSource()
    {
        try
        {
            MysqlDataSource d = new MysqlDataSource();
            d.setUser(username);
            d.setPassword(password);
            d.setURL(jdbcURL);
            d.setDatabaseName(dbName);
            conn = d.getConnection();
            System.out.println("Connection Successfull!");
        }

        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * This method returns the current connection.
     * This method is used to get the connection once the initial connection has been established.
     * @return the connection object
     */
    public static Connection getConnection()
    {
        return conn;
    }

}
