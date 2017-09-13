package com.hotent.platform.service.system;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.platform.model.system.SysOfficeTemplate;
import com.hotent.platform.dao.system.SysOfficeTemplateDao;

/**
 * 对象功能:office模版 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2012-05-25 10:16:16
 */
@Service
public class SysOfficeTemplateService extends BaseService<SysOfficeTemplate>
{
	@Resource
	private SysOfficeTemplateDao dao;
	
	public SysOfficeTemplateService()
	{
	}
	
	@Override
	protected IEntityDao<SysOfficeTemplate, Long> getEntityDao() 
	{
		return dao;
	}
}
