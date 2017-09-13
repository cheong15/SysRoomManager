package com.hotent.platform.service.bpm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import com.hotent.core.bpm.model.ProcessExecution;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.model.TaskExecutor;
import com.hotent.core.page.PageBean;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.dao.bpm.ExecutionDao;
import com.hotent.platform.dao.bpm.HistoryProcessInstanceDao;
import com.hotent.platform.dao.bpm.TaskOpinionDao;
import com.hotent.platform.model.bpm.TaskOpinion;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.system.SysUserService;
import com.hotent.platform.service.util.ServiceUtil;
import com.hotent.platform.webservice.model.BpmFinishTask;

/**
 * 对象功能:流程任务审批意见 Service类 
 * 开发公司:广州宏天软件有限公司 
 * 开发人员:csx 
 * 创建时间:2012-01-11 16:06:11
 */
@Service
public class TaskOpinionService extends BaseService<TaskOpinion> {
	@Resource
	private TaskOpinionDao dao;

	@Resource
	private ExecutionDao executionDao;
	@Resource
	private HistoryProcessInstanceDao historyProcessInstanceDao;
	@Resource
	private BpmService bpmService;
	
	@Resource
	private SysUserService sysUserService;
	@Resource
	private TaskUserService taskUserService;

	public TaskOpinionService() {
	}

	@Override
	protected IEntityDao<TaskOpinion, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 取得对应该任务的执行
	 * 
	 * @param taskId
	 * @return
	 */
	public TaskOpinion getByTaskId(Long taskId) {
		return dao.getByTaskId(taskId);
	}

	/**
	 * 取得某个任务的所有审批意见 按时间排序
	 * 
	 * @param actInstId
	 * @return
	 */
	public List<TaskOpinion> getByActInstId(String actInstId,boolean isAsc) {
		// 根据actInstId获取所以相关的流程实例ID
		List<String> actInstIds = new ArrayList<String>();
		
		ProcessExecution execution = executionDao.getById(actInstId);
		if (execution != null && !StringUtil.isEmpty(execution.getSuperExecutionId())) {
			putRelateActInstIdIntoList(actInstIds,  actInstId);
		}
		else{
			putSubActInstIdIntoList(actInstIds,  actInstId);
		}
		
		return dao.getByActInstId(actInstIds,isAsc);
	}
	/**
	 * 取得某个任务的所有审批意见 
	 * 
	 * @param actInstId
	 * @return
	 */
	public List<TaskOpinion> getByActInstId(String actInstId) {
		return getByActInstId( actInstId,true);
	}
	
	/**
	 * 递归获取所有关联的流程实例ID.(根据子流程获取所有父流程ID)
	 * 
	 * @param actInstIds
	 * @param actInstId
	 */
	private void putRelateActInstIdIntoList(List<String> actInstIds, String actInstId) {
		actInstIds.add(actInstId);
		ProcessExecution execution = executionDao.getById(actInstId);
		if (execution != null && !StringUtil.isEmpty(execution.getSuperExecutionId())) {
			ProcessExecution superExecution = executionDao.getById(execution.getSuperExecutionId());
			if (superExecution != null) {
				putRelateActInstIdIntoList(actInstIds, superExecution.getProcessInstanceId());
			}
		}
	}
	
	private void putSubActInstIdIntoList(List<String> actInstIds, String actInstId){
		actInstIds.add(actInstId);
		List<HistoricProcessInstanceEntity> list= historyProcessInstanceDao.getBySuperId(actInstId);
		if(list.size()>0){
			for(HistoricProcessInstanceEntity excution:list){
				putSubActInstIdIntoList(actInstIds,excution.getProcessInstanceId());
			}
		}
	}

	/**
	 * 根据act流程定义Id删除对应在流程任务审批意见
	 * 
	 * @param actDefId
	 */
	public void delByActDefIdTaskOption(String actDefId) {
		dao.delByActDefIdTaskOption(actDefId);
	}

