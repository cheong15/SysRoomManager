package com.hotent.platform.model.system;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:用户岗位表 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
public class UserPosition extends BaseModel
{
	public static Short PRIMARY_NO=0;
	public static Short PRIMARY_YES=1;
	
	
	// userPosId
	protected Long userPosId;
	// 岗位编号
	protected Long posId;
	// 用户ID
	protected Long userId;
	// 是否主岗位
	protected Short isPrimary=PRIMARY_NO;
	protected String account;
	protected String fullname;
	protected String posName;
	

	public String getPosName() {
		return posName;
	}
	public void setPosName(String posName) {
		this.posName = posName;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public void setUserPosId(Long userPosId) 
	{
		this.userPosId = userPosId;
	}
	/**
	 * 返回 userPosId
	 * @return
	 */
	public Long getUserPosId() 
	{
		return userPosId;
	}

	public void setPosId(Long posId) 
	{
		this.posId = posId;
	}
	/**
	 * 返回 岗位编号
	 * @return
	 */
	public Long getPosId() 
	{
		return posId;
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

	public void setIsPrimary(Short isPrimary) 
	{
		this.isPrimary = isPrimary;
	}
	/**
	 * 返回 是否主岗位
	 * @return
	 */
	public Short getIsPrimary() 
	{
		return isPrimary;
	}

   
   	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof UserPosition)) 
		{
			return false;
		}
		UserPosition rhs = (UserPosition) object;
		return new EqualsBuilder()
		.append(this.userPosId, rhs.userPosId)
		.append(this.posId, rhs.posId)
		.append(this.userId, rhs.userId)
		.append(this.isPrimary, rhs.isPrimary)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.userPosId) 
		.append(this.posId) 
		.append(this.userId) 
		.append(this.isPrimary) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("userPosId", this.userPosId) 
		.append("posId", this.posId) 
		.append("userId", this.userId) 
		.append("isPrimary", this.isPrimary) 
		.toString();
	}
   
  

}