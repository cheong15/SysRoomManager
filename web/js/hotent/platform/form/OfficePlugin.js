/**
 * Office插件，用于自定义表单。
 * 
 * 1.OfficePlugin.init();
 *  	加载office控件。
 * 
 * 2.OfficePlugin.submit();
 * 		保存office文档。
 */
OfficePlugin={
		//office控件对象
		officeObjs:new Array(),
		//附件对象
		fileObjs:null,
		//判断当前表单页面是否有office控件。
		hasOfficeFields:new Array(),		
		//判断当前表单页面所有office控件是否都提交了。
		hasSubmitOffices:new Array(),
		//有多少文档可以提交的
		submitNum:null,
		//当前提交文档变量
		submitNewNum:null,
		//是否初始化tab中的OFFICE工具栏目的隐藏问题。在FormUtil用到
		isTabItemOffice:true,
		//初始化
		//所做的操作如下：
		//1.检查当前表单中是否有office控件。
		//2.如果存在office控件
		// 	获取文件id，将office控件添加到容器中。
		init:function(){
			var num = 0;
			var myNum = 0;
			this.fileObjs=$("input[controltype='office']");
			for ( var i = 0; i < this.fileObjs.length; i++) {
				var fileObj = this.fileObjs.get(i);
				var name=fileObj.getAttribute("name");
				var fileId=fileObj.getAttribute("value");

				//获取附件的扩展名
				var doctype = "";
				if(fileId!=null&&fileId!=""){
					doctype = sysFileType(fileId);
				}else{
					fileId="";
				}
				
				//容器的ID  
				var divId="div_" + name.replaceAll(":","_");
				
				//容器样式修改
				var div_rq = $("#" + divId);
				if(!div_rq || div_rq.length<1){
					//如果找不到容器，则为表单生成模式，需要生成一个容器div
					div_rq = $('<div id="'+divId+'" class="office-div"></div>');
					$(fileObj).after(div_rq);
				}
				var dwidth = div_rq.css("width").toLowerCase().replaceAll("px",""); 
				if(dwidth<700){
					div_rq.css("width","700px");
				}
				var dheight = div_rq.css("height").toLowerCase().replaceAll("px",""); 
				if(dheight<700){
					div_rq.css("height","700px");
				}
				
				var right=fileObj.getAttribute("right");
				
				//有多少 个文档是可以做提交的
				if(right=="w"||right=="b"){
					//必填  已在其它处理
					if(right=="b"){
						var validRule = fileObj.getAttribute("validate");
						if ( validRule != null && 'undefined' != validRule.toLowerCase() && validRule.length>2 ){ 
							var json = eval('(' + validRule + ')');		
							if(json.required){
								//不做操作
							}else{
								var jsonStr = validRule.substring(0, validRule.lastIndexOf('}'));
								jsonStr +=",\'required\':true}";    //加上必填
								fileObj.setAttribute("validate",jsonStr);    
							}				
						}else{
							fileObj.setAttribute("validate","{\'required\':true}");     //必填
						}
					}	
					num++;           //做可提交的标志数量
				}
				
				//没有权限，删除div容器。
				if(right=="no"){
					div_rq.remove();
				}else{             //有读和写的权限，加载控件。
					$.ligerDialog.waitting('正在加载OFFICE文档,请稍候...');
					//加载控件。
					var officeObj= new OfficeControl();
					this.officeObjs.push(officeObj);
					//加载office控件。
					officeObj.renderTo(divId,{fileId:fileId,doctype:doctype,myNum:myNum,right:right});
					//是否有office控件。
					this.hasOfficeFields.push(true);
					//office控件文档标志
					this.hasSubmitOffices.push(false);
					myNum++; //序号
					$.ligerDialog.closeWaitting(); 
				}
			}
			this.submitNum = num;  //文档是可以做提交的数目   总娄
			this.submitNewNum = 0;  //文档是提交了的数目     变化的(重新清空)
		},
		//提交文件保存。
		//如果有office控件。则保存后将返回的附件id放到隐藏域。
		submit:function(){
			$.ligerDialog.waitting('正在保存OFFICE文档中,请稍候...');
		//	var _self=this;
			for ( var cn = 0; cn < this.officeObjs.length; cn++) {
				var officeObj= this.officeObjs[cn];
				if(!this.hasOfficeFields[cn]) return;
				var right=this.fileObjs.get(cn).getAttribute("right");
				//可写，保存office内容并上传。
				if(right=="w"||right=="b"){
					//保存到服务器。
					var result=officeObj.saveRemote(cn);
					if(result==-11){
						//由 火狐谷歌浏览器控件文档保存事件（异步的，IE是同步的）回调接管函数 OfficeControl.js 中有  saveToURLOnComplete 处理 
					}else{
						//将结果放到隐藏域。(同步的 处理方法 IE 360 等)
						this.fileObjs.get(cn).setAttribute("value",result);
						this.hasSubmitOffices[cn]=true; //完成标志
						this.submitNewNum = this.submitNewNum - 1;  //文档是提交了的数目     变化的(重新清空)
					}
				}else{
					this.hasSubmitOffices[cn]=true;    //完成标志					
				}
			}
			$.ligerDialog.closeWaitting();   //关闭提示
		}
};


function sysFileType(fileId)
{
	var doctype = "doc";
	var path = __ctx +'/platform/system/sysFile/getFileType.ht';
    $.ajaxSetup({async:false});  //同步
	$.post(path,{fileId:fileId},function(data){			
	   doctype = data;
    });
	$.ajaxSetup({async:true}); //异步
	return doctype;
};


