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

<display:table name="survivalClasses" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">
	
	<security:authorize access="hasRole('MANAGER')">
	<spring:message code="survivalClass.edit" var="survivalClassHeader" />	
	<display:column title="${survivalClassHeader}">
		
		<jstl:if test= "${row.trip.manager.id == principal.id}">
			<a href="survivalClass/manager/edit.do?survivalClassId=${row.id}"> <spring:message code="audit.edit" /></a>
		</jstl:if>
	</display:column>
	</security:authorize>
	
	<spring:message code="survivalClass.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="false" />
		<h1>
			<jstl:out value ="${row.title}"/> <br>
		</h1>			
					
	<spring:message code="survivalClass.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="false" />
		<h1>
			<jstl:out value ="${row.description}"/> <br>
		</h1>	
	
	<spring:message code="survivalClass.momentOrganized" var="momentOrganizedHeader" />
	<display:column title="${momentOrganizedHeader}">
	<fmt:formatDate value="${row.momentOrganized}" />
<!-- {0,date,dd/MM/yyyy HH:mm} -->
</display:column>
	
	
	<spring:message code="survivalClass.trip" var="titleHeader" />
	<display:column property="trip.title" title="${titleHeader}" sortable="false" />
		<h1>
			<jstl:out value ="${row.trip.title}"/> <br>
		</h1>	
	
	<spring:message code="survivalClass.location" var="titleHeader" />
	 <display:column title="${titleHeader}">
	
	<jstl:out value ="${row.location.name}:"/>
	<jstl:out value ="(${row.location.gpsPoint.latitude},"/> 
	<jstl:out value ="${row.location.gpsPoint.longitude})"/>
	</display:column>
				
	<security:authorize access="hasRole('MANAGER')">
	<div>
	<a href="survivalClass/manager/create.do"> 
		<spring:message code="survivalClass.create"/>
	</a>
	</div>
	</security:authorize>
	
	<security:authorize access="hasRole('MANAGER')">
	<div>
	<a href="survivalClass/manager/create.do"> 
		<spring:message code="survivalClass.create"/>
	</a>
	</div>
	</security:authorize>
	
	<security:authorize access="hasRole('EXPLORER')">
	<spring:message code="survivalClass.enrolASurvivalClass" var="titleHeader" />
	 
	 <display:column title="${titleHeader}">
			
				<jstl:if test="${!fn:contains(principal.survivalClasses,row.id) and fn:contains(enrolableSurvivalClasses,row.id)}">
						<a href="survivalClass/explorer/enrolAssure.do?survivalClassId=${row.id}"> <spring:message code="survivalClass.enrolASurvivalClass" /></a>
				</jstl:if>
				<jstl:if test="${!fn:contains(principal.survivalClasses,row.id) and !fn:contains(enrolableSurvivalClasses,row.id)}">
						<spring:message code="survivalClass.notEnrolable" />
				</jstl:if>
				
				<jstl:if test="${fn:contains(principal.survivalClasses,row.id) and fn:contains(enrolableSurvivalClasses,row.id)}">
						<spring:message code="survivalClass.enrolled" />
				</jstl:if>	
				<jstl:if test="${fn:contains(principal.survivalClasses,row.id) and !fn:contains(enrolableSurvivalClasses,row.id)}">
						<spring:message code="survivalClass.enrolled" />
				</jstl:if>	
 			
			</display:column>	
	
	</security:authorize>
	
</display:table><br/>
<security:authorize access="hasRole('MANAGER')">
	
			<a href="survivalClass/manager/create.do">
			 <spring:message code="survivalClass.create" /></a>
	
	</security:authorize>