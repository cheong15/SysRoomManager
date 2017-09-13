package com.hotent.platform.model.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:字段权限 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xwy
 * 创建时间:2012-02-14 16:55:15
 */
@XmlRootElement(name = "bpmFormRights")
@XmlAccessorType(XmlAccessType.NONE)
public class BpmFormRights extends BaseModel implements Cloneable
{
	public static final short FieldRights=1;
	
	public static final short TableRights=2;
	
	public static final short OpinionRights=3;
	
	public static final short TableShowRights=4;               //子表是否显示
	
	/** 无*/
	public static String TYPE_NONE = "none";
	/**所有人*/
	public static String TYPE_EVERYONE = "everyone";
	/**用户*/
	public static String TYPE_USER = "user";
	/**角色*/
	public static String TYPE_ROLE = "role";
	/**组织*/
	public static String TYPE_ORG = "org";
	/**组织负责人*/
	public static String TYPE_ORGMGR ="orgMgr";
	/**岗位*/
	public static String TYPE_POS ="pos";
	
	
	// id
	@XmlAttribute
	protected Long id;
	// 表单定义KEY
	@XmlAttribute
	protected Long formDefId;
	// 字段名
	@XmlAttribute
	protected String name;
	// 权限
	@XmlAttribute
	protected String permission = "";
	//权限类型(1,字段,2,子表,3,意见)
	@XmlAttribute
	protected short type=1;
	//流程定义ID
	@XmlAttribute
	protected String actDefId="";
	//流程任务ID
	@XmlAttribute
	protected String nodeId="";
	@XmlAttribute
	protected Integer sn=0;
	
	public BpmFormRights()
	{
	}
	
	public BpmFormRights(Long id, Long formDefId, String name, String permission,short type)
	{
		super();
		this.id = id;
		this.formDefId = formDefId;
		this.name = name;
		this.permission = permission;
		this.type=type;
	}
	public void setId(Long id) 
	{
		this.id = id;
	}
	/**
	 * 返回 id
	 * @return
	 */
	public Long getId() 
	{
		return id;
	}

	public void setFormDefId(Long formDefId) 
	{
		this.formDefId = formDefId;
	}
	/**
	 * 返回 表单定义ID
	 * @return
	 */
	public Long getFormDefId() 
	{
		return formDefId;
	}

	public void setName(String fieldName) 
	{
		this.name = fieldName;
	}
	/**
	 * 返回 字段名
	 * @return
	 */
	public String getName() 
	{
		return name;
	}

	public void setPermission(String permission) 
	{
		this.permission = permission;
	}
	/**
	 * 返回 权限
	 * @return
	 */
	public String getPermission() 
	{
		return permission;
	}
	
	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
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

   
   	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof BpmFormRights)) 
		{
			return false;
		}
		BpmFormRights rhs = (BpmFormRights) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.formDefId, rhs.formDefId)
		.append(this.name, rhs.name)
		.append(this.permission, rhs.permission)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.formDefId) 
		.append(this.name) 
		.append(this.permission) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("formDefId", this.formDefId) 
		.append("fieldName", this.name) 
		.append("permission", this.permission) 
		.toString();
	}
	
	
	public Object clone()
	{
		BpmFormRights obj=null;
		try{
			obj=(BpmFormRights)super.clone();
		}catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
   
  

}