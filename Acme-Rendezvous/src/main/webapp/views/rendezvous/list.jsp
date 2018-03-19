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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authentication property="principal" var="loggedactor"/>

<security:authorize access="isAnonymous()">
	<jstl:if test="${not empty categories}">
		<form:form action="rendezvous/listCategory.do" modelAttribute="categoryForm">
			<acme:select items="${categories}" itemLabel="name" code="rendezvous.category" path="categoryId" optionalRow="true"/>	
				<!-- Action buttons -->
			<acme:submit name="save" code="rendezvous.search" />
			<br>
			<br>
		</form:form>
	</jstl:if>
</security:authorize>

<display:table name="rendezvouses" id="row" requestURI="${requestURI}" pagesize="5">

	<jstl:set var="isDeleted" value="${row.isDeleted}" />
	<jstl:set var="isDraft" value="${row.isDraft}" />
	<jstl:set var="isAdult" value="${row.isAdultOnly}" />
	
	
	
	<jstl:if test="${isDeleted eq true}">
		<jstl:set var="style" value="background-color:SlateGray; color: white;" />
	</jstl:if>
	
	<jstl:if test="${(isDeleted eq false) && (isDraft eq false) && (isAdult eq false)}">
		<jstl:set var="style" value="background-color:lightSeaGreen; color: white;" />
	</jstl:if>
	
	<jstl:if test="${(isDeleted eq false) && (isDraft eq false) && (isAdult eq true)}">
		<jstl:set var="style" value="background-color:brown; color: white;" />
	</jstl:if>
	
	<jstl:if test="${(isDeleted eq false) && (isDraft eq true) && (isAdult eq true)}">
		<jstl:set var="style" value="background-color:sandyBrown; color: white;" />
	</jstl:if>
	
	<jstl:if test="${(isDeleted eq false) && (isDraft eq true) && (isAdult eq false)}">
		<jstl:set var="style" value="background-color:sandyBrown;" />
	</jstl:if>


	<spring:message code="rendezvous.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" style="${style}"/>
	
	<spring:message code="rendezvous.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" style="${style}" />
	
	<spring:message code="rendezvous.meetingMoment" var="meetingMomentHeader" />
	<spring:message code="rendezvous.meetingMoment.pattern" var="datePattern"/>
	<display:column title="${meetingMomentHeader}" style="${style}">
		<fmt:formatDate value="${row.meetingMoment}" pattern="${datePattern}"/>
	</display:column>
	
	
	<spring:message code="rendezvous.picture" var="pictureHeader" />
	<display:column title="${pictureHeader}" style="${style}">
		<acme:image imageURL="${row.picture}" imageNotFoundLocation="images/fotoNotFound.png" 
		codeError="rendezvous.unspecifiedImage" height="60" width="60"/>	
	</display:column>
	
	<spring:message code="rendezvous.gpsPoint.latitude" var="gpsPointLatitudeHeader" />
	<display:column property="gpsPoint.latitude" title="${gpsPointLatitudeHeader}" style="${style}"/>
	
	<spring:message code="rendezvous.gpsPoint.longitude" var="gpsPointLongitudeHeader" />
	<display:column property="gpsPoint.longitude" title="${gpsPointLongitudeHeader}" style="${style}" />
	
	<security:authorize access="hasRole('USER')"> 
	
				
	<spring:message code="rendezvous.isAdultOnly" var="isAdultOnlyHeader"/>
	<display:column title="${isAdultOnlyHeader}" style="${style}">
		<jstl:choose>
			<jstl:when test="${isAdult eq true}">
				<spring:message code="rendezvous.yes" var="adult"/>
				<jstl:out value="${adult}"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="rendezvous.no" var="notAdult"/>
				<jstl:out value="${notAdult}"/>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
				
	<spring:message code="rendezvous.isDraft" var="isDraftHeader"/>
	<display:column title="${isDraftHeader}" style="${style}">
		<jstl:choose>
			<jstl:when test="${isDraft eq true}">
				<spring:message code="rendezvous.yes" var="draft"/>
				<jstl:out value="${draft}"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="rendezvous.no" var="notDraft"/>
				<jstl:out value="${notDraft}"/>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	</security:authorize>
	
	<spring:message code="rendezvous.creator" var="creatorHeader" />	
	<display:column title="${creatorHeader}">	
		<a href="user/info.do?rendezvousId=${row.id}">
		 	<spring:message code="rendezvous.creatorButton" />
		</a>
	</display:column>
	
	<spring:message code="rendezvous.attendant" var="attendantHeader" />	
	<display:column title="${attendantHeader}">
	<jstl:choose>
		<jstl:when  test = "${fn:length(row.rsvps) ==0}">	
			<spring:message code="rendezvous.notAttendants"/>
		</jstl:when>
		<jstl:otherwise> 
			<a href="user/listAttendant.do?rendezvousId=${row.id}"> 
		<spring:message code="rendezvous.attendantButton"/>
		</a>	</jstl:otherwise>
	</jstl:choose>
	</display:column>
	
	<spring:message code="rendezvous.similar" var="similarHeader" />	
	<display:column title="${similarHeader}">
			<a href="rendezvous/list.do?rendezvousId=${row.id}"> 
		<spring:message code="rendezvous.similarButton"/>
		</a>	
	</display:column>
	
		<security:authorize access="hasAnyRole('ADMIN', 'MANAGER', 'USER')">
		<spring:message code="rendezvous.services" var="servicesHeader"/>
		<spring:message code="rendezvous.services.url" var="servicesURLMessage"/>
		
		<display:column title="${servicesHeader}">
			<security:authorize access="hasRole('USER')">
				<a href="service/user/list.do?rendezvousId=${row.id}"><jstl:out value="${servicesURLMessage}"/></a>
			</security:authorize>
			<security:authorize access="hasRole('MANAGER')">
				<a href="service/manager/list.do?rendezvousId=${row.id}"><jstl:out value="${servicesURLMessage}"/></a>
			</security:authorize>
			<security:authorize access="hasRole('ADMIN')">
				<a href="service/admin/list.do?rendezvousId=${row.id}"><jstl:out value="${servicesURLMessage}"/></a>
			</security:authorize>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
	<spring:message code="rendezvous.request" var="rendezvousRequestHeader"/>
		<display:column title="${rendezvousRequestHeader}">
			<jstl:choose>
				<jstl:when test="${row.isDeleted eq true}">
					<spring:message code= "rendezvous.request.isDeleted" var="rendezvousRequestIsDeletedMessage"/>
					<jstl:out value="${rendezvousRequestIsDeletedMessage}"/>
				</jstl:when>
				<jstl:when test="${row.isDraft eq true}">
					<spring:message code= "rendezvous.request.isDraft" var="rendezvousRequestIsDraftMessage"/>
					<jstl:out value="${rendezvousRequestIsDraftMessage}"/> 
				</jstl:when>
				<jstl:when test="${(fn:contains(principalRendezvouses, row))}">
					<spring:message code="service.requestThisService" var="serviceRequestThisServiceLink"/>
					<a href="request/user/create.do?rendezvousId=${row.id}"><jstl:out value="${serviceRequestThisServiceLink}"/></a>
				</jstl:when> 
			 	<jstl:otherwise>
					<spring:message code= "rendezvous.notOwner" var="rendezvousNotOwner"/>
					<jstl:out value="${rendezvousNotOwner}"/> 
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>
	
	<spring:message code="rendezvous.announcements" var="announcementsHeader" />
	<display:column title="${announcementsHeader}">
		<a href="announcement/list.do?rendezvousId=${row.id}">
			<spring:message code="rendezvous.showAnnouncements" />
		</a>
	</display:column>
	
	<spring:message code="rendezvous.comments" var="commentHeader" />	
	<display:column title="${commentHeader}">
		<a href="comment/list.do?rendezvousId=${row.id}"><spring:message code="rendezvous.commentButton"/></a>
	</display:column>
	
	
	<security:authorize access="hasRole('USER')">
		<spring:message code="rendezvous.delete" var="deleteHeader" />	
		<display:column title="${deleteHeader}">
			<jstl:choose>
				<jstl:when test="${!row.isDeleted && row.isDraft && row.creator.userAccount.username==loggedactor.username}">	
					<a href="rendezvous/user/delete.do?rendezvousId=${row.id}">
					 	<spring:message code="rendezvous.deleteButton" />
					</a>
				</jstl:when>
				<jstl:otherwise>
					<spring:message code= "rendezvous.notDeleted" var="rendezvousNotDeleted"/>
						<jstl:out value="${rendezvousNotDeleted}"/> 
				</jstl:otherwise>
			</jstl:choose>	
	
		</display:column>
		
		<spring:message code="rendezvous.edit" var="editHeader" />	
		<display:column title="${editHeader}">
			<jstl:choose>
			
				<jstl:when test="${!row.isDeleted && row.isDraft && row.creator.userAccount.username==loggedactor.username}">	
					<a href="rendezvous/user/edit.do?rendezvousId=${row.id}">
					 	<spring:message code="rendezvous.editButton" />
					</a>
				</jstl:when>
				<jstl:otherwise>
					<spring:message code= "rendezvous.notEdit" var="rendezvousNotEditable"/>
						<jstl:out value="${rendezvousNotEditable}"/> 
				</jstl:otherwise>
			</jstl:choose>	
				
				
		</display:column>
		
		<spring:message code="rendezvous.link" var="linkHeader" />	
		<display:column title="${linkHeader}">	
			<jstl:choose>
		
				<jstl:when test="${!row.isDeleted && row.creator.userAccount.username==loggedactor.username}">	
					<a href="rendezvous/user/link.do?rendezvousId=${row.id}">
					 	<spring:message code="rendezvous.linkButton" />
					</a>
				</jstl:when>
				<jstl:otherwise>
					<spring:message code= "rendezvous.notLinked" var="rendezvousNotLinked"/>
						<jstl:out value="${rendezvousNotLinked}"/> 
				</jstl:otherwise>
			</jstl:choose>
				
				
				
		</display:column>
		
		<spring:message code="rendezvous.RSVPButton" var="rsvpHeader" />
		<display:column title="${rsvpHeader}">
			<jstl:choose>
				<jstl:when test="${!principalRendezvouses.contains(row) and (row.isDraft eq false) and (row.isDeleted eq false)}">
					<a href="answer/user/respond.do?rendezvousId=${row.id}"> <spring:message code="rendezvous.RSVPButton" /></a>
				</jstl:when>
				
				<jstl:when test="${!principalRendezvouses.contains(row) and (row.isDraft eq true)}">
					<spring:message code="rendezvous.request.isDraft" />
				</jstl:when>
				<jstl:when test="${!principalRendezvouses.contains(row) and (row.isDeleted eq true)}">
					<spring:message code="rendezvous.request.isDeleted" />
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="rendezvous.AlreadyRSVPed" />
				</jstl:otherwise>
			</jstl:choose>	
		</display:column>
	
		<spring:message code="question.question" var="question"/>
		<display:column title="${question}" sortable="false">
			<a href="question/user/list.do?rendezvousId=${row.id}">
				<spring:message code="rendezvous.showquestions" />
			</a>
		</display:column>
		
		
		
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="rendezvous.delete" var="deleteHeader" />	
		<display:column title="${deleteHeader}">	
			<a href="rendezvous/administrator/delete.do?rendezvousId=${row.id}">
			 	<spring:message code="rendezvous.deleteButton" />
			</a>
		</display:column>
		
	</security:authorize>
</display:table>

<jstl:if test="${not empty rendezvouses}">
	<span style="background-color:lightSeaGreen; color:white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.allPublic"/>&nbsp;&nbsp;</span>
	<span style="background-color:brown; color: white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.adult"/>&nbsp;&nbsp;</span>
	<span style="background-color:sandyBrown; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.draft"/>&nbsp;&nbsp;</span>
	<span style="background-color:SlateGray; color: white; border-radius: 15px 50px;">&nbsp;&nbsp;<spring:message code="rendezvous.deleted"/>&nbsp;&nbsp;</span>
</jstl:if>