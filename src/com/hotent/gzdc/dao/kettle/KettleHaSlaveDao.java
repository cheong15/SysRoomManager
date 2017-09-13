package com.hotent.gzdc.dao.kettle;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.gzdc.model.kettle.KettleHaSlave;
/**
 *<pre>
 * 对象功能:KETTLE_HA_SLAVE Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:13
 *</pre>
 */
@Repository
public class KettleHaSlaveDao extends BaseDao<KettleHaSlave>
{
	@Override
	public Class<?> getEntityClass()
	{
		return KettleHaSlave.class;
	}

}