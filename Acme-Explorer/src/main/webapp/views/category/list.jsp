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

<display:table name="categories" id="row" requestURI="${requestURI}" pagesize="10" >

<security:authorize access="hasRole('ADMIN')">

<display:column title="${categoryEdit}">
<a href="category/administrator/edit.do?categoryId=${row.id}"><spring:message code="category.edit"/></a>
</display:column>

</security:authorize>
<!-- En este punto se puede navegar desde una categoria a todas sus hijas -->
<spring:message code="category.name" var="categoryName" />
<display:column title="${categoryName}">
<a href="category/list.do?categoryId=${row.id}"><jstl:out value="${row.name}"></jstl:out></a>
</display:column>

<spring:message code="category.browseTrips" var="categoryTrips" />
<display:column title="${categoryTrips}">
<a href="trip/listByCategory.do?categoryTripId=${row.id}"><spring:message code="category.browseTrips"></spring:message></a>
</display:column>

</display:table>

<security:authorize access="hasRole('ADMIN')">

<a href="category/administrator/create.do"><spring:message code="category.create"/></a>

</security:authorize>