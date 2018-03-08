<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('RANGER')">
	<form:form action="educationRecord/ranger/edit.do" modelAttribute="educationRecord">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:label path="diplomaTitle"><spring:message code="educationRecord.diplomaTitle"/>:</form:label>
		<form:input path="diplomaTitle"/>
		<form:errors cssClass="error" path="diplomaTitle"/>
		<br/>
		<form:label path="startDate"><spring:message code="educationRecord.startDate"/>:</form:label>
		<form:input path="startDate" placeholder="dd/MM/yyyy HH:mm"/>
		<form:errors cssClass="error" path="startDate"/>
		<br/>
		<form:label path="endDate"><spring:message code="educationRecord.endDate"/>:</form:label>
		<form:input path="endDate" placeholder="dd/MM/yyyy HH:mm"/>
		<form:errors cssClass="error" path="endDate"/>
		<br/>
		<form:label path="institution"><spring:message code="educationRecord.institution"/>:</form:label>
		<form:input path="institution"/>
		<form:errors cssClass="error" path="institution"/>
		<br/>
		<form:label path="attachmentLink"><spring:message code="educationRecord.attachmentLink"/>:</form:label>
		<form:input path="attachmentLink"/>
		<form:errors cssClass="error" path="attachmentLink"/>
		<br/>
		<form:label path="comments"><spring:message code="educationRecord.comments"/>:</form:label>
		<form:input path="comments"/>
		<form:errors cssClass="error" path="comments"/>
		<br/>
		<input type="submit" name="save" value="<spring:message code="educationRecord.save"/>" />
		<jstl:choose>
			<jstl:when test="${educationRecord.id!=0}">
				<input type="submit" name="delete" value="<spring:message code="educationRecord.delete"/>" />
			</jstl:when>
		</jstl:choose>	
		<input type="button" value="<spring:message code="educationRecord.cancel"/>" onClick="relativeRedir('/curriculum/display.do')"/>	
	</form:form>
</security:authorize>