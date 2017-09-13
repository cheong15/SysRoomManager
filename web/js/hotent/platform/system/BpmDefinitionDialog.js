/**
 * 流程选择窗口。
 * dialogWidth：窗口宽度，默认值700
 * dialogWidth：窗口宽度，默认值400
 * callback：回调函数
 * 回调参数如下：
 * key:参数key
 * name:参数名称
 * 使用方法如下：
 * 
 * BpmDefinitionDialog({isSingle:true,callback:dlgCallBack}){
 *		//回调函数处理
 *	}});
 * @param conf
 */
function BpmDefinitionDialog(conf)
{
	conf=conf||{ };
	var options={isSingle:false,showAll:1,validStatus:1};
	conf= $.extend(options,conf);
	var url=__ctx +"/platform/bpm/bpmDefinition/dialog.ht?isSingle={0}&showAll={1}&validStatus={2}";
	url=String.format(url,conf.isSingle,conf.showAll,conf.validStatus);
	
	var winArgs="dialogWidth=860px;dialogHeight=600px;help=0;status=0;scroll=1;center=1";
	url=url.getNewUrl();
	var rtn=window.showModalDialog(url,conf,winArgs);
	if(rtn!=undefined){
		if(conf.callback){
			var defIds=rtn.defIds;
			var subjects=rtn.subjects;
			if(conf.returnDefKey){
				var defKeys = rtn.defKeys;
				conf.callback.call(this,defIds,subjects,defKeys);
			}else{
				conf.callback.call(this,defIds,subjects);
			}
		}
	}
}