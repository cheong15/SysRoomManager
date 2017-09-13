/**
 * 下拉选项模版。
 */
var optiontemplate = '<option value="#value">#text</option>';

$(function(){
	getDialogs();
	$("select[name='methodName']").change(methodChange);	
	$('select[name="paraCt"]').live('change',function(){
		var parentDiv = $(this).closest('div');
		if($(this).val()==0){
			$('#settingSpan',parentDiv).show();
		}
		else{
			$('#settingSpan',parentDiv).hide();
		}
	});
});

//获取自定义对话框
function getDialogs(){
	var url = __ctx + '/platform/form/bpmFormDialog/getAllDialogs.ht';
	$.ajax({
	    type:"get",
	    async:false,
	    url:url,
	    success:function(data){
			if (data) {
				for(var i=0,c;c=data[i++];){
					var opt = $('<option value="'+c.alias+'">'+c.name+'</option>');
					opt.attr("fields",c.resultfield);
					$("select[name='dialog-type']",$("#settingSpan")).append(opt);
				}
			}
	    }
	});
};		
//选择不同的对话框
function dialogChange(obj){
	var dia=$(obj).find("option:selected");
	var v = dia.attr("fields");
	if(v){
		var paramSelector = $(obj).siblings("#dialog-param");
		var opt = paramSelector.find("option:first-child");
		paramSelector.text('');
		//添加   请选择…… option
		paramSelector.append(opt);
		var fields = $.parseJSON(v);
		for(var i=0,f;f=fields[i++];){
			opt = $('<option value="'+f.field+'">'+f.comment+'</option>');
			paramSelector.append(opt);
		}
	}
}

//选择的方法事件
function methodChange(){
	var option = $(this).find("option:selected");

	if(!option)return;
	var	methodInfo = option.data('methodInfo');
	if(!methodInfo)return;
	$("input[name='returnType']").val(methodInfo.returnType);		
	var param = constructParamTable(methodInfo);
	$("#paraInfo").empty().append(param);
};
//获取该类的方法
function getMethods(url){
	$.post(url,'',function(r){
		var data = eval("("+r+")");
		if(data.result){
			var methods = data.methods,
				methodSelect = $("select[name='methodName']").empty(),
				methodName = $("#methodName").val();

			for(var i=0,c;c=methods[i++];){
				var newOpt = $('<option></option>').val(c.methodName).text(c.methodName);
				if(c.methodName == methodName){
					var paraData = $("textarea[name='argument']").val();
					if(!paraData)continue;
					paraData = eval("("+paraData+")");
					c.para = paraData;
					$(newOpt).attr("selected",true);
				}
				$(newOpt).data('methodInfo',c);
				methodSelect.append(newOpt);
			}
			methodSelect.trigger("change");
		}
		else{
			$.ligerDialog.error(data.message,$lang.tip.errorMsg);
		}
	});
}

//根据表单ID保存并提交
function save(formId){
	var isReturnFalse = false;
	var settingSpan = $("[id='settingSpan']");
	var setting;
	for(var i=0;i<settingSpan.length;i++){
		setting = settingSpan[i];
		if($(setting).css('display')=='none') continue ;
		var dialog = $(setting).find('#dialog-type');
		var param = $(setting).find('#dialog-param');
		var paraName = $(setting).closest('tr').find('[name="paraName"]').text();
		if(!dialog.val()){
			$.ligerDialog.warn("请选择参数【"+paraName+"】所要绑定的对话框！",$lang.tip.warn);
			isReturnFalse = true;
			break;
		}else if(!param.val()){
			$.ligerDialog.warn("请选择参数【"+paraName+"】对应的对话框返回值！",$lang.tip.warn);
			isReturnFalse = true;
			break;
		}
	};
	if(isReturnFalse) return false;
	var json = [];
	$("#paraInfo").find("tr").each(function(){
		var me = $(this),
			paraNameSpan = $("span[name='paraName']",me);

		if(!paraNameSpan||paraNameSpan.length==0)return true;
		var paraTypeSpan = $("span[name='paraType']",me),
			paraDescInput = $("input[name='paraDesc']",me),
			paraCtlTypeSelect = $("[name='paraCt']",me),
			job = {};

		job.paraName = paraNameSpan.text();
		job.paraType = paraTypeSpan.text();
		job.paraDesc = paraDescInput.val();
		job.paraCt = paraCtlTypeSelect.val();
		if($('#settingSpan',me).css('display')!='none'){
			var dialog = $("select[name='dialog-type']",me),
				target = $("select[name='dialog-param']",me);
			job.dialog = dialog.val();
			job.target = target.val();
		}
		json.push(job);
	});
	json = JSON2.stringify(json);
	$("textarea[name='argument']").val(json);
	$("#"+formId).submit();
};
function showResponse(r){
	var data = eval("("+r+")");
	if(data.result){
		$.ligerDialog.success(data.message,$lang.tip.success,function(){
			window.location.href = 'list.ht';
		});
	}
	else{
		$.ligerDialog.warn(data.message,$lang.tip.errorMsg);
	}
}

/**
* 创建参数表格
*/
function constructParamTable(params){
	var table = $("#para-txt table").clone();
	var tbody = $("tbody",table).empty();
	for(var i=0;i<params.para.length;i++){
		var p = params.para[i];
		var tr = constructParamTr(p);
		tbody.append(tr);
	}
	return table;
};

/**
* 创建参数表格行
*/
function constructParamTr(p){
	var tr = $("#para-txt table tbody tr").clone();
	$("[name='paraName']",tr).text(p.paraName);
	$("[name='paraType']",tr).text(p.paraType);
	$("[name='paraDesc']",tr).val(p.paraDesc);
	$("[name='paraCt']",tr).val(p.paraCt);
	if(p.dialog){
		var settingSpan = $('#settingSpan',tr);
		settingSpan.show();
		$("#dialog-type",settingSpan).find("option[value='"+p.dialog+"']").attr("selected","selected");
		dialogChange($("#dialog-type",settingSpan));
		$("#dialog-param",settingSpan).find("option[value='"+p.target+"']").attr("selected","selected");
	}
	return tr;		
};