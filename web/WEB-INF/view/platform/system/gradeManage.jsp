<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.jee-soft.cn/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>分级组织管理</title>
<%@include file="/commons/include/get.jsp"%>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/system/OrgManage.js"></script>
<script type="text/javascript">
		var contextMenu=null;
		$(function() {
			$("#layout").ligerLayout({
				leftWidth : 220,
				height : '100%',
				allowLeftResize : false
			});
			height = $('#layout').height();
			$("#viewFrame").height(height - 25);
			
			$("#treeReFresh").click(function() {
				OrgTree.loadTree();
			});
		
			$("#treeExpand").click(function() {
				OrgTree.orgTree.expandAll(true);
			});
			$("#treeCollapse").click(function() {
				OrgTree.orgTree.expandAll(false);
			});
			getMenu();
			OrgTree.loadTree();
		});
		
		//右键菜单
		function getMenu() {
			contextMenu = $.ligerMenu({
				top : 100,
				left : 100,
				width : 100,
				items:<f:menu>
					[ {
						text : '增加',
						click: 'OrgTree.addOrg',
						alias:'gradeOrgEdit'
					}, {
						text : '编辑',
						click:"OrgTree.editOrg",
						alias:'gradeOrgEdit'
					}, {
						text : '参数属性',
						click: 'OrgTree.orgParam',
						alias:''
					}, {
						text : '删除',
						click: 'OrgTree.delNode',
						alias:'delOrg'
					}]
					</f:menu>       
			});

		};
</script>
<style type="text/css">
html {height: 100%}
body {padding: 0px;margin: 0;overflow: auto;}
#layout {width: 99.5%;margin: 0;padding: 0;}
</style>
</head>
<body>
	<div id="layout" style="bottom: 1; top: 1">
		<div position="left" title="组织机构管理" id="rogTree" style="height: 100%; width: 100% !important;">
		
			<div class="tree-toolbar" id="pToolbar">
				<div class="toolBar"
					style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					<div class="group">
						<a class="link reload" id="treeReFresh" ></a>
					</div>
					<div class="l-bar-separator"></div>
					<div class="group">
						<a class="link expand" id="treeExpand" ></a>
					</div>
					<div class="l-bar-separator"></div>
					<div class="group">
						<a class="link collapse" id="treeCollapse" ></a>
					</div>
				</div>
			</div>
			<ul id="orgTree" class="ztree" style="width: 180px; margin: 0; padding: 0;"></ul>

		</div>
		<div position="center" id="orgView" style="height: 100%;">
			<div class="l-layout-header">组织架构信息</div>
			<iframe id="viewFrame"  frameborder="0" width="100%" height="100%"></iframe>
		</div>
	</div>

</body>
</html>


