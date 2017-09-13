/**
 * 对象功能:报表参数 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-12 11:08:12
 */
package com.hotent.platform.dao.system;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.system.ReportParam;

@Repository
public class ReportParamDao extends BaseDao<ReportParam>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return ReportParam.class;
	}
}