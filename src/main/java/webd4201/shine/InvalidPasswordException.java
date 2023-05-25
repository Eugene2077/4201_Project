package webd4201.shine;
/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */
@SuppressWarnings(value = "serial")
public class InvalidPasswordException extends Exception {

	/**
	 * 
	 */
	public InvalidPasswordException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public InvalidPasswordException(String message) {
		super(message);
	}

}


