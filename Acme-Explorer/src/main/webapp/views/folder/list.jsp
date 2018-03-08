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

<div>
	<b><a href="message/actor/create.do"> 
		<spring:message code="message.create"/>
	</a></b>
</div><br/>

<security:authorize access="hasRole('ADMIN')">
	<div>
	<b><a href="message/administrator/create.do"><spring:message code="message.broadcast"/></a></b>
	</div><br/>
</security:authorize>

<display:table name="folders" id="row" requestURI="folder/actor/list.do" 
	pagesize="5" class="displaytag">
	
	<spring:message	code="folder.edit" var="editHeader"/>
	<display:column title="${editHeader}">
		<jstl:if test="${!row.systemFolder}">
			<a href="folder/actor/edit.do?folderId=${row.id}">
				<spring:message	code="folder.edit" />
			</a>
		</jstl:if>
	</display:column>
	
	<spring:message code="folder.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />
		
	<spring:message code="folder.systemFolder" var="systemFolderHeader" />
	<display:column property="systemFolder" title="${systemFolderHeader}" sortable="true" />
	
	<display:column>
		<a href="message/actor/list.do?folderId=${row.id}">
			<spring:message	code="folder.messages" />
		</a>
	</display:column>
	
	<spring:message code="folder.parent" var="parentHeader" />
	<display:column title="${parentHeader}">
		<jstl:if test="${row.parent != null}">
			<spring:message	text="${row.parent.name}" />
		</jstl:if>
	</display:column>
	
	<spring:message code="folder.children" var="childrenHeader" />
	<display:column title="${childrenHeader}">
		<a href="folder/actor/list.do?folderId=${row.id}">
			<spring:message	code="folder.children" />
		</a>
	</display:column>
			
</display:table>
	
<div>
	<b><a href="folder/actor/create.do"> 
		<spring:message code="folder.create"/>
	</a></b>
</div>