package com.hotent.platform.controller.console;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.engine.FreemarkEngine;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.system.MessageSend;
import com.hotent.platform.model.system.Resources;
import com.hotent.platform.model.system.SubSystem;
import com.hotent.platform.model.system.SysOrg;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.system.DesktopMycolumnService;
import com.hotent.platform.service.system.MessageSendService;
import com.hotent.platform.service.system.ResourcesService;
import com.hotent.platform.service.system.SubSystemService;
import com.hotent.platform.service.system.SubSystemUtil;
import com.hotent.platform.service.system.SysOrgService;
import com.hotent.platform.service.system.SysPaurService;




@Controller
@RequestMapping("/platform/console")
public class MainController extends BaseController {
	@Resource
	private SubSystemService subSystemService;
	@Resource
	private ResourcesService resourcesService;
	@Resource
	private DesktopMycolumnService desktopMycolumnService;
	@Resource 
	private MessageSendService msgSendService;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	private SysOrgService sysOrgService;

	
	/**
	 * 切换组织
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("turnToMain")
	public ModelAndView turnToMain (HttpServletRequest request,HttpServletResponse response) throws IOException {	
		logger.debug("....................................跳转....................................");
		return this.getView("console","turnToMain");
	}
	
	/**
	 * 控制台页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("main")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response) throws Exception{	
		//重置资源读取标识
		//SystemResourcesServiceImpl.reSetModify();
//		List<SubSystem> subSystemList=subSystemService.getByUser(ContextUtil.getCurrentUser());
//		return this.getView("console","selectCurrSys").addObject("subSystemList",subSystemList);
		SysUser curUser=ContextUtil.getCurrentUser();
		
		List<SubSystem> subSystemList=subSystemService.getByUser(curUser);
		SubSystem currentSystem=SubSystemUtil.getCurrentSystem(request);
		
		
		// 得到个人未读信息
		List<MessageSend> list=msgSendService.getNotReadMsg(ContextUtil.getCurrentUserId());
		Long currentUserId=ContextUtil.getCurrentUser().getUserId();
		List<SysOrg> sysOrgs = sysOrgService.getOrgsByUserId(currentUserId);
		SysOrg curSysOrg = ContextUtil.getCurrentOrg();
		//取个性话设置参数并设置logo路径
		String skinStyle=ContextUtil.getCurrentUserSkin(request);
		
		//获取我的标签主页资源ID
		Long myFlagResId = 0L;
		Resources res = resourcesService.getByAlias("myFlag");
		if(res != null && res.getResId() != null){
			myFlagResId = res.getResId();
		}
		
		//前系统有系统
		if(currentSystem!=null ){
			//以前有选择系统,但是现在即不再拥用该系统的权限
			if(!subSystemList.contains(currentSystem)){
				
				return this.getView("console","selectCurrSys").addObject("subSystemList",subSystemList)
						.addObject("skinStyle",skinStyle)
						.addObject("readMsg", list.size()).addObject("userId",currentUserId)
						.addObject("sysOrgs",sysOrgs).addObject("curSysOrg",curSysOrg)
						.addObject("myFlagResId", myFlagResId);
				
			}
			if(currentSystem.getLogo()!=null){
				String logoPath=currentSystem.getLogo();
				Integer startIndex=logoPath.indexOf("/styles/");
				Integer endIndex=logoPath.indexOf("/images/");
				String oldStyle=logoPath.substring(startIndex+8, endIndex);
				logoPath=logoPath.replace(oldStyle, skinStyle);
				currentSystem.setLogo(logoPath);
			}
			
			
			return this.getView("console","main")
			.addObject("skinStyle",skinStyle)
			.addObject("currentSystem", currentSystem)
			.addObject("currentSystemId", currentSystem.getSystemId())
			.addObject("subSystemList",subSystemList).addObject("readMsg", list.size()).addObject("userId",currentUserId)
			.addObject("sysOrgs",sysOrgs).addObject("curSysOrg",curSysOrg).addObject("myFlagResId", myFlagResId);
		}	
		
		String defaultEntrySystemId=AppConfigUtil.get("defaultEntrySystemId");
		Long defaultSystemId = null;
		if (StringUtils.isNotEmpty(defaultEntrySystemId)) {
			defaultSystemId = Long.valueOf(defaultEntrySystemId);
		}
		System.out.println("--"+defaultSystemId);
		boolean defaultEnable = false;
		if(subSystemList!=null){
			if(defaultSystemId != null){
				for(SubSystem m : subSystemList){
					if(defaultSystemId.longValue() == m.getSystemId()){
						defaultEnable = true;
						break;
					}
				}
			}
			boolean subSystemEnable = true;
			if(defaultEnable){
				subSystemService.setCurrentSystem(defaultSystemId,request, response);
			}else if(subSystemList.size() == 1){
				//只有一个系统有权限
				subSystemService.setCurrentSystem(subSystemList.get(0).getSystemId(),request, response);
			}else{
				subSystemEnable = false;
			}
			if(subSystemEnable){
				return this.getView("console","main")
						.addObject("skinStyle",skinStyle)		
						.addObject("currentSystem", subSystemList.get(0))
						.addObject("currentSystemId", subSystemList.get(0).getSystemId())
						.addObject("subSystemList",subSystemList).addObject("readMsg", list.size())
						.addObject("userId",currentUserId).addObject("sysOrgs",sysOrgs).addObject("curSysOrg",curSysOrg)
						.addObject("myFlagResId", myFlagResId);
			}
		}
		//以前没有选择系统
		if(currentSystem==null){
			return this.getView("console","selectCurrSys").addObject("subSystemList",subSystemList)
					.addObject("skinStyle",skinStyle)
					.addObject("readMsg", list.size()).addObject("userId",currentUserId)
					.addObject("sysOrgs",sysOrgs).addObject("curSysOrg",curSysOrg)
					.addObject("myFlagResId", myFlagResId);
		}
		
		
		return null;
	
	}
	/**
	 * 控制台页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("home")
	public ModelAndView home(HttpServletRequest request,HttpServletResponse response) throws Exception{		
		long userId = ContextUtil.getCurrentUserId();
		String ctxPath=request.getContextPath();
		Map mapData=desktopMycolumnService.getMyDeskData(userId,ctxPath);
		if(mapData==null)return null;
		
		String html = freemarkEngine.mergeTemplateIntoString("desktop/getDeskTop.ftl", mapData);
		
		return this.getView("console","home").addObject("html", html);
	}
	
	/**
	 * 获取桌面栏目对应模块的更多信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getResourceNode")
	@ResponseBody
	public Resources getResourceNode(HttpServletRequest request,HttpServletResponse response){
		Resources resource=null;
		try{
			String columnUrl=RequestUtil.getString(request, "columnUrl");
			resource=resourcesService.getByUrl(columnUrl);
		}catch (Exception e) {
			return null;
		}
		return resource;
	}

	/**
	 * 设置默认系统
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("saveCurrSys")
	public void saveCurrSys(HttpServletRequest request,HttpServletResponse response) throws IOException {	
		Long systemId=RequestUtil.getLong(request, "systemId");
		subSystemService.setCurrentSystem(systemId,request, response);
		response.sendRedirect(request.getContextPath()+"/platform/console/main.ht");
	}
	
	/**
	 * 切换组织
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("switchCurrentOrg")
	public void switchCurrentOrg(HttpServletRequest request,HttpServletResponse response) throws IOException {	
		Long orgId=RequestUtil.getLong(request, "orgId");
		ContextUtil.setCurrentOrg(orgId);
		response.sendRedirect(request.getContextPath()+"/platform/console/main.ht");
	}
	
	/**
	 * 取得总分类表用于显示树层次结构的分类可以分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception-------------
	 */
	@RequestMapping("getSysRolResTreeData")
	//@Action(description="取得总分类表树形数据")
	@ResponseBody
	public List<Resources> getSysRolResTreeData(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SubSystem currentSystem=SubSystemUtil.getCurrentSystem(request);
		List<Resources> resourcesList=resourcesService.getSysMenu(currentSystem,ContextUtil.getCurrentUser());
		ResourcesService.addIconCtxPath(resourcesList, request.getContextPath());
		return resourcesList;
	}
	
	/**
	 * 验证expression中的脚本
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getValidateResult")
	@ResponseBody
	public Object getValidateResult(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String script=RequestUtil.getString(request, "script");
		GroovyScriptEngine engine=(GroovyScriptEngine)AppUtil.getBean(GroovyScriptEngine.class);
		
		Map reObj=new HashMap();
		try{
			Object result = engine.executeObject(script, null);
			reObj.put("hasError", false);
			reObj.put("errorMsg", "");
			
			if(result!=null){
				reObj.put("result", result);
				reObj.put("resultType", result.getClass().getName());
			}
		}
		catch(Exception ex){
			reObj.put("hasError", true);
			reObj.put("errorMsg", ex.getMessage());
		}
		return reObj;
	}
}
