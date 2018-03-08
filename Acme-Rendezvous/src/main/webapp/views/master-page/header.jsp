<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href=""><img src="images/logo.png"
		alt="Acme Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="permitAll">
			<li><a class="fNiv" href="rendezvous/list.do"><spring:message code="master.page.rendezvous.list" /></a></li>
			<li><a class="fNiv" href="user/list.do"><spring:message code="user.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="administrator/administrator/list.do"><spring:message code="master.page.administrator" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="rendezvous/administrator/list.do"><spring:message code="master.page.administrator.rendezvous.list" /></a></li>
		</security:authorize>
		
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="administrator/dashboard.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="rendezvous/user/create.do"><spring:message code="master.page.user.rendezvous.create" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="rendezvous/user/list.do"><spring:message code="master.page.user.rendezvous.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="RSVP/user/list.do"><spring:message code="master.page.user.RSVP.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="RSVP/user/listRSVPs.do"><spring:message code="master.page.RSVP" /></a></li>
		</security:authorize>
		<security:authorize access="hasAnyRole('ADMIN', 'USER')">
			<li>
				<a class="fNiv">
					<spring:message code="master.page.announcement" />
				</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="announcement/user/list.do">   <spring:message code="master.page.announcement.createdByUser" /> </a></li>
						<li><a href="announcement/user/stream.do"> <spring:message code="master.page.announcement.stream" /> </a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="announcement/administrator/list.do">   <spring:message code="master.page.announcement.findAllByAdmin" /> </a></li>
					</security:authorize>				
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="user/create.do"><spring:message code="user.create" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv">
					<spring:message code="master.page.profile" />
					(<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasAnyRole('ADMIN', 'USER')">
						<li><a href="actor/actor/profile.do"><spring:message code="user.profile" /> </a></li>
					</security:authorize>
					<li><a href="j_spring_security_logout"> <spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

