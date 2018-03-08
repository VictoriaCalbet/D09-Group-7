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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="now" class="java.util.Date"/>

	<jstl:if test="${requestURI ne 'trip/manager/list.do'}">
		<form action="trip/searchWord.do" method="post">
			<label> <spring:message code="trip.keyWord" />
			</label> <input type="text" name="word" /> <input type="submit"
				name="searchWord" value="<spring:message code="trip.KeyWord.filter"/>" />
		</form>
		<br>
		
		<a href="category/list.do"><spring:message code="trip.browseTripsByCategory"/></a>
		<br><br>
	</jstl:if>
	
<security:authentication property="principal" var="loggedactor"/>

<display:table name="trips" id="row" requestURI="${requestURI}"
	pagesize="5">
	
	<security:authorize access = "hasRole('EXPLORER')">
		<spring:message code="trip.applications" var="applicationHeader" />	
		<display:column title="${applicationHeader}">
		
			<%-- Check if the current user has already applied to this trip --%>
			<jstl:set var="contains" value="${false}"/>
			<jstl:forEach items="${tripsToUserAccount[row.id]}" var="userAccountID">
				<jstl:if test="${userAccountID eq loggedactor.id}">
					<jstl:set var="contains" value="${true}"/>
				</jstl:if>
			</jstl:forEach>
			
			<jstl:if test="${contains eq false and row.cancelled eq false and row.startMoment.time > now.time}">
				<a href="application/explorer/create.do?tripId=${row.id}">
					<spring:message code="trip.join" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>


	<spring:message code="trip.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />
		
	<spring:message code="trip.moreInfo" var="moreInfoHeader" />	
	<display:column title="${moreInfoHeader}">	
		<a href="trip/info.do?tripId=${row.id}">
		 	<spring:message code="trip.moreInfoButton" />
		</a>
	</display:column>
	
	<security:authorize access="hasRole('MANAGER')">
	
		<spring:message code="trip.editTrip" var="editTripHeader" />	
		<display:column title="${editTripHeader}">	
			<jstl:if test="${now.time < row.publicationDate.time}">
			
				<a href="trip/manager/edit.do?tripId=${row.id}">
			 		<spring:message code="trip.editTripButton" />
			 	</a>
			</jstl:if>
		</display:column>
			
		<spring:message code="trip.deleteTrip" var="deleteTripHeader" />	
		<display:column title="${deleteTripHeader}">
			<jstl:if test="${now.time < row.publicationDate.time}">
				<a href="trip/manager/delete.do?tripId=${row.id}">
			 		<spring:message code="trip.deleteTripButton" />
			 	</a>
			</jstl:if>
		</display:column>
		
		<spring:message code="trip.cancelTrip" var="cancelTripHeader" />	
		<display:column title="${cancelTripHeader}">
			<jstl:if test="${!row.cancelled && now.time < row.startMoment.time && row.manager.userAccount.username==loggedactor.username}">	
				<a href="trip/manager/cancel.do?tripId=${row.id}">
			 		<spring:message code="trip.cancelTripButton" />
			 	</a>
			 </jstl:if>
		</display:column>
	</security:authorize>
	
	
	<spring:message code="trip.stages" var="stageHeader" />	
	<display:column title="${stageHeader}">	
		<a href="stage/list.do?tripId=${row.id}">
		 		<spring:message code="trip.stagesButton" />
		</a>
	</display:column>
	
	<spring:message code="trip.audits" var="auditHeader" />	
	<display:column title="${auditHeader}">
	<jstl:choose>
		<jstl:when  test = "${fn:length(row.audits) ==0}">	
			<spring:message code="trip.noAudits"/>
		</jstl:when>
		<jstl:otherwise> 
			<a href="audit/list.do?tripId=${row.id}"> 
		<spring:message code="audit.listFromTrip"/>
		</a>	</jstl:otherwise>
	</jstl:choose>
	</display:column>
	
	<security:authorize access = "hasRole('EXPLORER')">
		<spring:message code="trip.survivalClasses" var="survivalClassHeader" />	
		<display:column title="${survivalClassHeader}">
		<jstl:choose>
			<jstl:when  test = "${fn:length(row.survivalClasses) !=0}">	
			<a href="survivalClass/explorer/list.do?tripId=${row.id}"> 
			<spring:message code="trip.survivalClassesButton"/>
			</a>
			</jstl:when>
			<jstl:otherwise> <spring:message code="trip.noSurvivalClasses"/>	</jstl:otherwise>
		</jstl:choose>
		</display:column>
		</security:authorize>
		
		<security:authorize access = "hasRole('MANAGER')">
		<spring:message code="trip.survivalClasses" var="survivalClassHeader" />	
		<display:column title="${survivalClassHeader}">
		<jstl:choose>
			<jstl:when  test = "${fn:length(row.survivalClasses) !=0}">	
			<a href="survivalClass/manager/listByTrip.do?tripId=${row.id}"> 
			<spring:message code="trip.survivalClassesButton"/>
			</a>
			</jstl:when>
			<jstl:otherwise> <spring:message code="trip.noSurvivalClasses"/>	</jstl:otherwise>
		</jstl:choose>
		</display:column>
		</security:authorize>
			
	<security:authorize access="hasRole('MANAGER')">
	<spring:message code="trip.notes" var="noteHeader" />	
	<display:column title="${noteHeader}">
	<jstl:if test="${row.manager.userAccount.username==loggedactor.username}">
	<jstl:choose>
		<jstl:when  test = "${fn:length(row.notes) !=0}">	
		<a href="note/manager/list.do?tripId=${row.id}"> 
		<spring:message code="note.listFromTrip"/>
		</a>
		</jstl:when>
		<jstl:otherwise> <spring:message code="trip.noNotes"/>	</jstl:otherwise>
	</jstl:choose>
	</jstl:if>
	</display:column>
	
	
		<spring:message code="trip.applications" var="applicationHeader" />	
		<display:column title="${applicationHeader}">
		<jstl:choose>
			<jstl:when  test = "${fn:length(row.applications) !=0 && row.manager.userAccount.username==loggedactor.username}">	
			<a href="application/manager/list.do?tripId=${row.id}"> 
			<spring:message code="trip.applicationsButton"/>
			</a>
			</jstl:when>
			<jstl:otherwise> <spring:message code="trip.noApplications"/>	</jstl:otherwise>
		</jstl:choose>
		</display:column>
		
	</security:authorize>
	<spring:message code="trip.stories" var="storyHeader" />	
	<display:column title="${storyHeader}">
	<jstl:choose>
		<jstl:when  test = "${fn:length(row.stories) !=0}">	
		<a href="story/list.do?tripId=${row.id}"> 
		<spring:message code="story.listFromTrip"/>
		</a>
		</jstl:when>
		<jstl:otherwise> <spring:message code="trip.noStories"/>	</jstl:otherwise>
	</jstl:choose>
	</display:column>
</display:table>
<security:authorize access="hasRole('MANAGER')">
	
	<div>
		<b><a href="trip/manager/create.do"> 
			<spring:message code="trip.create"/>
		</a></b>
	</div>
</security:authorize>