package com.hotent.core.excel.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.core.util.DateFormatUtil;

/*
 * Excel文件读取类，读取Excel表格记录为JAVA对像
 * 
 * @author zxh
 * 
 */
public class ExcelReader {

	private ExcelReaderConfig excelReaderConfig;
	/**
	 * 创建文件输入流
	 */
	private BufferedReader reader = null;
	/**
	 * 文件二进制输入流
	 */
	private InputStream is = null;
	/**
	 * 当前工作表 sheet
	 */
	private int currSheet;
	/**
	 * 当前位置
	 */
	private int currPosittion;
	/**
	 * 工作表sheet的数量 *
	 */
	private int numOfSheets;
	/**
	 * HSSFWordbook
	 */
	private HSSFWorkbook workbook = null;

	/**
	 * 由文件输入流创建初始化一个ExcelReader
	 * 
	 * @param inputfile
	 *            文件输入流
	 * @throws IOException
	 * @throws Exception
	 */
	private void initExcelReader(InputStream inputFile) throws IOException,
			Exception {
		if (inputFile == null)
			throw new IOException("文件输入流为空");
		// 设置开始行
		this.currPosittion = 0;
		// 设置当前位置为0
		this.currSheet = 0;

		// 创建文件输入流
		this.is = inputFile;
		// 如果是Excel文件则创建BufferedReader读取
		this.workbook = new HSSFWorkbook(this.is);
		// 设置工作表Sheet数
		this.numOfSheets = this.workbook.getNumberOfSheets();
	}

