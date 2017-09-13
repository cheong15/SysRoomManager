/**
 * 统计函数
 * @function
 * @name baidu.editor.execCommands
 * @param    {String}    cmdName     cmdName="insertfunction"
 */
UE.commands['insertfunction'] = {	
	execCommand : function(cmdName) {
	},
	queryCommandState : function() {
		var el = this.selection.getRange().getClosedNode();
		return el ? 0 : -1;
	}
}