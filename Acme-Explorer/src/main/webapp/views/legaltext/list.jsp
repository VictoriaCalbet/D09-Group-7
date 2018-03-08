<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<display:table name="legaltexts" id="row" requestURI="${requestURI}" pagesize="5">
	
	<spring:message	code="legaltext.edit" var="editHeader"/>
	<display:column title="${editHeader}">
		<jstl:if test="${row.isDraft==true}">
			<a href="legaltext/administrator/edit.do?legaltextId=${row.id}">
			<spring:message code="legaltext.edit"/></a>
		</jstl:if>
	</display:column>
	
	<spring:message code="legaltext.title" var="legaltextTitle"/>
	<display:column property="title" title="${legaltextTitle}"><jstl:out value="${row.title}"></jstl:out></display:column>
	
	<spring:message code="legaltext.body" var="legaltextBody"/>
	<display:column property="body" title="${legaltextBody}"><jstl:out value="${row.body}"></jstl:out></display:column>
	
	<spring:message code="legaltext.numberLaw" var="legaltextNumberLaw"/>
	<display:column property="numberLaw" title="${legaltextNumberLaw}"><jstl:out value="${row.numberLaw}"></jstl:out></display:column>
	
	<spring:message code="legaltext.momentRegistered" var="legaltextMomentRegistered"/>
	<display:column property="momentRegistered" title="${legaltextMomentRegistered}"/>
	
	<spring:message code="legaltext.isDraft" var="legaltextisDraft"/>
	<display:column property="isDraft" title="${legaltextisDraft}"/>
	
</display:table>


<a href="legaltext/administrator/create.do">
<spring:message code="legaltext.create"/></a>
