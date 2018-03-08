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


<form:form action="audit/auditor/edit.do" modelAttribute= "audit" >
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="moment"/>

	<!-- If the audit is new, show the draft form, else, draft hidden -->
	<jstl:choose>
		<jstl:when test="${audit.id==0 or audit.isDraft==true}">
			<form:label path="isDraft">
				<spring:message code="audit.isDraft"/>
			</form:label>
	
			<form:select path="isDraft">					
				<form:option label="True" value="true"/>
				<form:option label="False" value="false"/>
				<form:errors cssClass="error" path="isDraft"/>
				<br>
			</form:select>
		</jstl:when>
		<jstl:otherwise> 
			<form:hidden path="isDraft"/>
		</jstl:otherwise>
	</jstl:choose>	
	<br><br>
	
	<form:label path="title">					
		<spring:message code="audit.title"></spring:message>
	</form:label>							
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br><br>
	
	<form:label path="description">
		<spring:message code="audit.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br><br>
	
	<form:label path="attachmentUrl">
		<spring:message code="audit.attachmentUrl"/>
	</form:label>
	<form:textarea path="attachmentUrl" placeholder="Ej:http://www.attachtmenturl.com"/>
	<form:errors cssClass="error" path="attachmentUrl"/>
	<br><br>

	<form:label path="trip">
		<spring:message code="audit.trip"/>
	</form:label>
	<form:select path="trip">					
		<form:options items="${trips}" itemLabel="title" itemValue="id"/>					
	</form:select>
	<form:errors cssClass="error" path="trip"/>
	<br><br>
	
	
	
	
	<security:authorize access="hasRole('AUDITOR')">
	<jstl:choose>
		<jstl:when test="${audit.id!=0}">
			<input type="submit" name="delete" value="<spring:message code="audit.delete"/> " />
		</jstl:when>
	</jstl:choose>
	
	</security:authorize>

	<security:authorize access="hasRole('AUDITOR')">
	
	<input type="button" value="<spring:message code="audit.cancel"/>" 
	onClick="relativeRedir('/audit/auditor/list.do')"/>
	
	<input type="submit" name="save" value="<spring:message code="audit.save"/>"/>
	
	</security:authorize>
</form:form>

	