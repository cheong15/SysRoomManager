package com.hotent.gzdc.dao.kettle;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.gzdc.model.kettle.KettleRepository;
/**
 *<pre>
 * 对象功能:KETTLE_REPOSITORY Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-10 09:31:32
 *</pre>
 */
@Repository
public class KettleRepositoryDao extends BaseDao<KettleRepository>
{
	@Override
	public Class<?> getEntityClass()
	{
		return KettleRepository.class;
	}

}