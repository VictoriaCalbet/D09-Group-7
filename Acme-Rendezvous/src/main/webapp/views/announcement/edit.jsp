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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authentication property="principal" var="loggedactor"/>

<form:form action="${requestURI}" modelAttribute="announcementForm">
	
	<jstl:choose>
		<jstl:when test="${not empty availableRendezvouses}">
			<!-- Hidden attributes -->
			
			<form:hidden path="id"/>
			
			<jstl:if test="${announcementForm.id ne 0 }">
				<form:hidden path="rendezvousId"/>
			</jstl:if>
			
			<!-- Attributes -->
			
			<b><form:label path="rendezvousId"/><spring:message code="announcement.rendezvous"/>:&nbsp;</b>
		
			<jstl:choose>
				<jstl:when test="${announcementForm.id eq 0}">
					<form:select path="rendezvousId">
						<form:options items="${availableRendezvouses}" itemLabel="name" itemValue="id"/>
					</form:select>
				</jstl:when>
					
				<jstl:when test="${announcementForm.id ne 0}">
					<jstl:out value="${rendezvousName}"/>
				</jstl:when>
			</jstl:choose>
			
			<acme:textbox code="announcement.title" path="title"/>
			<acme:textarea code="announcement.description" path="description"/>
		
			<!-- Action buttons -->
			
			<acme:submit name="save" code="announcement.save"/> &nbsp;
			<acme:cancel url="/announcement/user/list.do" code="announcement.cancel"/>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="message.rendezvousesNotAvailable"/>
		</jstl:otherwise>
	</jstl:choose>
</form:form>