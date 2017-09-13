/**
 * 对象功能:字段权限 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xwy
 * 创建时间:2012-02-10 10:48:16
 */
package com.hotent.platform.dao.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.form.BpmFormRights;

@Repository
public class BpmFormRightsDao extends BaseDao<BpmFormRights>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return BpmFormRights.class;
	}
	
	
	/**
	 * 根据actDefId 获取表单权限。
	 * @param actDefId
	 * @param cascade
	 * @return
	 */
	public List<BpmFormRights> getFormRightsByActDefId(String actDefId) {
		return this.getBySqlKey("getFormRightsByActDefId", actDefId);
	}
	
	/**
	 * 根据表单key 获取表单权限。
	 * @param formKey
	 * @param cascade
	 * @return
	 */
	public List<BpmFormRights> getByFormKey(Long formKey,boolean cascade) {
		String statment = null;
		if(cascade){
			statment = "getByFormKey";
		}else{
			statment = "getByFormKeyExcActDefId";
		}
		return this.getBySqlKey(statment, formKey);
	}
	
	
	/**
	 * 获取表单权限。
	 * @param formKey
	 * @param actDefId
	 * @return cascade 
	 * 
	 */
	public List<BpmFormRights> getByActDefId(Long formKey,String actDefId,boolean cascade) {
		String statment = null;
		if(cascade){
			statment = "getByActDefId";
		}else{
			statment = "getByActDefIdExcNodeId";
		}
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("formKey", formKey);
		params.put("actDefId", actDefId);
		return this.getBySqlKey(statment,params);
	}
	
	
	/**
	 * 根据流程定义id，节点id获取表单的权限。
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<BpmFormRights> getByActDefIdAndNodeId(Long formKey,String actDefId,String nodeId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("formKey", formKey);
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		return this.getBySqlKey("getByActDefIdAndNodeId", params);
	}
	
	/**
	 * 根据流程定义id，节点id删除表单权限。
	 * @param actDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void delByActDefIdAndNodeId(String actDefId,String nodeId){
		Map params=new HashMap();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		this.delBySqlKey("delByActDefIdAndNodeId", params);
	}
	
	/**
	 * 根据表id删除所有的表单权限数据。
	 * @param tableId
	 */
	public void deleteByTableId(Long tableId){
		String statment="deleteByTableId";
		this.delBySqlKey(statment, tableId);
	}
	
	/**
	 * 根据表单键删除权限。
	 * @param formKey
	 */
	public void delByFormKey(Long formKey){
		String statment="delByFormKey";
		this.delBySqlKey(statment, formKey);
	}
	
	/**
	 * 根据表单键删除权限。
	 * @param formKey
	 * @param cascade 是同时否删除表单的流程的流程节点表单权限设置
	 */
	public void delByFormKey(Long formKey,boolean cascade){
		String statment = null;
		if(cascade){
			statment="delByFormKey";	
		}else{
			statment="delByFormKeyExcActDefId";
		}
		this.delBySqlKey(statment, formKey);
	}
	
	
	/**
	 * 根据流程定义ID删除流程表单权限设置
	 * @param actDefId 流程定义ID
	 * @param cascade 是同时否删除的流程节点表单权限设置
	 */
	public void delByActDefId(String actDefId,boolean cascade){
		String statment=null;
		if(cascade){
			statment="delByActDefId";
		}else{
			statment="delByActDefIdExcNode";
		}
		this.delBySqlKey(statment, actDefId);
	}

	/**
	 * 根据表单key获得表单权限设置
	 * @param formKey 表单key
	 * 
	 */
	public List<BpmFormRights> getByFormKeyActDefIdIsNotNull(Long formKey) {
		return this.getBySqlKey("getByFormKeyActDefIdIsNotNull", formKey);
	}
	
	/**
	 * 根据流程定义id，节点id获取表单的权限。
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BpmFormRights> getByFlowFormNodeId(String actDefId,String nodeId) {
		Map params=new HashMap();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		return this.getBySqlKey("getByFlowFormNodeId", params);
	}
	
	
	/**
	 * 根据表单id获取表单权限。
	 * @param formDefId
	 * @return
	 */
	public List<BpmFormRights> getByFormDefId(Long formDefId) {
		return this.getBySqlKey("getByFormDefId", formDefId);
	}
	
	/**
	 * 根据formKey删除权限。
	 * @param formDefId
	 */
	public void delByFormDefId(Long formDefId) {
		this.delBySqlKey("delByFormDefId", formDefId);
	}
	
	/**
	 * 根据流程定义id，节点id删除表单权限。
	 * @param actDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void delByFlowFormNodeId(String actDefId,String nodeId){
		Map params=new HashMap();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		this.delBySqlKey("delByFlowFormNodeId", params);
	}
	
}