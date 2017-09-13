package com.hotent.gzdc.model.kettle;

import java.util.ArrayList;
import java.util.List;
import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:KETTLE_MONITOR Model对象
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-09 10:49:11
 */
public class KettleMonitor extends BaseModel
{
	// ID
	protected Long  id;
	// 调度名称
	protected String  jobname;
	// 任务组
	protected String  jobgroup;
	// 调度文件
	protected String  jobfile;
	// 执行状态
	protected String  jobstatus;
	// 执行开始时间
	protected java.util.Date  startTime;
	// 执行结束时间
	protected java.util.Date  endTime;
	// 执行用时
	protected Float  continuedTime;
	// 执行日志
	protected String  logmsg;
	// 错误日志
	protected String  errmsg;
	// 读取行
	protected Long  linesRead;
	// 写入行
	protected Long  linesWritten;
	// 修改行
	protected Long  linesUpdated;
	// 输入行
	protected Long  linesInput;
	// 输出行
	protected Long  linesOutput;
	// 错误行
	protected Long  linesError;
	// 删除行
	protected Long  linesDeleted;
	// 用户ID
	protected String  userid;
	// 集群名称
	protected Long  idCluster;
	// 子服务器名称
	protected Long  idSlave;
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
	public void setJobname(String jobname) 
	{
		this.jobname = jobname;
	}
	/**
	 * 返回 调度名称
	 * @return
	 */
	public String getJobname() 
	{
		return this.jobname;
	}
	public void setJobgroup(String jobgroup) 
	{
		this.jobgroup = jobgroup;
	}
	/**
	 * 返回 任务组
	 * @return
	 */
	public String getJobgroup() 
	{
		return this.jobgroup;
	}
	public void setJobfile(String jobfile) 
	{
		this.jobfile = jobfile;
	}
	/**
	 * 返回 调度文件
	 * @return
	 */
	public String getJobfile() 
	{
		return this.jobfile;
	}
	public void setJobstatus(String jobstatus) 
	{
		this.jobstatus = jobstatus;
	}
	/**
	 * 返回 执行状态
	 * @return
	 */
	public String getJobstatus() 
	{
		return this.jobstatus;
	}
	public void setStartTime(java.util.Date startTime) 
	{
		this.startTime = startTime;
	}
	/**
	 * 返回 执行开始时间
	 * @return
	 */
	public java.util.Date getStartTime() 
	{
		return this.startTime;
	}
	public void setEndTime(java.util.Date endTime) 
	{
		this.endTime = endTime;
	}
	/**
	 * 返回 执行结束时间
	 * @return
	 */
	public java.util.Date getEndTime() 
	{
		return this.endTime;
	}
	public void setContinuedTime(Float continuedTime) 
	{
		this.continuedTime = continuedTime;
	}
	/**
	 * 返回 执行用时
	 * @return
	 */
	public Float getContinuedTime() 
	{
		return this.continuedTime;
	}
	public void setLogmsg(String logmsg) 
	{
		this.logmsg = logmsg;
	}
	/**
	 * 返回 执行日志
	 * @return
	 */
	public String getLogmsg() 
	{
		return this.logmsg;
	}
	public void setErrmsg(String errmsg) 
	{
		this.errmsg = errmsg;
	}
	/**
	 * 返回 错误日志
	 * @return
	 */
	public String getErrmsg() 
	{
		return this.errmsg;
	}
	public void setLinesRead(Long linesRead) 
	{
		this.linesRead = linesRead;
	}
	/**
	 * 返回 读取行
	 * @return
	 */
	public Long getLinesRead() 
	{
		return this.linesRead;
	}
	public void setLinesWritten(Long linesWritten) 
	{
		this.linesWritten = linesWritten;
	}
	/**
	 * 返回 写入行
	 * @return
	 */
	public Long getLinesWritten() 
	{
		return this.linesWritten;
	}
	public void setLinesUpdated(Long linesUpdated) 
	{
		this.linesUpdated = linesUpdated;
	}
	/**
	 * 返回 修改行
	 * @return
	 */
	public Long getLinesUpdated() 
	{
		return this.linesUpdated;
	}
	public void setLinesInput(Long linesInput) 
	{
		this.linesInput = linesInput;
	}
	/**
	 * 返回 输入行
	 * @return
	 */
	public Long getLinesInput() 
	{
		return this.linesInput;
	}
	public void setLinesOutput(Long linesOutput) 
	{
		this.linesOutput = linesOutput;
	}
	/**
	 * 返回 输出行
	 * @return
	 */
	public Long getLinesOutput() 
	{
		return this.linesOutput;
	}
	public void setLinesError(Long linesError) 
	{
		this.linesError = linesError;
	}
	/**
	 * 返回 错误行
	 * @return
	 */
	public Long getLinesError() 
	{
		return this.linesError;
	}
	public void setLinesDeleted(Long linesDeleted) 
	{
		this.linesDeleted = linesDeleted;
	}
	/**
	 * 返回 删除行
	 * @return
	 */
	public Long getLinesDeleted() 
	{
		return this.linesDeleted;
	}
	public void setUserid(String userid) 
	{
		this.userid = userid;
	}
	/**
	 * 返回 用户ID
	 * @return
	 */
	public String getUserid() 
	{
		return this.userid;
	}
	public void setIdCluster(Long idCluster) 
	{
		this.idCluster = idCluster;
	}
	/**
	 * 返回 集群名称
	 * @return
	 */
	public Long getIdCluster() 
	{
		return this.idCluster;
	}
	public void setIdSlave(Long idSlave) 
	{
		this.idSlave = idSlave;
	}
	/**
	 * 返回 子服务器名称
	 * @return
	 */
	public Long getIdSlave() 
	{
		return this.idSlave;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof KettleMonitor)) 
		{
			return false;
		}
		KettleMonitor rhs = (KettleMonitor) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.jobname, rhs.jobname)
		.append(this.jobgroup, rhs.jobgroup)
		.append(this.jobfile, rhs.jobfile)
		.append(this.jobstatus, rhs.jobstatus)
		.append(this.startTime, rhs.startTime)
		.append(this.endTime, rhs.endTime)
		.append(this.continuedTime, rhs.continuedTime)
		.append(this.logmsg, rhs.logmsg)
		.append(this.errmsg, rhs.errmsg)
		.append(this.linesRead, rhs.linesRead)
		.append(this.linesWritten, rhs.linesWritten)
		.append(this.linesUpdated, rhs.linesUpdated)
		.append(this.linesInput, rhs.linesInput)
		.append(this.linesOutput, rhs.linesOutput)
		.append(this.linesError, rhs.linesError)
		.append(this.linesDeleted, rhs.linesDeleted)
		.append(this.userid, rhs.userid)
		.append(this.idCluster, rhs.idCluster)
		.append(this.idSlave, rhs.idSlave)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.jobname) 
		.append(this.jobgroup) 
		.append(this.jobfile) 
		.append(this.jobstatus) 
		.append(this.startTime) 
		.append(this.endTime) 
		.append(this.continuedTime) 
		.append(this.logmsg) 
		.append(this.errmsg) 
		.append(this.linesRead) 
		.append(this.linesWritten) 
		.append(this.linesUpdated) 
		.append(this.linesInput) 
		.append(this.linesOutput) 
		.append(this.linesError) 
		.append(this.linesDeleted) 
		.append(this.userid) 
		.append(this.idCluster) 
		.append(this.idSlave) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("jobname", this.jobname) 
		.append("jobgroup", this.jobgroup) 
		.append("jobfile", this.jobfile) 
		.append("jobstatus", this.jobstatus) 
		.append("startTime", this.startTime) 
		.append("endTime", this.endTime) 
		.append("continuedTime", this.continuedTime) 
		.append("logmsg", this.logmsg) 
		.append("errmsg", this.errmsg) 
		.append("linesRead", this.linesRead) 
		.append("linesWritten", this.linesWritten) 
		.append("linesUpdated", this.linesUpdated) 
		.append("linesInput", this.linesInput) 
		.append("linesOutput", this.linesOutput) 
		.append("linesError", this.linesError) 
		.append("linesDeleted", this.linesDeleted) 
		.append("userid", this.userid) 
		.append("idCluster", this.idCluster) 
		.append("idSlave", this.idSlave) 
		.toString();
	}
   
  

}