package com.hotent.gzdc.model.kettle;

import java.util.ArrayList;
import java.util.List;
import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:KETTLE_HA_SLAVE Model对象
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:13
 */
public class KettleHaSlave extends BaseModel
{
	// ID_SLAVE
	protected Long  idSlave;
	// NAME
	protected String  name;
	// HOST_NAME
	protected String  hostName;
	// PORT
	protected String  port;
	// WEB_APP_NAME
	protected String  webAppName;
	// USERNAME
	protected String  username;
	// PASSWORD
	protected String  password;
	// PROXY_HOST_NAME
	protected String  proxyHostName;
	// PROXY_PORT
	protected String  proxyPort;
	// NON_PROXY_HOSTS
	protected String  nonProxyHosts;
	// MASTER
	protected String  master;
	public void setIdSlave(Long idSlave) 
	{
		this.idSlave = idSlave;
	}
	/**
	 * 返回 ID_SLAVE
	 * @return
	 */
	public Long getIdSlave() 
	{
		return this.idSlave;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 NAME
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setHostName(String hostName) 
	{
		this.hostName = hostName;
	}
	/**
	 * 返回 HOST_NAME
	 * @return
	 */
	public String getHostName() 
	{
		return this.hostName;
	}
	public void setPort(String port) 
	{
		this.port = port;
	}
	/**
	 * 返回 PORT
	 * @return
	 */
	public String getPort() 
	{
		return this.port;
	}
	public void setWebAppName(String webAppName) 
	{
		this.webAppName = webAppName;
	}
	/**
	 * 返回 WEB_APP_NAME
	 * @return
	 */
	public String getWebAppName() 
	{
		return this.webAppName;
	}
	public void setUsername(String username) 
	{
		this.username = username;
	}
	/**
	 * 返回 USERNAME
	 * @return
	 */
	public String getUsername() 
	{
		return this.username;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	/**
	 * 返回 PASSWORD
	 * @return
	 */
	public String getPassword() 
	{
		return this.password;
	}
	public void setProxyHostName(String proxyHostName) 
	{
		this.proxyHostName = proxyHostName;
	}
	/**
	 * 返回 PROXY_HOST_NAME
	 * @return
	 */
	public String getProxyHostName() 
	{
		return this.proxyHostName;
	}
	public void setProxyPort(String proxyPort) 
	{
		this.proxyPort = proxyPort;
	}
	/**
	 * 返回 PROXY_PORT
	 * @return
	 */
	public String getProxyPort() 
	{
		return this.proxyPort;
	}
	public void setNonProxyHosts(String nonProxyHosts) 
	{
		this.nonProxyHosts = nonProxyHosts;
	}
	/**
	 * 返回 NON_PROXY_HOSTS
	 * @return
	 */
	public String getNonProxyHosts() 
	{
		return this.nonProxyHosts;
	}
	public void setMaster(String master) 
	{
		this.master = master;
	}
	/**
	 * 返回 MASTER
	 * @return
	 */
	public String getMaster() 
	{
		return this.master;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof KettleHaSlave)) 
		{
			return false;
		}
		KettleHaSlave rhs = (KettleHaSlave) object;
		return new EqualsBuilder()
		.append(this.idSlave, rhs.idSlave)
		.append(this.name, rhs.name)
		.append(this.hostName, rhs.hostName)
		.append(this.port, rhs.port)
		.append(this.webAppName, rhs.webAppName)
		.append(this.username, rhs.username)
		.append(this.password, rhs.password)
		.append(this.proxyHostName, rhs.proxyHostName)
		.append(this.proxyPort, rhs.proxyPort)
		.append(this.nonProxyHosts, rhs.nonProxyHosts)
		.append(this.master, rhs.master)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.idSlave) 
		.append(this.name) 
		.append(this.hostName) 
		.append(this.port) 
		.append(this.webAppName) 
		.append(this.username) 
		.append(this.password) 
		.append(this.proxyHostName) 
		.append(this.proxyPort) 
		.append(this.nonProxyHosts) 
		.append(this.master) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("idSlave", this.idSlave) 
		.append("name", this.name) 
		.append("hostName", this.hostName) 
		.append("port", this.port) 
		.append("webAppName", this.webAppName) 
		.append("username", this.username) 
		.append("password", this.password) 
		.append("proxyHostName", this.proxyHostName) 
		.append("proxyPort", this.proxyPort) 
		.append("nonProxyHosts", this.nonProxyHosts) 
		.append("master", this.master) 
		.toString();
	}
   
  

}