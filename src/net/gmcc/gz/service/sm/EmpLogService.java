package net.gmcc.gz.service.sm;
import javax.annotation.Resource;

import net.gmcc.gz.dao.sm.EmpLogDao;
import net.gmcc.gz.model.sm.EmpLog;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;

/**
 *<pre>
 * 对象功能:员工操作记录 Service类
 * 开发公司:从兴技术有限公司
 * 开发人员:John
 * 创建时间:2014-05-05 17:18:25
 *</pre>
 */
@Service
public class EmpLogService extends BaseService<EmpLog>
{
	@Resource
	private EmpLogDao dao;
	
	
	
	public EmpLogService()
	{
	}
	
	@Override
	protected IEntityDao<EmpLog, Long> getEntityDao() 
	{
		return dao;
	}
	
	
}
