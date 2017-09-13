
<%--
	time:2017-09-07 18:34:39
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>公告管理表明细</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">公告管理表详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link back" href="list.ht"><span></span>返回</a>
					</div>
				</div>
			</div>
		</div>
		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
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
				<th width="20%">创建人(portalid):</th>
				<td>${announcement.creatorPortalid}</td>
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
				<th width="20%">修改人(portalid):</th>
				<td>${announcement.editorPortalid}</td>
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
				<th width="20%">审核人(portalid):</th>
				<td>${announcement.checkerPortalid}</td>
			</tr>
			<tr>
				<th width="20%">审核人:</th>
				<td>${announcement.checkerName}</td>
			</tr>
			<tr>
				<th width="20%">审核时间:</th>
				<td>
				<fmt:formatDate value="${announcement.checkTime}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<th width="20%">发布部门:</th>
				<td>${announcement.publishDept}</td>
			</tr>
		</table>
		</div>
	</div>
</body>
</html>

