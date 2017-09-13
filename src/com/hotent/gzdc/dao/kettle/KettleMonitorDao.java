package com.hotent.gzdc.dao.kettle;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.gzdc.model.kettle.KettleMonitor;
/**
 *<pre>
 * 对象功能:KETTLE_MONITOR Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-09 10:49:11
 *</pre>
 */
@Repository
public class KettleMonitorDao extends BaseDao<KettleMonitor>
{
	@Override
	public Class<?> getEntityClass()
	{
		return KettleMonitor.class;
	}

}