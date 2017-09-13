
package com.hotent.demo.controller.LigerUI;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.hotent.demo.model.LigerUI.LgmoreLg;
import com.hotent.demo.service.LigerUI.LgmoreLgService;
/**
 * 页内编辑多种编辑器模式
 */
@Controller
@RequestMapping("/demo/LigerUI/lgmoreLg/")
public class LgmoreLgController extends BaseController
{
	@Resource
	private LgmoreLgService lgmoreLgService;
	/**
	 * 添加或更新lg多样化。
	 * @param request
	 * @param response
	 * @param lgmoreLg 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新lg多样化")
	public void save(HttpServletRequest request, HttpServletResponse response,LgmoreLg lgmoreLg) throws Exception
	{
		String resultMsg=null;		
		String json=RequestUtil.getString(request, "json");
		if(StringUtil.isNotEmpty(json)){
			JSONObject obj = JSONObject.fromObject(json);
			lgmoreLg = (LgmoreLg)JSONObject.toBean(obj, LgmoreLg.class);
			lgmoreLg.setBirthday(DateUtil.parseDate(obj.getString("birthday")));
			lgmoreLg.setJiehunDay(DateUtil.parseDate(obj.getString("jiehunDay")));
		}	
		try{
			if(lgmoreLg.getId()==null){
				Long id=UniqueIdUtil.genId();
				lgmoreLg.setId(id);
				lgmoreLgService.add(lgmoreLg);
				resultMsg=getText("添加","lg多样化");
			}else{
			    lgmoreLgService.update(lgmoreLg);
				resultMsg=getText("更新","lg多样化");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得lg多样化分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getList")
	@Action(description="查看lg多样化分页列表")
	public void getList(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		QueryFilter  queryFilter=new QueryFilter(request,true,"lgmoreLgItem");
		List<LgmoreLg> list=lgmoreLgService.getAll(queryFilter);
		sendJsonToWeb(list,response,queryFilter);  
	}
	
	/**
	 * 删除lg多样化
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除lg多样化")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			lgmoreLgService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除lg多样化成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
		getList(request,response);
	}
	
	/**
	 * 	编辑lg多样化
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑lg多样化")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		LgmoreLg lgmoreLg=lgmoreLgService.getById(id);
		
		return getAutoView().addObject("lgmoreLg",lgmoreLg)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得lg多样化明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看lg多样化明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		LgmoreLg lgmoreLg=lgmoreLgService.getById(id);
		return getAutoView().addObject("lgmoreLg", lgmoreLg);
	}
	
}
