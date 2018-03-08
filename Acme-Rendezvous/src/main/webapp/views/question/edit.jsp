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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('USER')">
	<form:form action="${requestURI}" modelAttribute="questionForm">
		<form:hidden path="questionId"/>
		<form:hidden path="rendezvousId"/>
		<acme:textbox code="question.question" path="text"/>
		<input type="submit" name="save" value="<spring:message code="question.save"/>" />
		<jstl:choose>
			<jstl:when test="${questionForm.questionId!=0}">
				<input type="submit" name="delete" value="<spring:message code="question.delete"/>" />
			</jstl:when>
		</jstl:choose>	
		<input type="button" value="<spring:message code="question.cancel"/>" onClick="relativeRedir('question/user/list.do?rendezvousId=${questionForm.rendezvousId}')"/>	
	</form:form>
</security:authorize>	