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

<form:form action="socialIdentity/actor/edit.do" modelAttribute="socialIdentity">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:label path="nick">
		<spring:message code="socialIdentity.nick"/>
	</form:label>
	<form:input path="nick"/>
	<form:errors cssClass="error" path="nick"/>
	<br/><br/>
	
	<form:label path="name">
		<spring:message code="socialIdentity.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/><br/>
	
	<form:label path="link">
		<spring:message code="socialIdentity.link"/>
	</form:label>
	<form:input path="link"/>
	<form:errors cssClass="error" path="link"/>
	<br/><br/>
	
	<form:label path="photo">
		<spring:message code="socialIdentity.photo"/>
	</form:label>
	<form:input path="photo"/>
	<form:errors cssClass="error" path="photo"/>
	<br/><br/>
	
	<input type="submit" name="save" value="<spring:message code="socialIdentity.save"/>"/>
	
	<jstl:if test="${socialIdentity.id > 0}">
		<input type="submit" name="delete" value="<spring:message code="socialIdentity.delete"/>"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="socialIdentity.cancel"/>" onclick="javascript: relativeRedir('socialIdentity/actor/list.do');"/>
	
	
</form:form>
