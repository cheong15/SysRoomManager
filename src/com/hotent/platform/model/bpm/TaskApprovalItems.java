package com.hotent.platform.model.bpm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:常用语管理 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-03-16 10:53:20
 */
@XmlRootElement(name = "taskApprovalItems")
@XmlAccessorType(XmlAccessType.NONE)
public class TaskApprovalItems extends BaseModel
{	

	public final static Short global = 1; 
	
	public final static Short notGlobal = 0; 
	
	// 主键
	@XmlAttribute
	protected Long itemId;
	// 节点设置ID
	@XmlAttribute
	protected Long setId;
	// actDefId
	@XmlAttribute
	protected String actDefId;
	// nodeId
	@XmlAttribute
	protected String nodeId;
	// 
	@XmlAttribute
	protected Short isGlobal;
	// expression
	@XmlAttribute
	protected String expItems;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSetId() {
		return setId;
	}

	public void setSetId(Long setId) {
		this.setId = setId;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Short getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(Short isGlobal) {
		this.isGlobal = isGlobal;
	}

	public String getExpItems() {
		return expItems;
	}

	public void setExpItems(String expItems) {
		this.expItems = expItems;
	}
	
   
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof TaskApprovalItems)) 
		{
			return false;
		}
		TaskApprovalItems rhs = (TaskApprovalItems) object;
		return new EqualsBuilder()
		.append(this.itemId, rhs.itemId)
		.append(this.setId, rhs.setId)
		.append(this.actDefId, rhs.actDefId)
		.append(this.nodeId, rhs.nodeId)
		.append(this.isGlobal, rhs.isGlobal)
		.append(this.expItems, rhs.expItems)
		.isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.itemId) 
		.append(this.setId) 
		.append(this.actDefId)
		.append(this.nodeId)
		.append(this.isGlobal)
		.append(this.expItems) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("itemId", this.itemId) 
		.append("setId", this.setId) 
		.append("actDefId", this.actDefId) 
		.append("nodeId", this.nodeId) 
		.append("isGlobal", this.isGlobal) 
		.append("expItems", this.expItems)
		.toString();
	}
   
  

}