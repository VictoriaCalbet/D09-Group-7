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

<display:table name="audits" id="row" requestURI="${requestURI}" 
	pagesize="5" class="displaytag">
	
	<!-- 	Edit column -->
	<security:authorize access="hasRole('AUDITOR')">
	<jstl:choose>
	<jstl:when test="${readable}">
	<display:column>
		<jstl:choose>
	 		<jstl:when test="${row.isDraft==true and fn:contains(principal.audits,row.id)}">		
				<a href="audit/auditor/edit.do?auditId=${row.id}"> <spring:message code="audit.edit" /></a>
			</jstl:when>
			<jstl:otherwise>
			<spring:message code= "audit.notEditable"/>
			</jstl:otherwise>
		</jstl:choose>
			
	</display:column>
	</jstl:when>
	</jstl:choose>
	</security:authorize>
	
	<spring:message code="audit.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
		

	<spring:message code="audit.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="false" />
	
	<!-- {0,date,dd/MM/yyyy HH:mm} -->
	<spring:message code="audit.moment" var="momentHeader"/>
	<spring:message code="audit.moment.pattern" var="datePattern"/>
	<display:column title="${momentHeader}">
		<fmt:formatDate value="${row.moment}" pattern="${datePattern}"/>
	</display:column>
	
	<spring:message code="audit.attachmentUrl" var="attachmentUrlHeader" />
	<display:column property="attachmentUrl" title="${attachmentUrlHeader}" sortable="false" />
		
	<spring:message code="audit.trip" var="tripHeader" />
	<display:column property="trip.title" title="${tripHeader}" sortable="false" />
	
</display:table>


<security:authorize access="hasRole('AUDITOR')">
<div>
	<a href="audit/auditor/create.do"> 
		<spring:message code="audit.create"/>
	</a>
</div>
</security:authorize>
