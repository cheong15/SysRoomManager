
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>岗位人员设置</title>
<%@include file="/commons/include/get.jsp" %>

<script type="text/javascript"	src="${ctx }/js/hotent/platform/system/SysDialog.js"></script>
<script type="text/javascript">
	function dlgCallBack(userIds,fullnames){
		
		if(userIds.length>0){
			var form=new com.hotent.form.Form();
			form.creatForm("form", "${ctx}/platform/system/userPosition/add.ht");
			form.addFormEl("posId", "${position.posId}");
			form.addFormEl("userIds", userIds);
			form.submit();
		}
	};
	function addClick(){
		UserDialog({callback:dlgCallBack,isSingle:false});
	};
</script>
</head>
<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${position.posName }-岗位人员设置</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" onclick="addClick();"><span></span>添加人员</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del"  action="del.ht"><span></span>删除</a></div>
					
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link back" href="${ctx }/platform/system/position/list.ht"><span></span>返回</a></div>
					
				</div>	
				
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="edit.ht?posId=${position.posId}">
							<ul class="row">
								<li><span class="label">姓名:</span><input type="text" name="Q_fullname_SL"  class="inputText" value="${param['Q_fullname_SL']}"/></li>
								<li><span class="label">帐号:</span><input type="text" name="Q_account_SL"  class="inputText" value="${param['Q_account_SL']}"/>			</li>		
							</ul>
					</form>
			</div>
		</div>
		</div>
		<div class="panel-body">
			
			
			
		    	<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table name="userPositionList" id="userPositionItem" requestURI="edit.ht" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
						  	<input type="checkbox" class="pk" name="userPosId" value="${userPositionItem.userPosId}">
						  	
					</display:column>
					<display:column property="fullname" title="用户" style="text-align:left"></display:column>
					<display:column property="account" title="帐号" style="text-align:left"></display:column>
					<display:column title="是否主岗位"  media="html">
						<c:if test="${userPositionItem.isPrimary==0}"><span class="red">否</span></c:if>
						<c:if test="${userPositionItem.isPrimary==1}"><span class="green">是</span></c:if>
					</display:column>
					<display:column title="管理" media="html" style="width:180px">
						<a href="del.ht?userPosId=${userPositionItem.userPosId}" class="link del">删除</a>
						<c:choose>
							<c:when test="${userPositionItem.isPrimary==0}">
								<a class="link primary" href="setIsPrimary.ht?userPosId=${userPositionItem.userPosId}">设置为主岗位</a>
							</c:when>
							<c:otherwise>
								<a class="link notPrimary" href="setIsPrimary.ht?userPosId=${userPositionItem.userPosId}">设置为非主岗位</a>
							</c:otherwise>
						</c:choose>									
					</display:column>
				</display:table>
				<hotent:paging tableId="userPositionItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


