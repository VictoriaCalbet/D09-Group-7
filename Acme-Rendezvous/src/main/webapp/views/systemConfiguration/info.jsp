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

<div>
	<b><spring:message code="systemConfiguration.businessName" />: </b>
	<jstl:out value="${systemConfiguration.businessName}" />
	<br /> <b><spring:message code="systemConfiguration.bannerURL" />: </b>
	<jstl:out value="${systemConfiguration.bannerURL}" />
	<br /> <b><spring:message code="systemConfiguration.englishWelcomeMessage" />: </b>
	<jstl:out value="${systemConfiguration.englishWelcomeMessage}" />
	<br /> <b><spring:message code="systemConfiguration.spanishWelcomeMessage" />: </b>
	<jstl:out value="${systemConfiguration.spanishWelcomeMessage}" />
</div>

	<security:authorize access="hasRole('ADMIN')">
	<br><a href="systemConfiguration/administrator/edit.do"> <spring:message code="systemConfiguration.edit" /></a>
	</security:authorize>