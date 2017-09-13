package com.hotent.platform.model.system;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:人员参数属性 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2012-02-24 10:04:50
 */
public class SysUserParam extends BaseModel
{
	// valueId
	protected Long valueId;
	// 用户ID
	protected Long userId;
	// paramId
	protected Long paramId;
	// 参数值
	protected String paramValue;
	//参数名称
	protected String paramName;
	//参数类型
	protected String dataType;
	//数字值
	protected Long paramIntValue;
	//日期值
	protected java.util.Date paramDateValue;
	
	

	public Long getParamIntValue() {
		return paramIntValue;
	}
	public void setParamIntValue(Long paramIntValue) {
		this.paramIntValue = paramIntValue;
	}
	public java.util.Date getParamDateValue() {
		return paramDateValue;
	}
	public void setParamDateValue(java.util.Date paramDateValue) {
		this.paramDateValue = paramDateValue;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public void setValueId(Long valueId) 
	{
		this.valueId = valueId;
	}
	/**
	 * 返回 valueId
	 * @return
	 */
	public Long getValueId() 
	{
		return valueId;
	}

	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}
	/**
	 * 返回 用户ID
	 * @return
	 */
	public Long getUserId() 
	{
		return userId;
	}

	public void setParamId(Long paramId) 
	{
		this.paramId = paramId;
	}
	/**
	 * 返回 paramId
	 * @return
	 */
	public Long getParamId() 
	{
		return paramId;
	}

	public void setParamValue(String paramValue) 
	{
		this.paramValue = paramValue;
	}
	/**
	 * 返回 参数值
	 * @return
	 */
	public String getParamValue() 
	{
		return paramValue;
	}

   
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof SysUserParam)) 
		{
			return false;
		}
		SysUserParam rhs = (SysUserParam) object;
		return new EqualsBuilder()
		.append(this.valueId, rhs.valueId)
		.append(this.userId, rhs.userId)
		.append(this.paramId, rhs.paramId)
		.append(this.paramValue, rhs.paramValue)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.valueId) 
		.append(this.userId) 
		.append(this.paramId) 
		.append(this.paramValue) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("valueId", this.valueId) 
		.append("userId", this.userId) 
		.append("paramId", this.paramId) 
		.append("paramValue", this.paramValue) 
		.toString();
	}
   
  

}