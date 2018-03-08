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

<form:form action="${requestURI}" modelAttribute="messageEntity">

	<jstl:if test="${requestURI eq 'message/actor/edit.do'}">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:hidden path="sendMoment"/>
		<form:hidden path="sender"/>
			
		<jstl:if test="${messageEntity.id == 0}">
		
			<form:hidden path="senderFolder"/>
			<form:hidden path="recipientFolder"/>
				
			<form:label path="recipient">
				<spring:message code="message.recipient"/>
			</form:label>
			<form:select path="recipient">
				<form:option label="----" value="0"/>
				<form:options items="${recipients}" itemValue="id" itemLabel="name"/>
			</form:select>
			<form:errors cssClass="error" path="recipient"/>
			<br/><br/>
			
			<form:label path="subject">
				<spring:message code="message.subject"/>
			</form:label>
			<form:input path="subject"/>
			<form:errors cssClass="error" path="subject"/>
			<br/><br/>
		
			<form:label path="body">
				<spring:message code="message.body"/>
			</form:label>
			<form:input path="body"/>
			<form:errors cssClass="error" path="body"/>
			<br/><br/>
				
			<form:label path="priority">
				<spring:message code="message.priority"/>
			</form:label>
			<form:select path="priority">
				<form:option label="----" value="0"/>
				<form:options items="${priorities}"/>
			</form:select>
			<form:errors cssClass="error" path="priority"/>
			<br/><br/>
			
		</jstl:if>
		
		<jstl:if test="${messageEntity.id > 0}">
		
			<form:hidden path="subject"/>
			<form:hidden path="body"/>
			<form:hidden path="priority"/>
			<form:hidden path="recipient"/>
			
			<jstl:if test="${principal.id == messageEntity.sender.id}">
			
				<form:hidden path="recipientFolder"/>
			
				<form:label path="senderFolder">
					<spring:message code="message.senderFolder"/>
				</form:label>
				<form:select path="senderFolder">
					<form:options items="${foldersToMove}" itemLabel="name"/>
				</form:select>
				<form:errors cssClass="error" path="senderFolder"/>
				<br/><br/>
			
			</jstl:if>
			
			<jstl:if test="${principal.id == messageEntity.recipient.id}">
			
				<form:hidden path="senderFolder"/>
			
				<form:label path="recipientFolder">
					<spring:message code="message.recipientFolder"/>
				</form:label>
				<form:select path="recipientFolder">
					<form:options items="${foldersToMove}" itemLabel="name"/>
				</form:select>
				<form:errors cssClass="error" path="recipientFolder"/>
				<br/><br/>
			
			</jstl:if>
		
		</jstl:if>
	
	</jstl:if>
	
	<jstl:if test="${requestURI eq 'message/administrator/edit.do'}">
	
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:hidden path="sendMoment"/>
		
		<form:hidden path="sender"/>
		<form:hidden path="recipient"/>
		<form:hidden path="senderFolder"/>
		<form:hidden path="recipientFolder"/>
		
		<form:label path="subject">
			<spring:message code="message.subject"/>
		</form:label>
		<form:input path="subject"/>
		<form:errors cssClass="error" path="subject"/>
		<br/><br/>
	
		<form:label path="body">
			<spring:message code="message.body"/>
		</form:label>
		<form:input path="body"/>
		<form:errors cssClass="error" path="body"/>
		<br/><br/>
			
		<form:label path="priority">
			<spring:message code="message.priority"/>
		</form:label>
		<form:select path="priority">
			<form:option label="----" value="0"/>
			<form:options items="${priorities}"/>
		</form:select>
		<form:errors cssClass="error" path="priority"/>
		<br/><br/>
	
	</jstl:if>
	
	<input type="submit" name="save" value="<spring:message code="message.save"/>"/>
	
	<jstl:if test="${messageEntity.id > 0}">
		<input type="submit" name="delete" value="<spring:message code="message.delete"/>"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="message.cancel"/>" onclick="javascript: relativeRedir('folder/actor/list.do');"/>
	
	
</form:form>
