<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form:form modelAttribute="application" action="${requestURI}">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" />
	<form:hidden path="momentMade" />
	<form:hidden path="comment" />
	<form:hidden path="creditCards" />
	<form:hidden path="trip" />
	
	
	<form:label path="reasonDenied">
		<spring:message code="application.reasonDenied"/>
	</form:label>
	<form:textarea path="reasonDenied"/>
	<form:errors cssClass="error" path="reasonDenied"/>
	<br/>
	
  <input type="submit" name="save" value="<spring:message code="application.save"/>"/>
  <input type="button" name="cancel" value="<spring:message code="application.cancel"/>" onclick="javascript: window.location.replace('')"/>

</form:form>