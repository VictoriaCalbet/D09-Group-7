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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="categoryForm">

	<!-- Hidden attributes -->
	<form:hidden path="categoryId"/>
	
	<!-- Editable attributes -->
	
	<acme:textbox code="category.name" path="name"/>	
	<acme:textbox code="category.description" path="description"/>
	
	<form:label path="parent">
	<spring:message code="category.parent"/>
	</form:label>
    	
	<form:select name="parent" path="parent">
			
		 <form:option value="${null}" label="None"></form:option>	

    	 <jstl:forEach var="category" items="${categories}">
    	 	
    	 	<jstl:if test="${category.id != categoryForm.categoryId}">
     	
    			<form:option value="${category.id}" label="${map.get(category.id)}"/>
    		
    		</jstl:if>
     	</jstl:forEach>
	</form:select>

	
	<form:errors cssClass="error" path="parent"/>
	
		<!-- Action buttons -->
	<br/>
	<acme:submit name="save" code="category.save" />
	
	
	<jstl:if test="${categoryForm.categoryId>0}">
	
		<acme:submit name="delete" code="category.delete" />
	
	</jstl:if>
	
	<acme:cancel url="/" code="category.cancel" /> <br/>

</form:form>

