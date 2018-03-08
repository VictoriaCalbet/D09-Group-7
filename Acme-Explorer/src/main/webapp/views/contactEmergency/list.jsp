<%--
 * list.jsp
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

<display:table name="contactEmergencies" id="row" requestURI="contactEmergency/explorer/list.do" 
	pagesize="5" class="displaytag">
	
	<display:column>
		<a href="contactEmergency/explorer/edit.do?contactEmergencyId=${row.id}">
			<spring:message	code="contactEmergency.edit" />
		</a>
	</display:column>
		
	<spring:message code="contactEmergency.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />
	
	<spring:message code="contactEmergency.email" var="emailHeader" />
	<display:column property="email" title="${emailHeader}" sortable="false" />
	
	<spring:message code="contactEmergency.phone" var="phoneHeader" />
	<display:column property="phone" title="${phoneHeader}" sortable="false" />
			
</display:table>

<div>
	<b><a href="contactEmergency/explorer/create.do"> 
		<spring:message code="contactEmergency.create"/>
	</a></b>
</div>