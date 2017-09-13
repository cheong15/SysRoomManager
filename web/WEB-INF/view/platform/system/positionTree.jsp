<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" import="com.hotent.platform.model.system.Position"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>岗位管理</title>
	<%@include file="/commons/include/form.jsp" %>
	<base target="_self"/> 
	<f:link href="tree/zTreeStyle.css" ></f:link>
	<script type="text/javascript"	src="${ctx }/js/tree/jquery.ztree.js"></script>
	<script type="text/javascript">
		//菜单
		var menu;
		//左击的树节点
		var selectNode;
		//树节点是否可点击
		var treeNodelickAble=true; 
		//当前访问
		var returnUrl="${ctx}/platform/system/position/list.ht";
		$(function()
		{
			loadTree();
			layout();
			menu();
		});
		//菜单
		function menu(){
			
			menu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
	        [
	        { text: '增加', click: addNode },
	        { text: '编辑', click: editNode  },
	        { text: '删除', click: delNode },
	        { text: '人员设置', click: users },
	        { text: '刷新', click: refreshNode }
	        ]
	        });
		};
		//刷新
		function refreshNode(){
			var selectNode=getSelectNode();
			reAsyncChild(selectNode);
		};
		//布局
		function layout(){
			$("#layout").ligerLayout( {
				leftWidth : 210,
				height:"98%",
				onHeightChanged: heightChanged,
				allowLeftResize:false
			});
			//取得layout的高度
	        var height = $(".l-layout-center").height();
	        $("#positionTree").height(height-60);
		};
		//布局大小改变的时候通知tab，面板改变大小
	    function heightChanged(options){
	     	$("#positionTree").height(options.middleHeight - 60);
	    };

		//树
		var positionTree;
		//加载树
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
						rootPId: <%=Position.ROOT_PID%>
					}
				},
				view: {
					showIcon: false,
					selectedMulti: false
				},
				edit: {
					drag: {
						prev: dropPrev,
						inner: dropInner,
						next: dropNext
					},
					enable: true,
					showRemoveBtn: false,
					showRenameBtn: false
				},
				async: {
					enable: true,
					url:"${ctx}/platform/system/position/getChildTreeData.ht",
					autoParam:["posId","parentId"],
					dataFilter: filter
				},
				callback:{
					onClick: zTreeOnLeftClick,
					onRightClick: zTreeOnRightClick,
					beforeClick:zTreeBeforeClick,
					beforeDrop:beforeDrop,
					onDrop: onDrop,
					onAsyncSuccess: zTreeOnAsyncSuccess
				}
				
			};
			positionTree=$.fn.zTree.init($("#positionTree"), setting);
			treeNodelickAble=true;
			
			
		};
	
		//刷新节点
		function reAsyncChild(targetNode){
			var posId=targetNode.posId;
			if(posId==<%=Position.ROOT_ID%>){
				loadTree();
			}else{
				positionTree = $.fn.zTree.getZTreeObj("positionTree");
				positionTree.reAsyncChildNodes(targetNode, "refresh", true);
			}
			treeNodelickAble=true;
		};
		//判断是否为子结点,以改变图标	
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			if(treeNode){
		  	 var children=treeNode.children;
			  	 if(children.length==0){
			  		treeNode.isParent=true;
			  		positionTree = $.fn.zTree.getZTreeObj("positionTree");
			  		if(positionTree)//不为空时更新节点
			  		positionTree.updateNode(treeNode);
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
		//向前拖
		function dropPrev(treeId,curDragNodes,treeNode) {
			var pNode =treeNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== treeNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		};
		//向内拖
		function dropInner(treeId,curDragNodes,treeNode) {
			if (treeNode && treeNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					if (!treeNode && curDragNodes[i].dropRoot === false) {
						return false;
					} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== treeNode && curDragNodes[i].getParentNode().childOuter === false) {
						return false;
					}
				}
			}
			return true;
		};
		//向后拖
		function dropNext(treeId,curDragNodes,treeNode) {
			var pNode =treeNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== treeNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		};
		//拖放 前准备
		function beforeDrop(treeId, treeNodes, targetNode, moveType) {
			if (!treeNodes) return false;
			else{
				var drop=true;
				$.each(treeNodes,function(i,n){
					if(n.parentId==targetNode.posId){
						drop=false;
					}
				});
				return drop;
			}
		};
		//拖放 后动作
		function onDrop(event, treeId, treeNodes, targetNode, moveType) {
			if(targetNode){
				var targetId=targetNode.posId;
				var originalIds="";
				$.each(treeNodes,function(i,n){
					originalIds+=n.posId+",";
				});
				if(originalIds.length>1){
					originalIds=originalIds.substring(0,originalIds.length-1);
					$.ajax({
						type: 'POST',
						url:"${ctx}/platform/system/position/move.ht?targetId="+targetId+"&originalIds="+originalIds,
						success: function(result){
							reAsyncChild(targetNode);
						}
					});
				}
			}
		}	
		//左击前
		function zTreeBeforeClick(treeId, treeNode, clickFlag){
			return treeNodelickAble;
		};
		//保存排序后的顺序
		function sortPos(targetNode,posIds){
			$.ajax({
				type: 'POST',
				url:"${ctx}/platform/system/position/sort.ht?posIds="+posIds,
				success: function(result){
					reAsyncChild(targetNode);
				}
			});
		};
		/**
		*显示
		*/
		var newCount = 1;
		function addHoverDom(treeId, treeNode) {
			//前结点
			var netxNode=treeNode.getNextNode();
			//后结点
			var preNode=treeNode.getPreNode();
			var sObj = $("#" + treeNode.tId + "_span");
			//向上
			if(preNode){
				if ($("#upBtn_"+treeNode.id).length>0) return;
		
				var upStr = "<button  type='button' class='link-sortUp' id='upBtn_" + treeNode.id	+ "' title='向前' ></button>";
				sObj.append(upStr);
		
				var upBtn_ = $("#upBtn_"+treeNode.id);
				if (upBtn_) upBtn_.bind("click", function(){
					treeNodelickAble=false;
					//取前结点
					var preNode = treeNode.getPreNode();
					//如果前结点存在
					if(preNode!=null){
						//前后结点置换
						var thisposId= treeNode.posId;
						var preposId= preNode.posId;
						treeNode.posId=preposId;
						preNode.posId =thisposId;
						//取当下同级结点所有ID
						var parentNode=treeNode.getParentNode();
						var children=parentNode.children;
						var posIds="";
						$.each( children, function(i, c){
							posIds+=c.posId+",";	 
						});
						if(posIds.length>1){
							posIds=posIds.substring(0,posIds.length-1);
							sortPos(parentNode,posIds);
						}
					}
				});
			}
			//向下
			if(netxNode){
				if ($("#downBtn_"+treeNode.id).length>0) return;
				var downStr = "<button  type='button' class='link-sortDown' id='downBtn_" + treeNode.id	+ "' title='向后' ></button>";
				sObj.append(downStr);
		
				var downBtn_ = $("#downBtn_"+treeNode.id);
				if (downBtn_) downBtn_.bind("click", function(){
					treeNodelickAble=false;
					//取后结点
					var nextNode = treeNode.getNextNode();
					//如果前结点存在
					if(nextNode!=null){
						//前后结点置换
						var thisposId= treeNode.posId;
						var nextposId= nextNode.posId;
						treeNode.posId =nextposId;
						nextNode.posId =thisposId;
						//取当下同级结点所有ID
						var parentNode=treeNode.getParentNode();
						var children=parentNode.children;
						var posIds="";
						$.each( children, function(i, c){
							posIds+=c.posId+",";	 
						});
						if(posIds.length>1){
							posIds=posIds.substring(0,posIds.length-1);
							//保存排序后的顺序
							sortPos(parentNode,posIds);
						}
					}
				});
			}
			//最上
			if(preNode){
			if ($("#topBtn_"+treeNode.id).length>0) return;
				var topBtnStr = "<button  type='button' class='link-sortTop' id='topBtn_" + treeNode.id	+ "' title='最前' ></button>";
				sObj.append(topBtnStr);
				var topBtn_ = $("#topBtn_"+treeNode.id);
				if (topBtn_) topBtn_.bind("click", function(){
					treeNodelickAble=false;
					//取父结点
					var parentNode = treeNode.getParentNode();
					//如果父结点存在
					if(parentNode!=null){
						var children=parentNode.children;
						var typeIds=treeNode.posId+",";
						$.each( children, function(i, c){
							if(c.posId!=treeNode.posId)
							typeIds+=c.posId+",";	 
						});
						if(typeIds.length>1){
							typeIds=typeIds.substring(0,typeIds.length-1);
							sortPos(parentNode,typeIds);
						}
					}
				});
			}
			//最下
			if(netxNode){
				if ($("#bottomBtn_"+treeNode.id).length>0) return;
				var bottomBtnStr = "<button  type='button' class='link-sortBottom' id='bottomBtn_" + treeNode.id	+ "' title='最后' ></button>";
				sObj.append(bottomBtnStr);
				var bottomBtn_ = $("#bottomBtn_"+treeNode.id);
				if (bottomBtn_) bottomBtn_.bind("click", function(){
					treeNodelickAble=false;
					//取父结点
					var parentNode = treeNode.getParentNode();
					//如果父结点存在
					if(parentNode!=null){
						var children=parentNode.children;
						var typeIds="";
						$.each( children, function(i, c){
							if(c.posId!=treeNode.posId)
							typeIds+=c.posId+",";	 
						});
						
						typeIds+=treeNode.posId+",";
						if(typeIds.length>1){
							typeIds=typeIds.substring(0,typeIds.length-1);
							sortPos(parentNode,typeIds);
						}
					}
				});
			}
		};
		/**
		*隐藏
		*/
		function removeHoverDom(treeId, treeNode) {
			$("#upBtn_"+treeNode.id).unbind().remove();
			$("#downBtn_"+treeNode.id).unbind().remove();
			$("#topBtn_"+treeNode.id).unbind().remove();
			$("#bottomBtn_"+treeNode.id).unbind().remove();
		};
		//左击
		function zTreeOnLeftClick(event, treeId, treeNode){
			returnUrl="${ctx}/platform/system/position/list.ht?Q_nodePath_S="+treeNode.nodePath+"&parentId="+treeNode.posId;
			selectNode = treeNode;
			$("#listFrame").attr("src",returnUrl);
		};
		/**
		 * 树右击事件
		 */
		function zTreeOnRightClick(e, treeId, treeNode) {
			if (treeNode&&!treeNode.notRight) {
				positionTree.selectNode(treeNode);
				 menu.show({ top: e.pageY, left: e.pageX });
			}
		};
		//展开收起
		function treeExpandAll(type){
			positionTree = $.fn.zTree.getZTreeObj("positionTree");
			positionTree.expandAll(type);
		};
		//异步加载展开
		function treeExpand() {
			posTree = $.fn.zTree.getZTreeObj("positionTree");
			var treeNodes = posTree.transformToArray(posTree.getNodes());
			for(var i=0;i<treeNodes.length;i++){
				if(treeNodes[i].children){
					posTree.expandNode(treeNodes[i], true, false, false);
				}
			}
		};
		//添加岗位
		function addNode(){
			
			$("#listFrame").attr("src","${ctx}/platform/system/position/edit.ht?parentId="+getSelectNode().posId+"&returnUrl="+returnUrl);
		};
		//编辑岗位
		function editNode(){
			var selectNode=getSelectNode();
			var posId=selectNode.posId;
			if(posId==<%=Position.ROOT_ID%>){$.ligerMsg.info('该节点为系统节点 ,不充许该操作');return;}
			$("#listFrame").attr("src","${ctx}/platform/system/position/edit.ht?posId="+posId+"&returnUrl="+returnUrl);
		};
		//删除岗位
		function delNode(){
			var selectNode=getSelectNode();
			var posId=selectNode.posId;
			if(posId==<%=Position.ROOT_ID%>){$.ligerMsg.info('该节点为系统节点 ,不充许该操作');return;}
		 	var callback = function(rtn) {
				if(rtn){
					$.ajax({
						type: 'POST',
						url:"${ctx}/platform/system/position/del.ht?posId="+posId+"&returnUrl="+returnUrl,
						success: function(result){
							posTree = $.fn.zTree.getZTreeObj("positionTree");
							posTree.removeNode(selectNode);
						}
					});
				}
			};
			$.ligerDialog.confirm('确认删除吗？','提示信息',callback);
		};
		//人员设置
		function users(){
			var selectNode=getSelectNode();
			var posId=selectNode.posId;
			if(posId==<%=Position.ROOT_ID%>){$.ligerMsg.info('该节点为系统节点 ,不允许该操作');return;}
			$("#listFrame").attr("src","${ctx}/platform/system/userPosition/edit.ht?posId="+posId+"&returnUrl="+returnUrl);
		};
		//选择岗位
		function getSelectNode()
		{
			positionTree = $.fn.zTree.getZTreeObj("positionTree");
			var nodes  = positionTree.getSelectedNodes();
			var node   = nodes[0];
			return node;
		}
	</script>

<style type="text/css">
html,body{ padding:0px; margin:0; width:100%;height:100%;overflow: hidden;}
.tree-title{overflow:hidden;width:8000px;}
.ztree{overflow: auto; padding: 0px;}
</style>

</head>

<body>

<div id="layout">
	<div position="left" title="岗位管理">
		<div class="tree-toolbar">
			<span class="toolBar">
				<div class="group" ><a class="link reload" id="treeFresh" href="javascript:loadTree();"></a></div>
				<div class="l-bar-separator"></div>
				<div class="group" ><a class="link expand" id="treeExpandAll" href="javascript:treeExpand()"></a></div>
				<div class="l-bar-separator"></div>
				<div class="group" ><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)"></a></div>
			</span>
		</div>
		<div id="positionTree" class="ztree" style=" clear: left;"></div>
	</div>
	<div position="center">
		<iframe id="listFrame" src="${ctx}/platform/system/position/list.ht" frameborder="no" width="100%" height="100%"></iframe>
	</div>
</div>
</body>
</html>

