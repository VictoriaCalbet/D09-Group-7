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

<jsp:useBean id="now" class="java.util.Date" />

<div>
	<b><spring:message code="manager.name" />: </b>
	<jstl:out value="${manager.name}" />
	<br /> <b><spring:message code="manager.surname" />: </b>
	<jstl:out value="${manager.surname}" />
	<br /> <b><spring:message code="manager.vat" />: </b>
	<jstl:out value="${manager.VAT}" />
	<br /> <b><spring:message code="manager.email" />: </b>
	<jstl:out value="${manager.email}" />
	<br /> <b><spring:message code="manager.phone" />: </b>
	<jstl:out value="${manager.phone}" />
	<br /> <b><spring:message code="manager.address" />: </b>
	<jstl:out value="${manager.address}" />
	<br /> <b><spring:message code="manager.birthDate" />
		<spring:message code="manager.birthDate.pattern" var="datePattern" />: </b>
	<fmt:formatDate value="${manager.birthDate}" pattern="${datePattern}" />
	<br />
</div>
		
	<security:authorize access="hasRole('MANAGER')">
	<br><a href="manager/manager/edit.do"> <spring:message code="manager.edit" /></a>
	</security:authorize>
	