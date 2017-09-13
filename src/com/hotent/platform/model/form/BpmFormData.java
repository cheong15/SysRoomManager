package com.hotent.platform.model.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 表单数据。
 * 将json转换为，可以使用的数据。
 * @author ray
 *
 */
public class BpmFormData {
	
	public BpmFormData(){}
	
	/**
	 * 根据formTable构造BpmFormData对象。
	 * @param formTable
	 */
	public BpmFormData(BpmFormTable formTable){
		this.tableId=formTable.getTableId();
		this.tableName=formTable.getTableName();
		this.dsAlias=formTable.getDsAlias();
		this.isExternal=formTable.isExtTable();
		this.bpmFormTable=formTable;
	}
	
	/**
	 * 主表ID
	 */
	private long tableId=0;
	
	/**
	 * 主表名
	 */
	private String tableName="";
	
	/**
	 * 是否外部表。
	 */
	private boolean isExternal=false;
	
	private BpmFormTable bpmFormTable;
	
	
	/**
	 * 主键值
	 */
	private PkValue pkValue;
	
	public PkValue getPkValue() {
		return pkValue;
	}
	
	public void setPkValue(PkValue pkValue) {
		this.pkValue = pkValue;
	}
	/**
	 * 数据源名称
	 */
	private String dsAlias;
	
	/**
	 * 子表数据
	 */
	private List<SubTable> subTableList=new ArrayList<SubTable>();

	/**
	 * 主表字段
	 */
	private Map<String, Object> mainFields=new HashMap<String, Object>();
	
	/**
	 * 意见
	 */
	private Map<String, String> options=new HashMap<String, String>();
	
	/**
	 * 流程变量数据
	 */
	private Map<String, Object> variables=new HashMap<String, Object>();


	public long getTableId() {
		return tableId;
	}


	public void setTableId(long tableId) {
		this.tableId = tableId;
	}


	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public List<SubTable> getSubTableList() {
		return subTableList;
	}
	
	public Map<String,SubTable> getSubTableMap(){
		Map<String, SubTable> map=new HashMap<String, SubTable>();
		for(Iterator<SubTable> it=subTableList.iterator();it.hasNext();){
			SubTable tb=it.next();
			map.put(tb.getTableName().toLowerCase(), tb);
		}
		return map;
	}


	public void setSubTableList(List<SubTable> subTableList) {
		this.subTableList = subTableList;
	}


	public Map<String, Object> getMainFields() {
		return mainFields;
	}
	
	


	public void setMainFields(Map<String, Object> mainFields) {
		this.mainFields = mainFields;
	}


	public Map<String, String> getOptions() {
		return options;
	}


	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	
	/**
	 * 添加意见。
	 * @param formName
	 * @param value
	 */
	public void addOpinion(String formName,String value){
		this.options.put(formName, value);
	}
	
	/**
	 * 添加子表。
	 * @param table
	 */
	public void addSubTable(SubTable table){
		this.subTableList.add(table);
	}
	
	/**
	 * 添加主键字段。
	 * @param key
	 * @param obj
	 */
	public void addMainFields(String key,Object obj){
		this.mainFields.put(key, obj);
	}
	
	public void addMainFields(Map<String, Object> map){
		this.mainFields.putAll(map);
	}
	
	/**
	 * 取得除主键字段的字段数据。
	 * @return
	 */
	public Map<String, Object> getMainCommonFields()
	{
		Map<String, Object> map=new HashMap<String, Object>();
		map.putAll(this.mainFields);
		map.remove(this.pkValue.getName());
		return map;
	}
	
	/**
	 * 获取流程变量。
	 * @return
	 */
	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public String getDsAlias() {
		return dsAlias;
	}

	public void setDsAlias(String dsAlias) {
		this.dsAlias = dsAlias;
	}

	
	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}
	
	public BpmFormTable getBpmFormTable() {
		return bpmFormTable;
	}

	public void setBpmFormTable(BpmFormTable bpmFormTable) {
		this.bpmFormTable = bpmFormTable;
	}


}
