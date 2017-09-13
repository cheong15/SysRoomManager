package com.hotent.platform.controller.bpm;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.bpm.BpmNodeSet;
import com.hotent.platform.model.bpm.TaskApprovalItems;
import com.hotent.platform.service.bpm.BpmNodeSetService;
import com.hotent.platform.service.bpm.BpmService;
import com.hotent.platform.service.bpm.TaskApprovalItemsService;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.model.system.SysAuditModelType;

/**
 * 对象功能:常用语管理 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-03-16 10:53:20
 */
@Controller
@RequestMapping("/platform/bpm/taskApprovalItems/")
@Action(ownermodel=SysAuditModelType.PROCESS_MANAGEMENT)
public class TaskApprovalItemsController extends BaseController
{
	@Resource
	private TaskApprovalItemsService taskApprovalItemsService;
	@Resource
	private BpmService bpmService;
	@Resource
	private BpmNodeSetService bpmNodeSetService;	
	
	/**
	 * 切换节点下拉框
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="切换节点下拉框")
	public void get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String nodeId = RequestUtil.getString(request, "nodeId");
		String actDefId = RequestUtil.getString(request, "actDefId");
		
		TaskApprovalItems nodeExpItems = taskApprovalItemsService.getTaskApproval(actDefId, nodeId, TaskApprovalItems.notGlobal);
		String nodeExp = (nodeExpItems==null)?"":nodeExpItems.getExpItems();
		
		writeResultMessage(response.getWriter(),nodeExp,ResultMessage.Success);
	}

	@RequestMapping("edit")
	@Action(description="编辑常用语管理")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long defId=RequestUtil.getLong(request,"defId");
		String nodeId=RequestUtil.getString(request,"nodeId");
		String actDefId=RequestUtil.getString(request,"actDefId");
		
		Long setId = null;
		// 流程节点
		Map<String,String> nodeMap=bpmService.getExecuteNodesMap(actDefId,true);
		if(StringUtil.isNotEmpty(nodeId)){
			BpmNodeSet bns = bpmNodeSetService.getByDefIdNodeId(defId, nodeId);
			setId = bns.getSetId();
		}
		
		Short isGlobal = -1;
		// 流程常用语
		TaskApprovalItems defExpItems = taskApprovalItemsService.getFlowApproval(actDefId, TaskApprovalItems.global);
		String defExp=(defExpItems==null) ?"":defExpItems.getExpItems();
		isGlobal = (defExpItems==null)?0:defExpItems.getIsGlobal();

		// 节点常用语
		TaskApprovalItems nodeExpItems = taskApprovalItemsService.getTaskApproval(actDefId, nodeId, TaskApprovalItems.notGlobal);
		String nodeExp=(nodeExpItems==null)?"":nodeExpItems.getExpItems();
		if(isGlobal.intValue()==1){
			isGlobal = (nodeExpItems==null)?1:nodeExpItems.getIsGlobal();
		}
		
		return getAutoView()
				.addObject("nodeMap",nodeMap)
				.addObject("nodeId", nodeId).addObject("actDefId", actDefId)
				.addObject("defExp", defExp).addObject("nodeExp", nodeExp)
				.addObject("setId", setId).addObject("isGlobal", isGlobal.toString());
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新节点运行脚本",
			detail="添加或更新流程定义【${SysAuditLinkService.getBpmDefinitionLink(actDefId)}】的节点" +
					"【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】的节点运行脚本（常用语）"
	)
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String isGlobal = RequestUtil.getString(request, "isGlobal");
		String approvalItem = RequestUtil.getString(request, "approvalItem");
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long setId = RequestUtil.getLong(request, "setId");
		
		if(isGlobal.equals("1")){
			taskApprovalItemsService.delFlowApproval(actDefId, TaskApprovalItems.global);
		}else{
			taskApprovalItemsService.delTaskApproval(actDefId, nodeId, TaskApprovalItems.notGlobal);
		}

		try{
			taskApprovalItemsService.addTaskApproval(approvalItem, isGlobal, actDefId, setId, nodeId);
			writeResultMessage(response.getWriter(),"保存常用语成功!",ResultMessage.Success);
		}
		catch ( Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,"保存常用语失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	
}
