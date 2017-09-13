<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>岗位选择</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<script type="text/javascript" src="${ctx}/js/lg/plugins/ligerLayout.js"></script>
	<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.js"></script>
	<f:js pre="js/lang/view/platform/selector" ></f:js>
	<script type="text/javascript">
		var isSingle="${param.isSingle}";
		var posTree = null;
		var expandByDepth = 0;
		forbidF5("Chrome");//禁止刷新页面
		//树节点是否可点击
		var treeNodelickAble=true;
		$(function() {
			$("#defLayout").ligerLayout({
				leftWidth: 190,
				allowRightResize:false,
				allowLeftResize:false,
				allowTopResize:false,
				allowBottomResize:false,
				height: '90%',
				minLeftWidth:160,
				rightWidth:170,
			});
			loadTree();
			//快速查找
			handQuickFind();
			//初始化原来的数据
			initData();
		});
		
		var findStr = '';
		function handQuickFind(){
			$("input.quick-find").bind('keyup',function(){
				var str = $(this).val();
				if(!str)return;
				if(str==findStr)return;
				findStr = str;
				var  tbody = $("#posList"), 	
					 firstTr = $('tr.hidden',tbody);
				$("tr",tbody).each(function(){
					var me = $(this),
						span = $('span',me),
						spanStr = span.html();
					if(!spanStr)return true;						
					if(spanStr.indexOf(findStr)>-1){
						$(this).insertAfter(firstTr);
					}
				});
			});
		}
	
		//展开收起
		function treeExpandAll(type) {
			posTree = $.fn.zTree.getZTreeObj("posTree");
			posTree.expandAll(type);
		};
		//异步加载展开
		function treeExpand() {
			posTree = $.fn.zTree.getZTreeObj("posTree");
			var treeNodes = posTree.transformToArray(posTree.getNodes());
			for(var i=1;i<treeNodes.length;i++){
				if(treeNodes[i].children){
					posTree.expandNode(treeNodes[i], true, false, false);
				}
			}
		};
		
		var posTree;
		function loadTree(){
			var setting = {
				data: {
					key : {
						
						name: "posName",
						title: "posName"
					},
				
					simpleData: {
						enable: true,
						idKey: "posId",
						pIdKey: "parentId",
						rootPId: 0
					}
				},
				async: {
					enable: true,
					url:"${ctx}/platform/system/position/getChildTreeData.ht",
					autoParam:["posId","parentId"],
					dataFilter: filter
				},
				callback:{
					onClick: treeClick,
					onAsyncSuccess: zTreeOnAsyncSuccess
				}
				
			};
			posTree=$.fn.zTree.init($("#posTree"), setting);
			treeNodelickAble=true;
			
			
		};
		
		//判断是否为子结点,以改变图标	
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			if(treeNode){
		  	 var children=treeNode.children;
			  	 if(children.length==0){
			  		treeNode.isParent=true;
			  		posTree = $.fn.zTree.getZTreeObj("posTree");
			  		posTree.updateNode(treeNode);
			  	 }
			}
		};
		
		//过滤节点,默认为父节点,以改变图标	
		function filter(treeId, parentNode, childNodes) {
				if (!childNodes) return null;
				for (var i=0, l=childNodes.length; i<l; i++) {
					if(!childNodes[i].isParent){
						childNodes[i].isParent = true;
					}
				}
				return childNodes;
		};
		//选择分类
		function getSelectNode() {
			posTree = $.fn.zTree.getZTreeObj("posTree");
			var nodes = posTree.getSelectedNodes();
			var node = nodes[0];
			if (node == null || node.posId == 0)
				return '';
			return node.posId;
		}
	
		function treeClick(event, treeId, treeNode) {
			var pid = getSelectNode();
			var url = "${ctx}/platform/system/position/selector.ht?pid=" + pid+"&isSingle=${param.isSingle}";
			$("#posFrame").attr("src", url);
		}
	
	
		function dellAll() {
			$("#posList").empty();
		};
		function del(obj) {
			var tr = $(obj).closest("tr");
			$(tr).remove();
		};
		
		//初始化父级窗口传进来的数据
		function initData(){
			var obj = window.dialogArguments;
			if(obj&&obj.length>0){
				for(var i=0,c;c=obj[i++];){
					var data = c.id+'#'+c.name;
					if(c.name!=undefined&&c.name!="undefined"&&c.name!=null&&c.name!=""){
						add(data);
					}
				}
			}
		};
		
		function add(data) {
			var aryTmp=data.split("#");
			var posId=aryTmp[0];
			var len= $("#pos_" + posId).length;
			if(len>0) return;
			var roleTemplate= $("#posTemplate").val();
			var html=roleTemplate.replace("#posId",posId)
					.replace("#data",data)
					.replace("#name",aryTmp[1]);
			$("#posList").append(html);
		};
	
		function selectMulti(obj) {
			if ($(obj).attr("checked") == "checked"){
				var data = $(obj).val();
				add(data);
			}	
		};
		
		function  selectPosition(){
			var pleaseSelect= $lang_selector.position.pleaseSelect;
			var aryPos;
			if(isSingle=='true'){
				aryPos = $('#posFrame').contents().find(":input[name='posId'][checked]");
			}else{
				aryPos =$("input[name='pos']", $("#posList"));
			}
			if(aryPos.length==0){
				alert(pleaseSelect);
				return;
			}
			var aryId=[];
			var aryName=[];
			var posJson = [];
			aryPos.each(function(){
				var data=$(this).val();
				var aryTmp=data.split("#");
				aryId.push(aryTmp[0]);
				aryName.push(aryTmp[1]);
				posJson.push({id:aryTmp[0],name:aryTmp[1]});
			});
			var posIds=aryId.join(",");
			var posNames=aryName.join(",");
			
			var obj={};
			obj.posId=posIds;
			obj.posName=posNames;
			obj.posJson = posJson;
			window.returnValue=obj;
			window.close();
		}
		
		//清空岗位
		function clearPosition(){
			window.returnValue={posId:'',posName:''};
			window.close();
		}
	</script>
	<style type="text/css">
		html { overflow-x: hidden; }
		.ztree {
			overflow: auto;
		}
		
		.label {
			color: #6F8DC6;
			text-align: right;
			padding-right: 6px;
			padding-left: 0px;
			font-weight: bold;
		}
				.panel-search {
    min-width: 321px;
    padding-bottom: 3px;
    padding-left: 0px;
    padding-top: 3px;
}
.l-layout-right{left:578px;}

	</style>
