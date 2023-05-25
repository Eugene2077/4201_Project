<!--  * WEBD4201 Assignment 
 * @version 2.0
 * @since Feb 15 2023 -->
<meta name="author" content="Eugene Shin">


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "webd4201.shine.*" %>

    <% 
        // page title
    	String pageTitle = "Home Page"; 
	    // get error message from the session variable and Cast it to a String
		String errMsg = (String)session.getAttribute("errors");
		// if first time login page, it didn't set any error message, so put empty string
		if(errMsg == null) { errMsg = ""; }

    %>



	<jsp:include page="./inc/header.jsp">
		<jsp:param name="title" value="Eugene Homepage" />
	</jsp:include>	
	
	<div id="contentContainer">
	
		<h3>This web site is for the LABs WEBD4201 Assignments</h3>
		<p>The current date and time is: <%= new java.util.Date()%></p>

	</div>

	<jsp:include page="./inc/footer.jsp">
		<jsp:param name="footerTitle" value="Main Homepage" />
	</jsp:include>	
	<!-- so, write as "${param.footerTitle}" you can call this value
	   this way, we can send a value to another page (we could set the parameter
	   to every page and customize with differently  -->

</div>

</body>
</html>