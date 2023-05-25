<!--  * WEBD4201 Assignment 
 * @version 2.0
 * @since Feb 15 2023 -->
<meta name="author" content="Eugene Shin">

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "webd4201.shine.*" %>

    <% 
    	String pageTitle = "Registration Page"; 
	    // get error message from the session variable and Cast it to a String
		String errMsg = (String)session.getAttribute("errors");
		// if first time login page, it didn't set any error message, so put empty string
		if(errMsg == null) { errMsg = ""; }
		HttpSession ses = request.getSession(true);
    %>
    
    <% 
	    // create an User object and put the user object from the session variable
	    User aUser = (User)session.getAttribute("user"); 
	
	    // if the user is logged in, redirect to dashboard page
	    if(aUser != null) { 
	    	ses.setAttribute("errors", "You are logged in. Log out first if you want registeration new user.");
	    	response.sendRedirect("./dashboard.jsp");
	    	}
		 
    %>
    
    
    <%@ page import = "webd4201.shine.*" %>
    <%
        //get the session variables for stiky form
    	String inputId = (String)session.getAttribute("regId");
	    String password = (String)session.getAttribute("regPw");
	    String fName = (String)session.getAttribute("regFname");
	    String lName = (String)session.getAttribute("regLname");
	    String email = (String)session.getAttribute("regEmail");
	    
	    if(inputId == null) {inputId = "";}
	    if(password == null) {password = "";}
	    if(fName == null) {fName = "";}
	    if(lName == null) {lName = "";}
	    if(email == null) {email = "";}
	    
		// get the form validation error message from register servlet
	    String formErrorMsg = (String)session.getAttribute("formErrorMsg");
	    // if the error message is null, make it empty
	    if(formErrorMsg == null) { formErrorMsg = "";}
	    
    %>
   
	<jsp:include page="./inc/header.jsp">
		<jsp:param name="title" value="<%= pageTitle %>" />
	</jsp:include>	
		
	
	
	<div id="contentContainer">
		<h4>Enter your information below</h4>
	<h3 id="regist">Registration</h3>
	<form method="post" action="./Register" autocomplete="off">
		<div id="id-group">
			<label>ID:</label><input type="text" name="id" id="id" value= <%= inputId %> ></input><br/>
			<div style="color:red;" class="errorMessage"></div>
		</div>
		<div id="pass1-group">
			<label>Password:</label><input type="password" name="password" id="inputPassword" value=<%= password %> ></input><br/>
			<div style="color:red;" class="errorMessage"></div>
		</div>
		<div id="pass2-group">
			<label>Confirm Password:</label><input type="password" name="password2" id="inputPassword2"  value=<%= password %> ></input><br/>
			<div style="color:red;" class="errorMessage"></div>
		</div>
		<div id="first-group">
			<label>First Name:</label><input type="text" name="firstName" id="inputFirst" value=<%= fName %> ></input><br/>
			<div style="color:red;" class="errorMessage"></div>
		</div>
		<div id="last-group">
			<label>Last Name:</label><input type="text" name="lastName" id="inputLast" value=<%= lName %> ></input><br/>
			<div style="color:red;" class="errorMessage"></div>
		</div>
		<div id="email-group">
			<label>Email:</label><input type="text" name="email" id="inputEmail" value=<%= email %> ></input><br/>
			<div style="color:red;" class="errorMessage"></div>
		</div>
		<div>
		<label>&nbsp;</label><button type="submit" name="Submit" id="btnRegSubmit">Submit</button>
		</div>
				
	</form>
		 <div class="errorMsg"><%= formErrorMsg %></div>
	</div>

	<jsp:include page="./inc/footer.jsp">
		<jsp:param name="footerTitle" value="<%= pageTitle %>"/>
	</jsp:include>	

	<script type="module" src="validationForm.js"></script> 
	
</div>

</body>
</html>