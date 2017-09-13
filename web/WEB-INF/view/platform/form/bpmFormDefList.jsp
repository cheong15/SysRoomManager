<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自定义表单列表</title>
<%@include file="/commons/include/get.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<link href="${ctx}/styles/default/css/jquery.qtip.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/js/hotent/platform/form/CopyFormDefDialog.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/ImportExportXmlUtil.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/plugins/htCatCombo.js"></script>
<script type="text/javascript" src="${ctx}/js/lang/view/platform/form/zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/plugins/jquery.qtip.js" ></script>
<script type="text/javascript" src="${ctx}/js/util/easyTemplate.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.form.js"></script>

<script type="text/javascript">
	var win;
	function newFormDef(){
		var url=__ctx + '/platform/form/bpmFormDef/gatherInfo.ht?categoryId=${categoryId}';
		win= $.ligerDialog.open({title:"新建表单", url: url, height: 350,width:550 ,isResize: false });	
	}

	function closeWin(){
		if(win){
			win.hidden();
		}
	}
	function reload(){
		location.href=location.href.getNewUrl();
	}
	
	$(function(){
		$("a.link.del").unbind("click");
		$("a[formKey]").each(showResult);	
		//delFormDef();
		handlerDelSelect(showResponse);
		publish();
		handNewVersion();
	});	
	
	function showResult(){
		var formKey=$(this).attr("formKey");
		var template=$("#txtBpmDefinitionListTemplate").val();
		$(this).qtip({  
			content:{
				text:$lang.tip.loading,
				ajax:{
					url:__ctx +"/platform/bpm/bpmDefinition/getBpmDefinitionByFormKey.ht",
					type:"GET",
					data:{formKey: formKey },
					success:function(data,status){
						var html=easyTemplate(template,data).toString();
						this.set("content.text",html);
					}
				},
				title:{
					text:"表单绑定的流程定义列表"		
				}
			},
	        position: {
	        	at:'top left',
	        	target:'event',
	        	
				viewport:  $(window)
	        },
	        show:{
	        	event:"click",
		     	solo:true
	        },   			     	
	        hide: {
	        	event:'unfocus mouseleave',
	        	fixed:true
	        },  
	        style: {
	       	  classes:'ui-tooltip-light ui-tooltip-shadow'
	        } 			    
	 	});	
	}
	
	function copyFormDef(formDefId){
		CopyFormDefDialog({formDefId:formDefId,center:0});
	};
	
	function publish(){
		$.confirm("a.link.deploy",'确认发布吗？');
	}
	
	function delFormDef(){
		$.confirm("table.table-grid td a.link.del",'确定删除该自定义表单吗？');
	}
	
	function handlerDelSelect(showResponse)
	{
		//单击删除超链接的事件处理
		$("a.link.del").click(function(){	
			if($(this).hasClass('disabled')) return false;
			var self=this;
			var action=$(this).attr("action");
			var aryId = null;
			var singleAction=$(this).attr("href");
			//提交到后台服务器进行日志删除批处理的日志编号字符串
			var delId="";
			var keyName="formKey";
			if(!action){
				//action属性为空，则认为是操作删除一条记录
				action=$(self).attr("href");
				keyName="";
			}else{
				aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
				if(aryId.length == 0){
					$.ligerDialog.warn("请选择记录！");
					return false;
				}
				aryId.each(function(){
					delId+=$(this).attr(keyName) +",";
				});
				action +="?" +keyName +"=" +delId ;
			}
			
			$.ligerDialog.confirm('确认删除所选自定义表单吗？','提示信息',function(rtn) {
				if(rtn) {
					var form = $('<form method="post"></form>');
					form.attr('action',action);
					form.ajaxForm({success:showResponse});
					form.submit();
				}
			});
			return false;
		});
	}
	
	function showResponse(responseText){
		var obj=new com.hotent.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			$.ligerDialog.closeWaitting();
			$.ligerDialog.success('<p><font color="green">'+obj.getMessage()+'</font></p>','提示信息',function(){
				location.reload(true);
			});
	    }else{//失败
			$.ligerDialog.closeWaitting();
			var message = '<p><font color="red">'+obj.getMessage()+'</font></p>';
			$.ligerDialog.tipDialog('提示信息',"删除结果如下:",message,null,function(){
				$.ligerDialog.hide();
			});
	    }
	}
	
	function handNewVersion(){
		$.confirm("a.link.newVersion",'确认新建版本吗？');
	}
	
	function authorizeDialog(formKey){
		var url=__ctx + '/platform/form/bpmFormDef/rightsDialog.ht?formKey=' + formKey;
		var winArgs="dialogWidth=800px;dialogHeight=600px;help=0;status=no;center=yes;resizable=no;";
		url=url.getNewUrl();
		window.showModalDialog(url,"",winArgs);
	}
	
	
	// 导出自定义表单
	function exportXml(){	
		var formDefIds = ImportExportXml.getChkValue('pk');
		if (formDefIds ==''){
			$.ligerDialog.warn('还没有选择,请选择一项自定义表单!','提示信息');
			return ;
		}

		var url=__ctx + "/platform/form/bpmFormDef/export.ht?formDefIds="+formDefIds;
		ImportExportXml.showModalDialog({url:url});
	}

	
	//导入自定义表单
	function importXml(){
		var url=__ctx + "/platform/form/bpmFormDef/import.ht";
		ImportExportXml.showModalDialog({url:url});
	}
	
	function getFormKey(){
	var aryChk=$("input:checkbox[name='formDefId']:checked");
	if(aryChk.length==0) return "";
	var aryFormKey=[];
	aryChk.each(function(){
		aryFormKey.push($(this).attr("formKey"));
	});
	return aryFormKey.join(",");
}

	

