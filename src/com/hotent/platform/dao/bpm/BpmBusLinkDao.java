package com.hotent.platform.dao.bpm;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.bpm.BpmBusLink;
/**
 *<pre>
 * 对象功能:业务数据关联表 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2013-08-21 16:51:49
 *</pre>
 */
@Repository
public class BpmBusLinkDao extends BaseDao<BpmBusLink>
{
	@Override
	public Class<?> getEntityClass()
	{
		return BpmBusLink.class;
	}
	
	public BpmBusLink getByPk(Long pk){
		return this.getUnique("getByPk", pk);
	}

	public BpmBusLink getByPkStr(String pk){
		return this.getUnique("getByPkStr", pk);
	}
	
	public void delByPk(Long pk){
		this.delBySqlKey("delByPk", pk);
	}
	
	public void delByPkStr(String pk){
		this.delBySqlKey("delByPkStr", pk);
	}
}