package com.hotent.demo.model.LigerUI;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.core.model.BaseModel;
/**
 * 页内编辑多种编辑器模式
 */
public class LgmoreLg extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *姓名
	 */
	protected String  name;
	/**
	 *性别
	 */
	protected String  sex;
	/**
	 *生日
	 */
	protected java.util.Date  birthday;
	/**
	 *年薪
	 */
	protected Long  money;
	/**
	 *toWife
	 */
	protected Long  toWife;
	/**
	 *入职日期
	 */
	protected java.util.Date  joinDate;
	/**
	 *结婚日期
	 */
	protected java.util.Date  jiehunDay;
	/**
	 *工作性质
	 */
	protected String  jobType;
	/**
	 *备注
	 */
	protected String  comment;
	/**
	 *选择用户
	 */
	protected String  selectUser;
	/**
	 *选择用户ID
	 */
	protected String  selectUserID;
	/**
	 *选择组织
	 */
	protected String  selectOrg;
	/**
	 *选择组织ID
	 */
	protected String  selectOrgID;
	/**
	 *选择角色
	 */
	protected String  selectRole;
	/**
	 *选择角色ID
	 */
	protected String  selectRoleID;
	/**
	 *选择岗位
	 */
	protected String  selectJob;
	/**
	 *选择岗位ID
	 */
	protected String  selectJobID;
	/**
	 *下拉选项
	 */
	protected String  selectOpinion;
	/**
	 *图片控件
	 */
	protected String  picSelect;
	/**
	 *文件上传
	 */
	protected String  fileUpload;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 姓名
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setSex(String sex) 
	{
		this.sex = sex;
	}
	/**
	 * 返回 性别
	 * @return
	 */
	public String getSex() 
	{
		return this.sex;
	}
	public void setBirthday(java.util.Date birthday) 
	{
		this.birthday = birthday;
	}
	/**
	 * 返回 生日
	 * @return
	 */
	public java.util.Date getBirthday() 
	{
		return this.birthday;
	}
	public void setMoney(Long money) 
	{
		this.money = money;
	}
	/**
	 * 返回 年薪
	 * @return
	 */
	public Long getMoney() 
	{
		return this.money;
	}
	public void setToWife(Long toWife) 
	{
		this.toWife = toWife;
	}
	/**
	 * 返回 toWife
	 * @return
	 */
	public Long getToWife() 
	{
		return this.toWife;
	}
	public void setJoinDate(java.util.Date joinDate) 
	{
		this.joinDate = joinDate;
	}
	/**
	 * 返回 入职日期
	 * @return
	 */
	public java.util.Date getJoinDate() 
	{
		return this.joinDate;
	}
	public void setJiehunDay(java.util.Date jiehunDay) 
	{
		this.jiehunDay = jiehunDay;
	}
	/**
	 * 返回 结婚日期
	 * @return
	 */
	public java.util.Date getJiehunDay() 
	{
		return this.jiehunDay;
	}
	public void setJobType(String jobType) 
	{
		this.jobType = jobType;
	}
	/**
	 * 返回 工作性质
	 * @return
	 */
	public String getJobType() 
	{
		return this.jobType;
	}
	public void setComment(String comment) 
	{
		this.comment = comment;
	}
	/**
	 * 返回 备注
	 * @return
	 */
	public String getComment() 
	{
		return this.comment;
	}
	public void setSelectUser(String selectUser) 
	{
		this.selectUser = selectUser;
	}
	/**
	 * 返回 选择用户
	 * @return
	 */
	public String getSelectUser() 
	{
		return this.selectUser;
	}
	public void setSelectUserID(String selectUserID) 
	{
		this.selectUserID = selectUserID;
	}
	/**
	 * 返回 选择用户ID
	 * @return
	 */
	public String getSelectUserID() 
	{
		return this.selectUserID;
	}
	public void setSelectOrg(String selectOrg) 
	{
		this.selectOrg = selectOrg;
	}
	/**
	 * 返回 选择组织
	 * @return
	 */
	public String getSelectOrg() 
	{
		return this.selectOrg;
	}
	public void setSelectOrgID(String selectOrgID) 
	{
		this.selectOrgID = selectOrgID;
	}
	/**
	 * 返回 选择组织ID
	 * @return
	 */
	public String getSelectOrgID() 
	{
		return this.selectOrgID;
	}
	public void setSelectRole(String selectRole) 
	{
		this.selectRole = selectRole;
	}
	/**
	 * 返回 选择角色
	 * @return
	 */
	public String getSelectRole() 
	{
		return this.selectRole;
	}
	public void setSelectRoleID(String selectRoleID) 
	{
		this.selectRoleID = selectRoleID;
	}
	/**
	 * 返回 选择角色ID
	 * @return
	 */
	public String getSelectRoleID() 
	{
		return this.selectRoleID;
	}
	public void setSelectJob(String selectJob) 
	{
		this.selectJob = selectJob;
	}
	/**
	 * 返回 选择岗位
	 * @return
	 */
	public String getSelectJob() 
	{
		return this.selectJob;
	}
	public void setSelectJobID(String selectJobID) 
	{
		this.selectJobID = selectJobID;
	}
	/**
	 * 返回 选择岗位ID
	 * @return
	 */
	public String getSelectJobID() 
	{
		return this.selectJobID;
	}
	public void setSelectOpinion(String selectOpinion) 
	{
		this.selectOpinion = selectOpinion;
	}
	/**
	 * 返回 下拉选项
	 * @return
	 */
	public String getSelectOpinion() 
	{
		return this.selectOpinion;
	}
	public void setPicSelect(String picSelect) 
	{
		this.picSelect = picSelect;
	}
	/**
	 * 返回 图片控件
	 * @return
	 */
	public String getPicSelect() 
	{
		return this.picSelect;
	}
	public void setFileUpload(String fileUpload) 
	{
		this.fileUpload = fileUpload;
	}
	/**
	 * 返回 文件上传
	 * @return
	 */
	public String getFileUpload() 
	{
		return this.fileUpload;
	}
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof LgmoreLg)) 
		{
			return false;
		}
		LgmoreLg rhs = (LgmoreLg) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.name, rhs.name)
		.append(this.sex, rhs.sex)
		.append(this.birthday, rhs.birthday)
		.append(this.money, rhs.money)
		.append(this.toWife, rhs.toWife)
		.append(this.joinDate, rhs.joinDate)
		.append(this.jiehunDay, rhs.jiehunDay)
		.append(this.jobType, rhs.jobType)
		.append(this.comment, rhs.comment)
		.append(this.selectUser, rhs.selectUser)
		.append(this.selectUserID, rhs.selectUserID)
		.append(this.selectOrg, rhs.selectOrg)
		.append(this.selectOrgID, rhs.selectOrgID)
		.append(this.selectRole, rhs.selectRole)
		.append(this.selectRoleID, rhs.selectRoleID)
		.append(this.selectJob, rhs.selectJob)
		.append(this.selectJobID, rhs.selectJobID)
		.append(this.selectOpinion, rhs.selectOpinion)
		.append(this.picSelect, rhs.picSelect)
		.append(this.fileUpload, rhs.fileUpload)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.name) 
		.append(this.sex) 
		.append(this.birthday) 
		.append(this.money) 
		.append(this.toWife) 
		.append(this.joinDate) 
		.append(this.jiehunDay) 
		.append(this.jobType) 
		.append(this.comment) 
		.append(this.selectUser) 
		.append(this.selectUserID) 
		.append(this.selectOrg) 
		.append(this.selectOrgID) 
		.append(this.selectRole) 
		.append(this.selectRoleID) 
		.append(this.selectJob) 
		.append(this.selectJobID) 
		.append(this.selectOpinion) 
		.append(this.picSelect) 
		.append(this.fileUpload) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("name", this.name) 
		.append("sex", this.sex) 
		.append("birthday", this.birthday) 
		.append("money", this.money) 
		.append("toWife", this.toWife) 
		.append("joinDate", this.joinDate) 
		.append("jiehunDay", this.jiehunDay) 
		.append("jobType", this.jobType) 
		.append("comment", this.comment) 
		.append("selectUser", this.selectUser) 
		.append("selectUserID", this.selectUserID) 
		.append("selectOrg", this.selectOrg) 
		.append("selectOrgID", this.selectOrgID) 
		.append("selectRole", this.selectRole) 
		.append("selectRoleID", this.selectRoleID) 
		.append("selectJob", this.selectJob) 
		.append("selectJobID", this.selectJobID) 
		.append("selectOpinion", this.selectOpinion) 
		.append("picSelect", this.picSelect) 
		.append("fileUpload", this.fileUpload) 
		.toString();
	}

}
