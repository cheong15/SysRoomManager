package com.hotent.platform.controller.form;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.TimeUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseFormController;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.form.BpmFormDefService;
import com.hotent.platform.service.form.FormUtil;


/**
 * 对象功能:BPM_FORM_DEF 控制器类 
 * 开发公司:广州宏天软件有限公司 
 * 开发人员:xwy 
 * 创建时间:2011-12-22 11:07:56
 */
@Controller
@RequestMapping("/platform/form/bpmFormDef/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class BpmFormDefFormController extends BaseFormController
{
	@Resource
	private BpmFormDefService service;
	

	/**
	 * 添加或更新自定义表单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description = "添加或更新自定义表单",execOrder=ActionExecOrder.AFTER,
			detail="<#if isSuccess>" +
					"<#if isAdd>添加" +
					"<#else>更新" +
					"</#if>自定义表单：" +
					"【${SysAuditLinkService.getBpmFormDefLink(defId)}】成功" +
					"<#else>" +
					"添加或更新自定义表单失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// 表定义
		String data = request.getParameter("data");
		
		JSONObject formDefJson=JSONObject.fromObject(data);
		
		String publishTime= formDefJson.getString("publishTime");
		if(StringUtil.isEmpty(publishTime)){
			formDefJson.put("publishTime", TimeUtil.getCurrentTime());
		}
		
		BpmFormDef bpmFormDef = (BpmFormDef) JSONObject.toBean(formDefJson, BpmFormDef.class);
			
		String html=bpmFormDef.getHtml();
		html=html.replace("？", "");
		String template=FormUtil.getFreeMarkerTemplate(html,bpmFormDef.getTableId());		
		bpmFormDef.setTemplate(template);

		boolean isSuccess=true;
		boolean isadd=false;
		long defId=0;
		try{
			if (bpmFormDef.getFormDefId() == 0){
				isadd=true;
				service.addForm(bpmFormDef);
				String msg = "添加自定义表单成功";
				writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
			}
			else{
				service.updateForm(bpmFormDef);
				String msg = "更新自定义表单成功";
				writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
			}
			defId=bpmFormDef.getFormDefId();
		}
		catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,"保存自定义表单数据失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
			isSuccess=false;
		}
		SysAuditThreadLocalHolder.putParamerter("isAdd", isadd);
		SysAuditThreadLocalHolder.putParamerter("isSuccess", isSuccess);
		SysAuditThreadLocalHolder.putParamerter("defId", String.valueOf(defId));
	}
	
}
