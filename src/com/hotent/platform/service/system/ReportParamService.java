package com.hotent.platform.service.system;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.platform.model.system.ReportParam;
import com.hotent.platform.dao.system.ReportParamDao;

/**
 * 对象功能:报表参数 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-12 11:08:12
 */
@Service
public class ReportParamService extends BaseService<ReportParam>
{
	@Resource
	private ReportParamDao dao;
	
	public ReportParamService()
	{
	}
	
	@Override
	protected IEntityDao<ReportParam, Long> getEntityDao() 
	{
		return dao;
	}
}
