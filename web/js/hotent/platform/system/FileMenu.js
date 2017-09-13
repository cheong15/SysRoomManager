FileMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
	};
	this.getMenu=function(treeNode,handler){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		
		var items=[];
		if(treeNode.userId==0){
			items.push({ text: $lang_js.globalType.menu.addType,key:'add', click: handler});
		}
		items.push({ text: $lang_js.globalType.menu.addMy,key:'addMy', click: handler});
		items.push({ text: $lang_js.globalType.menu.editType,key:'edit', click: handler});
		items.push({ text: $lang_js.globalType.menu.delType,key:'del', click: handler});
		this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 120, items:items});
		
		this.rootMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
	        [{ text: $lang_js.globalType.menu.addType,key:'add', click: handler  },
	         { text: $lang_js.globalType.menu.addMy,key:'addMy', click: handler  }]});
		if(isRoot==1){
			return this.rootMenu;
		}
		else{
			return this.menuMenu;
		}
	};
};
