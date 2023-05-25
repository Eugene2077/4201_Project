package webd4201.shine;

import java.math.BigInteger;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 12 2023
 */
public class User implements CollegeInterface{

	
	// constants ------------------------------------------
	/**
	 * @constant DEFAULT_ID
	 * @constant DEFAULT_PASSWORD
	 * @constant MINIMUM_PASSWORD_LENGTH
	 * @constant MAXIMUM_PASSWORD_LENGTH
	 * @constant DEFAULT_FIRST_NAME
	 * @constant DEFAULT_LAST_NAME
	 * @constant DEFAULT_EMAIL_ADDRESS
	 * @constant DEFAULT_ENABLED_STATUS : User's status
	 * @constant DEFAULT_TYPE : User's type
	 * @constant ID_NUMBER_LENGTH
	 * @constant DF : DateFormat.MEDIUM is a constant for medium style pattern
	 */ 
	public static final long DEFAULT_ID = 100123456L;
	public static final	String DEFAULT_PASSWORD = "password";
	public static final	byte MINIMUM_PASSWORD_LENGTH = 8;
	public static final byte MAXIMUM_PASSWORD_LENGTH = 40;
	public static final	String DEFAULT_FIRST_NAME = "John";
	public static final	String DEFAULT_LAST_NAME = "Doe";
	public static final	String DEFAULT_EMAIL_ADDRESS = "john.doe@dcmail.com";
	public static final	boolean DEFAULT_ENABLED_STATUS = true;
	public static final char DEFAULT_TYPE = 's';
	public static final	byte ID_NUMBER_LENGTH = 9;
	// reference: https://www.tutorialspoint.com/format-date-with-dateformat-medium-in-java
	public static final DateFormat DF = DateFormat.getDateInstance(DateFormat. MEDIUM, Locale.CANADA);

