<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h3><strong><spring:message code="finder.filter"/></strong></h3>
<strong><spring:message code="finder.keyWord"/>: </strong><jstl:out value="${finder.keyWord}"/>
<br/>
<strong><spring:message code="finder.minPrice"/>: </strong><jstl:out value="${finder.minPrice}"/>
<br/>
<strong><spring:message code="finder.maxPrice"/>: </strong><jstl:out value="${finder.maxPrice}"/>
<br/>
<strong><spring:message code="finder.startDate"/>: </strong><jstl:out value="${finder.startDate}"/>
<br/>
<strong><spring:message code="finder.endDate"/>: </strong><jstl:out value="${finder.endDate}"/>
<br/>
<a href="finder/explorer/edit.do">
	<spring:message code="finder.edit"/>
</a>
<h3><strong><spring:message code="finder.result"/></strong></h3>
<display:table name="trips" id="row" requestURI="${requestURI}"
	pagesize="${pageSize}">

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
		<jstl:when  test = "${fn:length(row.audits) !=0}">	
		<a href="audit/list.do?tripId=${row.id}"> 
		<spring:message code="audit.listFromTrip"/>
		</a>
		</jstl:when>
		<jstl:otherwise> <spring:message code="trip.noAudits"/>	</jstl:otherwise>
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
		
		<spring:message code="trip.applications" var="applicationHeader" />	
		<display:column title="${applicationHeader}">
			<a href="application/explorer/create.do?tripId=${row.id}">
				<spring:message code="trip.join" />
			</a>
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