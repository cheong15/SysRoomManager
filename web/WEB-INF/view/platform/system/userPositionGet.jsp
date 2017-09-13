
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>岗位人员设置</title>
	<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${position.posName }</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					
					<div class="group"><a class="link back" href="${returnUrl}"><span></span>返回</a></div>
					
				</div>	
			</div>
		</div>
		</div>
		<div class="panel-body">
		    	<display:table name="userPositionList" id="userPositionItem"  cellpadding="1" cellspacing="1" export="false"  class="table-grid">
					<display:column property="fullname" title="用户" style="text-align:left;" ></display:column>
					<display:column title="是否主岗位" media="html">
						<c:if test="${userPositionItem.isPrimary==0}"><font color="red"><b>否</b></c:if>
						<c:if test="${userPositionItem.isPrimary==1}"><font color="green"><b>是</b></font></c:if>
					</display:column>
				</display:table>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