</head>
<body>
	<div id="defLayout">
		<div position="left" title="岗位树" style="height:95%;" >
			<div class="tree-toolbar">
				<span class="toolBar">
					<div class="group">
						<a class="link reload" id="treeFresh" href="javascript:loadTree();">刷新</a>
					</div>
					<div class="l-bar-separator"></div>
					<div class="group">
						<a class="link expand" id="treeExpandAll" href="javascript:treeExpand()">展开</a>
					</div>
					<div class="l-bar-separator"></div>
					<div class="group">
						<a class="link collapse]" id="treeCollapseAll" href="javascript:treeExpandAll(false)">收起</a>
					</div>
				</span>
			</div>
			<ul id="posTree" class="ztree" style="overflow: auto;margin:0 0 5px 0; height:93%;"></ul>
		</div>
		<div position="center">
			<div class="l-layout-header">岗位列表</div> 
			<iframe id="posFrame" name="posFrame" height="95%" width="100%"  frameborder="0" src="${ctx}/platform/system/position/selector.ht?isSingle=${param.isSingle}"></iframe>
		</div>
		<c:if test="${param.isSingle==false}">
		 <div position="right" title="<span><a onclick='javascript:dellAll();' class='link del'>清空 </a><input type='text' class='quick-find' title='快速查询'/></span>" style="overflow: auto;height:95%;width:170px;">
  			<table width="145px"   class="table-grid table-list"  cellpadding="1" cellspacing="1">
  				<tbody id="posList">
  					<tr class="hidden"></tr>
  				</tbody>
			</table>
	    </div>
		</c:if>
	</div>
	<div position="bottom"  class="bottom" style='margin-top:10px;'>
		<a href='#' class='button'  onclick="selectPosition()" style="margin-right:10px;"><span class="icon ok"></span><span>选择</span></a>
		<a href="#" class="button"  onclick="clearPosition()"><span class="icon cancel" ></span><span class="cance" >清空</span></a>
		<a href='#' class='button' style='margin-left:10px;'  onclick="window.close()"><span class="icon cancel"></span><span>取消</span></a>
	</div>
	<textarea style="display: none;" id="posTemplate">
		<tr id="pos_#posId">
	  			<td>
	  				<input type="hidden" name="pos" value="#data"><span>#name</span>
	  			</td>
	  			<td style="width: 30px;" nowrap="nowrap"><a onclick="javascript:del(this);" class="link del" title="删除">&nbsp;</a> </td>
	  	</tr>
	</textarea>
</body>
</html>