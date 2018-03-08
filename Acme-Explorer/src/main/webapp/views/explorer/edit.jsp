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

<form:form action="${requestURI}" modelAttribute="explorer">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="isSuspicious"/>
	<form:hidden path="isBanned"/>
	
	<form:hidden path="userAccount.id"/>
	<form:hidden path="userAccount.version"/>
	<form:hidden path="userAccount.isEnabled"/>
	<form:hidden path="userAccount.authorities"/>
	<form:hidden path="sent"/>
	<form:hidden path="received"/>
	<form:hidden path="folders"/>
	<form:hidden path="socialIdentities"/>
	
	<form:hidden path="stories"/>
	<form:hidden path="applications"/>
	<form:hidden path="survivalClasses"/>
	<form:hidden path="contactEmergencies"/>
	
	<form:label path="name">
		<spring:message code="explorer.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/><br/>
	
	<form:label path="surname">
		<spring:message code="explorer.surname"/>
	</form:label>
	<form:input path="surname"/>
	<form:errors cssClass="error" path="surname"/>
	<br/><br/>
	
	<form:label path="email">
		<spring:message code="explorer.email"/>
	</form:label>
	<form:input path="email"/>
	<form:errors cssClass="error" path="email"/>
	<br/><br/>
	
	<form:label path="phone">
		<spring:message code="explorer.phone"/>
	</form:label>
	<form:input path="phone"/>
	<form:errors cssClass="error" path="phone"/>
	<br/><br/>
	
	<form:label path="address">
		<spring:message code="explorer.address"/>
	</form:label>
	<form:input path="address"/>
	<form:errors cssClass="error" path="address"/>
	<br/><br/>
	
	<spring:message code="explorer.creditCard"/>
	<br/><br/>
	
	<form:label path="creditCard.holderName">
		<spring:message code = "explorer.creditCard.holderName"/>
	</form:label>
	<form:input path="creditCard.holderName"/>
	<form:errors cssClass="error" path="creditCard.holderName"/>
	<br/><br/>
	
	<form:label path="creditCard.brandName">
		<spring:message code = "explorer.creditCard.brandName"/>
	</form:label>
	<form:input path="creditCard.brandName"/>
	<form:errors cssClass="error" path="creditCard.brandName"/>
	<br/><br/>
	
	<form:label path="creditCard.number">
		<spring:message code = "explorer.creditCard.number"/>
	</form:label>
	<form:input path="creditCard.number"/>
	<form:errors cssClass="error" path="creditCard.number"/>
	<br/><br/>
	
	<form:label path="creditCard.expirationMonth">
		<spring:message code = "explorer.creditCard.expirationMonth"/>
	</form:label>
	<form:input path="creditCard.expirationMonth"/>
	<form:errors cssClass="error" path="creditCard.expirationMonth"/>
	<br/><br/>
	
	<form:label path="creditCard.expirationYear">
		<spring:message code = "explorer.creditCard.expirationYear"/>
	</form:label>
	<form:input path="creditCard.expirationYear"/>
	<form:errors cssClass="error" path="creditCard.expirationYear"/>
	<br/><br/>
	
	<form:label path="creditCard.cvv">
		<spring:message code = "explorer.creditCard.cvv"/>
	</form:label>
	<form:input path="creditCard.cvv"/>
	<form:errors cssClass="error" path="creditCard.cvv"/>
	<br/><br/>
	
	<jstl:if test="${explorer.id == 0}">
		<form:label path="userAccount.username">
			<spring:message code="explorer.username" />
	</form:label>
	<form:input path="userAccount.username"/>	
	<form:errors class="error" path="userAccount.username" />
	<br/><br/>
	</jstl:if>
	
	<jstl:if test="${explorer.id > 0}">
		<form:hidden path="userAccount.username"/>
	</jstl:if>

	<form:label path="userAccount.password">
		<spring:message code="explorer.password" />
	</form:label>
	<form:password path="userAccount.password" />
	<form:errors class="error" path="userAccount.password" />
	<br/><br/>
	
	<spring:message var="patternMessage" code="explorer.phone.pattern"/>
	
	<input type="submit" name="save" value="<spring:message code="explorer.save"/>" onclick="return checkPhone('${patternMessage}')"/>
	<input type="button" name="cancel" value="<spring:message code="explorer.cancel"/>" onclick="javascript: relativeRedir('welcome/index.do');"/>

</form:form>
