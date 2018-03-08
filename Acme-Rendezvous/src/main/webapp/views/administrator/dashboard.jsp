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

<security:authorize access="hasRole('ADMIN')">
	<br/>
	<spring:message code="dashboard.avg" var="dashboardAvg"/>
	<spring:message code="dashboard.std" var="dashboardStd"/>
	<spring:message code="rendezvous.creator" var="rendezvousCreator"/>
	<spring:message code="rendezvous.name" var="rendezvousName"/>
	
	
	<!-- Dashboard 1 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdRendezvousesCreatedPerUser"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgRendezvousesCreatedPerUser}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdRendezvousesCreatedPerUser}"/></td>
		</tr>
	</table>
	
	<!-- Dashboard 2 -->
	<table border="1">
		<tr>
			<td> <b> <spring:message code="administrator.ratioUserRendezvousesCreatedVsNeverCreated"/>:&nbsp; </b> </td>
			<jstl:choose>
				<jstl:when test="${not empty ratioUserRendezvousesCreatedVsNeverCreated}">
					<td> <jstl:out value="${ratioUserRendezvousesCreatedVsNeverCreated}"/> </td>
				</jstl:when>
				<jstl:otherwise>
					<td> <spring:message code="dashboard.ratioUserRvCreatedVsNeverCreated"/> </td>
				</jstl:otherwise>
			</jstl:choose>
		</tr>
	</table>
	
	<!-- Dashboard 3 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdUsersRSVPsPerRendezvous"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgUsersRSVPsPerRendezvous}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdUsersRSVPsPerRendezvous}"/></td>
		</tr>
	</table>
	
	<!-- Dashboard 4 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdRendezvousRSVPsPerUsers"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgRendezvousRSVPsPerUsers}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdRendezvousRSVPsPerUsers}"/></td>
		</tr>
	</table>

	<!-- Dashboard 5 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.top10RendezvousByRSVPs"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${rendezvousCreator}"/>:&nbsp; </b> </td>
			<td> <b> <jstl:out value="${rendezvousName}"/>:&nbsp; </b> </td>
		</tr>
		<jstl:forEach items="${top10RendezvousByRSVPs}" var="rv">
			<tr>
				<td> <jstl:out value="${rv.creator.name}"/></td>
				<td> <jstl:out value="${rv.name}"/></td>
			</tr>
		</jstl:forEach>
	</table>
	
	<!-- Dashboard 6 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdAnnouncementPerRendezvous"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgAnnouncementPerRendezvous}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdAnnouncementPerRendezvous}"/></td>
		</tr>
	</table>
	
	<!-- Dashboard 7 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.rendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${rendezvousCreator}"/>:&nbsp; </b> </td>
			<td> <b> <jstl:out value="${rendezvousName}"/>:&nbsp; </b> </td>
		</tr>
		<jstl:forEach items="${rendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous}" var="rv">
			<tr>
				<td> <jstl:out value="${rv.creator.name}"/></td>
				<td> <jstl:out value="${rv.name}"/></td>
			</tr>
		</jstl:forEach>
	</table>
	
	<!-- Dashboard 8 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.rendezvousesThatLinkedToRvGreaterThanAvgPlus10"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${rendezvousCreator}"/>:&nbsp; </b> </td>
			<td> <b> <jstl:out value="${rendezvousName}"/>:&nbsp; </b> </td>
		</tr>
		<jstl:forEach items="${rendezvousesThatLinkedToRvGreaterThanAvgPlus10}" var="rv">
			<tr>
				<td> <jstl:out value="${rv.creator.name}"/></td>
				<td> <jstl:out value="${rv.name}"/></td>
			</tr>
		</jstl:forEach>
	</table>
	
	<!-- Dashboard 9 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdNoQuestionPerRendezvous"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgNoQuestionPerRendezvous}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdNoQuestionPerRendezvous}"/></td>
		</tr>
	</table>
	
	<!-- Dashboard 10 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdNoAnswersToTheQuestionsPerRendezvous"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgNoAnswersToTheQuestionsPerRendezvous}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdNoAnswersToTheQuestionsPerRendezvous}"/></td>
		</tr>
	</table>
	
	<!-- Dashboard 11 -->
	<table border="1">
		<tr>
			<td colspan="2"> <b> <spring:message code="administrator.avgAndStdRepliesPerComment"/>:&nbsp; </b> </td>
		</tr>
		<tr>
			<td> <b> <jstl:out value="${dashboardAvg}"/>:&nbsp; </b> <jstl:out value="${avgRepliesPerComment}"/></td>
			<td> <b> <jstl:out value="${dashboardStd}"/>:&nbsp; </b> <jstl:out value="${stdRepliesPerComment}"/></td>
		</tr>
	</table>
	
</security:authorize>