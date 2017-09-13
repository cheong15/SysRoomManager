package com.hotent.platform.service.bpm;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.platform.model.bpm.BpmProTransTo;
import com.hotent.platform.dao.bpm.BpmProTransToDao;

/**
 * 对象功能:流程流转状态 Service类 开发公司:广州宏天软件有限公司 开发人员:helh 创建时间:2013-09-18 09:32:55
 */
@Service
public class BpmProTransToService extends BaseService<BpmProTransTo> {
	@Resource
	private BpmProTransToDao dao;

	public BpmProTransToService() {
	}

	@Override
	protected IEntityDao<BpmProTransTo, Long> getEntityDao() {
		return dao;
	}
	
	public void add(BpmProTransTo bpmProTransTo){
		if(BeanUtils.isNotEmpty(bpmProTransTo)) {
			dao.add(bpmProTransTo);
		}
	}
	
	public BpmProTransTo getByTaskId(Long taskId) {
		return dao.getByTaskId(taskId);
	}
	
	public void delById(Long id){
		dao.delById(id);
	}
	
	public void delByActInstId(Long actInstId){
		dao.delByActInstId(actInstId);
	}
		
}
