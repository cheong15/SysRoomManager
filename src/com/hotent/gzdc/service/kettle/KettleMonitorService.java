package com.hotent.gzdc.service.kettle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.gzdc.model.kettle.KettleMonitor;
import com.hotent.gzdc.dao.kettle.KettleMonitorDao;

/**
 *<pre>
 * 对象功能:KETTLE_MONITOR Service类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-09 10:49:11
 *</pre>
 */
@Service
public class KettleMonitorService extends BaseService<KettleMonitor>
{
	@Resource
	private KettleMonitorDao dao;
	
	
	
	public KettleMonitorService()
	{
	}
	
	@Override
	protected IEntityDao<KettleMonitor, Long> getEntityDao() 
	{
		return dao;
	}
	
	
}
