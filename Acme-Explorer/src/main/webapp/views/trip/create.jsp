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

<form:form action="${requestURI}" modelAttribute="trip">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="ticker"/>
	<form:hidden path="cancelled"/>
	<form:hidden path="price"/>
	<form:hidden path="reason"/>
	<form:hidden path="stages"/>
	<form:hidden path="sponsorships"/>
	<form:hidden path="notes"/>
	<form:hidden path="audits"/>
	<form:hidden path="stories"/>
	<form:hidden path="applications"/>
	<form:hidden path="survivalClasses"/>
	<form:hidden path="tagValues"/>
	<form:hidden path="manager"/>
	
	
	<form:label path="title">
		<spring:message code="trip.title"/>
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="trip.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	<form:label path="requirements">
		<spring:message code="trip.requirements"/>
	</form:label>
	<form:textarea path="requirements"/><spring:message code="trip.requirements.explain"/> 
	<form:errors cssClass="error" path="requirements"/>
	<br/>
	
	<form:label path="publicationDate">	
		<spring:message code="trip.publicationDate"/>	
	</form:label>
	<form:input path="publicationDate"/> (dd/MM/yyyy HH:mm)	
	<form:errors cssClass="error" path="publicationDate"/>	
	<br/>
	
	<form:label path="startMoment">	
		<spring:message code="trip.startMoment"/>	
	</form:label>
	<form:input path="startMoment"/> (dd/MM/yyyy HH:mm)		
	<form:errors cssClass="error" path="startMoment"/>	
	<br/>
	
	<form:label path="endMoment">	
		<spring:message code="trip.endMoment"/>	
	</form:label>
	<form:input path="endMoment"/> (dd/MM/yyyy HH:mm)		
	<form:errors cssClass="error" path="endMoment"/>	
	<br/>
	<form:label path="ranger">
  		<spring:message code="trip.ranger"/>
 	</form:label>
 	<form:select path="ranger"> 
 		<form:option label="----" value="0"/>
  		<form:options items="${rangers}" itemValue="id" itemLabel="name"/>
 	</form:select>
 	<form:errors cssClass="error" path="ranger"/>
  	<br/>
 	<form:label path="legalText">
  		<spring:message code="trip.legalText"/>
 	</form:label>
 	<form:select path="legalText"> 
 		<form:option label="----" value="0"/>
  		<form:options items="${legalTexts}" itemValue="id" itemLabel="title"/>
 	</form:select>
 	 <form:errors cssClass="error" path="legalText"/>
 	<br/>
	<form:label path="category">
  		<spring:message code="trip.category"/>
 	</form:label>
 	<form:select path="category"> 
 		<jstl:forEach var="category" items="${categories}">
     	
    		<form:option value="${category.id}" label="${map.get(category.id)}"/>
    	
     	</jstl:forEach>
 	</form:select>
 	<form:errors cssClass="error" path="category"/>
	<br/>
	<br/>
	
	<input type="submit" name="save" value="<spring:message code="trip.save"/>"/>
	<input type="button" name="cancel" value="<spring:message code="trip.cancel"/>" onclick="javascript: relativeRedir('trip/manager/list.do');"/>
	
</form:form>
