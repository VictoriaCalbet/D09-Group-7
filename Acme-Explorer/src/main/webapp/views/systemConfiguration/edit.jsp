<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="systemConfiguration/administrator/edit.do" modelAttribute="systemConfiguration">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	
	<form:label path="vatNumber">
		<spring:message code="systemConfiguration.vatNumber"/>
	</form:label>
	<form:input path="vatNumber"/>
	<form:errors cssClass="error" path="vatNumber"/>
	<br/>
	
	<form:label path="bannerUrl">
		<spring:message code="systemConfiguration.bannerUrl"/>
	</form:label>
	<form:input path="bannerUrl"/>
	<form:errors cssClass="error" path="bannerUrl"/>
	<br/>
	
	<form:label path="welcomeMessageEnglish">
		<spring:message code="systemConfiguration.welcomeMessageEnglish"/>
	</form:label>
	<form:input path="welcomeMessageEnglish"/>
	<form:errors cssClass="error" path="welcomeMessageEnglish"/>
	<br/>
	
	<form:label path="welcomeMessageSpanish">
		<spring:message code="systemConfiguration.welcomeMessageSpanish"/>
	</form:label>
	<form:input path="welcomeMessageSpanish"/>
	<form:errors cssClass="error" path="welcomeMessageSpanish"/>
	<br/>
	
	<form:label path="defaultCC">
		<spring:message code="systemConfiguration.defaultCC"/>
	</form:label>
	<form:input path="defaultCC"/>
	<form:errors cssClass="error" path="defaultCC"/>
	<br/>
	
	<form:label path="defaultFinderNumber">
		<spring:message code="systemConfiguration.defaultFinderNumber"/>
	</form:label>
	<form:input path="defaultFinderNumber"/>
	<form:errors cssClass="error" path="defaultFinderNumber"/>
	<br/>
	
	<form:label path="defaultCacheTime">
		<spring:message code="systemConfiguration.defaultCacheTime"/>
	</form:label>
	<form:input path="defaultCacheTime"/>
	<form:errors cssClass="error" path="defaultCacheTime"/>
	<br/>
	
	<form:label path="spamWords">
		<spring:message code="systemConfiguration.spamWords"/>
	</form:label>
	<form:input path="spamWords"/>
	<form:errors cssClass="error" path="spamWords"/>
	<br/>
	
	
	<input type="submit" name="save" value="<spring:message code="systemConfiguration.save"/>"/>
				
	<a href="welcome/index.do">
			 		<spring:message code="systemConfiguration.cancel" />
				</a>
</form:form>