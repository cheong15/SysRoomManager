<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<%@include file="/commons/include/form.jsp" %>
	<title>消息模版导入</title>
	<f:js pre="js/lang/view/platform/system" ></f:js>
	<script type="text/javascript">
	window.name="win";
			
	$(function(){
		valid(showResponse);
		$("#btnSave").click(function(){
			var path = $('#xmlFile').val();
			var extNaem = path.substring(path.length-3, path.length);
			if(extNaem!='xml'){
				$.ligerDialog.warn($lang_system.sysTemplate.import_.file_validate_msg,$lang.tip.msg);
			}else{
				if($("#btnSave").hasClass("disabled"))return;
					$("#btnSave").addClass("disabled");
				$.ligerDialog.waitting($lang_system.sysTemplate.import_.waitting_msg);
				$("#importForm").submit();
			}
		});
	});
	
	function showResponse(responseText){
		var obj=new com.sf.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			$.ligerDialog.closeWaitting();
			var message = obj.getMessage();
			if(message){
				message =message.replaceAll("###","\'").replaceAll("!!!","<font ").replaceAll("%%%","</font>").replaceAll("&gt;",">").replaceAll("&lt;","<");   
			}
		  	$.ligerDialog.tipDialog($lang.tip.msg,$lang_system.sysTemplate.import_.result,message,null,function(){
				window.returnValue="true";
				window.close();
			});
	    }else{//失败
			$.ligerDialog.closeWaitting();
	    	$.ligerDialog.error(obj.getMessage(),$lang.tip.msg);
	    	$("#btnSave").removeClass("disabled");
	    }
	}
	
	function valid(showResponse){
		var options={success:showResponse};
		__valid=$("#importForm").validate({
			rules: {},
			messages: {},
			submitHandler:function(form){
				$(form).ajaxSubmit(options);
		    },
		    success: function(label) {}
		});
	}
	
	</script>
</head>
<body>
	<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">消息模版导入</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSave"><span></span>导入</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link close" onclick="javasrcipt:window.close()"><span></span>关闭</a></div>
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="importForm" name="importForm" method="post" target="win" action="importXml.ht" enctype="multipart/form-data">
			<div class="row">
			 
			 <table id="tableid" class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width="22%">导入文件：</th>
					<td width="78%"><input type="file" size="40" name="xmlFile" id="xmlFile"/></td>						
				</tr>
	 			<tr>
					<th>是否删除同用途的模版：</th>
					<td ><input type="checkbox"  name="clearAll" value="true" checked="checked"></td>						
				</tr>
				<tr>
					<th>是否设置为默认：</th>
					<td ><input type="checkbox"  name="setDefault" value="true" checked="checked"></td>						
				</tr>
			</table>				
			
			</div>
	    </form>
	</div><!-- end of panel-body -->				
</body>
</html>