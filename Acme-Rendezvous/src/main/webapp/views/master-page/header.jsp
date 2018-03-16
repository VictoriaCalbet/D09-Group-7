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
	<a href=""><img src="${bannerURL}" alt="${businessName}" height="150"/></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<!-- #Rendezvous and #RSVP -->
		<security:authorize access="permitAll">
			<li>
				<a class="fNiv" href="rendezvous/list.do"><spring:message code="master.page.rendezvous"/></a>
				<ul>
					<security:authorize access="hasRole('ADMIN')">
						<li class="arrow"></li>
						<li><a href="rendezvous/administrator/list.do"><spring:message code="master.page.administrator.rendezvous.list" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('USER')">
						<li class="arrow"></li>
						<li><a href="rendezvous/user/list.do"><spring:message code="master.page.user.rendezvous.list" /></a></li>
						<li><a href="RSVP/user/list.do"><spring:message code="master.page.user.RSVP.list" /></a></li>
						<li><a href="RSVP/user/listRSVPs.do"><spring:message code="master.page.RSVP" /></a></li>	
						<li><a href="rendezvous/user/create.do"><spring:message code="master.page.user.rendezvous.create" /></a></li>
					</security:authorize>
				</ul>
			</li>
		</security:authorize>
		
		<!-- #Service -->
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"><spring:message code="master.page.service"/></a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="service/administrator/list.do"><spring:message code="master.page.administrator.service.list" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('USER')">
						<li><a href="service/user/list.do"><spring:message code="master.page.user.service.list" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MANAGER')">
						<li><a href="service/manager/list.do"><spring:message code="master.page.manager.service.list"/></a></li>
						<li><a href="service/manager/list-created.do"><spring:message code="master.page.manager.service.listCreated"/></a></li>
					</security:authorize>
				</ul>
			</li>
		</security:authorize>
		
		<!-- #Announcement -->
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
		
		<!-- #User and #Manager -->
		<security:authorize access="permitAll">
			<li>
				<a class="fNiv" href="user/list.do"><spring:message code="user.list"/></a>
				<ul>
					<security:authorize access="isAnonymous()">
						<li class="arrow"></li>
						<li><a href="user/create.do"><spring:message code="user.create" /></a></li>
						<li><a href="manager/create.do"><spring:message code="manager.create" /></a></li>
					</security:authorize>
				</ul>
			</li>
		</security:authorize>
		
		<!-- #Administrator -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="administrator/administrator/list.do"><spring:message code="master.page.administrator" /></a></li>
		</security:authorize>
		
		<!-- #Category -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="category/administrator/list.do"><spring:message code="master.page.administrator.category.list" /></a></li>
		</security:authorize>
		
		<!-- #SystemConfiguration -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="systemConfiguration/administrator/info.do"><spring:message code="systemConfiguration.header" /></a></li>
		</security:authorize>
		
		<!-- #DashBoard -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="administrator/dashboard.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
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
					<security:authorize access="hasRole('MANAGER')">
						<li><a href="manager/manager/profile.do"><spring:message code="manager.profile" /> </a></li>
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

