<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="edu.uwm.cs361.util.PageTemplate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
				
<%= PageTemplate.printAdminHeader() %>	
	<div id='content'>
		<ul>
			<c:forEach items="${errors}" var="error">
       			<li class="error"><c:out value="${error}"/></li>
    		</c:forEach>
		</ul>
		<span class='title'>Create an Instructor:</span>
		<form id='form-id' method='POST' action='/createInstructor'>
			<label for='firstname-id'>First Name:</label>
			<input id='firstname-id' class='text-input' type='text' name='firstname' autofocus='autofocus' value='${firstname}'/><br/><br/>
			<label for='lastname-id'>Last Name:</label>
			<input id='lastname-id' class='text-input' type='text' name='lastname' value='${lastname}'/><br/><br/>
			<label for='email-id'>E-Mail:</label>
			<input id='email-id' class='text-input' type='email' name='email' value='${email}'/><br/><br/>
			<label for='phone_num-id'>Phone number:</label>
			<input id='phone_num-id' class='text-input' type='text' name='phonenumber' value='${phonenumber}'/><br/><br/>
			<label for='instructor_types'>Instructor Type:</label><br/>
			<input id='instructor_types' class='text-input' type='text' name='instructor_types' 
				<c:choose>
				      <c:when test="${instructor_types == ''}">
				      		placeholder='Ex: Dog Trainer, Tutor ...'/><br/><br/>
				      </c:when>
				      <c:otherwise>
				      		value='${instructor_types}'/><br/><br/>
				      </c:otherwise>
				</c:choose>
			<label for='username-id'>Username:</label>
			<input id='username-id' class='text-input' type='text' name='username' value='${username}'/><br/><br/> 
			<label for='password'>Password:</label>
			<input id='password' class='text-input' type='password' name='password' value='${password}'/><br/><br/>
			<label for='password_repeat'>Retype Password:</label>
			<input id='password_repeat' class='text-input' type='password' name='password_repeat' value='${password_repeat}'/><br/><br/>
			<div id='button-area'>
				<button id='submit-id' type='submit'>Create</button><br/><br/> 
			</div>
		</form>
	</div>	
<%= PageTemplate.printFooter() %>
		