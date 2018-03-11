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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authentication property="principal" var="loggedactor"/>

<h3>
	<spring:message code="service.name" var="serviceNameHeader"/>
	<b><jstl:out value="${serviceNameHeader}"/> :&nbsp;</b> <jstl:out value="${service.name}"/>
</h3>

<spring:message code="service.description" var="serviceDescriptionHeader"/>
<b><jstl:out value="${serviceDescriptionHeader}"/> :&nbsp;</b> <jstl:out value="${service.description}"/>
<br/>

<spring:message code="service.picture" var="servicePictureHeader" />
<b><jstl:out value="${servicePictureHeader}"/> :&nbsp;</b> <jstl:out value="${service.picture}"/>
<br/>

<spring:message code="service.isInapropiated" var="serviceIsInapropiatedHeader" />
<b><jstl:out value="${serviceIsInapropiatedHeader}"/> :&nbsp;</b> <jstl:out value="${service.isInapropiated}"/>
<br/>

<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${service.manager.userAccount.id eq loggedactor.id}">
		<spring:message var="serviceEditLink" code="service.edit"/>
		<a href="service/manager/edit.do?serviceId=${service.id}"><jstl:out value="${serviceEditLink}"/></a>
	</jstl:if>
</security:authorize>

<acme:cancel url="${cancelURI}" code="service.cancel"/>