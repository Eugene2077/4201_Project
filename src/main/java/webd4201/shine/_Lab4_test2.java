package webd4201.shine;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class _Lab4_test2 {

	public static void main(String[] args) {
		
		System.out.println("******************** Lab4-2 Test Output ********************\n");
		Connection c = null;

		User aUser;
		User dbUser;
		Student mainUser;

		long id = 100000022L;
		long possibleId = 200000102L;
		
		GregorianCalendar cal = new GregorianCalendar();
		Date lastAccess = cal.getTime();
		cal.set(2017, Calendar.SEPTEMBER, 3);
		Date enrol = cal.getTime();
		try{

			aUser = new User(id,"password", "Robert", "McReady", "bob.mcready@dcmail.ca",
					lastAccess, enrol, true, 's');
			
			mainUser = new Student(possibleId,"password", "Kelly", "Smith", "bob.mcready@dcmail.ca",
					lastAccess, enrol, true, 's', "AZ1101", "The Student", 1);
			
			//mainUser.dump();
			try{
				
	            // initialize the database (i.e. create a database connection)
	            c = DatabaseConnect.initialize();
	            User.initialize(c);
	            
	            try // attempt to get a User that does NOT exist, throws Exception
	            {
	            	System.out.println("\nAttempt to retrieve a user that does not exist (Id: " + id + ")");
	            	dbUser = User.retrieve(id);
	            	System.out.println("User record with id " + id + " retrieved from the database\n");
	            	dbUser.dump();
	            	
	            	            	
	            }
	            catch(NotFoundException e)
	            {	System.out.println(e.getMessage());}

	            try // attempt to get a User that does exist
	            {
	            	long idnot = 100111111L;
	            	System.out.println("\nAttempt to retrieve a User that does exist (Id: " + idnot + ")");
	            	dbUser = User.retrieve(idnot);
	            	System.out.println("User record with id " + idnot + " retrieved from the database\n");
	            	dbUser.dump();
	            }
	            catch(NotFoundException e)
	            {	System.out.println(e.getMessage());}
	            
	            try
	            {
	            	System.out.println("\nAttempt to insert a new User record for " 
	            						+ aUser.getFirstName() + " " + aUser.getLastName() 
	            						+ "(Id: " + aUser.getId()+")");
	            	aUser.create();
	            	System.out.println("\nAttempt to retrieve the createdd User record for " 
    						+ aUser.getFirstName() + " " + aUser.getLastName() 
    						+ "(Id: " + aUser.getId()+")");
	            	User retrievedUser = User.retrieve(id);
	                
            		System.out.println("this is the retrieved user: \n");
            		retrievedUser.dump();
	                
//	            	else
//	            	{System.out.println("Something went wrong in Creating DB");}
	            }
	            catch(DuplicateException e)
	            {	System.out.println(e);}
	            
	            try
	            {
	            	System.out.println("\nChange the mainUser object and attempt to update the user record for " 
	            						+ aUser.getFirstName() + " " + aUser.getLastName() 
	            						+ "(Id: " + aUser.getId() +")");
	            	aUser.setLastName("Tom");
	            	aUser.setFirstName("Cruise");
	            	aUser.update();
	                System.out.println("Update tried\n");
	            

		            System.out.println("this is the retrieved user: \n");
		            User retrievedUser = User.retrieve(id);
	        		retrievedUser.dump();
	            }
	            catch(NotFoundException e)
	            {	System.out.println(e);}
	            
//		            	
	            try // now, attempt to delete the new User
	            {
	            	System.out.println("\nAttempt to delete the new user record for " 
	            	   						+ aUser.getFirstName() + " " + aUser.getLastName() 
   						+ "(Id: " + aUser.getId() + ")");
	            	aUser.delete();
	            	
	            }
	            catch(NotFoundException e)
	                    {	System.out.println(e);}

	            try // now, try to find the deleted User
	            {
	            	System.out.println("\nTry to find the deleted user record for " 
	   						+ aUser.getFirstName() + " " + aUser.getLastName() 
			+ "(Id: " + aUser.getId() + ")");
	            	
	            	User retrievedUser = User.retrieve(id);
	        		retrievedUser.dump();
	        		


	            }
	            catch(NotFoundException e)
	            {
	            	System.out.println("Did not find user record with id " + id + ".\n");
	            }
	            
	            
	            
	            
	            
	            System.out.println("\n\n-----------Student User creation and deletion test ------------" );

	            
	            try {
	        		System.out.println("\nTry to create main user(a student)" );
	        		mainUser.create();
	        		System.out.println("\nTry to delete main user(a student)" );
	        		mainUser.delete();
	        		System.out.println("\nTry to retrieve deleted main user(a student)" );
	        		// trying to find the deleted user, if not found -> Exception will occur
	        		User.retrieve(possibleId);
	        		// if something found, it means deletion was not successful. 
	        		System.out.println("The user is Still in the DB/ Something wrong !!");

	            }
	            catch(Exception e) {
	            	System.out.println("Didn't find the deleted user/ Deletion was successful");
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