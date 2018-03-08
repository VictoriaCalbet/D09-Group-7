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

<jsp:useBean id="now" class="java.util.Date"/>

<display:table name="notes" id="row" requestURI="${requestURI}" pagesize="10" >

<security:authorize access="hasRole('MANAGER')">

<display:column>
<jstl:if test="${now.time > row.trip.publicationDate.time}">
<a href="note/manager/edit.do?noteId=${row.id}"><spring:message code="note.edit"/></a>
</jstl:if>
</display:column>

</security:authorize>

<spring:message code="note.momentCreated" var="creationMoment" />
<spring:message code="note.date" var="creationDate"/>
<display:column title="${creationMoment}"> <!-- DONE: format the date according to the language in use -->

<fmt:formatDate value="${row.creationMoment}" pattern="${creationDate}"/>
</display:column>
<spring:message code="note.remark" var="remark" />
<display:column property="remark" title="${remark}"><jstl:out value="${row.remark}"></jstl:out></display:column>

<spring:message code="note.response" var="response" />
<display:column property="response" title="${response}"><jstl:out value="${row.response}"></jstl:out></display:column>

<spring:message code="note.momentResponse" var="responseMoment" />
<spring:message code="note.date" var="responseDate"/>
<display:column title="${responseMoment}"> <!-- DONE: format the date according to the language in use -->
<fmt:formatDate value="${row.responseMoment}" pattern="${responseDate}"/>
</display:column>
<spring:message code="note.trip" var="noteTrip"/>
<display:column property="trip.title" title="${noteTrip}"><jstl:out value="${row.trip.title}"></jstl:out></display:column>

</display:table>

<security:authorize access="hasRole('AUDITOR')">
<jstl:if test="${noTrips==false}">
<a href="note/auditor/create.do">
<spring:message code="note.create"/></a>
</jstl:if>
</security:authorize>