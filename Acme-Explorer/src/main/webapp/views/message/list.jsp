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

<display:table name="messages" id="row" requestURI="message/actor/list.do" 
	pagesize="5" class="displaytag">
	
	<display:column>
		<jstl:if test="${principal.id == row.sender.id || principal.id == row.recipient.id}">
			<a href="message/actor/edit.do?messageId=${row.id}">
				<spring:message	code="message.edit" />
			</a>
		</jstl:if>
	</display:column>
	
	<spring:message code="message.sendMoment" var="momentHeader"/>
	<spring:message code="message.sendMoment.pattern" var="datePattern"/>
	<display:column title="${momentHeader}" sortable="true">
		<fmt:formatDate value="${row.sendMoment}" pattern="${datePattern}"/>
	</display:column>
	
	<spring:message code="message.subject" var="subjectHeader" />
	<display:column property="subject" title="${subjectHeader}" sortable="false" />
	
	<spring:message code="message.body" var="bodyHeader" />
	<display:column property="body" title="${bodyHeader}" sortable="false" />
	
	<spring:message code="message.priority" var="priorityHeader" />
	<display:column property="priority" title="${priorityHeader}" sortable="true" />
	
	<spring:message code="message.sender" var="senderHeader" />
	<display:column property="sender.name" title="${senderHeader}" sortable="true" />
	
	<spring:message code="message.recipient" var="recipientHeader" />
	<display:column property="recipient.name" title="${recipientHeader}" sortable="true" />
			
</display:table>