	 /**
     * 读取Excel表格表头的内容
     * @return String[] 表头内容的数组
     */
	private String[] readExcelTitle() {
    	HSSFSheet sheet = this.getCurrSheet();
    	HSSFRow  row = sheet.getRow(0);
    	if(row == null)
    		return null; 	
        // 标题总列数
    	int	colNum = row.getLastCellNum();
        
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell(i));
        }
        return title;
    }


	/**
	 * 读到当前Sheet的数据
	 * 
	 * @return
	 * @throws Exception
	 */
    private List<DataEntity> readSheet(){
		// 根据currSheet值获得当前的工作表Sheet
		HSSFSheet sheet = this.getCurrSheet();
		int lastRowNum =  sheet.getLastRowNum();
		// 判断当前行是否到当前工作表sheet的结尾
		List<DataEntity> dataEntityList =  new ArrayList<DataEntity>();
		for (int i = 1; i <= lastRowNum; i++) {
			DataEntity dataEntity  = new DataEntity();
			
			List<FieldEntity> fieldEntityList  =  this.getLine(sheet, i);
			if(fieldEntityList != null){
				FieldEntity fieldEntity = getFieldEntityKey(fieldEntityList);
				dataEntity.setPkName(fieldEntity.getName());
				dataEntity.setPkVal(fieldEntity.getValue());
				dataEntity.setFieldEntityList(fieldEntityList);
				
			}
			dataEntityList.add(dataEntity);
		}
		return dataEntityList;
	}

	private FieldEntity getFieldEntityKey(List<FieldEntity> fieldEntityList) {
		for (FieldEntity fieldEntity : fieldEntityList) {
			if(fieldEntity.getIsKey().shortValue() == FieldEntity.IS_KEY.shortValue())
				return fieldEntity;
		}
		return null;
	}

	/**
	 * 返回工作表sheet的一行数据
	 * 
	 * @param sheet
	 *            工作表
	 * @param row
	 *            行
	 * @return
	 */
	private List<FieldEntity> getLine(HSSFSheet sheet, int row) {
		// 根据行数取得sheet的一行
		HSSFRow rowline = sheet.getRow(row);
		if (rowline == null) 
		return null;
	
		// 获到当前行的列数
		int filledColumns = rowline.getLastCellNum();
		HSSFCell cell = null;
		// 开始读取的列，从第几列开始读。
		int colStart = this.excelReaderConfig.getColStartPosittion();
		List<FieldEntity> list  = new ArrayList<FieldEntity>();
		// 遍历所有列
		for (int i = colStart; i < filledColumns; i++) {
			// 取得当前单元格
			cell = rowline.getCell(i);
			String column = this.excelReaderConfig.getColumns()[i-colStart];			
			String cellValue = getCellFormatValue(cell);
			FieldEntity  fieldEntity = new FieldEntity();
			fieldEntity.setName(column);
			fieldEntity.setValue(cellValue);
			fieldEntity.setIsKey(i == colStart?FieldEntity.IS_KEY:FieldEntity.NOT_KEY);
			list.add(fieldEntity);
		}
		if((filledColumns+colStart)<this.excelReaderConfig.getColumns().length){			
			for(int i=filledColumns+colStart;i<this.excelReaderConfig.getColumns().length;i++){
				String column = this.excelReaderConfig.getColumns()[i-colStart];			
				String cellValue = "";
				FieldEntity  fieldEntity = new FieldEntity();
				fieldEntity.setName(column);
				fieldEntity.setValue(cellValue);
				fieldEntity.setIsKey(FieldEntity.NOT_KEY);
				list.add(fieldEntity);
			}
		}
		// 返回该行的数据
		return list;
	}

	/**
	 * 取得当前行的值
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(HSSFCell cell) {
		if (cell == null)
			return "";
		String cellvalue = "";
		// 判断当前单元格的type
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			// /取得当前Cell的字符串
			cellvalue = cell.getRichStringCellValue().getString();
			break;

		// 如果当前Cell的type为NUMERIC或者_FORMULA
		case HSSFCell.CELL_TYPE_NUMERIC:
		case HSSFCell.CELL_TYPE_FORMULA:
			// 判断当前的Cell是否为Date
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 如果是在Date类型，则取得该Cell的Date值
				Date date = cell.getDateCellValue();
				cellvalue = DateFormatUtil.format(date);
			} else {
				// 如果是纯数字
				// 取得当前cell的数值
				//----------------该处修改2014-4-23-----------------------------------//
				DecimalFormat df = new DecimalFormat("0");
				cellvalue = df.format(cell.getNumericCellValue());
				//cellvalue = String.valueOf(cell.getNumericCellValue());
				//----------------------------------------------------//
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellvalue = String.valueOf(cell.getBooleanCellValue());
			break;
		default:
			cellvalue = "";
		}
		return cellvalue;
	}

	/**
	 * 关闭函数执行流的操作
	 */
	public void close() {
		// 如果is不为空，则关闭InputStream文件输入流
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				is = null;
				e.printStackTrace();
			}
		}
		// 如果reader不为空,则关闭BufferedReader文件输入流
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				reader = null;
				e.printStackTrace();
			}
		}
	}

	private HSSFSheet getCurrSheet() {
		return this.workbook.getSheetAt(this.currSheet);
	}

	
	public ExcelReaderConfig getExcelReaderConfig() {
		return excelReaderConfig;
	}

	public void setExcelReaderConfig(ExcelReaderConfig excelReaderConfig) {
		this.excelReaderConfig = excelReaderConfig;
	}
	
	public int getNumOfSheets() {
		return numOfSheets;
	}

	public void setNumOfSheets(int numOfSheets) {
		this.numOfSheets = numOfSheets;
	}

	public void setCurrSheet(int currSheet) {
		this.currSheet = currSheet;
	}
	
	public int getCurrPosittion() {
		return currPosittion;
	}

	public void setCurrPosittion(int currPosittion) {
		this.currPosittion = currPosittion;
	}

	/**
	 * 获取当前表
	 * @param config
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	private TableEntity getTableEntity(ExcelReaderConfig config,Short isMain) {
		HSSFSheet sheet = this.getCurrSheet();
		String [] columns = this.readExcelTitle();//获取表头
		if(columns == null)
			return null;
		config.setColumns(columns);
		this.setExcelReaderConfig(config);	
		//读取当前字段
		List<DataEntity> dataEntityList =  this.readSheet();
		
		TableEntity tableEntity = new TableEntity();
		tableEntity.setName(sheet.getSheetName()) ;
		tableEntity.setIsMain(isMain);
		tableEntity.setDataEntityList(dataEntityList);
		return tableEntity; 
	}
	
	
	/**
	 * 读取Excel
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public TableEntity readFile(InputStream input) throws Exception{
		//读入Excel文件
		this.initExcelReader(input);
		ExcelReaderConfig config = new ExcelReaderConfig();
		TableEntity  tableEntity = getTableEntity(config,TableEntity.IS_MAIN);//包含excl的列、数据
		
		int numOfSheets = this.getNumOfSheets();
		if(numOfSheets >0){
			List<TableEntity>  subList = new ArrayList<TableEntity>();
			for (int i = 1; i < numOfSheets; i++) {
				this.setCurrSheet(i);
				TableEntity  table = getTableEntity(config,TableEntity.NOT_MAIN);//获取当前excl的列、数据
				subList.add(table);
			}
			if(subList.size() >0 )
				tableEntity.setSubTableEntityList(subList);
		}
		//关闭流
		this.close();
		return tableEntity;
	}
	
	/**
	 * 读取Excel的第一个sheet
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public TableEntity readFile0(InputStream input) throws Exception{
		//读入Excel文件
		this.initExcelReader(input);
		ExcelReaderConfig config = new ExcelReaderConfig();
		TableEntity  tableEntity = getTableEntity(config,TableEntity.IS_MAIN);//包含excl的列、数据
		//关闭流
		this.close();
		return tableEntity;
	}

	public static void main(String[] args) throws Exception {

		ExcelReader excel = new ExcelReader();

		File file = new File("d:\\test2.xls"); // 把testEntity.xls文件复制到d:
		InputStream input = new FileInputStream(file);
	
		TableEntity excelEntity = excel.readFile(input);
		Logger logger = LoggerFactory.getLogger(ExcelReader.class);
		
		logger.info(excelEntity.toString());
		
	}
	

}
