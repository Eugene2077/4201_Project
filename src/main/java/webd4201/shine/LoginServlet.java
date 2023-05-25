package webd4201.shine;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.Date;

/**
 * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023
 */

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// create a session variable 'ses'
		HttpSession session = request.getSession(true);
		// get the user name and password from the form
		String stId = request.getParameter("id");
		String pw = request.getParameter("password");
		// define administrator id(String up to 40 char)
		final String admin = "admin"; // it is already set in the DB

		
		// if this user logs in as the administrator
		if (stId.trim().toLowerCase().equals(admin)) {
			// build connection and initialize
			try {
				Connection c = null;
				c = DatabaseConnect.initialize();
		        User.initialize(c);
		        // try Authenticate Admin and put into a User object
		        User foundAdmin = UserDA.adminAuthenticate(admin, pw);
				
		        // if the administrator authenticated
		        if (foundAdmin != null) {
					System.out.println("Authentication successful as ADMIN");
					// we found the user in the DB, so put it to Session
					session.setAttribute("user", foundAdmin);
					// login was successful, set error message
					session.setAttribute("errors", "You Logged in as ADMINISTRATOR");
					// redirect user 
					response.sendRedirect("./adminDashboard.jsp");
		        }else {
		        	System.out.println("Authentication fail as ADMIN");
		        	session.setAttribute("errors", "Login ADMINISTRATOR failed: Check admin information");
		        	response.sendRedirect("./login.jsp");
		        }
			}catch(Exception e) {
				System.out.println("error: " + e);
			}
		
			
		// else, all users are logged in a regular user.
		}else {
			long id = 0;
			// Cast idString to integer
			try {
				id = Long.parseLong(stId);
			}
			catch (NumberFormatException ex) {
				System.out.println("Error: " + ex);
				System.out.println("Error: " + ex);
				}
			// try authenticate and put the found user from DB to session
			try {
				// build connection and initialize
				Connection c = null;
				c = DatabaseConnect.initialize();
		        Student.initialize(c);
				
				// call authenticate method from StudentDA and assign the user to foundUser
				User foundUser = Student.authenticate(id, pw);
				// if the user is not null means it found the user from the DB
				if(!(foundUser == null)) {
					// update the last access date
					User aUser = UserDA.retrieve(id);
					aUser.setLastAccess(new java.util.Date());
					System.out.println("Today new date: " + new java.util.Date());
					aUser.update();
					
					
					
					System.out.println("Authentication successful-!");
					// we found the user in the DB, so put it to Session
					session.setAttribute("user", foundUser);
					// login was successful, set error message
					session.setAttribute("errors", "You successfully logged in");
					// redirect user 
					response.sendRedirect("./dashboard.jsp");
				}
			} catch (NotFoundException e) {
				System.out.println("Error: " + e);
				System.out.println("Authentication Failed");
				session.setAttribute("errors", "Your login information is incorrect. Please try again");
				response.sendRedirect("./login.jsp");
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
		
	}


}











