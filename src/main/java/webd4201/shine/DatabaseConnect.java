
package webd4201.shine;

/**
 * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
	// connection string static variables (from pgSQL-pgADmin: db name, user, password)
	private static final String url = "jdbc:postgresql://127.0.0.1:5432/webd4201_db";
	private static final String user = "webd4201_admin";
	private static final String password = "webd4201_password";
	private static Connection aConnection;
	
	public static Connection initialize() {
		try {
			// load the JDBC Driver for PostGreSQL
			// always you need internet connection to get the newest driver when excute this program
			Class.forName("org.postgresql.Driver");
			// Create the Connection instance
			aConnection = DriverManager.getConnection(url, user, password);
		}
		// will occur if you did not import the PostGreSQL *.jar file with drivers
		catch (ClassNotFoundException e) { System.out.println(e); }
		//any other database exception (misnamed db, misnamed user, wrong password, etc)
		catch (SQLException sqle) { System.out.println(sqle); }
			
		return aConnection;
	}
	
	public static void terminate() {
		try { aConnection.close(); }
		catch (SQLException sqle) { System.out.println(sqle); }
	}

}
