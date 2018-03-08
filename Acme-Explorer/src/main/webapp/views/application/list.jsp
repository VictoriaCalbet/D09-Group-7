<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>


<display:table name="applications" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<jsp:useBean id="now" class="java.util.Date"/>
		
	<jstl:set var="tripStartMoment" value="${row.trip.startMoment}" />
	<jstl:set var="nowPlusAMonth" value="${nowPlusAMonth}" />
	<jstl:set var="status" value="${row.status}" />
	
	<jstl:if test="${status eq 'PENDING' && tripStartMoment.time > nowPlusAMonth.time}">
		<jstl:set var="style" value="background-color:white;" />
	</jstl:if>
	
	<jstl:if test="${status eq 'PENDING' && tripStartMoment.time <= nowPlusAMonth.time}">
		<jstl:set var="style" value="background-color:red;" />
	</jstl:if>
	
	<jstl:if test="${status eq 'REJECTED'}">
		<jstl:set var="style" value="background-color:grey;" />
	</jstl:if>
	
	<jstl:if test="${status eq 'DUE'}">
		<jstl:set var="style" value="background-color:yellow;" />
	</jstl:if>
	
	<jstl:if test="${status eq 'ACCEPTED'}">
		<jstl:set var="style" value="background-color:green;" />
	</jstl:if>
	
	<jstl:if test="${status eq 'CANCELLED'}">
		<jstl:set var="style" value="background-color:cyan;" />
	</jstl:if>
	
	<security:authorize access="hasRole('MANAGER')">
	
		<spring:message code="application.reject" var="rejectHeader" />
		<display:column title="${rejectHeader}" style="${style}">
			<jstl:if test="${principalManager.id eq row.trip.manager.id && row.status eq 'PENDING'}">
				<a href="application/manager/reject.do?applicationId=${row.id}">
	 				 <spring:message code="application.reject" />
				</a>
			</jstl:if>
		</display:column>
		
		<spring:message code="application.due" var="dueHeader" />
		<display:column title="${dueHeader}" style="${style}">
			<jstl:if test="${principalManager.id eq row.trip.manager.id && row.status eq 'PENDING'&& row.trip.startMoment.time > now.time && row.trip.cancelled == false}">
				<a href="application/manager/due.do?applicationId=${row.id}">
	 				 <spring:message code="application.due" />
				</a>
			</jstl:if>
		</display:column>
		
	</security:authorize>
	
	<security:authorize access="hasRole('EXPLORER')">
	
		<spring:message code="application.accept" var="acceptHeader" />
		<display:column title="${acceptHeader}" style="${style}">
			<jstl:if test="${row.status eq 'DUE' && row.trip.startMoment.time > now.time  && row.trip.cancelled == false}">
				<a href="application/explorer/accept.do?applicationId=${row.id}">
	 				 <spring:message code="application.accept" />
				</a>
			</jstl:if>
		</display:column>
		
		<spring:message code="application.cancelled" var="cancelledHeader" />
		<display:column title="${cancelledHeader}" style="${style}">
			<jstl:if test="${row.status eq 'ACCEPTED' && row.trip.startMoment.time > now.time && row.trip.cancelled == false}">
				<a href="application/explorer/cancel.do?applicationId=${row.id}">
	 				 <spring:message code="application.cancelled" />
				</a>
			</jstl:if>
		</display:column>
	
	</security:authorize>
	
	<spring:message code="application.momentMade" var="momentMadeHeader" />
	<display:column property="momentMade" title="${momentMadeHeader}" sortable="true" style="${style}"/>
	
	<spring:message code="application.status" var="statusHeader" />
	<display:column property="status" title="${statusHeader}" sortable="true" style="${style}"/>
	
	<spring:message code="application.comment" var="commentHeader" />
	<display:column property="comment" title="${commentHeader}" style="${style}"/>
	
	<spring:message code="application.reasonDenied" var="reasonDeniedHeader" />
	<display:column property="reasonDenied" title="${reasonDeniedHeader}" style="${style}"/>
	
	<spring:message code="application.creditCard" var="creditCardHeader" />
	<display:column title="${creditCardHeader}" sortable="true" style="${style}">
		<jstl:if test="${row.creditCards[0] != null}">
			<spring:message text="${row.creditCards[0].number}"/>
		</jstl:if>
	</display:column>
	
	<spring:message code="application.trip" var="tripHeader" />
	<display:column property="trip.ticker" title="${tripHeader}" sortable="true" style="${style}"/>

</display:table>

<%-- <display:table pagesize="5" class="displaytag" keepStatus="true" --%>
<%-- 	name="applications" requestURI="${requestURI}" id="row"> --%>
		
<%-- 		<jstl:set var="tripStartMoment" value="${row.trip.startMoment}" /> --%>
<%-- 		<jstl:set var="nowPlusAMonth" value="${nowPlusAMonth}" /> --%>
<%-- 		<jstl:set value="${row.status}" var="status"/> --%>
	
<%-- 		<jstl:if test = "${fn:contains(status, 'PENDING')}"> --%>
<%-- 			<jstl:if test="${nowPlusAMonth >= tripStartMoment}"> --%>
<%-- 				<jstl:set var="style" value="background-red;" /> --%>
<%-- 			</jstl:if> --%>
<%-- 			<jstl:if test="${nowPlusAMonth.time < tripStartMoment.time}"> --%>
<%-- 				<jstl:set var="style" value="background-color:white;" /> --%>
<%-- 			</jstl:if> --%>
<%-- 		</jstl:if> --%>

