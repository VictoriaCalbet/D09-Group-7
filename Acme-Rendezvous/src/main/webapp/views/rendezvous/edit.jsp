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


<form:form action="${requestURI}" modelAttribute="rendezvousForm">

	<!-- Hidden attributes -->
	<form:hidden path="id"/>
	<jstl:if test="${age < 18}">
		<form:hidden path="isAdultOnly"/>
	</jstl:if>
	
	<!-- Editable attributes -->
	
	<acme:textbox code="rendezvous.name" path="name"/>
	<acme:textarea code="rendezvous.description" path="description"/>
	<acme:date code="rendezvous.meetingMoment" path="meetingMoment"/>	
	<acme:textbox code="rendezvous.picture" path="picture"/>
	<acme:textbox code="rendezvous.gpsPoint.latitude" path="gpsPoint.latitude"/>
	<acme:textbox code="rendezvous.gpsPoint.longitude" path="gpsPoint.longitude"/>
	<jstl:if test="${age > 18}">
		<acme:checkbox code="rendezvous.isAdultOnly" path="isAdultOnly"/>
	</jstl:if>
	<acme:checkbox code="rendezvous.isDraft" path="isDraft"/>
	
		<!-- Action buttons -->
	<acme:submit name="save" code="rendezvous.save" /> &nbsp;
	<acme:cancel url="/" code="rendezvous.cancel" /> <br/>

</form:form>

