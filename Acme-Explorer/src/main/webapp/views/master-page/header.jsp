<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<img src="${siteBanner}" alt="Tanzanika, Inc." height="150"/>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message	code="master.page.trip" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="trip/manager/list.do"><spring:message code="master.page.trip.manager.list" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
			
		
		<security:authorize access="permitAll">
			<li><a class="fNiv" href="trip/list.do"><spring:message code="master.page.trip.list" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv" href="survivalClass/manager/list.do"><spring:message code="master.page.survivalClass" /></a></li>
			<li><a class="fNiv" href="tag/manager/list.do"><spring:message code="master.page.tag.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('SPONSOR')">
			<li><a href="sponsorship/sponsor/list.do"><spring:message code="master.page.sponsor.sponsorship" /> </a></li>
		</security:authorize>
		<security:authorize access="hasRole('RANGER')">
			<li><a href="curriculum/display.do"><spring:message code="master.page.ranger.curriculum" /> </a></li>
		</security:authorize>
		<security:authorize access="hasRole('AUDITOR')">
			<li><a class="fNiv"><spring:message code="master.page.audit"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="audit/auditor/list.do"><spring:message code="master.page.audit.list"/></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.note"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="note/auditor/list.do"><spring:message code="master.page.note.list"/></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('EXPLORER')">
			<li><a class="fNiv"><spring:message code="master.page.contactEmergency"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="contactEmergency/explorer/list.do"><spring:message code="master.page.contactEmergency.list"/></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message code="master.page.finder"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="finder/explorer/display.do"><spring:message code="master.page.finder.show"/></a></li>
				</ul>
			</li>
			
			
			<li><a class="fNiv"><spring:message code="master.page.applications"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="application/explorer/list.do"><spring:message code="master.page.application.list"/></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message code="master.page.stories"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="story/explorer/list.do"><spring:message code="master.page.story.list"/></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message code="master.page.actor.suspicious"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/administrator/list.do"><spring:message code="master.page.actor.suspicious"/></a></li>
					<li><a href="manager/administrator/listSuspicious.do"><spring:message code="master.page.manager.suspicious"/></a></li>
					<li><a href="ranger/administrator/listSuspicious.do"><spring:message code="master.page.ranger.suspicious"/></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.actor.register"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="auditor/administrator/create.do"><spring:message code="master.page.auditor.create"/></a></li>
					<li><a href="administrator/administrator/create.do"><spring:message code="master.page.administrator.create"/></a></li>
					<li><a href="manager/administrator/create.do"><spring:message code="master.page.manager.create"/></a></li>
					<li><a href="ranger/administrator/create.do"><spring:message code="master.page.ranger.create"/></a></li>
					<li><a href="sponsor/administrator/create.do"><spring:message code="master.page.sponsor.create"/></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv" href="tag/administrator/list.do"><spring:message code="master.page.tag.list" /></a></li>
			
			<li><a class="fNiv" href="category/administrator/list.do"><spring:message code="master.page.category.list" /></a></li>
			
			<li><a class="fNiv" href="legaltext/administrator/list.do"><spring:message code="master.page.legaltext.list" /></a></li>
			
			
			<li><a class="fNiv" href="systemConfiguration/administrator/display.do"><spring:message code="master.page.systemConfiguration.display" /></a></li>

		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message code="master.page.actor.register"/></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="explorer/create.do"><spring:message code="master.page.explorer.create" /></a></li>
					<li><a href="ranger/create.do"><spring:message code="master.page.ranger.create" /></a></li>
				</ul>
			</li>
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
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="administrator/administrator/edit.do"><spring:message code="master.page.actor.edit" /> </a></li>
						<li><a href="administrator/dashboard.do"><spring:message code="master.page.administrator.dashboard"/></a></li>
					</security:authorize>
					<security:authorize access="hasRole('RANGER')">
						<li><a href="ranger/ranger/edit.do"><spring:message code="master.page.actor.edit" /> </a></li>						
					</security:authorize>
					<security:authorize access="hasRole('MANAGER')">
						<li><a href="manager/manager/edit.do"><spring:message code="master.page.actor.edit" /> </a></li>
					</security:authorize>
					<security:authorize access="hasRole('EXPLORER')">
						<li><a href="explorer/explorer/edit.do"><spring:message code="master.page.actor.edit" /> </a></li>
					</security:authorize>
					<security:authorize access="hasRole('AUDITOR')">
						<li><a href="auditor/auditor/edit.do"><spring:message code="master.page.actor.edit" /> </a></li>
					</security:authorize>
					<security:authorize access="hasRole('SPONSOR')">
						<li><a href="sponsor/sponsor/edit.do"><spring:message code="master.page.actor.edit" /> </a></li>
					</security:authorize>
					<li><a href="socialIdentity/actor/list.do"><spring:message code="master.page.socialIdentity.list" /> </a></li>
					<li><a href="folder/actor/list.do"><spring:message code="master.page.folder.list" /> </a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

