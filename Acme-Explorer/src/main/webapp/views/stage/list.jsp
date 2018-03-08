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

<jsp:useBean id="now" class="java.util.Date"/>

<jstl:choose>
 <jstl:when test="${pageContext.response.locale.language=='en'}">
      <fmt:setLocale value = "en_US"/>
      <jstl:set var="patternCurrency" value="¤ #,##0.00"></jstl:set>
 </jstl:when>
 <jstl:otherwise>
      <fmt:setLocale value = "es_ES"/>
      <jstl:set var="patternCurrency" value="#,##0.00 ¤"></jstl:set>
 </jstl:otherwise> 
</jstl:choose>


<display:table name="stages" id="row" requestURI="${requestURI}"
	pagesize="5">
	
	<jstl:set var="trip" value="${row.trip}"></jstl:set>
	
	<spring:message code="stage.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />
	
	<spring:message code="stage.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />
	
	<spring:message code="stage.price" var="priceHeader" />
	<display:column title="${priceHeader}">
        <fmt:formatNumber value = "${row.price}" currencySymbol="&euro;" pattern="${patternCurrency}" type = "currency"  minFractionDigits="2"/>
	</display:column>
	
	<security:authorize access="hasRole('MANAGER')">
	
		<jstl:if test="${now.time < row.trip.publicationDate.time}">
			<spring:message code="stage.editStage" var="editStageHeader" />	
			<display:column title="${editStageHeader}">	
					<a href="stage/manager/edit.do?stageId=${row.id}">
				 		<spring:message code="stage.editStageButton" />
				 	</a>
			</display:column>
		
			<spring:message code="stage.deleteStage" var="deleteStageHeader" />	
			<display:column title="${deleteStageHeader}">
					<a href="stage/manager/delete.do?stageId=${row.id}">
				 		<spring:message code="stage.deleteStageButton" />
				 	</a>
			</display:column>
		</jstl:if>
		
		</security:authorize>

</display:table>
<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${now.time < t.publicationDate.time}">
		<div>
			<b><a href="stage/manager/create.do?tripId=${tripId}"> 
				<spring:message code="stage.create"/>
			</a></b>
		</div>
	</jstl:if>
</security:authorize>
