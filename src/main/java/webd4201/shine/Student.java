package webd4201.shine;

/**
 * 
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;



public class Student extends User{
	/**
	 * @Constant DEFAULT_PROGRAM_CODE : program code
	 * @Constant DEFAULT_PROGRAM_DESCRIPTION : about the program
	 * @Constant DEFAULT_YEAR : the student's year
	 */
	public static final String DEFAULT_PROGRAM_CODE = "UNDC";
	public static final String DEFAULT_PROGRAM_DESCRIPTION = "Undeclared";
	public static final int DEFAULT_YEAR = 1;
	
	/**
	 * @Attrubute programCode
	 * @Attrubute programDescription
	 * @Attrubute year
	 * @Attrubute marks
	 */
	private String programCode;
	private String programDescription;
	private int year;
	private Vector<Mark> marks;
	
	
	// getters and setters -------------------------------------------
	/**
	 * @return the programCode
	 */
	public String getProgramCode() {
		return programCode;
	}
	/**
	 * 
	 * @param programCode
	 * @throws InvalidUserDataException
	 */
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	/**
	 * @return the programDescription
	 */
	public String getProgramDescription() {
		return programDescription;
	}
	/**
	 * 
	 * @param programDescription
	 * @throws InvalidUserDataException
	 */
	public void setProgramDescription(String programDescription)  {
		this.programDescription = programDescription;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * 
	 * @param year
	 * @throws InvalidUserDataException
	 */
	public void setYear(int year)  {
		this.year = year;
	}
	/**
	 * @return the marks
	 */
	public Vector<Mark> getMarks() {
		return marks;
	}
	/**
	 * 
	 * @param marks
	 * @throws InvalidUserDataException
	 */
	public void setMarks(Vector<Mark> marks) {
		this.marks = marks;
	}
	
	
	
	// constructor(parameterized) -----------------------------------------
	/**
	 * @Override
	 * @param id
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param lastAccess
	 * @param enrolDate
	 * @param enabled
	 * @param type
	 */
	public Student(long id, String password, String firstName, String lastName,
			String emailAddress, Date lastAccess, Date enrolDate,
			boolean enabled, char type, String code, String description, int year, Vector<Mark> marks) throws InvalidUserDataException{
		
		super(id, password, firstName, lastName, emailAddress, lastAccess, enrolDate,
				enabled, type);
			setProgramCode(code);
			setProgramDescription(description);
			setYear(year);
			setMarks(marks);
		}
	

	// constructor(parameterized override) -----------------------------------------
	// this overloaded constructor takes arguments for all attributes for a Student object EXCEPT for a Vector of Mark objects.
	// This constructor should call the above constructor using the this keyword, sending all of its arguments PLUS an empty Vector of Mark objects.
	/**
	 * @Override
	 * @param id
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param lastAccess
	 * @param enrolDate
	 * @param enabled
	 * @param type
	 */
	public Student(long id, String password, String firstName, String lastName,
			String emailAddress, Date lastAccess, Date enrolDate,
			boolean enabled, char type, String code, String description, int year)throws InvalidUserDataException {
		
		this(id, password, firstName, lastName, emailAddress, lastAccess, enrolDate,
				enabled, type, code, description, year, new Vector<Mark>());
		
	}
	// at here, teacher wrote as vector<int>Mark  , he says Mark is int... 

	
	
	// constructor(default) -----------------------------------------------
	/**
	 * default constructor
	 */
	public Student() throws InvalidUserDataException {
		this(DEFAULT_ID, DEFAULT_PASSWORD, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, 
			DEFAULT_EMAIL_ADDRESS, new Date(), new Date(), DEFAULT_ENABLED_STATUS, 
			DEFAULT_TYPE, DEFAULT_PROGRAM_CODE, DEFAULT_PROGRAM_DESCRIPTION, DEFAULT_YEAR);		
	}
	
	
	
	
	
	// Methods  -------------------------------------------------------

	/* Override toString method for formatted output
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// date format change
		SimpleDateFormat DateFor = new SimpleDateFormat("dd-MMM-yyyy");
		String stringDate= DateFor.format(getEnrolDate());
		String mark = "";
		// if there is no mark in the vector Marks
		if(marks.isEmpty()) {
			mark = "No marks on record";
		}
		// if there is(are) marks in the vector Marks
		else {
			// get an iterator
			Iterator<Mark> iterator = marks.iterator();
			// iterate and add to the string 'mark'
			mark += "Mark:\n\t";
			while( iterator.hasNext()){
				mark += iterator.next() + "\n\t";
			}
		}
		
		return "Student Info for: " +
				"\n\t" + getFirstName() + " " + getLastName() + " (" + getId() + ")" +
				"\n\tCurrently in " + getYear() + getDayNumberSuffix(getYear()) + " year of \"" + getProgramDescription() + "\" (" + getProgramCode() + ")" +
				"\n\tat " + COLLEGE_NAME  +
				"\n\tEnrolled: " + stringDate +
				"\n\t" + mark;
	}
	
	/**
	 * get the suffix string for the year
	 * @param year
	 * @return
	 */
	private String getDayNumberSuffix(int year) {
		    switch (year) {
		    case 1:
		      return "st";
		    case 2:
		      return "nd";
		    case 3:
		      return "rd";
		    default:
		      return "th";
		    }
		  }
	
	/**
	 * initialize connection
	 * @param c
	 */
	public static void initialize(Connection c){
		StudentDA.initialize(c);
		}
	/**
	 * Terminate the connection
	 */
	public static void terminate(){
		StudentDA.terminate();
		}
	
	/**
	 * retrieving the student object
	 * @param id
	 * @return Student object
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static Student retrieve(long id) throws NotFoundException, ParseException {
		return StudentDA.retrieve(id);
	}
	
	/**
	 * Create a student in the DB(user and student DB)
	 * @throws DuplicateException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public boolean create() throws DuplicateException, ParseException, InvalidUserDataException{
		return StudentDA.create(this);
	}
	
	/**
	 * Delete the user and student from the DBs
	 * @throws NotFoundException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public int delete() throws NotFoundException, ParseException, InvalidUserDataException{
		return StudentDA.delete(this);
		}
	
	/**
	 * update the DBs information 
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public int update() throws NotFoundException, ParseException, InvalidUserDataException {
		return StudentDA.update(this);
		}
	
	
	/**
	 * Authenticate a Student from id and password
	 * @param studentNum
	 * @param pw
	 * @return
	 * @throws NotFoundException
	 */
	public static Student authenticate(long studentNum, String pw) throws NotFoundException{
		return StudentDA.authenticate(studentNum, pw);
		}

}




