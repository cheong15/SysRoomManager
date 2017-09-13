package com.hotent.platform.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.hotent.core.annotion.Action;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseFormController;

import com.hotent.platform.model.system.ReportParam;
import com.hotent.platform.service.system.ReportParamService;

/**
 * 对象功能:报表参数 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-12 11:08:13
 */
@Controller
@RequestMapping("/platform/system/reportParam/")
public class ReportParamFormController extends BaseFormController
{
	@Resource
	private ReportParamService reportParamService;
	
	/**
	 * 添加或更新报表参数。
	 * @param request
	 * @param response
	 * @param reportParam 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新报表参数")
	public void save(HttpServletRequest request, HttpServletResponse response, ReportParam reportParam,BindingResult bindResult) throws Exception
	{
		
		ResultMessage resultMessage=validForm("reportParam", reportParam, bindResult, request);
		//add your custom validation rule here such as below code:
		//bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
		if(resultMessage.getResult()==ResultMessage.Fail)
		{
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg=null;
		if(reportParam.getPARAMID()==null){
			reportParam.setPARAMID(UniqueIdUtil.genId());
			reportParamService.add(reportParam);
			resultMsg="添加报表参数成功";
		}else{
			reportParamService.update(reportParam);
			resultMsg="更新报表参数成功";
		}
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param PARAMID
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected ReportParam getFormObject(@RequestParam("PARAMID") Long PARAMID,Model model) throws Exception {
		logger.debug("enter ReportParam getFormObject here....");
		ReportParam reportParam=null;
		if(PARAMID!=null){
			reportParam=reportParamService.getById(PARAMID);
		}else{
			reportParam= new ReportParam();
		}
		return reportParam;
    }

}
