<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.hotent.platform.model.system.SysParam"
    pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>用户参数自定义</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<link href="${ctx}/js/jquery/plugins/link-div-default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"	src="${ctx}/js/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/system/ConditionExpression.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/BpmNodeRule.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/plugins/jquery.linkdiv.js"></script>
<script type="text/javascript">
	var args = window.dialogArguments;
	$(function() {
 		var cmpIdsJson = jQuery.parseJSON(args.cmpIds);
		$("#defLayout").ligerLayout({
			height : '90%'
		});
		
		$("#ruleDiv").linkdiv({data:cmpIdsJson,updateContent:updateContent,rule2json:rule2json});
	});

	function changeCondition(obj) {
		var me = obj;
		var dataType = $("select[name='paramKey']", $(me).closest("div")).find("option:selected").attr("title");
		if (dataType.length > 0) {
			var $paramCondition = $("select[name='paramCondition']", $(me).closest("div"));
			$("option", $paramCondition).remove();
			$paramCondition.append("<option value='='>=</option>");
			$paramCondition.append("<option value='!='>!=</option>");
			if (dataType == "String") {
				$paramCondition.append(
						"<option value=' like '>like</option>");
			} else {
				$paramCondition.append("<option value='>'>></option>");
				$paramCondition.append("<option value='<'><</option>");
				$paramCondition.append("<option value='>='>>=</option>");
				$paramCondition.append("<option value='<='><=</option>");
			}
		}
	};

	function validateVal(obj) {
		var me = obj;
		var dataType = $("select[name='paramKey']", $(me).closest("div")).find("option:selected").attr("title");
		var $paramValue = $("input[name='paramValue']", $(me).closest("div"));
		var paramValue = $paramValue.val();
		var yes = true;
		if (paramValue=="") {
			if ($paramValue.next().html() == null
					|| $paramValue.next().html() == '')
				$paramValue.after('<font color="red">条件不能为空。</font>');
			yes = false;
		}
		if (dataType == "Integer") {
			if (isNaN(paramValue)) {
				$paramValue.addClass("error");
				if ($paramValue.next().html() == null
						|| $paramValue.next().html() == '')
					$paramValue.after('<font color="red">请输入数字。</font>');
				yes = false;
			}
		} else if (dataType == "Date") {
			var pattern = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\s(((0?[0-9])|([1-2][0-3]))\:([0-5]?[0-9])((\s)|(\:([0-5]?[0-9])))))?$/;
			if (!pattern.exec(paramValue)) {
				$paramValue.addClass("error");
				if ($paramValue.next().html() == null
						|| $paramValue.next().html() == '')
					$paramValue.after('<font color="red">请输入日期。</font>');
				yes = false;
			}
		}
		if (yes) {
			$paramValue.removeClass("error");
			if ($paramValue.next().html() != null)
				$paramValue.next().empty();
		}
		return yes;
	};

	function selectParam() {
		var json = getData();
		var jsonStr = "";
		var text = [];
		if(json.length > 0){
			jsonStr = JSON2.stringify(json);
			compileConDesc(json, text);
		}
		window.returnValue = {
			paramValue1 : jsonStr,
			paramValue2 : text.join('')
		};
		window.close();
	};
	
	//组装规则描述
	function compileConDesc(json,text){
		for(var i=0,c;c=json[i++];){
			if(c.compType){
				text.push(c.compType=='or'?' 或者 ':' 并且 ');
			}
			if(c.branch){
				var branchDesc = ['('];
				compileConDesc(c.sub,branchDesc);
				branchDesc.push(')');
				text.push(branchDesc.join(''));
			}
			else{
				text.push(c.conDesc);
			}
		}
	};
	
	function preview() {
		var userParam = JSON2.stringify(getData());
		var winArgs = "dialogWidth=650px;dialogHeight=500px;help=0;status=0;scroll=0;center=1";
		var url = __ctx + '/platform/system/sysUserParam/getByParamKey.ht';
		url=url.getNewUrl();
		var obj={flag:0,params:userParam,url:url};
		window.showModalDialog(url,obj, winArgs);
	};
	
	function initParamCondition() {
		var dataType = $("#ruleDiv select[name='paramKey']:last").find("option:selected").attr("title");
		if(dataType=="String"){
			var paramCondition = $("#ruleDiv select[name='paramCondition']:last");
			paramCondition.find("option").remove();
			paramCondition.append("<option value='='>=</option>");
			paramCondition.append("<option value='!='>!=</option>");
			paramCondition.append("<option value=' like '>like</option>");
		}
	}
	
	function addDiv(ruleType) {
		$("#ruleDiv").linkdiv("addDiv",{ruleType:ruleType});
	}
	
	function removeDiv(){
		$("#ruleDiv").linkdiv("removeDiv");	
	};

	function assembleDiv(){
		$("#ruleDiv").linkdiv("assembleDiv");
	};

	function splitDiv(){
		$("#ruleDiv").linkdiv("splitDiv");
	};

	function getData(){
		var json = $("#ruleDiv").linkdiv("getData");
		return json;
	};
</script>
</head>
<body>
<div id="defLayout">
	<div position="center" style="height:90%;overflow:auto;">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">构造条件</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
				
					<div class="group"><a onclick="addDiv(3)" class="link add"><span></span>添加</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link switchuser" onclick="assembleDiv()"><span></span>组合规则</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link switchuser" onclick="splitDiv()"><span></span>拆分规则</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del" onclick="removeDiv()"><span></span>删除</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a onclick="preview();" class="link preview"><span></span>预览</a></div>
				</div>	
			</div>
		</div>
		<div class="panel-body">
			<div class="panel-detail" id="ruleDiv" style="border:3px solid #ccc;margin:5px 0 0 0;"></div>
		</div>  
	</div>
</div>
<%@include file="/commons/include/nodeRuleTemplate.jsp" %>
<div position="bottom"  class="bottom" style='margin-top:10px'>
		&nbsp;&nbsp;<a href='#' class='button'  onclick="selectParam()" ><span class="icon ok"></span><span >确定</span></a>
		&nbsp;&nbsp;<a href='#' class='button'  onclick="window.close()"><span class="icon cancel"></span><span >取消</span></a>
</div>
</body>
</html>