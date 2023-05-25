package webd4201.shine;

/**
 * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

public interface CollegeInterface {
	
	/**
	 * Name of the college
	 * it is a constant and has to be a static.
	 */
	public static final String COLLEGE_NAME = "Durham College";
	
	/**
	 * Name of the phone number
	 */
	public static final String PHONE_NUMBER  = "(905)721-2000";
	
	/**
	 * This method should be set up so that it is accessible anywhere and an instance method (not a class method).
	 * An abstract method getTypeForDisplay()
	 */
	public abstract String getTypeForDisplay(); 
	
}
