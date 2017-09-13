package com.hotent.platform.dao.bpm;

import org.springframework.stereotype.Repository;

import com.hotent.core.bpm.model.ProcessTaskHistory;
import com.hotent.core.db.BaseDao;
import com.hotent.core.db.GenericDao;
import com.hotent.platform.model.system.SysDataSource;

@Repository
public class TaskHistoryDao extends BaseDao<ProcessTaskHistory >
{
	
	
	@Override
	public Class getEntityClass()
	{
		return ProcessTaskHistory.class;
	}
}
