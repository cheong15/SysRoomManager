
package com.hotent.demo.dao.LigerUI;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.demo.model.LigerUI.LgmoreLg;
/*
 * 页内编辑多种编辑器模式
 */
@Repository
public class LgmoreLgDao extends BaseDao<LgmoreLg>
{
	@Override
	public Class<?> getEntityClass()
	{
		return LgmoreLg.class;
	}

}
