/**
 * 对象功能:BPM_FORM_DEF Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xwy
 * 创建时间:2011-12-22 11:07:56
 */
package com.hotent.platform.dao.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.form.BpmFormDef;


@Repository
public class BpmFormDefDao extends BaseDao<BpmFormDef>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return BpmFormDef.class;
	}
	
	/**
	 * 获得已发布版本数量
	 * @param formKey
	 * @return
	 */
	public Integer getCountByFormKey(Long formKey)
	{
		return (Integer)this.getOne("getCountByFormKey", formKey);
	}

	/**
	 * 获得默认版本
	 * @param formKey
	 * @return
	 */
	public BpmFormDef getDefaultVersionByFormKey(Long formKey)
	{
		return (BpmFormDef)this.getOne("getDefaultVersionByFormKey", formKey);
	}
	
	/**
	 * 根据formkey获取默认发布的表单。
	 * @param formKey
	 * @return
	 */
	public BpmFormDef getDefaultPublishedByFormKey(Long formKey)
	{
		return (BpmFormDef)this.getOne("getDefaultPublishedByFormKey", formKey);
	}
	
	/**
	 * 获得表是否定义了表单。
	 * @param tableId
	 * @return
	 */
	public boolean isTableHasFormDef(Long tableId){
		Integer result=(Integer)this.getOne("isTableHasFormDef", tableId);
		return result>0;
	}
	
	/**
	 * 获得表是否有数据。
	 * @param tableName
	 * @return
	 */
	public boolean isTableHasData(Map table){
		Integer result=(Integer)this.getOne("isTableHasData", table);
		return result>0;
	}
	
	public int getFormDefAmount(Long tableId){
		Integer result=(Integer)this.getOne("getFormDefAmount", tableId);
		return result;
	}
	
	/**
	 * 根据表单key获取表单列表。
	 * @param formKey
	 * @return
	 */
	public List<BpmFormDef> getByFormKey(Long formKey){
		return this.getBySqlKey("getByFormKey", formKey);
	}
	
	/**
	 * 取得发布的表单。
	 * @param queryFilter
	 * @return
	 */
	public List<BpmFormDef> getPublished(QueryFilter queryFilter){
		return this.getBySqlKey("getPublished", queryFilter);
	}
	
	/**
	 * 判断表单的ID是否已经被使用。
	 * @param formDefId
	 * @return
	 */
	public int  getFlowUsed(Long formKey){
		Integer rtn=(Integer) this.getOne("getFlowUsed", formKey);
		return rtn;
		
	}
	
	/**
	 * 根据表单key删除表单。
	 * @param formKey
	 */
	public void delByFormKey(Long formKey){
		 this.delBySqlKey("delByFormKey", formKey);
	}
	
	/**
	 * 设置默认版本
	 * @param formKey
	 * @param formDefId
	 */
	public void setDefaultVersion(Long formKey,Long formDefId){
		this.update("updNotDefaultByFormKey", formKey);
		this.update("updDefaultByFormId", formDefId);
	}
	
	
	/**
	 * 获取当前表单的最大版本。
	 * @param formKey
	 * @return
	 */
	public Integer getMaxVersionByFormKey(Long formKey){
		Integer rtn=(Integer)this.getOne("getMaxVersionByFormKey", formKey);
		return rtn;
	}

	/**
	 * 根据表单key和是否是默认版本获取表单列表。
	 * @param formKey 表单key
	 * @param isDefault 是否是默认版本
	 * @return
	 */
	public List<BpmFormDef> getByFormKeyIsDefault(Long formKey, Short isDefault) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("formKey", formKey);
		map.put("isDefault", isDefault);
		return this.getBySqlKey("getByFormKeyIsDefault", map);
	}
	
	/**
	 * 根据流程定义ID获取表单定义。
	 * @param defId
	 * @return
	 */
	public List<BpmFormDef> getByDefId(Long defId){
		List<BpmFormDef> list = this.getBySqlKey("getByDefId", defId);
		return list;
	}
	
	/**
	 * 根据流程定义ID，节点ID，取得流程表单定义
	 * @param actDefId
	 * @return
	 */
	public List<BpmFormDef> getByActDefIdAndNodeId(String actDefId,String nodeId){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		List<BpmFormDef> list = this.getBySqlKey("getByActDefIdAndNodeId", actDefId);
		return list;
		
	}
	
	/**
	 * 根据流程定义ID，表单设置类型，取得流程表单定义
	 * @param actDefId
	 * @return
	 */
	public List<BpmFormDef> getByActDefIdAndSetType(String actDefId,Short type){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("actDefId", actDefId);
		params.put("setType", type);
		List<BpmFormDef> list = this.getBySqlKey("getByActDefIdAndSetType", params);
		return list;
		
	}
	
	/**
	 * 设置流程分类。
	 * @param categoryId
	 * @param formKeyList
	 */
	public void updCategory(Long categoryId,List<Long> formKeyList){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", categoryId);
		map.put("formKeys", formKeyList);
		this.update("updCategory",  map);
	}

	public List<BpmFormDef> getAllPublished(String subject) {
		Map<String,Object> params = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(subject)){
			params.put("subject", "%"+subject+"%");
		}
		return this.getBySqlKey("getAllPublished",params);
	}

}