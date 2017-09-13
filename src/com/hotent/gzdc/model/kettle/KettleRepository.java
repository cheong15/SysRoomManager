package com.hotent.gzdc.model.kettle;

import java.util.ArrayList;
import java.util.List;
import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:KETTLE_REPOSITORY Model对象
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-10 09:31:32
 */
public class KettleRepository extends BaseModel
{
	// C_REPOSITORY_ID
	protected Long  CRepositoryId;
	// 资源库名称
	protected String  CRepositoryName;
	// 用户名
	protected String  CUserName;
	// 密码
	protected String  CPassword;
	// 版本
	protected String  CVersion;
	// 数据库主机
	protected String  CDbHost;
	// 端口
	protected String  CDbPort;
	// 数据库名
	protected String  CDbName;
	// 数据库类型
	protected String  CDbType;
	// C_DB_ACCESS
	protected String  CDbAccess;
	public void setCRepositoryId(Long CRepositoryId) 
	{
		this.CRepositoryId = CRepositoryId;
	}
	/**
	 * 返回 C_REPOSITORY_ID
	 * @return
	 */
	public Long getCRepositoryId() 
	{
		return this.CRepositoryId;
	}
	public void setCRepositoryName(String CRepositoryName) 
	{
		this.CRepositoryName = CRepositoryName;
	}
	/**
	 * 返回 资源库名称
	 * @return
	 */
	public String getCRepositoryName() 
	{
		return this.CRepositoryName;
	}
	public void setCUserName(String CUserName) 
	{
		this.CUserName = CUserName;
	}
	/**
	 * 返回 用户名
	 * @return
	 */
	public String getCUserName() 
	{
		return this.CUserName;
	}
	public void setCPassword(String CPassword) 
	{
		this.CPassword = CPassword;
	}
	/**
	 * 返回 密码
	 * @return
	 */
	public String getCPassword() 
	{
		return this.CPassword;
	}
	public void setCVersion(String CVersion) 
	{
		this.CVersion = CVersion;
	}
	/**
	 * 返回 版本
	 * @return
	 */
	public String getCVersion() 
	{
		return this.CVersion;
	}
	public void setCDbHost(String CDbHost) 
	{
		this.CDbHost = CDbHost;
	}
	/**
	 * 返回 数据库主机
	 * @return
	 */
	public String getCDbHost() 
	{
		return this.CDbHost;
	}
	public void setCDbPort(String CDbPort) 
	{
		this.CDbPort = CDbPort;
	}
	/**
	 * 返回 端口
	 * @return
	 */
	public String getCDbPort() 
	{
		return this.CDbPort;
	}
	public void setCDbName(String CDbName) 
	{
		this.CDbName = CDbName;
	}
	/**
	 * 返回 数据库名
	 * @return
	 */
	public String getCDbName() 
	{
		return this.CDbName;
	}
	public void setCDbType(String CDbType) 
	{
		this.CDbType = CDbType;
	}
	/**
	 * 返回 数据库类型
	 * @return
	 */
	public String getCDbType() 
	{
		return this.CDbType;
	}
	public void setCDbAccess(String CDbAccess) 
	{
		this.CDbAccess = CDbAccess;
	}
	/**
	 * 返回 C_DB_ACCESS
	 * @return
	 */
	public String getCDbAccess() 
	{
		return this.CDbAccess;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof KettleRepository)) 
		{
			return false;
		}
		KettleRepository rhs = (KettleRepository) object;
		return new EqualsBuilder()
		.append(this.CRepositoryId, rhs.CRepositoryId)
		.append(this.CRepositoryName, rhs.CRepositoryName)
		.append(this.CUserName, rhs.CUserName)
		.append(this.CPassword, rhs.CPassword)
		.append(this.CVersion, rhs.CVersion)
		.append(this.CDbHost, rhs.CDbHost)
		.append(this.CDbPort, rhs.CDbPort)
		.append(this.CDbName, rhs.CDbName)
		.append(this.CDbType, rhs.CDbType)
		.append(this.CDbAccess, rhs.CDbAccess)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.CRepositoryId) 
		.append(this.CRepositoryName) 
		.append(this.CUserName) 
		.append(this.CPassword) 
		.append(this.CVersion) 
		.append(this.CDbHost) 
		.append(this.CDbPort) 
		.append(this.CDbName) 
		.append(this.CDbType) 
		.append(this.CDbAccess) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("CRepositoryId", this.CRepositoryId) 
		.append("CRepositoryName", this.CRepositoryName) 
		.append("CUserName", this.CUserName) 
		.append("CPassword", this.CPassword) 
		.append("CVersion", this.CVersion) 
		.append("CDbHost", this.CDbHost) 
		.append("CDbPort", this.CDbPort) 
		.append("CDbName", this.CDbName) 
		.append("CDbType", this.CDbType) 
		.append("CDbAccess", this.CDbAccess) 
		.toString();
	}
   
  

}