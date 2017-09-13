package com.hotent.platform.model.system;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:系统岗位表 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
public class Position extends BaseModel
{
	/**
	 * 根结点的父ID
	 */
	public final static long ROOT_PID=-1;//重要
	/**
	 * 根结点的ID
	 */
	public final static long ROOT_ID=0;
	/**
	 * 根结点的深度
	 */
	public final static int ROOT_DEPTH=0;
	
	/**
	 *  是否父类
	 */
	public final static String IS_PARENT_N="false";
	public final static String IS_PARENT_Y="true";
	/**
	 * 是否叶子(0否,1是)
	 */
	public final static int IS_LEAF_N=0;
	public final static int IS_LEAF_Y=1;
	
	// posId
	protected Long posId;
	// 岗位名称
	protected String posName;
	// 岗位描述
	protected String posDesc;
	// parentId
	protected Long parentId;
	// nodePath
	protected String nodePath;
	// 层次
	protected Integer depth;
	// sn
	protected Short sn;
	
	protected Short isPrimary;
	
	protected String open="true";
	
	// 是否父类,主要用于树的展示时用
	protected String isParent;
	// 是否叶子结点(0否,1是),主要用于数据库保存
	protected Integer isLeaf;
	
	//职群名称
	protected String posgroup;

	
	public Integer getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
		if(isLeaf!=null&&isLeaf.equals(IS_LEAF_Y)){
			this.isParent=IS_PARENT_N;
		}else if(isLeaf!=null&&isLeaf.equals(IS_LEAF_N)){
			this.isParent=IS_PARENT_Y;
		}else{
			this.isParent=null;
		}
	}

	
	
	
	
	public String getIsParent() {
		if(this.isLeaf==null)return IS_PARENT_Y;
		else
		return this.isLeaf==IS_LEAF_Y?IS_PARENT_N:IS_PARENT_Y;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
		if(isParent!=null&&isParent.equals(IS_PARENT_N)){
			this.isLeaf=IS_LEAF_Y;
		}else if(isParent!=null&&isParent.equals(IS_PARENT_Y)){
			this.isLeaf=IS_LEAF_N;
		}else{
			this.isLeaf=null;
		}
	}

	
	
	
	

	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}

	public void setPosId(Long posId) 
	{
		this.posId = posId;
	}
	/**
	 * 返回 posId
	 * @return
	 */
	public Long getPosId() 
	{
		return posId;
	}

	public void setPosName(String posName) 
	{
		this.posName = posName;
	}
	/**
	 * 返回 岗位名称
	 * @return
	 */
	public String getPosName() 
	{
		return posName;
	}

	public void setPosDesc(String posDesc) 
	{
		this.posDesc = posDesc;
	}
	/**
	 * 返回 岗位描述
	 * @return
	 */
	public String getPosDesc() 
	{
		return posDesc;
	}

	public void setParentId(Long parentId) 
	{
		this.parentId = parentId;
	}
	/**
	 * 返回 parentId
	 * @return
	 */
	public Long getParentId() 
	{
		return parentId;
	}

	public void setNodePath(String nodePath) 
	{
		this.nodePath = nodePath;
	}
	/**
	 * 返回 nodePath
	 * @return
	 */
	public String getNodePath() 
	{
		return nodePath;
	}

	public void setDepth(Integer depth) 
	{
		this.depth = depth;
	}
	/**
	 * 返回 层次
	 * @return
	 */
	public Integer getDepth() 
	{
		return depth;
	}

	public void setSn(Short sn) 
	{
		this.sn = sn;
	}
	/**
	 * 返回 sn
	 * @return
	 */
	public Short getSn() 
	{
		return sn;
	}

	public Short getIsPrimary()
	{
		return isPrimary;
	}
	public void setIsPrimary(Short isPrimary)
	{
		this.isPrimary = isPrimary;
	}
	
	
	
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Position)) 
		{
			return false;
		}
		Position rhs = (Position) object;
		return new EqualsBuilder()
		.append(this.posId, rhs.posId)
		.append(this.posName, rhs.posName)
		.append(this.posDesc, rhs.posDesc)
		.append(this.parentId, rhs.parentId)
		.append(this.nodePath, rhs.nodePath)
		.append(this.depth, rhs.depth)
		.append(this.sn, rhs.sn)
		.append(this.isPrimary, rhs.isPrimary)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.posId) 
		.append(this.posName) 
		.append(this.posDesc) 
		.append(this.parentId) 
		.append(this.nodePath) 
		.append(this.depth) 
		.append(this.sn) 
		.append(this.isPrimary) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("posId", this.posId) 
		.append("posName", this.posName) 
		.append("posDesc", this.posDesc) 
		.append("parentId", this.parentId) 
		.append("nodePath", this.nodePath) 
		.append("depth", this.depth) 
		.append("sn", this.sn) 
		.append("isPrimary", this.isPrimary)
		.toString();
	}
	public String getPosgroup() {
		return posgroup;
	}
	public void setPosgroup(String posgroup) {
		this.posgroup = posgroup;
	}
   
  

}