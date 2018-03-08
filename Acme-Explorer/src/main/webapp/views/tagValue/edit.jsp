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

<form:form action="tagValue/manager/edit.do" modelAttribute="tagValue">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="name"/>
	<form:hidden path="tag"/>
	
	<jstl:if test="${tagValue.tag != null}">
	<form:label path="value">
		<spring:message code="tagValue.value"/>
	</form:label>
	<form:select path="value">
		<jstl:forEach var="singleValue" items="${groupOfValues}">
			<form:option value="${singleValue}"/>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="value"/>
	<br/><br/>
	</jstl:if>
	
	<jstl:if test="${tagValue.tag == null}">
		<form:hidden path="value"/>
	</jstl:if>
	
	<jstl:if test="${tagValue.id == 0}">
		<form:label path="trip">
			<spring:message code="tagValue.trip"/>
		</form:label>
		<form:select path="trip">
			<form:option label="----" value="0"/>
			<form:options items="${trips}" itemValue="id" itemLabel="ticker"/>
		</form:select>
		<form:errors cssClass="error" path="trip"/>
		<br/><br/>
	</jstl:if>
	
	<jstl:if test="${tagValue.id > 0}">
		<form:hidden path="trip"/>
	</jstl:if>
	
	<jstl:if test="${tagValue.tag != null}">
		<input type="submit" name="save" value="<spring:message code="tagValue.save"/>"/>
	</jstl:if>
	
	<jstl:if test="${tagValue.id > 0}">
		<input type="submit" name="delete" value="<spring:message code="tagValue.delete"/>"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="tagValue.cancel"/>" onclick="javascript: relativeRedir('trip/manager/list.do')" />		

</form:form>
