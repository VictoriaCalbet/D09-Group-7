<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authentication property="principal" var ="loggedactor"/>

<display:table name="stories" id="row" requestURI="${requestURI}" pagesize="10">

<spring:message code="story.title" var="storyTitle"/>
<display:column property="title" title="${storyTitle}"><jstl:out value="${row.title}"></jstl:out></display:column>

<spring:message code="story.pieceOfText" var="storyPieceOfText"/>
<display:column property="pieceOfText" title="${storyPieceOfText}"><jstl:out value="${row.pieceOfText}"></jstl:out></display:column>

<spring:message code="story.attachments" var="storyAttachments"/>
<display:column property="attachmentUrls" title="${storyAttachments}"><jstl:out value="${row.attachmentUrls}"></jstl:out></display:column>

<spring:message code="story.trips" var="storyTrip"/>
<display:column property="trip.title" title="${storyTrip}"/>


</display:table>

<security:authorize access="hasRole('EXPLORER')">
<jstl:if test="${noTrips==false}">
<a href="story/explorer/create.do">
<spring:message code="story.create"/></a>
</jstl:if>
</security:authorize>