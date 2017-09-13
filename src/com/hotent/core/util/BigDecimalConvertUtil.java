package com.hotent.core.util;

import java.text.DecimalFormat;

/**
 * 数据处理类型
 * @author yadi_coco
 *
 */
public class BigDecimalConvertUtil {
	public static Double doubleToString(Double d,String format){
		DecimalFormat df = new DecimalFormat(format+"0");  
		String s=df.format(d);
		return Double.parseDouble(s);
	}
	/**
	 * 保留精度的String转换为double
	 * @param d
	 * @param format
	 * @return
	 */
	public static Double stringToDouble(String s,String format){
		Double d=Double.parseDouble(s);
		DecimalFormat df = new DecimalFormat(format+"0");  
		String s1=df.format(d);
		return Double.parseDouble(s1);
	}
	public static void main(String[] args) {
		String s="10.01";
		System.out.println(BigDecimalConvertUtil.stringToDouble(s,"0.00"));
	}
}
