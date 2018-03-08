<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('RANGER')">
	<form:form action="miscellaneousRecord/ranger/edit.do" modelAttribute="miscellaneousRecord">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:label path="title"><spring:message code="miscellaneousRecord.title"/>:</form:label>
		<form:input path="title"/>
		<form:errors cssClass="error" path="title"/>
		<br/>
		<form:label path="attachmentLink"><spring:message code="miscellaneousRecord.attachmentLink"/>:</form:label>
		<form:input path="attachmentLink"/>
		<form:errors cssClass="error" path="attachmentLink"/>
		<br/>
		<form:label path="comments"><spring:message code="miscellaneousRecord.comments"/>:</form:label>
		<form:input path="comments"/>
		<form:errors cssClass="error" path="comments"/>
		<br/>
		<input type="submit" name="save" value="<spring:message code="miscellaneousRecord.save"/>" />
		<jstl:choose>
			<jstl:when test="${miscellaneousRecord.id!=0}">
				<input type="submit" name="delete" value="<spring:message code="miscellaneousRecord.delete"/>" />
			</jstl:when>
		</jstl:choose>	
		<input type="button" value="<spring:message code="miscellaneousRecord.cancel"/>" onClick="relativeRedir('/curriculum/display.do')"/>	
	</form:form>
</security:authorize>