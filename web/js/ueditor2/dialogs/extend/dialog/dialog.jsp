<%@page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/commons/include/form.jsp" %>
<link rel="stylesheet" type="text/css" href="../input.css">
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/js/ueditor2/dialogs/internal.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/plugins/jquery.dragdiv.js"></script>
<link rel="stylesheet" href="${ctx}/js/jquery/plugins/jquery.dragdiv.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/js/tree/zTreeStyle.cssyle.css" type="text/css" />
<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/util/easyTemplate.js"></script>
<script type="text/javascript" src="${ctx}/js/util/json2.js"></script>
<style type="text/css">
.field-ul {
	width: 95%;
	height: 95%;
	margin: 0; 
	padding: 0;
	overflow-y: auto;
	overflow-x: hidden;
}
.fields-div {
	float: left;
	border: 1px solid #828790;
	width: 160px;
	height: 260px;
	overflow: auto;
}
.domBtnDiv {
	display: block;
	margin-left:5px;
	float:left;
	width:340px;
	height:260px;
	background-color: powderblue;
	overflow-y: auto;
	overflow-x: hidden;
}
#fieldContainer{
	height:70px;
	overflow-y:auto;
	overflow-x:hidden;
}
#fieldTable{
	margin:0;
}
</style>
</head>
<body>
	<div id="inputPanel">
		<fieldset class="base">
			<legend><var id="lang_dialog_setting"></var></legend>
			<table>
				<tr>
					<th><var id="lang_choose_dialog"></var></th>
					<td>
					<select id="dialog-type" onchange="dialogChange()">
						<option value="0"></option>
					</select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">
						<div id="fieldContainer">
						<table id="fieldTable"></table>
						</div>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="base">
			<legend><var id="lang_return_setting"></var></legend>
				<div class="fields-div">
					<ul id="fields-tree" class="ztree field-ul"></ul>
				</div>
				<div class="domBtnDiv">
				</div>
		</fieldset>
	</div>
	
	<textarea id="templateTr" style="display:none;">
	<#list data as a>
		<#if (!a.items)>
		<tr class="fieldTr">
			<th>
			<#if (a_index == 0)>主表参数</#if>
			</th>
			<td>
				<input type="checkbox" style="width:10px;" class="fieldCheck" />&nbsp;
				<!-- 主表字段 -->
				<input type="text" class="mainField" style="width:60px" value="\${a.name}" readonly id="\${a.id}"/>&nbsp;
				<select class="fieldSelect" style="width:95px;display:none;">
					<option></option>
				</select>
			</td>
		</tr>
		<#else>
			<#list a.items as sub>
			<tr class="fieldTr">
				<th><#if (sub_index == 0)>子表参数</#if></th>
				<td>
					<input type="checkbox" style="width:10px;" class="fieldCheck" />&nbsp;
					<input type="text" class="subField" style="width:60px" value="\${sub.name}" readonly id="\${sub.id}"/>&nbsp;
					<select class="fieldSelect" style="width:95px;display:none;">
						<option></option>
					</select>
				</td>
			</tr>
			</#list>
		</#if>
	</#list>
	</textarea>
	<textarea id="templateOption" style="display:none;">
	<#list data as o>
		<option value="\${o.field}">\${o.field}</option>
	</#list>
	</textarea>
	<script type="text/javascript">
		var setting = {
				edit: {
					enable: true,
					showRemoveBtn: false,
					showRenameBtn: false,	
					drag:{}
				},				
				data: {
					keep: {
						parent: true,
						leaf: true
					},
					simpleData: {
						enable: true
					}
				},				
				view: {
					selectedMulti: false
				}
			},
			dragDiv;

		$(function() {
			$(".button-td").bind("mouseenter mouseleave", function() {
				$(this).toggleClass("button-td-hover");
			});
			getDialogs();
			
			$('.fieldCheck','.fieldTr').live("click",function(){
				$(this).siblings('.fieldSelect').each(function(){
					if($(this).css('display')!='none'){
						$(this).hide() ;
					}else{
						$(this).show() ;
					}
				});
				
			});
			
		});		
		//编辑时绑定数据
		function bindData(dialogStr) {
			var dialog = eval("("+dialogStr+")" ),field;
			if(!dialog)return;
			$("#dialog-type").find("option[value='"+dialog.name+"']").each(function(){
					$(this).attr("selected","selected");
					dialogChange();
				}
			);
			while(field=dialog.fields.pop()){
				var src=field.src,
					targets=field.target.split(','),target;
				while(target=targets.pop()){
					var item = $("span.item-span[itemId='"+target+"']").toggleClass("item-span item-span-Disabled");
					if(item.length>0){
						var node = {id:target, name: item.text()};
						addNode(src,node);
					}
				}
			}
			var dialogQueryArr = dialog.query;
			for(var i=0;i<dialogQueryArr.length;i++){
				var isMain = dialogQueryArr[i].isMain ;
				$(":text#"+dialogQueryArr[i].id,".fieldTr").each(function(){
					//字段isMain和属性class==mainfield同时为true或者false时
					if((isMain=="true" ^ $(this).attr("class")=="mainField")==0){
						$(this).siblings(".fieldCheck").click() ;
						$(this).siblings(".fieldSelect").find("option[value='"+dialogQueryArr[i].name+"']").each(function(){
							$(this).attr("selected","selected");
						});
					}
				});
			}
		};
		//添加树节点
		function addNode(id,node){
			var zTree = $.fn.zTree.getZTreeObj('fields-tree');
			if(!zTree)return;
			var parentNode = zTree.getNodeByParam("id",id,null);
			if(parentNode) zTree.addNodes(parentNode,node);
		};
		
		//获取自定义对话框
		function getDialogs(){
			var url = __ctx + '/platform/form/bpmFormDialog/getAllDialogs.ht';
			$.get(url,function(data){
				if (data) {
					for(var i=0,c;c=data[i++];){
						var opt = $('<option value="'+c.alias+'" fields="'+c.returnFields+'" conditionfield="'+c.conditionfield
								+'" istable="'+c.istable+'" objname="'+c.objname+'" dsname="'+c.dsalias+'" >'+c.name+'</option>');
						opt.data("fields",c.resultfield);
						opt.data("conditionfield",c.conditionfield);
						$("#dialog-type").append(opt);
						//$("#dialog-type").append('<option value="'+c.alias+'" fields="'+c.returnFields+'">'+c.name+'</option>');
					}
					getFileds(editor.tableId);
				}
			});
		};		
		//选择不同的对话框
		function dialogChange(){
			//var v=$("#dialog-type").find("option:selected").attr("fields");
			var dia=$("#dialog-type").find("option:selected");
			var v = dia.data("fields");
			var c = dia.data("conditionfield");

			if(v){
				var nodes=[];
				var fields = $.parseJSON(v);
				for(var i=0;i<fields.length;i++){
					var f=fields[i];
					nodes.push({id:f.field,pid:0,name:f.comment,isParent: true, open:true});
				}

				$("span.item-span-Disabled").each(function(){
						$(this).toggleClass("item-span-Disabled");
						$(this).toggleClass("item-span");
					}						
				);
				var tree = $.fn.zTree.init($("#fields-tree"), setting, nodes);
				//设置拖拽 树对象
				if(dragDiv)dragDiv.dragdiv('bind','fields-tree');
			}
			if(c){
				var conditionfield = $.parseJSON(c);
				//生成“绑定参数”下拉框选项
				var template = $('textarea#templateOption').val() ;
				var templateOptionHtml = easyTemplate(template,conditionfield).toString();
				$('#inputPanel table').find('.fieldSelect').html(templateOptionHtml);
			}
			
		}

		dialog.onok = function() {
			var name=$("#dialog-type").val();
			if(!name){
				$(editor.curInput).removeAttr("dialog");
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("fields-tree"),
			nodes=zTree.getNodes(),fields=[];
			
			for(var i=0,c;c=nodes[i++];){
				if(!c.children)continue;
				var target=[],child=null;				
				while(child=c.children.pop()){
					target.push(child.id);
				}
				var sub="{src:'"+c.id+"',target:'"+target.join(',')+"'}";
				fields.push(sub);
			}
			var queryArr = [] ;
			$(':checkbox:checked','table .fieldTr').each(function(){
				var queryObj = {} ;
				queryObj.id = $(this).siblings(':text').attr('id') ;
				queryObj.name = $(this).siblings('select').val() ;
				if($(this).siblings(':text').attr('class')=='mainField'){
					queryObj.isMain = 'true' ;
				}else{
					queryObj.isMain = 'false' ;
				}
				queryArr.push(queryObj);
			});
			var queryString = JSON2.stringify(queryArr).replaceAll("\"","'") ;
			var json="{name:'"+name+"',fields:["+fields.join(',')+"],query:"+queryString+"}";
			editor.curInput.setAttribute("dialog",json);
			editor.curInput = null;
		};
		//初始化字段面板
		function initFieldsDiv(data){
			var mainTable = data.table, data = {};
			data.name = mainTable.tableDesc + '('+editor.getLang("customdialog.main")+')';
			data.id = mainTable.tableName;
			data.desc = mainTable.tableId;

			var items = [];
			for ( var i = 0, c; c = mainTable.fieldList[i++];) {
				if(c.isHidden!=0) continue ;
				items.push({
					name : c.fieldDesc,
					id : c.fieldName
				});
			}

			for ( var i = 0, c; c = mainTable.subTableList[i++];) {
				var sub = {};
				sub.name = c.tableDesc + '('+editor.getLang("customdialog.sub")+')';
				sub.id = c.tableName;
				sub.desc = c.tableId;

				var subItems = [];
				for ( var y = 0, t; t = c.fieldList[y++];) {
					subItems.push({
						name : t.fieldDesc,
						id : t.fieldName
					});
				}
				sub.items = subItems;
				items.push(sub);
			}
			//生成“绑定参数”行
			var template = $('textarea#templateTr').val() ;
			var templateTrHtml = easyTemplate(template,items).toString();
			$('#fieldTable').append(templateTrHtml);
			
			var parentTableClass = $(editor.curInput).closest('div[type="subtable"]') ;
			if(!parentTableClass || parentTableClass.length<=0){
				//若为空，表示主表，则隐藏子表字段
				$('.subField').each(function(){
					$(this).closest('.fieldTr').hide() ;
				})
			}
			
			data.items = items;
			//初始化字段面板
			dragDiv = $(".domBtnDiv").dragdiv('init',{data : data});
			var dialogStr = $(editor.curInput).get(0).getAttribute("dialog");
			if (dialogStr) {
				bindData(dialogStr);
			}
		};

		//加载字段面板
		function getFileds(tableId) {
			if(tableId){
				var url = __ctx
						+ '/platform/form/bpmFormTable/getTableById.ht?tableId='
						+ tableId;
				$.post(url, function(data) {
					initFieldsDiv(data);
				});
			}
			else{	//编辑器设计表单时获取字段并验证字段
				var html = editor.getContent();		
				var params={};
				params.html=html;
				params.formDefId=editor.formDefId;
				
				
				$.post(__ctx + '/platform/form/bpmFormDef/validDesign.ht', params, function(data){
					if(data.valid){
						initFieldsDiv(data);
					}
					else{
						alert(data.errorMsg);
					}
				});
			}
		};
	</script>
</body>
</html>