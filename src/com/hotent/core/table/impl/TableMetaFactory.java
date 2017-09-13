package com.hotent.core.table.impl;

import java.util.Properties;

import com.hotent.core.bpm.util.BpmConst;
import com.hotent.core.table.BaseTableMeta;
import com.hotent.core.table.IDbView;
import com.hotent.core.table.SqlTypeConst;
import com.hotent.core.util.AppUtil;
import com.hotent.platform.model.system.SysDataSource;
import com.hotent.platform.service.system.SysDataSourceService;

/**
 * 元数据读取工厂。
 * 
 * @author ray
 * 
 */
public class TableMetaFactory {

	/**
	 * 根据数据源名称获取表元素据读取对象。 这里使用了简单工厂。
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	public static BaseTableMeta getMetaData(String dsName) throws Exception {
		SysDataSource sysDataSource=null;
		if(dsName==null){
			sysDataSource=(SysDataSource)AppUtil.getBean("sysdatasource");
		}
		else if(!dsName.equals(BpmConst.LOCAL_DATASOURCE)){ 
			SysDataSourceService sysDataSourceService = (SysDataSourceService) AppUtil
					.getBean(SysDataSourceService.class);
			sysDataSource = sysDataSourceService.getByAlias(dsName);
		}else{
			sysDataSource=getLocalSysDataSource(dsName);
			}
		String dbType =SqlTypeConst.getDbType(sysDataSource.getUrl());
		BaseTableMeta meta = null;
		if (dbType.equals(SqlTypeConst.ORACLE)) {
			meta = new OracleTableMeta();
		} else if (dbType.equals(SqlTypeConst.MYSQL)) {
			meta = new MySqlTableMeta();
		}else if (dbType.equals(SqlTypeConst.SQLSERVER)){
			meta =new SqlServerTableMeta();
		}else if (dbType.equals(SqlTypeConst.DB2)){
			meta =new Db2TableMeta();
		}else if (dbType.equals(SqlTypeConst.H2)){
			meta =new H2TableMeta();
		}else if (dbType.equals(SqlTypeConst.DM)){
			meta =new DmTableMeta();
		}else {
			throw new Exception("未知的数据库类型");
		}
		meta.setDataSource(sysDataSource);
		return meta;
	}

	/**
	 * 根据数据源获取
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	public static IDbView getDbView(String dsName) throws Exception {
		SysDataSource sysDataSource = null;
		if(dsName.equals(BpmConst.LOCAL_DATASOURCE)){
			sysDataSource=getLocalSysDataSource(dsName);
		}else{
			SysDataSourceService sysDataSourceService = (SysDataSourceService) AppUtil
					.getBean(SysDataSourceService.class);
			sysDataSource = sysDataSourceService.getByAlias(dsName);
		}
		
		String dbType =SqlTypeConst.getDbType(sysDataSource.getUrl());
		IDbView meta = null;
		if (dbType.equals(SqlTypeConst.ORACLE)) {
			meta = new OracleDbView();
		} else if (dbType.equals(SqlTypeConst.SQLSERVER)) {
			meta = new SqlserverDbView();
		} else if (dbType.equals(SqlTypeConst.MYSQL)){
			meta = new MysqlDbView();
		}else if (dbType.equals(SqlTypeConst.DB2)){
			meta = new Db2DbView();
		}else if (dbType.equals(SqlTypeConst.H2)){
			meta = new H2DbView();
		}else if (dbType.equals(SqlTypeConst.DM)){
			meta = new DmDbView();
		}else {
			throw new Exception("未知的数据库类型");
		}
		meta.setDataSource(sysDataSource);
		return meta;
	}
	public static SysDataSource  getLocalSysDataSource(String dsName){
		return (SysDataSource)AppUtil.getBean("sysdatasource");
	}

}
