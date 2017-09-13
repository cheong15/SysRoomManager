<%--
	time:2012-03-16 10:53:20
	desc:edit the 常用语管理
--%>
<%@page language="java" pageEncoding="UTF-8" import="java.util.*;"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 常用语管理</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=taskApprovalItems"></script>
	<script type="text/javascript">
		function showRequest(formData, jqForm, options) { 
			return true;
		} 
	
		$(function() {
			var nodeId =   '${nodeId}';
			var isGlobal = '${isGlobal}'
			
			if(isGlobal=="1"){
				checkIsCommon(0);
				$("#isGlobal").attr("checked",'true');
			}
			else
				checkIsCommon(1);
			valid(showRequest,function(){});

		});
		
		function save(){

			var rtn=$("#taskApprovalItemsForm").valid();
   		 	if(!rtn) return;
   		 
			var url=__ctx+ "/platform/bpm/taskApprovalItems/save.ht";
   		 	var para=$('#taskApprovalItemsForm').serialize();
   		 	
   	    	$.post(url,para,showResponse);
   	    		   		 	
		}
		
		function showResponse(data){
			var obj=new com.hotent.form.ResultMessage(data);
			if(obj.isSuccess()){//成功
				$.ligerDialog.confirm('操作成功,继续操作吗?','提示信息',function(rtn){
					if(rtn){
						location.reload();
					}else{
						window.close();
					}
				});
		    }else{//失败
		    	$.ligerDialog.err('出错信息',"保存常用语设置失败",obj.getMessage());
		    }
		};
		
		// 设置是否全局
		function checkIsCommon(value)
		{
			if(value==1){
				$('#trTaskNode').show();
				$('#isGlobal').attr('value',0);
				var nodeExp = $('#tmpNodeExp').attr('value').replaceAll("{,}","\r\n");
				$('#approvalItem').val(nodeExp);
			}
			else
			{
				$('#trTaskNode').hide();
				$('#isGlobal').attr('value',1);
				var defExp = $('#tmpDefExp').attr('value').replaceAll("{,}","\r\n");
				$('#approvalItem').val(defExp);
			}
		}
		
		// 节点ID
		function changeNodeId(value){
			
			$('#nodeId').attr('value',value);
			
			var url=__ctx+ "/platform/bpm/taskApprovalItems/get.ht";
   		 	var para=$('#taskApprovalItemsForm').serialize();
   		 	
   	    	$.post(url,para,function(result){
   	    			var obj = eval('(' + result + ')');
   	    			$('#tmpNodeExp').attr('value',obj.message);
   	    			$('#approvalItem').val(obj.message);
   	    		}
   	    	);
		}
		
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">常用语设置</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" onclick="save()"><span></span>保存</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link del" href="javascript:this.close()"><span></span>关闭</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="taskApprovalItemsForm" method="post" action="save.ht">
			<div class="panel-detail">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th width="20%">是否全局: </th>
						<td><input type="checkbox" id="isGlobal" name="isGlobal" value="0" onclick="checkIsCommon(value)"/></td>
					</tr>
					<tr id="trTaskNode">
						<th width="20%">任务节点: </th>
						<td>
							
							<div id="defNodeDiv">
								<select id="defNode" name="defNode"  onchange="changeNodeId(value)">
									<option value="">-请选择-</option>
									<c:forEach items="${nodeMap}" var="node">
										<c:choose>
											<c:when test="${node.key==nodeId }">
												<option value="${node.key }" selected="selected">${node.value }</option>
											</c:when>
											<c:otherwise>
												<option value="${node.key }">${node.value }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<th width="20%">常用语: </th>
						<td>
							<textarea rows="5" cols="60" id="approvalItem" name="approvalItem" 
								style="margin-top: 5px;margin-bottom: 5px;">${nodeExp}</textarea>
						</td>
					</tr>
				</table>
				
				<input type="hidden" id="tmpDefExp" name="tmpDefExp" value="${defExp}" />
				<input type="hidden" id="tmpNodeExp" name="tmpNodeExp" value="${nodeExp}" />
				<input type="hidden" id="actDefId" name="actDefId" value="${actDefId}" />
				<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" />
				<input type="hidden" id="setId" name="setId" value="${setId}" />
			</div>
		</form>
	</div>
</div>
</body>
</html>
