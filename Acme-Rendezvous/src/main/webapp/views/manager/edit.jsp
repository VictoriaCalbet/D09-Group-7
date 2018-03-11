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

<form:form action="${requestURI}" modelAttribute="managerForm">

	<!-- Hidden attributes -->
	<form:hidden path="id"/>
	
	<!-- Editable attributes -->
	<acme:textbox code="manager.name" path="name"/>
	<acme:textbox code="manager.surname" path="surname"/>
	<acme:textbox code="manager.vat" path="VAT"/>
	<acme:textbox code="manager.email" path="email"/>
	<acme:textbox code="manager.address" path="address"/>
	<acme:textbox code="manager.phone" path="phone"/>
	<acme:textbox code="manager.birthDate" path="birthDate" />
	
	<jstl:choose>
		<jstl:when test="${managerForm.id == 0}"> <acme:textbox code="manager.username" path="username" readonly="false"/> </jstl:when>
		<jstl:when test="${managerForm.id != 0}"> <acme:textbox code="manager.username" path="username" readonly="true"/> </jstl:when>
	</jstl:choose>	
	
	<acme:password code="manager.password" path="password"/>
	<acme:password code="manager.repeatedPassword" path="repeatedPassword"/>
	
	<jstl:if test="${managerForm.id == 0}">
		<acme:checkbox code="manager.acceptTerm" path="acceptTerms" />
		<a href="legal-terms/index.do"> <spring:message code="actorForm.acceptTerm.text" /></a>
	</jstl:if><br/>
		
	
	<!-- Action buttons -->
	<acme:submit name="save" code="manager.save" /> &nbsp;
	<acme:cancel url="manager/manager/profile.do" code="manager.cancel" /> <br/>

</form:form>