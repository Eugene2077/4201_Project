package webd4201.shine;
/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

@SuppressWarnings("serial")
public class DuplicateException extends Exception
{
    public DuplicateException()
    { super();}
    
    public DuplicateException(String message)
    { super(message);}
}