package com.hotent.demo.model.LigerUI;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.core.model.BaseModel;
/**
 //用于联合查询的ligerui的 Demo
 */
public class Uiuser extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *用户名
	 */
	protected String  UIname;
	/**
	 *密码
	 */
	protected String  password;
	/**
	 *年龄
	 */
	protected String  age;
	/**
	 *生日
	 */
	protected java.util.Date  birthday;
	/**
	 *年薪
	 */
	protected Long  salary;
	/**
	 *是否党员
	 */
	protected String  isInPart;
	/**
	 *备注
	 */
	protected String  comment;
	/**
	 *家庭住址
	 */
	protected String  homeAddress;
	/**
	 *所属部门
	 */
	protected String  department;
	/**
	 *入职日期
	 */
	protected java.util.Date  joinDate;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUIname(String UIname) 
	{
		this.UIname = UIname;
	}
	/**
	 * 返回 用户名
	 * @return
	 */
	public String getUIname() 
	{
		return this.UIname;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	/**
	 * 返回 密码
	 * @return
	 */
	public String getPassword() 
	{
		return this.password;
	}
	public void setAge(String age) 
	{
		this.age = age;
	}
	/**
	 * 返回 年龄
	 * @return
	 */
	public String getAge() 
	{
		return this.age;
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
	public void setSalary(Long salary) 
	{
		this.salary = salary;
	}
	/**
	 * 返回 年薪
	 * @return
	 */
	public Long getSalary() 
	{
		return this.salary;
	}
	public void setIsInPart(String isInPart) 
	{
		this.isInPart = isInPart;
	}
	/**
	 * 返回 是否党员
	 * @return
	 */
	public String getIsInPart() 
	{
		return this.isInPart;
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
	public void setHomeAddress(String homeAddress) 
	{
		this.homeAddress = homeAddress;
	}
	/**
	 * 返回 家庭住址
	 * @return
	 */
	public String getHomeAddress() 
	{
		return this.homeAddress;
	}
	public void setDepartment(String department) 
	{
		this.department = department;
	}
	/**
	 * 返回 所属部门
	 * @return
	 */
	public String getDepartment() 
	{
		return this.department;
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
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Uiuser)) 
		{
			return false;
		}
		Uiuser rhs = (Uiuser) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.UIname, rhs.UIname)
		.append(this.password, rhs.password)
		.append(this.age, rhs.age)
		.append(this.birthday, rhs.birthday)
		.append(this.salary, rhs.salary)
		.append(this.isInPart, rhs.isInPart)
		.append(this.comment, rhs.comment)
		.append(this.homeAddress, rhs.homeAddress)
		.append(this.department, rhs.department)
		.append(this.joinDate, rhs.joinDate)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.UIname) 
		.append(this.password) 
		.append(this.age) 
		.append(this.birthday) 
		.append(this.salary) 
		.append(this.isInPart) 
		.append(this.comment) 
		.append(this.homeAddress) 
		.append(this.department) 
		.append(this.joinDate) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("UIname", this.UIname) 
		.append("password", this.password) 
		.append("age", this.age) 
		.append("birthday", this.birthday) 
		.append("salary", this.salary) 
		.append("isInPart", this.isInPart) 
		.append("comment", this.comment) 
		.append("homeAddress", this.homeAddress) 
		.append("department", this.department) 
		.append("joinDate", this.joinDate) 
		.toString();
	}

}
