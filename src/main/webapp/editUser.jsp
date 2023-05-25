
<!--  * WEBD4201 Assignment 
 * @version 2.0
 * @since Feb 15 2023 -->
<meta name="author" content="Eugene Shin">

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- it says this page is made of Java  -->
    
<%@ page import = "webd4201.shine.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>

    
    <% 
        // page title
    	String pageTitle = "Edit User"; 
	    // get error message from the session variable and Cast it to a String
		String errMsg = (String)session.getAttribute("errors");
		// if first time login page, it didn't set any error message, so put empty string
		if(errMsg == null) { errMsg = ""; }
		HttpSession ses = request.getSession(true);
		
		Connection c = DatabaseConnect.initialize();
		User.initialize(c);
		List<User> users = UserDA.retrieveAll();
		User.terminate();
	%>
		
 
    

    <% 
    // create an User object and put the user object from the session variable
    User aUser = (User)session.getAttribute("user"); 
    // if the user is not logged in, redirect to login page
    if(aUser == null) { response.sendRedirect("./login.jsp");}
    else {
    %>
    
    
	<jsp:include page="./inc/header.jsp">
		<jsp:param name="title" value="Edit User" />
	</jsp:include>	
	
			<!-- put the error message at here -->
	<p style="color: red;"><%= errMsg %> </p>
	
	<!-- once the error message is displayed, delete it -->
	<% ses.setAttribute("errors", ""); %>
    
	
	<div id="contentContainer">
	
	
<!-- 	  <h1>Edit User</h1>
  <form action="/saveUser" method="POST">
    <label for="id">ID:</label>
    <input type="text" id="id" name="id" value="" readonly><br>

    Rest of the form fields

    <input type="submit" value="Save">
  </form>
  <script>
    // Get the ID parameter from the URL
    var urlParams = new URLSearchParams(window.location.search);
    var id = urlParams.get('id');

    // Set the ID field value
    document.getElementById('id').value = id;
  </script> -->
	
	
	<%
	// Get the ID parameter from the request
	String idString = request.getParameter("id");
	long id = Long.parseLong(idString);
    // create an User object and put the user object from the session variable
    User theUser = UserDA.retrieve(id); 

	%>
	
	


   <form action="./update" method="POST">
    <label for="id">ID:</label>
    <input type="text" id="id" name="id" value="<%=id %>" readonly><br>

    <label for="firstName">First Name:</label>
    <input type="text" id="firstName" name="firstName" value="<%=theUser.getFirstName() %>"><br>

    <label for="lastName">Last Name:</label>
    <input type="text" id="lastName" name="lastName" value="<%=theUser.getLastName() %>"><br>

    <label for="emailAddress">Email Address:</label>
    <input type="text" id="emailAddress" name="email" value="<%=theUser.getEmailAddress() %>"><br>

    <label for="enabled">Enabled:</label>
    <input type="checkbox" id="enabled" name="enabled" <% if (theUser.isEnabled()) { %>checked<% } %>><br>

    <label for="type">Type:</label>
<select id="type" name="type" style="color: #2ee;">
  <option class="inputOption" value="s" <% if (theUser.getType() == 's') { %>selected<% } %>>Student</option>
  <option class="inputOption" value="f" <% if (theUser.getType() == 'f') { %>selected<% } %>>Faculty</option>
</select>


    <input type="submit" value="Save">
  </form>
		   
<script>

</script>
		  
		  


	
	</div>
	
	
	
	<script type="module" src="adminDash.js"></script> 

	<jsp:include page="./inc/footer.jsp">
		<jsp:param name="footerTitle" value="Dashboard" />
	</jsp:include>	
	
	
	
	   <% } %>

</div>

</body>
</html>