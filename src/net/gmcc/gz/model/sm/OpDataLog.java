package net.gmcc.gz.model.sm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.core.annotion.query.Field;
import com.hotent.core.annotion.query.Table;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:操作记录数据保存 Model对象
 * 开发公司:从兴技术有限公司
 * 开发人员:John
 * 创建时间:2014-04-26 18:13:32
 */
 @Table(name="SM_EMP_OP_LOG",comment="操作记录数据保存",pk="REC_ID")
public class OpDataLog extends BaseModel
{
	// 记录ID
	@Field(name="REC_ID",desc="记录ID",dataType="NUMERIC")
	protected Long  recId;
	// 操作人ID
	@Field(name="USER_ID",desc="操作人ID",dataType="VARCHAR")
	protected String  userId;
	// 操作人姓名
	@Field(name="USER_NAME",desc="操作人姓名",dataType="VARCHAR")
	protected String  userName;
	// 员工ID
	@Field(name="EMPLOYEE_ID",desc="员工ID",dataType="VARCHAR")
	protected String  employeeId;
	// 员工姓名
	@Field(name="EMPLOYEE_NAME",desc="员工姓名",dataType="VARCHAR")
	protected String  employeeName;
	// 原始数据
	@Field(name="RAW_DATA",desc="原始数据",dataType="VARCHAR")
	protected String  rawData;
	// 修改后/新增的数据
	@Field(name="REVISED_DATA",desc="修改后/新增的数据",dataType="VARCHAR")
	protected String  revisedData;
	// 记录状态1已处理 0 未处理
	@Field(name="STATUS",desc="记录状态1已处理 0 未处理",dataType="VARCHAR")
	protected String  status;
	// 创建时间
	@Field(name="CREATE_TIME",desc="创建时间",dataType="DATE")
	protected java.util.Date  createTime;
	// 操作类型
	@Field(name="OP_TYPE",desc="操作类型",dataType="VARCHAR")
	protected String  opType;
	// 表ID
	@Field(name="TABLEID",desc="表ID",dataType="NUMERIC")
	protected Long  tableid;
	public void setRecId(Long recId) 
	{
		this.recId = recId;
	}
	/**
	 * 返回 记录ID
	 * @return
	 */
	public Long getRecId() 
	{
		return this.recId;
	}
	public void setUserId(String userId) 
	{
		this.userId = userId;
	}
	/**
	 * 返回 操作人ID
	 * @return
	 */
	public String getUserId() 
	{
		return this.userId;
	}
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	/**
	 * 返回 操作人姓名
	 * @return
	 */
	public String getUserName() 
	{
		return this.userName;
	}
	public void setEmployeeId(String employeeId) 
	{
		this.employeeId = employeeId;
	}
	/**
	 * 返回 员工ID
	 * @return
	 */
	public String getEmployeeId() 
	{
		return this.employeeId;
	}
	public void setEmployeeName(String employeeName) 
	{
		this.employeeName = employeeName;
	}
	/**
	 * 返回 员工姓名
	 * @return
	 */
	public String getEmployeeName() 
	{
		return this.employeeName;
	}
	public void setRawData(String rawData) 
	{
		this.rawData = rawData;
	}
	/**
	 * 返回 原始数据
	 * @return
	 */
	public String getRawData() 
	{
		return this.rawData;
	}
	public void setRevisedData(String revisedData) 
	{
		this.revisedData = revisedData;
	}
	/**
	 * 返回 修改后/新增的数据
	 * @return
	 */
	public String getRevisedData() 
	{
		return this.revisedData;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 记录状态1已处理 0 未处理
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	public void setCreateTime(java.util.Date createTime) 
	{
		this.createTime = createTime;
	}
	/**
	 * 返回 创建时间
	 * @return
	 */
	public java.util.Date getCreateTime() 
	{
		return this.createTime;
	}
	public void setOpType(String opType) 
	{
		this.opType = opType;
	}
	/**
	 * 返回 操作类型
	 * @return
	 */
	public String getOpType() 
	{
		return this.opType;
	}
	public void setTableid(Long tableid) 
	{
		this.tableid = tableid;
	}
	/**
	 * 返回 表ID
	 * @return
	 */
	public Long getTableid() 
	{
		return this.tableid;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof OpDataLog)) 
		{
			return false;
		}
		OpDataLog rhs = (OpDataLog) object;
		return new EqualsBuilder()
		.append(this.recId, rhs.recId)
		.append(this.userId, rhs.userId)
		.append(this.userName, rhs.userName)
		.append(this.employeeId, rhs.employeeId)
		.append(this.employeeName, rhs.employeeName)
		.append(this.rawData, rhs.rawData)
		.append(this.revisedData, rhs.revisedData)
		.append(this.status, rhs.status)
		.append(this.createTime, rhs.createTime)
		.append(this.opType, rhs.opType)
		.append(this.tableid, rhs.tableid)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.recId) 
		.append(this.userId) 
		.append(this.userName) 
		.append(this.employeeId) 
		.append(this.employeeName) 
		.append(this.rawData) 
		.append(this.revisedData) 
		.append(this.status) 
		.append(this.createTime) 
		.append(this.opType) 
		.append(this.tableid) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("recId", this.recId) 
		.append("userId", this.userId) 
		.append("userName", this.userName) 
		.append("employeeId", this.employeeId) 
		.append("employeeName", this.employeeName) 
		.append("rawData", this.rawData) 
		.append("revisedData", this.revisedData) 
		.append("status", this.status) 
		.append("createTime", this.createTime) 
		.append("opType", this.opType) 
		.append("tableid", this.tableid) 
		.toString();
	}
   
  

}