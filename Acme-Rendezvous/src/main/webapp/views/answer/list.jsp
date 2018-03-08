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

<display:table name="questionsAndAnswers" id="row" requestURI="${requestURI}" pagesize="5">

	<spring:message code="answer.questions" var="questions" />
	<display:column title="${questions}" sortable="false">
		<jstl:out value="${row.questionText}"/>
	</display:column>
		<spring:message code="answer.answers" var="answers" />
	<display:column title="${answers}" sortable="false">
		<jstl:out value="${row.answerText}"/>
	</display:column>
	<jstl:choose>
		<jstl:when test="${respondible==1}">
	<spring:message code="answer.respond" var="respond"/>
	<display:column sortable="false" title="${respond}">
		<a href="answer/user/respond.do?questionId=${row.questionId}">
			<spring:message code="answer.respond" />
		</a>
	</display:column>
		</jstl:when>
	</jstl:choose>	

</display:table>
<jstl:choose>
	<jstl:when test="${todoRespondido==1}">
		<input type="button" value="<spring:message code="answer.continue"/>" onClick="relativeRedir('RSVP/user/RSVPAssure.do?rendezvousId=${rendezvousId}')"/>	
	</jstl:when>
</jstl:choose>	
