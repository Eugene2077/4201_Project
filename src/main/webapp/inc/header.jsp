<!--  * WEBD4201 Assignment 
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 15 2023 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <!-- import everything in my package(class and servlet)to use in this webpage -->
    <%@ page import = "webd4201.shine.*" %>
    <%
    	User aUser = (User)session.getAttribute("user");
    %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>${param.title}</title>
<!-- using milligram -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/milligram/1.4.1/milligram.css">
<link rel="stylesheet" href="./css/style.css"/>

    <script
      src="https://code.jquery.com/jquery-3.6.3.min.js"
      integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU="
      crossorigin="anonymous"
    ></script>
</head>

<body>

<div id="container">
   <!-- IDE screams me 'no end tag' but it is OK -->
	<header>
	<p class="header">
		<% if(!(aUser == null)) {// if the user is Authenticated %>
			Welcome <%= aUser.getFirstName()%> |
			<a href="./Logout">Logout</a> 
			<a href="./update.jsp">Update</a> 
		<% } else { // if the user is NOT Authenticated %>
			<a href="./login.jsp">Login</a> |
			<a href="./register.jsp">Register</a> 
			<% } %>
	</p>
	<img width="100%" alt="" src="./images/sample.jpg">
	
	<!-- dynamic page title  -->
	<h2 id="title"><%= request.getParameter("title")%></h2>
	
	</header>
	
