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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form:form action="note/manager/edit.do" modelAttribute="note">

<form:hidden path="id"/>
<form:hidden path="version"/>
<form:hidden path="creationMoment"/>
<form:hidden path="remark"/>
<form:hidden path="trip"/>

	
<form:label path="response">
<spring:message code="note.response"/>
</form:label>
<form:textarea path="response"/>
<form:errors cssClass="error" path="response"/>
<br>
<br>

<input type="submit" name="save" value="<spring:message code="note.savereply"/>"/>


<input type="button" name="cancel" value="<spring:message code="note.cancel"/>" onclick="javascript: relativeRedir('trip/manager/list.do')" />

</form:form>

