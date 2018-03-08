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

<display:table name="tags" id="row" requestURI="${requestURI}" pagesize="5">

	<security:authorize access="hasRole('ADMIN')">
	
		<spring:message code="tag.edit" var="tagEdit"/>
		<display:column title="${tagEdit}">
			<a href="tag/administrator/edit.do?tagId=${row.id}"><spring:message code="tag.edit"/></a>
		</display:column>
	
	</security:authorize>
	
	<spring:message code="tag.name" var="tagName"/>
	<display:column property="name" title="${tagName}"/>
	
	<spring:message code="tag.values" var="tagValues"/>
	<display:column property="groupOfValues" title="${tagValues}"/>
	
	<security:authorize access="hasRole('MANAGER')">
	
		<spring:message code="tag.trip.assign" var="assignEdit"/>
		<display:column title="${assignEdit}">
			<a href="tagValue/manager/create.do?tagId=${row.id}"><spring:message code="tag.trip.assign"/></a>
		</display:column>
		
	</security:authorize>
	
</display:table>
	
	<security:authorize access="hasRole('ADMIN')">
	
		<a href="tag/administrator/create.do"><spring:message code="tag.create"/></a>
	
	</security:authorize>