	/**
	 * @Attribute id : User's Id
	 * @Attribute password
	 * @Attribute firstName
	 * @Attribute lastName
	 * @Attribute emailAddress
	 * @Attribute lastAccess : the last access date
	 * @Attribute enrolDate  : whether the user is enabled in the system (false would mean the user is disabled)
	 * @Attribute type : store the user�s type
	 */
	private long id;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress; 
	private Date lastAccess;
	private Date enrolDate;
	private boolean	enabled;
	private char type;
	
	
	// getters and setters ------------------------------------

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * @throws InvalidIdException
	 * @throws InvalidUserDataException
	 */
	public void setId(long id) throws InvalidIdException {
		if(verifyId(id)){
			this.id = id;
		}
		else {
			throw new InvalidIdException("ID has to be " + ID_NUMBER_LENGTH +" digits");
		}
//      catch block has moved to the constructor
//		catch (InvalidIdException ex) {
//			ex.printStackTrace();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

    /**
     * 
     * @param password
     * @throws InvalidPasswordException
     */
	public void setPassword(String password) throws InvalidPasswordException {
		if(veryfyPw(password)) {
			this.password = password;
		}
		else {	
			throw new InvalidPasswordException("Password has to be between " + MINIMUM_PASSWORD_LENGTH + "and" + MAXIMUM_PASSWORD_LENGTH +" digits");
		}
		
	}
	
	
	

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

    /**
     * 
     * @param firstName
     * @throws InvalidNameException
     */
	public void setFirstName (String firstName) throws InvalidNameException {
		try {
		if(verifyName(firstName, "The first name")) {
			this.firstName = firstName;
		}
		}
		catch(InvalidNameException exName) {
				throw new InvalidNameException(exName.getMessage());
			}

	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
    /**
     * 
     * @param lastName
     * @throws InvalidNameException
     */
	public void setLastName(String lastName) throws InvalidNameException {
		try {
		if(verifyName(lastName, "The last name")) {
			this.lastName = lastName;
		}
		}
		catch(InvalidNameException exName) {
			throw new InvalidNameException(exName.getMessage());
		}

	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * 
	 * @param emailAddress
	 * @throws InvalidUserDataException
	 */
	public void setEmailAddress(String emailAddress) {
		
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the lastAccess
	 */
	public Date getLastAccess() {
		return lastAccess;
	}

    /**
     * 
     * @param lastAccess
     * @throws InvalidUserDataException
     */
	public void setLastAccess(Date lastAccess)  {
		this.lastAccess = lastAccess;
	}

	/**
	 * @return the enrolDate
	 */
	public Date getEnrolDate() {
		return enrolDate;
	}

	/**
	 * 
	 * @param enrolDate
	 * @throws InvalidUserDataException
	 */
	public void setEnrolDate(Date enrolDate) {
		this.enrolDate = enrolDate;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * @param enabled
	 * @throws InvalidUserDataException
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 * @throws InvalidUserDataException
	 */
	public void setType(char type) {
		this.type = type;
	}


	// constructor(default) -----------------------------------------------
	// new Date() returns today's date, refer to (https://www.w3schools.com/js/js_dates.asp)
	/**
	 * default constructor
	 */
	public User() throws InvalidUserDataException {
		this(DEFAULT_ID, DEFAULT_PASSWORD, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_EMAIL_ADDRESS, new Date(), new Date(), DEFAULT_ENABLED_STATUS, DEFAULT_TYPE);
	};
	
	
	// constructor(parameterized) -------------------------------------------
	/**
	 * @overload // this constructor is overloading the default constructor
	 * @param id
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param lastAccess: the last access date in the system
	 * @param enrolDate : the enrolled date in the system
	 * @param enabled   : the user is enabled or not
	 * @param type      : the type of the user
	 */
	public User(long id, String password, String firstName, String lastName, String emailAddress, Date lastAccess, Date enrolDate, boolean	enabled, char type) throws InvalidUserDataException {
		try {
		setId(id);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setEmailAddress(emailAddress);
		setLastAccess(lastAccess);
		setEnrolDate(enrolDate);
		setEnabled(enabled);
		setType(type);
		} 
		catch(InvalidIdException exId) {
			throw new InvalidUserDataException(exId.getMessage());
		}
		catch(InvalidNameException exName) {
			throw new InvalidUserDataException(exName.getMessage());
		}
		catch(InvalidPasswordException exPassword) {
			throw new InvalidUserDataException(exPassword.getMessage());
		}
	}
	
	
	
	// Methods  -------------------------------------------------------

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// date format change
		SimpleDateFormat DateFor = new SimpleDateFormat("dd-MMM-yyyy");
		String enrolDate = DateFor.format(getEnrolDate());
		String lastAccess = DateFor.format(getLastAccess());
		// return formatted output
		return "User Info for: " + getId() + 
				"\n\tName: " + getFirstName() + " " + getLastName() + " (" + getEmailAddress() + ")" +
				"\n\tCreated on: " + enrolDate +
				"\n\tLast access: " + lastAccess;
	}

	/**
	 * displays the returned string from the toString() method 
	 */
    public void dump() throws InvalidUserDataException {
    	System.out.println(toString());
    }
	
    /**
     * check if the id is valid or not
     * @param id
     * @return
     */
    public static boolean verifyId(long id) {
    	if(id >= getRangeOfNumber("min") && id <= getRangeOfNumber("max")){
    		return true;}
    	else{
    		System.out.println("ID has to be " + ID_NUMBER_LENGTH +" digits");
    		return false;}
    }
    

   /**
    * verify password length
    * @param pw
    * @return
    * @throws InvalidPasswordException
    */
    public static boolean veryfyPw(String pw) throws InvalidPasswordException {
		int length = pw.length();
		Boolean retVal = true;
		if(length < MINIMUM_PASSWORD_LENGTH) {
			retVal = false;
			throw new InvalidPasswordException("The password has to be minimum length of " + MINIMUM_PASSWORD_LENGTH +" digits");
		}
		else if(length > MAXIMUM_PASSWORD_LENGTH) {
			retVal = false;
			throw new InvalidPasswordException("The password has to be maximum length of " + MAXIMUM_PASSWORD_LENGTH +" digits");
		}
		else {
			return retVal;
		}
    }
    
    /**
     * it returns the maximum and minimum ranges from the ID_NUMBER_LENGTH
     * @param maxOrMin
     * @return
     */
    public static long getRangeOfNumber (String maxOrMin) {
    	BigInteger maxDigit = BigInteger.TEN.pow(ID_NUMBER_LENGTH).subtract(BigInteger.ONE);
    	BigInteger minDigit = BigInteger.TEN.pow(ID_NUMBER_LENGTH-1);
    	if(maxOrMin == "max") return maxDigit.longValue();
    	else if(maxOrMin == "min") return minDigit.longValue();
    	else return 0;
    }
    
    /**
     * 
     * @param name
     * @param exMessgeHeader
     * @return
     * @throws InvalidNameException
     */
    public static boolean verifyName(String name, String exMessgeHeader) throws InvalidNameException {
    	boolean retVal = false;
		if (name.trim().isEmpty()) {
			throw new InvalidNameException(exMessgeHeader + " cannot be empty");
		}else if (name.matches("-?\\d+(\\.\\d+)?")){
			throw new InvalidNameException(exMessgeHeader + " cannot be number");
		}else {
			retVal = true;
		}
		return retVal;
    }
    
    /**
     * email validation
     * @param email
     * @return
     */
    public static boolean verifyEmailAddress(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
   
    
    /**
     * email validation: using javax.mail-1.5.5.jar
     * @param email
     * @return
     */
//    public static boolean isValidEmailAddress(String email){
//    	boolean result = true;
//    	try
//    	{
//	    	InternetAddress emailAddr = new InternetAddress(email);
//	    	emailAddr.validate();
//    	} catch (AddressException  ex) {
//    		result = false;
//    	}
//		return result;
//	}

    
    /**
     * Currently empty method, will use it later
     */
    public String getTypeForDisplay() {
    	return " ";
    }
    
    
    
	/**
	 * initialize connection
	 * @param c
	 */
	public static void initialize(Connection c){
		UserDA.initialize(c);
		}
	/**
	 * Terminate the connection
	 */
	public static void terminate(){
		UserDA.terminate();
		}
	
	/**
	 * retrieving the student object
	 * @param id
	 * @return Student object
	 * @throws NotFoundException
	 * @throws ParseException
	 */
	public static User retrieve(long id) throws NotFoundException, ParseException {
		return UserDA.retrieve(id);
	}
	
	/**
	 * Create a student in the DB(user and student DB)
	 * @throws DuplicateException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public boolean create() throws DuplicateException, ParseException, InvalidUserDataException{
		return UserDA.create(this);
	}
	
	/**
	 * Delete the user and student from the DBs
	 * @throws NotFoundException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public int delete() throws NotFoundException, ParseException, InvalidUserDataException{
		return UserDA.delete(this);
		}
	
	/**
	 * update the DBs information 
	 * @throws NotFoundException
	 * @throws ParseException
	 * @throws InvalidUserDataException 
	 */
	public int update() throws NotFoundException, ParseException, InvalidUserDataException{
		return UserDA.update(this);
		}
	
	
	public static User authenticate(long userID, String pw) throws NotFoundException{
		return UserDA.authenticate(userID, pw);
		}
    
    
    
    
	
}






