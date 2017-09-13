package net.gmcc.gz.model.sm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.core.annotion.query.Field;
import com.hotent.core.annotion.query.Table;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:员工操作记录 Model对象
 * 开发公司:从兴技术有限公司
 * 开发人员:John
 * 创建时间:2014-05-05 17:18:25
 */
 @Table(name="SM_EMP_LOG",comment="员工操作记录",pk="REC_ID")
public class EmpLog extends BaseModel
{
	// 记录ID
	@Field(name="REC_ID",desc="记录ID",dataType="NUMERIC")
	protected Long  recId;
	// 操作用户ID
	@Field(name="USER_ID",desc="操作用户ID",dataType="VARCHAR")
	protected String  userId;
	// 操作用户姓名
	@Field(name="USER_NAME",desc="操作用户姓名",dataType="VARCHAR")
	protected String  userName;
	// 员工ID
	@Field(name="EMPLOYEE_ID",desc="员工ID",dataType="VARCHAR")
	protected String  employeeId;
	// 员工姓名
	@Field(name="EMPLOYEE_NAME",desc="员工姓名",dataType="VARCHAR")
	protected String  employeeName;
	// 操作内容
	@Field(name="OP_CONTENT",desc="操作内容",dataType="VARCHAR")
	protected String  opContent;
	// 创建时间
	@Field(name="CREATE_TIME",desc="创建时间",dataType="DATE")
	protected java.util.Date  createTime;
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
	 * 返回 操作用户ID
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
	 * 返回 操作用户姓名
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
	public void setOpContent(String opContent) 
	{
		this.opContent = opContent;
	}
	/**
	 * 返回 操作内容
	 * @return
	 */
	public String getOpContent() 
	{
		return this.opContent;
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

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof EmpLog)) 
		{
			return false;
		}
		EmpLog rhs = (EmpLog) object;
		return new EqualsBuilder()
		.append(this.recId, rhs.recId)
		.append(this.userId, rhs.userId)
		.append(this.userName, rhs.userName)
		.append(this.employeeId, rhs.employeeId)
		.append(this.employeeName, rhs.employeeName)
		.append(this.opContent, rhs.opContent)
		.append(this.createTime, rhs.createTime)
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
		.append(this.opContent) 
		.append(this.createTime) 
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
		.append("opContent", this.opContent) 
		.append("createTime", this.createTime) 
		.toString();
	}
   
  

}