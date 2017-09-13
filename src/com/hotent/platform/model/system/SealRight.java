package com.hotent.platform.model.system;

import java.util.Date;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:电子印章权限 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:raise
 * 创建时间:2012-08-29 11:25:59
 */
public class SealRight extends BaseModel
{
	
	//权限Id
	protected Long  id;
	//电子印章Id
	protected Long  sealId;
	//受权类型
	protected String  rightType;	
	//受权对象ID
	protected Long  rightId;
	//受权对象名称
	protected String  rightName;
	//创建人
	protected Long  createUser;	
	// 建立时间
	private Date createTime;
	
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getSealId()
	{
		return sealId;
	}
	public void setSealId(Long sealId)
	{
		this.sealId = sealId;
	}
	public String getRightType()
	{
		return rightType;
	}
	public void setRightType(String rightType)
	{
		this.rightType = rightType;
	}
	public Long getRightId()
	{
		return rightId;
	}
	public void setRightId(Long rightId)
	{
		this.rightId = rightId;
	}
	public String getRightName()
	{
		return rightName;
	}
	public void setRightName(String rightName)
	{
		this.rightName = rightName;
	}
	public Long getCreateUser()
	{
		return createUser;
	}
	public void setCreateUser(Long createUser)
	{
		this.createUser = createUser;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

}