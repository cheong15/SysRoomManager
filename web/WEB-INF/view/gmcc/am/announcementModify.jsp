<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/js/hotent/scriptMgr.js"></script>
<script type="text/javascript">
	function afterOnload(){
		var afterLoadJs=[
		     			'${ctx}/js/hotent/formdata.js',
		     			'${ctx}/js/hotent/subform.js'
		 ];
		ScriptMgr.load({
			scripts : afterLoadJs
		});
	}
</script>

<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
	<tr>
		<th width="20%">公告标题: </th>
		<td><input type="text" id="title" name="title" value="${announcement.title}" validate="{required:false,maxlength:510}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">公告正文: </th>
		<td><input type="text" id="contents" name="contents" value="${announcement.contents}" validate="{required:false}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">公告类型: </th>
		<td><input type="text" id="type" name="type" value="${announcement.type}" validate="{required:false,maxlength:128}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">发布时间: </th>
		<td><input type="text" id="publishTime" name="publishTime" value="<fmt:formatDate value='${announcement.publishTime}' pattern='yyyy-MM-dd'/>" validate="{required:false,date:true}" class="inputText date"/></td>
	</tr>
	<tr>
		<th width="20%">过期时间: </th>
		<td><input type="text" id="overdueTime" name="overdueTime" value="<fmt:formatDate value='${announcement.overdueTime}' pattern='yyyy-MM-dd'/>" validate="{required:false,date:true}" class="inputText date"/></td>
	</tr>
	<tr>
		<th width="20%">公告状态: </th>
		<td><input type="text" id="status" name="status" value="${announcement.status}" validate="{required:false,maxlength:128}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">创建人(portalid): </th>
		<td><input type="text" id="creatorPortalid" name="creatorPortalid" value="${announcement.creatorPortalid}" validate="{required:false,maxlength:128}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">创建人: </th>
		<td><input type="text" id="creatorName" name="creatorName" value="${announcement.creatorName}" validate="{required:false,maxlength:256}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">创建时间: </th>
		<td><input type="text" id="createTime" name="createTime" value="<fmt:formatDate value='${announcement.createTime}' pattern='yyyy-MM-dd'/>" validate="{required:false,date:true}" class="inputText date"/></td>
	</tr>
	<tr>
		<th width="20%">修改人(portalid): </th>
		<td><input type="text" id="editorPortalid" name="editorPortalid" value="${announcement.editorPortalid}" validate="{required:false,maxlength:128}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">修改人: </th>
		<td><input type="text" id="editorName" name="editorName" value="${announcement.editorName}" validate="{required:false,maxlength:256}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">修改时间: </th>
		<td><input type="text" id="editTime" name="editTime" value="<fmt:formatDate value='${announcement.editTime}' pattern='yyyy-MM-dd'/>" validate="{required:false,date:true}" class="inputText date"/></td>
	</tr>
	<tr>
		<th width="20%">审核人(portalid): </th>
		<td><input type="text" id="checkerPortalid" name="checkerPortalid" value="${announcement.checkerPortalid}" validate="{required:false,maxlength:128}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">审核人: </th>
		<td><input type="text" id="checkerName" name="checkerName" value="${announcement.checkerName}" validate="{required:false,maxlength:256}" class="inputText"/></td>
	</tr>
	<tr>
		<th width="20%">审核时间: </th>
		<td><input type="text" id="checkTime" name="checkTime" value="<fmt:formatDate value='${announcement.checkTime}' pattern='yyyy-MM-dd'/>" validate="{required:false,date:true}" class="inputText date"/></td>
	</tr>
	<tr>
		<th width="20%">发布部门: </th>
		<td><input type="text" id="publishDept" name="publishDept" value="${announcement.publishDept}" validate="{required:false,maxlength:256}" class="inputText"/></td>
	</tr>
</table>
<input type="hidden" name="id" value="${announcement.id}" />