package webd4201.shine;
/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */
@SuppressWarnings(value = "serial")
public class InvalidNameException extends Exception {

	/**
	 * 
	 */
	public InvalidNameException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public InvalidNameException(String message) {
		super(message);
	}
}
