package com.hotent.platform.controller.bpm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.bpm.graph.ShapeMeta;
import com.hotent.core.bpm.model.FlowNode;
import com.hotent.core.bpm.model.NodeCache;
import com.hotent.core.bpm.util.BpmUtil;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.model.TaskExecutor;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.DateFormatUtil;
import com.hotent.core.util.Dom4jUtil;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.FileUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.TimeUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.util.ZipUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.bpm.BpmDefVar;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.BpmFormResult;
import com.hotent.platform.model.bpm.BpmNode;
import com.hotent.platform.model.bpm.BpmNodeButton;
import com.hotent.platform.model.bpm.BpmNodeRule;
import com.hotent.platform.model.bpm.BpmNodeScript;
import com.hotent.platform.model.bpm.BpmNodeSet;
import com.hotent.platform.model.bpm.BpmNodeUser;
import com.hotent.platform.model.bpm.BpmReferDefinition;
import com.hotent.platform.model.bpm.BpmUserCondition;
import com.hotent.platform.model.bpm.NodeUserMap;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.bpm.TaskApprovalItems;
import com.hotent.platform.model.bpm.TaskReminder;
import com.hotent.platform.model.form.BpmFormField;
import com.hotent.platform.model.system.Demension;
import com.hotent.platform.model.system.GlobalType;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.model.system.SysFile;
import com.hotent.platform.model.system.SysOrgType;
import com.hotent.platform.service.bpm.BpmDefVarService;
import com.hotent.platform.service.bpm.BpmDefinitionService;
import com.hotent.platform.service.bpm.BpmFormRunService;
import com.hotent.platform.service.bpm.BpmNodeButtonService;
import com.hotent.platform.service.bpm.BpmNodeRuleService;
import com.hotent.platform.service.bpm.BpmNodeScriptService;
import com.hotent.platform.service.bpm.BpmNodeSetService;
import com.hotent.platform.service.bpm.BpmNodeUserCalculationSelector;
import com.hotent.platform.service.bpm.BpmNodeUserService;
import com.hotent.platform.service.bpm.BpmReferDefinitionService;
import com.hotent.platform.service.bpm.BpmService;
import com.hotent.platform.service.bpm.BpmUserConditionService;
import com.hotent.platform.service.bpm.IBpmNodeUserCalculation;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.platform.service.bpm.TaskApprovalItemsService;
import com.hotent.platform.service.bpm.TaskReminderService;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.form.BpmFormFieldService;
import com.hotent.platform.service.system.DemensionService;
import com.hotent.platform.service.system.GlobalTypeService;
import com.hotent.platform.service.system.ShareService;
import com.hotent.platform.service.system.SysFileService;
import com.hotent.platform.service.system.SysOrgTypeService;
import com.hotent.platform.service.util.ServiceUtil;
import com.hotent.platform.xml.util.FileXmlUtil;
import com.hotent.platform.xml.util.MsgUtil;

/**
 * 对象功能:流程定义 控制器类 开发公司:广州宏天软件有限公司 开发人员:csx 创建时间:2011-11-21 22:50:46
 */
@Controller
@RequestMapping("/platform/bpm/bpmDefinition/")
@Action(ownermodel=SysAuditModelType.PROCESS_MANAGEMENT)
public class BpmDefinitionController extends BaseController {
	@Resource
	private BpmService bpmService;

	@Resource
	private BpmDefinitionService bpmDefinitionService;
	
	@Resource
	private ProcessRunService processRunService;

	@Resource
	private GlobalTypeService globalTypeService;

	@Resource
	private BpmNodeUserService bpmNodeUserService;

	@Resource
	private BpmNodeSetService bpmNodeSetService;


	@Resource
	private BpmFormRunService bpmFormRunService;

	@Resource
	private BpmNodeRuleService bpmNodeRuleService;
	@Resource
	private BpmNodeScriptService bpmNodeScriptService;
	@Resource
	private BpmNodeButtonService bpmNodeButtonService;
	@Resource
	private TaskReminderService taskReminderService;
	@Resource
	private TaskApprovalItemsService taskApprovalItemsService;
	@Resource
	private ShareService shareService;
	@Resource
	private DemensionService demensionService;
	@Resource
	private SysOrgTypeService sysOrgTypeService;
	@Resource
	private BpmUserConditionService bpmUserConditionService;
	@Resource
	private BpmFormFieldService bpmFormFieldService;
	@Resource
	private BpmNodeUserCalculationSelector bpmNodeUserCalculationSelector;
	
	@Resource
	private SysFileService sysFileService;

	@Resource
	private BpmDefVarService bpmDefVarService;
	
	/**
	 * @author tgl 添加流程引用接口
	 */
	@Resource
	private BpmReferDefinitionService bpmReferDefinitionService;