<%-- 		<jstl:if test = "${fn:contains(status, 'REJECTED')}"> --%>
<%-- 			<jstl:set var="style" value="background-color:grey;" /> --%>
<%-- 		</jstl:if> --%>
		
<%-- 		<jstl:if test = "${fn:contains(status, 'DUE')}"> --%>
<%-- 			<jstl:set var="style" value="background-color:yellow;" /> --%>
<%-- 		</jstl:if> --%>
		
<%-- 		<jstl:if test = "${fn:contains(status, 'ACCEPTED')}"> --%>
<%-- 			<jstl:set var="style" value="background-color:green;" /> --%>
<%-- 		</jstl:if> --%>
		
<%-- 		<jstl:if test = "${fn:contains(status, 'CANCELLED')}"> --%>
<%-- 			<jstl:set var="style" value="background-color:cyan;" /> --%>
<%-- 		</jstl:if> --%>
	
<%-- 		<spring:message code="application.momentMade" var="momentMadeHeader" /> --%>
<%-- 		<display:column property="momentMade" title="${momentMadeHeader}" sortable="true" style="${style}" /> --%>
<%-- 		<fmt:formatDate value="${row.momentMade}"/> --%>
			
<%-- 		<spring:message code="application.status" var="statusHeader" /> --%>
<%-- 		<display:column property="status" title="${statusHeader}" style="${style}" sortable="true"/> --%>
				
<%-- 		<spring:message code="application.comment" var="commentHeader" /> --%>
<%-- 		<display:column property="comment" title="${commentHeader}" style="${style}" /> --%>
<%-- 		<h1> <jstl:out value ="${ row.getComment()}"/> </h1> --%>
<!-- 		<br> -->
		
<%-- 		<spring:message code="application.reasonDenied" var="reasonDeniedHeader" /> --%>
<%-- 		<display:column property="reasonDenied" title="${reasonDeniedHeader}" style="${style}"/> --%>
<%-- 		<jstl:if test="${row.reasonDenied != null}"> --%>
<%-- 			<h1> <jstl:out value ="${ row.getReasonDenied()}"/> </h1> --%>
<!-- 			<br> -->
<%-- 		</jstl:if> --%>
<%-- 		<security:authorize access="hasRole('EXPLORER')"> --%>
<%-- 			<jstl:if test="${fn:contains(status, 'ACCEPTED')}"> --%>
<%-- 					<spring:message code="application.creditCard.number" var="numberHeader" /> --%>
<%-- 					<display:column value="${row.creditCards[0].number }" title="${numberHeader}" style="${style}"/> --%>
<!-- 							<br/> -->
<%-- 			</jstl:if> --%>
<%-- 			<jstl:set value="${row.creditCards}" var="card"/> --%>
<%-- 			<jstl:if test = "${fn:contains(status, 'DUE')}"> --%>
<%-- 					<display:column style="${style}"> --%>
<%-- 					<a href="application/explorer/addCreditCard.do?applicationId=${row.id}"> --%>
<%-- 				 		<spring:message code="application.addCreditCard" /> --%>
<!-- 				 	</a> -->
<%-- 			 		</display:column> --%>
<%-- 			</jstl:if> --%>
			
<%-- 			<jstl:if test="${fn:contains(status, 'ACCEPTED')}"> --%>
<%-- 				<display:column style="${style}"> --%>
<%-- 					<a href="application/explorer/cancelApplication.do?applicationId=${row.id}"> --%>
<%-- 				 		<spring:message code="application.cancelApplication" /> --%>
<!-- 				 	</a> -->
<%-- 					</display:column> --%>
<%-- 			</jstl:if> --%>
<%-- 		</security:authorize> --%>
		
	
<%-- 		<security:authorize access="hasRole('MANAGER')"> --%>
<%-- 			<spring:message code="application.reject" var="applicationRejectHeader" />	 --%>
<%-- 			<display:column title="${applicationRejectHeader}" style="${style}"> --%>
<%-- 			 <jstl:if test = "${fn:contains(status, 'PENDING')}"> --%>
<%-- 				<a href="application/manager/reject.do?applicationId=${row.id}"> --%>
<%-- 					<spring:message code="application.reject" /> --%>
<!-- 				</a> -->
<%-- 			 </jstl:if> --%>
<%-- 			</display:column> --%>
			 	
<%-- 			<spring:message code="application.due" var="applicationDueHeader" />	 --%>
<%-- 			<display:column title="${applicationDueHeader}" style="${style}"> --%>
<%-- 			 <jstl:if test = "${fn:contains(status, 'PENDING')}"> --%>
<%-- 				<a href="application/manager/due.do?applicationId=${row.id}"> --%>
<%-- 					<spring:message code="application.due" /> --%>
<!-- 				</a> -->
<%-- 			 </jstl:if> --%>
<%-- 			</display:column> --%>
<%-- 		</security:authorize> --%>
	
<%-- </display:table> --%>
<!-- <br/> -->

<%-- <a href="welcome/index.do"> <spring:message code="application.back" /> </a> --%>