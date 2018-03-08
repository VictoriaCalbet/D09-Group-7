<%--
 * index.jsp
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


<jstl:if test="${pageContext.response.locale.language=='en'}">
	<p><b>${welcomeMessageEnglish}</b></p>
</jstl:if>

<jstl:if test="${pageContext.response.locale.language=='es'}">
	<p><b>${welcomeMessageSpanish}</b></p>
</jstl:if>

<security:authorize access="isAuthenticated()">
	<p><b><spring:message code="welcome.greeting.prefix" /> ${name}</b></p>
</security:authorize>