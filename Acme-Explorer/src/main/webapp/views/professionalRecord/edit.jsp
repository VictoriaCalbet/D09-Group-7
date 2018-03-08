<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('RANGER')">
	<form:form action="professionalRecord/ranger/edit.do" modelAttribute="professionalRecord">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:label path="companyName"><spring:message code="professionalRecord.companyName"/>:</form:label>
		<form:input path="companyName"/>
		<form:errors cssClass="error" path="companyName"/>
		<br/>
		<form:label path="startDate"><spring:message code="professionalRecord.startDate"/>:</form:label>
		<form:input path="startDate" placeholder="dd/MM/yyyy HH:mm"/>
		<form:errors cssClass="error" path="startDate"/>
		<br/>
		<form:label path="endDate"><spring:message code="professionalRecord.endDate"/>:</form:label>
		<form:input path="endDate" placeholder="dd/MM/yyyy HH:mm"/>
		<form:errors cssClass="error" path="endDate"/>
		<br/>
		<form:label path="role"><spring:message code="professionalRecord.role"/>:</form:label>
		<form:input path="role"/>
		<form:errors cssClass="error" path="role"/>
		<br/>
		<form:label path="attachmentLink"><spring:message code="professionalRecord.attachmentLink"/>:</form:label>
		<form:input path="attachmentLink"/>
		<form:errors cssClass="error" path="attachmentLink"/>
		<br/>
		<form:label path="comments"><spring:message code="professionalRecord.comments"/>:</form:label>
		<form:input path="comments"/>
		<form:errors cssClass="error" path="comments"/>
		<br/>
		<input type="submit" name="save" value="<spring:message code="professionalRecord.save"/>" />
		<jstl:choose>
			<jstl:when test="${professionalRecord.id!=0}">
				<input type="submit" name="delete" value="<spring:message code="professionalRecord.delete"/>" />
			</jstl:when>
		</jstl:choose>	
		<input type="button" value="<spring:message code="professionalRecord.cancel"/>" onClick="relativeRedir('/curriculum/display.do')"/>	
	</form:form>
</security:authorize>