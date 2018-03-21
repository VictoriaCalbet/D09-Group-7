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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authentication property="principal" var="loggedactor"/>

<display:table name="services" id="row" requestURI="${requestURI}" pagesize="5">
	
	<jstl:choose>
		<jstl:when test="${row.isInappropriate eq true}">
			<jstl:set var="style" value="background-color:#F95734; color:white;"/>
		</jstl:when>
		<jstl:otherwise>
			<jstl:set var="style" value="background-color:#6FD6FF;"/>
		</jstl:otherwise>
	</jstl:choose>

	<!-- Links to edit or display a service -->
	<security:authorize access="hasRole('MANAGER')">
		<display:column style="${style}">
			<jstl:choose>
				<jstl:when test="${row.manager.userAccount.id eq loggedactor.id and empty row.requests and row.isInappropriate eq false}">
					<spring:message var="serviceEditLink" code="service.edit"/>
					<a href="service/manager/edit.do?serviceId=${row.id}"><jstl:out value="${serviceEditLink}"/></a>
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="service.list.noEditable" var="serviceNoEditableMessage" />
					<jstl:out value="${serviceNoEditableMessage}"/>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>

	<security:authorize access="hasAnyRole('USER', 'MANAGER', 'ADMIN')">
		<display:column style="${style}">
			<spring:message code="service.display" var="serviceDisplayLink"/>
			<a href="${displayURI}${row.id}"><jstl:out value="${serviceDisplayLink}"/></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column style="${style}">
			<jstl:choose>
				<jstl:when test="${row.isInappropriate eq false}">
					<spring:message code="service.markAsInappropriate" var="serviceMarkAsInappropriateLink"/>
					<a href="service/administrator/markAsInappropriate.do?serviceId=${row.id}"><jstl:out value="${serviceMarkAsInappropriateLink}"/></a>
				</jstl:when>
				<jstl:when test="${row.isInappropriate eq true}">
					<spring:message code="service.unmarkAsInappropriate" var="serviceUnmarkAsInappropriateLink"/>
					<a href="service/administrator/unmarkAsInappropriate.do?serviceId=${row.id}"><jstl:out value="${serviceUnmarkAsInappropriateLink}"/></a>
				</jstl:when>
			</jstl:choose>
		</display:column>
	</security:authorize>
	


	
	<spring:message code="service.name" var="serviceNameHeader"/>
	<display:column property="name" title="${serviceNameHeader}" style="${style}"/>
	
	<spring:message code="service.description" var="serviceDescriptionHeader"/>
	<display:column property="description" title="${serviceDescriptionHeader}" style="${style}"/>
	
	<spring:message code="service.isInappropriate" var="serviceIsInappropriateHeader"/>
	<display:column title="${serviceIsInappropriateHeader}" style="${style}">
		<jstl:choose>
			<jstl:when test="${row.isInappropriate eq true}">
				<spring:message code="service.isInappropriate.yes" var="serviceIsInappropriateYesInformation"/>
				<jstl:out value="${serviceIsInappropriateYesInformation}"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="service.isInappropriate.no" var="serviceIsInappropriateNoInformation"/>
				<jstl:out value="${serviceIsInappropriateNoInformation}"/>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	<spring:message code="service.pictureURL" var="servicepictureURLHeader"/>
	<display:column title="${servicepictureURLHeader}" style="${style}">
		<acme:image imageURL="${row.pictureURL}" imageNotFoundLocation="images/fotoNotFound.png" 
					codeError="service.unspecifiedURL" height="60" width="60"/>
	</display:column>
</display:table>

<jstl:if test="${not empty services}">
	<span style="background-color:#F95734; color:white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="service.list.thisServiceIsInappropriate"/>&nbsp;&nbsp;</span>
	<span style="background-color:#6FD6FF; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="service.list.thisServiceNotIsInappropriate"/>&nbsp;&nbsp;</span>
</jstl:if>
<br/>
<security:authorize access="hasRole('MANAGER')">
	<spring:message code="service.create" var="serviceCreateLink"/>
	<a href="service/manager/create.do"><jstl:out value="${serviceCreateLink}"/></a>
	<br/> 
</security:authorize>
<br/>


<acme:cancel url="welcome/index.do" code="service.cancel"/>
<br/>