<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>公告管理表管理</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">公告管理表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" href="edit.ht"><span></span>添加</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link update" id="btnUpd" action="edit.ht"><span></span>修改</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link ok" action="publish.ht?status=PUBLISH"><span></span>发布</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del" action="del.ht"><span></span>删除</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht">
					<div class="row">
						<span class="label">公告标题:</span><input type="text" name="Q_title_SL"  class="inputText" />
						<span class="label">公告类型:</span>
						<select class="inputText" id="Q_type_SL" name="Q_type_SL">
	                       <option value="">全部</option>
	                       <c:forEach items="${announcementTypeList}" var="type">
	                            <option value="${type}"
	                                    <c:if test="${param['type']==type}">selected="selected"</c:if>>${type}</option>
	                        </c:forEach> 
	                    </select>
						<span class="label">发布时间:</span> <input  name="Q_beginpublishTime_DL"  class="inputText date" />
						 至: </span><input  name="Q_endpublishTime_DG" class="inputText date" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="announcementList" id="announcementItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${announcementItem.id}">
				</display:column>
				<display:column property="title" title="公告标题" sortable="true" sortName="TITLE" maxLength="80"></display:column>
				<display:column property="contents" title="公告正文" sortable="true" sortName="CONTENTS" maxLength="20"></display:column>
				<display:column property="type" title="公告类型" sortable="true" sortName="TYPE"></display:column>
				<display:column property="publishDept" title="发布部门" sortable="true" sortName="PUBLISH_DEPT"></display:column>
				<display:column  title="发布时间" sortable="true" sortName="PUBLISH_TIME">
					<fmt:formatDate value="${announcementItem.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column  title="过期时间" sortable="true" sortName="OVERDUE_TIME">
					<fmt:formatDate value="${announcementItem.overdueTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column property="status" title="公告状态" sortable="true" sortName="STATUS"></display:column>
				<display:column property="creatorName" title="创建人" sortable="true" sortName="CREATOR_NAME"></display:column>
				<display:column  title="创建时间" sortable="true" sortName="CREATE_TIME">
					<fmt:formatDate value="${announcementItem.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<%-- <display:column property="editorName" title="修改人" sortable="true" sortName="EDITOR_NAME"></display:column>
				<display:column  title="修改时间" sortable="true" sortName="EDIT_TIME">
					<fmt:formatDate value="${announcementItem.editTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column property="checkerName" title="审核人" sortable="true" sortName="CHECKER_NAME"></display:column>
				<display:column  title="审核时间" sortable="true" sortName="CHECK_TIME">
					<fmt:formatDate value="${announcementItem.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column> --%>
				<display:column title="管理" media="html" style="width:220px">
					<a href="del.ht?id=${announcementItem.id}" class="link del">删除</a>
					<a href="edit.ht?id=${announcementItem.id}" class="link edit">编辑</a>
					<a href="get.ht?id=${announcementItem.id}" class="link detail">明细</a>
					<a href="get.ht?id=${announcementItem.id}" class="link detail">预览</a>
					<c:if test="${announcementItem.status=='草稿'}"><a href="checkView.ht?id=${announcementItem.id}" class="link detail">审批</a></c:if>
					<c:if test="${announcementItem.status=='审核通过'}"><a href="publish.ht?id=${announcementItem.id}&status=PUBLISH" class="link detail">发布</a></c:if>
					<c:if test="${announcementItem.status=='发布'}"><a href="publish.ht?id=${announcementItem.id}&status=CHECK_SUCCESS" class="link detail">取消发布</a></c:if>
				</display:column>
			</display:table>
			<hotent:paging tableId="announcementItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
<script type="text/javascript">
$("div.group > a.link.ok").click(function()
		{	
			if($(this).hasClass('disabled')) return false;
			
			var action=$(this).attr("action");
			var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
			
			if($aryId.length == 0){
				$.ligerDialog.warn("请选择记录！");
				return false;
			}
			
			//提交到后台服务器进行日志删除批处理的日志编号字符串
			var delId="";
			var keyName="";
			var len=$aryId.length;
			$aryId.each(function(i){
				var obj=$(this);
				if(i<len-1){
					delId+=obj.val() +",";
				}
				else{
					keyName=obj.attr("name");
					delId+=obj.val();
				}
			});
			var url=action +"?" +keyName +"=" +delId ;
			
			$.ligerDialog.confirm('确认发布吗？','提示信息',function(rtn) {
				if(rtn) {
					var form=new com.hotent.form.Form();
					form.creatForm("form", action);
					form.addFormEl(keyName, delId);
					form.submit();
				}
			});
			return false;
		
			
		});
</script>
</html>


