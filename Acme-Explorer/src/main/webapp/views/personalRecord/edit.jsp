<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('RANGER')">
	<form:form action="personalRecord/ranger/edit.do" modelAttribute="personalRecord">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:label path="name"><spring:message code="personalRecord.name"/>:</form:label>
		<form:input path="name"/>
		<form:errors cssClass="error" path="name"/>
		<br/>
		<form:label path="photo"><spring:message code="personalRecord.photo"/>:</form:label>
		<form:input path="photo"/>
		<form:errors cssClass="error" path="photo"/>
		<br/>
		<form:label path="email"><spring:message code="personalRecord.email"/>:</form:label>
		<form:input path="email"/>
		<form:errors cssClass="error" path="email"/>
		<br/>
		<form:label path="phone"><spring:message code="personalRecord.phone"/>:</form:label>
		<form:input path="phone"/>
		<form:errors cssClass="error" path="phone"/>
		<br/>
		<form:label path="linkedInLink"><spring:message code="personalRecord.linkedin"/>:</form:label>
		<form:input path="linkedInLink"/>
		<form:errors cssClass="error" path="linkedInLink"/>
		<br/>
		
		<input type="submit" name="save" value="<spring:message code="personalRecord.save"/>" />
		<jstl:choose>
			<jstl:when test="${fromWhere}">
				<input type="button" value="<spring:message code="personalRecord.cancel"/>" onClick="relativeRedir('curriculum/display.do')"/>
			</jstl:when>
			<jstl:when test="${!fromWhere}">
				<input type="button" value="<spring:message code="personalRecord.cancel"/>" onClick="relativeRedir('welcome/index.do')"/>
			</jstl:when>
		</jstl:choose>		
	</form:form>
</security:authorize>