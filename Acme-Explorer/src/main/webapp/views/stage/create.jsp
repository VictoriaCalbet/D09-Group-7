<%--
 * action-2.jsp
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

<form:form action="${requestURI}" modelAttribute="stage">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="trip"/>
	
	
	<form:label path="title">
		<spring:message code="stage.title"/>
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="stage.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	<form:label path="price">
		<spring:message code="stage.price"/>
	</form:label>
	<form:input path="price"/> <spring:message code="stage.price.explain"/>
	<form:errors cssClass="error" path="price"/>
	<br/>
	
	<input type="submit" name="save" value="<spring:message code="stage.save"/>"/>
	<input type="button" name="cancel" value="<spring:message code="stage.cancel"/>" onclick="javascript: relativeRedir('trip/manager/list.do');"/>
</form:form>
