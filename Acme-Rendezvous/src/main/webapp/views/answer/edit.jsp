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
	<form:form action="${requestURI}" modelAttribute="questionAndAnswerForm">
		<form:hidden path="questionId"/>
		<form:hidden path="questionText"/>
		<form:hidden path="answerId"/>
		<form:label path="answerText">${questionAndAnswerForm.questionText}:</form:label>
		<form:input path="answerText"/>
		<form:errors cssClass="error" path="answerText"/>
		<br/>
		<input type="submit" name="save" value="<spring:message code="answer.save"/>" />
		<input type="button" value="<spring:message code="answer.cancel"/>" onClick="relativeRedir('answer/user/list.do?rendezvousId=${rendezvousId}')"/>	
	</form:form>
</security:authorize>	