package net.gmcc.gz.model.am;

import net.gmcc.gz.util.CustomDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 对象功能:公告管理表 Model对象
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-09-05 15:35:25
 */
@JsonIgnoreProperties({"reqHeaders"})
public class AnnouncementList{
	
	// ID
	protected Long  id;
	// 公告标题
	protected String  title;
	// 公告类型
	protected String  type;
	// 发布部门
	protected String  publishDept;
	// 发布时间
	@JsonSerialize(using = CustomDateSerializer.class)
	protected java.util.Date publishTime;
	// 过期时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected java.util.Date  overdueTime;
	// 公告状态
	protected String  status;
	// 请求头参数对象

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPublishDept() {
		return publishDept;
	}
	public void setPublishDept(String publishDept) {
		this.publishDept = publishDept;
	}
	public java.util.Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(java.util.Date publishTime) {
		this.publishTime = publishTime;
	}
	public java.util.Date getOverdueTime() {
		return overdueTime;
	}
	public void setOverdueTime(java.util.Date overdueTime) {
		this.overdueTime = overdueTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
}