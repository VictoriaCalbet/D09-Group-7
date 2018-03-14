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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<display:table id="row" name="categories" requestURI="${requestURI}" pagesize="10">


<display:column><a href="category/administrator/edit.do?categoryId=${row.id}"><spring:message code="category.edit"/></a></display:column>


<spring:message code="category.name" var="categoryName" />
<display:column property="name" title="${categoryName}"><jstl:out value="${row.name}"></jstl:out></display:column>

<spring:message code="category.description" var="categoryDescription" />
<display:column property="description" title="${categoryDescription}"><jstl:out value="${row.description}"></jstl:out></display:column>

<spring:message code="category.services" var="categoryServices" />
<display:column title="${categoryServices}">

<jstl:forEach var="service" items="${row.services}">
		<jstl:out value="${service.name}"></jstl:out>
</jstl:forEach>

</display:column>

<spring:message code="category.children" var="listCategories"/>
<display:column title="${listCategories}">
			<a href="category/administrator/list.do?categoryId=${row.id}">
		<spring:message code="category.viewChildren"/></a>
</display:column>

</display:table>
<br/>

<a href="category/administrator/create.do"><spring:message code="category.create"/></a>