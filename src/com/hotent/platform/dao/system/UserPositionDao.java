/**
 * 对象功能:用户岗位表 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
package com.hotent.platform.dao.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.system.UserPosition;

@Repository
public class UserPositionDao extends BaseDao<UserPosition>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return UserPosition.class;
	}
	
	/**
	 * 根据岗位查询人员。
	 * @param posId
	 * @return
	 */
	public List<UserPosition> getByPosId(Long posId){
		List<UserPosition> list= this.getBySqlKey("getByPosId", posId);
		return list;
	}
	
	/**
	 * 根据岗位删除关联数据。
	 * @param posId
	 */
	public void delByPosId(Long posId){
		this.delBySqlKey( "delByPosId", posId);
	}
	/**
	 * 根据用户id和岗位id查询用户岗位映射对象。
	 */
	public UserPosition getUserPosModel(Long userId,Long posId)	
	{
		Map params=new HashMap();
		params.put("userId", userId);
		params.put("posId", posId);
		UserPosition userPosition=(UserPosition)getUnique("getUserPosModel", params);
		return userPosition;
	}
	
	/**
	 * 根据id数组,删除sys_user_pos关系信息
	 * @param Long[] ids
	 * @return
	 */
	public int delUserPosByIds(Long userId,Long posId)
	{
		Map param=new HashMap();
		param.put("userId", userId);
		param.put("posId", posId);
		int affectCount = this.delBySqlKey("delUserPosByIds",param);
		
		return affectCount;
	}
	
	/**
	 * 取得某个职位下的所有用户ID
	 * @param posId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Long> getUserIdsByPosId(Long posId)
	{
		List list=getBySqlKey("getUserIdsByPosId", posId);
		return list;
	}
	
	/**
	 * 取得岗位下的所有用户。
	 * @param posIds
	 * @return
	 */
	public List<UserPosition> getUserByPosIds(String posIds)
	{
		Map<String, String> param=new HashMap<String, String>();
		param.put("posIds", posIds);
		List list=getBySqlKey("getUserByPosIds", param);
		return list;
	}
	
	/**
	 * 根据用户id删除用户和岗文的关系。
	 * @param userId	用户ID
	 */
	public void delByUserId(Long userId){
		this.delBySqlKey("delByUserId", userId);
	}
	
	/**
	 * 根据userId得到岗位
	 * @param param
	 * @return
	 */
	public List<UserPosition> getByUserId(Long userId)
	{ 
		return getBySqlKey("getByUserId", userId);
	}	
	
	/**
	 * 根据用户id将用户的主岗位属性更新为非主岗位。
	 * @param userId
	 */
	public void updNotPrimaryByUser(Long userId){
		this.update("updNotPrimaryByUser", userId);
	}
	
	/**
	 * 根据用户ID得到用户的主岗位
	 * @param userId
	 * @return
	 */
	public UserPosition getMainPositionByUserId(Long userId){
		return this.getUnique("getMainPositionByUserId", userId);
	}

}