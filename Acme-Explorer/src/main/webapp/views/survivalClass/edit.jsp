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


<form:form action="${requestURI}" modelAttribute="survivalClass">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="momentOrganized"/>
	<form:hidden path="location.id"/>
	<form:hidden path="location.version"/>

	<form:label path="title">
		<spring:message code="survivalClass.title"></spring:message>
	</form:label>							
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/><br>
	
	<form:label path="description">
		<spring:message code="survivalClass.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br><br>
	<form:label path="trip">
		<spring:message code="survivalClass.trip"/>
	</form:label>
	<form:select path="trip">					
		<form:options items="${trips}" itemLabel="title" itemValue="id"/>					
	</form:select>
	<br><br>

 	<form:label path="location.name">					
	<spring:message code="survivalClass.location"></spring:message>
	</form:label>
 	<form:input path="location.name"/>
 	<form:errors cssClass="error" path="location.name"/>
	<br><br> 
	
	<form:label path="location.gpsPoint.latitude">				
	<spring:message code="survivalClass.location.gpsPoint.latitude"></spring:message>
	</form:label>
	<form:input path="location.gpsPoint.latitude"/>
	<form:errors cssClass="error" path="location.gpsPoint.latitude"/>
	<br><br>
	
	<form:label path="location.gpsPoint.longitude">					
		<spring:message code="survivalClass.location.gpsPoint.longitude"></spring:message>
	</form:label>
	<form:input path="location.gpsPoint.longitude"/>
	<form:errors cssClass="error" path="location.gpsPoint.longitude"/>
	<br><br>
	
	
	<input type="button" value="<spring:message code="survivalClass.cancel"/>" 
	onClick="relativeRedir('/survivalClass/manager/list.do')"/>
	
	
	
	
	<security:authorize access="hasRole('MANAGER')">
	
	<input type="submit" name="save" value="<spring:message code="survivalClass.save"/>"/>
	<jstl:choose>
		<jstl:when test="${survivalClass.id!=0}">
	
	<input type="submit" name="delete" value="<spring:message code="survivalClass.delete"/> " />
	</jstl:when>
	</jstl:choose>
	</security:authorize>
</form:form>
