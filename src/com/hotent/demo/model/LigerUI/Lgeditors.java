package com.hotent.demo.model.LigerUI;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.core.model.BaseModel;
/**
 * 对象功能:list页面带有树的Demo
 */
public class Lgeditors extends BaseModel
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
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Lgeditors)) 
		{
			return false;
		}
		Lgeditors rhs = (Lgeditors) object;
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
		.toString();
	}

}
