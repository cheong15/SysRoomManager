package com.hotent.platform.service.bpm;

import java.util.Date;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.bpm.TaskCommentDao;
import com.hotent.platform.model.bpm.TaskComment;

/**
 * 对象功能:流程任务评论 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-05 17:02:32
 */
@Service
public class TaskCommentService extends BaseService<TaskComment>
{
	@Resource
	private TaskCommentDao dao;
	
	public TaskCommentService()
	{
	}
	
	@Override
	protected IEntityDao<TaskComment, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 添加任务评论
	 * @param taskComment
	 * @throws Exception
	 */
	public void addTaskComment(TaskComment taskComment,TaskEntity taskEntity) throws Exception{
		taskComment.setCommentId(UniqueIdUtil.genId());
		taskComment.setAuthorId(ContextUtil.getCurrentUserId());
		taskComment.setAuthor(ContextUtil.getCurrentUser().getFullname());
		taskComment.setNodeName(taskEntity.getName());
		taskComment.setCommentTime(new Date());
		dao.add(taskComment);
	}
	
}
