package net.gmcc.gz.model.attach;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 对象功能:上传附件管理 Model对象
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:lchh
 * 创建时间:2017-09-06 17:31:55
 */
public class Attachment
{
	// 文件ID（UUID）
	protected String  fileid;
	// 业务类型：系统公告、项目公告等；用于区分不同业务上传的附件
	protected String  businessType;
	// 文件类型：如项目分了澄清、应答公告等；用于区分业务里面的分类上传的附件
	protected String  fileType;
	// 业务标识ID
	protected String  businessId;
	// 原始上传文件名
	protected String  oriFilename;
	// 重命名后的新文件名
	protected String  newFilename;
	// 文件存储路径
	protected String  filePath;
	// 文件序号（服务器端实现对文件的序号排序）
	protected Long  sort;
	// 文件后缀名
	protected String  fileSubpfix;
	// 文件图标名称
	protected String  fileIcon;
	// 文件大小（单位：KB ）
	protected Long  fileSize;
	// 文件上传时间
	protected java.util.Date  uploadTime;
	// 上传人ID
	protected String  uploaderId;
	// 上传人名称
	protected String  uploaderName;
	// 备注
	protected String  remark;
	public void setFileid(String fileid) 
	{
		this.fileid = fileid;
	}
	/**
	 * 返回 文件ID（UUID）
	 * @return
	 */
	public String getFileid() 
	{
		return this.fileid;
	}
	public void setBusinessType(String businessType) 
	{
		this.businessType = businessType;
	}
	/**
	 * 返回 业务类型：系统公告、项目公告等；用于区分不同业务上传的附件
	 * @return
	 */
	public String getBusinessType() 
	{
		return this.businessType;
	}
	public void setFileType(String fileType) 
	{
		this.fileType = fileType;
	}
	/**
	 * 返回 文件类型：如项目分了澄清、应答公告等；用于区分业务里面的分类上传的附件
	 * @return
	 */
	public String getFileType() 
	{
		return this.fileType;
	}
	public void setBusinessId(String businessId) 
	{
		this.businessId = businessId;
	}
	/**
	 * 返回 业务标识ID
	 * @return
	 */
	public String getBusinessId() 
	{
		return this.businessId;
	}
	public void setOriFilename(String oriFilename) 
	{
		this.oriFilename = oriFilename;
	}
	/**
	 * 返回 原始上传文件名
	 * @return
	 */
	public String getOriFilename() 
	{
		return this.oriFilename;
	}
	public void setNewFilename(String newFilename) 
	{
		this.newFilename = newFilename;
	}
	/**
	 * 返回 重命名后的新文件名
	 * @return
	 */
	public String getNewFilename() 
	{
		return this.newFilename;
	}
	public void setFilePath(String filePath) 
	{
		this.filePath = filePath;
	}
	/**
	 * 返回 文件存储路径
	 * @return
	 */
	public String getFilePath() 
	{
		return this.filePath;
	}
	public void setSort(Long sort) 
	{
		this.sort = sort;
	}
	/**
	 * 返回 文件序号（服务器端实现对文件的序号排序）
	 * @return
	 */
	public Long getSort() 
	{
		return this.sort;
	}
	public void setFileSubpfix(String fileSubpfix) 
	{
		this.fileSubpfix = fileSubpfix;
	}
	/**
	 * 返回 文件后缀名
	 * @return
	 */
	public String getFileSubpfix() 
	{
		return this.fileSubpfix;
	}
	public void setFileIcon(String fileIcon) 
	{
		this.fileIcon = fileIcon;
	}
	/**
	 * 返回 文件图标名称
	 * @return
	 */
	public String getFileIcon() 
	{
		return this.fileIcon;
	}
	public void setFileSize(Long fileSize) 
	{
		this.fileSize = fileSize;
	}
	/**
	 * 返回 文件大小（单位：KB ）
	 * @return
	 */
	public Long getFileSize() 
	{
		return this.fileSize;
	}
	public void setUploadTime(java.util.Date uploadTime) 
	{
		this.uploadTime = uploadTime;
	}
	/**
	 * 返回 文件上传时间
	 * @return
	 */
	public java.util.Date getUploadTime() 
	{
		return this.uploadTime;
	}
	public void setUploaderId(String uploaderId) 
	{
		this.uploaderId = uploaderId;
	}
	/**
	 * 返回 上传人ID
	 * @return
	 */
	public String getUploaderId() 
	{
		return this.uploaderId;
	}
	public void setUploaderName(String uploaderName) 
	{
		this.uploaderName = uploaderName;
	}
	/**
	 * 返回 上传人名称
	 * @return
	 */
	public String getUploaderName() 
	{
		return this.uploaderName;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}
	/**
	 * 返回 备注
	 * @return
	 */
	public String getRemark() 
	{
		return this.remark;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Attachment)) 
		{
			return false;
		}
		Attachment rhs = (Attachment) object;
		return new EqualsBuilder()
		.append(this.fileid, rhs.fileid)
		.append(this.businessType, rhs.businessType)
		.append(this.fileType, rhs.fileType)
		.append(this.businessId, rhs.businessId)
		.append(this.oriFilename, rhs.oriFilename)
		.append(this.newFilename, rhs.newFilename)
		.append(this.filePath, rhs.filePath)
		.append(this.sort, rhs.sort)
		.append(this.fileSubpfix, rhs.fileSubpfix)
		.append(this.fileIcon, rhs.fileIcon)
		.append(this.fileSize, rhs.fileSize)
		.append(this.uploadTime, rhs.uploadTime)
		.append(this.uploaderId, rhs.uploaderId)
		.append(this.uploaderName, rhs.uploaderName)
		.append(this.remark, rhs.remark)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.fileid) 
		.append(this.businessType) 
		.append(this.fileType) 
		.append(this.businessId) 
		.append(this.oriFilename) 
		.append(this.newFilename) 
		.append(this.filePath) 
		.append(this.sort) 
		.append(this.fileSubpfix) 
		.append(this.fileIcon) 
		.append(this.fileSize) 
		.append(this.uploadTime) 
		.append(this.uploaderId) 
		.append(this.uploaderName) 
		.append(this.remark) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("fileid", this.fileid) 
		.append("businessType", this.businessType) 
		.append("fileType", this.fileType) 
		.append("businessId", this.businessId) 
		.append("oriFilename", this.oriFilename) 
		.append("newFilename", this.newFilename) 
		.append("filePath", this.filePath) 
		.append("sort", this.sort) 
		.append("fileSubpfix", this.fileSubpfix) 
		.append("fileIcon", this.fileIcon) 
		.append("fileSize", this.fileSize) 
		.append("uploadTime", this.uploadTime) 
		.append("uploaderId", this.uploaderId) 
		.append("uploaderName", this.uploaderName) 
		.append("remark", this.remark) 
		.toString();
	}
   
  

}