<%--
 * display.jsp
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

<h3>
	<spring:message code="announcement.title" var="announcementTitleHeader"/>
	<b><jstl:out value="${announcementTitleHeader}"/> :&nbsp;</b> <jstl:out value="${announcement.title}"/>
</h3>

<spring:message code="announcement.rendezvous" var="announcementRendezvousHeader" />
<b><jstl:out value="${announcementRendezvousHeader}"/> :&nbsp;</b> <jstl:out value="${announcement.rendezvous.name}"/>
<br/>

<spring:message code="announcement.momentMade" var="announcementMomentMadeHeader"/>
<spring:message code="announcement.momentMade.pattern" var="datePattern"/>
<b><jstl:out value="${announcementMomentMadeHeader}"/> :&nbsp;</b> <fmt:formatDate value="${announcement.momentMade}" pattern="${datePattern}"/>
<br/>

<spring:message code="announcement.description" var="announcementDescriptionHeader" />
<b><jstl:out value="${announcementDescriptionHeader}"/> :&nbsp;</b> <jstl:out value="${announcement.description}"/>
<br/>

<acme:cancel url="/announcement/list.do?rendezvousId=${announcement.rendezvous.id}" code="announcement.cancel"/>