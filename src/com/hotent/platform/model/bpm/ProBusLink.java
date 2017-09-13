package com.hotent.platform.model.bpm;
import java.util.Date;
import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:流程实例与业务关联表 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2013-07-17 14:01:11
 */
public class ProBusLink extends BaseModel
{
	// ID
	protected Long  id;
	// 表名
	protected String  tableName;
	// 主键字段名
	protected String  pkField;
	// 键值
	protected String  pkVal;
	// 创建时间
	protected Date  createtime;
	// 流程运行ID
	protected Long  runId;
	// ACT_INST_ID
	protected Long  actInstId;
	// 流程定义ID
	protected Long  defId;
	// ACT_DEF_ID
	protected String  actDefId;
	// 执行人ID
	protected Long  userId;
	// ORG_ID
	protected Long  orgId;
	
	public ProBusLink() {
	
	}
	
	public ProBusLink(ProcessRun processRun){
		this.actDefId=processRun.getActDefId();
		this.actInstId=new Long(processRun.getActInstId());
		this.createtime=new Date();
		this.userId=processRun.getCreatorId();;
		this.runId=processRun.getRunId();
		this.orgId=processRun.getStartOrgId();
		this.defId=processRun.getDefId();
	}
	
	public void setId(Long id) 
	{
		this.id = id;
	}
	/**
	 * 返回 ID
	 * @return
	 */
	public Long getId() 
	{
		return this.id;
	}
	public void setTableName(String tableName) 
	{
		this.tableName = tableName;
	}
	/**
	 * 返回 表名
	 * @return
	 */
	public String getTableName() 
	{
		return this.tableName;
	}
	public void setPkField(String pkField) 
	{
		this.pkField = pkField;
	}
	/**
	 * 返回 主键字段名
	 * @return
	 */
	public String getPkField() 
	{
		return this.pkField;
	}
	public void setPkVal(String pkVal) 
	{
		this.pkVal = pkVal;
	}
	/**
	 * 返回 键值
	 * @return
	 */
	public String getPkVal() 
	{
		return this.pkVal;
	}
	public void setCreatetime(Date createtime) 
	{
		this.createtime = createtime;
	}
	/**
	 * 返回 创建时间
	 * @return
	 */
	public Date getCreatetime() 
	{
		return this.createtime;
	}
	public void setRunId(Long runId) 
	{
		this.runId = runId;
	}
	/**
	 * 返回 流程运行ID
	 * @return
	 */
	public Long getRunId() 
	{
		return this.runId;
	}
	public void setActInstId(Long actInstId) 
	{
		this.actInstId = actInstId;
	}
	/**
	 * 返回 ACT_INST_ID
	 * @return
	 */
	public Long getActInstId() 
	{
		return this.actInstId;
	}
	public void setDefId(Long defId) 
	{
		this.defId = defId;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public Long getDefId() 
	{
		return this.defId;
	}
	public void setActDefId(String actDefId) 
	{
		this.actDefId = actDefId;
	}
	/**
	 * 返回 ACT_DEF_ID
	 * @return
	 */
	public String getActDefId() 
	{
		return this.actDefId;
	}
	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}
	/**
	 * 返回 执行人ID
	 * @return
	 */
	public Long getUserId() 
	{
		return this.userId;
	}
	public void setOrgId(Long orgId) 
	{
		this.orgId = orgId;
	}
	/**
	 * 返回 ORG_ID
	 * @return
	 */
	public Long getOrgId() 
	{
		return this.orgId;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof ProBusLink)) 
		{
			return false;
		}
		ProBusLink rhs = (ProBusLink) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.tableName, rhs.tableName)
		.append(this.pkField, rhs.pkField)
		.append(this.pkVal, rhs.pkVal)
		.append(this.createtime, rhs.createtime)
		.append(this.runId, rhs.runId)
		.append(this.actInstId, rhs.actInstId)
		.append(this.defId, rhs.defId)
		.append(this.actDefId, rhs.actDefId)
		.append(this.userId, rhs.userId)
		.append(this.orgId, rhs.orgId)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.tableName) 
		.append(this.pkField) 
		.append(this.pkVal) 
		.append(this.createtime) 
		.append(this.runId) 
		.append(this.actInstId) 
		.append(this.defId) 
		.append(this.actDefId) 
		.append(this.userId) 
		.append(this.orgId) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("tableName", this.tableName) 
		.append("pkField", this.pkField) 
		.append("pkVal", this.pkVal) 
		.append("createtime", this.createtime) 
		.append("runId", this.runId) 
		.append("actInstId", this.actInstId) 
		.append("defId", this.defId) 
		.append("actDefId", this.actDefId) 
		.append("userId", this.userId) 
		.append("orgId", this.orgId) 
		.toString();
	}
   
  

}