<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.hotent.platform.model.bpm.BpmDefRights"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程定义扩展管理</title>
<%@include file="/commons/include/get.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/ImportExportXmlUtil.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/tabOperator.js" ></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/FlowRightDialog.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/FlowUtil.js" ></script>
<script type="text/javascript" src="${ctx}/js/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/plugins/htCatCombo.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.js"></script>


<script type="text/javascript">
	  
	// 导出流程定义
	function exportXml(){	
		var bpmDefIds = ImportExportXml.getChkValue('pk');
		if (bpmDefIds ==''){
			$.ligerDialog.warn('还没有选择,请选择一项流程定义!','提示信息');
			return ;
		}

		var url=__ctx + "/platform/bpm/bpmDefinition/export.ht?bpmDefIds="+bpmDefIds;
		ImportExportXml.showModalDialog({url:url,height:400});
	}
	
	//导入流程定义
	function importXml(){
		var url=__ctx + "/platform/bpm/bpmDefinition/import.ht";
		ImportExportXml.showModalDialog({url:url});
	}
	
	function cleanData(defId){
		$.ligerDialog.confirm("确定清除数据?",function(btn){
			if(!btn) return;
			$.post('cleanData.ht',{defId:defId},function(data){
	 			var obj=new com.hotent.form.ResultMessage(data);
	 			if(obj.isSuccess()){
	 				$.ligerDialog.success(obj.getMessage(),'清除数据成功');
	 			}else{
	 				$.ligerDialog.error('出错信息',"清除数据失败",obj.getMessage());
	 			}
			});
		});
	}
	
	
	var dialog=null;
	//重设分类
	function setCategory(){
		$('.catComBo').each(function() {
			$(this).htCatCombo();
		});
		
		var defKeys=getDefKey();
		if(defKeys==""){
			$.ligerDialog.warn($lang.operateTip.selectOneRecord,$lang.tip.msg);
			return;
		}
		if(dialog==null){
			dialog=$.ligerDialog.open({title:"设置分类",target:$("#dialogCategory"),width:400,height:250,buttons:
				[ {text : $lang.button.ok,onclick: setCategoryOk},
				{text : $lang.button.cancel,onclick: function (item, dialog) {
						dialog.hide();
					}
				}]
			});	
		}
		dialog.show();
	}
	
	function setCategoryOk(item, dialog){
		var typeId=$("#typeId").val();
		if(typeId==""){
			$.ligerDialog.warn($lang_form.bpmFormDef.list.selectCategory,$lang.tip.msg);
			return;
		}
		var defKeys=getDefKey();
		
		var params={defKeys:defKeys,typeId:typeId};
		var url="${ctx}/platform/bpm/bpmDefinition/setCategory.ht";
		$.post(url,params,function(responseText){
			var obj=new com.hotent.form.ResultMessage(responseText);
			if(obj.isSuccess()){
				$.ligerDialog.success($lang.save.success,$lang.tip.msg,function(){
					dialog.hide();
					var url=location.href.getNewUrl();
					location.href=url;
				});
			}
			else{
				$.ligerDialog.err($lang.tip.msg,$lang.save.failure,obj.getMessage());
			}
		});
	}
	function getDefKey(){
		var aryChk=$("input:checkbox[name='defId']:checked");
		if(aryChk.length==0) return "";
		var aryDefKey=[];
		aryChk.each(function(){
			aryDefKey.push($(this).attr("defKey"));
		});
		return aryDefKey.join(",");
	}
	 function getDefId(){
		 var aryChk=$("input:checkbox[name='defId']:checked");
			if(aryChk.length==0) return "";
			var aryDefId=[];
			aryChk.each(function(){
				aryDefId.push($(this).val());
			});
			return aryDefId.join(",");
	 }
	
	
	function abatchGrant(){
		var defKeys=getDefKey();
		var defIds=getDefId();
		if(defKeys==""||defIds==""){
			$.ligerDialog.warn($lang.operateTip.selectOneRecord,$lang.tip.msg);
			return;
		}
		FlowRightDialog(defIds,0,defKeys);
	}
	
