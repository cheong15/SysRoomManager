package com.hotent.gzdc.dao.kettle;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.gzdc.model.kettle.KettleHaCluster;
/**
 *<pre>
 * 对象功能:KETTLE_HA_CLUSTER Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:55
 *</pre>
 */
@Repository
public class KettleHaClusterDao extends BaseDao<KettleHaCluster>
{
	@Override
	public Class<?> getEntityClass()
	{
		return KettleHaCluster.class;
	}

}