
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
    	String pageTitle = "Admin Dashboard"; 
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
		<jsp:param name="title" value="Admin Dashboard" />
	</jsp:include>	
	
			<!-- put the error message at here -->
	<p style="color: red;"><%= errMsg %> </p>
	
	<!-- once the error message is displayed, delete it -->
	<% ses.setAttribute("errors", ""); %>

	
	<div id="contentContainer">
		<h2>Welcome <%= aUser.getFirstName() + ' ' + aUser.getLastName() %></h2>
		<p>this is only viewing to Admin!</p>
	</div>
	<div>
		<table id="user-table">
		  <thead>
		  	<tr>
			    <th>id</th>
			    <th>firstname</th>
			    <th>lastname</th>
			    <th>email</th>
			    <th>last access date</th>
			    <th>enroled date</th>
			    <th>enabled?</th>
			    <th>user type</th>
			    <th></th>
			    <th></th>
		    </tr>
		  </thead>
		  <tbody>
		  	<%
			for (int i = 0; i < users.size(); i++) {
				User eachUser = users.get(i);
			%>
			  <tr>
			  	<td><%=eachUser.getId() %></td>
			  	<td><%=eachUser.getFirstName() %></td>
			  	<td><%=eachUser.getLastName() %></td>
			  	<td><%=eachUser.getEmailAddress() %></td>
				<td><%=eachUser.getLastAccess()%></td>	  	
				<td><%=eachUser.getEnrolDate()%></td>
			  	<td><%=eachUser.isEnabled() %></td>
			  	<td><%=eachUser.getType() %></td>
			  	<td class="edit-entry" id="btnEdit"><a href="./editUser.jsp?id=<%=eachUser.getId()%>">Edit</a></td>

			  	<td class="edit-entry" id="btnDel">
  <button class="delete-user" data-userid="<%=eachUser.getId()%>" data-usertype="<%=eachUser.getType()%>">Delete</button>
</td>

			  </tr>
			 
			  <% } %>

		  </tbody>
		  
		  
		  <!-- Add table rows here -->
		</table>
	
	
	</div>
	
	
	
	<script type="module" src="adminDash.js"></script> 

	<jsp:include page="./inc/footer.jsp">
		<jsp:param name="footerTitle" value="Dashboard" />
	</jsp:include>	
	
	
	
	   <% } %>

</div>

</body>
</html>