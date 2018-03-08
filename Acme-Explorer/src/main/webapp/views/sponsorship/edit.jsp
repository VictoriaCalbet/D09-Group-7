<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('SPONSOR')">
	<form:form action="sponsorship/sponsor/edit.do" modelAttribute="sponsorship">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:label path="infoPage"><spring:message code="sponsorship.information"/>:</form:label>
		<form:input path="infoPage"/>
		<form:errors cssClass="error" path="infoPage"/>
		<br/>
		<form:label path="bannerUrl"><spring:message code="sponsorship.banner"/>:</form:label>
		<form:input path="bannerUrl"/>
		<form:errors cssClass="error" path="bannerUrl"/>
		<br/>
		<h3><strong><spring:message code="sponsorship.creditcard"/></strong></h3>
		<br/>
		<form:label path="creditCard.holderName"><spring:message code="sponsorship.holder"/>:</form:label>
		<form:input path="creditCard.holderName"/>
		<form:errors cssClass="error" path="creditCard.holderName"/>
		<br/>
		<form:label path="creditCard.brandName"><spring:message code="sponsorship.brand"/>:</form:label>
		<form:input path="creditCard.brandName"/>
		<form:errors cssClass="error" path="creditCard.brandName"/>
		<br/>
		<form:label path="creditCard.number"><spring:message code="sponsorship.number"/>:</form:label>
		<form:input path="creditCard.number"/>
		<form:errors cssClass="error" path="creditCard.number"/>
		<br/>
		<form:label path="creditCard.expirationMonth"><spring:message code="sponsorship.month"/>:</form:label>
		<form:input path="creditCard.expirationMonth"/>
		<form:errors cssClass="error" path="creditCard.expirationMonth"/>
		<br/>
		<form:label path="creditCard.expirationYear"><spring:message code="sponsorship.year"/>:</form:label>
		<form:input path="creditCard.expirationYear"/>
		<form:errors cssClass="error" path="creditCard.expirationYear"/>
		<br/>
		<form:label path="creditCard.cvv">cvv:</form:label>
		<form:input path="creditCard.cvv"/>
		<form:errors cssClass="error" path="creditCard.cvv"/>
		<br/>
		<jstl:choose>
			<jstl:when test="${sponsorship.id==0}">		
				<form:label path="trip"><spring:message code="sponsorship.trip"/>:</form:label>
				<form:select path="trip">	
					<form:option label="----" value="0"/>				
					<form:options items="${trips}" itemLabel="title" itemValue="id"/>					
				</form:select>
				<form:errors cssClass="error" path="trip"/>
			</jstl:when>
			<jstl:when test="${sponsorship.id!=0}">
				<form:hidden path="trip"/>
			</jstl:when>
		</jstl:choose>			
		
		<br/>
		<input type="submit" name="save" value="<spring:message code="sponsorship.save"/>" />
		<jstl:choose>
			<jstl:when test="${sponsorship.id!=0}">
				<input type="submit" name="delete" value="<spring:message code="sponsorship.delete"/>" />
			</jstl:when>
		</jstl:choose>		
		<input type="button" value="<spring:message code="sponsorship.cancel"/>" onClick="relativeRedir('/sponsorship/sponsor/list.do')"/>	
	
	</form:form>
</security:authorize>