<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
 // 移动行
 function move(tableId, direct,obj){
	var objTr=$("#"+tableId+">tbody.data>tr");
	if(objTr.length==0) return;

	var curObj = $(obj).parents('tr:first');
	if(direct=='up'){
		var prevObj=curObj.prev();
		if(prevObj.length>0){			
			alertConditionSn(curObj,prevObj,direct);		
		}
	}
	else{
		var nextObj=curObj.next();
		if(nextObj.length>0){
			alertConditionSn(curObj,nextObj,direct)	;
		}
	}
};




function delRows(tableId){
	var tableObj=$('#'+tableId);
	 var deletes=$('input[type=checkbox]:checked',tableObj);
	if(deletes.length==0){
		$.ligerMsg.info($lang.del.selectRecord);
		return ;
	}
	$.ligerDialog.confirm($lang.operateTip.sureDelete,$lang.tip.msg, function(rtn) {
		if (rtn) {		
			for(var i=deletes.length-1;i>=0;i--){
				var conditionId=$(deletes[i]).next('input[name="conditionId"]').val();
				if(conditionId!='' && conditionId!=undefined){
					$.post(__ctx+'/platform/bpm/bpmUserCondition/del.ht',{'id':conditionId});
				}
				$(deletes[i]).parent().parent().fadeOut(500, function() {
						$(this).remove();
				});
			}
			
		}
	});
}


function selectScript(){
	var objConditionCode=$("#txtScriptData")[0];
	ScriptDialog({callback:function(script){
		jQuery.insertText(objConditionCode,script);
	}});
}

var win;
function showScript(obj){
	$("#txtScriptData").val(obj.val());
	
	var divObj=$("#divScriptData");
	win= $.ligerDialog.open({ target:divObj , height: 350,width:500, modal :true,
		buttons: [ { text: $lang.button.ok, onclick: function (item, dialog) { 
				obj.val($("#txtScriptData").val());
				dialog.hide();
			} 
		}, 
		{ text: $lang.button.cancel, onclick: function (item, dialog) { dialog.hide(); } } ] }); 
	
}

function changeVar(obj){
	var val=$(obj).val();
	var objScript=$("#txtScriptData")[0];
	jQuery.insertText(objScript,val);
}

//显示其他节点的对话框
function showOtherNodeDlg(conf){
	var winArgs="dialogWidth=650px;dialogHeight=500px;help=0;status=0;scroll=1;center=1";
	url=__ctx + "/platform/bpm/bpmDefinition/taskNodes.ht?actDefId=${bpmDefinition.actDefId}&nodeId="+conf.nodeId;
	url=url.getNewUrl();
	var rtn=window.showModalDialog(url,"",winArgs);
	if(conf.callback){
		if(rtn!=undefined){
			conf.callback.call(this,rtn.nodeId,rtn.nodeName);
		}	
	}
}
function conditionDialog(tableId,edit)
{
	var tableObj=$('#'+tableId);
	var defId=$('input[type="hidden"][name="defId"]',tableObj).val();
	var nodeId=$('input[type="hidden"][name="nodeId"]',tableObj).val();
	var conditionId;
	if(edit){
		 conditionId=$('input[type="checkbox"]:checked:first',tableObj).next('input[name="conditionId"]').val();
	}
	
	var url;
	if(conditionId){
		url=__ctx + '/platform/bpm/bpmDefinition/conditionEdit.ht?defId='+defId+'&nodeId='+nodeId+'&conditionId='+conditionId;
	}	
	else if(edit && !conditionId){
		$.ligerDialog.warn("请先选择要修改的规则条件！",$lang.tip.msg);
		return;
	}else{
		url=__ctx + '/platform/bpm/bpmDefinition/conditionEdit.ht?defId='+defId+'&nodeId='+nodeId;
	}
	
	var dialogWidth=900;
	var dialogHeight=640;
	var conf={};
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1},conf);

	var winArgs="dialogWidth:"+conf.dialogWidth+"px;dialogHeight:"+conf.dialogHeight
		+"px;help:" + conf.help +";status:" + conf.status +";scroll:" + conf.scroll +";center:" +conf.center;
	
	
	url=url.getNewUrl();
	var rtn=window.showModalDialog(url,'',winArgs);
	if(rtn){
		//document.getElementById("btnReflesh").click();
		location.href=location.href.getNewUrl();
	}
}

function changeCheck(obj){
	var tableObj=$(obj).closest('table');
	var ck=$(obj).attr('checked');
	$('input[type="checkbox"]:checked',tableObj).attr('checked',false);
	if(ck=="checked"){
		$(obj).attr('checked',true);
	}
	else{
		$(obj).attr('checked',false);
	}
	
}
$(function(){
	$("div.group > a.link.update").unbind('click');
	$("select[name='variableIds']").live("change",function(){
		var _this=this;
		var assignType=$(_this).parent().parent().find(':input[name="assignType"]').val();
		//兼容老版本
		if(assignType>=16&&assignType<=19){
			$(_this).parent().find('input[name="cmpIds"]').val(_this.value);
			$(_this).parent().find('textarea[name="cmpNames"]').val($("option:selected",_this).text());
			$(_this).parent().find(':input[name="variableNames"]').val($("option:selected",_this).text());
		}else{
			$(_this).parent().find(':input[name="variableNames"]').val($("option:selected",_this).text());
		}
	});
	$("a.moveup,a.movedown").unbind("click");
	$("a.moveup,a.movedown").click(function(){
		sortCondition(this);
	});
	initConditionShow();
});
//初始化 规则的描述内容
function initConditionShow(){
	$("div[name='conditionShow']").each(function(){
		var me = $(this),
			textarea = me.find("textarea"),
			value = textarea.val().trim();
		if(value=="")return true;
		value = eval("("+value+")");
		var text = [];
		compileConDesc(value,text)
		textarea.before(text.join(' '));
	});
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
			if(c.judgeCon2){
				text.push('(');
				text.push(c.conDesc);
				text.push(')');
			}
			else{
				text.push(c.conDesc);
			}
		}
	}
};

function updSn(tbodyObj){
	var conditionObjs=$("input[name='conditionId']",tbodyObj);
	if(conditionObjs.length<2)return;
	var aryCondition=[];
	conditionObjs.each(function(){
		aryCondition.push(this.value);
	});
	var conditionIds=aryCondition.join(",");
	$.post('${ctx}/platform/bpm/bpmUserCondition/updateSn.ht',{"conditionIds":conditionIds},function(data){
		var resultData=eval('('+data+')');
		if(resultData.result==1){
			
		}else{
			$.ligerDialog.warn(resultData.message,$lang.tip.msg);
		}
	});
}


function sortCondition(btnObj){
	var obj=$(btnObj);
	var direct=obj.hasClass("moveup")?1:0;
	var curObj = obj.closest('tr');
	var tbodyObj = obj.closest('tbody');
	if(direct==1){
		var prevObj=curObj.prev();
		if(prevObj!=null){
			curObj.insertBefore(prevObj);	
		}
	}
	else{
		var nextObj=curObj.next();
		if(nextObj!=null){
			curObj.insertAfter(nextObj);
		}
	}
	updSn(tbodyObj);
	
}
</script>