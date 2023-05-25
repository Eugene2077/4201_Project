package webd4201.shine;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class updateServlet
 */
@WebServlet("/update")
public class updateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// server connection
		Connection c = null;
		c = DatabaseConnect.initialize();
        Student.initialize(c);
        Faculty.initialize(c);
        User.initialize(c);

		// get the id and command parameters
		String userId = request.getParameter("id").trim();
		String userType = request.getParameter("type");
		String command = request.getParameter("command");
		long id = Long.parseLong(userId);
		
		System.out.println("I got ID: " + userId +"\nCommand: "+ command + "\nUserType: " + userType);
		
		// if the command is delete
		if(command != null && command.equals("delete") ) {
			// if the user to be deleted is a student
			if(userType.equals("s")) {
				System.out.println("Hmm, it'e Delete a student");
				Student aStudent;
				try {
					aStudent = StudentDA.retrieve(id);
					aStudent.dump();
					aStudent.delete();
					System.out.println("Student Id: "+ id + "successfully deleted by user's request"); 
				} catch (Exception e) {
					System.out.println("Error: "+e);
				} 
			}
			else if(userType.equals("f")) {
				System.out.println("Hmm, it'e Delete a faculty");
				Faculty aFaculty;
				try {
					aFaculty = FacultyDA.retrieve(id);
					aFaculty.dump();
					aFaculty.delete();
					System.out.println("Faculty Id: "+ id + "successfully deleted by user's request"); 
				} catch (Exception e) {
					System.out.println("Error: "+e);
				} 
			}
			else {
				System.out.println("Something is wrong with user type ");
			}
		}


		
		
		
		
		// when it is coming from the User edit page
		else {
					// obtain values from the form
			String fname = request.getParameter("firstName");
			String lname = request.getParameter("lastName");
			String email = request.getParameter("email");
			String isEnable = request.getParameter("enabled");
			boolean enable = true;
			if(isEnable==null) {enable = false;}
			System.out.println(
					"ID: " + id +
					"\nfname " + fname +
					"\nlname " + lname +
					"\nemail " + email +
					"\nisEnable " + isEnable +
					"\ntype " + userType);
			
			
			try {
				// create a user object and fill in the info with retrieved 
				User aUser;
				// get all the Student's info
				aUser = UserDA.retrieve(id);
				char beforeUpdatedUserType = aUser.getType();
				System.out.println("the user info before changed: ");
				aUser.dump();
				// set user info to new ones
				aUser.setFirstName(fname);
				aUser.setLastName(lname);
				aUser.setEmailAddress(email);
				aUser.setEnabled(enable); 
				char charType = userType.charAt(0);
				aUser.setType(charType);
				System.out.println("Hmm, now it is updated like this: ");
				aUser.dump();

				// update user information : update into a Student
				if(userType.equals("s")) {
					System.out.println("Hmm, let's update to student");
					//if update is from Faculty to Student, we need create a student before update
					if(beforeUpdatedUserType == 'f') {
					// it means a faculty is turn into a student, create a student with the id
						System.out.println("--------- adding a student ----------");
						if(StudentDA.addStudent(id)) {
							System.out.println("a student added with id: " + id);
						}
					}
				}
				// update user information : update into a Faculty
				else if(userType.equals("f")) {
					System.out.println("Hmm, let's update to faculty");
					//if update is from a Student to Faculty, we need create a faculty before update
					if(beforeUpdatedUserType == 's') {
					// it means a faculty is turn into a student, create a student with the id
						System.out.println("--------- adding a faculty ----------");
						if(FacultyDA.addFaculty(id)) {
							System.out.println("a faculty added with id: " + id);
						}
					
					}
				}
				// Student DB or Faculty DB is already checked, only update User DB
				aUser.update(); 
				System.out.println("User Id: "+ id +
						" successfully updated by user's request");
				// redirect to dash board
				response.sendRedirect("./adminDashboard.jsp");		
					
				
				
				
				
 
			} catch (Exception e) {
				System.out.println("Error: "+e);
			} 


			
		}
        try{  User.terminate(); }catch(Exception e){}
        try{  Student.terminate(); }catch(Exception e){}
        try{  Faculty.terminate(); }catch(Exception e){}
        try{  DatabaseConnect.terminate(); }catch(Exception e){}
	}


}