</script>
</head>
<body>      
	<div class="panel">
	<div class="panel-top">	
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
					
					<a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del"  action="del.ht"><span></span>删除</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link flowDesign" onclick="window.open('design.ht')"><span></span>在线流程设计</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link export"  href="#" onclick="exportXml()"><span></span>导出</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link import"  href="#" onclick="importXml()"><span></span>导入</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link reload"  href="#" onclick="window.location.reload()"><span></span>刷新</a></div>	
					<div class="l-bar-separator"></div>
					<div class="group"><a onclick="setCategory()" href="#" class="link category"><span></span>设置分类</a></div>	
					<div class="l-bar-separator"></div>
					<div class="group"><a onclick="abatchGrant()" href="#" class="link grant"><span></span>批量授权</a></div>									
				</div>	
			</div>
			<div class="panel-search">
			       
				<form id="searchForm" method="post" action="list.ht">	    
					<ul class="row">
						<input type="hidden" name="typeId" value="${param['typeId']}" title="流程分类ID"></input>
						<li><span class="label">标题:</span><input type="text" name="Q_subject_SL"  class="inputText" value="${param['Q_subject_SL']}"/></li>
						<!-- 
						<span class="label">流程分类:</span><input type="text" name="Q_typeName_S" class="inputText" value="${param['Q_typeName_S']}"/>
						 -->
						<li><span class="label">流程定义Key:</span><input type="text" name="Q_defKey_SL"  class="inputText" value="${param['Q_defKey_SL']}"/></li>
						<li><span class="label">描述:</span><input type="text" name="Q_descp_SL" class="inputText" maxlength="125" value="${param['Q_descp_SL']}"/></li>
						<div class="row_date">
						<li><span class="label">创建时间:</span><input type="text" name="Q_createtime_DL"  class="inputText date" value="${param['Q_createtime_DL']}"/></li>
						<li><span class="label">至</span><input name="Q_endcreatetime_DG" class="inputText date" value="${param['Q_endcreatetime_DG']}"/></li>
						</div>
						<li><span class="label">状态:</span><select name="Q_status_L" class="select" value="${param['Q_status_L']}">
							<option value="">所有</option>
							<option value="1" <c:if test="${param['Q_status_L'] == '1'}">selected</c:if>>已发布</option>
							<option value="0" <c:if test="${param['Q_status_L'] == '0'}">selected</c:if>>未发布</option>
							<option value="4" <c:if test="${param['Q_status_L'] == '4'}">selected</c:if>>测试</option>
							<option value="2" <c:if test="${param['Q_status_L'] == '2'}">selected</c:if>>禁用</option>
							<option value="3" <c:if test="${param['Q_status_L'] == '3'}">selected</c:if>>禁用实例</option>
						</select>
						</li>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
		    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table name="bpmDefinitionList" id="bpmDefinitionItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
						  	<input type="checkbox" class="pk" name="defId" 
						  	value="${bpmDefinitionItem.defId}" defKey="${bpmDefinitionItem.defKey}">
					</display:column>
					<display:column property="subject" title="标题" sortable="true" sortName="subject" ></display:column>
					
					<display:column title="分类" sortable="true" sortName="typeId">
						<c:out value="${bpmDefinitionItem.typeName}"></c:out>
					</display:column>
					<display:column property="versionNo" title="版本号" sortable="true" sortName="versionNo" style="width:25px"></display:column>
					<display:column title="发布状态" sortable="true" sortName="status" style="width:45px;">
						<c:choose>
							<c:when test="${bpmDefinitionItem.status eq 0}"><span class="red">未发布</span></c:when>
							<c:when test="${bpmDefinitionItem.status eq 1}"><span class="green">已发布</span></c:when>
							<c:when test="${bpmDefinitionItem.status eq 2}"><span class="red">禁用</span></c:when>
							<c:when test="${bpmDefinitionItem.status eq 3}"><span class="red">禁用(实例)</span></c:when>
							<c:when test="${bpmDefinitionItem.status eq 4}"><span class="red">测试</span></c:when>
							<c:otherwise><span class="red">未选择</span></c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="创建时间" sortable="true" sortName="createtime"  style="width:70px;">
						<fmt:formatDate value="${bpmDefinitionItem.createtime}" pattern="yyyy-MM-dd"/>
					</display:column>
					
					<display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
						<f:a alias="delProcess" href="del.ht?defId=${bpmDefinitionItem.defId}" css="link del" >删除</f:a>
						<f:a alias="flex" href="design.ht?defId=${bpmDefinitionItem.defId}" target="_blank" css="link flowDesign" >设计</f:a>
						<c:if test="${bpmDefinitionItem.status!=0}">
							<f:a alias="setBpm" href="detail.ht?defId=${bpmDefinitionItem.defId}" css="link setting" >设置</f:a>
						</c:if>
						<c:if test="${bpmDefinitionItem.status==1||bpmDefinitionItem.status==4}">
							<f:a alias="startProcess" href="#" onclick="FlowUtil.startFlow(${bpmDefinitionItem.defId},'${bpmDefinitionItem.actDefId}')" css="link run" >启动</f:a>
						</c:if>
						<f:a alias="grantProcess" href="javascript:FlowRightDialog(${bpmDefinitionItem.defId},0,'${bpmDefinitionItem.defKey}')" css="link grant" >授权</f:a>
						<c:if test="${bpmDefinitionItem.status==0}">
							<f:a alias="publishProcess" href="deploy.ht?defId=${bpmDefinitionItem.defId}" css="link deploy" >发布</f:a>
						</c:if>
						<a  href="javascript:cleanData('${bpmDefinitionItem.defId}')" class="link clean" >清除数据</a>
					</display:column>
				</display:table>
				<hotent:paging tableId="bpmDefinitionItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
	<div id="dialogCategory" style="width: 380px;display: none;">
	<div class="panel">
			<div class="panel-body">
				<form id="frmDel">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th style="width:150px;">设置分类:</th>
							<td>
								<input class="catComBo" catKey="FLOW_TYPE" valueField="typeId" catValue="" name="typeName" height="150" width="150"/>
							</td>
						</tr>
					</table>
				
				</form>
			</div>
		</div>
</div>
</body>
</html>