	/**
	 * 返回流程设计生成的BPMNxml
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("bpmnXml")
	public ModelAndView bpmnXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "defId");
		BpmDefinition po = bpmDefinitionService.getById(id);
		if (po.getActDeployId() != null) {
			String bpmnXml = bpmService.getDefXmlByDeployId(po.getActDeployId()
					.toString());

			if (bpmnXml
					.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
				bpmnXml = bpmnXml.replace(
						"<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
			}
			if (bpmnXml.startsWith("<?xml version=\"1.0\" encoding=\"GBK\"?>")) {
				bpmnXml = bpmnXml.replace(
						"<?xml version=\"1.0\" encoding=\"GBK\"?>", "");
			}
			bpmnXml = bpmnXml.trim();

			request.setAttribute("bpmnXml", bpmnXml);
		}
		return getAutoView();
	}

	/**
	 * 返回流程设计的xml
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("designXml")
	public ModelAndView designXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "defId");
		BpmDefinition po = bpmDefinitionService.getById(id);
		String defXml = po.getDefXml();
		if (defXml.trim().startsWith(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
			defXml = defXml.replace(
					"<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
		}
		request.setAttribute("designXml", defXml);
		return getAutoView();
	}

	/**
	 * 取得流程定义分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看流程定义分页列表,含按分类查询所有流程")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmDefinitionItem");
		Long typeId = RequestUtil.getLong(request, "typeId", 0);
		if (typeId != 0 && (typeId + "").length() > 1) {
			GlobalType globalType = globalTypeService.getById(typeId);
			if (globalType != null) {
				filter.getFilters().put("nodePath",
						globalType.getNodePath() + "%");
			}
		}
		List<BpmDefinition> list = bpmDefinitionService.getAll(filter);
		ModelAndView mv = getAutoView().addObject("bpmDefinitionList", list);
		return mv;
	}

	/**
	 * 查找我的流程分类列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myList")
	@Action(description = "查看我的流程定义分页列表")
	public ModelAndView myList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmDefinitionItem");
		Long typeId = RequestUtil.getLong(request, "typeId", 0);
		//filter.addFilter("status", BpmDefinition.STATUS_ENABLED);
		//filter.addFilter("disableStatus", BpmDefinition.DISABLEStATUS_EN);
		List<BpmDefinition> list = bpmDefinitionService.getList(filter, typeId);

		ModelAndView mv = getAutoView().addObject("bpmDefinitionList", list);
		return mv;
	}

	/**
	 * 查看某一流程的所有历史版本
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("versions")
	@Action(description = "查看某一流程的所有历史版本")
	public ModelAndView versions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		List<BpmDefinition> list = bpmDefinitionService
				.getAllHistoryVersions(defId);
		ModelAndView mv = getAutoView().addObject("bpmDefinitionList", list)
				.addObject("bpmDefinition", bpmDefinition);

		return mv;
	}

	/**
	 * 删除流程定义
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除流程定义",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除流程<#list StringUtils.split(defId,\",\") as item>" +
					  "【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(item))}】" +
				   "</#list>的流程定义"
	)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		String preUrl = RequestUtil.getPrePage(request);
		// 是否删除
		String isOnlyVersion = request.getParameter("isOnlyVersion");
		boolean onlyVersion = "true".equals(isOnlyVersion) ? onlyVersion = true : false;
		try {
			String lAryId = RequestUtil.getString(request, "defId");
			if (StringUtil.isEmpty(lAryId)) {
				resultMessage = new ResultMessage(ResultMessage.Success,
						"没有传入流程定义ID!");
			} else {
				String[] aryDefId = lAryId.split(",");
				for (String defId : aryDefId) {
					Long lDefId = Long.parseLong(defId);
					// 级联删除流程定义
					bpmDefinitionService.delDefbyDeployId(lDefId, onlyVersion);
				}
				resultMessage = new ResultMessage(ResultMessage.Success,
						"删除流程定义成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage = new ResultMessage(ResultMessage.Fail, "删除流程定义失败:"
					+ e.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 取得流程定义明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看流程定义明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "defId");
		String defKey = RequestUtil.getString(request, "defKey");
		BpmDefinition po = null;
		if (StringUtil.isNotEmpty(defKey)) {
			po = bpmDefinitionService.getMainByDefKey(defKey);
		} else {
			po = bpmDefinitionService.getById(id);
		}
		ModelAndView modelAndView = getAutoView();
		if (po.getTypeId() != null) {
			GlobalType globalType = globalTypeService.getById(po.getTypeId());
			modelAndView.addObject("globalType", globalType);
		}
		if (po.getActDeployId() != null) {
			String defXml = bpmService.getDefXmlByDeployId(po.getActDeployId()
					.toString());
			modelAndView.addObject("defXml", defXml);
		}

		return modelAndView.addObject("bpmDefinition", po);

	}

	/**
	 * 取得流程定义明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("detail")
	@Action(description = "查看流程定义明细")
	public ModelAndView detail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "defId");
		BpmDefinition po = bpmDefinitionService.getById(id);
		ModelAndView modelAndView = getAutoView();
		//保存查看明细之前的流程定义ID，用于返回
				long defIdForReturn = RequestUtil.getLong(request, "defIdForReturn",0);
				if(defIdForReturn!=0){
					modelAndView.addObject("defIdForReturn", defIdForReturn) ;
				}
		if (po.getTypeId() != null) {
			GlobalType globalType = globalTypeService.getById(po.getTypeId());
			modelAndView.addObject("globalType", globalType);
		}

		return modelAndView.addObject("bpmDefinition", po);
	}

	/**
	 * 流程节点设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("nodeSet")
	@Action(description = "流程节点设置")
	public ModelAndView nodeSet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition po = bpmDefinitionService.getById(defId);
		ModelAndView modelAndView = getAutoView();
		if (po.getActDeployId() != null) {
			String defXml = bpmService.getDefXmlByDeployId(po.getActDeployId()
					.toString());
			modelAndView.addObject("defXml", defXml);
			ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
			modelAndView.addObject("shapeMeta", shapeMeta);
		}
		List<FlowNode> flowNodeList = NodeCache.getFirstNode(po.getActDefId());
		return modelAndView.addObject("bpmDefinition", po).addObject("flowNodeList", flowNodeList);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("userSet")
	@Action(description = "人员设置")
	public ModelAndView userSet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		List<BpmNodeSet> setList = bpmNodeSetService.getByDefId(defId);
		List<NodeUserMap> nodeUserMapList = new ArrayList<NodeUserMap>();
		ModelAndView modelView = getAutoView();
		// 单节点人员设置
		if (StringUtil.isNotEmpty(nodeId)) {
			BpmNodeSet nodeSet = bpmNodeSetService.getByDefIdNodeId(defId,nodeId);
			List<BpmUserCondition> bpmUserConditionList = bpmUserConditionService.getBySetId(nodeSet.getSetId());
			NodeUserMap nodeUserMap = new NodeUserMap();
			nodeUserMap.setSetId(nodeSet.getSetId());
			nodeUserMap.setNodeId(nodeSet.getNodeId());
			nodeUserMap.setNodeName(nodeSet.getNodeName());
			nodeUserMap.setBpmUserConditionList(bpmUserConditionList);
			nodeUserMapList.add(nodeUserMap);
			modelView.addObject("nodeId", nodeId);
		}
		// 整个流程的人员设置
		else {
			// 获取每个节点的人员设置
			for (BpmNodeSet nodeSet : setList) {
				List<BpmUserCondition> bpmUserConditionList = bpmUserConditionService.getBySetId(nodeSet.getSetId()); 
				NodeUserMap nodeUserMap = new NodeUserMap();
				nodeUserMap.setSetId(nodeSet.getSetId());
				nodeUserMap.setNodeId(nodeSet.getNodeId());
				nodeUserMap.setNodeName(nodeSet.getNodeName());
				nodeUserMap.setBpmUserConditionList(bpmUserConditionList);
				nodeUserMapList.add(nodeUserMap);
			}
		}
		modelView.addObject("nodeSetList", setList);
		modelView.addObject("bpmDefinition", bpmDefinition);
		modelView.addObject("nodeUserMapList", nodeUserMapList);
		modelView.addObject("defId", defId);
		return modelView;
	}

	/**
	 * 跳转规则设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("ruleSet")
	@Action(description = "跳转规则设置")
	public ModelAndView ruleSet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		// String nodeId=RequestUtil.getString(request, "nodeId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		List<BpmNodeSet> nodeSets = bpmNodeSetService.getByDefId(defId);
		Map<Long, List<BpmNodeRule>> nodeSetNodeRulesMap = getNodeRuleByNodeSet(nodeSets);
		ModelAndView modelView = getAutoView();
		for (BpmNodeSet nodeSet : nodeSets) {
			if (nodeSet.getNodeId() == null) {
				nodeSets.remove(nodeSet);
				break;
			}

		}
		modelView.addObject("bpmDefinition", bpmDefinition);
		modelView.addObject("nodeSets", nodeSets);
		modelView.addObject("nodeSetNodeRulesMap", nodeSetNodeRulesMap);
		modelView.addObject("defId", defId);
		return modelView;
	}

	/**
	 * 根据节点设置，取得对应的节点规则
	 * 
	 * @param nodeSets
	 * @return
	 */
	private Map<Long, List<BpmNodeRule>> getNodeRuleByNodeSet(
			List<BpmNodeSet> nodeSets) {
		Map<Long, List<BpmNodeRule>> nodeSetRuleMap = new HashMap<Long, List<BpmNodeRule>>();
		for (BpmNodeSet nodeSet : nodeSets) {
			List<BpmNodeRule> nodeRules = bpmNodeRuleService.getByDefIdNodeId(
					nodeSet.getActDefId(), nodeSet.getNodeId());
			nodeSetRuleMap.put(nodeSet.getSetId(), nodeRules);
		}
		return nodeSetRuleMap;
	}

	/**
	 * 流程通过flex 在线设置流程
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("design")
	@Action(description = "流程在线设计")
	public ModelAndView design(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long defId = RequestUtil.getLong(request, "defId");
		// String typeId = RequestUtil.getString(request, "typeId");
		SysAuditThreadLocalHolder.putParamerter("isExist", defId);
		if (defId > 0) {
			BpmDefinition po = bpmDefinitionService.getById(defId);
			request.setAttribute("proDefinition", po);
			request.setAttribute("subject", po.getSubject());
		} else {
			request.setAttribute("subject", "未命名");
		}
		Long uId = ContextUtil.getCurrentUserId(); // 当前用户id
		// xml流程分类
		String sb = globalTypeService.getXmlByCatkey(GlobalType.CAT_FLOW);
		request.setAttribute("xmlRecord", sb);
		request.setAttribute("uId", uId); // 当前用户id
		request.setAttribute("defId", defId); // defId流程定义id
		return getAutoView();
	}

	/**
	 * flex，保存发布流程信息。
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("flexDefSave")
	@Action(description = "流程在线设计，保存，发布操作",
			detail="设计并保存流程定义【${SysAuditLinkService.getBpmDefinitionLinkByKey(defKey)}】," +
	               "<#if Boolean.parseBoolean(deploy)>" +
					  "<#if 0==bpmDefinition.getStatus()>"+
				          "版本号为【${bpmDefinition.versionNo}】" +
					  "<#else> 版本号为【${bpmDefinition.versionNo+1}】" +
				      "</#if>"+
	               "</#if>"
	)
	public ModelAndView flexDefSave(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 新建流程定义时，若仅是保存流程定义时，则不发布流程。但下次更新时，若点发布新版时，则需要发布流程定义。
		// 新建流程定义时，若点了发布新版，则发布流程定义,下次再更新定义时，也是直接修改流程的定义。也可以重新发布新版本的流程
		// 是否发布新流程定义
		Boolean isDeploy = Boolean.parseBoolean(request.getParameter("deploy"));
		String actFlowDefXml = "";
		ResultMessage resultMessage = null;
		try {
			// 获取flex中提交过来的数据构建流程定义对象。
			BpmDefinition bpmDefinition = getFromRequest(request);
			SysAuditThreadLocalHolder.putParamerter("defKey", bpmDefinition.getDefKey());
			actFlowDefXml = BpmUtil.transform(bpmDefinition.getDefKey(),
					bpmDefinition.getSubject(), bpmDefinition.getDefXml());
			bpmDefinitionService.saveOrUpdate(bpmDefinition, isDeploy,
					actFlowDefXml);
			response.getWriter().print("success");
		} catch (Exception ex) {
			ex.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						"流程定义保存或发布失败:\r\n" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ex.getMessage();
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
		return null;
	}

	/**
	 * 根据从flex提交的数据构建流程定义对象。
	 * 
	 * @param request
	 * @return
	 */
	private BpmDefinition getFromRequest(HttpServletRequest request) throws Exception {
		Long proTypeId = RequestUtil.getLong(request, "bpmDefinition.typeId"); // 流程分类
		String subject = RequestUtil
				.getString(request, "bpmDefinition.subject"); // 流程标题
		String defKey = RequestUtil.getString(request, "bpmDefinition.defKey"); // 流程key
		String descp = RequestUtil.getString(request, "bpmDefinition.descp"); // description
		String defXml = RequestUtil.getString(request, "bpmDefinition.defXml"); // defXml
		defXml=defXml.replace("''", "'");
		String reason = RequestUtil.getString(request, "bpmDefinition.reason");// reason
		Long defId = RequestUtil.getLong(request, "bpmDefinition.defId");

		BpmDefinition bpmDefinition = null;
		if (defId != null && defId > 0) {
			bpmDefinition = bpmDefinitionService.getById(defId);
		} else if (StringUtil.isNotEmpty(defKey)) {
			if(bpmDefinitionService.isDefKeyExists(defKey)){
				throw new Exception("流程key已存在");
			}
		}
		if (bpmDefinition == null) {
			bpmDefinition = new BpmDefinition();
			if (StringUtils.isNotEmpty(defKey)) {
				bpmDefinition.setDefKey(defKey);
				// 流程定义规则。
				bpmDefinition.setTaskNameRule(BpmDefinition.DefaultSubjectRule);
			}
		}
		// 设置属性值
		if (proTypeId != null && proTypeId > 0) {
			bpmDefinition.setTypeId(proTypeId);
		}
		if (StringUtils.isNotEmpty(subject)) {
			bpmDefinition.setSubject(subject);
		}
		if (StringUtils.isNotEmpty(reason)) {
			bpmDefinition.setReason(reason);
		}
		if (StringUtils.isNotEmpty(descp)) {
			bpmDefinition.setDescp(descp);
		} else {
			bpmDefinition.setDescp(subject);
		}
		if (StringUtils.isNotEmpty(defXml)) {
			bpmDefinition
					.setDefXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							+ defXml);
		}

