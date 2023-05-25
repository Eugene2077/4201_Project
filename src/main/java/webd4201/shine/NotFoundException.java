package webd4201.shine;

/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 12 2023
 */

@SuppressWarnings("serial")
public class NotFoundException extends Exception
{
	public NotFoundException()
	{ super();}
	
	public NotFoundException(String message)
	{ super(message);}
}