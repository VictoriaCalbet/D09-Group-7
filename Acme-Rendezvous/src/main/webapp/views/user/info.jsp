<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jsp:useBean id="now" class="java.util.Date" />

<div>
	<b><spring:message code="user.name" />: </b>
	<jstl:out value="${user.name}" />
	<br /> <b><spring:message code="user.surname" />: </b>
	<jstl:out value="${user.surname}" />
	<br /> <b><spring:message code="user.email" />: </b>
	<jstl:out value="${user.email}" />
	<br /> <b><spring:message code="user.phone" />: </b>
	<jstl:out value="${user.phone}" />
	<br /> <b><spring:message code="user.address" />: </b>
	<jstl:out value="${user.address}" />
	<br /> <b><spring:message code="user.birthDate" />
		<spring:message code="user.birthDate.pattern" var="datePattern" />: </b>
	<fmt:formatDate value="${user.birthDate}" pattern="${datePattern}" />
	<br />
</div>

<jstl:choose>
	<jstl:when test="${requestURI ne 'actor/actor/profile.do'}">

		<display:table name="rendezvouses" id="row" requestURI="${requestURI}" pagesize="5">
			
			<jstl:set var="isDeleted" value="${row.isDeleted}" />
			<jstl:set var="isDraft" value="${row.isDraft}" />
			<jstl:set var="isAdult" value="${row.isAdultOnly}" />
			
			<jstl:if test="${isDeleted eq true}">
				<jstl:set var="style" value="background-color:SlateGray; color: white;" />
			</jstl:if>
			
			<jstl:if test="${(isDeleted eq false) && (isDraft eq false) && (isAdult eq false)}">
				<jstl:set var="style" value="background-color:lightSeaGreen; color: white;" />
			</jstl:if>
			
			<jstl:if test="${(isDeleted eq false) && (isDraft eq false) && (isAdult eq true)}">
				<jstl:set var="style" value="background-color:brown; color: white;" />
			</jstl:if>
			
			<jstl:if test="${(isDeleted eq false) && (isDraft eq true) && (isAdult eq true)}">
				<jstl:set var="style" value="background-color:brown; color:white;" />
			</jstl:if>
			
			<jstl:if test="${(isDeleted eq false) && (isDraft eq true) && (isAdult eq false)}">
				<jstl:set var="style" value="background-color:sandyBrown;" />
			</jstl:if>
			
			<spring:message code="rendezvous.name" var="nameHeader" />
			<display:column property="name" title="${nameHeader}" style="${style}"/>
			
			<spring:message code="rendezvous.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" style="${style}" />
			
			<spring:message code="rendezvous.meetingMoment" var="meetingMomentHeader" />
			<spring:message code="rendezvous.meetingMoment.pattern" var="datePattern"/>
			<display:column title="${meetingMomentHeader}" style="${style}">
				<fmt:formatDate value="${row.meetingMoment}" pattern="${datePattern}"/>
			</display:column>
			
			<spring:message code="rendezvous.picture" var="pictureHeader" />
			<display:column title="${pictureHeader}" style="${style}">
				<acme:image imageURL="${row.picture}" imageNotFoundLocation="images/fotoNotFound.png" 
					codeError="rendezvous.unspecifiedImage" height="60" width="60"/>
			</display:column>
			
			<spring:message code="rendezvous.gpsPoint.latitude" var="gpsPointLatitudeHeader" />
			<display:column property="gpsPoint.latitude" title="${gpsPointLatitudeHeader}" style="${style}"/>
			
			<spring:message code="rendezvous.gpsPoint.longitude" var="gpsPointLongitudeHeader" />
			<display:column property="gpsPoint.longitude" title="${gpsPointLongitudeHeader}" style="${style}" />
		
		</display:table>
		
		<jstl:if test="${not empty rendezvouses}">
			<span style="background-color:lightSeaGreen; color: white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.allPublic"/>&nbsp;&nbsp;</span>
			<span style="background-color:brown; color: white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.adult"/>&nbsp;&nbsp;</span>
			<span style="background-color:sandyBrown; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.draft"/>&nbsp;&nbsp;</span>
			<span style="background-color:SlateGray; color:white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.deleted"/>&nbsp;&nbsp;</span>
		</jstl:if>
	</jstl:when>
	
	<jstl:when test="${requestURI eq 'actor/actor/profile.do'}">
	
		<security:authorize access="hasRole('ADMIN')">
		<br><a href="administrator/administrator/edit.do"> <spring:message code="administrator.edit" /></a>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
		<br><a href="user/user/edit.do"> <spring:message code="user.edit" /></a>
		</security:authorize>
	
	</jstl:when>

</jstl:choose>