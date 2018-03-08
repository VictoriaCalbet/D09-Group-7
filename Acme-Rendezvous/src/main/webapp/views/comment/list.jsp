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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="now" class="java.util.Date"/>

<security:authentication property="principal" var="loggedactor"/>

<display:table id="row" name="comments" requestURI="${requestURI}" pagesize="10">

<!-- Link to delete a comment, visible only by admins -->
<security:authorize access="hasRole('ADMIN')">

<display:column><a href="comment/administrator/delete.do?commentId=${row.id}"><spring:message code="comment.delete">
</spring:message></a></display:column>

</security:authorize>

<!-- Link to reply to a comment, visible only to users -->

<security:authorize access="hasRole('USER')">

<display:column>
<jstl:if test="${principalRendezvouses.contains(rendezvous)}">
<a href="comment/user/reply.do?commentId=${row.id}"><spring:message code="comment.reply">
</spring:message></a>
</jstl:if>
</display:column>

</security:authorize>

<spring:message code="comment.text" var="commentText" />
<display:column property="text" title="${commentText}"><jstl:out value="${row.text}"></jstl:out></display:column>

<spring:message code="comment.picture" var="commentPic" />
<display:column property="picture" title="${commentPic}"><jstl:out value="${row.picture}"></jstl:out></display:column>

<spring:message code="comment.momentWritten" var="commentMomentWritten" />
<spring:message code="comment.date" var="commentDate"/>
<display:column property="momentWritten" title="${commentMomentWritten}"></display:column>
<fmt:formatDate value="${row.momentWritten}" pattern="${commentDate}"/>

<spring:message code="comment.user" var="commentUser" />
<display:column property="user.name" title="${commentUser}"><jstl:out value="${row.user.name}"></jstl:out></display:column>


<spring:message code="comment.replies" var="listReplies"/>
<display:column title="${listReplies}">
			<a href="comment/listReplies.do?commentId=${row.id}">
		<spring:message code="comment.viewReply"/></a>
</display:column>

</display:table>
<br/>
<security:authorize access="hasRole('USER')">

<!-- Link to create a new comment in the rendezvous, which has no replies -->

<jstl:if test="${principalRendezvouses.contains(rendezvous)}">
		<a href="comment/user/create.do?rendezvousId=${rendezvous.id}"><spring:message code="comment.create"></spring:message></a>
</jstl:if>	
	
</security:authorize>