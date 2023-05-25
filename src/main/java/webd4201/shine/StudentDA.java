package webd4201.shine;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 12 2023
 */

import java.sql.*;
import java.text.ParseException;
import java.util.Date;



public class StudentDA {
	
	/**
	 * declare constant and a student object
	 */

	public static Student aStudent;
	
	/**
	 * declare connection variables
	 */
	private static Connection aConnection;
	private static Statement userStatement;
	private static Statement studentStatement;
	
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
	static String programCode;
	static String programDescription;
	static int year;

	/**
	 * establish connection
	 * @param conn
	 */
	public static void initialize(Connection conn) {
		try {
			aConnection = conn;
			userStatement = aConnection.createStatement();
			studentStatement = aConnection.createStatement();
		}
		catch (SQLException sqle) { System.out.println(sqle);}
	}
	
	/**
	 * close the database statement and hence connection
	 */
	public static void terminate() {
		 try { 	// close the statement
			 userStatement.close();
			 studentStatement.close();
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
	 * Retrieve the student from DB
	 * @param key
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static Student retrieve(long key) throws NotFoundException, ParseException
	{ // retrieve student and Boat data
		aStudent = null;

		try
 		{
			// prepared statement get ready 
			// retrieve music AND media info by using SQL JOIN / where you wanna use parameter, use '?'

			String prepareSqlStudent = "SELECT* FROM students WHERE id = ?" ;
			PreparedStatement myStmtStudent = aConnection.prepareStatement(prepareSqlStudent);
			
			// set parameter(s) to the argument from the caller('key')/ 1 means first parameter, in this case only one parameter
			

			myStmtStudent.setLong(1, key);
			
			// execution with the parameter(key)

            ResultSet rsStudent = myStmtStudent.executeQuery();
            
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
        		
            
    		// Get the data from students DB
    		boolean gotit = rsStudent.next();
    		if(gotit) {
    		programCode = rsStudent.getString("program_code");
    		programDescription = rsStudent.getString("program_description");
    		year = rsStudent.getInt("year");
    		}
    		else {
    			throw new NotFoundException("Student with id of  " + key +" not found in the Students database.");
    		}
    		
		// create a Student
            try{
            	aStudent = new Student(id,password,firstname,lastName,emailAddress,lastAccess,enrolDate,enabled,type,programCode,programDescription,year);
            }
            catch (InvalidUserDataException e) { 
            	System.out.println("Record for " + firstname + lastName + " contains an invalid ID number.  Verify and correct the information.");
            }
                
            rsStudent.close();
                             
	   	}catch (SQLException e)
		{ System.out.println(e);}
                
		return aStudent;
	}
	
	/**
	 * create a student in the DB
	 * @param aStudent
	 * @return
	 * @throws DuplicateException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public static boolean create(Student aStudent) throws DuplicateException, ParseException, InvalidUserDataException
	{	
		
		// connect DB
		aConnection = DatabaseConnect.initialize();
		UserDA.initialize(aConnection);
		StudentDA.initialize(aConnection);
		
		User aUser = new User();
		boolean inserted = false; //insertion success flag
		String sqlInsertStudentsDB = "";
		PreparedStatement psCreateUser = null;
		
		try {
			// bring the aStudent attribute values from the passed parameter
			id = aStudent.getId();
			password = aStudent.getPassword();
			firstname = aStudent.getFirstName();
			lastName = aStudent.getLastName();
			emailAddress = aStudent.getEmailAddress();
			lastAccess = aStudent.getLastAccess();
			enrolDate = aStudent.getEnrolDate();
			enabled = aStudent.isEnabled();
			type = aStudent.getType();
			programCode = aStudent.getProgramCode();
			programDescription = aStudent.getProgramDescription();
			year = aStudent.getYear();
			
			// build a new new User to ask UserDB to create a new User DB
			aUser = new User(id, password, firstname, lastName, emailAddress,
					lastAccess, enrolDate, enabled, type);
			
			// create a sql for creation in the students DB
			sqlInsertStudentsDB = "INSERT INTO students " +
					   "(id, program_code , program_description, year) " +
					   "VALUES(?, ?, ?, ?)";
			
		    // prepare to execute put it into Student DB. 
			psCreateUser = aConnection.prepareStatement(sqlInsertStudentsDB);
			
			// variables fill in
			
			psCreateUser.setLong(1, id);
			psCreateUser.setString(2, programCode);
			psCreateUser.setString(3, programDescription);
			psCreateUser.setInt(4, year);
			
			

			// create a User and execute the SQL update statement(student)
			
		}
		catch (InvalidUserDataException e ) {
			System.out.println(e);
		}
		catch (SQLException ee)
		{ System.out.println(ee);	} 
		
		try
		{;
			// see if this student already exists in the database
			retrieve(id);
			// if NOT found the matched User(and student) from the DB, skip below line
			throw (new DuplicateException("Problem with creating Student record, Student ID " + id +" already exists in the system."));
		}
//		 if NotFoundException, add student to database(userDB and studentDB)
		catch(NotFoundException e)
		{
			try
 			{  // create a User and execute the SQL update statement(student)
				
				// auto commit off for rolling back if fail
				aConnection.setAutoCommit(false);
				
				// try to create User DB, and if it is successful
				if(aUser.create())
				{
					System.out.println("Creation successful on User DB!! ");
					
					// executeUpdate() returns how many row has created in the DB
					if(psCreateUser.executeUpdate() ==  1)
					{
						System.out.println("Creation successful on Student DB!! ");
						inserted = true;
						aConnection.commit();
					}
					else {
						System.out.println("Creation fail on Student DB!! ");
						// when creating student DB fails, roll back the aUser creation(delete)
						System.out.println("Roll back User DB!! ");
						aConnection.rollback();
					}
				}
				else {
					System.out.println("Creation fail on User DB!! ");
					aConnection.rollback();
					System.out.println("Roll back User DB!! ");
				}
					
 			}

			catch (SQLException ee)
				{ System.out.println(ee);	} 

		}
        try{  User.terminate(); }catch(Exception e){}
        try{  Student.terminate(); }catch(Exception e){}
        try{  DatabaseConnect.terminate(); }catch(Exception e){}
		// return if the creation was successful
		return inserted;
	}
	
	/**
	 * add only student DB, when the user type is changed
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean addStudent(Long id) throws SQLException {
		
		boolean inserted = false;
		try {
			// connect DB
			aConnection = DatabaseConnect.initialize();
			StudentDA.initialize(aConnection);
			
			String sqlInsertStudentsDB = "";
			PreparedStatement psCreateUser = null;
			// create a sql for creation in the students DB
			sqlInsertStudentsDB = "INSERT INTO students " +
					   "(id, program_code , program_description, year) " +
					   "VALUES(?, ?, ?, ?)";
			
		    // prepare to execute put it into Student DB. 
			psCreateUser = aConnection.prepareStatement(sqlInsertStudentsDB);
			
			// variables fill in
		
			psCreateUser.setLong(1, id);
			psCreateUser.setString(2, Student.DEFAULT_PROGRAM_CODE);
			psCreateUser.setString(3, Student.DEFAULT_PROGRAM_DESCRIPTION);
			psCreateUser.setInt(4, Student.DEFAULT_YEAR);
			
			// executeUpdate() returns how many row has created in the DB
			if(psCreateUser.executeUpdate() == 1 ) {
				// if it is successful, delete faculty record
				FacultyDA.removeFaculty(id);
				inserted = true;
			}
			
			System.out.println("a Student Added: " + inserted);

		}
		catch(Exception e) {
			System.out.println("Error: "+e);
		}
        
		try{  User.terminate(); }catch(Exception e){}
		try{  Student.terminate(); }catch(Exception e){}
		try{  DatabaseConnect.terminate(); }catch(Exception e){}
        
        return inserted;
		
		}
	
	
	
	public static boolean removeStudent(Long id) throws SQLException {
		
	
		boolean deleted = false;
		try {
			// connect DB
			aConnection = DatabaseConnect.initialize();
			StudentDA.initialize(aConnection);
			
			//create the SQL update statement using attribute values (Faculty DB)
			String sqlDeleteFacultyDB = "DELETE FROM students " + "WHERE id = ?";
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
		try{  DatabaseConnect.terminate(); }catch(Exception  e){}
		
		return deleted;
	}
	
	
	
	/**
	 * update the student info from the DB
	 * @param aStudent
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public static int update(Student aStudent) throws NotFoundException, ParseException, InvalidUserDataException
	{	
		User aUser = new User();
		String sqlInsertStudentsDB = "";
		int records = 0;  // record variable if there is a record in Users table
		PreparedStatement psUpdateUser = null;
		
		try {
			// get the values from aStudent to build a User object
			id = aStudent.getId();
			password = aStudent.getPassword();
			firstname = aStudent.getFirstName();
			lastName = aStudent.getLastName();
			emailAddress = aStudent.getEmailAddress();
			lastAccess = aStudent.getLastAccess();
			enrolDate = aStudent.getEnrolDate();
			enabled = aStudent.isEnabled();
			type = aStudent.getType();
			
			// create a new user
			aUser = new User(id,password,firstname,lastName,emailAddress,lastAccess,enrolDate,enabled,type);
			
			
			// variables for updating Student DB
			programCode = aStudent.getProgramCode();
			programDescription = aStudent.getProgramDescription();
			year = aStudent.getYear();
			
	                                  
			//create the SQL update statement using attribute values (Student DB)
			sqlInsertStudentsDB = "Update students SET " +
                    "program_code = ? ," +
                    "program_description = ? ," +
                    "year = ? " + 
                    "WHERE id = ?";
			
		    // prepare to execute put it into Student DB. 
			psUpdateUser = aConnection.prepareStatement(sqlInsertStudentsDB);
			
			// variables fill in
			psUpdateUser.setString(1, programCode);
			psUpdateUser.setString(2, programDescription);
			psUpdateUser.setInt(3, year);			
			psUpdateUser.setLong(4, id);
				
			
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
			//determine if there is a User record to be updated
			Student.retrieve(id);  //determine if there is a User record to be updated
			// if found, keep going

			
			aConnection = DatabaseConnect.initialize();
			UserDA.initialize(aConnection);
			StudentDA.initialize(aConnection);
			
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
	 * delete the requested student from DB
	 * @param aStudent
	 * @return
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static int delete(Student aStudent) throws NotFoundException, ParseException, InvalidUserDataException
	{	
		User aUser = new User();
		int records = 0;  // record variable if there is a record in Users table

		aConnection = DatabaseConnect.initialize();
		initialize(aConnection);
		UserDA.initialize(aConnection);
		
		try {
			// get the values from aStudent and set aUser attributes
			aUser.setId(aStudent.getId());
			aUser.setPassword(aStudent.getPassword());
			aUser.setFirstName(aStudent.getFirstName());
			aUser.setLastName(aStudent.getLastName());
			aUser.setEmailAddress(aStudent.getEmailAddress());
			aUser.setLastAccess(aStudent.getLastAccess());
			aUser.setEnrolDate(aStudent.getEnrolDate());
			aUser.setEnabled(aStudent.isEnabled());
			aUser.setType(aStudent.getType());
			
			Long id = aStudent.getId();
		
			// sql statement                        
			String sqlDeleteStudentsDB = "DELETE FROM students " + "WHERE id = ?";
			
			System.out.println("Now I am about to delete ID: " + id);
			//determine if there is a User record to be deleted
			Student.retrieve(id);  
			// if found, keep going
			System.out.println("I found the ID: " + id + " to delete in the Student");
			
			
			// prepare to execute put it into Student DB. 
			PreparedStatement psDeleteUser = aConnection.prepareStatement(sqlDeleteStudentsDB);
			     
			psDeleteUser.setLong(1, id);
			
			// auto commit off for rolling back if fail
			aConnection.setAutoCommit(false);

			if(psDeleteUser.executeUpdate() == 1)
			{
				System.out.println("Deletion successful on Student DB!! ");
				// try to delete User in User's DB / if unsuccessful
				aConnection.commit();
		
				if(aUser.delete() == 1)
				{
					System.out.println("Deletion successful on User DB!! ");
					records = 1;
				}

				else{
					System.out.println("Deletion fail on User DB!! ");
					aConnection.rollback();
					System.out.println("Roll back DB!! ");
				}
				
			}
			else {
				System.out.println("Deletion fail on Student DB!! ");
				aConnection.rollback();
			}

		} 
		catch(NotFoundException e)
		{
			throw new NotFoundException("User with ID number " + id 
					+ " cannot be updated, does not exist in the system.");
		}catch (SQLException e){ 
			System.out.println(e);
		}catch (Exception eg) {
			System.out.println(eg);
		}
		
		// return the number of updated users
		return records;
	}
		

	
    /**
     * authenticate user
     * @param studentNumber
     * @param password
     * @return
     * @throws UnsupportedEncodingException 
     */
	public static Student authenticate(long studentNum, String pw) throws NotFoundException{
		// when the login is not successful, return a null object
		Student aStudent = null;
		// get the user's data from the DB 
		try {
			Student retrievedStudent = StudentDA.retrieve(studentNum);	
			
			// Convert input pw to SHA-1 hash hex code of String. (retrieved from: https://www.denismigol.com/posts/370/java-sha-1-hash-hex-string?i=1 )
		    MessageDigest md5 = MessageDigest.getInstance("SHA1");
		    byte[] digest = md5.digest(pw.getBytes("UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < digest.length; ++i) {
		        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
		    }
		    String hashedPw = sb.toString();
					
		    // compare the student ID and password
			if (hashedPw.equals(retrievedStudent.getPassword())) {
				System.out.println("Login Successful!");
				
				// when the login successful, return student object
				aStudent = retrievedStudent;
			} else {
				// for debugging
				throw new NotFoundException("The input password is not match with ID: " + studentNum);
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
		return aStudent;
	
	}
	
	
}
	
	




























	
	
	

