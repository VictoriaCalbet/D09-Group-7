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

<form:form action="tag/administrator/edit.do" modelAttribute="tag">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="tagValues"/>
		
	<form:label path="name">
		<spring:message code="tag.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/><br/>
	
	<form:label path="groupOfValues">
		<spring:message code="tag.values"/>
	</form:label>
	<form:textarea path="groupOfValues"/>
	<form:errors cssClass="error" path="groupOfValues"/>
	<br/><br/>
		
	<input type="submit" name="save" value="<spring:message code="tag.save"/>"/>
	
	<jstl:if test="${tag.id > 0}">
		<input type="submit" name="delete" value="<spring:message code="tag.delete"/>"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="tag.cancel"/>" onclick="javascript: relativeRedir('tag/administrator/list.do')" />		

</form:form>

