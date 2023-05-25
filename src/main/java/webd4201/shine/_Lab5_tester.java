package webd4201.shine;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class _Lab5_tester {

	public static void main(String[] args) {
		
		System.out.println("******************** Lab5 Test Output ********************\n");
		Connection c = null;



			
			//mainUser.dump();
			try{
				
	            // initialize the database (i.e. create a database connection)
	            c = DatabaseConnect.initialize();
	            User.initialize(c);
	            
	            try // attempt to get a User that does NOT exist, throws Exception
	            {
	            	System.out.println("\nAttempt to retrieve all user by type: s ");
	            	
	            	List<User> allUsers = UserDA.retrieveAll();
	            	
	            	for (User user : allUsers) {
	            	    System.out.println("\n"+user.getId());
	            	    System.out.println(user.getFirstName());
	            	    System.out.println(user.getLastName());
	            	    System.out.println(user.getEmailAddress());
	            	    						
	            	}
	            	
	            	
//	                // Using an iterator
//	                Iterator<String> iterator = myList.iterator();
//	                while (iterator.hasNext()) {
//	                    String item = iterator.next();
//	                    System.out.println(item);
//	                }
	            	
	            	
	            	
	            	            	
	            }
	            catch(NotFoundException e) {
	            	System.out.println(e.getMessage());
	        		System.out.println("The user is Still in the DB/ Something wrong !!");
	            }
	            catch(Exception e) {
	            	System.out.println("Error: " + e );
	            }
	            
	            
			 }catch(Exception e){   //catch for database initialize/connect try
				  System.out.println(e.toString());
			 }finally{ // close the database resources, if possible            
		          try{  User.terminate(); }catch(Exception e){}
		          try{  DatabaseConnect.terminate(); }catch(Exception e){}
			 }

		}

		
   }
