<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<P><b><spring:message code="systemConfiguration.bannerUrl"/>:</b> ${systemConfiguration.bannerUrl}</P>

<P><b><spring:message code="systemConfiguration.welcomeMessageEnglish"/>:</b> ${systemConfiguration.welcomeMessageEnglish}</P>

<P><b><spring:message code="systemConfiguration.welcomeMessageSpanish"/>:</b> ${systemConfiguration.welcomeMessageSpanish}</P>

<P><b><spring:message code="systemConfiguration.vatNumber"/>:</b> ${systemConfiguration.vatNumber}</P>

<P><b><spring:message code="systemConfiguration.defaultCC"/>:</b> ${systemConfiguration.defaultCC}</P>

<P><b><spring:message code="systemConfiguration.defaultFinderNumber"/>:</b> ${systemConfiguration.defaultFinderNumber}</P>

<P><b><spring:message code="systemConfiguration.defaultCacheTime"/>:</b> ${systemConfiguration.defaultCacheTime}</P>

<b><spring:message code="systemConfiguration.spamWords"/>:</b>
	<jstl:forEach var="word" items = "${systemConfiguration.spamWords}">
	 	<P><jstl:out value="${word}"></jstl:out></P>
	</jstl:forEach>


<a href="systemConfiguration/administrator/edit.do">
	<spring:message code="systemConfiguration.edit" />
</a>
<br/>
<a href="welcome/index.do">
	<spring:message code="systemConfiguration.index" />
</a>