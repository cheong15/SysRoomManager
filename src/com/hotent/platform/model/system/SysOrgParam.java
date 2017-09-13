package com.hotent.platform.model.system;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:组织参数属性 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2012-02-24 10:04:50
 */
public class SysOrgParam extends BaseModel
{
	// valueId
	protected Long valueId;
	// 组织ID
	protected Long orgId;
	// paramId
	protected Long paramId;
	// 参数值
	protected String paramValue;
	protected String paramName;
	protected String dataType;
	
	protected Long paramIntValue;
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
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
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

	public void setOrgId(Long orgId) 
	{
		this.orgId = orgId;
	}
	/**
	 * 返回 组织ID
	 * @return
	 */
	public Long getOrgId() 
	{
		return orgId;
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
		if (!(object instanceof SysOrgParam)) 
		{
			return false;
		}
		SysOrgParam rhs = (SysOrgParam) object;
		return new EqualsBuilder()
		.append(this.valueId, rhs.valueId)
		.append(this.orgId, rhs.orgId)
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
		.append(this.orgId) 
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
		.append("orgId", this.orgId) 
		.append("paramId", this.paramId) 
		.append("paramValue", this.paramValue) 
		.toString();
	}
   
  

}