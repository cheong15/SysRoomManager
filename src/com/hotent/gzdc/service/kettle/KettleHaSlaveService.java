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
import com.hotent.gzdc.model.kettle.KettleHaSlave;
import com.hotent.gzdc.dao.kettle.KettleHaSlaveDao;

/**
 *<pre>
 * 对象功能:KETTLE_HA_SLAVE Service类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:13
 *</pre>
 */
@Service
public class KettleHaSlaveService extends BaseService<KettleHaSlave>
{
	@Resource
	private KettleHaSlaveDao dao;
	
	
	
	public KettleHaSlaveService()
	{
	}
	
	@Override
	protected IEntityDao<KettleHaSlave, Long> getEntityDao() 
	{
		return dao;
	}
	
	
}
