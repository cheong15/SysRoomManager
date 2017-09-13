/**
 * ueditor扩展插件的配置
 */
var ExtendConf = UE.ExtendConf = {
	iframeUrlMap: {
		'input': '~/dialogs/extend/input/input.jsp',
		'opinion': '~/dialogs/extend/opinion/opinion.jsp',
		'insertfunction': '~/dialogs/extend/insertfunction/MathExpEditor.jsp',
		'importform': '~/dialogs/extend/import/importform.jsp',
		'customdialog': '~/dialogs/extend/dialog/dialog.jsp',
		'customquery':'~/dialogs/extend/query/query.jsp',
		'cascadequery':'~/dialogs/extend/query/queryMulVar.jsp',
		'numbervalidate':'~/dialogs/extend/validate/number.jsp',
		'international':'~/dialogs/extend/international/international.jsp'
	},
		
	btnCmds:['tableformat','choosetemplate','opinion','input','taskopinion','flowchart',
	         'insertfunction','cutsubtable','pastesubtable','customdialog','clearcell',
	         'insertrownext','insertcolnext','customquery','uncustomquery','numbervalidate','international','serialnum',
	         'hidedomain','textinput','textarea','checkbox','radioinput','selectinput',
             'dictionary','personpicker','departmentpicker','datepicker','officecontrol','subtable',
             'ckeditor','rolepicker','positionpicker','attachement','importform','exportform','pasteinput','cascadequery'],

	dialogBtns: ['importform', 'customquery','cascadequery','opinion','customdialog','input','insertfunction','numbervalidate','international','uncascadequery','setreadonly','delreadonly']
}