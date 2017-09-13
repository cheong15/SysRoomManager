<%--
	time:2011-12-28 14:04:30
	desc:edit the 模版管理
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑模版管理</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript"
	src="${ctx}/servlet/ValidJs?form=sysTemplate"></script>
<script type="text/javascript"
	src="${ctx }/js/hotent/platform/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript"
	src="${ctx}/js/ckeditor/ckeditor_sysTemp.js"></script>
<script type="text/javascript">
	$(function() {
		function showRequest(formData, jqForm, options) { 
			return true;
		} 
		innerEditor = ckeditor('innerContent');
		mailEditor = ckeditor('mailContent');
		valid(showRequest,showResponse);
		$("a.save").click(function() {
			$('#innerContent').val(innerEditor.getData());
			$('#mailContent').val(mailEditor.getData());
			$('#sysTemplateForm').submit(); 
		});					
	});
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<c:choose>
					<c:when test="${sysTemplate.templateId !=null }">
						<span class="tbar-label">编辑模版管理</span>
					</c:when>
					<c:otherwise>
						<span class="tbar-label">添加模版管理</span>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="#"><span></span>保存</a>
					</div>
					<div class="l-bar-separator"></div>
					<div class="group">
						<a class="link back" href="list.ht"><span></span>返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">

			<form id="sysTemplateForm" method="post" action="save.ht">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th>模版名称: </th>
							<td><input type="text" id="name" name="name" value="${sysTemplate.name}" class="inputText" style="width:500px !important"/></td>
						</tr>
						<tr>
							<th>模版用途: </th>
							<td>
								<select name="useType">
									<option value="1"<c:if test="${sysTemplate.useType eq 1}"> selected="selected"</c:if>>终止提醒</option>
									<option value="2"<c:if test="${sysTemplate.useType eq 2}"> selected="selected"</c:if>>催办提醒</option>
									<option value="3"<c:if test="${sysTemplate.useType eq 3}"> selected="selected"</c:if>>审批提醒</option>
									<option value="4"<c:if test="${sysTemplate.useType eq 4}"> selected="selected"</c:if>>撤销提醒</option>
									<option value="5"<c:if test="${sysTemplate.useType eq 5}"> selected="selected"</c:if>>取消转办</option>
									<option value="6"<c:if test="${sysTemplate.useType eq 6}"> selected="selected"</c:if>>沟通提醒</option>
									<option value="7"<c:if test="${sysTemplate.useType eq 7}"> selected="selected"</c:if>>归档提醒</option>
									<option value="8"<c:if test="${sysTemplate.useType eq 8}"> selected="selected"</c:if>>转办提醒</option>
									<option value="9"<c:if test="${sysTemplate.useType eq 9}"> selected="selected"</c:if>>退回提醒</option>
									<option value="10"<c:if test="${sysTemplate.useType eq 10}"> selected="selected"</c:if>>沟通反馈</option>
									<option value="11"<c:if test="${sysTemplate.useType eq 11}"> selected="selected"</c:if>>取消代理</option>
									<option value="12"<c:if test="${sysTemplate.useType eq 12}"> selected="selected"</c:if>>抄送提醒</option>
									<option value="13"<c:if test="${sysTemplate.useType eq 13}"> selected="selected"</c:if>>流程节点无人员</option>
									<option value="19"<c:if test="${sysTemplate.useType eq 19}"> selected="selected"</c:if>>逾期提醒</option>
									<option value="22"<c:if test="${sysTemplate.useType eq 22}"> selected="selected"</c:if>>代理提醒</option>
									<option value="23"<c:if test="${sysTemplate.useType eq 23}"> selected="selected"</c:if>>消息转发</option>
									<option value="24"<c:if test="${sysTemplate.useType eq 24}"> selected="selected"</c:if>>重启任务</option>
									<option value="25"<c:if test="${sysTemplate.useType eq 25}"> selected="selected"</c:if>>通知任务所属人(代理)</option>
									<option value="26"<c:if test="${sysTemplate.useType eq 26}"> selected="selected"</c:if>>加签提醒</option>
									<option value="27"<c:if test="${sysTemplate.useType eq 27}"> selected="selected"</c:if>>加签反馈</option>
									<option value="28"<c:if test="${sysTemplate.useType eq 28}"> selected="selected"</c:if>>取消流转</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>标题: </th>
							<td>					
								<textarea rows="5" cols="30" id="title" name="title">${fn:escapeXml(sysTemplate.title)}</textarea>																											
							</td>
						</tr>
						
						<tr>
							<th>站内消息: </th>
							<td>					
								<textarea id="innerContent" name="innerContent">${fn:escapeXml(sysTemplate.innerContent)}</textarea>																											
							</td>
						</tr>
						<tr>
							<th>邮件: </th>
							<td>					
								<textarea id="mailContent" name="mailContent">${fn:escapeXml(sysTemplate.mailContent)}</textarea>																											
							</td>
						</tr>
						<tr>
							<th>短信: </th>
							<td>					
								<textarea rows="5" cols="30" id="smsContent" name="smsContent">${fn:escapeXml(sysTemplate.smsContent)}</textarea>																											
							</td>
						</tr>
					</table>
				<input type="hidden" name="templateId" value="${sysTemplate.templateId}" />
			</form>

		</div>
	</div>
</body>
</html>