		return bpmDefinition;
	}

	/**
	 * 
	 * 
	 * flex，根据defId获取，流程对应的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("flexGet")
	@Action(description = "流程在线设计，查看详细信息")
	public ModelAndView flexGet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long defId = RequestUtil.getLong(request, "defId");
		SysAuditThreadLocalHolder.putParamerter("isExist", defId);
		BpmDefinition bpmDefinition = null;
		if (defId > 0) {
			bpmDefinition = bpmDefinitionService.getById(defId);
		} else {
			bpmDefinition = new BpmDefinition();
			Long proTypeId = RequestUtil.getLong(request, "defId");
			if (proTypeId > 0) {
				bpmDefinition.setTypeId(new Long(proTypeId));
			}
		}
		StringBuffer msg = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
		msg.append("<defId>" + bpmDefinition.getDefId() + "</defId>");
		msg.append("<defXml>" + bpmDefinition.getDefXml() + "</defXml>");
		if (bpmDefinition.getTypeId() != null) {
			GlobalType proType = globalTypeService.getById(bpmDefinition
					.getTypeId());
			msg.append("<typeName>" + proType.getTypeName() + "</typeName>");
			msg.append("<typeId>" + proType.getTypeId() + "</typeId>");
		}
		msg.append("<subject>" + bpmDefinition.getSubject() + "</subject>");
		msg.append("<defKey>" + bpmDefinition.getDefKey() + "</defKey>");
		msg.append("<descp>" + bpmDefinition.getDescp() + "</descp>");
		msg.append("<versionNo>" + bpmDefinition.getVersionNo()
				+ "</versionNo>");
		msg.append("</Result>");

		PrintWriter out = response.getWriter();
		out.println(msg.toString());
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("saveUser")
	@Action(description = "保存流程的节点人员设置",
			detail = "修改流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}】的节点人员设置"
	)
	public void saveUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		// String nodeTag = RequestUtil.getString(request, "nodeTag");
		Long conditionId = RequestUtil.getLong(request, "conditionId");
		String[] assignTypes = request.getParameterValues("assignType");
		
		
		String[] cmpIdss = request.getParameterValues("cmpIds");
		String[] cmpNamess = request.getParameterValues("cmpNames");
		String[] nodeUserIds = request.getParameterValues("nodeUserId");
		String[] compTypes = request.getParameterValues("compType");

		BpmDefinition bpmDefintion = bpmDefinitionService.getById(defId);
		String resultMsg = "";
		try {
			if (assignTypes != null && assignTypes.length > 0) {
				for (int i = 0; i < assignTypes.length; i++) {
					BpmNodeUser bnUser = null;
					if (StringUtils.isNotEmpty(nodeUserIds[i])) {
						long nodeUserId = new Long(nodeUserIds[i]);

						bnUser = bpmNodeUserService.getById(nodeUserId);
						bnUser.setCmpIds(cmpIdss[i]);
						bnUser.setCmpNames(cmpNamess[i]);
						bnUser.setCompType(new Short(compTypes[i]));
						bnUser.setSn(i);
						bnUser.setConditionId(conditionId);
						bpmNodeUserService.update(bnUser);
					} else {
						long nodeUserId = UniqueIdUtil.genId();

						bnUser = new BpmNodeUser();
						bnUser.setCmpIds(cmpIdss[i]);
						bnUser.setCmpNames(cmpNamess[i]);
						
						bnUser.setAssignType(assignTypes[i]);
						bnUser.setCompType(new Short(compTypes[i]));
					
						bnUser.setNodeUserId(nodeUserId);
						bnUser.setSn(i);
						bnUser.setConditionId(conditionId);
						bpmNodeUserService.add(bnUser);
					}
				}
			}
			writeResultMessage(response.getWriter(), resultMsg,
					ResultMessage.Success);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(),
					resultMsg + "," + e.getMessage(), ResultMessage.Fail);
		}
	}

	/**
	 * 删除流程节点的人员设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("delBpmNodeUser")
	@ResponseBody
	@Action(description = "删除流程节点的人员设置",
			execOrder=ActionExecOrder.BEFORE,
			detail = "<#assign nodeuser=bpmNodeUserService.getById(Long.vlaueOf(nodeUserId))/>" +
					"<#assign usercond=bpmUserConditionService.getById(nodeuser.conditionId)/>" +
					"<#assign bpmdef=bpmDefinitionService.getByActDefId(usercond.actdefid)/>" +
					"删除流程定义【${bpmdef.subject}】的节点【${SysAuditLinkService.getNodeName(usercond.actdefid,usercond.nodeid)}】的人员设置"
	)
	public String delBpmNodeUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long nodeUserId = RequestUtil.getLong(request, "nodeUserId");
		bpmNodeUserService.delById(nodeUserId);
		return "{success:true}";
	}

	/**
	 * 取得流程实例扩展分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("instance")
	// @Action(description="查看流程实例扩展分页列表")
	public ModelAndView instance(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();
		
		QueryFilter filter = new QueryFilter(request, "processRunItem");
		//过滤掉草稿实例
		filter.addFilter("exceptStatus", 4);
		filter.addFilter("exceptDefStatus",3);
		List<ProcessRun> list = processRunService.getAll(filter);
		Long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);

		mv.addObject("bpmDefinition", bpmDefinition);
		mv.addObject("processRunList", list);
		return mv;
	}

	@RequestMapping("deploy")
	@Action(description = "发布流程",
			detail="发布流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}】"
	)
	public void deploy(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		Long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);

		String defXml = bpmDefinition.getDefXml();
		String actDefXml = BpmUtil.transform(bpmDefinition.getDefKey(),
				bpmDefinition.getSubject(), defXml);
		bpmDefinitionService.deploy(bpmDefinition, actDefXml);
		ResultMessage message = new ResultMessage(ResultMessage.Success,
				"发布流程成功!");
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 设置流程分支条件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("setCondition")
	public ModelAndView setCondition(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String deployId = RequestUtil.getString(request, "deployId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		long defId = RequestUtil.getLong(request, "defId");

		ModelAndView mv = null;
		String vers = request.getHeader("USER-AGENT");
		if (vers.indexOf("MSIE 6") != -1) {
			mv = new ModelAndView(
					"/platform/bpm/bpmDefinitionSetCondition_ie6.jsp");
		} else {
			mv = getAutoView();
		}

		ProcessDefinitionEntity proDefEntity = bpmService
				.getProcessDefinitionByDeployId(deployId);
		// 找到当前设置的节点
		ActivityImpl curActivity = proDefEntity.findActivity(nodeId);

		String curNodeType = (String) curActivity.getProperty("type");
		Boolean ifInclusiveGateway = curNodeType.equals("inclusiveGateway");
		// 如果是条件同步节点，则获取之前是否已经设置了选择执行路径
		if (ifInclusiveGateway) {
			String curNodeName = (String) curActivity.getId();
			BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
			Object val = bpmDefinition.getCanChoicePathNodeMap().get(
					curNodeName);
			if (val != null) {
				mv.addObject("canChoicePathGateway", curNodeName).addObject(
						"selectCanChoicePathNodeId", val);
			}
		}
		// 找到该节点的入口节点及出口节点
		List<BpmNode> incomeNodes = new ArrayList<BpmNode>();
		List<BpmNode> outcomeNodes = new ArrayList<BpmNode>();
		// 取得当前节点的上一节点列表
		List<PvmTransition> inTrans = curActivity.getIncomingTransitions();
		for (PvmTransition tran : inTrans) {
			ActivityImpl act = (ActivityImpl) tran.getSource();
			String id = act.getId();
			String nodeName = (String) act.getProperty("name");
			String nodeType = (String) act.getProperty("type");
			Boolean isMultiple = act.getProperty("multiInstance") != null ? true
					: false;
			incomeNodes.add(new BpmNode(id, nodeName, nodeType, isMultiple));
		}

		String xml = bpmService.getDefXmlByDeployId(deployId);
		Map<String, String> conditionMap = BpmUtil.getDecisionConditions(xml,
				nodeId);

		// 取得当前节点下一节点列表
		List<PvmTransition> outTrans = curActivity.getOutgoingTransitions();
		for (PvmTransition tran : outTrans) {
			ActivityImpl act = (ActivityImpl) tran.getDestination();
			String id = act.getId();
			String nodeName = (String) act.getProperty("name");
			String nodeType = (String) act.getProperty("type");
			Boolean isMultiple = act.getProperty("multiInstance") != null ? true
					: false;
			BpmNode bpmNode = new BpmNode(id, nodeName, nodeType, isMultiple);
			String condition = conditionMap.get(id);
			if (condition != null) {
				bpmNode.setCondition(condition);
			}
			outcomeNodes.add(bpmNode);
		}

		mv.addObject("defId", defId)
				.addObject("ifInclusiveGateway", ifInclusiveGateway)
				.addObject("nodeId", nodeId).addObject("deployId", deployId)
				.addObject("incomeNodes", incomeNodes)
				.addObject("outcomeNodes", outcomeNodes);
		return mv;
	}

	/**
	 * 保存条件xml,更新流程定义文件和流程设计文件。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("saveCondition")
	@Action(description="保存流程条件设置",
			detail="设置流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}】节点【${SysAuditLinkService.getNodeName(Long.valueOf(defId),nodeId)}】的条件设置"
	)
	public void saveCondition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			long defId = RequestUtil.getLong(request, "defId");
			String nodeId = request.getParameter("nodeId");
			String tasks = request.getParameter("tasks");
			String conditions = request.getParameter("conditions");
			String[] aryTasks = tasks.split("#split#");
			String canChoicePathNodeId = RequestUtil.getString(request,
					"canChoicePathNodeId");
			conditions = " " + conditions + " ";
			String[] aryCondition = conditions.split("#split#");
			Map<String, String> map = new HashMap<String, String>();

			for (int i = 0; i < aryTasks.length; i++) {
				String condition = aryCondition[i].trim();
				map.put(aryTasks[i], condition);
			}
			bpmService.saveCondition(defId, nodeId, map, canChoicePathNodeId);

			writeResultMessage(writer, "保存流程条件成功", ResultMessage.Success);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error:" + e.getMessage());
			writeResultMessage(writer, "出错:" + e.getMessage(),
					ResultMessage.Fail);
		}
	}

	/**
	 * 显示流程定义中的每个节点锚点信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("flowImg")
	public ModelAndView flowImg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = request.getParameter("actDefId");

		String defXml = bpmService.getDefXmlByProcessDefinitionId(actDefId);
		ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);

		ModelAndView modelAndView = getAutoView();

		modelAndView.addObject("shapeMeta", shapeMeta);
		modelAndView.addObject("actDefId", actDefId);

		return modelAndView;
	}

	/**
	 * 显示流程定义中的每个节点人员的情况
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("nodeUser")
	public ModelAndView nodeUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String processDefinitionId = request
				.getParameter("processDefinitionId");
		String nodeId = request.getParameter("nodeId");
		Map<String, Object> vars = new HashMap<String, Object>();
		String curUserId=ContextUtil.getCurrentUserId().toString();
		List<TaskExecutor> list = bpmNodeUserService.getExeUserIds(processDefinitionId, null, nodeId, curUserId, curUserId, vars);
		ModelAndView modelAndView = getAutoView();
		modelAndView.addObject("candidateUsers", list);
		return modelAndView;
	}

	/**
	 * 设置流程标题规则页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("otherParam")
	public ModelAndView otherParam(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
//		String firstTaskName = NodeCache.getFirstNodeId(bpmDefinition.getActDefId()).getNodeId();
		boolean isStartMultipleNode = NodeCache.isMultipleFirstNode(bpmDefinition.getActDefId());
		List<BpmNodeSet> list = bpmNodeSetService.getByDefId(defId);
		SysFile sysFile = null;
		if (bpmDefinition.getAttachment() != null
				&& bpmDefinition.getAttachment() > 0L) {
			sysFile = sysFileService.getById(bpmDefinition.getAttachment());
		}
		List<BpmReferDefinition> referList = bpmReferDefinitionService
				.getByDefId(defId);

		ModelAndView modelAndView = getAutoView();
		return modelAndView.addObject("bpmDefinition", bpmDefinition)
				.addObject("ccMessageType", bpmDefinition.getCcMessageType())
				.addObject("defId", bpmDefinition.getDefId())
				.addObject("nodeSetList", list)
				.addObject("referList", referList)
//				.addObject("firstTaskName", firstTaskName)
				.addObject("sysFile", sysFile)
				.addObject("isStartMultipleNode", isStartMultipleNode);
	}

	/**
	 * 设置流程标题规则
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveParam")
	@Action(description="保存流程参数",
			detail="设置流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}】的其他参数设置"
	)
	public void saveParam(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		BpmDefinition bpmDefinition = new BpmDefinition();
		//流程ID
		long defId = RequestUtil.getLong(request, "defId");
		bpmDefinition.setDefId(defId);
		//表单变量
		String taskNameRule = RequestUtil.getString(request, "taskNameRule");
		bpmDefinition.setTaskNameRule(taskNameRule);
		//跳过第一个任务
		short toFirstNode = (short) RequestUtil.getInt(request, "toFirstNode");
		bpmDefinition.setToFirstNode(toFirstNode);
		//流程启动选择执行人
		short showFirstAssignee = (short) RequestUtil.getInt(request,
				"showFirstAssignee", 0);
		bpmDefinition.setShowFirstAssignee(showFirstAssignee);
		//表单明细Url
		String formDetailUrl = RequestUtil.getString(request, "formDetailUrl",
				"");
		bpmDefinition.setFormDetailUrl(formDetailUrl);
		//提交是否需要确认
		int submitConfirm = RequestUtil.getInt(request, "submitConfirm", 0);
		bpmDefinition.setSubmitConfirm(submitConfirm);
		//是否允许转办
		int allowDivert = RequestUtil.getInt(request, "allowDivert", 0);
		bpmDefinition.setAllowDivert(allowDivert);
		//是否允许我的办结转发
		int allowFinishedDivert = RequestUtil.getInt(request,"allowFinishedDivert", 0);
		bpmDefinition.setAllowFinishedDivert(allowFinishedDivert);
		//归档时发送消息给发起人
		String informStart = RequestUtil.getString(request, "informStart");
		bpmDefinition.setInformStart(informStart);
		//通知类型
		String informType = RequestUtil.getString(request, "informType");
		bpmDefinition.setInformType(informType);
		//是否允许办结抄送
		Integer allowFinishedCc = RequestUtil.getInt(request, "allowFinishedCc");
		bpmDefinition.setAllowFinishedCc(allowFinishedCc);
		//流程实例归档后是否允许打印表单
		short isPrintForm = (short) RequestUtil.getInt(request, "isPrintForm", 0);
		bpmDefinition.setIsPrintForm(isPrintForm);
		//流程帮助  的附件
		Long attachment = RequestUtil.getLong(request, "attachment");
		bpmDefinition.setAttachment(attachment);
		//流程状态 
		short status = (short) RequestUtil.getInt(request, "status", 1);
		bpmDefinition.setStatus(status);
		//相邻任务节点人员相同是自动跳过
		short sameExecutorJump = (short) RequestUtil.getInt(request, "sameExecutorJump", 0);
		bpmDefinition.setSameExecutorJump(sameExecutorJump);
		
		//允许API调用
		short isUseOutForm = (short) RequestUtil.getInt(request, "isUseOutForm", 0);
		bpmDefinition.setIsUseOutForm(isUseOutForm);
		//流程参考
		Integer allowRefer = RequestUtil.getInt(request, "allowRefer");
		bpmDefinition.setAllowRefer(allowRefer);
		//参考条数
		Integer instanceAmount = RequestUtil.getInt(request, "instanceAmount");
		bpmDefinition.setInstanceAmount(instanceAmount);
		//直接启动流程。
		Integer directstart = RequestUtil.getInt(request, "directstart",0);
		bpmDefinition.setDirectstart(directstart);
		//抄送消息类型
		String ccMessageType = RequestUtil.getString(request, "ccMessageType");
		bpmDefinition.setCcMessageType(ccMessageType);
		
		//测试标签
		String testStatusTag=RequestUtil.getString(request, "testStatusTag");
		bpmDefinition.setTestStatusTag(testStatusTag);
		
		int count = bpmDefinitionService.saveParam(bpmDefinition);
		if (count > 0) {
			ResultMessage message = new ResultMessage(ResultMessage.Success,"设置参数成功");
			out.print(message.toString());
		} else {
			ResultMessage message = new ResultMessage(ResultMessage.Fail,"参数设置失败");
			out.print(message.toString());
		}
	}
	
	@RequestMapping("dialog")
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView mv = getAutoView();
		int showAll = RequestUtil.getInt(request, "showAll",0);
		int validStatus = RequestUtil.getInt(request, "validStatus",0);
		boolean isSingle = RequestUtil.getBoolean(request, "isSingle",false);
		
		mv.addObject("showAll", showAll)
		.addObject("validStatus", validStatus)
		.addObject("isSingle", isSingle);
		
		return mv;
	}
			

	/**
	 * 用于对话框选择流程。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmDefinitionItem");
		Long typeId = RequestUtil.getLong(request, "typeId", 0);
		//是否显示全部流程
		int showAll = RequestUtil.getInt(request, "showAll",0);
		boolean isSingle = RequestUtil.getBoolean(request, "isSingle");
		List<BpmDefinition> list = null;
		//显示所有的流程，或者不忽略管理员的情况
		if(showAll==1){
			if (typeId != 0) {
				GlobalType globalType = globalTypeService.getById(typeId);
				if (BeanUtils.isNotEmpty(globalType))
					filter.getFilters().put("nodePath",globalType.getNodePath() + "%");
			}
			list = bpmDefinitionService.getAll(filter);
		} 
		else{
			list = bpmDefinitionService.getList(filter, typeId);
		}
		ModelAndView mv = getAutoView().addObject("bpmDefinitionList", list)
									   .addObject("isSingle",isSingle);
		return mv;
	}

	/**
	 * 导出xml窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("export")
	@Action(description = "导出xml窗口")
	public ModelAndView export(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String bpmDefIds = RequestUtil.getString(request, "bpmDefIds");
		ModelAndView mv = this.getAutoView();
		mv.addObject("bpmDefIds", bpmDefIds);
		return mv;
	}

	/**
	 * 导出流程定义信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description = "导出流程定义信息")
	public void exportXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] bpmDefIds = RequestUtil.getLongAryByStr(request, "bpmDefIds");
		if (BeanUtils.isEmpty(bpmDefIds)) 
			return;
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("bpmDefinition", true);
			map.put("bpmDefinitionHistory",
					RequestUtil.getBoolean(request, "bpmDefinitionHistory"));
			map.put("bpmNodeSet", true);
			map.put("bpmUserCondition",
					RequestUtil.getBoolean(request, "bpmUserCondition"));
			map.put("bpmNodeUser",
					RequestUtil.getBoolean(request, "bpmNodeUser"));
			map.put("bpmNodeUserUplow",
					RequestUtil.getBoolean(request, "bpmNodeUserUplow"));
			map.put("bpmDefRights",
					RequestUtil.getBoolean(request, "bpmDefRights"));
			map.put("taskApprovalItems",
					RequestUtil.getBoolean(request, "taskApprovalItems"));
			map.put("bpmNodeRule",
					RequestUtil.getBoolean(request, "bpmNodeRule"));
			map.put("bpmNodeScript",
					RequestUtil.getBoolean(request, "bpmNodeScript"));
			map.put("bpmNodeButton",
					RequestUtil.getBoolean(request, "bpmNodeButton"));
			map.put("bpmDefVar", 
					RequestUtil.getBoolean(request, "bpmDefVar"));
			map.put("bpmNodeMessage",
					RequestUtil.getBoolean(request, "bpmNodeMessage"));
			map.put("bpmNodeSign",
					RequestUtil.getBoolean(request, "bpmNodeSign"));
			map.put("taskReminder",
					RequestUtil.getBoolean(request, "taskReminder"));
			map.put("bpmGangedSet", true);
			map.put("bpmReferDefinition", true);
			map.put("subBpmDefinition",
					RequestUtil.getBoolean(request, "subBpmDefinition"));
			map.put("bpmFormDef", 
					RequestUtil.getBoolean(request, "bpmFormDef"));
			map.put("bpmFormTable",
					RequestUtil.getBoolean(request, "bpmFormDef"));
			try {
				exportZip(bpmDefIds, map, response,request);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 导出压缩文件
	 * @param bpmDefIds
	 * @param map
	 * @param response
	 * @param request
	 * @throws Exception 
	 */
	private void exportZip(Long[] bpmDefIds, Map<String, Boolean> map,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		String nowDate = DateFormatUtil.getNowByString("yyyyMMddHHmmss");
		String fileName = "bpmDefinition_" + nowDate;
//		String name = nowDate;
//		if(bpmDefIds.length==1){
//			BpmDefinition bpmDefinition = dao.getById(bpmDefIds[0]);
//			name = bpmDefinition.getSubject()+"_"+nowDate;
//		}
//		else name="多条流程定义记录_"+nowDate;
		String zipFileName = fileName + ".zip";
 
		String realFilePath = StringUtil.trimSufffix(
				ServiceUtil.getBasePath().toString(), File.separator)
				+ File.separator
				+ "attachFiles"
				+ File.separator
				+ "tempZip"
				+ File.separator;

		String filePath = realFilePath + fileName;
		String zipFilePath = realFilePath + File.separator + zipFileName;

		//导出xml
		String strXml = bpmDefinitionService.exportXml(bpmDefIds, map, filePath);

		// 写XML
		FileXmlUtil.writeXmlFile(fileName + ".flow.xml", strXml, filePath);
		//logger.info("写XML：" + fileName + ".flow.xml");
		// 打包
		ZipUtil.zip(filePath, true);
		//logger.info("打包：" + zipFileName);
		// 导出
		FileUtil.downLoadFile(request,response, zipFilePath, zipFileName);
		//logger.info("导出：" + zipFileName);
		// 删除导出的文件
		FileUtil.deleteFile(zipFilePath);
		//logger.info("删除导出的文件：" + zipFileName);
	}

	/**
	 * 导入流程定义信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description = "导入流程定义信息")
	public void importXml(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			bpmDefinitionService.importZip(fileLoad);
			message = new ResultMessage(ResultMessage.Success,
					MsgUtil.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.clean();
			FileUtil.deleteFile(MsgUtil.getFilePath());
			MsgUtil.cleanFilePath();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				message = new ResultMessage(
						ResultMessage.Fail, "导出失败:" + str);
			} else {
				String msg = ExceptionUtil.getExceptionMessage(e);
				message = new ResultMessage(
						ResultMessage.Fail, msg);
			}

		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 编辑节点设置为分发或汇总
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editForkJoin")
	public ModelAndView editForkJoin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");

	  //BpmNodeSet bpmNodeSet = bpmNodeSetService.getByActDefIdNodeId(actDefId,nodeId);
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getByMyActDefIdNodeId(actDefId,nodeId);

		ModelAndView view = getAutoView();

		if (bpmNodeSet != null) {
			view.addObject("bpmNodeSet", bpmNodeSet);
		}

		Map<String, String> nodeMap = bpmService.getExecuteNodesMap(actDefId,
				false);
		nodeMap.remove(nodeId);

		view.addObject("actDefId", actDefId);
		view.addObject("nodeId", nodeId);
		view.addObject("nodeMap", nodeMap);

		return view;
	}
	
	/**
	 * 保存节点分发或汇总的设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveForkJoin")
	@Action(description="保存节点分发或汇总的设置",
			detail="设置流程定义【${SysAuditLinkService.getBpmDefinitionLink(actDefId)}】节点" +
					"【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】为" +
					"<#if '0'==nodeType>【普通任务】<#else>【分发任务】,汇总任务名称为【${joinTaskName}】</#if>"
	)
	public void saveForkJoin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		String nodeType = request.getParameter("nodeType");
		String joinTaskKey = request.getParameter("joinTaskKey");
		String joinTaskName = request.getParameter("joinTaskName");

	  //BpmNodeSet bpmNodeSet = bpmNodeSetService.getByActDefIdNodeId(actDefId,nodeId);
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getByMyActDefIdNodeId(actDefId,nodeId);
		
		BpmNodeSet joinKeyNodeSet = bpmNodeSetService.getByActDefIdJoinTaskKey(
				actDefId, joinTaskKey);

		// 查看该汇总是否被其他节点设置了为汇总节点
		if (joinKeyNodeSet != null) {
			if (bpmNodeSet == null
					|| (bpmNodeSet != null && !bpmNodeSet.getSetId().equals(
							joinKeyNodeSet.getSetId()))) {
				writeResultMessage(
						response.getWriter(),
						"该汇总节点[" + joinTaskName + "]已经由 ["
								+ joinKeyNodeSet.getNodeName() + "]节点设置了！",
						ResultMessage.Fail);
				return;
			}
		}
		if (bpmNodeSet != null) {
			bpmNodeSet.setNodeType(new Short(nodeType));
			bpmNodeSet.setJoinTaskKey(joinTaskKey);
			bpmNodeSet.setJoinTaskName(joinTaskName);
			bpmNodeSetService.update(bpmNodeSet);
			writeResultMessage(response.getWriter(), "成功设置分发或汇总",
					ResultMessage.Success);
		}
	}

	/**
	 * 设置流程任务节点是否支持手机查看页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("mobileSet")
	public ModelAndView mobileSet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getByMyActDefIdNodeId(actDefId,
				nodeId);
		return getAutoView().addObject("bpmNodeSet", bpmNodeSet);
	}
	
	/**
	 * 清除流程数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("cleanData")
	@Action(description="清除测试状态的流程数据",
	detail="清除流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.parseLong(defId))}】" +
			"测试状态下启动流程产生的流程数据" )
	public void cleanData(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Long defId=RequestUtil.getLong(request, "defId");
		try {
			bpmDefinitionService.cleanData(defId);
			writeResultMessage(response.getWriter(), new ResultMessage(
					ResultMessage.Success, "清除数据成功"));
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), new ResultMessage(
					ResultMessage.Fail, "清除数据失败"));
		}
		
	}
	
	/**
	 * 设置任务节点通知方法页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("informType")
	public ModelAndView informType(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getBpmNodeSetByActDefIdNodeId(actDefId,
				nodeId);
		return getAutoView().addObject("bpmNodeSet", bpmNodeSet);
	}
	
	/**
	 * 保存节点任务的通知方式设置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveInformType")
	@Action(description="设置节点任务的通知方式",
			detail="设置流程【${SysAuditLinkService.getBpmDefinitionLink(actDefId)}】的节点" +
					"【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】的任务通知方式" +
					"<#if '1'==isSendMail>" +
					    "【邮件方式】" +
					"</#if>" +
				    "<#if '1'==isSendMobile>" +
				        "【短信方式】" +
				    "</#if>"
					
	)
	public void saveInformType(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		String informType=RequestUtil.getString(request, "informTypes");
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getBpmNodeSetByActDefIdNodeId(actDefId,nodeId);
		bpmNodeSet.setInformType(informType);
		try {
			bpmNodeSetService.update(bpmNodeSet);
			writeResultMessage(response.getWriter(), new ResultMessage(
					ResultMessage.Success, "设置成功"));
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, "设置失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	
	/**
	 * 设置流程任务节点是否支持手机
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("setMobile")
	@Action(description="设置流程任务节点是否支持手机",
			detail="设置流程【${SysAuditLinkService.getBpmDefinitionLink(actDefId)}】的任务节点" +
				"【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】" +
				"<#if '1'==isAllowMobile>能<#else>不</#if>支持手机访问"
	)
	public void setMobile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		Short isAllowMobile = RequestUtil.getShort(request, "isAllowMobile");
		BpmNodeSet bpmNodeSet = bpmNodeSetService.getByMyActDefIdNodeId(actDefId,
				nodeId);
		bpmNodeSet.setIsAllowMobile(isAllowMobile);
		try {
			bpmNodeSetService.update(bpmNodeSet);
			writeResultMessage(response.getWriter(), new ResultMessage(
					ResultMessage.Success, "设置成功"));
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, "设置失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	@RequestMapping("taskNodes")
	public ModelAndView taskNodes(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Map<String, String> taskDefNodeMap = bpmService.getTaskNodes(actDefId,nodeId);
		ModelAndView view = getAutoView();
		view.addObject("taskNodeMap", taskDefNodeMap);
		return view;
	}

	/**
	 * 根据流程定义id获取流程定义 
	 * 并判断是否有开始节点表单的配置。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getCanDirectStart")
	@ResponseBody
	public boolean getCanDirectStart(HttpServletRequest request) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		boolean rtn = bpmFormRunService.getCanDirectStart(defId);
		return rtn;
	}

	/**
	 * 通过流程定义标题自动生成流程KEY
	 * 
	 * @param request
	 * @return flowkey
	 * @throws Exception
	 */
	@RequestMapping("getFlowKey")
	@Action(description = "流程在线设计，通过标题获取defkey")
	public ModelAndView getFlowKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subject = RequestUtil.getString(request, "subject");
		if (StringUtil.isEmpty(subject))
			return null;
		String pingyin = shareService.getPingyin(subject);
		String key = pingyin;
		int count = 0;
		// BpmDefinition bpmDefinition;
		do {
			key = pingyin + (count == 0 ? "" : count);
			count++;
		} while (bpmDefinitionService.isActDefKeyExists(key));
		PrintWriter out = response.getWriter();
		out.println(key);
		return null;
	}

	@RequestMapping("getFlowListByTypeId")
	@Action(description = "流程在线设计，获取分类的所有流程")
	public ModelAndView getFlowListByTypeId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = RequestUtil.getString(request, "typeId");
		String word = RequestUtil.getString(request, "word");
		List<BpmDefinition> list;
		if (StringUtil.isEmpty(word)) {
			list = bpmDefinitionService.getPublishedByTypeId(id);
		} else {
			list = bpmDefinitionService.getAllPublished("%" + word + "%");
		}
		StringBuffer msg = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
		for (BpmDefinition bpmDefinition : list) {
			msg.append("<item name=\"" + bpmDefinition.getSubject()
					+ "\" key=\"" + bpmDefinition.getDefKey() + "\" type=\""
					+ bpmDefinition.getTypeId() + "\"></item>");
		}
		msg.append("</Result>");
		PrintWriter out = response.getWriter();
		out.println(msg.toString());
		return null;
	}

	/**
	 * 外部子过程的流程示意图
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("subFlowImage")
	public ModelAndView subFlowImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subDefKey = null;
		BpmDefinition subBpmDefinition = null;
		String subDefXml = null;
		Long defId = RequestUtil.getLong(request, "defId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		String defXml = null;
		if (bpmDefinition.getActDeployId() != null) {
			defXml = bpmService.getDefXmlByDeployId(bpmDefinition
					.getActDeployId().toString());
		} else {
			defXml = BpmUtil.transform(bpmDefinition.getDefKey(),
					bpmDefinition.getSubject(), bpmDefinition.getDefXml());
		}
		defXml = defXml.replace(
				"xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();

		List<Node> callActivityList = root
				.selectNodes("./process//callActivity");
		for (Node node : callActivityList) {
			Element element = (Element) node;
			String id = element.attributeValue("id");
			if (id.equals(nodeId)) {
				subDefKey = element.attributeValue("calledElement");
				break;
			}
		}
		subBpmDefinition = bpmDefinitionService
				.getMainDefByActDefKey(subDefKey);

		if (subBpmDefinition.getActDeployId() != null) {
			subDefXml = bpmService.getDefXmlByDeployId(subBpmDefinition
					.getActDeployId().toString());
		} else {
			subDefXml = BpmUtil
					.transform(subBpmDefinition.getDefKey(),
							subBpmDefinition.getSubject(),
							subBpmDefinition.getDefXml());
		}
		ShapeMeta shapeMeta = BpmUtil.transGraph(subDefXml);
		ModelAndView mv = getAutoView();
		mv.addObject("bpmDefinition", subBpmDefinition);
		mv.addObject("defXml", subDefXml);
		mv.addObject("shapeMeta", shapeMeta);
		return mv;
	}
	
	/**
	 * 外部子过程的流程明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("subFlowDetail")
	public void subFlowDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subDefKey = null;
		BpmDefinition subBpmDefinition = null;
		Long defId = RequestUtil.getLong(request, "defId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		String defXml = null;
		if (bpmDefinition.getActDeployId() != null) {
			defXml = bpmService.getDefXmlByDeployId(bpmDefinition
					.getActDeployId().toString());
		} else {
			defXml = BpmUtil.transform(bpmDefinition.getDefKey(),
					bpmDefinition.getSubject(), bpmDefinition.getDefXml());
		}
		defXml = defXml.replace(
				"xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();

		List<Node> callActivityList = root
				.selectNodes("./process//callActivity");
		for (Node node : callActivityList) {
			Element element = (Element) node;
			String id = element.attributeValue("id");
			if (id.equals(nodeId)) {
				subDefKey = element.attributeValue("calledElement");
				break;
			}
		}
		subBpmDefinition = bpmDefinitionService
				.getMainDefByActDefKey(subDefKey);
		response.sendRedirect(request.getContextPath()+"/platform/bpm/bpmDefinition/detail.ht?defId=" + subBpmDefinition.getDefId());
	}

	/**
	 * 外部子过程的流程示意图
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("typeSetDialog")
	public ModelAndView typeSetDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Demension> demensionList = demensionService.getAll();
		List<SysOrgType> sysOrgTypelist = sysOrgTypeService.getAll();
		ModelAndView mv = getAutoView().addObject("demensionList",
				demensionList).addObject("sysOrgTypelist", sysOrgTypelist);
		return mv;
	}
	
	
	 
	

	@RequestMapping("conditionEdit")
	public ModelAndView conditionEdit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		Long conditionId = RequestUtil.getLong(request, "conditionId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		//默认为流程人员，可以的值是抄送人员，消息节点人员。
		Integer conditionType = RequestUtil.getInt(request, "conditionType", 0);
	
		//获取表单结果。
		BpmFormResult bpmFormResult=bpmDefinitionService. getBpmFormResult(defId);
		
		if(bpmFormResult.getResult()==1){
			return ServiceUtil.getTipInfo("系统表单对应多个主表，请先修改流程定义表单配置!");
		}
		
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		ModelAndView modelView = getAutoView();
		String actDefId = bpmDefinition.getActDefId();
		
		//获取节点名称
		String nodeName="";
		boolean isMultiInstance = false;
		//获取节点设置名称是否会签节点。
		if(StringUtil.isNotEmpty(nodeId)){
			FlowNode flowNode = NodeCache.getNodeByActNodeId(actDefId, nodeId);
			if(flowNode!=null){
				nodeName = flowNode.getNodeName();
				if("userTask"==flowNode.getNodeType()){
					isMultiInstance = flowNode.getIsMultiInstance();
				}
			}
		}
		
		List<BpmNodeUser> userList=new ArrayList<BpmNodeUser>();
		// conditionId不为空说明是修改表单规则
		if (conditionId!=null &&  conditionId>0) {
			BpmUserCondition bpmUserCondition = bpmUserConditionService.getById(conditionId);
			if (bpmUserCondition != null) {
				String formIdentity = bpmUserCondition.getFormIdentity();
				if(bpmFormResult.getResult()==0){
					String name=bpmFormResult.getTableName();
					//表名不相等，则说明 之前设置的规则不适合现在的表
					if(!name.equals( formIdentity)){
						bpmUserCondition.setCondition("");
					}
				}
			}
			modelView.addObject("bpmUserCondition", bpmUserCondition);
			userList = bpmNodeUserService.getByConditionId(conditionId);
		} else {
			
			BpmUserCondition bpmUserCondition = new BpmUserCondition();
			Long currentSn = TimeUtil.getCurrentTimeMillis();
			bpmUserCondition.setSn(currentSn);
			bpmUserCondition.setFormIdentity(bpmFormResult.getTableName());
			bpmUserCondition.setConditionType(conditionType);
			modelView.addObject("bpmUserCondition", bpmUserCondition);
		}
		//添加节点名称
		modelView.addObject("nodeName", nodeName);
		//添加用户选项。
		modelView.addObject("userList", userList);
		//是否多实例
		modelView.addObject("isMultiInstance", isMultiInstance);
		//获取流程变量
		List<BpmFormField> flowVars = bpmFormFieldService.getFlowVarByFlowDefId(defId, true);
		List<BpmDefVar> bpmdefVars= bpmDefVarService.getVarsByFlowDefId(defId);
		//获取人员类型。
		Map<String,IBpmNodeUserCalculation> assignUserTypes= bpmNodeUserCalculationSelector.getBpmNodeUserCalculation();

		modelView.addObject("flowVars", flowVars).addObject("defVars",bpmdefVars)
				.addObject("bpmDefinition", bpmDefinition)
				.addObject("defId", defId)
				.addObject("nodeId", nodeId)
				.addObject("assignUserTypes",assignUserTypes)
				.addObject("userList", userList);
				
		return modelView;
	}

	

	/**
	 * 节点概要
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("nodeSummary")
	@Action(description = "节点概要")
	public ModelAndView nodeSummary(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		List<BpmNodeSet> nodeSetList = bpmNodeSetService.getByDefId(defId);
		BpmNodeSet globalNodeSet = bpmNodeSetService.getBySetType(defId,
				BpmNodeSet.SetType_GloabalForm);
		for (BpmNodeSet nodeSet : nodeSetList) {
			if (nodeSet.getNodeId() == null) {
				nodeSetList.remove(nodeSet);
				break;
			}
		}

		String actDefId = bpmDefinition.getActDefId();
		FlowNode startFlowNode = NodeCache.getStartNode(actDefId);
		List<FlowNode> endFlowNodeList = NodeCache.getEndNode(actDefId);

		Map<String, Boolean> startScriptMap = new HashMap<String, Boolean>();
		Map<String, Boolean> endScriptMap = new HashMap<String, Boolean>();
		Map<Long, Boolean> preScriptMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> afterScriptMap = new HashMap<Long, Boolean>();

		Map<Long, Boolean> assignScriptMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> nodeRulesMap = new HashMap<Long, Boolean>();

		Map<Long, Boolean> bpmFormMap = new HashMap<Long, Boolean>();
		Map<String, Boolean> buttonMap = new HashMap<String, Boolean>();
		Map<Long, Boolean> nodeButtonMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> taskReminderMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> mobileSetMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> nodeUserMap = new HashMap<Long, Boolean>();
		Map<String, Boolean> formMap = new HashMap<String, Boolean>();
		Map<Long, Boolean> taskApprovalItemsMap = new HashMap<Long, Boolean>();
		Map<String, Boolean> globalApprovalMap = new HashMap<String, Boolean>();
		// 用户设置
		this.getNodeUserMap(nodeSetList, actDefId, nodeUserMap);
		// 常用语
		this.geTaskApprovalItemsMap(nodeSetList, actDefId,
				taskApprovalItemsMap, globalApprovalMap);
		// 流程事件(脚本)
		this.getNodeScriptMap(nodeSetList, actDefId, startScriptMap,
				endScriptMap, preScriptMap, afterScriptMap, assignScriptMap);
		// 流程节点规则
		this.getNodeRulesMap(nodeSetList, actDefId, nodeRulesMap);

		// 操作按钮
		this.getNodeButtonMap(nodeSetList, defId, buttonMap, nodeButtonMap);

		// 催办信息
		this.getTaskReminderMap(nodeSetList, actDefId, taskReminderMap);
		// 手机，表单
		this.getNodeSetMap(nodeSetList, bpmFormMap, mobileSetMap);

		// 全局
		if (checkForm(globalNodeSet))
			formMap.put("global", true);
		return this.getAutoView().addObject("bpmDefinition", bpmDefinition)
				.addObject("deployId", bpmDefinition.getActDeployId())
				.addObject("defId", defId).addObject("actDefId", actDefId)
				.addObject("nodeSetList", nodeSetList)
				.addObject("startScriptMap", startScriptMap)
				.addObject("endScriptMap", endScriptMap)
				.addObject("preScriptMap", preScriptMap)
				.addObject("afterScriptMap", afterScriptMap)
				.addObject("assignScriptMap", assignScriptMap)
				.addObject("nodeRulesMap", nodeRulesMap)
				.addObject("nodeUserMap", nodeUserMap)
				.addObject("bpmFormMap", bpmFormMap)
				.addObject("formMap", formMap)
				.addObject("buttonMap", buttonMap)
				.addObject("nodeButtonMap", nodeButtonMap)
				.addObject("taskReminderMap", taskReminderMap)
				.addObject("taskApprovalItemsMap", taskApprovalItemsMap)
				.addObject("globalApprovalMap",globalApprovalMap)
				.addObject("mobileSetMap", mobileSetMap)
				.addObject("startFlowNode", startFlowNode)
				.addObject("endFlowNodeList", endFlowNodeList);

	}

	/**
	 * 用户设置
	 * 
	 * @param nodeSetList
	 * @param actDefId
	 * @param nodeUserMap
	 */
	private void getNodeUserMap(List<BpmNodeSet> nodeSetList, String actDefId,
			Map<Long, Boolean> nodeUserMap) {
		List<BpmUserCondition> bpmUserConditionList = bpmUserConditionService
				.getByActDefId(actDefId);
		for (BpmNodeSet nodeSet : nodeSetList) {

			for (BpmUserCondition bpmUserCondition : bpmUserConditionList) {
				if (nodeSet.getNodeId().equals(bpmUserCondition.getNodeid()))
					nodeUserMap.put(nodeSet.getSetId(), true);
			}

		}
	}

	/**
	 * 常用语设置
	 * 
	 * @param nodeSetList
	 * @param actDefId
	 * @param globalApprovalMap
	 * @param nodeUserMap
	 */
	private void geTaskApprovalItemsMap(List<BpmNodeSet> nodeSetList,
			String actDefId, Map<Long, Boolean> taskApprovalItemsMap,
			Map<String, Boolean> globalApprovalMap) {
		List<TaskApprovalItems> taskApprovalItemsList = taskApprovalItemsService
				.getByActDefId(actDefId);
		for (TaskApprovalItems taskApprovalItems : taskApprovalItemsList) {
			if (taskApprovalItems.getIsGlobal().shortValue() == TaskApprovalItems.global)
				globalApprovalMap.put("global", true);
		}

		for (BpmNodeSet nodeSet : nodeSetList) {
			for (TaskApprovalItems taskApprovalItems : taskApprovalItemsList) {
				if (BeanUtils.isNotEmpty(taskApprovalItems.getNodeId())) {
					if (nodeSet.getNodeId().equals(
							taskApprovalItems.getNodeId()))
						taskApprovalItemsMap.put(nodeSet.getSetId(), true);
				}
			}
		}

	}

	private void getNodeSetMap(List<BpmNodeSet> nodeSetList,
			Map<Long, Boolean> bpmFormMap, Map<Long, Boolean> mobileSetMap) {
		for (BpmNodeSet nodeSet : nodeSetList) {
			if (nodeSet.getIsAllowMobile().shortValue() == 1)
				mobileSetMap.put(nodeSet.getSetId(), true);
			if (checkForm(nodeSet))
				bpmFormMap.put(nodeSet.getSetId(), true);
		}
	}

	/**
	 * 判断是否设置表单
	 * 
	 * @param nodeSet
	 * @return
	 */
	private Boolean checkForm(BpmNodeSet bpmNodeSet) {
		if (BeanUtils.isEmpty(bpmNodeSet))
			return false;
		if (bpmNodeSet.getFormType().shortValue() == BpmNodeSet.FORM_TYPE_ONLINE
				.shortValue()) {
			if (bpmNodeSet.getFormKey().longValue() > 0L)
				return true;
		} else if (bpmNodeSet.getFormType().shortValue() == BpmNodeSet.FORM_TYPE_URL
				.shortValue()) {
			if (StringUtil.isNotEmpty(bpmNodeSet.getFormUrl()))
				return true;
		}
		return false;
	}

	/**
	 * 流程事件(脚本)
	 * 
	 * @param nodeSetList
	 * @param actDefId
	 * @param startScriptMap
	 * @param endScriptMap
	 * @param preScriptMap
	 * @param afterScriptMap
	 * @param assignScript
	 * @return
	 */
	private void getNodeScriptMap(List<BpmNodeSet> nodeSetList,
			String actDefId, Map<String, Boolean> startScriptMap,
			Map<String, Boolean> endScriptMap, Map<Long, Boolean> preScriptMap,
			Map<Long, Boolean> afterScriptMap,
			Map<Long, Boolean> assignScriptMap) {
		// 这个逻辑有问题，暂时这样处理，前置脚本和开始节点脚本为同一种类型，后置脚本跟结束节点脚本为同一种类型。
		List<BpmNodeScript> bpmNodeScriptList = bpmNodeScriptService
				.getByNodeScriptId(null, actDefId);
		// 处理开始和结束
		for (BpmNodeScript bpmNodeScript : bpmNodeScriptList) {
			if (StringUtil.isNotEmpty(bpmNodeScript.getNodeId())) {
				if (bpmNodeScript.getScriptType().intValue() == BpmNodeScript.SCRIPT_TYPE_1
						.intValue())
					startScriptMap.put(bpmNodeScript.getNodeId(), true);
				else if (bpmNodeScript.getScriptType().intValue() == BpmNodeScript.SCRIPT_TYPE_2
						.intValue())
					endScriptMap.put(bpmNodeScript.getNodeId(), true);
			}
		}
		// 处理前置、后置，分配
		for (BpmNodeSet nodeSet : nodeSetList) {
			for (BpmNodeScript bpmNodeScript : bpmNodeScriptList) {
				if (StringUtil.isNotEmpty(bpmNodeScript.getNodeId())
						&& nodeSet.getNodeId()
								.equals(bpmNodeScript.getNodeId())) {
					if (bpmNodeScript.getScriptType().intValue() == BpmNodeScript.SCRIPT_TYPE_1
							.intValue())
						preScriptMap.put(nodeSet.getSetId(), true);

					else if (bpmNodeScript.getScriptType().intValue() == BpmNodeScript.SCRIPT_TYPE_2
							.intValue())
						afterScriptMap.put(nodeSet.getSetId(), true);

					else if (bpmNodeScript.getScriptType().intValue() == BpmNodeScript.SCRIPT_TYPE_4
							.intValue())
						assignScriptMap.put(nodeSet.getSetId(), true);
				}

			}
		}
	}

	/**
	 * 节点规则
	 * 
	 * @param nodeSetList
	 * @param actDefId
	 * @param nodeRulesMap
	 * @return
	 */
	private void getNodeRulesMap(List<BpmNodeSet> nodeSetList, String actDefId,
			Map<Long, Boolean> nodeRulesMap) {
		List<BpmNodeRule> nodeRuleList = bpmNodeRuleService
				.getByActDefId(actDefId);
		for (BpmNodeSet nodeSet : nodeSetList) {
			for (BpmNodeRule bpmNodeRule : nodeRuleList) {
				if (nodeSet.getNodeId().equals(bpmNodeRule.getNodeId()))
					nodeRulesMap.put(nodeSet.getSetId(), true);
			}
		}
	}

	/**
	 * 操作按钮
	 * 
	 * @param nodeSetList
	 * @param defId
	 * @param buttonMap
	 * @param nodeButtonMap
	 */
	private void getNodeButtonMap(List<BpmNodeSet> nodeSetList, Long defId,
			Map<String, Boolean> buttonMap, Map<Long, Boolean> nodeButtonMap) {
		List<BpmNodeButton> nodeButtonList = bpmNodeButtonService
				.getByDefId(defId);
		for (BpmNodeButton bpmNodeButton : nodeButtonList) {
			buttonMap.put(bpmNodeButton.getNodeid(), true);
		}

		for (BpmNodeSet nodeSet : nodeSetList) {
			for (BpmNodeButton bpmNodeButton : nodeButtonList) {
				if (nodeSet.getNodeId().equals(bpmNodeButton.getNodeid()))
					nodeButtonMap.put(nodeSet.getSetId(), true);
			}
		}
	}

	private void getTaskReminderMap(List<BpmNodeSet> nodeSetList,
			String actDefId, Map<Long, Boolean> taskReminderMap) {
		List<TaskReminder> taskReminderList = taskReminderService
				.getByActDefId(actDefId);
		for (BpmNodeSet nodeSet : nodeSetList) {
			for (TaskReminder taskReminder : taskReminderList) {
				if (nodeSet.getNodeId().equals(taskReminder.getNodeId()))
					taskReminderMap.put(nodeSet.getSetId(), true);
			}
		}
	}
	
	/**
	 * 更新流程定义启用状态 0启用，1禁止
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("updateDisableStatus")
	@Action(description="更新流程定义启用状态",
			detail="设置流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}】为" +
				   "<#if '1'==disableStatus>启用<#else>禁止</#if>状态"
	)
	public void updateDisableStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		Short disableStatus = RequestUtil.getShort(request, "disableStatus");
		//从启用改成禁止，或从禁止改成启用
		disableStatus=(disableStatus==BpmDefinition.STATUS_DISABLED?BpmDefinition.STATUS_ENABLED:BpmDefinition.STATUS_DISABLED);
		bpmDefinitionService.updateDisableStatus(defId, disableStatus);
		response.sendRedirect(RequestUtil.getPrePage(request));
	}
	
	/**
	 * 检查流程版本
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("checkVersion")
	@Action(description = "检查流程版本")
	public String checkVersion(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		JSONObject jsonObject = new JSONObject();
		try {
			if(BpmDefinition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus())){
				jsonObject.accumulate("success", false).accumulate("msg",
						"流程定义已经禁用实例！" );
				return jsonObject.toString();
			}
			jsonObject.accumulate("success", true)
					.accumulate("isMain", bpmDefinition.getIsMain())
					.accumulate("msg", "检查版本成功!");
		} catch (Exception e) {
			jsonObject.accumulate("success", false).accumulate("msg",
					"检查版本错误!" + e.getMessage());
		}
		return jsonObject.toString();

	}
	
	@RequestMapping("copyUserList")
	@Action(description = "流程抄送人员设置列表")
	public ModelAndView copyUserList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		BpmDefinition bpmDefinition = bpmDefinitionService.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		List<BpmUserCondition> copyUserList = bpmUserConditionService.getCcByActDefId(actDefId);

		ModelAndView mv = getAutoView();
		mv.addObject("copyUserList", copyUserList).addObject("bpmDefinition",
				bpmDefinition);
		return mv;
	}
	
	
	/**
	 * 取得流程定义分页列表
	 * 
	 * @author tgl
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("defReferSelector")
	@Action(description = "选择流程定义分页列表,含按分类查询所有流程")
	public ModelAndView defReferSelector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		QueryFilter filter = new QueryFilter(request, "bpmDefinitionItem");
		filter.addFilter("validStatus", "1");
		
		Long typeId = RequestUtil.getLong(request, "typeId", 0);
		if (typeId != 0 && (typeId + "").length() > 1) {
			GlobalType globalType = globalTypeService.getById(typeId);
			if (globalType != null) {
				filter.addFilter("typeId",typeId);
				filter.addFilter("nodePath",	globalType.getNodePath() + "%");
			}
		}

		List<BpmDefinition> list = bpmDefinitionService.getAll(filter);
	
		Long defId = RequestUtil.getLong(request, "defId");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("defId", defId);
		modelMap.put("bpmDefinitionList", list);
		
		ModelAndView mv = getAutoView().addAllObjects(modelMap).addObject("defId", defId);
		return mv;
	}
	
	/**
	 * @author tgl 保存流程定义引用
	 */
	@RequestMapping("saveReferDef")
	@Action(description = "保存流程定义引用",
	detail = "流程定义【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(defId))}】添加流程引用" +
			"<#list StringUtils.split(refers,\",\") as item>" +
			"【${SysAuditLinkService.getBpmDefinitionLink(Long.valueOf(item))}】 "+
			"</#list>"		
	)
	public void saveReferDef(HttpServletRequest request,HttpServletResponse response) throws Exception {
		long defId = RequestUtil.getLong(request, "defId");
		String[] refers = RequestUtil.getStringAryByStr(request, "refers");
		List<BpmReferDefinition> list = bpmReferDefinitionService.getByDefId(defId);
		List<String> referList = new ArrayList<String>();
		int i = 0;
		for (i = 0; i < refers.length; i++) {
			referList.add(refers[i]);
		}
		for (BpmReferDefinition refer : list) {
			for (i = 0; i < referList.size(); i++) {
				if (refer.getReferDefId().longValue() == Long.parseLong(referList.get(i))) {
					referList.remove(i);
				}
			}
		}
		for (String r : referList) {
			if (StringUtils.isNotBlank(r)) {
				BpmReferDefinition ref = new BpmReferDefinition();
				ref.setId(UniqueIdUtil.genId());
				ref.setDefId(defId);
				ref.setReferDefId(new Long(r));
				ref.setState("1");
				ref.setRemark("流程定义引用");
				bpmReferDefinitionService.saveReferDef(ref);
			}
		}
		writeResultMessage(response.getWriter(), "流程引用成功！",
				ResultMessage.Success);

		
	}
	
	
	@RequestMapping("setCategory")
	@Action(description = "设置分类" , 
			detail="设置表单" +
					"<#list StringUtils.split(defKeys,\",\") as item>" +
					" 【${SysAuditLinkService.getBpmDefinitionLinkByKey(item)}】 "+
					"</#list>" +
					"的分类为" +
					"${SysAuditLinkService.getGlobalTypeLink(Long.valueOf(typeId))}")
	public void setCategory(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		Long typeId = RequestUtil.getLong(request, "typeId", 0);
		String defKeys = RequestUtil.getString(request, "defKeys");
		String[] aryDefKey = defKeys.split(",");

		if (typeId == 0L) {
			// writer
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,
					ContextUtil.getMessages("controller.bpmFormDef.setCategory.notCategoryId")));
			return;
		}
		
		if (StringUtil.isEmpty(defKeys)) {
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,
					  ContextUtil.getMessages("controller.bpmFormDef.setCategory.notSelectForm")));
			return;
		}
		
		List<String> list = new ArrayList<String>();
		
		for (String defKey : aryDefKey) {
			list.add(defKey);
		}
		try {
			bpmDefinitionService.updCategory(typeId, list);
			writeResultMessage(writer, new ResultMessage(ResultMessage.Success,
					  ContextUtil.getMessages("controller.bpmDefinition.setCategory.success")));
		} catch (Exception ex) {
			String msg = ExceptionUtil.getExceptionMessage(ex);
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,
					msg));
		}
		
	}
	
	/**
	 * @author tgl 删除流程定义引用
	 */
	@RequestMapping("delReferDef")
	@Action(description = "删除流程定义引用",
			execOrder=ActionExecOrder.BEFORE,
			detail="<#assign entity=bpmReferDefinitionService.getById(Long.valueOf(refId))/>" +
					"流程定义【${SysAuditLinkService.getBpmDefinitionLink(entity.defId)}】"+
					"<#assign entity1=bpmDefinitionService.getById(entity.referDefId)/>" +
					"删除流程引用【${entity1.subject}】"
	)
	public void delReferDef(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "refId");
		bpmReferDefinitionService.getById(id);
		try {
			bpmReferDefinitionService.delById(id);
			writeResultMessage(response.getWriter(),  "删除成功",
					ResultMessage.Success);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(),  "删除失败",
					ResultMessage.Success);
		}
	}
	
	/**
	 * @author tgl 根据表单key查找绑定的流程
	 */
	@RequestMapping("getBpmDefinitionByFormKey")
	@Action(description = "根据表单key查找绑定的流程",detail="根据表单key查找绑定的流程")
	@ResponseBody
	public List<BpmDefinition> getBpmDefinitionByFormKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formKey = RequestUtil.getLong(request, "formKey");
		List<BpmDefinition> list = bpmDefinitionService.getBpmDefinitionByFormKey(formKey);
		return list;
	}

}


