package com.hotent.core.service;

import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.query.util.QueryUtil;
import com.hotent.platform.model.bus.BusQueryRule;

import java.util.List;

/**
 * 实现业务的基本操作类，实体主键为Long类型
 * 
 * @author csx
 * 
 * @param <E>
 *            实体类型，如Role
 */
public abstract class BaseServiceStr<E> extends GenericService<E, String> {

	/**
	 * 高级查询的方法
	 * 
	 * @param queryFilter 查询
	 * @param busQueryRule 查询规则
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> getAll(QueryFilter queryFilter, BusQueryRule busQueryRule) {
		if (QueryUtil.isSuperAdmin())
			return getEntityDao().getAll(queryFilter);
		if (busQueryRule != null) // 从busQueryRule中获得规则进行查询
			return (List<E>) QueryUtil.getPageList(busQueryRule, queryFilter);
		else// 默认方式
			return getEntityDao().getAll(queryFilter);
	}

}
