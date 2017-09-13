package com.hotent.platform.service.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.system.UserPositionDao;
import com.hotent.platform.model.system.UserPosition;

/**
 * 对象功能:用户岗位表 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
@Service
public class UserPositionService extends BaseService<UserPosition>
{
	@Resource
	private UserPositionDao userPositionDao;
	
	public UserPositionService()
	{
	}
	
	@Override
	protected IEntityDao<UserPosition, Long> getEntityDao() 
	{
		return userPositionDao;
	}
	
	/**
	 * 添加对象
	 * @param entity
	 * @throws Exception 
	 */
	public void add(Long posId,Long[] userIds) throws Exception
	{
		if(posId==null || posId==0 || userIds==null || userIds.length==0) return;
		
		for(long userId:userIds){
			UserPosition userPosition = userPositionDao.getUserPosModel(userId, posId);
			if(userPosition!=null) continue;
			long userPosId=UniqueIdUtil.genId();
			UserPosition up=new UserPosition();
			up.setUserPosId(userPosId);
			up.setIsPrimary(UserPosition.PRIMARY_YES);
			userPositionDao.updNotPrimaryByUser(userId);
			up.setPosId(posId);
			up.setUserId(userId);
			userPositionDao.add(up);
		}
		
	}
	
	/**
	 * 设置为非主岗位。
	 * @param userPosId		主岗位ID
	 */
	public void setIsPrimary(Long userPosId){
		UserPosition userPosition =  userPositionDao.getById(userPosId);
		if( userPosition.getIsPrimary()==UserPosition.PRIMARY_NO){
			userPosition.setIsPrimary(UserPosition.PRIMARY_YES);
			userPositionDao.updNotPrimaryByUser(userPosition.getUserId());
		}
		else{
			userPosition.setIsPrimary(UserPosition.PRIMARY_NO);
		}
		userPositionDao.update(userPosition);
	}
	
	/**
	 * 根据岗位id获取用户岗位关联对象列表。
	 * @param posId
	 * @return
	 * @throws Exception
	 */
	public List<UserPosition> getByPosId(Long posId) throws Exception
	{
		return userPositionDao.getByPosId(posId);
	}
	
	public void delByPosId(Long posId){
		userPositionDao.delByPosId(posId);
	}
	
	/**
	 * 根据用户和岗位id查询用户岗位映射对象。 
	 */
	public UserPosition getUserPosModel(Long userId,Long posId)	
	{
		return userPositionDao.getUserPosModel(userId,posId);
	}
		
	/**
	 * 根据岗位数组和用户id删除对应的岗位和用户映射对象。
	 * @param lAryId
	 * @param userId
	 */
	public void delUserPosByIds(String[] lAryId,Long userId){
		if(BeanUtils.isEmpty(lAryId)) return;
		for (String posId : lAryId){
			if(StringUtil.isEmpty(posId)) continue;
	        userPositionDao.delUserPosByIds(userId, Long.parseLong(posId));
		}			
	}
	
	
	/**
	 * 保存用户和岗位的映射关系。
	 * @param userId	用户ID
	 * @param posIds	岗位ID
	 * @param posIdPrimary	主岗位ID
	 * @throws Exception
	 */
	public void saveUserPos(Long userId ,Long[] posIds,Long posIdPrimary) throws Exception
	{
		userPositionDao.delByUserId(userId);
		if(BeanUtils.isEmpty(posIds)) return;
		for(Long posId:posIds){
			UserPosition userPosition=new UserPosition();
			userPosition.setUserPosId(UniqueIdUtil.genId());
			userPosition.setPosId(posId);
			userPosition.setUserId(userId);
			if(posIdPrimary!=null && posId.equals(posIdPrimary)){
				userPosition.setIsPrimary(UserPosition.PRIMARY_YES);
			}
			userPositionDao.add(userPosition);
		}
	}
	
	
	/**
	 * 取得某个职位下的所有用户ID
	 * @param posId
	 * @return
	 */
	public List<Long> getUserIdsByPosId(Long posId)
	{
		return userPositionDao.getUserIdsByPosId(posId);
	}
	
	/**
	 * 取指定岗位的所有用户ID
	 * @param posId
	 * @return
	 */
	public List<UserPosition> getUserByPosIds(String posId)
	{
		return userPositionDao.getUserByPosIds(posId);
	}
	
	/**
	 * 根据userId得到岗位
	 * @param param
	 * @return
	 */
	public List<UserPosition> getByUserId(Long userId)
	{ 
		return userPositionDao.getByUserId(userId);
	}
	
	/**
	 * 根据userId删除用户岗位关系
	 * @param param
	 * @return
	 */
	public void delByUserId(Long userId)
	{ 
		userPositionDao.delByUserId(userId);
	}
	
}
