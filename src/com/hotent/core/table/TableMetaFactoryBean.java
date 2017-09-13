package com.hotent.core.table;


import org.springframework.beans.factory.FactoryBean;

import com.hotent.core.table.impl.Db2TableMeta;
import com.hotent.core.table.impl.DmTableMeta;
import com.hotent.core.table.impl.H2TableMeta;
import com.hotent.core.table.impl.MySqlTableMeta;
import com.hotent.core.table.impl.OracleTableMeta;
import com.hotent.core.table.impl.SqlServerTableMeta;
import com.hotent.platform.model.system.SysDataSource;

/**
 * TableOperator factorybean，用户创建ITableOperator对象。
 * <pre>
 * 配置文件：app-beans.xml
 * &lt;bean id="tableOperator" class="com.hotent.core.customertable.TableOperatorFactoryBean">
 *			&lt;property name="dbType" value="${jdbc.dbType}"/>
 *			&lt;property name="jdbcTemplate" ref="jdbcTemplate"/>
 * &lt;/bean>
 * </pre>
 * @author ray
 *
 */
public class TableMetaFactoryBean implements FactoryBean<BaseTableMeta> {
	

	
	private BaseTableMeta tableMeta;
	
	private String dbType=SqlTypeConst.MYSQL;

	private SysDataSource sysDataSource;

	@Override
	public BaseTableMeta getObject() throws Exception {
		dbType=sysDataSource.getDbType();
		if(dbType.equals(SqlTypeConst.ORACLE)){
			tableMeta = new OracleTableMeta();
		}
		else if(dbType.equals(SqlTypeConst.SQLSERVER)){
			tableMeta = new SqlServerTableMeta();
		}
		else if(dbType.equals(SqlTypeConst.MYSQL)){
			tableMeta = new MySqlTableMeta();
		}else if(dbType.equals(SqlTypeConst.DB2)){
			tableMeta = new Db2TableMeta();
		}else if(dbType.equals(SqlTypeConst.H2)){
			tableMeta = new H2TableMeta();
		}else if(dbType.equals(SqlTypeConst.DM)){
			tableMeta = new DmTableMeta();
		}else{
			throw new Exception("没有设置合适的数据库类型");
		}
	//	sysDataSource.setPassword(sysDataSource.getEncPassword());
		tableMeta.setDataSource(sysDataSource);
		return tableMeta;
	}
	
	
	/**
	 * 设置数据库类型
	 * @param dbType
	 */
	public void setDbType(String dbType)
	{
		 this.dbType=dbType;
	}
	
	
	public void setSysDataSource(SysDataSource sysDataSource) {
		this.sysDataSource = sysDataSource;
	}

	
	 

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return BaseTableMeta.class;
	}

	@Override
	public boolean isSingleton() {
		
		return true;
	}

}
