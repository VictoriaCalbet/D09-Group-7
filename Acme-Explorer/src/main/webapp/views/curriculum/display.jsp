<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<spring:message code="curriculum.create" var="create"/>
<spring:message code="curriculum.edit" var="edit"/>
<spring:message code="curriculum.datePattern" var="datePattern"/>
<h3><strong><spring:message code="curriculum.personalRecord"/></strong></h3>
<jstl:choose>
	<jstl:when test="${curriculum.personalRecord!=null}">
<strong><spring:message code="curriculum.name"/>: </strong><jstl:out value="${curriculum.personalRecord.name}"/>
<br/>
<spring:url var="url1" value="${curriculum.personalRecord.photo}"/>
<strong><spring:message code="curriculum.photo"/>: </strong><img src="${url1}" alt="<spring:message code="curriculum.photo"/>" />
<br/>
<strong><spring:message code="curriculum.email"/>: </strong><jstl:out value="${curriculum.personalRecord.email}"/>
<br/>
<strong><spring:message code="curriculum.phone"/>: </strong><jstl:out value="${curriculum.personalRecord.phone}"/>
<br/>
<spring:url var="url2" value="${curriculum.personalRecord.linkedInLink}"/>
<strong><spring:message code="curriculum.linkedin"/>: </strong><a href="${url2}" >${url2}</a>
<br/>
<strong><spring:message code="curriculum.ticker"/>: </strong><jstl:out value="${curriculum.ticker}"/>
<br/>
	</jstl:when>
	<jstl:when test="${curriculum.personalRecord==null}">
		<spring:message code="curriculum.nothing"/>
	</jstl:when>
</jstl:choose>	
<jstl:choose>
	<jstl:when test="${canEdit==1}">
<a href="personalRecord/ranger/edit.do?personalRecordId=${curriculum.personalRecord.id}">
	${edit}
</a>
	</jstl:when>
</jstl:choose>	

<h3><strong><spring:message code="curriculum.endorserRecords"/></strong></h3>
<display:table name="endorserRecords" id="row1" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	<jstl:choose>
	<jstl:when test="${canEdit==1}">
	<display:column sortable="false" title="${edit}">
		<a href="endorserRecord/ranger/edit.do?endorserRecordId=${row1.id}">
			${edit}
		</a>
	</display:column>
		</jstl:when>
</jstl:choose>	
	<spring:message code="curriculum.name" var="name1"/>
	<display:column property="name" title="${name1}" sortable="false"/>
	
	<spring:message code="curriculum.email" var="email1"/>
	<display:column property="email" title="${email1}" sortable="false"/>
	
	<spring:message code="curriculum.phone" var="phone1"/>
	<display:column property="phone" title="${phone1}" sortable="false"/>
	
	<spring:message code="curriculum.linkedin" var="linkedin1"/>
	<display:column title="${linkedin1}" sortable="false">
		<a href="${row1.linkedInLink}">
			${row1.linkedInLink}
		</a>
	</display:column>
	
	<spring:message code="curriculum.comments" var="comments1"/>
	<display:column property="comments" title="${comments1}" sortable="false"/>
	
	
</display:table>
<jstl:choose>
	<jstl:when test="${canEdit==1}">
<a href="endorserRecord/ranger/create.do">${create}</a>
	</jstl:when>
</jstl:choose>	
<h3><strong><spring:message code="curriculum.professionalRecords"/></strong></h3>
<display:table name="professionalRecords" id="row2" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	<jstl:choose>
	<jstl:when test="${canEdit==1}">
	<display:column sortable="false" title="${edit}">
		<a href="professionalRecord/ranger/edit.do?professionalRecordId=${row2.id}">
			${edit}
		</a>
	</display:column>
		</jstl:when>