var dialog=null;

function setCategory(){
	
	var formKeys=getFormKey();
	if(formKeys==""){
		$.ligerDialog.warn($lang.operateTip.selectOneRecord,$lang.tip.msg);
		return;
	}
	$.initCatComboBox();
	if(dialog==null){
		dialog=$.ligerDialog.open({title:$lang_form.bpmFormDef.list.setCategory,target:$("#dialogCategory"),width:400,height:250,buttons:
			[ {text : $lang.button.ok,onclick: setCategoryOk},
			{text : $lang.button.cancel,onclick: function (item, dialog) {
					dialog.hide();
				}
			}]});	
	}
	dialog.show();
}


function setCategoryOk(item, dialog){
	var categoryId=$("#categoryId").val();
	if(categoryId==""){
		$.ligerDialog.warn($lang_form.bpmFormDef.list.selectCategory,$lang.tip.msg);
		return;
	}
	var formKeys=getFormKey();
	var params={formKeys:formKeys,categoryId:categoryId};
	var url="${ctx}/platform/form/bpmFormDef/setCategory.ht";
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

//业务数据模板
function dataTemplate(formKey,tableId){
	var url=__ctx+"/platform/form/bpmDataTemplate/edit.ht?formKey="+formKey+"&tableId="+tableId;
	$.openFullWindow(url);
}
</script>
<!-- 
<style>
.ops_box{
	position:absolute;
    top:-40px;
    right:0px;
   	z-index: 5;
}
</style>
 -->

</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">自定义表单列表</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link add" onclick="newFormDef()"  href="#"><span></span>添加</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><f:a alias="delForm" css="link del" action="delByFormKey.ht"><span></span>删除</f:a></div>
				<div class="l-bar-separator"></div>
				<div class="group">
					<a onclick="exportXml()" href="#" class="link export"><span></span>导出</a>
				</div>
			   <div class="l-bar-separator"></div>
				<div class="group">
					<a onclick="importXml()" href="#" class="link import"><span></span>导入</a>
				</div>
				<div class="l-bar-separator"></div>
				<div class="group">
					<a onclick="setCategory()" href="#" class="link category"><span></span>设置分类</a>
				</div>
			</div>	
		</div>
		<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht">
				<ul class="row">
						<input type="hidden" name="categoryId" value="${param['categoryId']}" title="表单分类ID"></input>
					<li>
						<span class="label">表单标题:</span><input type="text" name="Q_subject_SL"  class="inputText" value="${param['Q_subject_SL']}"/>	
					</li>
					<li>
						<span class="label">对应表:</span><input type="text" name="Q_tableName_SL"  class="inputText" value="${param['Q_tableName_SL']}"/>
					</li>
					<li>
						<span class="label">设计类型:</span>
						<select name="Q_designType_SN" value="${param['Q_designType_SN']}">
							<option value="">全选</option>
							<option value="1" <c:if test="${param['Q_designType_SN'] == '1'}">selected</c:if>>编辑器设计</option>	
							<option value="0" <c:if test="${param['Q_designType_SN'] == '0'}">selected</c:if>>通过表生成</option>
						</select>
					</li>
					<li>
						<span class="label">是否发布:</span>
						<select name="Q_isPublished_SN" value="${param['Q_isPublished_SN']}">
							<option value="">全选</option>
							<option value="1" <c:if test="${param['Q_isPublished_SN'] == '1'}">selected</c:if>>已发布</option>	
							<option value="0" <c:if test="${param['Q_isPublished_SN'] == '0'}">selected</c:if>>未发布</option>
						</select>
					</li>
					</ul>
				</form>
		</div>
	</div>
	</div>
	<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
		    <display:table name="bpmFormDefList" id="bpmFormDefItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="formDefId"
						value="${bpmFormDefItem.formDefId}" formKey="${bpmFormDefItem.formKey}">
				</display:column>
				<display:column title="表单标题" sortable="true" sortName="subject">
					<a href="#${bpmFormDefItem.formKey}" formKey="${bpmFormDefItem.formKey}" style="text-align:left;" >${bpmFormDefItem.subject}</a>
				</display:column>
				<display:column property="tableName" title="对应表" sortable="true" sortName="tableName" style="width:120px;white-space:nowrap;"></display:column>
				<display:column property="categoryName" title="表单分类" sortable="true" sortName="categoryId" style="width:80px;"></display:column>
				<display:column  title="发布状态" sortable="true" sortName="isPublished" style="text-align:left;width:60px;">
					<c:choose>
						<c:when test="${bpmFormDefItem.isPublished==1 }">
							<span class="green">已发布</span>
						</c:when>
						<c:otherwise>
							<span class="red">未发布</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column  title="数据模版"  >
					<c:choose>
						<c:when test="${dataTemplateCounts[bpmFormDefItem.formDefId] > 0}">
							<span class="green">已添加</span>
						</c:when>
						<c:otherwise>
							<span class="red">未添加</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column title="设计类型" style="text-align:left;width:60px;">
					<c:choose>
						<c:when test="${bpmFormDefItem.designType==0 }">
							<span class="green">通过表生成</span>
						</c:when>
						<c:when test="${bpmFormDefItem.designType==1 }">
							<span class="brown">编辑器设计</span>
						</c:when>
					</c:choose>
				</display:column>
				<display:column title="版本信息" style="text-align:left;width:95px;white-space: nowrap;">
					<c:if test="${publishedCounts[bpmFormDefItem.formDefId] > 0}">
						默认<a href="get.ht?formDefId=${bpmFormDefItem.formDefId}" >版本${defaultVersions[bpmFormDefItem.formDefId].versionNo}</a>
					</c:if>
					<c:choose>
						<c:when test="${publishedCounts[bpmFormDefItem.formDefId] > 0}">
							&nbsp;<a href="versions.ht?formKey=${bpmFormDefItem.formKey}" >更多版本(${publishedCounts[bpmFormDefItem.formDefId]})</a>
						</c:when>
						<c:otherwise>
							&nbsp;共${publishedCounts[bpmFormDefItem.formDefId]}个版本
						</c:otherwise>
					</c:choose>
				</display:column>				
				<display:column title="管理" media="html"  style="width:50px;text-align:center" class="rowOps">
					<c:choose>
						<c:when test="${bpmFormDefItem.isPublished== 1}">
							<a href="newVersion.ht?formDefId=${bpmFormDefItem.formDefId}"  class="link newVersion">新建版本</a>
						</c:when>
						<c:otherwise>
						<c:choose>
							<c:when test="${bpmFormDefItem.designType==0 }">
								<a href="#" onclick="javascript:jQuery.openFullWindow('edit.ht?formDefId=${bpmFormDefItem.formDefId}');" class="link edit">编辑</a>
								<a href="publish.ht?formDefId=${bpmFormDefItem.formDefId }" class="link deploy" >发布</a>
							</c:when>
							<c:when test="${bpmFormDefItem.designType==1 }">
								<c:choose>
									<c:when test="${empty bpmFormDefItem.tableName}">
										<a href="#" onclick="javascript:jQuery.openFullWindow('designEdit.ht?formDefId=${bpmFormDefItem.formDefId}');" class="link edit">编辑</a>
									</c:when>
									<c:otherwise>
									<a href="#" onclick="javascript:jQuery.openFullWindow('edit.ht?formDefId=${bpmFormDefItem.formDefId}');" class="link edit">编辑</a>
									<a href="publish.ht?formDefId=${bpmFormDefItem.formDefId }" class="link deploy" >发布</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
						</c:otherwise>
					</c:choose>
					<f:a alias="delForm" css="link del" href="delByFormKey.ht?formKey=${bpmFormDefItem.formKey}">删除</f:a>
					<a href="get.ht?formDefId=${bpmFormDefItem.formDefId}" class="link detail">明细</a>
					
					<c:choose>
							<c:when test="${bpmFormDefItem.designType==0 }">
								<a target="_blank" href="${ctx}/platform/form/bpmFormHandler/edit.ht?formDefId=${bpmFormDefItem.formDefId}" class="link preview">预览</a>
								<a  class="link auth" href="javascript:authorizeDialog(${bpmFormDefItem.formKey})">表单权限</a>
							</c:when>
							<c:when test="${bpmFormDefItem.designType==1 }">
								<a href="#" onclick="javascript:jQuery.openFullWindow('preview.ht?formDefId=${bpmFormDefItem.formDefId}');" class="link preview">预览</a>
								<c:if test="${bpmFormDefItem.isPublished==1}">
									<a class="link auth"  href="javascript:authorizeDialog(${bpmFormDefItem.formKey})">表单权限</a>
								</c:if>
							</c:when>
					</c:choose>
					<c:if test="${bpmFormDefItem.isPublished==1}">
						<a  href="javascript:;" onclick="copyFormDef(${bpmFormDefItem.formDefId})" class="link copy">复制</a>
						<a  href="${ctx}/platform/form/bpmPrintTemplate/list.ht?formKey=${bpmFormDefItem.formKey}" class="link dataList">打印模板</a>
						<a  href="javascript:;" onclick="dataTemplate(${bpmFormDefItem.formKey},${bpmFormDefItem.tableId})" class="link preview">业务数据模板</a>
						
					</c:if>
				</display:column>
			</display:table>
			<hotent:paging tableId="bpmFormDefItem"/>
		
	</div><!-- end of panel-body -->				
 <!-- end of panel -->
 	<textarea id="txtBpmDefinitionListTemplate"  style="display: none;">
	    <div  style="height:150px;width:150px;overflow:auto">
	    	<ui>
	    		<#list data as obj>
	    		<li style="margin-top:10px;"><a href="${ctx}/platform/bpm/bpmDefinition/detail.ht?defId=\${obj.defId}">\${obj.subject}</a></li>
	    		</#list>
	    	</ui>
	  	</div>
	  </textarea>
</body>
<div id="dialogCategory" style="width: 380px;display: none;">
	<div class="panel">
			<div class="panel-body">
				<form id="frmDel">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th style="width:113px;text-align:center;">设置分类:</th>
							<td>
								<input class="catComBo" catKey="FORM_TYPE" valueField="categoryId" catValue="" name="typeName" height="150" width="235"/>
							</td>
						</tr>
					</table>
				
				</form>
			</div>
		</div>
</div>
</html>