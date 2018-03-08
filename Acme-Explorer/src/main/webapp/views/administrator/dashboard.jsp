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
<br>
	<!-- Dashboard 1 -->
	<h3><spring:message code="administrator.averageApplicationsPerTrip"/></h3>
	<h4><jstl:out value="${averageApplicationsPerTrip}"></jstl:out>	</h4>

	<!-- Dashboard 2 -->
	<h3><spring:message code="administrator.minimumApplicationsPerTrip"/></h3>
	 	<h4><jstl:out value="${minimumApplicationsPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 3 -->
	<h3><spring:message code="administrator.maximumApplicationsPerTrip"/></h3>
	<h4><jstl:out value="${maximumApplicationsPerTrip}"></jstl:out></h4>
	
	
	<!-- Dashboard 4 -->
	<h3><spring:message code="administrator.standardDeviationOfApplicationsPerTrip"/></h3>
	<h4><jstl:out value="${standardDeviationApplicationsPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 5 -->
	<h3><spring:message code="administrator.averageTripsPerManager"/></h3>
	<h4><jstl:out value="${averageTripsPerManager}"></jstl:out></h4>
	
	<!-- Dashboard 6 -->
	<h3><spring:message code="administrator.minimumTripsPerManager"/></h3>
	<h4><jstl:out value="${minimumTripsPerManager}"></jstl:out></h4>
	
	<!-- Dashboard 7 -->
	<h3><spring:message code="administrator.maximumTripsPerManager"/></h3>
	<h4><jstl:out value="${maximumTripsPerManager}"></jstl:out></h4>
	
	<!-- Dashboard 8-->
	<h3><spring:message code="administrator.standardDeviationOfTripPerManager"/></h3>
	<h4><jstl:out value="${standardDeviationOfTripPerManager}"></jstl:out></h4>
	
	<!-- Dashboard 9-->
	<h3><spring:message code="administrator.averagePriceOfTrips"/></h3>
	<h4><jstl:out value="${averagePriceOfTrips}"></jstl:out></h4>
	
	<!-- Dashboard 10-->
	<h3><spring:message code="administrator.minimumPriceOfTrips"/></h3>
	<h4><jstl:out value="${minimumPriceOfTrips}"></jstl:out></h4>
	
	<!-- Dashboard 11-->
	<h3><spring:message code="administrator.maximumPriceOfTrips"/></h3>
	<h4><jstl:out value="${maximumPriceOfTrips}"></jstl:out></h4>
	
	<!-- Dashboard 12-->
	<h3><spring:message code="administrator.standardDeviationPriceOfTrips"/></h3>
	<h4><jstl:out value="${standardDeviationPriceOfTrips}"></jstl:out></h4>
	
	<!-- Dashboard 13-->
	<h3><spring:message code="administrator.averageTripsPerRanger"/></h3>
	<h4><jstl:out value="${averageTripsPerRanger}"></jstl:out></h4>
	
	<!-- Dashboard 14-->
	<h3><spring:message code="administrator.minimumTripsPerRanger"/></h3>
	<h4><jstl:out value="${minimumTripsPerRanger}"></jstl:out></h4>
	
	<!-- Dashboard 15-->
	<h3><spring:message code="administrator.maximumTripsPerRanger"/></h3>
	<h4><jstl:out value="${maximumTripsPerRanger}"></jstl:out></h4>
	
	<!-- Dashboard 16-->
	<h3><spring:message code="administrator.standardDeviationTripsPerRanger"/></h3>
	<h4><jstl:out value="${standardDeviationTripsPerRanger}"></jstl:out></h4>
	
	<!-- Dashboard 17-->
	<h3><spring:message code="administrator.ratioApplicationsWithPending"/></h3>
	<h4><jstl:out value="${ratioApplicationsWithPending}"></jstl:out></h4>
	
	<!-- Dashboard 18-->
	<h3><spring:message code="administrator.ratioApplicationsWithDue"/></h3>
	<h4><jstl:out value="${ratioApplicationsWithDue}"></jstl:out></h4>
	
	<!-- Dashboard 19-->
	<h3><spring:message code="administrator.ratioApplicationsWithAccepted"/></h3>
	<h4><jstl:out value="${ratioApplicationsWithAccepted}"></jstl:out></h4>
	
	<!-- Dashboard 20-->
	<h3><spring:message code="administrator.ratioApplicationsWithCancelled"/></h3>
	<h4><jstl:out value="${ratioApplicationsWithCancelled}"></jstl:out></h4>
	
	<!-- Dashboard 21-->
	<h3><spring:message code="administrator.ratioCancelledTripsVersusTotal"/></h3>
	<h4><jstl:out value="${ratioCancelledTripsVersusTotal}"></jstl:out></h4>
	
	<!-- Dashboard 22-->
	
	<h3><spring:message code="administrator.tripsWithAtLeast10MoreApplicationsThanAverageOrderedByNumberOfApplications"/></h3>
	<display:table name="nameTripsWith10MoreThanAverageOrdered" id="row"  requestURI="${requestURI}">
	<spring:message code="administrator.trip" var="nameHeader" />
	<display:column property="title" title="${nameHeader}" sortable="false" >
			<jstl:out value="${row.title}"/>
			</display:column>
			<h3><spring:message code="administrator.numberApplications" var="numberApplicationsHeader"/></h3>
	<display:column  title="${numberApplicationsHeader}" sortable="false" >
		<jstl:out value="${fn:length(row.applications)}"/>
		</display:column>		
	</display:table>
	
	
	
	<!-- Dashboard 23-->
	<h3><spring:message code="administrator.numberTimesEachLegalTextsBeenReferenced"/></h3>
	<display:table name="legalTextName" id="row"  requestURI="${requestURI}">
	<spring:message code="administrator.legalTextName" var="nameHeader" />
	<display:column property="title" title="${nameHeader}" sortable="false" >
			<jstl:out value="${row.title}"/>
			</display:column>
		<spring:message code="administrator.timesReferenced" var="timesReferenced" />
	<display:column  title="${timesReferenced}" sortable="false" >
			<jstl:out value="${fn:length(row.trips)}"/>
			</display:column>
			
	</display:table>
	<!-- Dashboard 24-->
	<h3><spring:message code="administrator.averageNumberNotesPerTrip"/></h3>
	<h4><jstl:out value="${averageNumberNotesPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 25-->
	<h3><spring:message code="administrator.minimumNumberNotesPerTrip"/></h3>
	<h4><jstl:out value="${minimumNumberNotesPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 26-->
	<h3><spring:message code="administrator.maximumNumberNotesPerTrip"/></h3>
	<h4><jstl:out value="${maximumNumberNotesPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 27-->
	<h3><spring:message code="administrator.standardDeviationOfNumberNotesPerTrip"/></h3>
	<h4><jstl:out value="${standardDeviationOfNumberNotesPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 28-->
	<h3><spring:message code="administrator.averageAuditsPerTrip"/></h3>
	<h4><jstl:out value="${averageAuditsPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 29-->
	<h3><spring:message code="administrator.minimumAuditsPerTrip"/></h3>
	<h4><jstl:out value="${minimumAuditsPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 30-->
	<h3><spring:message code="administrator.maximumAuditsPerTrip"/></h3>
	<h4><jstl:out value="${maximumAuditsPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 31-->
	<h3><spring:message code="administrator.standardDeviationOfAuditsPerTrip"/></h3>
	<h4><jstl:out value="${standardDeviationOfAuditsPerTrip}"></jstl:out></h4>
	
	<!-- Dashboard 32-->
	<h3><spring:message code="administrator.ratioTripsWithAuditRecord"/></h3>
	<h4><jstl:out value="${ratioTripsWithAuditRecord}"></jstl:out></h4>

	<!-- Dashboard 33-->
	<h3><spring:message code="administrator.ratioRangersWithCurriculaRegistered"/></h3>
	<h4><jstl:out value="${ratioRangersWithCurriculaRegistered}"></jstl:out></h4>
	
	<!-- Dashboard 33-->
	<h3><spring:message code="administrator.ratioRangersWithCurriculumEndorsed"/></h3>
	<h4><jstl:out value="${ratioRangersWithCurriculumEndorsed}"></jstl:out></h4>
	
	<!-- Dashboard 34-->
	<h3><spring:message code="administrator.ratioOfSuspiciousManagers"/></h3>
	<h4><jstl:out value="${ratioOfSuspiciousManagers}"></jstl:out></h4>
	
</security:authorize>


