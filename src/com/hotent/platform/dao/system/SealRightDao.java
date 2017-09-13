/**
 * 对象功能:电子印章权限Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:raise
 * 创建时间:2012-08-29 11:26:00
 */
package com.hotent.platform.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.system.Seal;
import com.hotent.platform.model.system.SealRight;

@Repository
public class SealRightDao extends BaseDao<SealRight>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return SealRight.class;
	}
	
	/**
	 * 获取电子印章ID 的所以权限
	 * @param  sealId
	 * @return
	 */
	public List<SealRight> getRightBySealId(Long sealId)
	{
		List<SealRight> list=getBySqlKey("getBySealId", sealId);
		return list;
	}
	
	/**
	 * 根据电子印章ID进行权限删除。
	 * @param  sealId
	 * @return
	 */
	public int delBySealId(Long sealId)
	{
		int affectCount= this.delBySqlKey("delBySealId", sealId);
		return affectCount;
	}
	
}