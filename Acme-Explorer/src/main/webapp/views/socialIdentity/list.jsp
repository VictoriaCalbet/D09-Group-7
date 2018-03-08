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

<display:table name="socialIdentities" id="row" requestURI="socialIdentity/actor/list.do" 
	pagesize="5" class="displaytag">
	
	<display:column>
		<a href="socialIdentity/actor/edit.do?socialIdentityId=${row.id}">
			<spring:message	code="socialIdentity.edit" />
		</a>
	</display:column>
	
	<spring:message code="socialIdentity.nick" var="nickHeader" />
	<display:column property="nick" title="${nickHeader}" sortable="true" />
		
	<spring:message code="socialIdentity.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />
	
	<spring:message code="socialIdentity.link" var="linkHeader" />
	<display:column property="link" title="${linkHeader}" sortable="false" />
		
	<spring:message code="socialIdentity.photo" var="photoHeader" />
	<display:column property="photo" title="${photoHeader}" sortable="false" />
			
</display:table>

<div>
	<b><a href="socialIdentity/actor/create.do"> 
		<spring:message code="socialIdentity.create"/>
	</a></b>
</div>