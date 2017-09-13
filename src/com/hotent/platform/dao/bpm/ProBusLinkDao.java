package com.hotent.platform.dao.bpm;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.bpm.ProBusLink;
/**
 *<pre>
 * 对象功能:流程实例与业务关联表 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2013-07-17 14:01:11
 *</pre>
 */
@Repository
public class ProBusLinkDao extends BaseDao<ProBusLink>
{
	@Override
	public Class<?> getEntityClass()
	{
		return ProBusLink.class;
	}

}