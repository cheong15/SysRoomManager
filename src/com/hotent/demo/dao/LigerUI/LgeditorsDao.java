
package com.hotent.demo.dao.LigerUI;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.demo.model.LigerUI.Lgeditors;
/*
 * 对象功能:list页面带有树的Demo
 */
@Repository
public class LgeditorsDao extends BaseDao<Lgeditors>
{
	@Override
	public Class<?> getEntityClass()
	{
		return Lgeditors.class;
	}

}
