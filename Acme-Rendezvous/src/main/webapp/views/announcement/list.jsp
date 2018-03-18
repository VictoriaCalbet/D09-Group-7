<%--
 * list.jsp
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

<display:table name="announcements" id="row" requestURI="${requestURI}" pagesize="10">
	
	<!-- Links to edit or display an announcement -->
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${row.rendezvous.creator.userAccount.id eq loggedactor.id}">
					<a href="announcement/user/edit.do?announcementId=${row.id}">
						<spring:message code="announcement.edit"/>
					</a>
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="announcement.list.noEditable" var="announcementNoEditableMessage"/>
					<jstl:out value="${announcementNoEditableMessage}"/> 
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="announcement/administrator/delete.do?announcementId=${row.id}">
				<spring:message code="announcement.delete"/>
			</a>
		</display:column>
	</security:authorize>
	
	<display:column>
		<a href="announcement/display.do?announcementId=${row.id}">
			<spring:message code="announcement.display"/>
		</a>
	</display:column>
	
	<spring:message code="announcement.title" var="announcementTitleHeader"/>
	<display:column property="title" title="${announcementTitleHeader}" />
	
	<spring:message code="announcement.rendezvous" var="announcementRendezVousHeader"/>
	<display:column property="rendezvous.name" title="${announcementRendezVousHeader}"/>
	
	<spring:message code="announcement.momentMade" var="announcementMomentMadeHeader"/>
	<spring:message code="announcement.momentMade.pattern" var="datePattern"/>
	<display:column title="${announcementMomentMadeHeader}">
		<fmt:formatDate value="${row.momentMade}" pattern="${datePattern}"/>
	</display:column>
</display:table>
<br/>
<br/>

<!-- Link to create an announcement. It's only visible by users -->
<security:authorize access="hasRole('USER')">
	<a href="announcement/user/create.do"> <spring:message code="announcement.create"/> </a>
</security:authorize>