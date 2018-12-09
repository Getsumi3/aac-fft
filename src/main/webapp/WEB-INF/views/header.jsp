<%@ include file="/common/taglib.jsp"%>
<%@ page pageEncoding="UTF-8"%>
<div class="container">
	<div class="row">
		<div class="col col-lg-12 col-md-12 col-sm-12 col-xs-12 header-social">
			<security:authorize access="hasRole('ROLE_ADMIN')">
				<a href="<c:url value="/admin/main" />">admin</a>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')">
				<a href="<c:url value="/moderation" />">moderator</a>
			</security:authorize>

			Join Us
			<security:authorize access="hasRole('ROLE_USER')">,&nbsp;${pageContext.request.userPrincipal.name} </security:authorize>
			! <a href="#"> <i class="fa fa-facebook"></i></a> <a href="#"> <i
				class="fa fa-twitter"></i> <a href="#"> <i
					class="fa fa-envelope"></i> <a href="#"> <i
						class="fa fa-google-plus"></i> <a href="#"> <i
							class="fa fa-youtube"></i> <a href="#"> &nbsp;&nbsp;&nbsp; <security:authorize
									access="hasRole('ROLE_ANONYMOUS')">
									<a href="registration.html">Register</a>&nbsp;or&nbsp;<a
										href="login.html">Login</a>
								</security:authorize> <security:authorize access="hasRole('ROLE_USER')">
									<a href="#">Profile</a>
									<a href="#" onclick="document.forms['logoutForm'].submit()">Logout</a>
								</security:authorize> <c:if test="${pageContext.request.userPrincipal.name != null}">
									<form id="logoutForm" method="POST"
										action="${contextPath}/logout">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" />
									</form>
								</c:if>
		</div>
	</div>
	<div class="row header-main">
		<!--  		<div class="col col-xs-12 col-sm-4 col-md-3 col-lg-3 header-logo">
			<a href="${contextPath}/home.html"><img src="${contextPath}/resources/img/logo_white.png" /></a>
		</div>
		
		-->
		<div class="col col-xs-12 col-sm-8 col-md-9 col-lg-9 header-menu">
			
			<p style="">Fast Stock</p>
			<div class="col-lg-8" style="margin-left: 25%;">
			<a href="${contextPath}/">Home</a> 
			<a href="${contextPath}/stock">Stock quotes</a>
			<a href="#">About</a>
			</div>
			
		</div>
	</div>
	<div class="homeText">
		<h1>In order to have access to all features please SIGN IN or REGISTER</h1>
		<p></p>
		</div>
</div>