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

	<display:table name="questions" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag" >
		<jstl:choose>
			<jstl:when test="${canEdit==1 && isRSVP==0}">
				<spring:message code="question.edit" var="edit"/>
				<display:column sortable="false" title="${edit}">
					<a href="question/user/edit.do?questionId=${row.id}">
						<spring:message code="question.edit" />
					</a>
				</display:column>
			</jstl:when>
		</jstl:choose>	
		<spring:message code="question.question" var="question"/>
		<display:column title="${question}" sortable="false">
			<jstl:out value="${row.text}"/>
		</display:column>
	</display:table>
	
	<jstl:choose>
		<jstl:when test="${canEdit==1}">
				<jstl:choose>
					<jstl:when test="${isRSVP==0}">
						<spring:message code="question.create" var="create"/>
						<a href="question/user/create.do?rendezvousId=${rendezvousId}">${create}</a>
					</jstl:when>
					<jstl:otherwise>
						<spring:message code="question.isRSVP"/>
					</jstl:otherwise>
				</jstl:choose>
		</jstl:when>
	</jstl:choose>	
