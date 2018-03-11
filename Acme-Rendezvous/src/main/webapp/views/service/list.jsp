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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<security:authentication property="principal" var="loggedactor"/>

<display:table name="services" id="row" requestURI="${requestURI}" pagesize="10">

	<!-- Links to edit or display a service -->
	<security:authorize access="hasRole('MANAGER')">
		<display:column>
			<jstl:if test="${row.manager.userAccount.id eq loggedactor.id}">
				<a href="service/manager/edit.do?serviceId=${row.id}">
					<spring:message code="service.edit"/>
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<security:authorize access="isAnyRole('USER', 'MANAGER', 'ADMIN')">
		<display:column>
			<a href="${displayURI}${row.id}">
				<spring:message code="service.display"/>
			</a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${row.manager.userAccount.id eq loggedactor.id}">
					<spring:message code="service.markAsInapropiate" var="serviceMarkAsInapropiateLink"/>
					<a href="service/administrator/markAsInapropiate.do?serviceId=${row.id}"> <jstl:out value="${serviceMarkAsInapropiateLink}"/> </a>
				</jstl:when>
			</jstl:choose>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('MANAGER')">
		<display:column>
			<jstl:if test="${row.manager.userAccount.id eq loggedactor.id}">
				<a href="service/manager/edit.do?serviceId=${row.id}">
					<spring:message code="service.edit"/>
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<spring:message code="service.name" var="serviceNameHeader"/>
	<display:column property="name" title="${serviceNameHeader}" />
	
	<spring:message code="service.description" var="serviceDescriptionHeader"/>
	<display:column property="description" title="${serviceDescriptionHeader}" />
	
	<spring:message code="service.picture" var="servicePictureHeader"/>
	<display:column property="picture" title="${servicePictureHeader}"/>
	
	<spring:message code="service.isInapropiate" var="serviceIsInapropiateHeader"/>
	<display:column property="isInapropiate" title="${serviceIsInapropiateHeader}"/>
</display:table>

<security:authorize access="hasRole('MANAGER')">
	<spring:message code="service.create" var="serviceCreateLink"/>
	<a href="service/manager/create.do"> <jstl:out value="${serviceCreateLink}"/> </a>
</security:authorize>

<spring:message code="service.cancel" var="serviceCancelLink"/>
<a href="welcome/index.do"> <jstl:out value="${serviceCancelLink}"/> </a>