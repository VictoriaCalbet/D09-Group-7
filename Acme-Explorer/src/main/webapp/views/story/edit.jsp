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

<form:form action="story/explorer/edit.do" modelAttribute="story">

<form:hidden path="id"/>
<form:hidden path="version"/>

<form:label path="title">
<spring:message code="story.title"/>
</form:label>
<form:input path="title"/>
<form:errors cssClass="error" path="title"/>
<br><br>	
<form:label path="pieceOfText">
<spring:message code="story.pieceOfText"/>
</form:label>
<form:input path="pieceOfText"/>
<form:errors cssClass="error" path="pieceOfText"/>
<br><br>	
<form:label path="attachmentUrls">
<spring:message code="story.attachments"/>
</form:label>
<form:input path="attachmentUrls"/>
<form:errors cssClass="error" path="attachmentUrls"/>
<br><br>	
<spring:message code="story.trip" var="storyTrip"/>
<form:select path="trip">
<form:options
items="${trips}"
itemLabel="title"
itemValue="id"
/>
</form:select>
<br><br>		
<input type="submit" name="save" value="<spring:message code="story.save"/>"/>

<input type="button" name="cancel" value="<spring:message code="story.cancel"/>" onclick="javascript: relativeRedir('story/explorer/list.do')" />		
</form:form>

