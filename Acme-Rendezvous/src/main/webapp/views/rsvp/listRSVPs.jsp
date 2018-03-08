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

<security:authentication property="principal" var="loggedactor"/>

<display:table name="RSVPs" id="row" requestURI="${requestURI}" pagesize="5">

	
<spring:message code="rsvp.isCancelled" var="isCanceledHeader" />
<display:column property="isCancelled" title="${isCanceledHeader}" sortable="false" />

<spring:message code="rendezvous.name" var="rendezvousHeader" />
<display:column property="rendezvous.name" title="${rendezvousHeader}" sortable="false" />


<spring:message code="rsvp.isCancelled" />		
<display:column title="${cancelHeader}">			
			<jstl:choose>
	<jstl:when test="${row.isCancelled==false }">
	
		<a href="RSVP/user/cancelRSVP.do?rendezvousToCancelId=${row.rendezvous.id}">
			 	<spring:message code="RSVP.cancelButton" />
	</a>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="rsvp.isCancelled" />
		</jstl:otherwise>
		</jstl:choose>
	</display:column>
</display:table>