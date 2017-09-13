/**
 * 对象功能:系统岗位表 Dao类
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
import com.hotent.platform.model.system.GlobalType;
import com.hotent.platform.model.system.Position;

@Repository
public class PositionDao extends BaseDao<Position>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return Position.class;
	}
	
	/**
	 * 根据路径取子结点
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Position> getByNodePath(String nodePath) {
		Map<String, String> p = new HashMap<String, String>();
		p.put("nodePath", nodePath);
		return this.getSqlSessionTemplate().selectList(
				this.getIbatisMapperNamespace() + ".getAll", p);
	}
	
	/**
	 * 根据路径取子结点
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Position> getChildByParentId(long parentId) {
		return this.getSqlSessionTemplate().selectList(
				this.getIbatisMapperNamespace() + ".getByParentId", parentId);
	}
	
	/**
	 * 根据岗位名称获得岗位信息
	 * @param posName
	 * @return
	 */
	public List<Position> getByPosName(String posName) {
		return this.getBySqlKey("getByPosName", posName);
	}
	
	/**
	 * 取得孩子数
	 * @param parentId
	 * @return Integer
	 */
	public Integer getChildCountByParentId(long parentId){
		return (Integer) this.getSqlSessionTemplate().selectOne(
				this.getIbatisMapperNamespace() + ".getChildCountByParentId", parentId);
	}
	
	
	
	/**
	 * 更新序号。
	 * @param posId 岗位id
	 * @param sn	排序号
	 */
	public void updSn(Long posId,Short sn){
		Map params=new HashMap();
		params.put("posId", posId);
		params.put("sn", sn);
		this.update("updSn", params);
	}
	
	/**
	 * 根据用户id获取岗位列表。
	 * @param userId
	 * @return
	 */
	public List<Position> getByUserId(Long userId){
		return this.getBySqlKey("getByUserId", userId);
	}
	
	/**
	 * 根据用户ID得到该用户的主岗位名称。
	 * @param userId
	 * @return
	 */
	public Position getPosByUserId(Long userId){
		return (Position)this.getUnique("getPosByUserId", userId);
	}
	
	
	
	/**
	 * 根据用户ID得到该用户上级领导的主岗位
	 * @param userId
	 * @return
	 */
	public Position getDirectStartUserPos(Long userId){
		return this.getUnique("getDirectStartUserPos", userId);
	}
}