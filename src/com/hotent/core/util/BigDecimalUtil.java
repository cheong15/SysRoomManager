package com.hotent.core.util;

import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * 数字处理工具类
 * @author xiao
 *
 */
public class BigDecimalUtil {
	
	
	
	/**
	 * 浮点型转换成字符串
	 * @return
	 */
	public static String doubleToString(XSSFCell cell){
		DecimalFormat df = new DecimalFormat("0");  
		return df.format(cell);
	}
	
	public static String doubleToString2(HSSFCell cell){
		DecimalFormat df = new DecimalFormat("0");
		return df.format(cell);
	}
}
