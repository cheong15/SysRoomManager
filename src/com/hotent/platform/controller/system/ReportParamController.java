package com.hotent.platform.controller.system;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hotent.core.annotion.Action;
import org.springframework.web.servlet.ModelAndView;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.system.ReportParam;
import com.hotent.platform.service.system.ReportParamService;
import com.hotent.core.web.ResultMessage;

/**
 * 对象功能:报表参数 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-12 11:08:12
 */
@Controller
@RequestMapping("/platform/system/reportParam/")
public class ReportParamController extends BaseController
{
	@Resource
	private ReportParamService reportParamService;
	
	/**
	 * 取得报表参数分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看报表参数分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<ReportParam> list=reportParamService.getAll(new QueryFilter(request,"reportParamItem"));
		ModelAndView mv=this.getAutoView().addObject("reportParamList",list);
		
		return mv;
	}
	
	/**
	 * 删除报表参数
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除报表参数")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "PARAMID");
			reportParamService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除报表参数成功!");
		}
		catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description="编辑报表参数")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long PARAMID=RequestUtil.getLong(request,"PARAMID");
		String returnUrl=RequestUtil.getPrePage(request);
		ReportParam reportParam=null;
		if(PARAMID!=0){
			 reportParam= reportParamService.getById(PARAMID);
		}else{
			reportParam=new ReportParam();
		}
		return getAutoView().addObject("reportParam",reportParam).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得报表参数明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看报表参数明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"PARAMID");
		ReportParam reportParam = reportParamService.getById(id);		
		return getAutoView().addObject("reportParam", reportParam);
	}

}
