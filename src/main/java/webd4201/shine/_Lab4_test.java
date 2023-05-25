package webd4201.shine;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class _Lab4_test {

	public static void main(String[] args) {
		
		System.out.println("******************** Lab4 Test Output ********************\n");
		Connection c = null;
		Student mainUser;  //object for a program created user
		Student dbUser;   //object for database retrieved User
		
		
		long possibleId = 100400019L;
		
		
		
		GregorianCalendar cal = new GregorianCalendar();
		Date lastAccess = cal.getTime();
		cal.set(2017, Calendar.SEPTEMBER, 3);
		Date enrol = cal.getTime();
		try{
			mainUser = new Student();
			dbUser = new Student();
			System.out.println("\nCreate a User user to insert/delete later in the program, passing:\n\t" +
					"User User1 = new User(" + possibleId + "L, \"password\", \"Robert\", \"McReady\"," +
					" \"bob.mcready@dcmail.ca\", enrol, lastAccess, 's', true, 's', 'AZ1101', 'desc', 2);\n"); 
			
			mainUser = new Student(possibleId,"password", "Robert", "McReady", "bob.mcready@dcmail.ca",
					lastAccess, enrol, true, 's', "AZ1101", "The best Student", 2);
			//mainUser.dump();
			try{
				
	            // initialize the database (i.e. create a database connection)
	            c = DatabaseConnect.initialize();
	            Student.initialize(c);
	            User.initialize(c);
	            
	            try // attempt to get a User that does NOT exist, throws Exception
	            {
	            	System.out.println("\nAttempt to retrieve a user that does not exist (Id: " + possibleId + ")");
	            	dbUser = Student.retrieve(possibleId);
	            	System.out.println("Student record with id " + possibleId + " retrieved from the database\n");
	            	dbUser.dump();
	            }
	            catch(NotFoundException e)
	            {	System.out.println(e.getMessage());}

	            try // attempt to get a User that does exist
	            {
	            	possibleId = 100111111L;
	            	System.out.println("\nAttempt to retrieve a Student that does exist (Id: " + possibleId + ")");
	            	dbUser = Student.retrieve(possibleId);
	            	System.out.println("User record with id " + possibleId + " retrieved from the database\n");
	            	dbUser.dump();
	            }
	            catch(NotFoundException e)
	            {	System.out.println(e.getMessage());}
	            
	            try
	            {
	            	System.out.println("\nAttempt to insert a new Student record for " 
	            						+ mainUser.getFirstName() + " " + mainUser.getLastName() 
	            						+ "(Id: " + mainUser.getId()+")");
	            	
	            	
	            	
	            	
	            	
	            	mainUser.create();
	                
            		System.out.println("Student record added to the database.\n");
                	mainUser.dump();
	                
//	            	else
//	            	{System.out.println("Something went wrong in Creating DB");}
	            }
	            catch(DuplicateException e)
	            {	System.out.println(e);}
	            
	            try
	            {
	            	System.out.println("\nChange the mainUser object and attempt to update the user record for " 
	            						+ mainUser.getFirstName() + " " + mainUser.getLastName() 
	            						+ "(Id: " + mainUser.getId() +")");
	            	mainUser.setLastName("changed LastName");
	            	mainUser.setFirstName("Changed first name");
	            	mainUser.setProgramDescription("Now he is changed to a Bad Man");
	            	mainUser.update();
	                System.out.println("User record updated in the database.\n");
	            }
	            catch(NotFoundException e)
	            {	System.out.println(e);}
	            
	            

	            System.out.println("Updated user information is: .\n");
	            mainUser.dump();
//
//		            	
	            try // now, attempt to delete the new User
	            {
	            	System.out.println("\nAttempt to delete the new user record for " 
	            	   						+ mainUser.getFirstName() + " " + mainUser.getLastName() 
   						+ "(Id: " + mainUser.getId() + ")");
	            	mainUser.delete();
	            	
	            	
	            	
	            	
	        	   	System.out.println("User record with id " + mainUser.getId() + " successfully removed from the database.\n");
	            }
	            catch(NotFoundException e)
	                    {	System.out.println(e);}

	            try // now, try to find the deleted User
	            {
	            	
	            	mainUser = Student.retrieve(possibleId);
	            	mainUser.dump();
	            	//mainUser.delete();
	            }
	            catch(NotFoundException e)
	            {
	            	System.out.println("Did not find user record with id " + possibleId + ".\n");
	            }
			 }catch(Exception e){   //catch for database initialize/connect try
				  System.out.println(e.toString());
			 }finally{ // close the database resources, if possible            
		          try{  User.terminate(); }catch(Exception e){}
		          try{  DatabaseConnect.terminate(); }catch(Exception e){}
			 }
		}catch(InvalidUserDataException iude){
			System.out.println(iude.getMessage());
		}

		
   }
}