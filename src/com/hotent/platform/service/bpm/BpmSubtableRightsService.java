package com.hotent.platform.service.bpm;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.model.bpm.BpmSubtableRights;
import com.hotent.platform.dao.bpm.BpmSubtableRightsDao;

/**
 *<pre>
 * 对象功能:子表权限 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:wwz
 * 创建时间:2013-01-16 10:04:39
 *</pre>
 */
@Service
public class BpmSubtableRightsService extends BaseService<BpmSubtableRights>
{
	@Resource
	private BpmSubtableRightsDao dao;
	
	public BpmSubtableRightsService()
	{
	}
	
	@Override
	protected IEntityDao<BpmSubtableRights, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public BpmSubtableRights getByDefIdAndNodeId(String actDefId, String nodeId,Long tableId){
		return dao.getByDefIdAndNodeId(actDefId, nodeId, tableId);
	}
	
}
