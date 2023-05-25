package webd4201.shine;

/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

@SuppressWarnings(value = "serial")
public class InvalidIdException extends Exception {
	/**
	 * if there is a version of Id exceptions, 1L is a null exception(not showing)
	 * we can use one of this or @SuppressWarnings(value = "serial")
	 */
	// private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public InvalidIdException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public InvalidIdException(String message) {
		super(message);
	}
}
