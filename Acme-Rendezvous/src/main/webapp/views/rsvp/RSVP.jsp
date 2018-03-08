<%--	
* Copyright (C) 2017 Universidad de Sevilla
	
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
	
<form:form action="${requestURI}" modelAttribute="rendezvous">
	
	<form:hidden path="id"/>

	<form:hidden path="name"/>
	<form:hidden path="description"/>
	<form:hidden path="meetingMoment"/>	
	<form:hidden path="picture"/>
	
	<form:hidden path="gpsPoint.latitude"/>
	<form:hidden path="gpsPoint.longitude"/>
	<form:hidden path="isDeleted"/>
	<form:hidden path="isAdultOnly"/>
	<form:hidden path="isDraft"/>
	<form:hidden path="creator"/>
	<form:hidden path="rsvps"/>		
	<form:hidden path="questions"/>	
	<form:hidden path="comments"/>			
	<form:hidden path="announcements"/>
	<form:hidden path="isLinkedTo"/>
			
<security:authorize access="hasRole('USER')">


<acme:submit name="save" code="rendezvous.RSVPButton" /> 
<acme:cancel url="rendezvous/user/list.do?rendezvousId=${row.id}" code="rendezvous.cancel" /> <br/>

</security:authorize>

</form:form>