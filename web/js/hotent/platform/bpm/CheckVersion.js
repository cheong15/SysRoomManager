//检查版本升级
function checkVersion(conf){
	var u = __ctx +'/platform/bpm/bpmDefinition/checkVersion.ht';
	$.post(u,{defId:conf.defId},function(res){
		var result = eval("("+res+")");		
		if(result.success){
			var url = "";
			if(	conf.type)
				url = __ctx +'/platform/bpm/task/startFlowForm.ht?draftId='+conf.draftId+'&defId='+conf.defId;
	 		else
	 			url = __ctx +'/platform/bpm/task/startFlowForm.ht?defId='+conf.defId+'&copyKey='+conf.businessKey;

			if(result.isMain == 1){
				jQuery.openFullWindow(url);	
			}else{
				var msg =  conf.type?$lang_bpm.checkVersion.runMsg:$lang_bpm.checkVersion.copyMsg;
				$.ligerDialog.warn(msg,$lang.tip.msg)
			}
		}else{
			$.ligerDialog.error(result.msg,$lang.tip.msg);
		}
	});
}