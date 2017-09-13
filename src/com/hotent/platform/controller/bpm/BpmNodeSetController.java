package com.hotent.platform.controller.bpm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.bpm.util.BpmUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.BpmNodeSet;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.service.bpm.BpmDefinitionService;
import com.hotent.platform.service.bpm.BpmNodeSetService;
import com.hotent.platform.service.bpm.BpmService;
import com.hotent.platform.service.form.BpmFormDefService;
import com.hotent.platform.service.form.BpmFormTableService;

/**
 * 对象功能:节点设置 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-12-09 14:57:19
 */
@Controller
@RequestMapping("/platform/bpm/bpmNodeSet/")
@Action(ownermodel=SysAuditModelType.PROCESS_MANAGEMENT)
public class BpmNodeSetController extends BaseController
{
	@Resource
	private BpmNodeSetService bpmNodeSetService;
	@Resource
	private BpmDefinitionService bpmDefinitionService;
	@Resource
	private BpmService bpmService;
	@Resource
	private BpmFormDefService bpmFormDefService;
	@Resource
	private BpmFormTableService bpmFormTableService;
	
	/**
	 * 取得节点设置列表。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看节点设置分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		Long defId=RequestUtil.getLong(request, "defId");
		String isNew = "yes";         //是否是新的流程 或者 重新设置    并且没有绑定任何表单的流程   
		BpmDefinition bpmDefinition=bpmDefinitionService.getById(defId);
		String deployId=bpmDefinition.getActDeployId().toString();
		List<String> nodeList=new ArrayList<String>();
		
		Map<String, Map<String, String>> activityList=new HashMap<String, Map<String, String>>();
		String defXml = bpmService.getDefXmlByDeployId(deployId);
		Map<String, Map<String, String>> activityAllList= BpmUtil.getTranstoActivitys(defXml, nodeList);
		List<BpmNodeSet> list=bpmNodeSetService.getByDefId(defId);
		Map<String, String> taskMap=activityAllList.get("任务节点");
		for(int i=0;i<list.size();i++){	
			String nodeId=list.get(i).getNodeId();
			Map<String, String> tempMap=new HashMap<String,String>();
			Set<Map.Entry<String, String>> set = taskMap.entrySet();
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			   Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			   if(!nodeId.equals(entry.getKey())){
				   tempMap.put(entry.getKey(), entry.getValue());
			   }
			}
			activityList.put(list.get(i).getNodeId(), tempMap);
		}
		
		BpmNodeSet globalForm=bpmNodeSetService.getBySetType(defId,BpmNodeSet.SetType_GloabalForm);
		BpmNodeSet bpmForm=bpmNodeSetService.getBySetType(defId,BpmNodeSet.SetType_BpmForm);
		for(BpmNodeSet bpmNodeSet:list){
			BpmFormDef bpmFormDef = bpmFormDefService.getById(bpmNodeSet.getFormKey());
			if(BeanUtils.isNotEmpty(bpmFormDef) &&  BeanUtils.isNotEmpty(bpmFormDef.getTableId()) ){
				if("yes".equals(isNew)){
					isNew = "no";
				}
				List<BpmFormTable> formTablelist = bpmFormTableService.getSubTableByMainTableId(bpmFormDef.getTableId());
				if(BeanUtils.isNotEmpty(formTablelist)){
					bpmNodeSet.setExistSubTable(new Short("1"));
					bpmNodeSet.setMainTableId(bpmFormDef.getTableId());
				}else{
					bpmNodeSet.setExistSubTable(new Short("0"));
					bpmNodeSet.setMainTableId(0l);
				}
			}
		}
		
		if( "yes".equals(isNew)&& (BeanUtils.isNotEmpty(globalForm)||BeanUtils.isNotEmpty(bpmForm)) ){    //当 bpmNodeSetList globalForm bpmForm 三个都为空时 表示没有绑定表单
			isNew = "no";
		}
		
		ModelAndView mv=this.getAutoView()
				.addObject("bpmNodeSetList",list)
				.addObject("bpmDefinition",bpmDefinition)
				.addObject("globalForm", globalForm)
				.addObject("bpmForm", bpmForm)
				.addObject("activityList", activityList)
		        .addObject("isNew", isNew);
		return mv;
	}
	
	/**
	 * 删除节点设置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除节点设置",
			execOrder=ActionExecOrder.BEFORE,
			detail="<#list StringUtils.split(setId,\",\") as item>" +
						"<#assign entity = bpmNodeSetService.getById(Long.valueOf(item))/>" +
						"<#if item_index==0>" +
							"删除流程定义【${SysAuditLinkService.getBpmDefinitionLink(entity.defId)}】的节点" +
						"</#if>"+
						"【${entity.nodeName}】" +
					"</#list>的设置"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "setId");
		bpmNodeSetService.delByIds(lAryId);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description="编辑节点设置")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long setId=RequestUtil.getLong(request,"setId");
		String returnUrl=RequestUtil.getPrePage(request);
		BpmNodeSet bpmNodeSet=null;
		if(setId!=null){
			 bpmNodeSet= bpmNodeSetService.getById(setId);
		}else{
			bpmNodeSet=new BpmNodeSet();
		}
		return getAutoView().addObject("bpmNodeSet",bpmNodeSet).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得节点设置明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看节点设置明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"setId");
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getById(id);		
		return getAutoView().addObject("bpmNodeSet", bpmNodeSet);
	}
	
	
	@RequestMapping("save")
	@Action(description="成功设置流程节点表单",
			detail="设置流程定义${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}的节点表单及跳转方式"
	)
	public ModelAndView save(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Long defId=RequestUtil.getLong(request,"defId");
		BpmDefinition bpmDefinition=bpmDefinitionService.getById(defId);
		
		String[] nodeIds=request.getParameterValues("nodeId");
		String[] nodeNames=request.getParameterValues("nodeName");
		String[] formTypes=request.getParameterValues("formType");
		String[] aryFormKey=request.getParameterValues("formKey");
		String[] formUrls=request.getParameterValues("formUrl");
		String[] formDefNames=request.getParameterValues("formDefName");
		String[] aryBeforeHandler=request.getParameterValues("beforeHandler");
		String[] aryAfterHandler=request.getParameterValues("afterHandler");
		String[] aryDetailUrl=request.getParameterValues("detailUrl");
		Map<String,BpmNodeSet> nodeMap= bpmNodeSetService.getMapByDefId(defId);
	
		List<BpmNodeSet> nodeList=new ArrayList<BpmNodeSet>();
		for(int i=0;i<nodeIds.length;i++){
			String nodeId=nodeIds[i];
			BpmNodeSet nodeSet=new BpmNodeSet();
			if(nodeMap.containsKey(nodeId)){
				nodeSet=nodeMap.get(nodeId);
				//设置原有的表单key，用于删除之前设置表单节点的权限。
				if(nodeSet.getFormKey()>0L){
					nodeSet.setOldFormKey(nodeSet.getFormKey());
				}
				
			}
			nodeSet.setNodeId(nodeId);
			nodeSet.setActDefId(bpmDefinition.getActDefId());
			nodeSet.setNodeName(nodeNames[i]);
			
			
			short formType= Short.parseShort(formTypes[i]);
			
			String beforeHandler=aryBeforeHandler[i];
			String afterHandler=aryAfterHandler[i];
			
			beforeHandler=getHandler(beforeHandler);
			afterHandler=getHandler(afterHandler);
			
			String detailUrl=aryDetailUrl[i];
			String formUrl=formUrls[i];
			
			nodeSet.setFormType(formType);
			//没有选择表单
			if(formType==-1){
				nodeSet.setFormUrl("");
				nodeSet.setDetailUrl("");
				
				nodeSet.setFormKey(0L);
				nodeSet.setFormDefName("");
				
			}
			//在线表单
			else if(formType==0){
				nodeSet.setFormUrl("");
				nodeSet.setDetailUrl("");
				
				Long formKey=0L;
				if(StringUtil.isNotEmpty(aryFormKey[i])){
					formKey=Long.parseLong(aryFormKey[i]);
					
					nodeSet.setFormKey(formKey);
					nodeSet.setFormDefName(formDefNames[i]);
				}
				else{
					nodeSet.setFormKey(0L);
					nodeSet.setFormDefName("");
					
					nodeSet.setFormType((short) -1);
				}
			}
			//url表单
			else{
				nodeSet.setFormKey(0L);
				nodeSet.setFormDefName("");
				
				nodeSet.setFormUrl(formUrl);
				nodeSet.setDetailUrl(detailUrl);
			}

			nodeSet.setBeforeHandler(beforeHandler);
			nodeSet.setAfterHandler(afterHandler);
						
			nodeSet.setDefId(new Long(defId));
			
			String[] jumpType=request.getParameterValues("jumpType_"+nodeId);
			if(jumpType!=null){
				nodeSet.setJumpType(StringUtil.getArrayAsString(jumpType));
			}else{
				nodeSet.setJumpType("");
			}
			String isHideOption=request.getParameter("isHideOption_"+nodeId);
			String isHidePath=request.getParameter("isHidePath_"+nodeId);
			if(StringUtil.isNotEmpty(isHideOption)){
				nodeSet.setIsHideOption(BpmNodeSet.HIDE_OPTION);
			}else{
				nodeSet.setIsHideOption(BpmNodeSet.NOT_HIDE_OPTION);
			}
			if(StringUtil.isNotEmpty(isHidePath)){
				nodeSet.setIsHidePath(BpmNodeSet.HIDE_PATH);
			}else{
				nodeSet.setIsHidePath(BpmNodeSet.NOT_HIDE_PATH);
			}
			nodeSet.setSetType(BpmNodeSet.SetType_TaskNode);
			nodeList.add(nodeSet);
		}
		List<BpmNodeSet> list=getGlobalBpm(request,bpmDefinition);
		nodeList.addAll(list);
		bpmNodeSetService.save(defId, nodeList);
		
		addMessage(new ResultMessage(ResultMessage.Success,"成功设置流程节点表单及跳转方式 !"), request);
		return new ModelAndView("redirect:list.ht?defId="+defId +"&time=" + System.currentTimeMillis());
	}
	
	/**
	 * 从request中构建全局和流程实例表单数据。
	 * @param request
	 * @param bpmDefinition
	 * @return
	 */
	private List<BpmNodeSet> getGlobalBpm(HttpServletRequest request,BpmDefinition bpmDefinition){
		List<BpmNodeSet> list=new ArrayList<BpmNodeSet>();
		
		int globalFormType=RequestUtil.getInt(request, "globalFormType");
		if(globalFormType>=0){
			Long defaultFormKey=RequestUtil.getLong(request, "defaultFormKey");
			String defaultFormName=RequestUtil.getString(request, "defaultFormName");
			String beforeHandlerGlobal=RequestUtil.getString(request, "beforeHandlerGlobal");
			String afterHandlerGlobal=RequestUtil.getString(request, "afterHandlerGlobal");
			beforeHandlerGlobal=getHandler(beforeHandlerGlobal);
			afterHandlerGlobal=getHandler(afterHandlerGlobal);
			String formUrlGlobal=RequestUtil.getString(request, "formUrlGlobal");
			String detailUrlGlobal=RequestUtil.getString(request,"detailUrlGlobal");
			BpmNodeSet bpmNodeSet=new BpmNodeSet();
			bpmNodeSet.setDefId(bpmDefinition.getDefId());
			bpmNodeSet.setActDefId(bpmDefinition.getActDefId());
			bpmNodeSet.setFormKey(defaultFormKey);
			bpmNodeSet.setFormDefName(defaultFormName);
			bpmNodeSet.setFormUrl(formUrlGlobal);
			bpmNodeSet.setBeforeHandler(beforeHandlerGlobal);
			bpmNodeSet.setAfterHandler(afterHandlerGlobal);
			bpmNodeSet.setFormType((short)globalFormType);
			bpmNodeSet.setDetailUrl(detailUrlGlobal);
			bpmNodeSet.setSetType(BpmNodeSet.SetType_GloabalForm);
			
			if(globalFormType==BpmNodeSet.FORM_TYPE_ONLINE){
				if(defaultFormKey>0){
					list.add(bpmNodeSet);
				}
			}
			else{
				if(StringUtil.isNotEmpty(formUrlGlobal)){
					bpmNodeSet.setFormKey(null);
					list.add(bpmNodeSet);
				}
			}
		}
		int bpmFormType=RequestUtil.getInt(request, "bpmFormType");
		if(bpmFormType>=0){
			Long bpmFormKey=RequestUtil.getLong(request, "bpmFormKey");
			String bpmFormName=RequestUtil.getString(request, "bpmFormName");
			String bpmFormUrl=RequestUtil.getString(request, "bpmFormUrl");
			
			BpmNodeSet bpmNodeSet=new BpmNodeSet();
			bpmNodeSet.setDefId(bpmDefinition.getDefId());
			bpmNodeSet.setActDefId(bpmDefinition.getActDefId());
			if(bpmFormKey>0){
				bpmNodeSet.setFormKey(bpmFormKey);
				bpmNodeSet.setFormDefName(bpmFormName);
			}
			bpmNodeSet.setFormUrl(bpmFormUrl);
			bpmNodeSet.setFormType((short)bpmFormType);
			bpmNodeSet.setSetType(BpmNodeSet.SetType_BpmForm);
			list.add(bpmNodeSet);
		}
		return list;
	}
	
	private String getHandler(String handler){
		if(StringUtil.isEmpty(handler) || handler.indexOf(".")==-1){
			handler="";
		}
		return handler;
	}
	
	/**
	 * 验证handler。
	 * 输入格式为 serviceId +"." + 方法名。
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("validHandler")
	@Action(description="验证处理器")
	public void validHandler(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String handler=RequestUtil.getString(request,"handler");
		int rtn=BpmUtil.isHandlerValid(handler);
		String template="{\"result\":\"%s\",\"msg\":\"%s\"}";
		String msg="";
		switch (rtn) {
			case 0:
				msg="输入有效";
				break;
			case -1:
				msg="输入格式无效";
				break;
			case -2:
				msg="没有service类";
				break;
			case -3:
				msg="没有对应的方法";
				break;
			default:
				msg="其他错误";
				break;
		}
		String str=String.format(template, rtn,msg);
		response.getWriter().print(str);
	}
	
	
	

}
