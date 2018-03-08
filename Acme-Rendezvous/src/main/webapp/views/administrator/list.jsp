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

<display:table name="administrators" id="row" requestURI="${requestURI}" pagesize="5">

	<spring:message code="administrator.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" />
	
	<spring:message code="administrator.surname" var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}" />
	
	<spring:message code="administrator.email" var="emailHeader" />
	<display:column property="email" title="${emailHeader}" />
	
	<spring:message code="administrator.address" var="addressHeader" />
	<display:column property="address" title="${addressHeader}" />
	
	<spring:message code="administrator.phone" var="phoneHeader" />
	<display:column property="phone" title="${phoneHeader}" />
	
	<spring:message code="administrator.birthDate" var="birthDateHeader" />
	<spring:message code="administrator.birthDate.pattern" var="datePattern"/>
	<display:column title="${birthDateHeader}" >
		<fmt:formatDate value="${row.birthDate}" pattern="${datePattern}"/>
	</display:column>

</display:table>

<security:authorize access="hasRole('ADMIN')">
		<a href="administrator/administrator/create.do"><spring:message code="administrator.create"/></a>	
</security:authorize>

