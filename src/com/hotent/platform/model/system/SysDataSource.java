package com.hotent.platform.model.system;

import com.hotent.core.encrypt.EncryptUtil;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:系统数据源管理 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2011-11-16 16:34:15
 */
public class SysDataSource extends BaseModel{
	
	
	/**
	 * 数据库类型=oracle
	 */
	public final static String DBTYPE_ORACLE = "oracle";
	/**
	 * 数据库类型=sql2005
	 */
	public final static String DBTYPE_SQL2005 = "sql2005";
	/**
	 * 数据库类型=mysql
	 */
	public final static String DBTYPE_MYSQL = "mysql";
	/**
	 * 数据库类型=db2
	 */
	public final static String DBTYPE_DB2= "db2";
	/**
	 * 数据库类型=h2
	 */
	public final static String DBTYPE_H2 = "h2";
	/**
	 * 数据库类型=达梦
	 */
	public final static String DBTYPE_DM= "dm";
	
	/**
	 * 代表本地数据源，即代表连接本库
	 */
	public final static String DS_LOCAL="LOCAL";
	
	
	/***/
	private static final long serialVersionUID = -3760293574264972800L;
	// 主键
	private Long id;
	// 数据源名称
	private String name;
	// 别名
	private String alias;
	// 驱动名称
	private String driverName;
	// 数据库URL
	private String url;
	// 用户名
	private String userName;
	// 密码
	private String password;
	
	private String encPassword;
	//数据库类型
	private String dbType="";

	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 返回 数据源名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	/**
	 * 返回 驱动名称
	 * @return
	 */
	public String getDriverName() {
		return driverName;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 返回 数据库URL
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 返回 用户名
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 返回 密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	public String getEncPassword() {
		return encPassword;
	}
	public void setEncPassword(String pwd) throws Exception {
		pwd=EncryptUtil.decrypt(pwd);
		this.encPassword = pwd;
	}
}