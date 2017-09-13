/**
 * 对象功能:下属管理 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2012-07-05 10:08:08
 */
package com.hotent.platform.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.system.UserUnder;

@Repository
public class UserUnderDao extends BaseDao<UserUnder>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return UserUnder.class;
	}

	
	/**
	 * 取得我的下属
	 * @param userId
	 * @return
	 */
	public List<UserUnder> getMyUnderUser(Long userId){
		return this.getBySqlKey("getMyUnderUser", userId);
	}


	/**
	 * 根据用户id，下属Id判断是否已经存在
	 * @param userUnder
	 * @return
	 */
	public Integer isExistUser(UserUnder userUnder){
		return (Integer)this.getOne("isExistUser", userUnder);
	}

	
	/**
	 * 根据用户ID获取领导。
	 * @param userId
	 * @return
	 */
	public List<UserUnder> getMyLeader(Long userId){
		return this.getBySqlKey("getMyLeader", userId);
	}

}