	/**
	 * 根据流程实例Id及任务定义Key取得审批列表
	 * 
	 * @param actInstId
	 * @param taskKey
	 * @return
	 */
	public List<TaskOpinion> getByActInstIdTaskKey(Long actInstId, String taskKey) {
		return dao.getByActInstIdTaskKey(actInstId, taskKey);
	}
	
	/**
	 * 取到最新的某个节点的审批记录
	 * 
	 * @param actInstId
	 * @param taskKey
	 * @return
	 */
	public TaskOpinion getLatestTaskOpinion(Long actInstId, String taskKey) {
		List<TaskOpinion> list = getByActInstIdTaskKey(actInstId, taskKey);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得某个流程实例中，某用户最新的完成的审批记录
	 * 
	 * @param actInstId
	 * @param exeUserId
	 * @return
	 */
	public TaskOpinion getLatestUserOpinion(String actInstId, Long exeUserId) {
		List<TaskOpinion> taskOpinions = dao.getByActInstIdExeUserId(actInstId, exeUserId);
		if (taskOpinions.size() == 0)
			return null;
		return taskOpinions.get(0);
	}

	/**
	 * 按任务ID删除
	 * 
	 * @param taskId
	 */
	public void delByTaskId(Long taskId) {
		dao.delByTaskId(taskId);
	}

	/**
	 * 取得已经审批完成的任务
	 * 
	 * @param userId
	 * @param subject
	 *            事项名
	 * @param taskName
	 *            任务名
	 * @param pb
	 *            分页
	 * @return
	 */
	public List<BpmFinishTask> getByFinishTask(Long userId, String subject, String taskName, PageBean pb) {
		return dao.getByFinishTask(userId, subject, taskName, pb);
	}
	
	
	/**
	 * 获取正在审批的意见。
	 * @param actInstId
	 * @return
	 */
	public List<TaskOpinion> getCheckOpinionByInstId(Long actInstId){
		return dao.getCheckOpinionByInstId(actInstId);
	}

	public List<TaskOpinion> setTaskOpinionExecutor(List<TaskOpinion> opinions){
		for (TaskOpinion taskOpinion : opinions) {
			if(taskOpinion.getCheckStatus()==TaskOpinion.STATUS_CHECKING){
				TaskEntity task = bpmService.getTask(taskOpinion.getTaskId().toString());
				if(BeanUtils.isNotEmpty(task) ){
					//执行人不为空
					String assignee=task.getAssignee();
					if(ServiceUtil.isAssigneeNotEmpty(assignee)){
						String fullname = "";
						SysUser sysuser = sysUserService.getById(new Long(assignee));
						if(BeanUtils.isNotEmpty(sysuser))
							fullname =  sysuser.getFullname();
						taskOpinion.setExeFullname(fullname);
					}
					//获取候选人
					else{
						 Set<TaskExecutor> set= taskUserService.getCandidateExecutors(task.getId());
//						 String fullname="";
						 Set<SysUser> sysUsers=new HashSet<SysUser>();
						 for(Iterator<TaskExecutor> it=set.iterator();it.hasNext();){
							 TaskExecutor taskExe=it.next();
							 if(taskExe==null){
								 continue;
							 }
							 Set<SysUser> users = taskExe.getSysUser();
							 sysUsers.addAll(users);
						 }
						 if(taskOpinion.getCandidateUsers()==null){
							 taskOpinion.setCandidateUsers(new ArrayList<SysUser>());
						 }
						taskOpinion.getCandidateUsers().addAll(sysUsers);
					}
				}
			}
		}
		return opinions;
	}
	
	/**
	 * 设置执行人的名称
	 * @param list
	 * @return
	 */
	public List<TaskOpinion> setTaskOpinionExeFullname(List<TaskOpinion> list) {
		for (TaskOpinion taskOpinion : list) {
			if(taskOpinion.getCheckStatus()==TaskOpinion.STATUS_CHECKING){
				TaskEntity task = bpmService.getTask(taskOpinion.getTaskId().toString());
				if(BeanUtils.isNotEmpty(task) ){
					//执行人为空
					String assignee=task.getAssignee();
					if(ServiceUtil.isAssigneeNotEmpty(assignee)){
						String fullname = "";
						SysUser sysuser = sysUserService.getById(new Long(assignee));
						if(BeanUtils.isNotEmpty(sysuser))
							fullname =  sysuser.getFullname();
						taskOpinion.setExeFullname(fullname);
					}
					//获取候选人
					else{
						 Set<TaskExecutor> set= taskUserService.getCandidateExecutors(task.getId());
						 String fullname="";
						 for(Iterator<TaskExecutor> it=set.iterator();it.hasNext();){
							 TaskExecutor taskExe=it.next();
							 String type=taskExe.getType();
							 if(taskExe.getType().equals(TaskExecutor.USER_TYPE_USER)){
								  fullname +=  sysUserService.getById(new Long(taskExe.getExecuteId())).getFullname() +"<br/>";								 
							 } 
						 }
						taskOpinion.setExeFullname(fullname);
						taskOpinion.setCandidateUser(fullname);
					}
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 设置执行人的名称
	 * @param list
	 * @return
	 */
	public List<TaskOpinion> setTaskOpinionListExeFullname(List<TaskOpinion> list) {
		for(TaskOpinion taskOpinion:list){
			if(taskOpinion.getCheckStatus()==TaskOpinion.STATUS_CHECKING){
				TaskEntity task = bpmService.getTask(taskOpinion.getTaskId().toString());
				if(BeanUtils.isNotEmpty(task) ){
					//执行人为空
					String assignee=task.getAssignee();
					if(ServiceUtil.isAssigneeNotEmpty(assignee)){
						String fullname =  sysUserService.getById(new Long(assignee)).getFullname();
						taskOpinion.setExeFullname(fullname);
					}
					//获取候选人
					else{
						 Set<TaskExecutor> set= taskUserService.getCandidateExecutors(task.getId());
						 String fullname="";
						 for(Iterator<TaskExecutor> it=set.iterator();it.hasNext();){
							 TaskExecutor taskExe=it.next();
							 String type=taskExe.getType();
							 if(taskExe.getType().equals(TaskExecutor.USER_TYPE_USER)){
								  fullname +=  sysUserService.getById(new Long(assignee)).getFullname() +"<br/>";								 
							 } 
						 }
						//taskOpinion.setExeFullname(fullname);
						taskOpinion.setCandidateUser(fullname);
					}
				}else{
					if(BeanUtils.isNotEmpty(taskOpinion.getExeUserId())){
						String fullname =  sysUserService.getById(new Long(taskOpinion.getExeUserId())).getFullname();
						taskOpinion.setExeFullname(fullname);
					}
				}
			}
			else{
				String fullname =  sysUserService.getById(new Long(taskOpinion.getExeUserId())).getFullname();
				taskOpinion.setExeFullname(fullname);
			}
		}
		return list;
	}
	
	
	/**
	 * 根据实例节点获取任务实例状态。
	 * @param actInstId
	 * @param taskKey
	 * @param checkStatus
	 * @return
	 */
	public List<TaskOpinion> getByActInstIdTaskKeyStatus(String actInstId,
			String taskKey, Short checkStatus){
		return dao.getByActInstIdTaskKeyStatus(actInstId,taskKey,checkStatus);
	}
	
	public TaskOpinion getOpinionByTaskId(Long taskId,Long userId) {
		return dao.getOpinionByTaskId(taskId, userId);
	}
	
	
	/**
	 * 根据actInstId更新。
	 * @param actInstId
	 * @param oldActInstId
	 * @return
	 */
	public int updateActInstId(String actInstId,String oldActInstId){
		return dao.updateActInstId(actInstId, oldActInstId);
	}
}
