
package com.hotent.demo.controller.LigerUI;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.DateUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.demo.model.LigerUI.Uiuser;
import com.hotent.demo.service.LigerUI.UiuserService;

/**
 * 对象功能:用于联合查询的ligerui的 Demo
 */
@Controller
@RequestMapping("/demo/LigerUI/uiuser/")
public class UiuserController extends BaseController
{
	@Resource
	private UiuserService uiuserService;
	/**
	 * 添加或更新jqueryUI用户表。
	 * @param request
	 * @param response
	 * @param uiuser 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新jqueryUI用户表")
	public void save(HttpServletRequest request, HttpServletResponse response,Uiuser uiuser) throws Exception
	{
		String resultMsg=null;		
		String json=RequestUtil.getString(request, "json");
		if(StringUtil.isNotEmpty(json)){
			JSONObject obj = JSONObject.fromObject(json);
			uiuser = (Uiuser)JSONObject.toBean(obj, Uiuser.class);
		}
		try{
			if(uiuser.getId()==null){
				Long id=UniqueIdUtil.genId();
				uiuser.setId(id);
				uiuserService.add(uiuser);
				resultMsg=getText("record.added","jqueryUI用户表");
			}else{
			    uiuserService.update(uiuser);
				resultMsg=getText("record.updated","jqueryUI用户表");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			e.printStackTrace();
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
		
	}
	@RequestMapping("getList")
	@Action(description="查看jqueryUI用户表分页列表")
	public void getList(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		QueryFilter  queryFilter=new QueryFilter(request,true,"uiuserItem");
		//用于联合查询的ligerui的 Demo
		Object list=uiuserService.getListJoinLgeditors(queryFilter);
		sendJsonToWeb(list,response,queryFilter);
	}
	
	
	@RequestMapping("getListByURL")
	@Action(description="查看jqueryUI用户表分页列表")
	public String getListByURL(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		QueryFilter  queryFilter=new QueryFilter(request,"uiuserItem",false);
		List<Uiuser> list=uiuserService.getAll(queryFilter);
		JSONArray json=new JSONArray();
		return json.fromObject(list).toString();
	}
	
	/**
	 * 删除jqueryUI用户表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除jqueryUI用户表")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			uiuserService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除jqueryUI用户表成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
		getList(request,response);
	}
	
	/**
	 * 	编辑jqueryUI用户表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑jqueryUI用户表")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		Uiuser uiuser=uiuserService.getById(id);
		
		return getAutoView().addObject("uiuser",uiuser)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得jqueryUI用户表明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看jqueryUI用户表明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		Uiuser uiuser=uiuserService.getById(id);
		return getAutoView().addObject("uiuser", uiuser);
	}
	
}
