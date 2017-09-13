<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>系统数据源管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	$(function() 
	{
		$('#testConnect').click(function() {
			var ids = '';
			$(':checkbox[name=id][checked]').each(function() {
				ids += ',' + $(this).val();
			});
			if(ids.length == 0) {
				$.ligerDialog.warn('请选择记录!');
				return;
			}
			$.ligerDialog.waitting('正在测试连接，请等待...');
			$.post('testConnectById.ht', {id: ids.substring(1)}, function(data) {
				$.ligerDialog.closeWaitting();
				var msg = '';
				var success = true;
				$(data).each(function(i, d) {
					if(d.success) {
						msg += '<p>' +  d.name + ': <font color="green">连接成功!</font></p>';
					} else {
						success = false;
						msg += '<p>' +  d.name + ': <font color="red">连接失败!<br>原因：' + d.msg + '</font></p>';
					}
				});
				if(success) {
					$.ligerDialog.success(msg);
				} else {
					$.ligerDialog.error(msg);
				}
			});
			
		});
	});
</script>
</head>
<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统数据源列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" href="edit.ht"><span></span>添加</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link update" id="btnUpd" action="edit.ht"><span></span>修改</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del"  action="del.ht"><span></span>删除</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link test" id="testConnect"><span></span>测试连接</a></div>
				</div>	
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.ht">
							<ul class="row">
										<li><span class="label">数据源名称:</span><input type="text" name="Q_name_SL"  class="inputText" value="${param['Q_name_SL']}"/></li>
									
										<li><span class="label">别名:</span><input type="text" name="Q_alias_SL"  class="inputText" value="${param['Q_alias_SL']}"/></li>
									
										<li><span class="label">数据库类型:</span>
										<select id="Q_dbType_S"  name="Q_dbType_S">
											<option value="">请选择</option>
											<option value="oracle" <c:if test="${param['Q_dbType_S'] == 'oracle'}">selected</c:if>>ORACLE</option>
											<option value="mssql" <c:if test="${param['Q_dbType_S'] == 'mssql'}">selected</c:if>>MSSQL2005</option>
											<option value="mysql" <c:if test="${param['Q_dbType_S'] == 'mysql'}">selected</c:if>>MYSQL</option>
											<option value="db2" <c:if test="${param['Q_dbType_S'] == 'db2'}">selected</c:if>>DB2</option>
											<option value="db2" <c:if test="${param['Q_dbType_S'] == 'h2'}">selected</c:if>>H2</option>
											<option value="dm"  <c:if test="${param['Q_dbType_S'] == 'dm'}">selected</c:if>>达梦</option>
										</select></li>
							</ul>
					</form>
			</div>
		</div>
		</div>
		<div class="panel-body">
			
			
		    	<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table name="sysDataSourceList" id="sysDataSourceItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
						  	<input type="checkbox" class="pk" name="id" value="${sysDataSourceItem.id}">
					</display:column>
					<display:column property="name" style="text-align:left;" title="数据源名称" sortable="true" sortName="name"></display:column>
					<display:column   title="数据库类型" >
							<c:choose>
								<c:when test="${sysDataSourceItem.dbType=='oracle' }">ORACLE</c:when>
								<c:when test="${sysDataSourceItem.dbType=='mssql'}">MSSQL2005</c:when>
								<c:when test="${sysDataSourceItem.dbType=='mysql' }">MYSQL</c:when>
								<c:when test="${sysDataSourceItem.dbType=='db2'}">DB2</c:when>
								<c:when test="${sysDataSourceItem.dbType=='h2'}">H2</c:when>
								<c:when test="${sysDataSourceItem.dbType=='dm' }">达梦</c:when>
								<c:otherwise>
									未知类型
								</c:otherwise>
							</c:choose>
					</display:column>
					<display:column property="alias" title="别名" sortable="true" sortName="alias"></display:column>
					<display:column title="管理" media="html" style="width:120px;text-align:center">
						<f:a alias="delDataresource" href="del.ht?id=${sysDataSourceItem.id}" css="link del">删除</f:a>
						<a href="edit.ht?id=${sysDataSourceItem.id}" class="link edit">编辑</a>
					</display:column>
				</display:table>
				<hotent:paging tableId="sysDataSourceItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


