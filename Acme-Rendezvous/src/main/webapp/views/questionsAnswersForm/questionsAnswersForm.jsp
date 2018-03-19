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

	
	
	
	
<form id="answeredQuestions" action="${resquestURI}" method="post">
	<!-- Hidden attributes -->
	<input id="rendezvousId" name="rendezvousId" type="hidden" value="${answeredQuestions.rendezvousId}"/>
	<!-- Editable attributes -->
	<jstl:forEach items="${questionsAndAnswers}" var="questionAndAnswerForm" varStatus="i">
		<label for="${questionAndAnswerForm.questionId}">${questionAndAnswerForm.questionText}</label>
		<input id="answerId${questionAndAnswerForm.questionId}" name="answerId${questionAndAnswerForm.questionId}" type="hidden" value="${questionAndAnswerForm.answerId}"/>
		<input type="text" id="${questionAndAnswerForm.questionId}" name="${questionAndAnswerForm.questionId}" value="${questionAndAnswerForm.answerText}"/>
		<jstl:choose>
			<jstl:when test="${questionAndAnswerForm.isBlank}">
				<span id="answerText.errors" class="error">Must not be blank</span>
			</jstl:when>
		</jstl:choose>
		
		<br/>
		
	</jstl:forEach>


	<!-- Action buttons -->
	<input type="submit" name="save" value="<spring:message code="questionsAnswersForm.RSVP"/>" />
	<input type="button" value="<spring:message code="questionsAnswersForm.cancel"/>" onClick="relativeRedir('/')"/>	

</form>