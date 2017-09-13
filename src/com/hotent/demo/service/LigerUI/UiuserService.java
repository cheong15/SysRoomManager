package com.hotent.demo.service.LigerUI;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.demo.dao.LigerUI.UiuserDao;
import com.hotent.demo.model.LigerUI.Uiuser;


@Service
public class UiuserService extends BaseService<Uiuser>
{
	@Resource
	private UiuserDao dao;
	
	public UiuserService()
	{
	}
	
	@Override
	protected IEntityDao<Uiuser,Long> getEntityDao() 
	{
		return dao;
	}

	public Object getListJoinLgeditors(QueryFilter queryFilter) {
		//用于联合查询的ligerui的 Demo
		return dao.getListJoinLgeditors(queryFilter);
	}
	
}
