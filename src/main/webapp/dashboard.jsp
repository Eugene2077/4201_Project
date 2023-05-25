
<!--  * WEBD4201 Assignment 
 * @version 2.0
 * @since Feb 15 2023 -->
<meta name="author" content="Eugene Shin">

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- it says this page is made of Java  -->
    
<%@ page import = "webd4201.shine.*" %>
    
    <% 
        // page title
    	String pageTitle = "Dashboard"; 
	    // get error message from the session variable and Cast it to a String
		String errMsg = (String)session.getAttribute("errors");
		// if first time login page, it didn't set any error message, so put empty string
		if(errMsg == null) { errMsg = ""; }
		HttpSession ses = request.getSession(true);
    %>
    

    <% 
    // create an User object and put the user object from the session variable
    User aUser = (User)session.getAttribute("user"); 
    // if the user is not logged in, redirect to login page
    if(aUser == null) { response.sendRedirect("./login.jsp");}
    else {
    %>
    
    
	<jsp:include page="./inc/header.jsp">
		<jsp:param name="title" value="User Dashboard" />
	</jsp:include>	
	
			<!-- put the error message at here -->
	<p style="color: red;"><%= errMsg %> </p>
	
	<!-- once the error message is displayed, delete it -->
	<% ses.setAttribute("errors", ""); %>
	
	
	<div id="contentContainer">
		<h2>Welcome <%= aUser.getFirstName() + ' ' + aUser.getLastName() %></h2>
		<p>The current date and time is: <%= new java.util.Date()%></p>
		<p>You should only see this if you are logged in!</p>
	</div>

	<jsp:include page="./inc/footer.jsp">
		<jsp:param name="footerTitle" value="Dashboard" />
	</jsp:include>	

	   
	   <% } %>

</div>

</body>
</html>