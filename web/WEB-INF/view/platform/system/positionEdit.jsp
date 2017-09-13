<%--
	time:2011-11-30 09:49:45
	desc:edit the 系统岗位表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 系统岗位</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx }/js/lg/plugins/ligerWindow.js" ></script>
	<script type="text/javascript"	src="${ctx }/js/hotent/platform/system/SysDialog.js"></script>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=position"></script>
	<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			}
			function showResponse(responseText, statusText)  { 
				var self=this;
				var obj=new com.hotent.form.ResultMessage(responseText);
				if(obj.isSuccess()){//成功
					$.ligerDialog.confirm( obj.getMessage()+",是否继续操作","提示信息",function(rtn){
						if(!rtn){
							var returnUrl=$("#returnUrl").val();
							if($("#returnUrl").length>0 && returnUrl!=""){
								location.href=returnUrl;
								parent.loadTree();// 刷新树节点
								return;
							}
							var linkBack=$("a.back");
							if(linkBack.length>0){
								var returnUrl=linkBack.attr("href");
								if(returnUrl!=""){
									location.href=returnUrl;
									parent.loadTree();
									return;
								}
							}
						}
						else{
							if(self.isReset==1){
								__valid.resetForm();
								parent.loadTree();
							}
						}
					});
					
			    }else{//失败
			    	$.ligerDialog.error( obj.getMessage(),"出错了");
			    }
			}
			
			if(${position.posId ==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				$('#positionForm').submit();
			});
		});
		
		function getInsertUser(userId,fullname){
			var aryTr=['<tr>',
			'<td>',
			'	<input type="checkbox" class="pk" name="userPosId" value="">',
			'</td>',
			
			'<td>',
			'   <input type="hidden" name="userId" id="'+ userId +'" value="'+userId+'" readonly="readonly">',
			fullname,
			'</td>',
			'<td>',
			'	<input type="checkbox" name="chkPrimary"	 checked="checked" value="'+userId+'" >',
			'</td>',
			'<td >',
			'	<a href="#" class="link del" onclick="singleDel(this);">删除</a>',
			'</td>',
			'</tr>'];
			return aryTr.join('');
		}

		function dlgCallBack(userIds,fullnames){
			var newUserIds=userIds.split(",");
			var newUserNames=fullnames.split(",");
			for(var i=0;i<newUserIds.length;i++){
				var userId=newUserIds[i];
				var userName=newUserNames[i];
				var obj=$("#" +userId);
				if(obj.length>0) continue;
				var tr=getInsertUser(userId,userName);
				$("#userPositionItem").append(tr);
			}
		};
		
		function add(){
			UserDialog({isSingle:false,callback:dlgCallBack});
		};
		
		function checkDell(){
			var trCheckeds=$("#userPositionItem").find(":checkbox[name='userPosId'][checked]");
			$.each(trCheckeds,function(i,c){
				var tr=$(c).parents('tr');
				$(tr).remove();
			});
			
		};
		function singleDel(obj){
			var tr=$(obj).parents('tr');
			$(tr).remove();
		};
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
			    <c:choose>
			       <c:when test="${position.posId !=null}">
			           <span class="tbar-label">编辑岗位信息</span>
			       </c:when>
			       <c:otherwise>
			           <span class="tbar-label">添加岗位信息</span>
			       </c:otherwise>
			    </c:choose>
				
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="#"><span></span>保存</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link back" href="${returnUrl}"><span></span>返回</a></div>
				</div>
			</div>
		</div>
		<form id="positionForm" method="post" action="save.ht">
		<div class="panel-detail">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width="20%">岗位名称:  <span class="required">*</span></th>
					<td><input type="text" id="posName" name="posName" value="${position.posName}"  class="inputText"/></td>
				</tr>
				<tr>
					<th width="20%">岗位描述: </th>
					<td>
					<textarea rows="3" cols="50" id="posDesc" name="posDesc">${position.posDesc}</textarea>
					<input type="hidden" id="parentId" name="parentId" value="${position.parentId}"  class="inputText"/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="posId" value="${position.posId}" />
			<input type="hidden" id="returnUrl" value="${returnUrl}" />
		</div>
				
		<c:if test="${position.posId==null }">
		
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">岗位人员</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link add" href="#" onclick="add();"><span></span>添加</a>
						</div>
						<div class="l-bar-separator"></div>
						
						<div class="group">
							<a class="link del" href="#" onclick="checkDell();"><span></span>删除</a>
						</div>
					</div>
				</div>
			</div>
		
		
			<div class="panel-data" style="padding-top: 5px;">
			   	<table id="userPositionItem" class="table-grid table-list" id="0" cellpadding="1" cellspacing="1">
			   		<thead>
			   			<th width="7px"><input type="checkbox" id="chkall"></th>
			   			<th style="display: none;">用户ID</th>
			    		<th width="40%">用户</th>
			    		<th >是否主岗位</th>
			    	
			    		<th >管理</th>
			    	</thead>
			    	<tbody>
			    	<c:forEach items="${userPositionList}" var="userPositionItem">
			    		<tr>
			    			<td>
				    			<input type="checkbox" class="pk"	name="userPosId" value="${userPositionItem.userPosId}">
				    		</td>
				    		<td>
			    				<input type="hidden" name="userId"	id="${userPositionItem.userId}" value="${userPositionItem.userId}" readonly="readonly">
			    				${userPositionItem.fullname}
			    			</td>
			    			<td >
			    				<input type="checkbox" name="chkPrimary"	 value="${userPositionItem.userId}"  <c:if test="${userPositionItem.isPrimary==0}">checked="checked"</c:if> >
			    			</td>
			    			<td >
			    				<a href="#" class="link del" onclick="singleDel(this);">删除</a>
							</td>
			    		</tr>
			    	</c:forEach>
			    	</tbody>
			    </table>
			  </div>
			</c:if>
			
		</div>
		</form>
</div>	
</body>
</html>
