(function($){
	
	// 操作附件访问路径
	var fileUrl = __ctx+"/platform/system/sysFile/";
	
	// 初始化控件
	$.fn.attach = function(){
		var input = $(this);
		input.css("display","none");
		var td = input.parent();
		var ul=$('<ul></ul>');
		ul.attr('id',input.attr('id')+"Show");
		td.append(ul);
		var link = $('<a href="#"></a>');
		link.html("添加报表");
		link.bind('click',function(){
			var type = input.attr("attachType");
			type = typeof type!='undefined'?type:1;
			if(type==1){
				attachUpload1($(this));
			}else if(type==2){
				attachUpload2($(this));
			}
		});
		td.append(link);
		var attachs = getAttachs(input);
		initAttach(input,attachs);
		setJsonStr(input,attachs);
	};
	
	// 打开上传控件
	function attachUpload1(tdobj){
		var curobj = tdobj.parent().find('input:first');
		var isSingle=0;
		var url=__ctx + "/platform/system/sysFile/dialog.ht?isSingle="+isSingle;
		var dialogWidth=700;
		var dialogHeight=500;
		var conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},{});
		var winArgs="dialogWidth:"+dialogWidth+"px;dialogHeight:"+dialogHeight
			+"px;help:" + conf.help +";status:" + conf.status +";scroll:" + conf.scroll +";center:" +conf.center;
		if (!conf.isSingle)conf.isSingle = false;
		url=url.getNewUrl();
		var p = {};
		p.callback=conf.callback;
		var atts=window.showModalDialog(url,"",winArgs);
		if(atts!=undefined){
			var attachs = getAttachs(curobj);
			for(var idx=0;idx<atts.length;idx++){
				attachs.push(atts[idx]);
			}
			initAttach(curobj,atts);
			setJsonStr(curobj,attachs);
		}
	}
	
	// 打开上传控件
	function attachUpload2(tdobj){
		var curobj = tdobj.parent().find('input:first');
		var url=__ctx+"/platform/system/report/upload.ht?reportPath="+$('#reportPath').val() ;
	 	var winArgs="dialogWidth:500px;dialogHeight:300px";
	 	url=url.getNewUrl();
	 	var param = {};
	 	param.atthMaxSize = curobj.atthMaxSize;
	 	param.sizeUnit = curobj.sizeUnit;
	 	var atts=window.showModalDialog(url,param,winArgs);
		if(atts!=undefined){
			var attachs = getAttachs(curobj);
			for(var idx=0;idx<atts.length;idx++){
				attachs.push(atts[idx]);
			}
			
			initAttach(curobj,atts);
			setJsonStr(curobj,attachs);
		}
	}
	
	// 初始化附件内容
	function initAttach(obj,attachs)
	{
		for(var idx=0;idx<attachs.length;idx++){
			var attach = attachs[idx];
			var li = $("<li style='float:left;'></li>");
			li.attr('id',attach.id);
			var attachDiv=$("<div></div>").addClass('attach-div');
			var attachAttach=$("<span></span>").addClass('attach-sign');
			var attachLabel = $("<span></span>").addClass('attach-label');
			var downlink = $("<a></a>");
			var delurl = fileUrl+"download.ht?fileId="+attach.id;
			downlink.attr("href",delurl);
			//wzh 2013-07-01
			downlink.attr("fileId",attach.id);
			
			downlink.html(attach.name);
			attachLabel.append(downlink);
			var attachBtn = $("<span></span>").addClass('attach-del').append("&nbsp;");
			
			attachDiv.append(attachAttach);
			attachDiv.append(attachLabel);
			attachDiv.append(attachBtn);

			li.append(attachDiv);
			$("#"+obj.attr("id")+"Show").append(li);
		}
		attachQtip();
		attachDel();
	}
	
	// 得到附件json
	function getAttachs(obj){
		var str = "{\"attachs\":[]}";
		if(obj.val()!=''){
			str = obj.val();
			str=str.replaceAll("quot;","\"");
		}
		var jsonobj = eval("("+str+")");
		return jsonobj.attachs;
	}
	
	// 得到附件json字符串
	function setJsonStr(curobj,attachs){
		var sb=new StringBuffer();
		for(var idx=0;idx<attachs.length;idx++){
			var attach = attachs[idx];
			if(typeof attach!='undefined'){
				sb.append("{quot;idquot;:quot;");sb.append(attach.id);sb.append("quot;,");
				sb.append("quot;namequot;:quot;");sb.append(attach.name);sb.append("quot;},");
			}
		}
		var json = sb.toString();
		if(json.length>0){
			json=json.toString().substring(0,json.length-1);
		}
		curobj.val("{quot;attachsquot;:["+json+"]}");
	}
	
	// 绑定显示附件信息层动作
	function attachQtip()
	{
		$('.attach-label').each(function(){
			var obj = $(this);
			var item = obj.closest('li');
			var attachId = item.attr('id');
			var url = fileUrl+'getFile.ht';
			var params ={};
			params.fileId = attachId;
			$.post(url,params,function(result){
				var html = getTableHtml(result);
				obj.qtip({
					content:{
						text:html,
						title:{
							text:result.fileName			
						}
					},
				    position: {
			        	at:'center',
			        	target:'event',
			        	adjust: {
			        		x:-5,
			        		y:-5
		   				},  
		   				viewport: $(window)
				    },
			        show:{   			        	
	  			     	effect: function(offset) {
	  						$(this).slideDown(200);
	  					}
			        },   			     	
			        hide: {
			        	event:'mouseleave',
			        	fixed:true
			        },  
			        style: {
			       	  	classes:'ui-tooltip-light ui-tooltip-shadow'
			        }
				});
			});
		});
	}

	// 删除附件
	function attachDel(){
		$('.attach-del').each(function(){
			var curobj = $(this);
			curobj.bind('click',function(){
				$.ligerMessageBox.confirm('提示信息','确认删除吗？',function(rtn) {
					if(rtn) {
						var attach = curobj.closest('li');
						var url = fileUrl+'/delByFileId.ht';
						var param = {};
						param.ids = attach.attr('id');
						$.post(url,param,function(result){
							var obj = eval("("+result+")");
							if (obj.success)
							{
								var input = curobj.closest('ul').prev('input');
								var attachs = getAttachs(input);
								$.each(attachs,function(i,item){
									if(attach.attr('id')==item.id){
										delete attachs[i];
										setJsonStr(input,attachs);
									}
								});
								attach.remove();
							}
							else
							{
								$.ligerDialog.err('提示信息',"页面出错了",obj.message);
							}
						});
					}
				});
			});
		});
	}

	//构建显示的html
	function getTableHtml(attach)
	{
		var html=[];
		if(attach!=''){
			var sb=new StringBuffer();
			sb.append('<table class="attach-table" cellpadding="0" cellspacing="0" border="0">');
			sb.append('<tr><th width="30%">文件名: </th>');
			sb.append('<td>'+attach.fileName+'</td></tr>');
			sb.append('<tr><th width="30%">文件类型: </th>');
			sb.append('<td>'+attach.ext+'</td></tr>');
			sb.append('<tr><th width="30%">文件说明: </th>');
			sb.append('<td>'+attach.note+'</td></tr>');
			sb.append('<tr><th width="30%">文件大小: </th>');
			sb.append('<td>'+attach.totalBytes+'</td></tr>');
			sb.append("</table><br>");
			html.push('<div class="attach-info">');
			html.push(sb.toString());
		}else{
			html.push('<div class="attach-info-null">');
			html.push('附件不存在！');
		}
		
		html.push('</div>');
		return html.join('');
	}
	
})(jQuery);

$(function(){
	$('.attach').each(function(){
		$(this).attach();
	});
});
