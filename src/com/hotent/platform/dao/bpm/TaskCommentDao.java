/**
 * 对象功能:流程任务评论 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-05 17:02:32
 */
package com.hotent.platform.dao.bpm;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.bpm.TaskComment;

@Repository
public class TaskCommentDao extends BaseDao<TaskComment>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return TaskComment.class;
	}
}