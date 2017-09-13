
package com.hotent.platform.controller.system;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;

import com.hotent.platform.model.system.Resources;
import com.hotent.platform.model.system.SubSystem;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.system.ResourcesService;
import com.hotent.platform.service.system.SubSystemService;
import com.hotent.platform.service.system.SubSystemUtil;
import com.hotent.core.log.SysAuditThreadLocalHolder;

/**
 * 对象功能:子系统管理 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-21 12:22:06
 */
@Controller
@RequestMapping("/platform/system/subSystem/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class SubSystemController extends BaseController
{
	@Resource
	private SubSystemService service;
	@Resource
	private ResourcesService resourcesService;

	@RequestMapping("tree")
	@ResponseBody
	public List<SubSystem> tree(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		List<SubSystem> list=service.getAll();
		SubSystem root=new SubSystem();
		root.setSystemId(0l);
		root.setParentId(-1l);
		root.setSysName("所有系统");
		list.add(root);
		
		return list;
	}
	
	/**
	 * 取得子系统管理分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="取得子系统管理分页列表",detail="取得子系统管理分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<SubSystem> list=service.getAll(new QueryFilter(request,"subSystemItem"));
		ModelAndView mv=this.getAutoView().addObject("subSystemList",list);
		
		return mv;
	}
	
	
	@RequestMapping("edit")
	@Action(description="添加或编辑子系统管理",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if isAdd>添加子系统管理<#else>编辑子系统管理" +
					"<#assign entity=SubSystemService.getById(Long.valueOf(id))/>" +
					"【${entity.sysName}】</#if>"
	)
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		SubSystem subSystem=null;
		boolean isadd=true;
		if(id!=0){
			subSystem= service.getById(id);
			isadd=false;
		}else{
			subSystem=new SubSystem();
		}
		SysAuditThreadLocalHolder.putParamerter("isAdd", isadd);
		return getAutoView().addObject("subSystem",subSystem).addObject("returnUrl", returnUrl);
	}
	
	


	/**
	 * 删除子系统管理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除子系统管理",
	execOrder=ActionExecOrder.BEFORE,
	detail= "删除子系统管理" +
			"<#list StringUtils.split(id,\",\") as item>" +
				"<#assign entity=subSystemService.getById(Long.valueOf(item))/>" +
				"【${entity.sysName}】" +
			"</#list>"
			)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage message=null;
		String preUrl= RequestUtil.getPrePage(request);
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			service.delByIds(lAryId);
			
			//从session清除当前系统。
			SubSystemUtil.removeSystem();
			message=new ResultMessage(ResultMessage.Success,"删除子系统成功");
		}catch (Exception e) {
			message=new ResultMessage(ResultMessage.Fail,"删除子系统失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}


	/**
	 * 取得子系统管理明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="取得子系统管理明细",detail="取得子系统管理明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		SubSystem po = service.getById(id);		
		return this.getAutoView().addObject("subSystem", po).addObject("canReturn",canReturn);
		
	}
	
	/**
	 * 导出子系统资源
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description="导出子系统资源",
			execOrder=ActionExecOrder.AFTER,
			detail="导出子系统资源【${subSystem.sysName}】"
			)
	public void exportXml(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		long id=RequestUtil.getLong(request, "systemId");
		if(id!=0){
			String strXml=service.exportXml(id);
			SubSystem subSystem=service.getById(id);
			String fileName=subSystem.getAlias();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(fileName,"UTF-8") + ".xml");
			response.getWriter().write(strXml);
			response.getWriter().flush();
			response.getWriter().close();
			try {
				SysAuditThreadLocalHolder.putParamerter("subSystem", subSystem);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		
	}
	
	/**
	 * 导入子系统资源。
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description="导入子系统资源",
			execOrder=ActionExecOrder.AFTER,
			detail="导入子系统资源<#if isAdd><#assign entity=subSystemService.getById(Long.valueOf(systemId))/>" +
					"【${entity.sysName}】成功<#else>失败</#if>"
			)
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException
	{
		long systemId=RequestUtil.getLong(request, "systemId");
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage resultMessage = null;
		try {
			service.importXml(fileLoad.getInputStream(),systemId);
			resultMessage = new ResultMessage(ResultMessage.Success, "导入成功!");
			SysAuditThreadLocalHolder.putParamerter("isAdd", true);
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			SysAuditThreadLocalHolder.putParamerter("isAdd", false);
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,"导入失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
}
