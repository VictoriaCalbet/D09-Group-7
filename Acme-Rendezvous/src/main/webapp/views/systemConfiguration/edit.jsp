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

<form:form action="${requestURI}" modelAttribute="systemConfiguration">

	<!-- Hidden attributes -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Editable attributes -->
	<acme:textbox code="systemConfiguration.businessName" path="businessName"/>
	<acme:textbox code="systemConfiguration.bannerURL" path="bannerURL"/>
	<acme:textbox code="systemConfiguration.englishWelcomeMessage" path="englishWelcomeMessage"/>
	<acme:textbox code="systemConfiguration.spanishWelcomeMessage" path="spanishWelcomeMessage"/>
	
	<!-- Action buttons -->
	<acme:submit name="save" code="systemConfiguration.save" /> &nbsp;
	<acme:cancel url="systemConfiguration/administrator/info.do" code="systemConfiguration.cancel" /> <br/>

</form:form>