package webd4201.shine;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

public class UserDA {
	

	/**
	 * declare constant and a user object
	 */
	public static User aUser;
	
	/**
	 * declare connection variables
	 */
	private static Connection aConnection;
	private static Statement userStatement;
	
	/**
	 * declare static variables for all user's instance attribute values
	 */
	static long id;
	static String password;
	static String firstname;
	static String lastName;
	static String emailAddress; 
	// to use in a sql string, date format has String data type in here
	static Date lastAccess;
	static Date enrolDate;
	static boolean	enabled;
	static char type;


	/**
	 * establish connection
	 * @param conn
	 */
	public static void initialize(Connection conn) {
		try {
			aConnection = conn;
			userStatement = aConnection.createStatement();
		}
		catch (SQLException sqle) { System.out.println(sqle);}
	}
	
	/**
	 * close the database statement and hence connection
	 */
	public static void terminate() {
		 try { 	// close the statement
			 userStatement.close();
         }
         catch (SQLException sqle)
         { System.out.println(sqle); }
	}
	
	/**
	 * Retrieve the user from DB
	 * @param key
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static User retrieve(long key) throws NotFoundException, ParseException
	{ // retrieve user and Boat data
		aUser = null;

		try
 		{
			aConnection = DatabaseConnect.initialize();
			UserDA.initialize(aConnection);
			
			// prepared statement get ready 
			// retrieve music AND media info by using SQL JOIN / where you wanna use parameter, use '?'
			String prepareSqlUser = "SELECT* FROM users WHERE id = ?" ;
			PreparedStatement myStmtUser = aConnection.prepareStatement(prepareSqlUser);

			// set parameter(s) to the argument from the caller('key')/ 1 means first parameter, in this case only one parameter
			// DB stores id as String value, so convert it
			
			myStmtUser.setLong(1, key);
			
			// execution with the parameter(key)
			ResultSet rsUser = myStmtUser.executeQuery();
           
            // next method sets cursUser or & returns true if there is data
            boolean gotIt = rsUser.next();
            if (gotIt)
            {	// extract the data from users table
            	id = rsUser.getLong("id");
        		password = rsUser.getString("password");
        		firstname = rsUser.getString("firstname");
        		lastName = rsUser.getString("lastname");
        		emailAddress = rsUser.getString("email");
        		// get the date and convert it to a date type to create a user object
        		// get the date(string) from DB
        		String lastAccessString = rsUser.getString("lastaccess");
        		String enrolDateString = rsUser.getString("enroldate");

        		// convert date(String) to date type
        		try {
	        		lastAccess = new SimpleDateFormat("yyyy-mm-dd").parse(lastAccessString);
	        		enrolDate = new SimpleDateFormat("yyyy-mm-dd").parse(enrolDateString);
        		}
        		catch (ParseException e)
        			{ System.out.println(e);}
        		
        		enabled = rsUser.getBoolean("enabled");
        		// As pgSQL sees it, it's all Strings because it has no type for a single character, Just extract the Java char from the resulting String
        		type = rsUser.getString("type").charAt(0);
        		
        		
        		// BUILD a aUser
                try{
                	aUser = new User(id,password,firstname,lastName,emailAddress,lastAccess,enrolDate,enabled,type);
                }
                catch (InvalidUserDataException e) { 
                	System.out.println("Record for " + firstname + lastName + " contains an invalid ID number.  Verify and correct.");
                }
                
            } else	// nothing was retrieved
            {throw (new NotFoundException("User with id of  " + key +" not found in the database."));}

            rsUser.close();
                             
	   	}catch (SQLException e)
		{ System.out.println(e);}
                
		return aUser;
	}
	
	/**
	 * this method returns all the user by their type(s, f)
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static List<User> retrieveAll() throws NotFoundException, ParseException
	{ // retrieve user and Boat data
		List<User> allUser = new ArrayList<>();
		try
 		{
			aConnection = DatabaseConnect.initialize();
			UserDA.initialize(aConnection);
			
			// prepared statement get ready 
			String prepareSqlUser = "SELECT* FROM users" ;
			PreparedStatement myStmtUser = aConnection.prepareStatement(prepareSqlUser);

			// type is char, but here used String, hope it goes well
			
			
			// execution with the parameter(key)
			ResultSet rsUser = myStmtUser.executeQuery();
           
            // next method sets cursUser or & returns true if there is data
           
            while(rsUser.next()){
            	// extract the data from users table
            	try {
            		id = rsUser.getLong("id");	
	        		// if the data is 'admin(id 100000000)', skip it
	        		if(id != 100000000) {   		
		        		password = rsUser.getString("password");
		        		firstname = rsUser.getString("firstname");
		        		lastName = rsUser.getString("lastname");
		        		emailAddress = rsUser.getString("email");
		        		lastAccess = rsUser.getDate("lastaccess");
		        		enrolDate = rsUser.getDate("enroldate");
		        		enabled = rsUser.getBoolean("enabled");
		        		// As pgSQL sees it, it's all Strings because it has no type for a single character, Just extract the Java char from the resulting String
		        		type = rsUser.getString("type").charAt(0);
	            	
	
	        		// BUILD a aUser
	                	User aUser = new User(id,password,firstname,lastName,emailAddress,lastAccess,enrolDate,enabled,type);
	                	allUser.add(aUser);
	        		}
                }
                catch (InvalidUserDataException e) { 
                	System.out.println("Record for " + firstname + lastName + " contains an invalid ID number.  Verify and correct.");
                }
            	catch(Exception e) {
            		System.out.println("Error8: " + e);
            	}
            } 

            rsUser.close();
                             
	   	}catch (SQLException e)
		{ System.out.println(e);}
                
		return allUser;
	}
	
	
	/**
	 * create a user in the DB
	 * @param aUser
	 * @return
	 * @throws DuplicateException
	 * @throws ParseException
	 */
	public static boolean create(User aUser) throws DuplicateException, ParseException
	{	
		//insertion success flag
		Boolean inserted = false;
		
		try {
			// check if the user is already in the DB
			retrieve(aUser.getId());
			throw (new DuplicateException("The User name already exist in the DB"));
		}
		catch (NotFoundException e){
			try {
				System.out.println("The ID does not exist, ok to create a new one!");
				
				// retrieve the aUser attribute values
				id = aUser.getId();
				password = aUser.getPassword();
				firstname = aUser.getFirstName();
				lastName = aUser.getLastName();
				emailAddress = aUser.getEmailAddress();
				Date lastAccess = aUser.getLastAccess();
				Date enrolDate = aUser.getEnrolDate();
				enabled = aUser.isEnabled();
				type = aUser.getType();
				
				// create the SQL insert statement using attribute values
				
				String sqlInsertUserDB = "INSERT INTO users(id, password, firstname, lastname, email, lastaccess, enroldate, enabled, type) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
								
				PreparedStatement psCreateUser = aConnection.prepareStatement(sqlInsertUserDB);
				
				psCreateUser.setLong(1, id);
				psCreateUser.setString(2, hashPassword(password));
				psCreateUser.setString(3, firstname);
				psCreateUser.setString(4, lastName);
				psCreateUser.setString(5, emailAddress);
				psCreateUser.setDate(6 , new java.sql.Date (lastAccess.getTime()));
				psCreateUser.setDate(7 , new java.sql.Date (enrolDate.getTime()));
				psCreateUser.setBoolean(8 ,enabled);
				psCreateUser.setString(9 , String.valueOf(type));
				
				inserted = (psCreateUser.executeUpdate () == 1 ); //return value of the method

				System.out.println("User Insert Result: " + inserted);
				
			}
			catch (SQLException sqle) {
				System.out.println("SQL Error: " + sqle);
			}
		}
		
		// return true if inserted row is more than zero
		return inserted;
	}
	
