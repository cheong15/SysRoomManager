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
import com.hotent.gzdc.model.kettle.QrtzTriggers;
import com.hotent.gzdc.dao.kettle.QrtzTriggersDao;

/**
 *<pre>
 * 对象功能:QRTZ_TRIGGERS Service类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-07 17:37:45
 *</pre>
 */
@Service
public class QrtzTriggersService extends BaseService<QrtzTriggers>
{
	@Resource
	private QrtzTriggersDao dao;
	
	
	
	public QrtzTriggersService()
	{
	}
	
	@Override
	protected IEntityDao<QrtzTriggers, Long> getEntityDao() 
	{
		return dao;
	}
	
	
}
