package webd4201.shine;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;

public class FacultyDA {
	
	public static Faculty aFaculty;
	
	/**
	 * declare connection info
	 */
	private static Connection aConnection;
	private static Statement userStatement;
	private static Statement FacultyStatement;
	
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
	
	static String schoolCode;
	static String schoolDescription;
	static String office;
	static int extension;

    /**
     * establish connection
     * @param conn
     */
	public static void initialize(Connection conn) {
		try {
			aConnection = conn;
			userStatement = aConnection.createStatement();
			FacultyStatement = aConnection.createStatement();
		}
		catch (SQLException sqle) { System.out.println(sqle);}
	}
	
	/**
	 * close the database statement and hence connection
	 */
	public static void terminate() {
		 try { 	// close the statement
			 userStatement.close();
			 FacultyStatement.close();
         }
         catch (SQLException sqle)
         { System.out.println(sqle); }
	}
	
	
	/**
	 * function for Initialize the User db connection 
	 */
	public static void initiallizeUserDb() {
		Connection c = null;
		c = DatabaseConnect.initialize();
		User.initialize(c);
	} 
	

	
	
	/**
	 * Faculty retrieve 
	 * @param key
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static Faculty retrieve(long key) throws NotFoundException, ParseException
	{ // retrieve Faculty and Boat data
		aFaculty = null;

		try
 		{
			// prepare statements
			String prepareSqlFaculty = "SELECT* FROM faculty WHERE id = ?" ;

			PreparedStatement myStmtFaculty = aConnection.prepareStatement(prepareSqlFaculty);
			
			// set parameter(s) to the argument from the caller('key')/ 1 means first parameter, in this case only one parameter

			myStmtFaculty.setLong(1, key);
			
			// execution with the parameter(key)

            ResultSet rsFaculty = myStmtFaculty.executeQuery();
           
    		// Retrieve the User data from user DB from UserDA
            try {
            	// call the function for user DB Initialize
            	initiallizeUserDb();
            	// pull the User information from the User DB
	    		User dbUser = User.retrieve(key);
	    		
    		    // assign the User's information to each attributes for building a Student object
	    		id = dbUser.getId();
	    		password = dbUser.getPassword();
	    		firstname = dbUser.getFirstName();
	    		lastName = dbUser.getLastName();
	    		emailAddress = dbUser.getEmailAddress();
	    		lastAccess = dbUser.getLastAccess();
	    		enrolDate = dbUser.getEnrolDate();
	    		enabled = dbUser.isEnabled();
	    		type = dbUser.getType();
            } catch(ParseException e) {
    			System.out.println(e);
    		} catch(NotFoundException e) {
    			throw new NotFoundException("Student with id of  " + key +" not found in the User database.");
    		}
        		
        		// extract the data from Faculty table
        		boolean gotthat = rsFaculty.next();
        		if(gotthat) {
        			schoolCode = rsFaculty.getString("school_code");
        			schoolDescription = rsFaculty.getString("school_description");
        			office = rsFaculty.getString("office");
        			extension = rsFaculty.getInt("extension");
        		}
        		else {
        			throw new NotFoundException("Student with id of  " + key +" not found in the Students database.");
        		}
 		
        		// create a Faculty
                try{
                	aFaculty = new Faculty(id,password,firstname,lastName,emailAddress,lastAccess,
                			enrolDate,enabled,type,schoolCode,schoolDescription,office,extension);
                }
                catch (InvalidUserDataException e) { 
                	System.out.println("Record for " + firstname + lastName + " contains an invalid ID number.  Verify and correct.");
                }

            rsFaculty.close();
                             
	   	}catch (SQLException e)
		{ System.out.println(e);}
                
		return aFaculty;
	}
	
	/**
	 * creating a new faculty
	 * @param aFaculty
	 * @return
	 * @throws DuplicateException
	 * @throws ParseException
	 */
	public static boolean create(Faculty aFaculty) throws DuplicateException, ParseException, InvalidUserDataException
	{	
		aConnection = DatabaseConnect.initialize();
		UserDA.initialize(aConnection);
		initialize(aConnection);
		
		User aUser = new User();
		boolean inserted = false; //insertion success flag
		String sqlInsertFacultysDB = "";
		PreparedStatement psCreateUser = null;
		
		try {
			// bring the aFaculty attribute values from the passed parameter
			id = aFaculty.getId();
			password = aFaculty.getPassword();
			firstname = aFaculty.getFirstName();
			lastName = aFaculty.getLastName();
			emailAddress = aFaculty.getEmailAddress();
			lastAccess = aFaculty.getLastAccess();
			enrolDate = aFaculty.getEnrolDate();
			enabled = aFaculty.isEnabled();
			type = aFaculty.getType();
			schoolCode = aFaculty.getSchoolCode();
			schoolDescription = aFaculty.getSchoolDescription();
			office = aFaculty.getOffice();
			extension = aFaculty.getExtension();
			
			// build a new new User to ask UserDB to create a new User DB
			aUser = new User(id, password, firstname, lastName, emailAddress,
					lastAccess, enrolDate, enabled, type);
			
			// create the SQL insert statement for faculty DB
			sqlInsertFacultysDB = "INSERT INTO faculty " +
					   "(id, program_code , program_description, year) " +
					   "VALUES(?, ?, ?, ?, ?)";
			
		    // prepare to execute put it into Student DB. 
			psCreateUser = aConnection.prepareStatement(sqlInsertFacultysDB);
			
			psCreateUser.setLong(1, id);
			psCreateUser.setString(2, schoolCode);
			psCreateUser.setString(3, schoolDescription);
			psCreateUser.setString(4, office);
			psCreateUser.setInt(5, extension);
		
		}
		catch (InvalidUserDataException e ) {
			System.out.println(e);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
			// see if this Faculty already exists in the database
			retrieve(id);
			// when it is already in the DB
			throw (new DuplicateException("Problem with creating Faculty record, Faculty ID " + id +" already exists in the system."));
		}
//		 if NotFoundException, add Faculty to database(userDB and FacultyDB)
		catch(NotFoundException e)
		{
			try
 			{  // execute the SQL update statement(user and Faculty)
				

				
				aConnection.setAutoCommit(false);
				
				// try to create User DB, and if it is successful
				if(aUser.create())
				{

				
					// executeUpdate() returns how many row has created in the DB
					// so, if it is lager than zero, it created at least one row of data in the DB
					if(psCreateUser.executeUpdate() ==  0)
					{
						// when creating student DB fails, roll back the aUser creation(delete)
						aConnection.rollback();
					}
					
					// both DB creation has successful
					inserted = true;
					aConnection.commit();
				}
				else {
					aConnection.rollback();
				}
				
			}
			catch (SQLException ee)
				{ System.out.println(ee);	} 

		}
		
		// return if the creation was successful
		return inserted;
	}
	
				
	/**
	 * 	add only faculty DB, when the user type is changed
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean addFaculty(Long id) throws SQLException {
		
		boolean inserted = false;
		try {
			// connect DB
			aConnection = DatabaseConnect.initialize();
			FacultyDA.initialize(aConnection);
			
			String sqlInsertFacultysDB = "";
			PreparedStatement psUpdateUser = null;
			
			//create the SQL update statement using attribute values (Faculty DB)
			sqlInsertFacultysDB = "INSERT INTO faculty " +
					"(id, school_code , school_description, office, extension)" +
					"VALUES(?,?,?,?,?)";
		                
		    // prepare to execute put it into faculty DB. 
			psUpdateUser = aConnection.prepareStatement(sqlInsertFacultysDB);
			
			// variables fill in
			psUpdateUser.setLong(1, id);
			psUpdateUser.setString(2, Faculty.DEFAULT_SCHOOL_CODE);
			psUpdateUser.setString(3, Faculty.DEFAULT_SCHOOL_DESCRIPTION);
			psUpdateUser.setString(4, Faculty.DEFAULT_OFFICE);
			psUpdateUser.setInt(5, Faculty.DEFAULT_PHONE_EXTENSION);
	
			
			// executeUpdate() returns how many row has created in the DB
			if(psUpdateUser.executeUpdate() == 1 ) {
				StudentDA.removeStudent(id);
				inserted = true;
			}

			System.out.println("a Faculty Added: " + inserted);

		}
		catch(Exception e) {
			System.out.println("Error: "+e);
		}
        
		try{  Faculty.terminate(); }catch(Exception e){}
		try{  DatabaseConnect.terminate(); }catch(Exception e){}
        
        return inserted;
		
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean removeFaculty(Long id) throws SQLException {
		
		boolean deleted = false;
		try {
			// connect DB
			aConnection = DatabaseConnect.initialize();
			FacultyDA.initialize(aConnection);
			
			//create the SQL update statement using attribute values (Faculty DB)
			String sqlDeleteFacultyDB = "DELETE FROM faculty " + "WHERE id = ?";
			PreparedStatement psUpdateUser = aConnection.prepareStatement(sqlDeleteFacultyDB);
			
			// variables fill in
			psUpdateUser.setLong(1, id);
	
			// executeUpdate() returns how many row has created in the DB
			if(psUpdateUser.executeUpdate() == 1 ) {
				deleted = true;
			}
		
		}
		catch (Exception e) {
			System.out.println("Error: "+e);
		}
		
		try{  Faculty.terminate(); }catch(Exception e){}
		try{  DatabaseConnect.terminate(); }catch(Exception e){}
		
		return deleted;
	}
	

	
	/**
	 * update existing faculty
	 * @param aFaculty
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static int update(Faculty aFaculty) throws NotFoundException, ParseException, InvalidUserDataException
	{	
		User aUser = new User();
		String sqlInsertFacultysDB = "";
		int records = 0;  // record variable if there is a record in Users table
		PreparedStatement psUpdateUser = null;
		
		try {
			// retrieve the aFaculty argument attribute values
			id = aFaculty.getId();
			password = aFaculty.getPassword();
			firstname = aFaculty.getFirstName();
			lastName = aFaculty.getLastName();
			emailAddress = aFaculty.getEmailAddress();
			lastAccess = aFaculty.getLastAccess();
			enrolDate = aFaculty.getEnrolDate();
			enabled = aFaculty.isEnabled();
			type = aFaculty.getType();
			
			// create a new user to ask to update User
			aUser = new User(id,password,firstname,lastName,emailAddress,lastAccess,enrolDate,enabled,type);
		
	
			// variables for updating Faculty DB
			schoolCode = aFaculty.getSchoolCode();
			schoolDescription = aFaculty.getSchoolDescription();
			office = aFaculty.getOffice();
			extension = aFaculty.getExtension();
			
	
			//create the SQL update statement using attribute values (Faculty DB)
			sqlInsertFacultysDB = "Update faculty " +
		                "SET school_code = ? ," +
		                "school_description = ? ," +
		                "office = ? ," +
		                "extension = ? " +
		                "WHERE id = ? ";
			
		    // prepare to execute put it into faculty DB. 
			psUpdateUser = aConnection.prepareStatement(sqlInsertFacultysDB);
			
			// variables fill in
			psUpdateUser.setString(1, schoolCode);
			psUpdateUser.setString(2, schoolDescription);
			psUpdateUser.setString(3, office);
			psUpdateUser.setInt(4, extension);
			psUpdateUser.setLong(5, id);
			
		}
		catch (InvalidUserDataException e ) {
			System.out.println(e);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		// see if this customer exists in the database
		// NotFoundException is thrown by find method
		try
		{
			// is there a Faculty in the DB?
            Faculty.retrieve(id);  
            // if found, keep going
            
			aConnection = DatabaseConnect.initialize();
			UserDA.initialize(aConnection);
			initialize(aConnection);
			
			aConnection.setAutoCommit(false);
			// try to create User DB, and if it is successful
			if(aUser.update() == 1)
			{
				// executeUpdate() returns how many row has created in the DB
				if(psUpdateUser.executeUpdate() ==  0)
				{
					// when creating student DB fails, roll back the aUser creation(delete)
					aConnection.rollback();
				}
				
				// both DB creation has successful
				records = 1;
				aConnection.commit();
			}
			else {
				aConnection.rollback();
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
	 * delete the requested faculty
	 * @param aFaculty
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static int delete(Faculty aFaculty) throws NotFoundException, ParseException, InvalidUserDataException
	{	
		User aUser = new User();
		int records = 0;  // record variable if there is a record in Users table
		
		aConnection = DatabaseConnect.initialize();
		initialize(aConnection);
		UserDA.initialize(aConnection);
		
		try {
			// get the values from aStudent to build a User object
			aUser.setId(aFaculty.getId());
			aUser.setPassword(aFaculty.getPassword());
			aUser.setFirstName(aFaculty.getFirstName());
			aUser.setLastName(aFaculty.getLastName());
			aUser.setEmailAddress(aFaculty.getEmailAddress());
			aUser.setLastAccess(aFaculty.getLastAccess());
			aUser.setEnrolDate(aFaculty.getEnrolDate());
			aUser.setEnabled(aFaculty.isEnabled());
			aUser.setType(aFaculty.getType());
			
			Long id = aFaculty.getId();
			
			// sql statement                        
			String sqlDeleteFacultyDB = "DELETE FROM faculty " + "WHERE id = ?";
			
			System.out.println("Now I am about to delete ID: " + id);
			//determine if there is a User record to be deleted
			Faculty.retrieve(id);
			// if found, keep going
			System.out.println("I found the ID: " + id + " to delete in the Faculty");
			
			// prepare to execute put it into Student DB. 
			PreparedStatement psDeleteUser = aConnection.prepareStatement(sqlDeleteFacultyDB);
		
			psDeleteUser.setLong(1, id);
			
			// auto commit off for rolling back if fail
			aConnection.setAutoCommit(false);
			
			// try to delete student DB first(foreign Key constrain, student DB is deleted first before delete User DB)
			if(psDeleteUser.executeUpdate() ==  1)
			{
				System.out.println("Deletion successful on Student DB!! ");
				
				aConnection.commit();
				
				if(aUser.delete() == 1)
				{
					System.out.println("Deletion successful on User DB!! ");
					records = 1;
				}
				
				else {
					System.out.println("Deletion fail on User DB!! ");
					aConnection.rollback();
					System.out.println("Roll back DB!! ");
				}
			}
			else {
				System.out.println("Deletion fail on Faculty DB!! ");
				aConnection.rollback();
			}
			
 
		}catch(NotFoundException e)
		{
			throw new NotFoundException("User with ID number " + id 
					+ " cannot be updated, does not exist in the system.");
		}catch (SQLException e)
		{ 
			System.out.println(e);
	    }catch (Exception eg) {
			System.out.println(eg);
		}
		

		// return the number of updated users
		return records;
	}
	
	/**
	 * Authencate the faculty member from id and password
	 * @param id
	 * @param pw
	 * @return
	 * @throws NotFoundException
	 */
	public static Faculty authenticate(long id, String pw) throws NotFoundException{
		// when the login is not successful, return a null object
		Faculty aFaculty = null;
		// get the user's data from the DB 
		try {
			Faculty retrievedFaculty = retrieve(id);	
			
			// Convert input pw to SHA-1 hash hex code of String. (retrieved from: https://www.denismigol.com/posts/370/java-sha-1-hash-hex-string?i=1 )
		    MessageDigest md5 = MessageDigest.getInstance("SHA1");
		    byte[] digest = md5.digest(pw.getBytes("UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < digest.length; ++i) {
		        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
		    }
		    String hashedPw = sb.toString();
					
		    // compare the student ID and password
			if (hashedPw.equals(retrievedFaculty.getPassword())) {
				System.out.println("Login Successful!");
				
				// when the login successful, return student object
				aFaculty = retrievedFaculty;
			} else {
				// for debugging
				throw new NotFoundException("The input password is not match with ID: " + id);
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
		return aFaculty;
	
	}
}