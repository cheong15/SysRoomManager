package net.gmcc.gz.dao.sm;

import net.gmcc.gz.model.sm.EmpLog;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
/**
 *<pre>
 * 对象功能:员工操作记录 Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:John
 * 创建时间:2014-05-05 17:18:25
 *</pre>
 */
@Repository
public class EmpLogDao extends BaseDao<EmpLog>
{
	@Override
	public Class<?> getEntityClass()
	{
		return EmpLog.class;
	}

}