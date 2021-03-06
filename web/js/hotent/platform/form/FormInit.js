$(function(){
	//初始化表单tab
	FormUtil.initTab();
	//初始化日期控件。
	FormUtil.initCalendar();
	//附件初始化
	AttachMent.init();
	//子表权限初始化
	SubtablePermission.init();
	//下拉框级联只读情况
	ReadOnlyQuery.init();
	
	FormUtil.InitMathfunction();
	//绑定对话框。
	FormUtil.initCommonDialog();
	FormUtil.initCommonQuery();
	
	FormUtil.initExtLink();
	
	QueryUI.init();
});

$(window).bind("load",function(){
	//Office控件初始化。
	OfficePlugin.init();
	//WebSign控件初始化。
	WebSignPlugin.init();
	//PictureShow 图片展示控件
	PictureShowPlugin.init();	
	//初始化第一个表单tab 中部分样式问题
	FormUtil.initTabStyle();
});