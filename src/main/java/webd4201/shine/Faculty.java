
package webd4201.shine;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;


/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */
public class Faculty extends User{

    /**
     * @Constant DEFAULT_SCHOOL_CODE
     * @Constant DEFAULT_SCHOOL_DESCRIPTION
     * @Constant DEFAULT_OFFICE
     * @Constant DEFAULT_PHONE_EXTENSION
     */
	public static final String DEFAULT_SCHOOL_CODE = "SET";
	public static final String DEFAULT_SCHOOL_DESCRIPTION = "School of Engineering & Technology";
	public static final String DEFAULT_OFFICE = "H-140";
	public static final int DEFAULT_PHONE_EXTENSION = 1234;

    /**
     * @Attribute schoolCode
     * @Attribute schoolDescription
     * @Attribute office
     * @Attribute extension
     */
	private String schoolCode;
	private String schoolDescription;
	private String office;
	private int extension;
	
	
	// getters and setters -------------------------------------------
	/**
	 * @return the schoolCode
	 */
	public String getSchoolCode() {
		return schoolCode;
	}
    /**
     * 
     * @param schoolCode
     * @throws InvalidUserDataException
     */
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	/**
	 * @return the schoolDescription
	 */
	public String getSchoolDescription() {
		return schoolDescription;
	}
    /**
     * 
     * @param schoolDescription
     * @throws InvalidUserDataException
     */
	public void setSchoolDescription(String schoolDescription) {
		this.schoolDescription = schoolDescription;
	}
	/**
	 * @return the office
	 */
	public String getOffice() {
		return office;
	}
    /**
     * 
     * @param office
     * @throws InvalidUserDataException
     */
	public void setOffice(String office) {
		this.office = office;
	}
	/**
	 * @return the extension
	 */
	public int getExtension() {
		return extension;
	}
    /**
     * 
     * @param extension
     * @throws InvalidUserDataException
     */
	public void setExtension(int extension) {
		this.extension = extension;
	}
	
	// constructor(default) -----------------------------------------------
	/**
	 * default constructor
	 */
	public Faculty() throws InvalidUserDataException {
		super();
		setSchoolCode(DEFAULT_SCHOOL_CODE);
		setSchoolDescription(DEFAULT_SCHOOL_DESCRIPTION);
		setOffice(DEFAULT_OFFICE);
		setExtension(DEFAULT_PHONE_EXTENSION);
	};
	
	// constructor(parameterized) -----------------------------------------
	/**
	 * @overload
	 * @param id
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param lastAccess
	 * @param enrolDate
	 * @param enabled
	 * @param type
	 * @param schoolCode
	 * @param schoolDescription
	 * @param office
	 * @param extension
	 */
	public Faculty(long id, String password, String firstName, String lastName,
			String emailAddress, Date lastAccess, Date enrolDate,
			boolean enabled, char type, String schoolCode,
			String schoolDescription, String office, int extension) throws InvalidUserDataException {
		
		super(id, password, firstName, lastName, emailAddress, lastAccess,
				enrolDate, enabled, type);
		setSchoolCode(schoolCode);
		setSchoolDescription(schoolDescription);
		setOffice(office);
		setExtension(extension);

	}
	
	
	// Methods  -------------------------------------------------------
	
	/**
	 * Implements the abstract getTypeForDisplay() method 
	 * simply returns the word �Faculty�
	 */
	public String getTypeForDisplay() {
		return "Faculty";
	}
	
	/**
	 * Override method from the parent class User
	 */
	@Override
	public String toString() {
		String generalInfo = super.toString();
		String output = generalInfo + 
				"\n\tSchool Description: " + getSchoolDescription() +
				"\n\tOffece: " + getOffice() +
				"\n\tTelephone: " + PHONE_NUMBER + " x" + getExtension(); 
		output = output.replaceAll("User", getTypeForDisplay());
		return output;
		
	}
	
	/**
	 * initialize connection
	 * @param c
	 */
	public static void initialize(Connection c){
		FacultyDA.initialize(c);
		}
	/**
	 * Terminate the connection
	 */
	public static void terminate(){
		FacultyDA.terminate();
		}
	
	/**
	 * retrieving the student object
	 * @param id
	 * @return Student object
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static Faculty retrieve(long id) throws NotFoundException, ParseException {
		return FacultyDA.retrieve(id);
	}
	
	/**
	 * Create a student in the DB(user and student DB)
	 * @throws DuplicateException
	 * @throws ParseException
	 */
	public boolean create() throws DuplicateException, ParseException, InvalidUserDataException{
		return FacultyDA.create(this);
	}
	
	/**
	 * Delete the user and student from the DBs
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public int delete() throws NotFoundException, ParseException, InvalidUserDataException{
		return FacultyDA.delete(this);
		}
	
	/**
	 * update the DBs information 
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public int update() throws NotFoundException, ParseException, InvalidUserDataException{
		return FacultyDA.update(this);
		}
	
	/**
	 * Authenticate a faculty member from id and password
	 * @param id
	 * @param pw
	 * @return
	 * @throws NotFoundException
	 */
	public static Faculty authenticate(long id, String pw) throws NotFoundException{
		return FacultyDA.authenticate(id, pw);
		}

	
	
}
