function getNowFormatDate(){
    var day = new Date();
    var Year = 0;
    var Month = 0;
    var Day = 0;
    var CurrentDate = "";
    Year= day.getFullYear();//支持IE和火狐浏览器.
    Month= day.getMonth()+1;
    Day = day.getDate();
    CurrentDate += Year+"-";
    if (Month >= 10 ){
     CurrentDate += Month+"-";
    }
    else{
     CurrentDate += "0" + Month+"-";
    }
    
    if (Day >= 10 ){
     CurrentDate += Day ;
    }
    else{
     CurrentDate += "0" + Day ;
    }
    return CurrentDate;
 }
function checkDate(){
	//var startdd =$("input[name='entryday']").val();
	var startdd=$("input[name='entryday']").val().substring(0,10);
	if(startdd==null||startdd==""){
		return;
	}
	var enddd= getNowFormatDate();
	var monthbetween=getMonthBetween(startdd, enddd);
	var work_year=0;
	var work_month=0;
	if(monthbetween>=12){
		work_year=parseInt( monthbetween/12);
		work_month=monthbetween%12;
	}else{
		work_month=monthbetween;
	}
	$("#work_year").val(work_year);
	$("#work_month").val(work_month);
}
//得到两个日期月数
function getMonthBetween(startDate,endDate){
	var year=0;
	var month=0;
	var num =0;
	var sdate=new Date(startDate.replace(/-/g,'/'));
	var edate=new Date(endDate.replace(/-/g,'/'));
	year=edate.getFullYear()-sdate.getFullYear();
	num =year*12;
	month=edate.getMonth()-sdate.getMonth();
	num+=month;
	var day=edate.getDate()-sdate.getDate();
	if(day>0){
		num+=1;
	}
	return num;
}
function showWorkYear(){
	var startdd=$("#entryday").val();
	var date=new Date(Date.parse(startdd.replace('年','-').replace('月','-').replace('日','')));
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	if(month<10){
		month="0"+month;
	}
	var date_str="";
	var day=date.getDate();
	startdd=year+"-"+month+"-"+day;
	var enddd= getNowFormatDate();
	var monthbetween=getMonthBetween(startdd, enddd);
	var work_year=0;
	var work_month=0;
	if(monthbetween>=12){
		work_year=parseInt( monthbetween/12);
		work_month=monthbetween%12;
	}else{
		work_month=monthbetween;
	}
	if(work_year>0){
		if(work_year==1){
			date_str=work_year+" year";
		}else{
			date_str=work_year+" years";
		}
	}else {
		if(work_month>=1){
			date_str=work_month+" months";
		}else{
			date_str=work_month+" month";
		}
	}
	$("#wordyear").text(date_str);
}
function showWorkTime(){
	var workyear=$("#workyear").val();
	var workmonth=$("#workmonth").val();
	var worktime=null;
	if(workyear==""||workyear=="0")
	{
		if(workmonth==1){
			worktime=workmonth+" month";
		}else{
			worktime=workmonth+" months";
		}
	}
	else
	{
		if(workyear==1){
			worktime=workyear+" year";
		}else{
			worktime=workyear+" years";
		}	
	}
	$("#wordyear").text(worktime);
}