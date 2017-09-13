/**
 * office控件。
 * 使用方法：
 * var obj=new OfficeControl();
 * obj.renderTo("divContainer",{fileId:123});
 * 	divContainer： 文档容器id
 * 	fileId：附件id，如果指定那么根据该文件id加载word文档。
 *  
 *  saveRemote:保存文档到服务器
 *  
 * @returns {OfficeControl}
 */
OfficeControl=function(){
	{
		
		var Sys = {};
		var ua = navigator.userAgent.toLowerCase();
		var s;
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : (s = ua
			    .match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : (s = ua
				.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : (s = ua
				.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : (s = ua
				.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		
		
		this.controlId="";
		this.controlObj=null;
		this.height="100%";
		this.width="100%";
		var _self=this;
		this.isFileOpen=false;
		this.templatetype = 1;// 模板类型
		//这里发布给客户时，可以修改ProductCaption，ProductKey值。
		this.params={Caption:"广州宏天软件有限公司",MakerCaption:"广州宏天软件有限公司",
			MakerKey:"CF4960BFDB79D36ADDC5493B116D39D6A4E335D9",
			ProductCaption:"广州宏天软件有限公司",
			ProductKey:"8B095D33365E9D16DB26ACF41F2FA28B276FF77C",
			TitlebarColor:"14402205",
			IsCheckEkey:"0"
		};
		this.config={doctype:'doc',fileId:"",controlId:"officeObj",myNum:'0',right:'w'};
	};
	/**
	 * 处理文件菜单事件。
	 */
	this.itemclick=function(item){
		var txt=item.text;
		switch(txt){
			case "新建":
				_self.newDoc();
				break;
			case "打开":
				_self.controlObj.showDialog(1);
				break;
			case "另存为":
				if(!_self.isFileOpen) return;
				_self.controlObj.showDialog(3);
				break;
			case "关闭":
				if(!_self.isFileOpen) return;
				_self.closeDoc();
				break;
			case "打印设置":
				if(!_self.isFileOpen) return;
				_self.controlObj.showdialog(5);
				break;
			case "打印预览":
				if(!_self.isFileOpen) return;
				_self.controlObj.PrintPreview();
				break;
			case "打印":
				if(!_self.isFileOpen) return;
				_self.controlObj.printout(true);
				break;
		}
		
	},
	/**
	 * 获取文档类型。
	 */
	this.getDocType=function(){
		var docType="Word.Document";
		var type=this.config.doctype;
		switch(type){
			case "doc":
				docType="Word.Document";
				break;
			case "xls":
				docType="Excel.Sheet";
				break;
			case "ppt":
				docType="PowerPoint.Show";
				break;
		}
		return docType;
	},
	//click事件需要事先进行定义。
	this.memuItems= { width: 120, items:
        [{text: '新建', click: this.itemclick },
         {text: '打开', click: this.itemclick },
         {text: '另存为', click: this.itemclick },
         {text: '关闭', click: this.itemclick },
         {line: true },
         {text: '打印设置', click: this.itemclick },
         {text: '打印预览', click: this.itemclick },
         {text: '打印', click: this.itemclick }]
     };
	/**
	 * 处理菜单点击事件。
	 */
	this.buttonClick=function(item){
	
		var txt=item.text;
		switch(txt){
			case "留痕":
				_self.controlObj.ActiveDocument.TrackRevisions=true;
				break;
			case "不留痕":
				_self.controlObj.ActiveDocument.TrackRevisions=false;
				break;
			case "清除痕迹":
				_self.controlObj.ActiveDocument.AcceptAllRevisions();
				break;
			case "模版套红":
				_self.insertContentTemplate();
				break;
			case "选择模版":
				_self.insertTemplate();
				break;
			case "手写签名":
				try{
					_self.controlObj.DoHandSign2(
							"ntko",//手写签名用户名称
							"ntko",//signkey,DoCheckSign(检查印章函数)需要的验证密钥。
							0,//left
							0,//top
							1,//relative,设定签名位置的参照对象.0：表示按照屏幕位置插入，此时，Left,Top属性不起作用。1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落（为兼容以前版本默认方式）
							100);
				}catch(err){
				}
				break;
			case "盖章":
				_self.signature();
				break;
			case "全屏":
				_self.controlObj.FullScreenMode=true;
				break;
			case "转成PDF":
				_self.officeToPdf();
				break;
			case "Ekey盖章":
				_self.signatureFromEkey();
				break;
			case "PDF盖章":
				_self.signaturePdfFromEkey();
				break;
		}
	},
	/**
	 * 获取模版
	 */
	this.getTemplate=function(){
		var url=__ctx +"/platform/system/sysOfficeTemplate/dialog.ht?type="+this.templatetype;
		var winArgs="dialogWidth=600px;dialogHeight=400px;help=0;status=0;scroll=1;center=1";
		url=url.getNewUrl();
		var rtn=window.showModalDialog(url,"",winArgs);
		return rtn;
	},
	/**
	 * 模版套红。
	 */
	this.insertContentTemplate=function(){
		try{
			this.templatetype = 2;
			var rtn=_self.getTemplate();
			this.templatetype = 1;
			if(rtn==undefined || rtn==null ){
				
				return false;
			}
		//	var templateUrl=__ctx +"/" + rtn.path;
			var templateUrl = __ctx + "/platform/system/sysOfficeTemplate/getTemplateById.ht?templateId=" + rtn.templateId;
			
			//选择对象当前文档的所有内容
			var curSel = _self.controlObj.ActiveDocument.Application.Selection;
			curSel.WholeStory();
			curSel.Cut();
			
			if(!(Sys.firefox) && !(Sys.chrome)){      //IE是同步的，它 会等待模版加载成功后执行书签的插入 （火狐和谷歌就不可以）
				//插入模板
				_self.controlObj.AddTemplateFromURL(templateUrl);     //AddTemplateFromURL
				var BookMarkName = "content";
				if(!_self.controlObj.ActiveDocument.BookMarks.Exists(BookMarkName)){
					alert("Word 模板中不存在名称为：\""+BookMarkName+"\"的书签！");
					return false;
				}
				var bkmkObj = _self.controlObj.ActiveDocument.BookMarks(BookMarkName);	
				var saverange = bkmkObj.Range;
				saverange.Paste();
				_self.controlObj.ActiveDocument.Bookmarks.Add(BookMarkName,saverange);	
			}else{
				//插入模板(火狐谷歌 异步)
				_self.controlObj.AddTemplateFromURL(templateUrl);     //AddTemplateFromURL
			}			
			
		}catch(err){
			$.ligerDialog.error("insertTemplate:" +err.name + ": " + err.message);
		}
	},
	/**
	 * 插入word模版。
	 */
	this.insertTemplate=function(){
		try{
			var rtn=_self.getTemplate();
			if(rtn==undefined || rtn==null ){
				
				return;
			}
		//	var headFileURL=__ctx +"/" + rtn.path;
			var headFileURL = __ctx + "/platform/system/sysOfficeTemplate/getTemplateById.ht?templateId=" + rtn.templateId;
			_self.controlObj.ActiveDocument.Application.Selection.HomeKey(6,0);//光标移动到文档开头
			_self.controlObj.OpenFromURL(headFileURL);//在光标位置插入红头文档
		}catch(err){
			$.ligerDialog.error("insertTemplate:" +err.name + ": " + err.message);
		}
	},
		
	/**
	 * 获取控件的html。
	 */
	this.getControlHtml=function(controlId){
			var cabPath=__ctx +"/media/office/OfficeControl.cab#version=5,0,2,2";
			var str = '';
			if (Sys.ie) {
				str = '<object  id="'+controlId+'" codeBase="'+cabPath+'" height="'+this.height+'" width="'+this.width+'" classid="clsid:A39F1330-3322-4a1d-9BF0-0BA2BB90E970" style="z-index:-1;">';
				str +='<param name="IsUseUTF8URL" value="-1">   ';
				str +='<param name="IsUseUTF8Data" value="-1">   ';
				str +='<param name="BorderStyle" value="1">   ';
				str +='<param name="BorderColor" value="14402205">   ';
			//	str +='<param name="TitlebarColor" value="15658734">   ';
				str +='<param name="TitlebarTextColor" value="0">   ';
				str +='<param name="MenubarColor" value="14402205">   ';
				str +='<param name="MenuButtonColor" VALUE="16180947">   ';
				str +='<param name="MenuBarStyle" value="3">   ';
				str +='<param name="MenuButtonStyle" value="7">   ';
				for(var key in this.params){
					str+='<param name="'+key+'" value="'+this.params[key]+'">';
				}
				str +='<param name="WebUserName" value="宏天软件">   ';
	            str+="</object>";
			} else if (Sys.firefox) {
				str = '<object id="'+controlId+'" codeBase="'+cabPath+'" height="'+this.height+'" width="'+this.width+'"  type="application/ntko-plug" ForOnSaveToURL="saveMethodOnComplete"  ForOnAddTemplateFromURL="addTemplateOnComplete'+ this.config.myNum +'" ';			
				str +='ForOnpublishAshtmltourl="publishashtml"	';
				str +='ForOnpublishAspdftourl="publishaspdf"	';
				str +='ForOnSaveAsOtherFormatToUrl="saveasotherurl"	 ';
				str +='ForOnDoWebGet="dowebget"	';
				str +='ForOnDoWebExecute="webExecute"	';
				str +='ForOnDoWebExecute2="webExecute2"	';
				str +='ForOnFileCommand="FileCommand"	';
				str +='ForOnCustomMenuCmd2="CustomMenuCmd"	';
				str +='_IsUseUTF8URL="-1"  ';
				str +='_IsUseUTF8Data="-1"  ';
				str +='_BorderStyle="1"   ';
				str +='_BorderColor="14402205"   ';
				str +='_MenubarColor="14402205"   ';
				str +='_MenuButtonColor="16180947"   ';
				str +='_MenuBarStyle="3"  ';
				str +='_MenuButtonStyle="7"   ';				
				for(var key in this.params){
					str += '_'+key+'="'+this.params[key]+'"	';
				}
				str +='_WebUserName="宏天软件"   ';
				str +='clsid="{A39F1330-3322-4a1d-9BF0-0BA2BB90E970}" >';
		//		str +='<SPAN STYLE="color:red">尚未安装NTKO Web FireFox跨浏览器插件。请点击<a href="ntkoplugins.xpi">安装组1件</a></SPAN>   ';
				str +='</object>  ';				
			} else if (Sys.chrome) {
				str ='<object id="'+controlId+'" clsid="{A39F1330-3322-4a1d-9BF0-0BA2BB90E970}"  ForOnSaveToURL="saveMethodOnComplete" ForOnAddTemplateFromURL="addTemplateOnComplete'+ this.config.myNum +'" ';
				str +='ForOnpublishAshtmltourl="publishashtml"	';
				str +='ForOnpublishAspdftourl="publishaspdf"	';
				str +='ForOnSaveAsOtherFormatToUrl="saveasotherurl"	';
				str +='ForOnDoWebGet="dowebget"	';
				str +='ForOnDoWebExecute="webExecute"	';
				str +='ForOnDoWebExecute2="webExecute2"	';
				str +='ForOnFileCommand="FileCommand"	';
				str +='ForOnCustomMenuCmd2="CustomMenuCmd"	';
				str +='codeBase="'+cabPath+'" height="'+this.height+'" width="'+this.width+'" type="application/ntko-plug" ';
				str +='_IsUseUTF8URL="-1"   ';
				str +='_IsUseUTF8Data="-1"   ';
				str +='_BorderStyle="1"   ';
				str +='_BorderColor="14402205"   ';
				str +='_MenubarColor="14402205"   ';
				str +='_MenuButtonColor="16180947"   ';
				str +='_MenuBarStyle="3"  ';
				str +='_MenuButtonStyle="7"   ';
				for(var key in this.params){
					str += '_'+key+'="'+this.params[key]+'"	';
				}
				str +='_WebUserName="宏天软件" >   ';
			//	str +='<SPAN STYLE="color:red">尚未安装NTKO Web Chrome跨浏览器插件。请点击<a href="ntkoplugins.crx">安装组件</a></SPAN>   ';
				str +='</object>';
			}/* else if (Sys.opera) {
				alert("对不起,控件暂时不支持opera!");
			} else if (Sys.safari) {
				alert("对不起,控件暂时不支持safari!");
			} else{
				alert("对不起,控件暂时不支持该浏览器!");
			}*/
			
			//是火狐和谷歌时 增加插入套红模版的回调函数
			if(Sys.firefox||Sys.chrome){
				str += ' <script type="text/javascript" > ';
				str += '		function addTemplateOnComplete'+ this.config.myNum +'(){ ';
				str += '			addTemplateOnComplete("'+this.config.myNum+'"); ';
				str += '		} ';						
				str += ' </script> ';
			}
            return str;
	},
	/**
	 * 将控件添加到div容器中。
	 * 第一个参数：
	 * div的容器ID
	 * 第二个参数:
	 * conf:
	 * doctype:文挡类型：可以为doc，xls，ppt
	 * fileId:服务器保存的文件ID
	 */
	this.renderTo=function(divContainerId,conf){
		this.config=$.extend({},this.config,conf);
		
/*		var div_rq = $("#" + divContainerId);
		this.height = div_rq.css("height");
		this.width = div_rq.css("width");
		alert("dwidth="+this.width +"   dheight="+this.height);*/
		this.controlId="office_" + divContainerId;
		var html=this.getControlHtml(this.controlId);
		var menuBar = '<div name="menuBar"></div>';
		$("#" +divContainerId).html("");
		if(this.config.right=='r'){
			menuBar = '<div name="menuBar" style="display:none;"></div>';
		}
		$("#" +divContainerId).append(menuBar);
		$("#" +divContainerId).append(html);
		
		var obj=document.getElementById(this.controlId);
		
		this.controlObj=obj;
		this.controlObj.MenuBar=false;
		this.controlObj.Titlebar=false;
		this.controlObj.IsShowToolMenu=true;
		this.controlObj.ToolBars=true;
		var jqControlObj=$(this.controlObj);
		
		//修改控件的高度。
		jqControlObj.height(jqControlObj.parent().height()-26);
		
		var docType=this.config.doctype;
		
		var items=[];	 
		 //2013-05-31 add
		 if(docType=="pdf"){
			 items.push( { text: 'PDF盖章' ,click:this.buttonClick});
		 }else{
			 items.push({ text: '文件', menu: this.memuItems});
			 if(docType=="doc"){
				 items.push({ text: '留痕',click:this.buttonClick });
				 items.push({ text: '不留痕' ,click:this.buttonClick});
				 items.push( { text: '清除痕迹' ,click:this.buttonClick});
			 }
			 items.push( { text: '模版套红' ,click:this.buttonClick});
			 items.push({ text: '选择模版' ,click:this.buttonClick});
			 items.push({ text: '手写签名' ,click:this.buttonClick});
			 items.push({ text: '盖章' ,click:this.buttonClick});
			//2013-05-31 add
			 items.push( { text: '转成PDF' ,click:this.buttonClick});
			 items.push( { text: 'Ekey盖章' ,click:this.buttonClick});
		 }
		 items.push({ text: '全屏' ,click:this.buttonClick});

		 $("#" +divContainerId +" div[name='menuBar']").ligerMenuBar({ items:items});
		 
		 if(Sys.ie||Sys.firefox||Sys.chrome){
			 if(docType=="pdf"){
				 this.addPDFSupport();
				 this.isFileOpen=true;
			 }else{
				 this.initDoc();
			 }
		 }
		 else{
			 $.ligerDialog.warn('office控件只支持IE、Firefox和Chrome 32位版的浏览器!');
		 }
		 
    };
	
    /**
	 * 控件载入时，载入文档。
	 */
	this.initDoc=function(){
		//指定了文件。
		if(this.config.fileId!="" && this.config.fileId>0){
			var path= __ctx + "/platform/system/sysFile/getFileById.ht?fileId=" + this.config.fileId;
			try{
				this.controlObj.OpenFromURL(path);
				this.isFileOpen=true;
			}
			catch(err){
				try{
					this.addPDFSupport();
					this.isFileOpen=true;
				}
				catch(err){
					this.newDoc();
				}
			}
		}
		//新建文档。
		else{
			this.newDoc();
		}
	};
	/**
	 * 关闭文档。
	 */
	this.closeDoc=function(){
		if(Sys.ie||Sys.firefox||Sys.chrome){     //$.browser.msie
			 this.initDoc();
		}else{
			 $.ligerDialog.warn('office控件只支持IE、Firefox和Chrome 32位版的浏览器!');
		}
		try{
			this.controlObj.close();
			this.isFileOpen=false;
		}catch(err){			
			$.ligerDialog.error('closeDoc:' +err.name + ": " + err.message);
		}
	};
	
	/**
	 * 新建文档。
	 */
	this.newDoc=function(){
		try
		{
			var docType=this.getDocType();
			this.controlObj.CreateNew(docType);
			this.isFileOpen=true;
		}
		catch(err){
			$.ligerDialog.error("newDoc:" +err.name + ": " + err.message);
		}
	};
	
	/**
	 * 保存文件到服务器。
	 * 服务器返回文件id到this.config.fileId，同时返回文件ID。
	 */
	this.saveRemote=function(inputObjNum){
		var path= __ctx + "/platform/system/sysFile/saveFile.ht";
		var uploadName = this.controlId +"_name";
		var params="fileId=" + this.config.fileId + "&uploadName="+ uploadName;
		try{
			//保存数据到服务器。
			var curDate=new Date();
			var docName=Math.random()*curDate.getMilliseconds()*10000;
			/*如果人后辍名为空时,需要用对象的类型来区分是什么文件返回当前控件中的文档类型,
				  只读 0:  没有文档； 100 =其他文档类型；
				1=word；2=Excel.Sheet或者 Excel.Chart ；
				3=PowerPoint.Show； 4= Visio.Drawing； 
				5=MSProject.Project； 6= WPS Doc；
				7:Kingsoft Sheet； 51 = PDF文档
			*/
			if(this.config.doctype==''||this.config.doctype==null|| 'undefined' == typeof (this.config.doctype)){
				var type = this.controlObj.DocType;
				if(type==2){
					this.config.doctype = "xls";
				}else if(type==3){
					this.config.doctype = "ppt";
				}else if(type==51){
					this.config.doctype = "pdf";
				}else{
					this.config.doctype = "doc";
				}
			}
			var result;
			if(Sys.firefox||Sys.chrome){   //火狐谷歌浏览器控件文档保存事件（异步的）
				if(typeof(inputObjNum)!=undefined && inputObjNum!=null){
					params += "&inputObjNum="+ inputObjNum;        // 用于保存返回的值对象的名称 （异步的才会有）
				}				
				this.controlObj.SaveToURL(path,uploadName,params,docName+"." + this.config.doctype,0);
				result=-11;
			}else{   //IE是同步的
				result=this.controlObj.SaveToURL(path,uploadName,params,docName+"." + this.config.doctype,0);
				this.config.fileId=result;
			}			
			return result;
		}
		catch(err){
			alert("saveRemote:" +err.name + ": " + err.message);
			return -1;
		}
	};
	
	/**
	 * 对文档进行签单
	 */
	this.signature=function(){
		
		var url = __ctx + "/platform/system/seal/dialog.ht";
		var winArgs = "dialogWidth=800px;dialogHeight=600px;help=0;status=0;scroll=1;center=0;resizable=1;";
		url = url.getNewUrl();
		var retVal = window.showModalDialog(url, "", winArgs);		
		if(typeof(retVal)==undefined||retVal==null){
			return false;
		}
		if(retVal.fileId.isEmpty()){
			return false;
		}
		var sealUrl=__ctx + "/platform/system/sysFile/getFileById.ht?fileId=" + retVal.fileId;
		try{
//			this.controlObj.AddSignFromURL(retVal.userName,sealUrl);
			this.controlObj.AddSecSignFromURL(retVal.sealName,//签章的用户名
					sealUrl,//印章所在服务器相对url
					0,//left
					0,//top
					1,//relative
					2,  //print mode 2
					false,//是是否使用证书，true或者false，
					false //是否锁定印章
					);
		}catch(err){
			alert("signature:" +err.name + ": " + err.message);
			return -1;
		}
	};
	
	
	/**
	 * 对Office文档进行Ekey硬件签章
	 */
	this.signatureFromEkey = function(){
		if(this.controlObj!=null){
			/*if(!this.controlObj.IsEkeyConnected)       //暂时不确定
			{
				alert("没有检测到EKEY,请将EKEY插入到计算机!然后点击确定继续.");
				return;
			}*/	
			this.controlObj.AddSecSignFromEKEY(
							"广州宏天软件有限公司",     //username
							0,  //left
							0,  // top,
							1,  // relative,
							2,  // PrintMode,
							false,  // IsUseCertificate,
							false,  // IsLocked,
							true,  // IsCheckDocChange,
							false,  //  IsShowUI
							true,  //   signpass,
							-1,    // ekeySignIndex,
							true,  //  IsAddComment,
							true  //  IsBelowText
			);
		}	
	};

	/**
	 * 对PDF文档进行Ekey硬件签章
	 */
	this.signaturePdfFromEkey=function(){

		if(this.controlObj!=null){
			/*if(!this.controlObj.IsEkeyConnected)
			{
				alert("没有检测到EKEY.请将EKEY插入到计算机!然后点击确定继续.");
				return;
			}*/
			this.controlObj.AddPDFSecSignFromEKEY("广州宏天软件有限公司",null,"111111",null,1,null,null,null,null,true,false,true,false,null);
		}			
	};
	
	
	
	
	/**
	 * 把Office文件转换成PDF文件。
	 * 服务器返回文件id到this.config.fileId，同时返回文件ID。
	 */
	this.officeToPdf=function(){
		if(!confirm("文档转换成PDF后将不可以恢复原有格式文档，确认转换吗？"))
		{
			return;
		}		
		try{
			//保存数据到服务器。
			var pdfUrl = __ctx + "/platform/system/sysFile/saveFilePdf.ht";
			var uploadName = this.controlId +"_pdf";
			var params="fileId=" + this.config.fileId + "&uploadName="+ uploadName;
			var pdfName = this.config.fileId+".pdf";
			this.controlObj.PublishAsPDFToURL(pdfUrl,uploadName,params,pdfName,0,null,true,true,false,true,true,true);
			//window.location.href=window.location.href;
		}
		catch(err){
			alert("officeToPdf:" +err.name + ": " + err.message);
		}			
	};
    
	
	/**
	 * 把打开PDF文件
	 */
	this.addPDFSupport=function()
	{
	//	debugger;
	//	this.controlObj = document.getElementById(this.controlId);
		if(document.URL.indexOf("file://")>=0)
		{
			if(!confirm("如果从本地磁盘打开的URL，需要手工运行命令'regsvr32 ntkopdfdoc.dll'注册插件文件.您确认已经注册了吗？"))
			{
				return;
			}
		}
		var path = __ctx + "/platform/system/sysFile/getFileById.ht?fileId=" + this.config.fileId;
        this.controlObj.AddDocTypePlugin(".pdf","PDF.NtkoDocument","4.0.0.1",__ctx+"/media/office/ntkopdfdoc.cab",51,true);	//引用pdf组件
	//	this.controlObj.BeginOpenFromURL(path);  //打开PDF从URL "media/office/bpm.pdf"
		this.controlObj.OpenFromURL(path);
		
	};
	
	
};



/**
 * 火狐谷歌浏览器控件文档保存事件（异步的，IE是同步的）回调接管函数 注意不在OfficeControl类里面  一定是单独方法  是控件属性的ForOnSaveToURL对应的方法 （SaveToURL保存后的回调函数）
 * html 为后台返回的内容
 */
function saveMethodOnComplete(type, code, html) {
	var arrys = html.split("##"); 
	var arryNum = arrys[0];    //保存对象的序号
	var arryValue = arrys[1];    //要保存的内容
	if(arryNum>=0&&arryValue!=''){
		OfficePlugin.fileObjs.get(arryNum).setAttribute("value",arryValue);   //保存到对象的值
		OfficePlugin.officeObjs[arryNum].config.fileId = arryValue;  //控件中config对象的fileId
	}
	OfficePlugin.hasSubmitOffices[arryNum]=true; //完成标志
	OfficePlugin.submitNewNum = OfficePlugin.submitNewNum + 1;   //每回调一次就 提交数量的变量就 加上 1 
	if(OfficePlugin.submitNum == OfficePlugin.submitNewNum){    //当提交问题 等于 提交数量的变量 时 表示所有文档 都提交了  然后做 业务相关的提交
		var data=CustomForm.getData();
		//设置表单数据
		$("#formData").val(data);
		$('#frmWorkFlow').submit();
		OfficePlugin.submitNewNum = 0; //重置  提交数量的变量
	}
	
}


/**
 * 火狐谷歌浏览器控件文档在套红模版事件（异步的，IE是同步的）回调接管函数 注意不在OfficeControl类里面  一定是单独方法  是控件属性的ForOnAddTemplateFromURL对应的方法 （AddTemplateFromURL保存后的回调函数）
 * html 为后台返回的内容
 */
function addTemplateOnComplete(myNum) {
	//OfficePlugin.fileObjs.get(myNum).setAttribute("value",arryValue);   //保存到对象的值
	myObj = OfficePlugin.officeObjs[myNum];  //OfficeControl 实例对象
	var BookMarkName = "content";
	if(!myObj.controlObj.ActiveDocument.BookMarks.Exists(BookMarkName)){
		alert("Word 模板中不存在名称为：\""+BookMarkName+"\"的书签！");
		return false;
	}
//	var bkmkObj = myObj.controlObj.ActiveDocument.BookMarks(BookMarkName);   //IE的方法
	var bkmkObj = myObj.controlObj.ActiveDocument.BookMarks.Item(BookMarkName);	  //火狐和谷歌特有的
	var saverange = bkmkObj.Range;
	saverange.Paste();
	myObj.controlObj.ActiveDocument.Bookmarks.Add(BookMarkName,saverange);	
	
}

