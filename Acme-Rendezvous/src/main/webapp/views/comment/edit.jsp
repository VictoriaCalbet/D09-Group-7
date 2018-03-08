<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="commentForm">

	<!-- Hidden attributes -->
	<form:hidden path="id"/>
	<form:hidden path="momentWritten"/>
	
	<!-- Editable attributes -->
	
	<acme:textbox code="comment.text" path="text"/>	
	<acme:textbox code="comment.picture" path="picture"/>
	
		<!-- Action buttons -->
	<security:authorize access="hasRole('USER')">	
	<acme:submit name="save" code="comment.save" />
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">	
	<acme:submit name="delete" code="comment.delete" />
	</security:authorize>
	
	
	<acme:cancel url="/" code="comment.cancel" /> <br/>

</form:form>

