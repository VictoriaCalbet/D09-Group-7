<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('RANGER')">
	<form:form action="endorserRecord/ranger/edit.do" modelAttribute="endorserRecord">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:label path="name"><spring:message code="endorserRecord.name"/>:</form:label>
		<form:input path="name"/>
		<form:errors cssClass="error" path="name"/>
		<br/>
		<form:label path="email"><spring:message code="endorserRecord.email"/>:</form:label>
		<form:input path="email"/>
		<form:errors cssClass="error" path="email"/>
		<br/>
		<form:label path="phone"><spring:message code="endorserRecord.phone"/>:</form:label>
		<form:input path="phone"/>
		<form:errors cssClass="error" path="phone"/>
		<br/>
		<form:label path="linkedInLink"><spring:message code="endorserRecord.linkedin"/>:</form:label>
		<form:input path="linkedInLink"/>
		<form:errors cssClass="error" path="linkedInLink"/>
		<br/>
		<form:label path="comments"><spring:message code="endorserRecord.comments"/>:</form:label>
		<form:input path="comments"/>
		<form:errors cssClass="error" path="comments"/>
		<br/>
		
		<input type="submit" name="save" value="<spring:message code="endorserRecord.save"/>" />
		<jstl:choose>
			<jstl:when test="${endorserRecord.id!=0}">
				<input type="submit" name="delete" value="<spring:message code="educationRecord.delete"/>" />
			</jstl:when>
		</jstl:choose>	
		<input type="button" value="<spring:message code="endorserRecord.cancel"/>" onClick="relativeRedir('curriculum/display.do')"/>	
	</form:form>
</security:authorize>