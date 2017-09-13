package com.hotent.platform.service.bpm;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.bpm.BpmBusLinkDao;
import com.hotent.platform.model.bpm.BpmBusLink;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.system.SysOrg;
import com.hotent.platform.model.system.SysUser;

/**
 *<pre>
 * 对象功能:业务数据关联表 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2013-08-21 16:51:49
 *</pre>
 */
@Service
public class BpmBusLinkService extends BaseService<BpmBusLink>
{
	@Resource
	private BpmBusLinkDao dao;
	
	
	
	public BpmBusLinkService()
	{
	}
	
	@Override
	protected IEntityDao<BpmBusLink, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 添加业务数据。
	 * @param pk
	 * @param bpmFormTable
	 */
	public void add(String pk, BpmFormTable bpmFormTable){
		this.add(pk,null, bpmFormTable);
	}
	
	/**
	 * 添加业务数据。
	 * @param pk
	 * @param defId
	 * @param flowRunId
	 * @param bpmFormTable
	 */
	public void add(String pk,ProcessRun processRun, BpmFormTable bpmFormTable){
		SysUser curUser=ContextUtil.getCurrentUser();
		SysOrg curOrg=ContextUtil.getCurrentOrg();
		Long id=UniqueIdUtil.genId();
		BpmBusLink busLink=new BpmBusLink();
		busLink.setBusId(id);
		if(bpmFormTable.isExtTable()){
			if(BpmFormTable.PKTYPE_NUMBER.equals(bpmFormTable.getKeyDataType())){
				busLink.setBusPk(Long.parseLong(pk));
			}
			else{
				busLink.setBusPkstr(pk);
			}
		}
		else{
			busLink.setBusPk(Long.parseLong(pk));
		}
		
		busLink.setBusCreatorId(curUser.getUserId());
		busLink.setBusCreator(curUser.getFullname());
		busLink.setBusCreatetime(new Date());
		if(BeanUtils.isNotEmpty(curOrg)){
			busLink.setBusOrgName(curOrg.getOrgName());
			busLink.setBusOrgId(curOrg.getOrgId());
		}
		busLink.setBusUpdid(curUser.getUserId());
		busLink.setBusUpd(curUser.getFullname());
		busLink.setBusUpdtime(new Date());
		
		if(processRun!=null){
			busLink.setBusDefId(processRun.getDefId());
			busLink.setBusFlowRunid(processRun.getRunId());
		}
		dao.add(busLink);
	}
	
	/**
	 * 更新业务关联数据。
	 * @param pk
	 * @param bpmformtable
	 */
	public void updBusLink(String pk,BpmFormTable bpmformtable){
		SysUser curUser=ContextUtil.getCurrentUser();
		BpmBusLink link=null;
		if(bpmformtable.isExtTable()){
			if (BpmFormTable.PKTYPE_NUMBER.equals(bpmformtable.getKeyDataType())){
				link=dao.getByPk(new Long(pk));
			}
			else{
				link=dao.getByPkStr(pk);
			}
		}
		else{
			link=dao.getByPk(new Long(pk));
		}
		if(BeanUtils.isNotEmpty(link)){
			link.setBusUpdid(curUser.getUserId());
			link.setBusUpd(curUser.getFullname());
			link.setBusUpdtime(new Date());
			dao.update(link);
		}
		
	}
	
	/**
	 * 删除关联数据记录。
	 * @param pk
	 * @param bpmformtable
	 */
	public void delBusLink(String pk,BpmFormTable bpmformtable){
		if(bpmformtable.isExtTable()){
			if (BpmFormTable.PKTYPE_NUMBER.equals(bpmformtable.getKeyDataType())){
				dao.delByPk(new Long(pk));
			}
			else{
				dao.delByPkStr(pk);
			}
		}
		else{
			dao.delByPk(new Long(pk));
		}
		
	}
}
