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


<form:form action="${requestURI}" modelAttribute="survivalClass">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="momentOrganized"/>
	<form:hidden path="location.id"/>
	<form:hidden path="location.version"/>
	<form:hidden path="location.gpsPoint.longitude"/>	
	<form:hidden path="location.gpsPoint.latitude"/>
	<form:hidden path="location.name"/>	
	<form:hidden path="title"/>
	<form:hidden path="description"/>
	<form:hidden path="trip"/>
	<form:hidden path="explorers" value="${explorers}"/>
	<security:authorize access="hasRole('EXPLORER')">
	
	<input type="submit" name="save" value="<spring:message code="survivalClass.enrolASurvivalClassButton"/>"/>
	
	
	
<!-- 	Si cancela, se va a la vista anterior de survivalClasses -->
	<input type="button" value="<spring:message code="survivalClass.cancel"/>" 
	onClick="relativeRedir('survivalClass/explorer/list.do?tripId=${survivalClass.trip.id}')"/>
	
	</security:authorize>
		
	
</form:form>
