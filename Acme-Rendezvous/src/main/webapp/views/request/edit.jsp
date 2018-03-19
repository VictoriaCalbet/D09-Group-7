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


<acme:textbox code="request.comments" path="comments" /><br/>
<acme:select code="request.service" path="service" items="${availableServices}" itemLabel="name"/><br/>
<fieldset> 
	<spring:message code="request.edit.creditCard.legend" var="requestCreditCardLegend"/>
	<legend><b><jstl:out value="${requestCreditCardLegend}"/>:&nbsp;</b></legend>
		<acme:textbox2 code="request.creditCard.brandName" path="creditCard.brandName" valueImput="${brandCookie}" />
		<acme:textbox2 code="request.creditCard.holderName" path="creditCard.holderName" valueImput="${holderCookie}"/>
		<acme:textbox2 code="request.creditCard.number" path="creditCard.number" valueImput="${numberCookie}"/>
		<acme:textbox2 code="request.creditCard.expirationMonth" path="creditCard.expirationMonth" valueImput="${monthCookie}"/>
		<acme:textbox2 code="request.creditCard.expirationYear" path="creditCard.expirationYear" valueImput="${yearCookie}"/>
		<acme:textbox2 code="request.creditCard.cvv" path="creditCard.cvv" valueImput="${cvvCookie}"/>
</fieldset>

<br/>
<input type="submit" name="save" value="<spring:message code="request.request"/>"/>
<acme:cancel url="rendezvous/list.do" code="request.cancel" /> <br/>

</security:authorize>

</form:form>