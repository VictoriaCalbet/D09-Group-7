<%--
 * edit.jsp
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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authentication property="principal" var="loggedactor"/>

<fieldset>
	<legend>
		<spring:message code="service.name" var="serviceNameLabel"/>
		<h2><b><jstl:out value="${serviceNameLabel}"/>:&nbsp;</b> <jstl:out value="${service.name}"/></h2>
	</legend>
	<table>
		<tr>
			<td>
				<spring:message code="service.description" var="serviceDescriptionLabel"/>
				<b><jstl:out value="${serviceDescriptionLabel}"/>:&nbsp;</b> <jstl:out value="${service.description}"/>
			</td>
			<td>
				<spring:message code="service.creator" var="serviceCreatorLabel"/>
				<b><jstl:out value="${serviceCreatorLabel}"/>:&nbsp;</b><jstl:out value="${service.manager.name}"/>
			</td>
		</tr>
		<tr>
			<td>
				<spring:message code="service.noRequests" var="serviceNoRequestsLabel" />
				<b><jstl:out value="${serviceNoRequestsLabel}"/>:&nbsp;</b> <jstl:out value="${fn:length(service.requests)}"/>
			</td>
			<td>
				<spring:message code="service.categories" var="serviceCategoriesLabel"/>
				<b><jstl:out value="${serviceCategoriesLabel}"/>:&nbsp;</b> 
				<jstl:choose>
					<jstl:when test="${not empty service.categories}">
						<jstl:forEach items="${service.categories}" var="srv">
							<jstl:out value="${srv.name}"/>
						</jstl:forEach>
					</jstl:when>
					<jstl:otherwise>
						<spring:message code="service.display.itHasNoCategory" var="serviceItHasNoCategoryMessage"/>
						<jstl:out value="${serviceItHasNoCategoryMessage}"/>
					</jstl:otherwise>
				</jstl:choose>
				
			</td>
		</tr>
		<tr>
			<td>
				<spring:message code="service.isInappropriate" var="serviceIsInappropriateLabel" />
				<b><jstl:out value="${serviceIsInappropriateLabel}"/>:&nbsp;</b> 
				<jstl:choose>
					<jstl:when test="${service.isInappropriate eq true}">
						<spring:message code="service.isInappropriate.yes" var="serviceIsInappropriateYes" />
						<jstl:out value="${serviceIsInappropriateYes}"/>
					</jstl:when>
					<jstl:otherwise>
						<spring:message code="service.isInappropriate.no" var="serviceIsInappropriateNo"/>
						<jstl:out value="${serviceIsInappropriateNo}"/>
					</jstl:otherwise>
				</jstl:choose>
			</td>
		</tr>
		<tr>
			<td>
				<spring:message code="service.pictureURL" var="servicepictureURLLabel" />
				<b><jstl:out value="${servicepictureURLLabel}"/>:&nbsp;</b> 
				<acme:image imageURL="${service.pictureURL}" imageNotFoundLocation="images/fotoNotFound.png" codeError="service.unspecifiedURL" height="100" width="100"/>
			</td>
		</tr>
	</table>
	<security:authorize access="hasRole('MANAGER')">
		<jstl:if test="${service.manager.userAccount.id eq loggedactor.id and empty service.requests and service.isInappropriate eq false}">
			<spring:message var="serviceEditLink" code="service.edit"/>
			<a href="service/manager/edit.do?serviceId=${service.id}"><jstl:out value="${serviceEditLink}"/></a>
		</jstl:if>
	</security:authorize>
</fieldset> 

<br/>
<acme:cancel url="${cancelURI}" code="service.cancel"/>