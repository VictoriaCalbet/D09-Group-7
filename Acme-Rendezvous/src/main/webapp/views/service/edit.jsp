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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authentication property="principal" var="loggedactor"/>

<form:form action="${actionURI}" modelAttribute="service">
	<jstl:choose>
		<jstl:when test="${service.manager.userAccount.id eq loggedactor.id and empty service.requests and service.isInappropriate eq false}">
			<form:hidden path="id"/>
			<form:hidden path="version"/>
			<form:hidden path="isInappropriate"/>
			<form:hidden path="manager"/>
			<form:hidden path="requests"/>
		
			<acme:selectMultiple items="${categories}" itemLabel="name" code="service.categories" path="categories"/>
			<acme:textbox code="service.name" path="name"/>
			<acme:textbox code="service.description" path="description"/>
			<acme:textbox code="service.pictureURL" path="pictureURL"/>
			<br/>
			
			<acme:submit name="save" code="service.save"/> &nbsp;
			
			<jstl:if test="${service.id ne 0 and empty service.requests}">
				<spring:message code="service.delete" var="serviceDeleteButton"/>
				<input type="submit" name="delete" value="${serviceDeleteButton}" />
			</jstl:if>
			
			
			<acme:cancel url="/service/manager/list.do" code="service.cancel"/>
		</jstl:when>
		<jstl:otherwise>
			<jstl:if test="${service.manager.userAccount.id ne loggedactor.id}">
				<spring:message var="serviceEditManagerWrongOwner" code="service.edit.manager.wrongOwner.information"/>
				<b><jstl:out value="${serviceEditManagerWrongOwner}"/></b>
				<br/>
			</jstl:if>
			<jstl:if test="${not empty service.requests}">
				<spring:message var="serviceEditRequestsNotEmpty" code="service.edit.requests.notEmpty.information"/>
				<b><jstl:out value="${serviceEditRequestsNotEmpty}"/></b>
				<br/>
			</jstl:if>
			<jstl:if test="${service.isInappropriate eq true}">
				<spring:message var="serviceEditInapproppriate" code="service.edit.inapproppriate.information"/>
				<b><jstl:out value="${serviceEditInapproppriate}"/></b>
				<br/>
			</jstl:if>
		</jstl:otherwise>
	</jstl:choose>
</form:form>