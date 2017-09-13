<%--
	time:2011-11-30 09:49:45
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>岗位明细</title>
	<%@include file="/commons/include/get.jsp" %>
</head>
<body>
<div class="panel">
<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">岗位详细信息</span>
			</div>
				<c:if test="${isOtherLink==0}">
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group">
								<a class="link back" href="../position/list.ht"><span></span>返回</a>
							</div>
						</div>
					</div>
				</c:if>
			</div>
		</div>
		<div class="panel-body">
			<div class="panel-detail">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th width="20%">岗位名称:</th>
						<td>${position.posName}</td>
					</tr>
					<tr>
						<th width="20%">岗位描述:</th>
						<td>${position.posDesc}</td>
					</tr>
					
					<tr>
						<th width="20%">序号:</th>
						<td>${position.sn}</td>
					</tr>
					
				</table>
			</div>
		</div>
</body>
</html>
