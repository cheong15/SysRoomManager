/**
 * 对象功能:自定义表单数据处理
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xwy
 * 创建时间:2011-12-22 11:07:56
 */
package com.hotent.platform.dao.form;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.hotent.core.bpm.util.BpmConst;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.core.table.ITableOperator;
import com.hotent.core.table.TableModel;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.TimeUtil;
import com.hotent.platform.dao.bpm.BpmSubtableRightsDao;
import com.hotent.platform.dao.system.SysDataSourceDao;
import com.hotent.platform.model.bpm.BpmSubtableRights;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.form.BpmFormData;
import com.hotent.platform.model.form.BpmFormField;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.form.PkValue;
import com.hotent.platform.model.form.SqlModel;
import com.hotent.platform.model.form.SubTable;
import com.hotent.platform.model.system.SysDataSource;
import com.hotent.platform.model.system.SysOrg;
import com.hotent.platform.model.util.FieldPool;
import com.hotent.platform.service.bpm.BpmBusLinkService;
import com.hotent.platform.service.form.BpmFormTableService;
import com.hotent.platform.service.form.FormDataUtil;

@Repository
public class BpmFormHandlerDao {

	private Log logger = LogFactory.getLog(BpmFormHandlerDao.class);

	@Resource
	private BpmFormTableDao bpmFormTableDao;
	@Resource
	private SysDataSourceDao sysDataSourceDao;
	@Resource
	private BpmFormTableService bpmFormTableService;
	@Resource
	private BpmSubtableRightsDao bpmSubtableRightsDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private BpmBusLinkService bpmBusLinkService;
	@Resource
	private ITableOperator tableOperator;
	/**
	 * 处理动态表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	public void  handFormData(BpmFormData bpmFormData,ProcessRun processRun,String nodeId) throws Exception {
		JdbcTemplate jdbcTemplate =null;
		BpmFormTable bpmFormTable=bpmFormData.getBpmFormTable();
		
		if(bpmFormTable!=null){
			if(bpmFormTable.getIsExternal() == BpmFormTable.EXTERNAL){
				if(bpmFormTable.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE)){
					jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
				}else{
					SysDataSource sysDataSource=sysDataSourceDao.getByAlias(bpmFormTable.getDsAlias());
					if(sysDataSource==null){
						logger.error("datasource" + bpmFormTable.getDsAlias() + " is null, mybe has deleted...");
						return ;
					}
					DriverManagerDataSource dataSource= sysDataSourceDao.getDriverMangerDataSourceByAlias(bpmFormTable.getDsAlias());
					jdbcTemplate=new JdbcTemplate(dataSource);
				}
			}else{
				jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			}
		}
		String actDefId="";
		if(processRun!=null){
			actDefId=processRun.getActDefId();
		}
		
		List<SqlModel> list = FormDataUtil.parseSql(bpmFormData,actDefId,nodeId);
		for (SqlModel sqlModel : list) {
			String sql = sqlModel.getSql();
			if (StringUtil.isEmpty(sql)) continue;
			Object[] obs = sqlModel.getValues();
			jdbcTemplate.update(sql, obs);
		}
		//处理关联数据。
		handlerBusLinkData(list,processRun);
	}
	
	/**
	 * 处理关联数据。
	 * @param list
	 * @param processRun
	 */
	private void handlerBusLinkData(List<SqlModel> list,ProcessRun processRun){
		for (SqlModel sqlModel : list) {
			String sql = sqlModel.getSql();
			if (StringUtil.isEmpty(sql) ) continue; 
			switch(sqlModel.getSqlType()){
				case SqlModel.SQLTYPE_INSERT:
					bpmBusLinkService.add(sqlModel.getPk(),processRun,sqlModel.getBpmFormTable() );
					break;
				case SqlModel.SQLTYPE_UPDATE:
					bpmBusLinkService.updBusLink(sqlModel.getPk(), sqlModel.getBpmFormTable());
					break;
				case SqlModel.SQLTYPE_DEL:
					bpmBusLinkService.delBusLink(sqlModel.getPk(), sqlModel.getBpmFormTable());
					break;
			}
		}
	}
	
	/**
	 * 保存表单数据
	 * @param bpmFormData
	 * @throws Exception
	 */
	public void handFormData(BpmFormData bpmFormData,ProcessRun processRun) throws Exception {
		handFormData( bpmFormData,processRun, "");
	}
	
