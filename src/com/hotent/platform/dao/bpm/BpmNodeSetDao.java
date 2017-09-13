/**
 * 对象功能:InnoDB free: 8192 kB Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2011-12-06 13:41:44
 */
package com.hotent.platform.dao.bpm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.bpm.BpmNodeSet;

@Repository
public class BpmNodeSetDao extends BaseDao<BpmNodeSet>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return BpmNodeSet.class;
	}
	
	/**
	 * 根据流程设置ID取流程节点设置
	 * @param defId
	 * @return
	 */
	public List<BpmNodeSet> getByDefId(Long defId)
	{
		return getBySqlKey("getByDefId", defId);
	}
	
	/**
	 * 
	 * 根据流程设置ID取流程节点设置(所有的)
	 * @param defId
	 * @return
	 */
	public List<BpmNodeSet> getAllByDefId(Long defId){
		return getBySqlKey("getAllByDefId", defId);
	}
	
	/**
	 * 根据流程的定义ID获取流程节点设置列表。
	 * @param actDefId	流程定义ID。
	 * @return
	 */
	public List<BpmNodeSet> getByActDef(String actDefId){
		return this.getBySqlKey("getByActDef", actDefId);
	}
	
	
	
	/**
	 * 通过节点Id及流程定义Id获取节点设置实体
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeSet getByDefIdNodeId(Long defId,String nodeId)
	{
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("defId", defId);
		params.put("nodeId", nodeId);
		
		return getUnique("getByDefIdNodeId",params);
	}
	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeSet getByActDefIdNodeId(String actDefId,String nodeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		return getUnique("getByActDefIdNodeId",params);
	}
	
	/**
	 * 取得某个流程定义中对应的某个节点为汇总节点的配置
	 * @param actDefId
	 * @param joinTaskKey
	 * @return
	 */
	public BpmNodeSet getByActDefIdJoinTaskKey(String actDefId,String joinTaskKey)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("joinTaskKey", joinTaskKey);
		return getUnique("getByActDefIdJoinTaskKey",params);
	}
	
	/**
	 * 根据formKey获取关联的BpmNodeSet
	 * @param formKey
	 * @return
	 */
	public List<BpmNodeSet> getByFormKey(Long formKey)
	{
		return this.getBySqlKey("getByFormKey", formKey);
	}
	
	/**
	 * 根据流程defId删除流程节点。
	 * @param defId
	 */
	public void delByDefId(Long defId){
		this.delBySqlKey("delByDefId", defId);
	}
	
	/**
	 * 根据流程定义Id和 表单类型查询 默认表单和起始表单。
	 * @param defId
	 * @param setType 值为(1，开始表单，2，全局表单)
	 * @return
	 */
	public BpmNodeSet getBySetType(Long defId,Short setType){
		Map params=new HashMap();
		params.put("defId", defId);
		params.put("setType", setType);
		return this.getUnique("getBySetType", params);
	}
	
	/**
	 * 根据流程定义获取开始和全局表单的配置。
	 * @param defId
	 * @return
	 */
	public BpmNodeSet getByStartGlobal(Long defId){
		List<BpmNodeSet> list=this.getBySqlKey("getByStartGlobal", defId);
		if(list.size()==0)
			return null;
		return list.get(0);
	}
	
	/**
	 * 取得非节点的NODESET.
	 * @param defId
	 * @return
	 */
	public List<BpmNodeSet>  getByOther(Long defId){
		List<BpmNodeSet> list=this.getBySqlKey("getByOther", defId);
		return list;
	}
	
	/**
	 * 删除起始和缺省的表单。
	 * @param defId
	 */
	public void delByStartGlobalDefId(Long defId){
		this.delBySqlKey("delByStartGlobalDefId", defId);
	}
	
	/**
	 * 根据defid 获取节点数据，并转换为map。
	 * @param defId
	 * @return
	 */
	public Map<String, BpmNodeSet> getMapByDefId(Long defId){
		Map<String, BpmNodeSet> map=new HashMap<String, BpmNodeSet>();
		List<BpmNodeSet> list= getByDefId(defId);
		for(BpmNodeSet bpmNodeSet:list){
			map.put(bpmNodeSet.getNodeId(), bpmNodeSet);
		}
		return map;
	}
	
	/**
	 * 根据actDefId获取流程节点数据。
	 * <pre>
	 * 这个关联了在线表单最新的表单id。
	 * </pre>
	 * @param actDefId
	 * @return
	 */
	public List<BpmNodeSet> getByActDefId(String actDefId){
		return this.getBySqlKey("getByActDefId", actDefId);
	}
	
	/**
	 * 根据actdefid 获取在线表单的数据。
	 * @param actDefId
	 * @return
	 */
	public List<BpmNodeSet> getOnlineFormByActDefId(String actDefId){
		return this.getBySqlKey("getOnlineFormByActDefId", actDefId);
	}
	
	/**
	 * 通过定义ID及节点Id更新isJumpForDef字段的设置
	 * @param nodeId
	 * @param actDefId
	 * @param isJumpForDef
	 */
	public void updateIsJumpForDef(String nodeId,String actDefId,Short isJumpForDef){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("isJumpForDef", isJumpForDef);
		params.put("nodeId", nodeId);
		params.put("actDefId", actDefId);
		update("updateIsJumpForDef",params);
	}

}