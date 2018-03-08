<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('SPONSOR')">
	<display:table name="sponsorships" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag" >
		<spring:message code="sponsorship.edit" var="edit"/>
		<display:column sortable="false" title="${edit}">
			<a href="sponsorship/sponsor/edit.do?sponsorshipId=${row.id}">
				<spring:message code="sponsorship.edit" />
			</a>
		</display:column>
		<spring:url var="infoURL" value="${row.infoPage}"/>
		<spring:message code="sponsorship.information" var="information"/>
		<display:column sortable="false" title="${information}">
			<a href="${infoURL}">
				${infoURL}
			</a>
		</display:column>
		<spring:url var="bannerURL" value="${row.bannerUrl}"/>
		<spring:message code="sponsorship.banner" var="banner_url"/>
		<display:column sortable="false" title="${banner_url}">
			<a href="${bannerURL}">
				${bannerURL}
			</a>
		</display:column>
		<spring:message code="sponsorship.holder" var="holder_var"/>
		<display:column property="creditCard.holderName" title="${holder_var}" sortable="false"/>
		
		<spring:message code="sponsorship.brand" var="brand_var"/>
		<display:column property="creditCard.brandName" title="${brand_var}" sortable="false"/>
		
		<spring:message code="sponsorship.number" var="number_var"/>
		<display:column property="creditCard.number" title="${number_var}" sortable="false"/>
		
		<spring:message code="sponsorship.month" var="month_var"/>
		<display:column property="creditCard.expirationMonth" title="${month_var}" sortable="false"/>
		
		<spring:message code="sponsorship.year" var="year_var"/>
		<display:column property="creditCard.expirationYear" title="${year_var}" sortable="false"/>	
			
		<display:column property="creditCard.cvv" title="cvv" sortable="false"/>	
		
		<spring:message code="sponsorship.trip" var="trip"/>
		<display:column sortable="false" title="${trip}">
			<a href="http://localhost:8080/Acme-Explorer/trip/info.do?tripId=${row.trip.id}">
				<spring:message code="sponsorship.displayTrip"/>
			</a>
		</display:column>
		
		
	
	</display:table>
	<a href="sponsorship/sponsor/create.do"><spring:message code="sponsorship.create"/></a>
</security:authorize>