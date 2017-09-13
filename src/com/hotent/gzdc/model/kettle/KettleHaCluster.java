package com.hotent.gzdc.model.kettle;

import java.util.ArrayList;
import java.util.List;
import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:KETTLE_HA_CLUSTER Model对象
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:55
 */
public class KettleHaCluster extends BaseModel
{
	// ID_CLUSTER
	protected Long  idCluster;
	// NAME
	protected String  name;
	// BASE_PORT
	protected String  basePort;
	// SOCKETS_BUFFER_SIZE
	protected String  socketsBufferSize;
	// SOCKETS_FLUSH_INTERVAL
	protected String  socketsFlushInterval;
	// SOCKETS_COMPRESSED
	protected String  socketsCompressed;
	// DYNAMIC_CLUSTER
	protected String  dynamicCluster;
	public void setIdCluster(Long idCluster) 
	{
		this.idCluster = idCluster;
	}
	/**
	 * 返回 ID_CLUSTER
	 * @return
	 */
	public Long getIdCluster() 
	{
		return this.idCluster;
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
	public void setBasePort(String basePort) 
	{
		this.basePort = basePort;
	}
	/**
	 * 返回 BASE_PORT
	 * @return
	 */
	public String getBasePort() 
	{
		return this.basePort;
	}
	public void setSocketsBufferSize(String socketsBufferSize) 
	{
		this.socketsBufferSize = socketsBufferSize;
	}
	/**
	 * 返回 SOCKETS_BUFFER_SIZE
	 * @return
	 */
	public String getSocketsBufferSize() 
	{
		return this.socketsBufferSize;
	}
	public void setSocketsFlushInterval(String socketsFlushInterval) 
	{
		this.socketsFlushInterval = socketsFlushInterval;
	}
	/**
	 * 返回 SOCKETS_FLUSH_INTERVAL
	 * @return
	 */
	public String getSocketsFlushInterval() 
	{
		return this.socketsFlushInterval;
	}
	public void setSocketsCompressed(String socketsCompressed) 
	{
		this.socketsCompressed = socketsCompressed;
	}
	/**
	 * 返回 SOCKETS_COMPRESSED
	 * @return
	 */
	public String getSocketsCompressed() 
	{
		return this.socketsCompressed;
	}
	public void setDynamicCluster(String dynamicCluster) 
	{
		this.dynamicCluster = dynamicCluster;
	}
	/**
	 * 返回 DYNAMIC_CLUSTER
	 * @return
	 */
	public String getDynamicCluster() 
	{
		return this.dynamicCluster;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof KettleHaCluster)) 
		{
			return false;
		}
		KettleHaCluster rhs = (KettleHaCluster) object;
		return new EqualsBuilder()
		.append(this.idCluster, rhs.idCluster)
		.append(this.name, rhs.name)
		.append(this.basePort, rhs.basePort)
		.append(this.socketsBufferSize, rhs.socketsBufferSize)
		.append(this.socketsFlushInterval, rhs.socketsFlushInterval)
		.append(this.socketsCompressed, rhs.socketsCompressed)
		.append(this.dynamicCluster, rhs.dynamicCluster)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.idCluster) 
		.append(this.name) 
		.append(this.basePort) 
		.append(this.socketsBufferSize) 
		.append(this.socketsFlushInterval) 
		.append(this.socketsCompressed) 
		.append(this.dynamicCluster) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("idCluster", this.idCluster) 
		.append("name", this.name) 
		.append("basePort", this.basePort) 
		.append("socketsBufferSize", this.socketsBufferSize) 
		.append("socketsFlushInterval", this.socketsFlushInterval) 
		.append("socketsCompressed", this.socketsCompressed) 
		.append("dynamicCluster", this.dynamicCluster) 
		.toString();
	}
   
  

}