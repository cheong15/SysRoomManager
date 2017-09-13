<%--
	time:2017-09-07 18:34:39
	desc:edit the 公告管理表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 公告管理表</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/js/hotent/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/formdata.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#formInfo").ligerTab();
			$("a.save").click(function() {
				$("#announcementForm").attr("action","save.ht");
				submitForm();
			});
		});
		//提交表单
		function submitForm(){
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
		    <c:choose>
			    <c:when test="${announcement.id !=null}">
			        <span class="tbar-label">编辑公告管理表</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label">添加公告管理表</span>
			    </c:otherwise>			   
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="#">保存</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link back" href="list.ht">返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body" type="custform">
		<form id="announcementForm" method="post" action="save.ht">
			<div id="formInfo" >
				<div title="公告管理表主表明细">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
						<input type="hidden" name="id" value="${announcement.id}" />   <!-- id放到主表TABLE里面,生成的内容才能获取主表提交的数据的ID ??? -->
						<tr>
							<th width="20%">公告标题: </th>
							<td><input type="text" id="title" name="title" value="${announcement.title}"  class="inputText" validate="{required:false,maxlength:510}"  /></td>
						</tr>
						<tr>
							<th width="20%">发布部门: </th>
							<td><input type="text" id="publishDept" name="publishDept" value="${announcement.publishDept}"  class="inputText" validate="{required:false,maxlength:256}"  /></td>
						</tr>
						<tr>
							<th width="20%">公告类型: </th>
							<td>
								<select class="inputText" id="Q_type_SL" name="type">
			                       <c:forEach items="${announcementTypeList}" var="type">
			                            <option value="${type}"
			                                    <c:if test="${announcement.type==type}">selected="selected"</c:if>>${type}</option>
			                        </c:forEach> 
			                    </select>
							</td>
							<%-- <td><input type="text" id="type" name="type" value="${announcement.type}"  class="inputText" validate="{required:false,maxlength:128}"  /></td> --%>
						</tr>
						<tr>
							<th width="20%">发布时间: </th>
							<td><input type="text" id="publishTime" name="publishTime" value="<fmt:formatDate value='${announcement.publishTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" class="inputText date" validate="{date:true}" /></td>
						</tr>
						<tr>
							<th width="20%">过期时间: </th>
							<td><input type="text" id="overdueTime" name="overdueTime" value="<fmt:formatDate value='${announcement.overdueTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" class="inputText date" validate="{date:true}" /></td>
						</tr>
						<%-- <tr>
							<th width="20%">公告状态: </th>
							<td><input type="text" id="status" name="status" value="${announcement.status}"  class="inputText" validate="{required:false,maxlength:128}"  /></td>
						</tr> --%>
						<tr>
							<th width="20%">公告正文: </th>
							<td class="formInput" colspan="4">
		                        <textarea rows="12" name="contents" style="width:620px">${announcement.contents}</textarea>
		                    </td>
						</tr>
					</table>
				</div>
			</div>		
		</form>
	</div>
</div>
</body>
</html>
