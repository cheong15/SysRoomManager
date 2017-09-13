package com.hotent.platform.controller.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.PinyinUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.BpmTaskExe;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.bpm.TaskAmount;
import com.hotent.platform.model.system.GlobalType;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.model.system.SysTypeKey;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.model.system.SystemConst;
import com.hotent.platform.service.bpm.BpmDefinitionService;
import com.hotent.platform.service.bpm.BpmService;
import com.hotent.platform.service.bpm.BpmTaskExeService;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.system.GlobalTypeService;
import com.hotent.platform.service.system.SysOrgService;
import com.hotent.platform.service.system.SysRoleService;
import com.hotent.platform.service.system.SysTypeKeyService;

/**
 * 对象功能:系统分类表 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2011-11-23 11:07:27
 */
@Controller
@RequestMapping("/platform/system/globalType/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class GlobalTypeController extends BaseController
{
	@Resource
	private GlobalTypeService globalTypeService;
	
	@Resource
	private SysTypeKeyService sysTypeKeyService;
	
	@Resource
	private SysRoleService sysRoleService;
	
	@Resource
	private SysOrgService sysOrgService;
	
	@Resource
	private ProcessRunService processRunService;
	
	@Resource
	private BpmService bpmService;
	
	@Resource
	private BpmTaskExeService bpmTaskExeService;
	
	@Resource
	BpmDefinitionService bpmDefinitionService;
	/**
	 * 获取中文字全部拼音
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getPingyin")
	@ResponseBody
	@Action(description="获取中文字全部拼音",detail="获取中文字全部拼音")
	public String getPingyin (HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String typeName=RequestUtil.getString(request, "typeName"); 
		String nodeKey=PinyinUtil.getPinyinToLowerCase(typeName);
		return nodeKey;
	}
	

	/**
	 * 取得系统分类表用于显示树层次结构的分类可以分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getByParentId")
	@ResponseBody
	public List<GlobalType> getByParentId(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		Long parentId=RequestUtil.getLong(request, "parentId",-1);
		Long catId=RequestUtil.getLong(request, "catId",0);
		List<GlobalType> list=globalTypeService.getByParentId(parentId==-1?parentId=catId:parentId);
		//如果分类的ID和父类ID一致的情况表明，是根节点
		//为根节点的情况需要添加一个跟节点。
		if(catId.equals(parentId)){
			SysTypeKey sysTypeKey= sysTypeKeyService.getById(catId);
			GlobalType globalType=new GlobalType();
			globalType.setTypeName(sysTypeKey.getTypeName());
			globalType.setCatKey(sysTypeKey.getTypeKey());
			
			globalType.setTypeId(sysTypeKey.getTypeId());
			globalType.setParentId(0L);
			globalType.setType(sysTypeKey.getType());
			if(list.size()==0){
				globalType.setIsParent("false");
			}
			else{
				globalType.setIsParent("true");
				globalType.setOpen("true");
			}
			globalType.setNodePath(sysTypeKey.getTypeId() +".");
			list.add(0,globalType);
		}
		
		return list;
	}
	
	/**
	 * 取得系统分类表分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tree")
	//@Action(description="查看系统分类列表")
	public ModelAndView tree(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<SysTypeKey> list=sysTypeKeyService.getAll();
		SysTypeKey sysTypeKey=list.get(0);
		ModelAndView mv=this.getAutoView()
		.addObject("typeList", list)
		.addObject("sysTypeKey", sysTypeKey);
		 
		return mv;
	}

	/**
	 * 删除系统分类。
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除系统分类",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除系统分类" +
					"<#assign entity=globalTypeService.getById(Long.valueOf(typeId))/>" +
					"【${entity.typeName}】"
			)
	public void del(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		ResultMessage resultMessage=null;
		try {
			Long typeId =RequestUtil.getLong(request, "typeId");
			globalTypeService.delByTypeId(typeId);
			resultMessage=new ResultMessage(ResultMessage.Success, "删除系统分类成功");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,"删除系统分类失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
			    resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	
	/**
	 * 导出系统分类。
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description="导出系统分类",
			execOrder=ActionExecOrder.AFTER,
			detail="导出系统分类" +
					"【${SysAuditLinkService.getGlobalTypeLink(Long.valueOf(typeId))}】"
			)
	public void exportXml(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		long typeId=RequestUtil.getLong(request, "typeId");
		String filename="";
		if(typeId!=0){
			String strXml=globalTypeService.exportXml(typeId);
			GlobalType globalType=globalTypeService.getById(typeId);
			if(globalType!=null){
				filename=globalType.getTypeName();
			}else{
				filename=sysTypeKeyService.getById(typeId).getTypeName();
			}
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" +StringUtil.encodingString(filename, "GBK",
					"ISO-8859-1") + ".xml");
			response.getWriter().write(strXml);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
	
	/**
	 * 导入系统分类。
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description="导入系统分类",
			execOrder=ActionExecOrder.AFTER,
			detail="导入系统分类" +
					"【${SysAuditLinkService.getGlobalTypeLink(Long.valueOf(typeId))}】" +
					"<#else>失败</#if>"
	)
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException
	{
		long typeId=RequestUtil.getLong(request, "typeId");
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage resultMessage = null;
		try {
			globalTypeService.importXml(fileLoad.getInputStream(),typeId);
			resultMessage = new ResultMessage(ResultMessage.Success, "导入成功!");
			SysAuditThreadLocalHolder.putParamerter("isAdd", true);
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
			SysAuditThreadLocalHolder.putParamerter("isAdd", false);
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,"导入系统分类失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	/**
	 * 取得系统分类表明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看系统分类表明细",
			detail="查看系统分类表明细"
			)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"typeId");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		GlobalType po = globalTypeService.getById(id);		
		return getAutoView().addObject("globalType", po).addObject("canReturn",canReturn);
	}
	
	/**
	 * 排序分类列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("sortList")
	@Action(description="排序分类列表",detail="排序分类列表")
	public ModelAndView sortList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long parentId=RequestUtil.getLong(request, "parentId",-1);
		List<GlobalType> list=globalTypeService.getByParentId(parentId);
		return getAutoView().addObject("globalTypeList",list);
	}
	/**
	 * 排序分类
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("sort")
	@Action(description="系统分类排序",
			detail="查看系统分类表明细"
			)
	public void sort(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage resultObj=null;
		PrintWriter out = response.getWriter();
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "typeIds");
		if(BeanUtils.isNotEmpty(lAryId)){
			for(int i=0;i<lAryId.length;i++){
				Long typeId=lAryId[i];
				long sn=i+1;
				globalTypeService.updSn(typeId,sn);
			}
		}
		resultObj=new ResultMessage(ResultMessage.Success,"排序分类完成");
		out.print(resultObj);
	}
	
	/**
	 * 移动分类数据。
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("move")
	@Action(description="转移分类",
			detail="<#assign dragEntity=globalTypeService.getById(Long.valueOf(dragId))/>" +
					   "<#assign targetEntity=globalTypeService.getById(Long.valueOf(targetId))/>" +
					   "分类【${dragEntity.typeName}】转移到" +
					   "分类【${targetEntity.typeName}】<#if moveType=='prev'>之前<#elseif moveType=='next'>之后<#else>之内</#if>"
					   )
	public void move(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage resultMessage=null;
		PrintWriter out = response.getWriter();
		long targetId=RequestUtil.getLong(request, "targetId",0);
		long dragId=RequestUtil.getLong(request, "dragId",0);
		String moveType=RequestUtil.getString(request, "moveType");
		try{
			globalTypeService.move(targetId, dragId, moveType);
			resultMessage=new ResultMessage(ResultMessage.Success,"转移分类完成");
		}
		catch(Exception ex){
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,"转移分类失败:" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}
	
	@RequestMapping("edit")
	@Action(description="添加或编辑系统分类",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if typeId>编辑系统分类" +
					"<#assign entity=globalTypeService.getById(Long.valueOf(typeId))/>" +
					"【${entity.typeName}】<#else>添加系统分类</#if>"
					)
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		return getEditView(request);
	}
	
	@RequestMapping("dialog")
	@Action(description="添加或编辑系统分类",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if typeId>编辑系统分类" +
					"<#assign entity=globalTypeService.getById(Long.valueOf(typeId))/>" +
					"【${entity.typeName}】<#else>添加系统分类</#if>"
			)
	public ModelAndView dialog(HttpServletRequest request) throws Exception
	{
		return getEditView(request);
	}
	
	/**
	 * 取得编辑视图。
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private ModelAndView getEditView(HttpServletRequest request) throws Exception{
		int isRoot=RequestUtil.getInt(request,"isRoot",0);
		Long parentId=RequestUtil.getLong(request,"parentId");
		Long typeId=RequestUtil.getLong(request,"typeId");
		int isPrivate=RequestUtil.getInt(request,"isPrivate",0);
		
		String parentName="";
		GlobalType globalType=null;
		boolean isDict=false;
		boolean isAdd=false;
		if(typeId>0){
			globalType= globalTypeService.getById(typeId);	
			parentId=globalType.getParentId();
			isDict=globalType.getCatKey().equals(GlobalType.CAT_DIC);
		}else{
			GlobalType tmpGlobalType=globalTypeService.getInitGlobalType(isRoot,parentId);
			parentName=tmpGlobalType.getTypeName();
			isDict=tmpGlobalType.getCatKey().equals(GlobalType.CAT_DIC);
			globalType=new GlobalType();
			globalType.setType(tmpGlobalType.getType());
			isAdd=true;
		}
		return getAutoView()
				.addObject("globalType",globalType)
				.addObject("parentId",parentId)
				.addObject("isRoot",isRoot)
				.addObject("isAdd",isAdd)
				.addObject("parentName", parentName)
				.addObject("isDict", isDict)
				.addObject("isPrivate", isPrivate);
		
	}
	
	/**
	 * 根据catKey获取分类 。
	 * catKey：分类key
	 * hasRoot：是否有根节点。1，有根节点，0，无根节点。
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKey")
	@ResponseBody
	public List<GlobalType> getByCatKey(HttpServletRequest request){
		String catKey=RequestUtil.getString(request,"catKey");
		logger.debug("[catKey]:"+catKey);
		boolean hasRoot=RequestUtil.getInt(request, "hasRoot",1)==1;
		 
		List<GlobalType> list=globalTypeService.getByCatKey(catKey, hasRoot);
		return list;
	}
	
	/**
	 * 根据catKey获取流程分类Bpm。
	 * catKey：分类key
	 * hasRoot：是否有根节点。1，有根节点，0，无根节点。
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpm")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpm(HttpServletRequest request){
		SysUser curUser=ContextUtil.getCurrentUser();
		//取得当前用户所有的角色Ids
		String roleIds=sysRoleService.getRoleIdsByUserId(curUser.getUserId());
		//取得当前组织
		String orgIds=sysOrgService.getOrgIdsByUserId(curUser.getUserId());
		Set<GlobalType> globalTypeSet=null;
		//非超级管理员需要按权限过滤
//		if(!curUser.getAuthorities().contains(SystemConst.ROLE_GRANT_SUPER)){
//			globalTypeSet=globalTypeService.getByBpmRightCat(curUser.getUserId(), roleIds, orgIds, true);
//		}else{
			globalTypeSet=new HashSet<GlobalType>();
			globalTypeSet.addAll(globalTypeService.getByCatKey(GlobalType.CAT_FLOW, true));
//		}
		
		return globalTypeSet;
	}
	
	/**
	 * 根据catKey获取表单分类
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForForm")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForForm(HttpServletRequest request){
		SysUser curUser=ContextUtil.getCurrentUser();
		//取得当前用户所有的角色Ids
		String roleIds=sysRoleService.getRoleIdsByUserId(curUser.getUserId());
		//取得当前组织
		String orgIds=sysOrgService.getOrgIdsByUserId(curUser.getUserId());
		Set<GlobalType> globalTypeSet=null;
		//非超级管理员需要按权限过滤
		if(!curUser.getAuthorities().contains(SystemConst.ROLE_GRANT_SUPER)){
			globalTypeSet=globalTypeService.getByFormRightCat(curUser.getUserId(), roleIds, orgIds, true);
		}else{
			globalTypeSet=new HashSet<GlobalType>();
			globalTypeSet.addAll(globalTypeService.getByCatKey(GlobalType.CAT_FORM, true));
		}		
		return globalTypeSet;
	}
	
	/**
	 * 取得个人的分类。
	 * @param request
	 * @return
	 */
	@RequestMapping("getPersonType")
	@ResponseBody
	public List<GlobalType> getPersonType(HttpServletRequest request){
		String catKey=RequestUtil.getString(request,"catKey");
		boolean hasRoot=RequestUtil.getInt(request, "hasRoot",1)==1;
		Long userId=ContextUtil.getCurrentUserId();
		 
		List<GlobalType> list=globalTypeService.getPersonType(catKey,userId, hasRoot);
		return list;
	}
	
	/**
	 * 根据catKey获取我的请求流程分类。
	 * 
	 * @Methodname: getByCatKeyForBpmMyRequest
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpmMyRequest")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpmMyRequest(HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilter("creatorId", ContextUtil.getCurrentUserId());
		// 查询我的请求的list
		List<ProcessRun> list = processRunService.getMyRequestList(filter);

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (ProcessRun processRun : list) {
			Long typeId = processRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}
		return this.setGlobalTypeSet(globalTypeList, typeMap, emptyTypeCount);
	}
	
	/**
	 * 根据catKey获取我的办结 流程分类。
	 * 
	 * @Methodname: getByCatKeyForBpmMyCompleted
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpmMyCompleted")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpmMyCompleted(
			HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);
		// 我的办结
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilter("creatorId", ContextUtil.getCurrentUserId());
		List<ProcessRun> list = processRunService.getMyCompletedList(filter);

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (ProcessRun ProcessRun : list) {
			Long typeId = ProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}

		return this.setGlobalTypeSet(globalTypeList, typeMap, emptyTypeCount);
	}
	/**
	 * 计算map的数量
	 * 
	 * @param paraTypeMap
	 *            当前map
	 * @param key
	 * @param num
	 */
	private void mapCount(Map<Long, Integer> map, Long key, Integer num) {
		Integer count = map.get(key);
		if (count != null)
			map.put(key, count + num);
		else
			map.put(key, num);
	}
	
	/**
	 * 设置当前GlobalTypeSet
	 * 
	 * @param globalTypeList
	 *            当前分类list
	 * @param typeMap
	 *            分类Map
	 * @param emptyTypeCount
	 * @return
	 */
	private Set<GlobalType> setGlobalTypeSet(List<GlobalType> globalTypeList,
			Map<Long, Integer> typeMap, Integer emptyTypeCount) {
		SysTypeKey sysTypeKey = sysTypeKeyService.getByKey(GlobalType.CAT_FLOW);
		// 计算流程分类
		Map<Long, Integer> typeCountMap = new HashMap<Long, Integer>();

		for (GlobalType globalType : globalTypeList) {
			Long typeId = globalType.getTypeId();
			Integer count = typeMap.get(typeId);
			if (count == null)
				continue;
			// 计算当前级别分类数量
			this.mapCount(typeCountMap, typeId, count);

			// 计算上级的分类数量(先移除当前的分类)
			Long[] globalTypeIds = this.removeElementToLong(globalType
					.getNodePath().split("[.]"), typeId);
			for (GlobalType type : globalTypeList) {
				for (Long globalTypeId : globalTypeIds) {
					if (type.getTypeId().longValue() == globalTypeId
							.longValue())
						this.mapCount(typeCountMap, globalTypeId, count);
				}
			}
		}
		Set<GlobalType> globalTypeSet = new LinkedHashSet<GlobalType>();
		// 拼接出来最后的结果
		for (GlobalType globalType : globalTypeList) {
			Long typeId = globalType.getTypeId();
			Integer count = typeCountMap.get(typeId);
			if (count == null)
				continue;
			String typeName = globalType.getTypeName();

			globalType.setTypeName(typeName + "(" + count + ")");
			globalTypeSet.add(globalType);
		}

		// 设置空的类型
		this.setEmptyGlobalTypeSet(globalTypeSet, GlobalType.CAT_FLOW, "");
		return globalTypeSet;

	}
	/**
	 * 移除当前数组的元素，并转换Long
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	private Long[] removeElementToLong(String[] array, Long element) {
		if (ArrayUtils.isEmpty(array))
			return null;
		Long[] l = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			l[i] = Long.parseLong(array[i]);
		}
		return (Long[]) ArrayUtils.removeElement(l, element);

	}

	/**
	 * 设置空的类型
	 * 
	 * @param globalTypeSet
	 * @param catKey
	 * @param name
	 *            如果为空 设置为 (0)
	 */
	private void setEmptyGlobalTypeSet(Set<GlobalType> globalTypeSet,
			String catKey, String name) {
		if (BeanUtils.isNotEmpty(globalTypeSet))
			return;
		GlobalType globalType = globalTypeService.getRootByCatKey(catKey);
		if (StringUtils.isEmpty(name))
			name = globalType.getTypeName() + "(0)";
		else
			name = globalType.getTypeName() + name;
		globalType.setTypeName(name);
		globalTypeSet.add(globalType);

	}
	
	/**
	 * 根据catKey获取流程分类Bpm。并且计算待办数量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("forPending")
	@ResponseBody
	public Set<GlobalType> forPending(HttpServletRequest request) {

		String catKey = GlobalType.CAT_FLOW;
		Set<GlobalType> globalTypeSet = new LinkedHashSet<GlobalType>();
		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(catKey,true);
		SysTypeKey sysTypeKey = sysTypeKeyService.getByKey(GlobalType.CAT_FLOW);
		// QueryFilter filter = new QueryFilter(request);
		// 计算待办数量
		List<TaskAmount> countlist = bpmService.getMyTasksCount(ContextUtil.getCurrentUserId());

		// 待办的分类
		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 待办未读的分类
		Map<Long, Integer> typeNotReadMap = new HashMap<Long, Integer>();
		
		for (TaskAmount taskAmount : countlist) {
			
			Long typeId=taskAmount.getTypeId();
			if(BeanUtils.isEmpty( typeId) ){
				continue;
			}	
			typeMap.put(typeId, taskAmount.getTotal());
			typeNotReadMap.put(typeId, taskAmount.getNotRead());
			
		}

		// 计算代办分类
		Map<Long, Integer> typePendingMap = new HashMap<Long, Integer>();
		// 计算代办已读分类
		Map<Long, Integer> typePendingReadMap = new HashMap<Long, Integer>();

		for (GlobalType globalType : globalTypeList) {
			Long typeId = globalType.getTypeId();
			Integer count = typeMap.get(typeId);
			Integer readCount = typeNotReadMap.get(typeId);
			if (count == null)
				continue;
			// 计算当前级分类都数量
			readCount = (readCount == null ? 0 : readCount);
			this.mapCount(typePendingMap, typeId, count);
			this.mapCount(typePendingReadMap, typeId, readCount);
			// 计算上级的分类数量(先移除当前的分类)
			Long[] globalTypeIds = this.removeElementToLong(globalType
					.getNodePath().split("[.]"), typeId);
			for (GlobalType type : globalTypeList) {
				for (Long globalTypeId : globalTypeIds) {
					if (type.getTypeId().longValue() == globalTypeId
							.longValue()) {
						this.mapCount(typePendingMap, globalTypeId, count);
						this.mapCount(typePendingReadMap, globalTypeId,
								readCount);
					}
				}
			}
		}
		// 拼接出来最后的结果
		for (GlobalType globalType : globalTypeList) {
			Long typeId = globalType.getTypeId();
			Integer count = typePendingMap.get(typeId);
			Integer readCount = typePendingReadMap.get(typeId);
			if (count == null)
				continue;
			readCount = (readCount == null ? 0 : readCount);
	
			String typeName = globalType.getTypeName();
			globalType.setTypeName(typeName + "(" + readCount + "/" + count
					+ ")");
			globalTypeSet.add(globalType);
		}

		// 设置空的类型
		this.setEmptyGlobalTypeSet(globalTypeSet, catKey, "(0/0)");
		return globalTypeSet;
	}
	
	/**
	 * 根据catKey获取已办事宜 流程分类。
	 * 
	 * @Methodname: getByCatKeyForBpmAlready
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpmAlready")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpmAlready(HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);
		// 已办事宜
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilter("assignee", ContextUtil.getCurrentUserId().toString());

		List<ProcessRun> list = processRunService.getAlreadyMattersList(filter);

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (ProcessRun ProcessRun : list) {
			Long typeId = ProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}

		return this.setGlobalTypeSet(globalTypeList, typeMap, emptyTypeCount);
	}

	/**
	 * 根据catKey获取流程查询 流程分类。
	 * 
	 * @Methodname: getByCatKeyForSelectPro
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForSelectPro")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForSelectPro(HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);
		// 流程查询
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilter("currentUserId", ContextUtil.getCurrentUserId()
				.toString());

		List<ProcessRun> list = processRunService.selectPro(filter);

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (ProcessRun ProcessRun : list) {
			Long typeId = ProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}

		return this.setGlobalTypeSet(globalTypeList, typeMap, emptyTypeCount);
	}
	/**
	 * 根据catKey获取转办、代理事宜 流程分类。
	 * 
	 * @Methodname: getByCatKeyForBpmAccording
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpmAccording")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpmAccording(HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);
		// 转办事宜
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilter("ownerId", ContextUtil.getCurrentUserId());
		// filter.addFilter("assignType", BpmTaskExe.TYPE_TRANSMIT);

		List<BpmTaskExe> list = bpmTaskExeService.accordingMattersList(filter);

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (BpmTaskExe bpmTaskExe : list) {
			Long typeId = bpmTaskExe.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}

		return this.setGlobalTypeSet(globalTypeList, typeMap, emptyTypeCount);
	}
	/**
	 * 根据catKey获取办结事宜 流程分类。
	 * 
	 * @Methodname: getByCatKeyForBpmCompleted
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpmCompleted")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpmCompleted(HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);
		// 办结事宜
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilter("assignee", ContextUtil.getCurrentUserId().toString());
		List<ProcessRun> list = processRunService
				.getCompletedMattersList(filter);

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (ProcessRun ProcessRun : list) {
			Long typeId = ProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}

		return this.setGlobalTypeSet(globalTypeList, typeMap, emptyTypeCount);
	}
	
	/**
	 * 根据catKey获取新建流程流程分类。
	 * 
	 * @Methodname: getByCatKeyForBpmNewPro
	 * @Discription:
	 * @param request
	 * @return
	 */
	@RequestMapping("getByCatKeyForBpmNewPro")
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpmNewPro(HttpServletRequest request) {

		List<GlobalType> globalTypeList = globalTypeService.getByCatKey(
				GlobalType.CAT_FLOW, true);

		QueryFilter filter = new QueryFilter(request, false);
		Long glTypeId = RequestUtil.getLong(request, "typeId", 0);
		//filter.addFilter("status", BpmDefinition.STATUS_ENABLED);
		Long start=System.currentTimeMillis();
		List<BpmDefinition> list = bpmDefinitionService.getList(filter, glTypeId);
		
		

		Map<Long, Integer> typeMap = new HashMap<Long, Integer>();
		// 空类型的数量
		int emptyTypeCount = 0;
		for (BpmDefinition bpmDefinition : list) {
			Long typeId = bpmDefinition.getTypeId();
			if (BeanUtils.isNotEmpty(typeId)) {
				this.mapCount(typeMap, typeId, 1);
			} else {
				emptyTypeCount++;
			}
		}
		Set<GlobalType> globalTypeSet = this.setGlobalTypeSet(globalTypeList,typeMap, emptyTypeCount);
		
		logger.debug("getByCatKeyForBpmNewPro:--------------" +(System.currentTimeMillis()-start) +"-------------------");
		
		SysTypeKey sysTypeKey = sysTypeKeyService.getByKey(GlobalType.CAT_FLOW);

		// 添加原OA流程节点
		if ("1".equals(AppConfigUtil.get("isNewProcessIncludeOA"))) {
			GlobalType oaType = new GlobalType();
			oaType.setTypeName("原OA流程");
			oaType.setCatKey(sysTypeKey.getTypeKey());
			oaType.setParentId(sysTypeKey.getTypeId());
			oaType.setIsParent("false");
			oaType.setTypeId(-1000L);
			oaType.setType(sysTypeKey.getType());
			oaType.setNodePath(sysTypeKey.getTypeId() + ".");
			globalTypeSet.add(oaType);
		}

		return globalTypeSet;
	}
	
}