</jstl:choose>	
	<spring:message code="curriculum.companyName" var="companyName2"/>
	<display:column property="companyName" title="${companyName2}" sortable="false"/>
	
	<spring:message code="curriculum.startDate" var="startDate2"/>
	<display:column property="startDate" title="${startDate2}" format="${datePattern}" sortable="false"/>
	
	<spring:message code="curriculum.endDate" var="endDate2"/>
	<display:column property="endDate" title="${endDate2}" format="${datePattern}" sortable="false"/>
	
	<spring:message code="curriculum.role" var="role2"/>
	<display:column property="role" title="${role2}" sortable="false"/>
	
	<spring:message code="curriculum.attachmentLink" var="attachmentLink2"/>
	<display:column sortable="false" title="${attachmentLink2}">
		<a href="${row2.attachmentLink}">
			${row2.attachmentLink}
		</a>
	</display:column>
	
	<spring:message code="curriculum.comments" var="comments2"/>
	<display:column property="comments" title="${comments2}" sortable="false"/>
	
</display:table>
<jstl:choose>
	<jstl:when test="${canEdit==1}">
<a href="professionalRecord/ranger/create.do">${create}</a>
	</jstl:when>
</jstl:choose>	
<h3><strong><spring:message code="curriculum.educationRecords"/></strong></h3>
<display:table name="educationRecords" id="row3" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	<jstl:choose>
	<jstl:when test="${canEdit==1}">
	<display:column sortable="false" title="${edit}">
		<a href="educationRecord/ranger/edit.do?educationRecordId=${row3.id}">
			${edit}
		</a>
	</display:column>
		</jstl:when>
</jstl:choose>	
	<spring:message code="curriculum.diplomaTitle" var="diplomaTitle3"/>
	<display:column property="diplomaTitle" title="${diplomaTitle3}" sortable="false"/>
	
	<spring:message code="curriculum.startDate" var="startDate3"/>
	<display:column property="startDate" title="${startDate3}" format="${datePattern}" sortable="false"/>
	
	<spring:message code="curriculum.endDate" var="endDate3"/>
	<display:column property="endDate" title="${endDate3}" format="${datePattern}" sortable="false"/>
	
	<spring:message code="curriculum.institution" var="role3"/>
	<display:column property="institution" title="${role3}" sortable="false"/>
	
	<spring:message code="curriculum.attachmentLink" var="attachmentLink3"/>
	<display:column sortable="false" title="${attachmentLink3}">
		<a href="${row3.attachmentLink}">
			${row3.attachmentLink}
		</a>
	</display:column>
	
	<spring:message code="curriculum.comments" var="comments3"/>
	<display:column property="comments" title="${comments3}" sortable="false"/>
	
</display:table>
<jstl:choose>
	<jstl:when test="${canEdit==1}">
<a href="educationRecord/ranger/create.do">${create}</a>
	</jstl:when>
</jstl:choose>	
<h3><strong><spring:message code="curriculum.miscellaneousRecords"/></strong></h3>
<display:table name="miscellaneousRecords" id="row4" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	<jstl:choose>
	<jstl:when test="${canEdit==1}">
	<display:column sortable="false" title="${edit}">
		<a href="miscellaneousRecord/ranger/edit.do?miscellaneousRecordId=${row4.id}">
			${edit}
		</a>
	</display:column>
		</jstl:when>
</jstl:choose>	
	<spring:message code="curriculum.title" var="title4"/>
	<display:column property="title" title="${title4}" sortable="false"/>
	
	<spring:message code="curriculum.attachmentLink" var="attachmentLink4"/>
	<display:column sortable="false" title="${attachmentLink4}">
		<a href="${row4.attachmentLink}">
			${row4.attachmentLink}
		</a>
	</display:column>
	
	<spring:message code="curriculum.comments" var="comments4"/>
	<display:column property="comments" title="${comments4}" sortable="false"/>
	
</display:table>
<jstl:choose>
	<jstl:when test="${canEdit==1}">
<a href="miscellaneousRecord/ranger/create.do">${create}</a>
	</jstl:when>
</jstl:choose>	
<br/>
<br/>
<jstl:choose>
	<jstl:when test="${canEdit==1}">
<form:form action="curriculum/ranger/delete.do" modelAttribute="curriculum">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="submit" name="delete" value="<spring:message code="curriculum.delete"/> curriculum" />
</form:form>
	</jstl:when>
</jstl:choose>	