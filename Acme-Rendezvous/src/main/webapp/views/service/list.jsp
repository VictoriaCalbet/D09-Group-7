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
			<jstl:if test="${row.manager.userAccount.id eq loggedactor.id and empty row.requests}">
				<a href="service/manager/edit.do?serviceId=${row.id}">
					<spring:message code="service.edit"/>
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
	-
	<security:authorize access="hasAnyRole('USER', 'MANAGER', 'ADMIN')">
		<display:column>
			<a href="${displayURI}${row.id}">
				<spring:message code="service.display"/>
			</a>
		</display:column>
	</security:authorize>
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:if test="${row.isInappropriate eq false}">
				<spring:message code="service.markAsInappropriate" var="serviceMarkAsInappropriateLink"/>
				<a href="service/administrator/markAsInappropriate.do?serviceId=${row.id}"> <jstl:out value="${serviceMarkAsInappropriateLink}"/> </a>
			</jstl:if>				
		</display:column>
	</security:authorize>
	
	<spring:message code="service.name" var="serviceNameHeader"/>
	<display:column property="name" title="${serviceNameHeader}" />
	
	<spring:message code="service.description" var="serviceDescriptionHeader"/>
	<display:column property="description" title="${serviceDescriptionHeader}" />
	
	<spring:message code="service.pictureURL" var="servicepictureURLHeader"/>
	<display:column title="${servicepictureURLHeader}">
		<acme:image imageURL="${row.pictureURL}" imageNotFoundLocation="images/fotoNotFound.png" codeError="service.unspecifiedURL" height="60" width="60"/>
	</display:column>
</display:table>

<security:authorize access="hasRole('MANAGER')">
	<spring:message code="service.create" var="serviceCreateLink"/>
	<a href="service/manager/create.do"><jstl:out value="${serviceCreateLink}"/></a>
	<br/> 
</security:authorize>
<br/>
<acme:cancel url="welcome/index.do" code="service.cancel"/>