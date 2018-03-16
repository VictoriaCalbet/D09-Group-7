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

<display:table name="services" id="row" requestURI="${requestURI}" pagesize="10">

	<!-- Links to edit or display a service -->
	<security:authorize access="hasRole('MANAGER')">
		<display:column>
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
		<display:column>
			<spring:message code="service.display" var="serviceDisplayLink"/>
			<a href="${displayURI}${row.id}"><jstl:out value="${serviceDisplayLink}"/></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
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
	<display:column property="name" title="${serviceNameHeader}" />
	
	<spring:message code="service.description" var="serviceDescriptionHeader"/>
	<display:column property="description" title="${serviceDescriptionHeader}" />
	
	<spring:message code="service.requests" var="serviceRequestsHeader" />
	<security:authorize access="hasRole('USER')">
		<display:column title="${serviceRequestsHeader}">
			<jstl:if test="${row.isInappropriate eq false}">
				<jstl:choose>
					<jstl:when test="${!fn:contains(servicesPrincipal, row)}">
						<spring:message code="service.requestThisService" var="serviceRequestThisServiceLink"/>
						<a href="request/user/create.do?serviceId=${row.id}"><jstl:out value="${serviceRequestThisServiceLink}"/></a>
					</jstl:when>
					<jstl:otherwise>
						<spring:message code= "service.requested" var="serviceRequestedMessage"/>
						<jstl:out value="${serviceRequestedMessage}"/>
					</jstl:otherwise>
				</jstl:choose>
			</jstl:if>	
		</display:column>
	</security:authorize>
	
	<spring:message code="service.pictureURL" var="servicepictureURLHeader"/>
	<display:column title="${servicepictureURLHeader}">
		<acme:image imageURL="${row.pictureURL}" imageNotFoundLocation="images/fotoNotFound.png" 
					codeError="service.unspecifiedURL" height="60" width="60"/>
	</display:column>
</display:table>

<security:authorize access="hasRole('MANAGER')">
	<spring:message code="service.create" var="serviceCreateLink"/>
	<a href="service/manager/create.do"><jstl:out value="${serviceCreateLink}"/></a>
	<br/> 
</security:authorize>
<br/>

<acme:cancel url="welcome/index.do" code="service.cancel"/>
<br/>