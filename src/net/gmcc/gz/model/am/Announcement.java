package net.gmcc.gz.model.am;

import java.util.List;

import net.gmcc.gz.model.attach.Attachment;
import net.gmcc.gz.util.CustomDateSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 对象功能:公告管理表 Model对象
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-09-07 18:34:39
 */
public class Announcement {
	// ID
	protected Long  id;
	// 公告标题
	protected String  title;
	// 公告正文
	protected String  contents;
	// 公告类型
	protected String  type;
	// 发布时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected java.util.Date  publishTime;
	// 过期时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected java.util.Date  overdueTime;
	// 公告状态
	protected String  status;
	// 创建人(portalid)
	protected String  creatorPortalid;
	// 创建人
	protected String  creatorName;
	// 创建时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected java.util.Date  createTime;
	// 修改人(portalid)
	protected String  editorPortalid;
	// 修改人
	protected String  editorName;
	// 修改时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected java.util.Date  editTime;
	// 审批人(portalid)
	protected String  checkerPortalid;
	// 审批人
	protected String  checkerName;
	// 审批时间
	protected java.util.Date  checkTime;
	// 发布部门
	protected String  publishDept;
	// 审批意见
	protected String  checkOpinion;
	
	//附件列表
	private List<Attachment> attachmentList;
	
	public void setId(Long id) 
	{
		this.id = id;
	}
	/**
	 * 返回 ID
	 * @return
	 */
	public Long getId() 
	{
		return this.id;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	/**
	 * 返回 公告标题
	 * @return
	 */
	public String getTitle() 
	{
		return this.title;
	}
	public void setContents(String contents) 
	{
		this.contents = contents;
	}
	/**
	 * 返回 公告正文
	 * @return
	 */
	public String getContents() 
	{
		return this.contents;
	}
	public void setType(String type) 
	{
		this.type = type;
	}
	/**
	 * 返回 公告类型
	 * @return
	 */
	public String getType() 
	{
		return this.type;
	}
	public void setPublishTime(java.util.Date publishTime) 
	{
		this.publishTime = publishTime;
	}
	/**
	 * 返回 发布时间
	 * @return
	 */
	public java.util.Date getPublishTime() 
	{
		return this.publishTime;
	}
	public void setOverdueTime(java.util.Date overdueTime) 
	{
		this.overdueTime = overdueTime;
	}
	/**
	 * 返回 过期时间
	 * @return
	 */
	public java.util.Date getOverdueTime() 
	{
		return this.overdueTime;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 公告状态
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	public void setCreatorPortalid(String creatorPortalid) 
	{
		this.creatorPortalid = creatorPortalid;
	}
	/**
	 * 返回 创建人(portalid)
	 * @return
	 */
	public String getCreatorPortalid() 
	{
		return this.creatorPortalid;
	}
	public void setCreatorName(String creatorName) 
	{
		this.creatorName = creatorName;
	}
	/**
	 * 返回 创建人
	 * @return
	 */
	public String getCreatorName() 
	{
		return this.creatorName;
	}
	public void setCreateTime(java.util.Date createTime) 
	{
		this.createTime = createTime;
	}
	/**
	 * 返回 创建时间
	 * @return
	 */
	public java.util.Date getCreateTime() 
	{
		return this.createTime;
	}
	public void setEditorPortalid(String editorPortalid) 
	{
		this.editorPortalid = editorPortalid;
	}
	/**
	 * 返回 修改人(portalid)
	 * @return
	 */
	public String getEditorPortalid() 
	{
		return this.editorPortalid;
	}
	public void setEditorName(String editorName) 
	{
		this.editorName = editorName;
	}
	/**
	 * 返回 修改人
	 * @return
	 */
	public String getEditorName() 
	{
		return this.editorName;
	}
	public void setEditTime(java.util.Date editTime) 
	{
		this.editTime = editTime;
	}
	/**
	 * 返回 修改时间
	 * @return
	 */
	public java.util.Date getEditTime() 
	{
		return this.editTime;
	}
	public void setCheckerPortalid(String checkerPortalid) 
	{
		this.checkerPortalid = checkerPortalid;
	}
	/**
	 * 返回 审核人(portalid)
	 * @return
	 */
	public String getCheckerPortalid() 
	{
		return this.checkerPortalid;
	}
	public void setCheckerName(String checkerName) 
	{
		this.checkerName = checkerName;
	}
	/**
	 * 返回 审核人
	 * @return
	 */
	public String getCheckerName() 
	{
		return this.checkerName;
	}
	public void setCheckTime(java.util.Date checkTime) 
	{
		this.checkTime = checkTime;
	}
	/**
	 * 返回 审核时间
	 * @return
	 */
	public java.util.Date getCheckTime() 
	{
		return this.checkTime;
	}
	public void setPublishDept(String publishDept) 
	{
		this.publishDept = publishDept;
	}
	/**
	 * 返回 发布部门
	 * @return
	 */
	public String getPublishDept() 
	{
		return this.publishDept;
	}

   	public String getCheckOpinion() {
		return checkOpinion;
	}
	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

}