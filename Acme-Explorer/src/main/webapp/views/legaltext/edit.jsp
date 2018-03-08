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

<form:form action="legaltext/administrator/edit.do" modelAttribute="legaltext">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="trips"/>
	<form:hidden path="momentRegistered"/>

	<form:label path="title">					
		<spring:message code="legaltext.title"></spring:message>
	</form:label>							
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/><br/>
	
	<form:label path="body">
	<spring:message code="legaltext.body"/>
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body"/>
	<br/><br/>
	
	<form:label path="numberLaw">
	<spring:message code="legaltext.numberLaw"/>
	</form:label>
	<form:textarea path="numberLaw"/>
	<form:errors cssClass="error" path="numberLaw"/>
	<br/><br/>
	
	<form:label path="isDraft">
	<spring:message code="legaltext.isDraft"/>
	</form:label>
	<form:select path="isDraft">
		<form:option label="True" value="true"/>
		<form:option label="False" value="false"/>
	</form:select>
	<form:errors cssClass="error" path="isDraft"/>
	<br/><br/>

	<input type="submit" name="save" value="<spring:message code="legaltext.save"/>"/>

	<jstl:if test="${legaltext.id > 0 and legaltext.isDraft == true}">
		<input type="submit" name="delete" value="<spring:message code="legaltext.delete"/>"/>
	</jstl:if>

	<input type="button" name="cancel" value="<spring:message code="legaltext.cancel"/>" onclick="javascript: relativeRedir('legaltext/administrator/list.do')" />		
</form:form>