package com.hotent.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {
 
	
	private static final Log logger = LogFactory.getLog(DateUtil.class);

	/**
	 * 设置当前时间为当天的最初时间（即00时00分00秒）
	 * 
	 * @param cal
	 * @return
	 */
	public static Calendar setStartDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal;
	}

	public static Calendar setEndDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal;
	}

	/**
	 * 把源日历的年月日设置到目标日历对象中
	 * @param destCal
	 * @param sourceCal
	 */
	public static void copyYearMonthDay(Calendar destCal,Calendar sourceCal){
		destCal.set(Calendar.YEAR, sourceCal.get(Calendar.YEAR));
		destCal.set(Calendar.MONTH, sourceCal.get(Calendar.MONTH));
		destCal.set(Calendar.DAY_OF_MONTH, sourceCal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 格式化日期为
	 * 
	 * @return
	 */
	public static String formatEnDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

		return sdf.format(date).replaceAll("上午", "AM").replaceAll("下午", "PM");
	}

	public static Date parseDate(String dateString) {
		Date date = null;
		try {
			date = DateUtils.parseDate(dateString, new String[]{StringPool.DATE_FORMAT_DATETIME,StringPool.DATE_FORMAT_DATE});
		} catch (Exception ex) {
			logger.error("Pase the Date(" + dateString + ") occur errors:"
					+ ex.getMessage());
		}
		return date;
	}
	
	/**
	 * 日期加一天
	 * @param date
	 * @return
	 */
	public static String addOneDay(String date){
		DateFormat format=new SimpleDateFormat(StringPool.DATE_FORMAT_DATE);
		Calendar calendar=Calendar.getInstance();
		try {
			Date dd=format.parse(date);
			calendar.setTime(dd);
			calendar.add(Calendar.DAY_OF_MONTH,1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String tmpDate = format.format(calendar.getTime());
		return tmpDate.substring(5,7)+"/"+tmpDate.substring(8,10)
				+"/"+tmpDate.substring(0,4);
	}
	
	/**
	 * 加一小时
	 * @param date
	 * @return
	 */
	public static String addOneHour(String date){
		
		String amPm = date.substring(20,22);
		
		DateFormat format=new SimpleDateFormat(StringPool.DATE_FORMAT_DATETIME);
		Calendar calendar=Calendar.getInstance();

		int hour = Integer.parseInt(date.substring(11,13));
		try {
			if(amPm.equals("PM")){
				hour+=12;
			}
			date = date.substring(0,11)+(hour>=10?hour:"0"+hour)+date.substring(13,19);
			Date dd=format.parse(date);
			calendar.setTime(dd);
			calendar.add(Calendar.HOUR_OF_DAY,1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String tmpDate = format.format(calendar.getTime());
		
		hour = Integer.parseInt(tmpDate.substring(11,13));
		amPm = hour>=12&&hour!=0?"PM":"AM";
		if(amPm.equals("PM")){
			hour-=12;
		}
		tmpDate = tmpDate.substring(5,7)+"/"+tmpDate.substring(8,10)+"/"+tmpDate.substring(0,4)
			+" "+(hour>=10?hour:"0"+hour)+tmpDate.substring(13, tmpDate.length())+" "+amPm;
		
		return tmpDate;
	}
	
	/**
	 * 标准时间格式转为时间字符格式
	 * @param timeStr eg:Mon Feb 06 00:00:00 CST 2012
	 * @return
	 */
	public static String timeStrToDateStr(String timeStr){

		String dateStr=timeStr.substring(24, 28)+"-";
		
		String mon = timeStr.substring(4, 7);
		if(mon.equals("Jan")){
			dateStr+="01";
		}else if(mon.equals("Feb")){
			dateStr+="02";
		}else if(mon.equals("Mar")){
			dateStr+="03";
		}else if(mon.equals("Apr")){
			dateStr+="04";
		}else if(mon.equals("May")){
			dateStr+="05";
		}else if(mon.equals("Jun")){
			dateStr+="06";
		}else if(mon.equals("Jul")){
			dateStr+="07";
		}else if(mon.equals("Aug")){
			dateStr+="08";
		}else if(mon.equals("Sep")){
			dateStr+="09";
		}else if(mon.equals("Oct")){
			dateStr+="10";
		}else if(mon.equals("Nov")){
			dateStr+="11";
		}else if(mon.equals("Dec")){
			dateStr+="12";
		}
		
		dateStr+="-"+timeStr.substring(8, 10);
		
		return dateStr;
	}
	
	
	
	
	
	/**
	 * 根据日期得到星期多余天数
	 * @param sDate
	 * @return
	 */
	public static int getExtraDayOfWeek(String sDate){
		try{
			    SimpleDateFormat format = new SimpleDateFormat(StringPool.DATE_FORMAT_DATE);
				Date date=format.parse(sDate);
				String weekday = date.toString().substring(0,3);
				if(weekday.equals("Mon")){
					return 1;
				}else if(weekday.equals("Tue")){
					return 2;
				}else if(weekday.equals("Wed")){
					return 3;
				}else if(weekday.equals("Thu")){
					return 4;
				}else if(weekday.equals("Fri")){
					return 5;
				}else if(weekday.equals("Sat")){
					return 6;
				}else{
					return 0;
				}
		    
	    }catch(Exception ex){
		   return 0;
	    }
	}
	
	/**
	 * 根据日期得到星期多余天数
	 * @param sDate
	 * @return
	 */
	public static String getDateWeekDay(String sDate){
		try{
		    SimpleDateFormat format = new SimpleDateFormat(StringPool.DATE_FORMAT_DATE);
			Date date=format.parse(sDate);
			String weekday = date.toString().substring(0,3);
//			format.format(date)+" "+ 
			return weekday;
			
	    }catch(Exception ex){
		   return "";
	    }
	}
	
	/**
	 * 取得上下五年
	 * @param cal
	 * @return
	 */
	public static List<String> getUpDownFiveYear(Calendar cal){
		List<String> yearlist = new ArrayList<String>();
		
		int curyear = cal.get(Calendar.YEAR);
		yearlist.add(String.valueOf(curyear-2));
		yearlist.add(String.valueOf(curyear-1));
		yearlist.add(String.valueOf(curyear));
		yearlist.add(String.valueOf(curyear+1));
		yearlist.add(String.valueOf(curyear+2));
		
		return yearlist;
	}
	
	/**
	 * 取得12个月
	 * @param cal
	 * @return
	 */
	public static List<String> getTwelveMonth(){
		List<String> monthlist = new ArrayList<String>();
		
		for(int idx=1;idx<=12;idx++){
			monthlist.add(String.valueOf(idx));
		}
		
		return monthlist;
	}
	
	/**
	 * 得到两日期间所有日期
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static String[] getDaysBetweenDate(String startTime, String endTime){

		String[] dateArr = null;
		try {
			
			String stime = timeStrToDateStr(startTime);
			String etime = timeStrToDateStr(endTime);
			
			//日期相减算出秒的算法
			Date date1 = new SimpleDateFormat(StringPool.DATE_FORMAT_DATE).parse(stime);
			Date date2 = new SimpleDateFormat(StringPool.DATE_FORMAT_DATE).parse(etime);
			
			long day = (date1.getTime()-date2.getTime())/(24*60*60*1000)>0 ? (date1.getTime()-date2.getTime())/(24*60*60*1000):
			(date2.getTime()-date1.getTime())/(24*60*60*1000);
			
			dateArr = new String[Integer.valueOf(String.valueOf(day+1))];
			for(int idx=0;idx<dateArr.length;idx++){
				if(idx==0){
					dateArr[idx] = stime;
				}else{
					stime = addOneDay(stime);
					stime = stime.substring(6,10)+"-"+stime.substring(0,2)
							+"-"+stime.substring(3,5);
					dateArr[idx] = stime;
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateArr;
	}
	
	
}
