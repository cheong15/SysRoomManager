package com.hotent.platform.dao.form;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.form.BpmDataTemplate;
/**
 *<pre>
 * 对象功能:业务数据模板 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zxh
 * 创建时间:2013-09-05 14:14:50
 *</pre>
 */
@Repository
public class BpmDataTemplateDao extends BaseDao<BpmDataTemplate>
{
	@Override
	public Class<?> getEntityClass()
	{
		return BpmDataTemplate.class;
	}

	public BpmDataTemplate getByFormKey(Long formKey) {
		return  this.getUnique("getByFormKey",formKey);
	}
	
	/**
	 * 根据表单的formKey删除表单模版。
	 * @param formKey
	 */
	public void delByFormKey(Long formKey){
		this.delBySqlKey("delByFormKey", formKey);
	}
	
	/**
	 * 根据formkey获取业务表单数量。
	 * @param formKey
	 * @return
	 */
	public Integer getCountByFormKey(Long formKey){
		return (Integer)this.getOne("getCountByFormKey", formKey);
	}

}