	/**
	 * update the user info from the DB
	 * @param aUser
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static int update(User aUser) throws NotFoundException, ParseException
	{	
		int records = 0; 	
		
		// create the SQL update statement using attribute values(User DB)
		try {
			
			System.out.println("About to update user: " + aUser.getId() + " / " + aUser.getFirstName() + aUser.getLastName());
			
			// retrieve the aUser attribute values
			id = aUser.getId();
			password = aUser.getPassword();
			firstname = aUser.getFirstName();
			lastName = aUser.getLastName();
			emailAddress = aUser.getEmailAddress();
			Date lastAccess = aUser.getLastAccess();
			Date enrolDate = aUser.getEnrolDate();
			enabled = aUser.isEnabled();
			type = aUser.getType();
			
			// create the SQL update statement using attribute values
			String sqlUpdateUserDB = "Update users SET " +
					"password = ? ,"+
                    "firstname = ? ,"+
                    "lastname = ? ,"+
                    "email = ? ,"+
                    "lastaccess = ? ," +
                    "enroldate = ? ," +
                    "enabled = ? ," +
                    "type = ? " + 
                    "WHERE id = ?";
			
			PreparedStatement psUpdateUser = aConnection.prepareStatement(sqlUpdateUserDB);
			
			psUpdateUser.setString(1, password);
			psUpdateUser.setString(2, firstname);
			psUpdateUser.setString(3, lastName);
			psUpdateUser.setString(4, emailAddress);
			psUpdateUser.setDate(5 , new java.sql.Date(lastAccess.getTime()));
			psUpdateUser.setDate(6 , new java.sql.Date(enrolDate.getTime()));
			psUpdateUser.setBoolean(7 ,enabled);
			psUpdateUser.setString(8 , String.valueOf(type));
			psUpdateUser.setLong(9, id);
			
			System.out.println(
					"\nUpdate SQL info: " +
					"\nid: " + id + 
					"\nName: " + firstname + " " + lastName +
					"\nLast Access: " + new java.sql.Date(lastAccess.getTime())
);
		
			if (retrieve(id) != null) {    //determine if there is a User record to be updated
				// if found,
				// execute update in UserDB
				records = psUpdateUser.executeUpdate();
				System.out.println("User Update Result: " + records);
			} else {
				System.out.println("The User is not in the DB");
				throw new NotFoundException();
			}

                    
		}catch(NotFoundException e)
		{
			throw new NotFoundException("User with ID number " + id 
					+ " cannot be updated, does not exist in the system.");
		}catch (SQLException e)
		{ System.out.println(e);}
		// return the number of updated users
		return records;
	}

	/**
	 * delete the requested user from DB
	 * @param aUser
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static int delete(User aUser) throws NotFoundException, ParseException
	{	
		int records = 0;  // record variable if there is a record in Users table
		// this variable is not used now, but It could be used in the later.
		long userId = aUser.getId();
		// create the SQL delete statement
		String sqlDeleteUser = "DELETE FROM users " +
                                    "WHERE id = ?";

		// see if this User already exists in the database
		try
		{
			retrieve(userId);  //used to determine if record exists for the passed User
    		// if found, prepare the SQL delete statement to Delete the record
			PreparedStatement psDeleteUser = aConnection.prepareStatement(sqlDeleteUser);
			psDeleteUser.setLong(1, userId);
			
			// execute delete in UserDB
			records = psDeleteUser.executeUpdate();
			
			System.out.println("User Delete Result: " + records);
			
		}catch(NotFoundException e)
		{
			throw new NotFoundException("User with ID " + userId 
					+ " cannot be deleted, does not exist.");
		}catch (SQLException e)
			{ System.out.println(e);	}
		// return the number of deleted users
		return records;
	}
	
    /**
     * authenticate user
     * @param user id
     * @param password
     * @return
     * @throws UnsupportedEncodingException 
     */
	public static User authenticate(long userId, String pw) throws NotFoundException{
		// when the login is not successful, return a null object
		User aUser = null;
		// get the user's data from the DB 
		try {
			User retrievedUser = retrieve(userId);	
			
			// Convert input pw to SHA-1 hash hex code of String. (retrieved from: https://www.denismigol.com/posts/370/java-sha-1-hash-hex-string?i=1 )
		    MessageDigest md5 = MessageDigest.getInstance("SHA1");
		    byte[] digest = md5.digest(pw.getBytes("UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < digest.length; ++i) {
		        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
		    }
		    String hashedPw = sb.toString();
					
		    // compare the user ID and password
			if (hashedPw.equals(retrievedUser.getPassword())) {
				System.out.println("Login Successful!");
				
				// when the login successful, return user object
				aUser = retrievedUser;
			} else {
				// for debugging
				throw new NotFoundException("The input password is not match with ID: " + userId);
			}
			// catch statements
		} catch(NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch(UnsupportedEncodingException e) {
			System.out.println(e);
		} catch(ParseException e) {
			System.out.println(e);
		} catch(NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
		return aUser;
	
	}
	
	
	
	/**
	 * Admin Authenticate
	 * @param inputPw
	 * @return
	 */
	public static User adminAuthenticate(String admin, String inputPw) {
		
		try
 		{
			User adminUser = new User();
			long adminId = 100000000;
			
			aConnection = DatabaseConnect.initialize();
			UserDA.initialize(aConnection);
		
			String prepareSqlUser = "SELECT* FROM users WHERE id = ?" ;
			PreparedStatement myStmtUser = aConnection.prepareStatement(prepareSqlUser);
			
			myStmtUser.setLong(1, adminId);
			// execution with the parameter(key)
			ResultSet rsUser = myStmtUser.executeQuery();
			// if there is match result
            boolean gotIt = rsUser.next();
            if (gotIt)
            {	// extract the ADMIN id and pw data from users table
        		String adminPassword = rsUser.getString("password");
        		
        		// Hash input password and compare the retrieved Admin's password
    		    MessageDigest md5 = MessageDigest.getInstance("SHA1");
    		    byte[] digest = md5.digest(inputPw.getBytes("UTF-8"));
    		    StringBuilder sb = new StringBuilder();
    		    for (int i = 0; i < digest.length; ++i) {
    		        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
    		    }
    		    String hashedPw = sb.toString();
    					
    		    // compare the user ID and password
    			if (hashedPw.equals(adminPassword)) {
    				System.out.println("Login Successful!");
    				
    				adminUser.setId(100000000);
    				adminUser.setFirstName("ADMIN");
    				adminUser.setLastName("ADMIN");
    				adminUser.setType('a');
    				
    				return adminUser;

    			} else {
    				// for debugging
    				throw new NotFoundException("The input password is not match with the registered ADMIN password");
    			}
    		    
    			
            } else	// nothing was retrieved
            {throw (new NotFoundException("admin id: " + admin + " not found in the database. check Users database"));}
			
 		}catch(Exception e) {
 			System.out.println("Error: " + e);
 		}

		return null;
	}
	
	
	
	/**
	 * password hashing using sha1, to use in DB request
	 * @return
	 */
	public static String hashPassword(String pw) {
		String hashedPw = "";
		try {
		    MessageDigest md5 = MessageDigest.getInstance("SHA1");
		    byte[] digest = md5.digest(pw.getBytes("UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < digest.length; ++i) {
		        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
		    }
		    hashedPw = sb.toString();
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println(e);
		} 
		catch(UnsupportedEncodingException e) {
			System.out.println(e);
		}
	    
	    return hashedPw;
	}

	

}
