<%@page
	import="org.springframework.security.authentication.AuthenticationServiceException"%>
<%@page
	import="org.springframework.security.authentication.AccountExpiredException"%>
<%@page
	import="org.springframework.security.authentication.DisabledException"%>
<%@page
	import="org.springframework.security.authentication.LockedException"%>
<%@page
	import="org.springframework.security.authentication.BadCredentialsException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.hotent.core.util.AppUtil"%>
<%@page import="java.util.Properties"%>
<%@ page pageEncoding="UTF-8"%>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>广州移动人力资源平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/styles/hrlogin/login.css"></link>
<%
	Properties configProperties=(Properties)AppUtil.getBean("configproperties");
	String validCodeEnabled=configProperties.getProperty("validCodeEnabled");
%>
<script type="text/javascript">
		if(top!=this){//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			  top.location='${ctx}/login.jsp';
		}
		
		function reload(){
			var url="${ctx}/servlet/ValidCode?rand=" +new Date().getTime();
			document.getElementById("validImg").src=url;
		}
		
		$(function(){
			$('.main_login').find('input').keydown(function(event){
				if(event.keyCode==13){
					$('#form-login').submit();
				}
			});
		});
</script>
<!--[if IE 6]> 
    <script type="text/javascript" src="/DD_belatedPNG_0.0.8a.js"></script>
    <script type="text/javascript"> 
    DD_belatedPNG.fix('div,ul,img,li,input,a,background,span'); //兼容IE6的PNG图片
    </script>  
    <![endif]-->
</head>
<body>
	<form id="form-login" action="${ctx}/login.ht" method="post">

		<div class="main_login">
			<div class="content_login">
				<span class="bpmlogo_login"></span>
				<table class="column">
				<tr>
					<td class="login_font">用户名:</td> 
					<td><input
						class="login_input type=" text" name="username" class="login" /><br></td>
				</tr>
				<tr>
					<td class="login_font">密&nbsp;&nbsp;&nbsp;&nbsp;码:</td> <td><input
						class="login_input" type="password" name="password" /></td>
				</tr>
				<%
                          if(validCodeEnabled!=null && "true".equals(validCodeEnabled)){
                     %>
				<div class="vcode column">
					<div>
						<span class="login_font">验证码:</span><br> <input type="text"
							name="validCode" />
					</div>
					<div class="vcode_img">
						<img id="validImg" src="${ctx}/servlet/ValidCode" /><br> <a
							onclick="reload()">看不清，换一张</a>
					</div>

				
				<%
                          }
                    %>
				<%
				Object loginError=(Object)request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				
				if(loginError!=null ){
					String msg="";
					if(loginError instanceof BadCredentialsException){
						msg="密码输入错误";
					}
					else if(loginError instanceof AuthenticationServiceException){
						AuthenticationServiceException ex=(AuthenticationServiceException)loginError;
						msg=ex.getMessage();
					}
					else{
						msg=loginError.toString();
					}
				%>
				<tr class="errormsg">
					<td align="right" colspan="2" style="color:#ff0000;"><%=msg %></td>
				</tr>
				<%
				}
				%>

					<tr>
						<td><input type="checkbox"
							name="_spring_security_remember_me" value="1" /><span
							class="login_font">系统记住我</span></td>
							<td>
						<a class="reset_btn btn r"
							onclick="document.getElementById('form-login').reset();"></a>
						<a class="login_btn btn r"
							onclick="document.getElementById('form-login').submit();"></a></td>
					</tr>
				</table>
			</div>
		</div>

	</form>
	<div class="copy">&copy;版权所有 中国移动(China Mobile)</div>
</body>
</html>
