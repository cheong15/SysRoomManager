<%--
	time:2017-09-07 18:34:39
	desc:edit the 公告管理表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>公告审核</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/js/hotent/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/formdata.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#formInfo").ligerTab();
			$("#check_success").click(function() {
				$("#announcementForm").attr("action","check.ht?status=CHECK_SUCCESS");
				submitForm();
			});
			$("#check_fail").click(function() {
				$("#announcementForm").attr("action","check.ht?status=CHECK_FAIL");
				submitForm();
			});
		});
		//提交表单
		function submitForm(){
			if($("#checkOpinion").val() == ""){
				$.ligerDialog.warn("审批意见不许为空！","提示信息");
				return;
			}
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#announcementForm').form();
			frm.setData();
			frm.ajaxForm(options);
			if(frm.valid()){
				form.submit();
			}
		}
		
		function showResponse(responseText) {
			var obj = new com.hotent.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.success(obj.getMessage(),"提示信息", function(rtn) {
					if(rtn){
						if(window.opener){
							window.opener.location.reload();
							window.close();
						}else{
							this.close();
							window.location.href="list.ht";
						}
					}
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
		
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">公告审批</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="check_success" href="#">审批通过</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link back" id="check_fail" href="#">审批不通过</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link back" href="list.ht">返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body" type="custform">
		<form id="announcementForm" method="post" action="save.ht">
			<div id="formInfo" >
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
					<input type="hidden" name="id" value="${announcement.id}" />   <!-- id放到主表TABLE里面,生成的内容才能获取主表提交的数据的ID ??? -->
					<tr>
						<th width="20%">公告标题:</th>
						<td>${announcement.title}</td>
					</tr>
					<tr>
						<th width="20%">公告正文:</th>
						<td>${announcement.contents}</td>
					</tr>
					
					<tr>
						<th width="20%">公告类型:</th>
						<td>${announcement.type}</td>
					</tr>
					<tr>
						<th width="20%">发布部门:</th>
						<td>${announcement.publishDept}</td>
					</tr>
					<tr>
						<th width="20%">发布时间:</th>
						<td>
						<fmt:formatDate value="${announcement.publishTime}" pattern="yyyy-MM-dd"/>
						</td>
					</tr>
					<tr>
						<th width="20%">过期时间:</th>
						<td>
						<fmt:formatDate value="${announcement.overdueTime}" pattern="yyyy-MM-dd"/>
						</td>
					</tr>
					<tr>
						<th width="20%">公告状态:</th>
						<td>${announcement.status}</td>
					</tr>
					<tr>
						<th width="20%">创建人:</th>
						<td>${announcement.creatorName}</td>
					</tr>
					
					<tr>
						<th width="20%">创建时间:</th>
						<td>
						<fmt:formatDate value="${announcement.createTime}" pattern="yyyy-MM-dd"/>
						</td>
					</tr>
					<tr>
						<th width="20%">修改人:</th>
						<td>${announcement.editorName}</td>
					</tr>
					
					<tr>
						<th width="20%">修改时间:</th>
						<td>
						<fmt:formatDate value="${announcement.editTime}" pattern="yyyy-MM-dd"/>
						</td>
					</tr>
					<tr>
						<th width="20%"><font color="red">*</font>审批意见: </th>
						<td class="formInput" colspan="4">
	                        <textarea rows="4" name="checkOpinion" id="checkOpinion" style="width:620px"></textarea>
	                    </td>
					</tr>
				</table>
			</div>		
		</form>
	</div>
</div>
</body>
</html>
