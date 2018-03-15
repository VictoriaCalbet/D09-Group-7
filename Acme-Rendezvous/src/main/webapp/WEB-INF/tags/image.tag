<%--
 * images.tag
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 

<%@ attribute name="imageURL"              required="true" %>
<%@ attribute name="imageNotFoundLocation" required="false" %>
<%@ attribute name="codeError"             required="true" %>
<%@ attribute name="height"                required="true" %>
<%@ attribute name="width"                 required="true" %>

<%-- Definition --%>

<jstl:choose>
	<jstl:when test="${not empty imageURL and not empty imageNotFoundLocation}">
		<img src="${imageURL}" onerror="this.src='${imageNotFoundLocation}';"  height="${height}" width="${width}">
	</jstl:when>
	<jstl:when test="${not empty imageURL and empty imageNotFoundLocation}">
		<img src="${imageURL}" height="${height}" width="${width}">
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="${codeError}" var="unspecifiedURLLink"/>
		<jstl:out value="${unspecifiedURLLink}"/>
	</jstl:otherwise>
</jstl:choose>