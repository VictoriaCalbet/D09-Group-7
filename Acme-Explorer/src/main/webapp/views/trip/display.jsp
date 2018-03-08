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


<div>
 <b><spring:message code="trip.ticker" />: </b><jstl:out value="${trip.ticker}" />
 <br/>
 <b><spring:message code="trip.title" />: </b><jstl:out value="${trip.title}" />
 <br/>
 <b><spring:message code="trip.description" />: </b><jstl:out value="${trip.description}" />
 <br/>
 <b><spring:message code="trip.price" />: </b><fmt:formatNumber value = "${trip.price}" currencySymbol="&euro;" pattern="${patternCurrency}" type = "currency" minFractionDigits="2"/> (<spring:message code="trip.price.iva"/>)

 <br/>
 <b><spring:message code="trip.requirements" />:</b> <jstl:out value="${trip.requirements}" />
 <br/>
 <b><spring:message code="trip.publicationDate" /><spring:message code="trip.pattern" var="datePattern"/>: </b><fmt:formatDate value="${trip.publicationDate}" pattern="${datePattern}"/>
 <br/>
 <b><spring:message code="trip.startMoment" /><spring:message code="trip.pattern" var="datePattern"/>: </b><fmt:formatDate value="${trip.startMoment}" pattern="${datePattern}"/>
 <br/>
 <b><spring:message code="trip.endMoment" /><spring:message code="trip.pattern" var="datePattern"/>: </b><fmt:formatDate value="${trip.endMoment}" pattern="${datePattern}"/>
 <br/>
 <b><spring:message code="trip.cancelled" />: </b>
	 <jstl:if test="${!trip.cancelled}">
	  <spring:message code="trip.cancelled.false" var="no" />
	  <jstl:out value="${no}"></jstl:out>
	  <br/>
	 </jstl:if>	
	 <jstl:if test="${trip.cancelled}">
	  <spring:message code="trip.cancelled.true" var="yes" />
	  <jstl:out value="${yes}"></jstl:out>
	  <br/>
	   <b><spring:message code="trip.reason" />: </b><jstl:out value="${trip.reason}" />
	  <br/>
	 </jstl:if>
	 
  <h3><spring:message code="trip.ranger"/></h3>
  <b><spring:message code="trip.ranger.name" />: </b><jstl:out value="${trip.ranger.name}" />
  <br/>  
  <b><spring:message code="trip.ranger.surname" />: </b><jstl:out value="${trip.ranger.surname}" />
  <br/> 
  <b><spring:message code="trip.ranger.email" />: </b><jstl:out value="${trip.ranger.email}" />
  <br/> 
  <b><spring:message code="trip.ranger.phone" />: </b><jstl:out value="${trip.ranger.phone}" />
  <br/> 
  <b><spring:message code="trip.ranger.address" />: </b><jstl:out value="${trip.ranger.address}" />
  <br/> 
  <b><spring:message code="trip.ranger.curriculum" />: <a href="curriculum/display.do?rangerId=${trip.ranger.id}"><spring:message code="trip.ranger.seeCurriculum" /></a></b>
  <h3><spring:message code="trip.legalText"/></h3>
  <b><spring:message code="trip.legalText.title" />: </b><jstl:out value="${trip.legalText.title}" />
  <br/>
  <b><spring:message code="trip.legalText.body" />: </b><jstl:out value="${trip.legalText.body}" />
  <br/>
  <b><spring:message code="trip.legalText.numberLaw" />: </b><jstl:out value="${trip.legalText.numberLaw}" />
  <br/>
  <b><spring:message code="trip.legalText.momentRegistered" />: <spring:message code="trip.pattern" var="datePattern"/>: </b><fmt:formatDate value="${trip.legalText.momentRegistered}" pattern="${datePattern}"/>
  <br/>
  <h3><spring:message code="trip.category"/></h3>
  <b><spring:message code="trip.category.name" />: </b><jstl:out value="${trip.category.name}" />
  <br/>		
    
  <h3><spring:message code="trip.tags"/></h3>
  <display:table name="${trip.tagValues}" id="row" requestURI="trip/info.do" pagesize="5">
	
	<jstl:if test="${now.time < trip.publicationDate.time}">
		<display:column>
			<a href="tagValue/manager/edit.do?tagValueId=${row.id}">
				<spring:message	code="trip.editTripButton" />
			</a>
		</display:column>
	</jstl:if>
		
	<spring:message code="trip.tags.name" var="tagsNameHeader" />
	<display:column property="name" title="${tagsNameHeader}" />
	
	<spring:message code="trip.tags.value" var="tagsValueHeader" />
	<display:column property="value" title="${tagsValueHeader}" />
	
  </display:table>
  
	<h3><spring:message code="trip.sponsorships"/></h3>
		<jstl:if test="${banner!=null}">	
			<spring:url var="bannerURL" value="${banner}"/>
			<spring:message code="trip.sponsorships.bannerUrl" var="bannerURL2" />
			<img src="${bannerURL}" alt="${bannerURL2}"/>
		</jstl:if>
		<jstl:if test="${banner==null}">
			<spring:message code="trip.sponsorships.noBannerUrl"/>
		</jstl:if>
</div>