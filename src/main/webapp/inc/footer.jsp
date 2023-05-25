

<!--  * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023 -->



<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <%@ page import = "webd4201.shine.*" %>
    <%
    	User aUser = (User)session.getAttribute("user");
    %>
    
	<footer> 
		<hr/>
		<% if(!(aUser == null)) {// if the user is Authenticated %>
			Welcome <%= aUser.getFirstName()%> |
			<a href="./Logout">Logout</a> 
			<a href="./update.jsp">Update</a> 
		<% } else { // if the user is NOT Authenticated %>
			<a href="./login.jsp">Login</a> |
			<a href="./register.jsp">Register</a> 
			<% } %>
		<br/>
		&copy; Eugene Shin for WEBD4201
		<a href="./">Home</a>
		<br/>
		${param.footerTitle}
	</footer>
    
    
