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

<display:table name="users" id="row" requestURI="${requestURI}" pagesize="5">

	<spring:message code="user.profile" var="profileHeader" />	
	<display:column title="${profileHeader}">	
		<a href="user/profile.do?userId=${row.id}">
		 	<spring:message code="user.profile" />
		</a>
	</display:column>

	<spring:message code="user.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />
	
	<spring:message code="user.surname" var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}" sortable="true" />
	
	<spring:message code="user.email" var="emailHeader" />
	<display:column property="email" title="${emailHeader}"/>
	
	<spring:message code="user.phone" var="phoneHeader" />
	<display:column property="phone" title="${phoneHeader}"/>
	
	<spring:message code="user.address" var="addressHeader" />
	<display:column property="address" title="${addressHeader}"/>
	
	<spring:message code="user.birthDate" var="birthDateHeader" />
	<spring:message code="user.birthDate.pattern" var="datePattern"/>
	<display:column title="${birthDateHeader}" >
		<fmt:formatDate value="${row.birthDate}" pattern="${datePattern}"/>
	</display:column>	
	<jstl:choose>
		<jstl:when test="${ showAnswers == 1}">
			<jstl:choose>
				<jstl:when test="${ row.id == creatorUserId}">
					<spring:message code="user.answers" var="answers"/>
					<display:column sortable="false" title="${answers}">
						<spring:message code="user.noanswers" />
					</display:column>
					
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="user.answers" var="answers"/>
					<display:column sortable="false" title="${answers}">
					<a href="answer/list.do?rendezvousId=${rendezvousId}&userId=${row.id}">
					<spring:message code="user.show" />
					</a>
					</display:column>
				</jstl:otherwise>
			
			</jstl:choose>	
		</jstl:when>
	</jstl:choose>
</display:table>