	public void handFormData(BpmFormData bpmFormData) throws Exception {
		handFormData( bpmFormData,null, "");
	}

	/**
	 * 判断指定的表是否有数据。
	 * 
	 * @param tableName
	 *            数据库表名
	 */
	public boolean getHasData(String tableName) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		int rtn = jdbcTemplate.queryForInt("select count(*) from " + tableName);
		return rtn > 0;
	}
	
	/**
	 * 检查表是否存在，可以是该用户空间下的所有表
	 * @Methodname: tableExists
	 * @param tableName
	 * @return
	 *
	 */
	public boolean tableExists(String tableName){
//		StringBuffer sql  = new StringBuffer();
//		sql.append("select COUNT(1) from user_tables t where t.TABLE_NAME='").append(tableName.toUpperCase()).append("'");
//
//		return	jdbcTemplate.queryForInt(sql.toString())>0?true:false;
		return tableOperator.isTableExist(tableName);
	}

	/**
	 * 根据主键查询列表数据。
	 * 
	 * @param tableId
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	public BpmFormData getByKey(long tableId, String pkValue,boolean isHandleData) throws Exception {
		return getByKey(tableId, pkValue, null, null,isHandleData);
	}
	
	/**
	 * 根据流程定义id，节点id和表ID获取子表显示条件的SQL片段。
	 * @param tableId
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	private String getLimitSql(Long tableId, String actDefId,String nodeId){
		if(StringUtil.isEmpty(actDefId) || StringUtil.isEmpty(nodeId)) return "";
		String limitSql = "";
		
		BpmSubtableRights rule = bpmSubtableRightsDao.getByDefIdAndNodeId(actDefId, nodeId, tableId);
		
		if(rule==null) return "";
		int permissionType=rule.getPermissiontype().intValue();
		// 处理权限,生成sql约束片段.
		switch (permissionType) {
			case 0:// 简单配置:判断用户
				Long userId = ContextUtil.getCurrentUserId();
				limitSql = " and b.BUS_CREATOR_ID   = " + userId;
				break;
			case 1:// 简单配置:判断组织
				SysOrg org = ContextUtil.getCurrentOrg();
				if (org == null) {
					limitSql = " and 1 = 2";// 强制查询不到
				} else {
					limitSql = " and b.BUS_ORG_ID = " + org.getOrgId();
				}
				break;
			case 2:// 脚本
				GroovyScriptEngine scriptEngine = (GroovyScriptEngine) AppUtil.getBean(GroovyScriptEngine.class);
				limitSql = scriptEngine.executeString(rule.getPermissionseting(), new HashMap<String, Object>());// 不需流程变量入参
				break;
			default:
				break;
		}
		return limitSql;
		
	}

	/**
	 * 根据表ID，当前流程定义ID,流程节点ID查询业务数据。
	 * @param tableId		表ID
	 * @param pkValue		主键
	 * @param actDefId		流程定义ID
	 * @param nodeId		节点ID
	 * @return
	 * @throws Exception
	 */
	public BpmFormData getByKey(long tableId, String pkValue, String actDefId, String nodeId,boolean isHandleData) throws Exception {
		BpmFormTable mainFormTableDef=bpmFormTableService.getByTableId(tableId,1);
		// 获取jdbctemplate对象。
		JdbcTemplate jdbcTemplate =null;
		//外部数据源
		if(mainFormTableDef.isExtTable()){
			if(mainFormTableDef.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE)){
				jdbcTemplate=(JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			}else{
				DriverManagerDataSource dataSource=sysDataSourceDao.getDriverMangerDataSourceByAlias(mainFormTableDef.getDsAlias());
				jdbcTemplate=new JdbcTemplate(dataSource);
			}
		}else{
			jdbcTemplate=(JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		}
		
		String tableName = mainFormTableDef.getFactTableName();
		String pkField = mainFormTableDef.getPkField();
	
		PkValue pk = new PkValue(pkField, pkValue);
		// 取得主表的数据
		Map<String, Object> mainData = getByKey(jdbcTemplate,  pk, mainFormTableDef,isHandleData);

		// 取子表的数据
		BpmFormData bpmFormData = new BpmFormData(mainFormTableDef);
		List<BpmFormTable> tableList=mainFormTableDef.getSubTableList();
		for (BpmFormTable table : tableList) {
			SubTable subTable = new SubTable();
			//TableRelation tableRelation= table.getTableRelation();			
			String fk=table.getRelation();
			String subPk =table.getPkField();		
			List<Map<String, Object>> list = getByFk(table, pk.getValue().toString(), actDefId,nodeId,isHandleData);
			subTable.setTableName(table.getTableName().toLowerCase());
			subTable.setDataList(list);
			subTable.setFkName(fk);
			subTable.setPkName(subPk);
			bpmFormData.addSubTable(subTable);
		}

		bpmFormData.setTableId(tableId);
		bpmFormData.setTableName(tableName);
		bpmFormData.setPkValue(pk);
		bpmFormData.addMainFields(mainData);

		return bpmFormData;
	}

	/**
	 * 根据主键查询一条数据。
	 * 
	 * @param tableName
	 *            需要查询的表名
	 * @param pk
	 *            主键
	 * @param tableId
	 *            表ID
	 * @return
	 */
	public Map<String, Object> getByKey(JdbcTemplate jdbcTemplate,  PkValue pk, BpmFormTable bpmFormTable ,boolean isHandleData) {
		String sql = "select a.* from " + bpmFormTable.getFactTableName() + " a where " + pk.getName() + "=" + pk.getValue();
		List<BpmFormField> fieldList = bpmFormTable.getFieldList();
		Map<String, BpmFormField> fieldMap = convertToMap(fieldList);
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
			if(isHandleData){
				map = handMap(bpmFormTable, fieldMap, map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			map = new HashMap<String, Object>();
		}
		return map;
	}

	/**
	 * 根据表名和主键获取一行数据。
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public Map<String, Object> getByKey(String tableName, String pk) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		String sql = "select a.* from " + tableName + " a where id="  + pk;
		
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
		} catch (Exception ex) {
			map = new HashMap<String, Object>();
		}
		return map;
	}

	/**
	 * 将list转换为map对象。
	 * 
	 * @param fieldList
	 * @return
	 */
	private Map<String, BpmFormField> convertToMap(List<BpmFormField> fieldList) {
		Map<String, BpmFormField> map = new HashMap<String, BpmFormField>();
		for (BpmFormField field : fieldList) {
			String fieldName = field.getFieldName().toLowerCase();
			map.put(fieldName, field);
		}
		return map;
	}

	/**
	 * 根据外键查询列表数据。
	 * 
	 * @param tableName
	 * @param fkValue
	 * @param tableId
	 * @return
	 */
	public List<Map<String, Object>> getByFk(BpmFormTable table, String fkValue,boolean isHandleData) {
		return getByFk(table, fkValue, null, null,isHandleData);
	}

	/**
	 * 根据外键查询列表数据。
	 * @param table
	 * @param fkValue
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<Map<String, Object>> getByFk(BpmFormTable table, String fkValue, String actDefId,String nodeId,boolean isHandleData) {
		String subTableName = table.getFactTableName();
		String fkField=table.getRelation();
		String pkField=table.getPkField();
		short keyDataType= table.getKeyDataType();
		List<BpmFormField> fieldList=table.getFieldList();
		
		Long tableId=table.getTableId();
		JdbcTemplate jdbcTemplate=null;
		if(table.isExtTable()){
			if(table.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE)){
				jdbcTemplate=(JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			}else{
				DriverManagerDataSource dataSource=sysDataSourceDao.getDriverMangerDataSourceByAlias(table.getDsAlias());
				jdbcTemplate=new JdbcTemplate(dataSource);
			}
		}else{
			jdbcTemplate= (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		}
		String sql="";
		//数字类型
		if(keyDataType==0){
			sql = "select a.* from " + subTableName + " a,bpm_bus_link b where  a."+pkField+"=b.bus_pk and a." + fkField + "=" + fkValue;
		}
		else{
			sql = "select a.* from " + subTableName + " a,bpm_bus_link b where  a."+pkField+"=b.bus_pkstr and  a." + fkField + "=" + fkValue;
		}
		 
		
		String limitSql=getLimitSql(tableId, actDefId, nodeId);
		
		// 处理子表权限.2013-1-17,by wwz.
		if (!StringUtil.isEmpty(limitSql)) {
			sql += " " + limitSql;
		}
		sql+=" order by b.BUS_ID";
		Map<String, BpmFormField> fieldMap = convertToMap(fieldList);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(!isHandleData)return list;
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : list) {
			Map<String, Object> obj = handMap(table,fieldMap, map);
			rtnList.add(obj);
		}
		return rtnList;
	}

	/**
	 * 处理返回的map数据 将key转换成小写。 如果是本地数据库，则替换字段前缀。 如果数据是日期类型，则转换数据格式。
	 * 
	 * @param isExternal
	 * @param fieldMap
	 * @param map
	 */
	private Map<String, Object> handMap(BpmFormTable table,Map<String, BpmFormField> fieldMap, Map<String, Object> map) {
		String pkField=table.getPkField();
		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String fieldName = entry.getKey().toLowerCase();
//			
			String key = fieldName;
			if(!table.isExtTable() && fieldName.indexOf(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase()) == 0){
				key = fieldName.replaceFirst(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(), "");
			}
			Object obj = entry.getValue();
			if (obj == null) {
				rtnMap.put(key, "");
				continue;
			}
			if(pkField.equalsIgnoreCase(key)){
				rtnMap.put(key, obj);
				continue;
			}
			BpmFormField bpmFormField = fieldMap.get(key);
			if(bpmFormField==null) continue;
			
			// 对时间字段单独处理。
			if (obj instanceof Date) {
				
				String format = "yyyy-MM-dd";
				if(bpmFormField!=null){
					String confFormat = bpmFormField.getPropertyMap().get("format");
					if (StringUtil.isNotEmpty(confFormat)) {
						format=confFormat;
					}
				}
				String str = TimeUtil.getDateTimeString((Date) obj, format);
				rtnMap.put(key, str);
			} 
			else if(obj instanceof Number){
				
				Map<String,String> paraMap=bpmFormField.getPropertyMap();
				Object isShowComdify= paraMap.get("isShowComdify");
				Object decimalValue= paraMap.get("decimalValue");
				Object coinValue= paraMap.get("coinValue");
				
				String str =StringUtil. getNumber(obj,isShowComdify,decimalValue,coinValue);
				rtnMap.put(key, str);
			}
			else {
				
				//附件替换
				if(BeanUtils.isNotEmpty(bpmFormField) && BeanUtils.isNotEmpty(bpmFormField.getControlType()) && bpmFormField.getControlType().shortValue() ==FieldPool.ATTACHEMENT ){
					obj = ((String) obj).replace("\"", "￥@@￥");
				}	
				rtnMap.put(key, obj);
			}
		}
		return rtnMap;
	}
	
	/**
	 * 查找
	 * 
	 * @param tableName
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAll(Long tableId, Map<String, Object> param) throws Exception {
		BpmFormTable table = bpmFormTableDao.getById(tableId);
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");

		// 字段名与数据
		StringBuffer where = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			where.append(entry.getKey()).append(" LIKE ? AND ");
			values.add(entry.getValue());
		}

		String tableName = table.getFactTableName();
		// sql
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.* FROM ");
		sql.append(tableName + " a");
		if (where.length() > 0) {
			sql.append(" WHERE ");
			sql.append(where.substring(0, where.length() - 5));
		}

		RowMapper<Map<String, Object>> rowMapper = new RowMapper<Map<String, Object>>() {

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int num) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData rsm = rs.getMetaData();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					String name = rsm.getColumnName(i);
					Object value = rs.getObject(name);
					map.put(name, value);
				}
				return map;
			}
		};

		// 执行
		return jdbcTemplate.query(sql.toString(), values.toArray(), rowMapper);

	}

	/**
	 * 根据表名和主键是否有数据。
	 * @param tableName		表名
	 * @param pk			主键
	 * @return
	 */
	public boolean isHasDataByPk(String tableName, Long pk ){
		String sql = "select count(1) from " + tableName + " a where id=?"  ;
//		int  rtn=jdbcTemplate.queryForInt(sql, pk);
//		return rtn>0;
//		int  rtn=jdbcTemplate.queryForInt(sql, pk);
		return true;
	}
	
	/**
	 * 根据主键获取子表的数据。
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public List<Map<String,Object>> getByFk(String tableName,Long fk){
		String sql = "select * from " + tableName + " a where "+TableModel.FK_COLUMN_NAME+"=?"  ;
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql, fk);
		return list;
	}

}