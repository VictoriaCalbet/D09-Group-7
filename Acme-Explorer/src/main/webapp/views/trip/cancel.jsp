<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form:form action="${requestURI}" modelAttribute="trip">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="ticker"/>
	<form:hidden path="title"/>
	<form:hidden path="description"/>
	<form:hidden path="requirements"/>
	<form:hidden path="publicationDate"/>
	<form:hidden path="startMoment"/>
	<form:hidden path="endMoment"/>
	<form:hidden path="ranger"/>
	<form:hidden path="legalText"/>
	<form:hidden path="category"/>
	<form:hidden path="cancelled"/>
	<form:hidden path="price"/>
	<form:hidden path="stages"/>
	<form:hidden path="sponsorships"/>
	<form:hidden path="notes"/>
	<form:hidden path="audits"/>
	<form:hidden path="stories"/>
	<form:hidden path="applications"/>
	<form:hidden path="survivalClasses"/>
	<form:hidden path="tagValues"/>
	<form:hidden path="manager"/>



	<form:label path="reason">
		<spring:message code="trip.reason"/>
	</form:label>
	<form:textarea path="reason"/>
	<form:errors cssClass="error" path="reason"/>
	<br/>
	
	<input type="submit" name="save" value="<spring:message code="trip.save"/>"/>
	<input type="button" name="cancel" value="<spring:message code="trip.cancel"/>" onclick="javascript: relativeRedir('trip/manager/list.do');"/>
	
	
</form:form>