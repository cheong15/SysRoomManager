package com.hotent.platform.controller.system;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotent.core.annotion.Action;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.system.Position;
import com.hotent.platform.model.system.UserPosition;
import com.hotent.platform.service.system.PositionService;
import com.hotent.platform.service.system.UserPositionService;

/**
 * 对象功能:用户岗位表 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
@Controller
@RequestMapping("/platform/system/userPosition/")
public class UserPositionController extends BaseController
{
	@Resource
	private UserPositionService userPositionService;
	@Resource
	private PositionService positionService;
	
	
	/**
	 * 删除用户岗位表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除用户岗位表")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage message=null;
		String preUrl= RequestUtil.getPrePage(request);
		try {
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "userPosId");
			userPositionService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除用户岗位成功");
		} catch (Exception e) {
			message=new ResultMessage(ResultMessage.Fail, "删除用户岗位失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description="编辑用户岗位表")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		ModelAndView mv=this.getAutoView();
		Long posId=RequestUtil.getLong(request,"posId");
		
		Position position= positionService.getById(posId);
		if(position!=null){
			mv.addObject("position", position);
			List<UserPosition> list=userPositionService.getAll(new QueryFilter(request,"userPositionItem",true));
			mv.addObject("userPositionList",list);
		}
		
		String returnUrl=RequestUtil.getString(request, "returnUrl", RequestUtil.getPrePage(request));
		mv.addObject("returnUrl", returnUrl);
		
		return mv;
	}


	
	/**
	 * 添加或更新用户岗位表。
	 * @param request
	 * @param response
	 * @param userPosition 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("add")
	@Action(description="添加或更新用户岗位表")
	public void add(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long posId=RequestUtil.getLong(request, "posId",0);
		Long[] userIds =RequestUtil.getLongAryByStr(request, "userIds");
		userPositionService.add(posId, userIds);
		
		String returnUrl=RequestUtil.getString(request, "returnUrl", RequestUtil.getPrePage(request));
		response.sendRedirect(RequestUtil.getPrePage(request));
	}
	
	/**
	 * 设置是否主岗位。
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("setIsPrimary")
	public void setIsPrimary(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Long userPosId=RequestUtil.getLong(request, "userPosId",0);
		ResultMessage message=null;
		String preUrl= RequestUtil.getPrePage(request);
		try{
			userPositionService.setIsPrimary(userPosId);
			message=new ResultMessage(ResultMessage.Success, "设置岗位成功");
		}
		catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "设置岗位失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	
	@RequestMapping("get")
	@Action(description="编辑用户岗位表")
	public ModelAndView get(HttpServletRequest request) throws Exception
	{
		ModelAndView mv=this.getAutoView();
		Long posId=RequestUtil.getLong(request,"posId");
		
		Position position= positionService.getById(posId);
		if(position!=null){
			mv.addObject("position", position);
			List<UserPosition> list=userPositionService.getAll(new QueryFilter(request,"userPositionItem",false));
			mv.addObject("userPositionList",list);
		}
		
		String returnUrl=RequestUtil.getString(request, "returnUrl", RequestUtil.getPrePage(request));
		mv.addObject("returnUrl", returnUrl);
		return mv;
	}
	
	
	@RequestMapping("getUserListByPosId")
	@ResponseBody
	@Action(description="根据岗位ID取得用户List")
	public List<UserPosition> getUserListByPosId(HttpServletRequest request) throws Exception
	{
		String posId=RequestUtil.getString(request,"posId");
		return userPositionService.getUserByPosIds(posId);
	}

}
