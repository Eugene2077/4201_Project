<!--  * WEBD4201 Assignment 
 * @version 2.0
 * @since Feb 15 2023 -->
<meta name="author" content="Eugene Shin">


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "webd4201.shine.*" %>


<% 
    // page title
	String pageTitle = "User Authentication"; 
    // get error message from the session variable and Cast it to a String
	String errMsg = (String)session.getAttribute("errors");
	// if first time login page, it didn't set any error message, so put empty string
	if(errMsg == null) { errMsg = ""; }
	// create a session: I wanna delete the errMsg after once it displayed
	HttpSession ses = request.getSession(true);
%>
    <% 
	    // create an User object and put the user object from the session variable
	    User aUser = (User)session.getAttribute("user"); 
	
	    // if the user is logged in, redirect to dashboard page
	    if(aUser != null) { 
	    	ses.setAttribute("errors", "You already logged in. redirected to dashboard. If you want log in another account, logout first.");
	    	response.sendRedirect("./dashboard.jsp");
	    	}
		 
    %>



    
	<jsp:include page="./inc/header.jsp">
		<jsp:param name="title" value="<%= pageTitle %>" />
	</jsp:include>	
	
	<div id="contentContainer">
	
		<!-- put the error message at here -->
		<p style="color: red;"><%= errMsg %> </p>
		
		<!-- once the error message is displayed, delete it -->
		<% ses.setAttribute("errors", ""); %>
	
		<h2>Please Login below</h2>
		<p>The current date and time is: <%= new java.util.Date()%></p>
		

		<form method="post" action="./Login">
            <label class="fixedlabel">Log in</label>
            <input name="id" type="text"><br/>
            <label class="fixedlabel">Password</label>
            <input name="password" type="password"><br/>
            <label class="fixedlabel"></label>
            <button type="submit">Login</button>
        </form>
		
	</div>

	<jsp:include page="./inc/footer.jsp">
		<jsp:param name="footerTitle" value="<%= pageTitle %>" />
	</jsp:include>	
	<!-- so, write as "${param.footerTitle}" you can call this value
	   this way, we can send a value to another page (we could set the parameter
	   to every page and customize with differently  -->

</div>

</body>
</html>