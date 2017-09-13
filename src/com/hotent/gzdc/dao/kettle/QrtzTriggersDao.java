package com.hotent.gzdc.dao.kettle;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.gzdc.model.kettle.QrtzTriggers;
/**
 *<pre>
 * 对象功能:QRTZ_TRIGGERS Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-07 17:37:45
 *</pre>
 */
@Repository
public class QrtzTriggersDao extends BaseDao<QrtzTriggers>
{
	@Override
	public Class<?> getEntityClass()
	{
		return QrtzTriggers.class;
	}

}