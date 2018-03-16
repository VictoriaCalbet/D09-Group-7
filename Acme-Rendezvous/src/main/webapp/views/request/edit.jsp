<%--	
* Copyright (C) 2017 Universidad de Sevilla
	
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
	
<form:form action="${requestURI}" modelAttribute="requestForm">
	
	<form:hidden path="id"/>
	<form:hidden path="rendezvous"/>
<security:authorize access="hasRole('USER')">


<acme:textbox code="request.comment" path="comment"/>
<acme:textbox code="request.creditCard.brandName" path="creditCard.brandName"/>
<acme:textbox code="request.creditCard.holderName" path="creditCard.holderName"/>
<acme:textbox code="request.creditCard.number" path="creditCard.number"/>
<acme:textbox code="request.creditCard.expirationMonth" path="creditCard.expirationMonth"/>
<acme:textbox code="request.creditCard.expirationYear" path="creditCard.expirationYear"/>
<acme:textbox code="request.creditCard.cvv" path="creditCard.cvv" />
<acme:select code="request.service" path="service" items="${availableServices}" itemLabel="name"/>
	

<input type="submit" name="save" value="<spring:message code="request.request"/>"/>
<acme:cancel url="service/user/list.do" code="request.cancel" /> <br/>

</security:authorize>

</form:form>