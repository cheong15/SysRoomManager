package com.hotent.platform.service.bpm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.bpm.TaskApprovalItemsDao;
import com.hotent.platform.model.bpm.TaskApprovalItems;

/**
 * 对象功能:常用语管理 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-03-16 10:53:20
 */
@Service
public class TaskApprovalItemsService extends BaseService<TaskApprovalItems>
{
	@Resource
	private TaskApprovalItemsDao dao;
	
	public TaskApprovalItemsService()
	{
	}
	
	@Override
	protected IEntityDao<TaskApprovalItems, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 根据流程定义ID取常用语
	 * @param defId
	 * @return
	 */
	public TaskApprovalItems getFlowApproval(String actDefId, int isGlobal){
		
		return dao.getFlowApproval(actDefId, isGlobal);
	}
	
	/**
	 * 根据节点ID取常用语
	 * @param nodeId
	 * @return
	 */
	public TaskApprovalItems getTaskApproval(String actDefId, String nodeId, int isGlobal){
		
		return dao.getTaskApproval(actDefId, nodeId, isGlobal);
	}
	
	/**
	 * actDefId删除流程常用语
	 * @return
	 */
	public void delFlowApproval(String actDefId, int isGlobal){
		
		dao.delFlowApproval(actDefId, isGlobal);
	}
	
	/**
	 * actDefId, nodeId删除节点常用语
	 * @return
	 */
	public void delTaskApproval(String actDefId, String nodeId, int isGlobal){
		
		dao.delTaskApproval(actDefId, nodeId, isGlobal);
	}
	
	/**
	 * 添加常用语
	 * @param exp
	 * @param isGlobal
	 * @param actDefId
	 * @param setId
	 * @param nodeId
	 * @throws Exception
	 */
	public void addTaskApproval(String exp, String isGlobal, String actDefId,
			Long setId, String nodeId) throws Exception{
		
		TaskApprovalItems taItem = null;
			taItem = new TaskApprovalItems();
			taItem.setItemId(UniqueIdUtil.genId());
			taItem.setActDefId(actDefId);
			if(!isGlobal.equals("1")){
				
				taItem.setSetId(setId);
				taItem.setNodeId(nodeId);
				taItem.setIsGlobal(TaskApprovalItems.notGlobal);
			}else{
				taItem.setIsGlobal(TaskApprovalItems.global);
			}
			taItem.setExpItems(exp);
			add(taItem);
	}
	
	/**
	 * 取流程常用语。
	 * @param actDefId	流程定义ID。
	 * @param nodeId	活动节点ID。
	 * @return
	 */
	public List<String> getApprovalByActDefId(String actDefId, String nodeId){
		List<String> taskAppItemsList = new ArrayList<String>();
		List<TaskApprovalItems> taskAppItems = dao.getApprovalByActDefId(actDefId, nodeId);
		if (BeanUtils.isNotEmpty(taskAppItems)) {
			for(TaskApprovalItems taskAppItem:taskAppItems){
			String expItem = taskAppItem.getExpItems();
			String[] itemArr = null;
			if(StringUtil.isNotEmpty(expItem)){
				 itemArr = taskAppItem.getExpItems().split("\r\n");
				}if(itemArr!=null){
					for (String item : itemArr) {
						taskAppItemsList.add(item);
					}
					}
			}
		}

		return taskAppItemsList;
	}
	
	
	/**
	 * 根据流程定义ID取常用语
	 * 
	 * @param actDefId 流程定义ID
	 * @return
	 */
	public List<TaskApprovalItems> getByActDefId(String actDefId) {
		return dao.getByActDefId(actDefId);
	}
}
