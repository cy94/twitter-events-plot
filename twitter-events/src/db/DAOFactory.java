package db;

import java.lang.*;
import java.sql.*;

/**
 * A Factory for DAO objects
 * 
 * @author Chandan Yeshwanth
 *
 */
public class DAOFactory {
	/**
	 * Database details
	 */
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/twitter";
	static final String USER = "root";
	static final String PASS = "internship";
	
	Connection dbconnection = null;
	TweetDAO tweetDAO = null;
	boolean activeConnection = false;

	public DAOFactory()
	{
		dbconnection = null;
		activeConnection = false;
	}
	
	/**
	 * Connects to the MySQL database 
	 * 
	 * @throws Exception
	 */
	public void activateConnection() throws Exception
	{
		if( activeConnection == true )
			throw new Exception("Connection already active");

		try{
			Class.forName("com.mysql.jdbc.Driver");
			dbconnection = DriverManager.getConnection(DB_URL, USER, PASS);
			activeConnection = true;
			System.out.println("Activated connection");
		} catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("Unable to connect, try again.");
		}
	}
	
	/**
	 * Close connection
	 */
	public void deactivateConnection()
	{
		// Okay to keep deactivating an already deactivated connection
		activeConnection = false;
		
		if( dbconnection != null ){
			try{
				
				dbconnection.close();
				dbconnection = null;

				// null all DAO objects
				tweetDAO = null;
				
				System.out.println("Deactivated connection");
			}
			catch (SQLException ex) {
			    // handle any errors
			    System.out.println("Could not disconnect from database.");
			}
		}
	}
	
	/**
	 * Implements a singleton pattern for the DAO object 
	 * 
	 * @return a TweetDAO_JDBC object
	 * @throws Exception
	 */
	public TweetDAO getTweetDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated.");

		if( tweetDAO == null )
			tweetDAO = new TweetDAO_JDBC( dbconnection );

		return tweetDAO;
	}
}
