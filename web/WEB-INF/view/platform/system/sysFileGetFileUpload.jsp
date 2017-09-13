<%@page import="com.sf.core.web.util.RequestUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sf.core.util.ContextUtil" %>
<%@include file="/commons/include/html_doctype.html"%>
<%
	Long typeId =RequestUtil.getLong(request, "typeId") ; // 附件分类编号
%>
<html>
  <head>
	<%@include file="/commons/include/form.jsp" %>
  	<title>文件上传</title>
  </head>
  <style type="text/css"> 
	html, body	{ height:100%; }
	body { margin:0; padding:0; overflow:auto; text-align:center;  background-color: #ffffff; }   
	#flashContent { display:none; }
  </style>
  	   <script type="text/javascript" src="${ctx}/media/swf/fileupload/swfobject.js"></script>
       <script type="text/javascript">
       
       var uploadPath="${ctx}/platform/system/sysFile/fileUpload.ht;jsessionid=<%=session.getId()%>?typeId=<%=typeId%>" ;
       var delPath="${ctx}/platform/system/sysFile/delByFileId.ht;jsessionid=<%=session.getId()%>" ;
       
       var fileExt="document@*.doc;*.docx#images@*.jpg;*.png,*.gif";
       
       function initFlashUpload(){
      	   //设置swfobject对象参数
      	   var swfVersionStr = '10.0.0';
           var xiSwfUrlStr = '${ctx}/media/swf/fileupload/playerProductInstall.swf';
           var flashvars = {};
           flashvars.uploadPath=uploadPath;
           flashvars.delPath=delPath;
           flashvars.fileExt=fileExt;
           var params = {};
           params.quality = 'high';
           params.bgcolor = '#ffffff';
           params.allowscriptaccess = 'sameDomain';
           params.allowfullscreen = 'true';
           var attributes = {};
           attributes.id = 'flexupload';
           attributes.name = 'flexupload';
           attributes.align = 'middle';
           swfobject.embedSWF( '${ctx}/media/swf/fileupload/flexupload_'+__lang+'.swf', 'flashContent', 
              '100%', '100%', swfVersionStr, xiSwfUrlStr, flashvars, params, attributes);
           swfobject.createCSS('#flashContent', 'display:block;text-align:left;');
       }
    	/**
		 * 关闭Extjs页面同时刷新GridPanel,【在flex中调用】
		 */
		function winClose(){
			window.close();
		}
    	
    	$(function(){
    		initFlashUpload();
    	})
	</script>
  	<body>
  		<div id="flashContent" >
			<h1>找不到上传控件</h1>
			<p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
		</div>
	  
	</body>
</html>