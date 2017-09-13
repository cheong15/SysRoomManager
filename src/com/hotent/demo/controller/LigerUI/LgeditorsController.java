
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
import com.hotent.demo.model.LigerUI.Lgeditors;
import com.hotent.demo.service.LigerUI.LgeditorsService;
/**
 * 对象功能:list页面带有树的Demo
 */
@Controller
@RequestMapping("/demo/LigerUI/lgeditors/")
public class LgeditorsController extends BaseController
{
	@Resource
	private LgeditorsService lgeditorsService;
	/**
	 * 添加或更新lgEditor多样化。
	 * @param request
	 * @param response
	 * @param lgeditors 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新lgEditor多样化")
	public void save(HttpServletRequest request, HttpServletResponse response,Lgeditors lgeditors) throws Exception
	{
		String resultMsg=null;		
		String json=RequestUtil.getString(request, "json");
		if(StringUtil.isNotEmpty(json)){
			JSONObject obj = JSONObject.fromObject(json);
			lgeditors = (Lgeditors)JSONObject.toBean(obj, Lgeditors.class);
			lgeditors.setBirthday(DateUtil.parseDate(obj.getString("birthday")));
			lgeditors.setJoinDate(DateUtil.parseDate(obj.getString("joinDate")));
		}	
		try{
			if(lgeditors.getId()==null){
				Long id=UniqueIdUtil.genId();
				lgeditors.setId(id);
				lgeditorsService.add(lgeditors);
				resultMsg=getText("record.added","lgEditor多样化");
			}else{
			    lgeditorsService.update(lgeditors);
				resultMsg=getText("record.updated","lgEditor多样化");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得lgEditor多样化分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getList")
	@Action(description="查看jqueryUI用户表分页列表")
	public void getList(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		QueryFilter  queryFilter=new QueryFilter(request,true,"lgeditorsItem");
		List<Lgeditors> list=lgeditorsService.getAll(queryFilter);
		sendJsonToWeb(list,response,queryFilter);  
	}
	
	/**
	 * 删除lgEditor多样化
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除lgEditor多样化")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			lgeditorsService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除lgEditor多样化成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
		getList(request,response);
	}
	
	/**
	 * 	编辑lgEditor多样化
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑lgEditor多样化")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		Lgeditors lgeditors=lgeditorsService.getById(id);
		
		return getAutoView().addObject("lgeditors",lgeditors)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得lgEditor多样化明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看lgEditor多样化明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		Lgeditors lgeditors=lgeditorsService.getById(id);
		return getAutoView().addObject("lgeditors", lgeditors);
	}
	
}
