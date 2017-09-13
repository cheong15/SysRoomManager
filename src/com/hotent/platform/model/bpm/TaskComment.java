package com.hotent.platform.model.bpm;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:流程任务评论Code: TASK_CMT Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-06 17:10:11
 */
public class TaskComment extends BaseModel
{
	// commentId
	protected Long commentId;
	// 流程定义ID
	protected String actDefId;
	// runId
	protected Long runId;
	// 评论人ID
	protected Long authorId;
	// 评论人
	protected String author;
	// 评论时间
	protected java.util.Date commentTime;
	// 评论内容
	protected String content;
	// 任务节点名称
	protected String nodeName;
	// 任务ID
	protected Long taskId;
	// 流程名称
	protected String subject;

	public void setCommentId(Long commentId) 
	{
		this.commentId = commentId;
	}
	/**
	 * 返回 commentId
	 * @return
	 */
	public Long getCommentId() 
	{
		return commentId;
	}

	public void setActDefId(String actDefId) 
	{
		this.actDefId = actDefId;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getActDefId() 
	{
		return actDefId;
	}

	public void setRunId(Long runId) 
	{
		this.runId = runId;
	}
	/**
	 * 返回 runId
	 * @return
	 */
	public Long getRunId() 
	{
		return runId;
	}

	public void setAuthorId(Long authorId) 
	{
		this.authorId = authorId;
	}
	/**
	 * 返回 评论人ID
	 * @return
	 */
	public Long getAuthorId() 
	{
		return authorId;
	}

	public void setAuthor(String author) 
	{
		this.author = author;
	}
	/**
	 * 返回 评论人
	 * @return
	 */
	public String getAuthor() 
	{
		return author;
	}

	public void setCommentTime(java.util.Date commentTime) 
	{
		this.commentTime = commentTime;
	}
	/**
	 * 返回 评论时间
	 * @return
	 */
	public java.util.Date getCommentTime() 
	{
		return commentTime;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}
	/**
	 * 返回 评论内容
	 * @return
	 */
	public String getContent() 
	{
		return content;
	}

	public void setNodeName(String nodeName) 
	{
		this.nodeName = nodeName;
	}
	/**
	 * 返回 任务节点名称
	 * @return
	 */
	public String getNodeName() 
	{
		return nodeName;
	}

	public void setTaskId(Long taskId) 
	{
		this.taskId = taskId;
	}
	/**
	 * 返回 任务ID
	 * @return
	 */
	public Long getTaskId() 
	{
		return taskId;
	}


	/**
	 * 返回 任务流程
	 * @return
	 */
	public String getSubject() 
	{
		return subject;
	}
   
	public void setSubject(String subject) 
	{
		this.subject = subject;
	}
	
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof TaskComment)) 
		{
			return false;
		}
		TaskComment rhs = (TaskComment) object;
		return new EqualsBuilder()
		.append(this.commentId, rhs.commentId)
		.append(this.actDefId, rhs.actDefId)
		.append(this.runId, rhs.runId)
		.append(this.authorId, rhs.authorId)
		.append(this.author, rhs.author)
		.append(this.commentTime, rhs.commentTime)
		.append(this.content, rhs.content)
		.append(this.nodeName, rhs.nodeName)
		.append(this.taskId, rhs.taskId)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.commentId) 
		.append(this.actDefId) 
		.append(this.runId) 
		.append(this.authorId) 
		.append(this.author) 
		.append(this.commentTime) 
		.append(this.content) 
		.append(this.nodeName) 
		.append(this.taskId) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("commentId", this.commentId) 
		.append("actDefId", this.actDefId) 
		.append("runId", this.runId) 
		.append("authorId", this.authorId) 
		.append("author", this.author) 
		.append("commentTime", this.commentTime) 
		.append("content", this.content) 
		.append("nodeName", this.nodeName) 
		.append("taskId", this.taskId) 
		.toString();
	}
   
  

}