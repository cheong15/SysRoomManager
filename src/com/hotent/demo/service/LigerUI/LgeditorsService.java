package com.hotent.demo.service.LigerUI;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.demo.dao.LigerUI.LgeditorsDao;
import com.hotent.demo.model.LigerUI.Lgeditors;

/*
 * 对象功能:list页面带有树的Demo
 */
@Service
public class LgeditorsService extends BaseService<Lgeditors>
{
	@Resource
	private LgeditorsDao dao;
	
	public LgeditorsService()
	{
	}
	
	@Override
	protected IEntityDao<Lgeditors,Long> getEntityDao() 
	{
		return dao;
	}
	
}
