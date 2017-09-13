package com.hotent.demo.service.LigerUI;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.demo.dao.LigerUI.LgmoreLgDao;
import com.hotent.demo.model.LigerUI.LgmoreLg;

/**
 * 页内编辑多种编辑器模式
 */
@Service
public class LgmoreLgService extends BaseService<LgmoreLg>
{
	@Resource
	private LgmoreLgDao dao;
	
	public LgmoreLgService()
	{
	}
	
	@Override
	protected IEntityDao<LgmoreLg,Long> getEntityDao() 
	{
		return dao;
	}
	
}
