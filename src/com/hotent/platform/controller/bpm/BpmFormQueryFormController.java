package com.hotent.platform.controller.bpm;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotent.core.annotion.Action;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.table.BaseTableMeta;
import com.hotent.core.table.IDbView;
import com.hotent.core.table.TableModel;
import com.hotent.core.table.impl.TableMetaFactory;

import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.hotent.platform.model.bpm.BpmFormQuery;
import com.hotent.platform.model.form.BpmFormDialog;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.model.system.SysDataSource;
import com.hotent.platform.service.bpm.BpmFormQueryService;
import com.hotent.platform.service.system.SysDataSourceService;
import com.hotent.core.web.ResultMessage;
/**
 * 对象功能:通用表单查询 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2012-11-27 10:37:13
 */
@Controller
@RequestMapping("/platform/bpm/bpmFormQuery/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class BpmFormQueryFormController extends BaseController
{
	@Resource
	private BpmFormQueryService bpmFormQueryService;
	
	/**
	 * 添加或更新通用表单查询。
	 * @param request
	 * @param response
	 * @param bpmFormQuery 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新通用表单查询",
			detail="<#if StringUtil.isNotEmpty(isAdd)>" +
			"<#if isAdd==0>添加<#else>更新</#if>" +
			"通用表单查询【${SysAuditLinkService.getBpmFormQueryLink(Long.valueOf(queryId))}】成功" +
			"<#else>添加或更新通用表单查询【${name}】失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response,BpmFormQuery bpmFormQuery) throws Exception
	{
		String  isAdd="0";
		String resultMsg=null;		
		if(StringUtil.isEmpty(bpmFormQuery.getConditionfield())){
			resultMsg="未设置查询的字段";
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}
		if(StringUtil.isEmpty(bpmFormQuery.getResultfield())){
			resultMsg="未设置返回的字段";
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}
		
		if(bpmFormQuery.getId()==0){
			bpmFormQuery.setId(UniqueIdUtil.genId());
			String alias=bpmFormQuery.getAlias();
			boolean isExist=bpmFormQueryService.isExistAlias(alias);
			if(isExist){
				resultMsg="该别名已经存在！";
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
				return;
			}
			bpmFormQueryService.add(bpmFormQuery);
			resultMsg="添加通用表单查询成功";
			
		}else{
			String alias=bpmFormQuery.getAlias();
			Long id=bpmFormQuery.getId();
			boolean isExist=bpmFormQueryService.isExistAliasForUpd(id, alias);
			if(isExist){
				resultMsg="该别名已经存在！";
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
				return;
			}
			isAdd="1";
			bpmFormQueryService.update(bpmFormQuery);
			resultMsg="更新通用表单查询成功";
		}
		SysAuditThreadLocalHolder.putParamerter("isAdd", isAdd);
		SysAuditThreadLocalHolder.putParamerter("queryId", bpmFormQuery.getId().toString());
		SysAuditThreadLocalHolder.putParamerter("name", bpmFormQuery.getName());
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 取得 BpmFormQuery 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute
	protected BpmFormQuery getFormObject(@RequestParam("id") Long id, Model model) throws Exception
	{
		BpmFormQuery bpmFormQuery = null;
		if (id > 0){
			bpmFormQuery = bpmFormQueryService.getById(id);
		} 
		else{
			bpmFormQuery = new BpmFormQuery();
		}
		return bpmFormQuery;
	}
}
