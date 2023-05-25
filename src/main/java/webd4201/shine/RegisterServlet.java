package webd4201.shine;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


/**
 * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
				
	try {
		HttpSession session = request.getSession(true);
		// id variable( stId will parsed to long data type)
		long parsedId = 0;
		// obtain values from the form
		String stId = request.getParameter("id").trim();
		String pw = request.getParameter("password").trim();
		String pw2 = request.getParameter("password2").trim();
		String fname = request.getParameter("firstName").trim();
		String lname = request.getParameter("lastName").trim();
		String email = request.getParameter("email").trim();
		
		
	
//		// verification of input data ---------------------------------------------------------
//
		boolean anyErrors= false;
		StringBuffer errorBuffer = new StringBuffer();
		
		// verify id
		try {
			// check if the input is empty
			if (stId.length() == 0)
			{
				anyErrors= true;
				errorBuffer.append("<p class='errMsg'>You must enter an ID.</p>\n");
				System.out.println("ID is empty");
			}
			// if not empty
			else {
				try {
					try {
					// check if the input is numeric
						parsedId = Long.parseLong(stId);
					
						// check if the id is already in the DB
						try {
							User.retrieve(parsedId);
							// if the user ID is already in the DB
							anyErrors= true;
							errorBuffer.append("<p>" + stId
							+ " is already in the DB, User a different ID to register.</p>\n");
							session.setAttribute("id", "");
							System.out.println("The input ID found in the DB");
						}
						// if the input ID is not in the DB, we can use this ID
						catch(NotFoundException e) {
							//let's verify if it is 9 digit: if NOT
							if(!(User.verifyId(parsedId))) {
								anyErrors= true;
								errorBuffer.append("<p> the ID has to be " + User.ID_NUMBER_LENGTH + " digit.</p>\n");
								session.setAttribute("id", "");
								System.out.println("The ID is not 9 digit");
							}
							// when everything is good
							else {
								session.setAttribute("regId", stId);
								System.out.println("ID verified successfuly");
								}
						}
					}
					catch(Exception e) {
						System.out.println(e + "Parsing it not successful");
						errorBuffer.append("<p>" + stId
								+ " is not a valid id number.</p>\n");
					}
					
				}catch(NumberFormatException fe){
					anyErrors= true;
					errorBuffer.append("<p>" + stId
					+ " is not a valid id number.</p>\n");
					session.setAttribute("regId", "");//place empty on session for form
					System.out.println(fe);
					System.out.println("An Error occor during verification ID");
				}
			}

		}
		catch (Exception e) {
			System.out.println(e);
		}

	
				
		// verify password 	
		try {
			if (pw.length() == 0 || pw2.length() == 0) {
				anyErrors= true;
				errorBuffer.append("<p>Please input password and confirm password.</p>\n");
				session.setAttribute("regPw", "");//place empty on session for form
				System.out.println("The password is empty.");
			}
			// if two passwords are not the same		
			else if (!(pw.equals(pw2))) {		
				anyErrors= true;
				errorBuffer.append("<p>The passwords are not match.</p>\n");
				session.setAttribute("regPw", "");//place empty on session for form
				System.out.println("The passwords are not match");
			}
			// if the password length is not correct
			else if( (pw.length() < User.
					MINIMUM_PASSWORD_LENGTH) || (pw.length() > User.MAXIMUM_PASSWORD_LENGTH)) {
				anyErrors= true;
				errorBuffer.append("<p>Password has to be between " + User.
						MINIMUM_PASSWORD_LENGTH + " and " + User.MAXIMUM_PASSWORD_LENGTH +"  digits</p>\n");
				session.setAttribute("regPw", "");
				System.out.println("The password length is not correct");
			}
			// if all good
			else {
				session.setAttribute("regPw", "pw");
				System.out.println("The passwords verified");
				session.setAttribute("regPw", pw);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	
		
		
		// First Name verification
		try {
			// when verified
			if(User.verifyName(fname, "The first name")) {
				session.setAttribute("regFname", fname);
				System.out.println("the first name is valid");
			}
		}
		// not verified
		catch(InvalidNameException exName) {
			anyErrors= true;
			session.setAttribute("regFname", "");
			errorBuffer.append("<p class='errMsg'>The first name cannot be empty or number. Please input your First Name</p>\n");
			System.out.println(exName + ", Please check your first name");
			}

		// Last Name verification
		try {
			if(User.verifyName(lname, "The last name")) {
				session.setAttribute("regLname", lname);
				System.out.println("the last name is valid");
			}			
		}
		// not verified
		catch(InvalidNameException exName) {
			anyErrors= true;
			errorBuffer.append("<p class='errMsg'>The last name cannot be empty or number. Please input your Last Name</p>\n");
			session.setAttribute("regLname", "");
				System.out.println(exName + ", Please check your last name");
			}
		
		
		// verify email address
		try {
			// check if the email is empty
			if(email.length() == 0)
			{
				anyErrors= true;
				errorBuffer.append("<p class='errMsg'>You must enter an email addres.</p>\n");
				System.out.println("email is empty");
			// check if the email is a correct format.
			}else if(!(User.verifyEmailAddress(email))){
				anyErrors= true;
				errorBuffer.append("<p class='errMsg'>" + email
				+ " is not a valid email.</p>\n");
				session.setAttribute("regEmail", "");
				System.out.println("NOT a valid E-mail");
			}
			// everything is correct
			else {
				session.setAttribute("regEmail", email);
				System.out.println("email is valid");
			}
		}
		catch(Exception e) {
			System.out.println(e + ", Please check email");
		}
		
		// input data verification is done -----------------------------------

		
		
		// convert error messages to string
		String formErrorString = errorBuffer.toString();
		
		
		System.out.println(formErrorString);
		
		// register the message to session
		
		session.setAttribute("formErrorMsg", formErrorString);
		
		/*
		 * RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
		 * rd.forward(request, response);
		 */
		 
		
		
		// when all the input is valid
		if (anyErrors == false) {
		
			try {
				// create a new student object
				Student newStudent = new Student(parsedId, pw, fname, lname, email, new Date(), new Date(), 
					Student.DEFAULT_ENABLED_STATUS, Student.DEFAULT_TYPE,  Student.DEFAULT_PROGRAM_CODE, 
					Student.DEFAULT_PROGRAM_DESCRIPTION, Student.DEFAULT_YEAR);
				
				// connect server and register in the DB
				Connection aConnection;
				aConnection = DatabaseConnect.initialize();
				User.initialize(aConnection);
				Student.initialize(aConnection);
				
				// try to create this new user to DB
				if(newStudent.create()) {
					// when success, 
					System.out.println("Transaction Returned TRUE!");
				
					// check again if the registered Student is in the DB
					User foundUser = Student.authenticate(parsedId, pw);
					System.out.println("User: " + foundUser.getFirstName() + " found in the DB");
					// set session the user(like as a logged in user)
					session.setAttribute("user", foundUser);
					// login was successful, store an empty error message to the session variable 'ses'
					session.setAttribute("errors", "");
					
					//redirect to dashboard.jsp
					response.sendRedirect("./dashboard.jsp");
				}
				// if the transaction has failed
				else {
					System.out.println("Transaction Returned False!");
					session.setAttribute("msg", "Error: Registration Failed!");
					response.sendRedirect("./register.jsp");
				}
				
				// terminate the connection
				User.terminate();
				Student.terminate();
				
			}
			catch (Exception e){   
				  System.out.println(e.toString());
			}
		}
		else {
			// when input verification is failed redirect to register.jsp
			response.sendRedirect("./register.jsp");

		}

			

			
	}
	catch (Exception e) {
		// Error, redirect to register.jsp
		System.out.println("An error occurred: " + e);
		response.sendRedirect("./register.jsp");
	}

		
 }
}

