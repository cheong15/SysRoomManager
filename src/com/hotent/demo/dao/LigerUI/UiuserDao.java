
package com.hotent.demo.dao.LigerUI;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.demo.model.LigerUI.Uiuser;

@Repository
public class UiuserDao extends BaseDao<Uiuser>
{
	@Override
	public Class<?> getEntityClass()
	{
		return Uiuser.class;
	}

	public Object getListJoinLgeditors(final QueryFilter queryFilter) {
		//用于联合查询的ligerui的 Demo
		return this.getBySqlKey("getListJoinLgeditors", queryFilter);
	}

}
