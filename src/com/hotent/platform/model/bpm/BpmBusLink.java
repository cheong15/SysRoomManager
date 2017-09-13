package com.hotent.platform.model.bpm;

import java.util.ArrayList;
import java.util.List;
import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:业务数据关联表 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2013-08-21 16:51:49
 */
public class BpmBusLink extends BaseModel
{
	// 主键
	protected Long  busId;
	// 对应关联表主键
	protected Long  busPk;
	// 对应关联表主键(字符串)
	protected String  busPkstr;
	// 流程定义ID
	protected Long  busDefId;
	// 流程运行ID
	protected Long  busFlowRunid;
	// 发起人ID
	protected Long  busCreatorId;
	// 发起人
	protected String  busCreator;
	// 发起组织
	protected Long  busOrgId;
	// 组织名称
	protected String  busOrgName;
	// 发起时间
	protected java.util.Date  busCreatetime;
	// 最后更新人
	protected Long  busUpdid;
	// 最后更新人
	protected String  busUpd;
	// 最后更新时间
	protected java.util.Date  busUpdtime;
	
	public void setBusId(Long busId) 
	{
		this.busId = busId;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public Long getBusId() 
	{
		return this.busId;
	}
	public void setBusPk(Long busPk) 
	{
		this.busPk = busPk;
	}
	/**
	 * 返回 对应关联表主键
	 * @return
	 */
	public Long getBusPk() 
	{
		return this.busPk;
	}
	public void setBusPkstr(String busPkstr) 
	{
		this.busPkstr = busPkstr;
	}
	/**
	 * 返回 对应关联表主键(字符串)
	 * @return
	 */
	public String getBusPkstr() 
	{
		return this.busPkstr;
	}
	public void setBusDefId(Long busDefId) 
	{
		this.busDefId = busDefId;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public Long getBusDefId() 
	{
		return this.busDefId;
	}
	public void setBusFlowRunid(Long busFlowRunid) 
	{
		this.busFlowRunid = busFlowRunid;
	}
	/**
	 * 返回 流程运行ID
	 * @return
	 */
	public Long getBusFlowRunid() 
	{
		return this.busFlowRunid;
	}
	public void setBusCreatorId(Long busCreatorId) 
	{
		this.busCreatorId = busCreatorId;
	}
	/**
	 * 返回 发起人ID
	 * @return
	 */
	public Long getBusCreatorId() 
	{
		return this.busCreatorId;
	}
	public void setBusCreator(String busCreator) 
	{
		this.busCreator = busCreator;
	}
	/**
	 * 返回 发起人
	 * @return
	 */
	public String getBusCreator() 
	{
		return this.busCreator;
	}
	public void setBusOrgId(Long busOrgId) 
	{
		this.busOrgId = busOrgId;
	}
	/**
	 * 返回 发起组织
	 * @return
	 */
	public Long getBusOrgId() 
	{
		return this.busOrgId;
	}
	public void setBusOrgName(String busOrgName) 
	{
		this.busOrgName = busOrgName;
	}
	/**
	 * 返回 组织名称
	 * @return
	 */
	public String getBusOrgName() 
	{
		return this.busOrgName;
	}
	public void setBusCreatetime(java.util.Date busCreatetime) 
	{
		this.busCreatetime = busCreatetime;
	}
	/**
	 * 返回 发起时间
	 * @return
	 */
	public java.util.Date getBusCreatetime() 
	{
		return this.busCreatetime;
	}
	public void setBusUpdid(Long busUpdid) 
	{
		this.busUpdid = busUpdid;
	}
	/**
	 * 返回 最后更新人
	 * @return
	 */
	public Long getBusUpdid() 
	{
		return this.busUpdid;
	}
	public void setBusUpd(String busUpd) 
	{
		this.busUpd = busUpd;
	}
	/**
	 * 返回 最后更新人
	 * @return
	 */
	public String getBusUpd() 
	{
		return this.busUpd;
	}
	public void setBusUpdtime(java.util.Date busUpdtime) 
	{
		this.busUpdtime = busUpdtime;
	}
	/**
	 * 返回 最后更新时间
	 * @return
	 */
	public java.util.Date getBusUpdtime() 
	{
		return this.busUpdtime;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof BpmBusLink)) 
		{
			return false;
		}
		BpmBusLink rhs = (BpmBusLink) object;
		return new EqualsBuilder()
		.append(this.busId, rhs.busId)
		.append(this.busPk, rhs.busPk)
		.append(this.busPkstr, rhs.busPkstr)
		.append(this.busDefId, rhs.busDefId)
		.append(this.busFlowRunid, rhs.busFlowRunid)
		.append(this.busCreatorId, rhs.busCreatorId)
		.append(this.busCreator, rhs.busCreator)
		.append(this.busOrgId, rhs.busOrgId)
		.append(this.busOrgName, rhs.busOrgName)
		.append(this.busCreatetime, rhs.busCreatetime)
		.append(this.busUpdid, rhs.busUpdid)
		.append(this.busUpd, rhs.busUpd)
		.append(this.busUpdtime, rhs.busUpdtime)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.busId) 
		.append(this.busPk) 
		.append(this.busPkstr) 
		.append(this.busDefId) 
		.append(this.busFlowRunid) 
		.append(this.busCreatorId) 
		.append(this.busCreator) 
		.append(this.busOrgId) 
		.append(this.busOrgName) 
		.append(this.busCreatetime) 
		.append(this.busUpdid) 
		.append(this.busUpd) 
		.append(this.busUpdtime) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("busId", this.busId) 
		.append("busPk", this.busPk) 
		.append("busPkstr", this.busPkstr) 
		.append("busDefId", this.busDefId) 
		.append("busFlowRunid", this.busFlowRunid) 
		.append("busCreatorId", this.busCreatorId) 
		.append("busCreator", this.busCreator) 
		.append("busOrgId", this.busOrgId) 
		.append("busOrgName", this.busOrgName) 
		.append("busCreatetime", this.busCreatetime) 
		.append("busUpdid", this.busUpdid) 
		.append("busUpd", this.busUpd) 
		.append("busUpdtime", this.busUpdtime) 
		.toString();
	}
   
  

}