package com.hotent.gzdc.controller.kettle;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hotent.core.annotion.Action;
import org.springframework.web.servlet.ModelAndView;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.util.StringUtil;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import com.hotent.core.util.StringUtil;

import com.hotent.gzdc.model.kettle.KettleHaSlave;
import com.hotent.gzdc.service.kettle.KettleHaSlaveService;
import com.hotent.core.web.ResultMessage;
/**
 *<pre>
 * 对象功能:KETTLE_HA_SLAVE 控制器类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:13
 *</pre>
 */
@Controller
@RequestMapping("/gzdc/kettle/kettleHaSlave/")
public class KettleHaSlaveController extends BaseController
{
	@Resource
	private KettleHaSlaveService kettleHaSlaveService;
	
	
	/**
	 * 添加或更新KETTLE_HA_SLAVE。
	 * @param request
	 * @param response
	 * @param kettleHaSlave 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新KETTLE_HA_SLAVE")
	public void save(HttpServletRequest request, HttpServletResponse response,KettleHaSlave kettleHaSlave) throws Exception
	{
		String resultMsg=null;		
		try{
			if(kettleHaSlave.getIdSlave()==null||kettleHaSlave.getIdSlave()==0){
				kettleHaSlave.setIdSlave(UniqueIdUtil.genId());
				kettleHaSlaveService.add(kettleHaSlave);
				resultMsg=getText("record.added","KETTLE_HA_SLAVE");
			}else{
			    kettleHaSlaveService.update(kettleHaSlave);
				resultMsg=getText("record.updated","KETTLE_HA_SLAVE");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得KETTLE_HA_SLAVE分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看KETTLE_HA_SLAVE分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<KettleHaSlave> list=kettleHaSlaveService.getAll(new QueryFilter(request,"kettleHaSlaveItem"));
		ModelAndView mv=this.getAutoView().addObject("kettleHaSlaveList",list);
		
		return mv;
	}
	
	/**
	 * 删除KETTLE_HA_SLAVE
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除KETTLE_HA_SLAVE")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "idSlave");
			kettleHaSlaveService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除KETTLE_HA_SLAVE成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑KETTLE_HA_SLAVE
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑KETTLE_HA_SLAVE")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long idSlave=RequestUtil.getLong(request,"idSlave",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		KettleHaSlave kettleHaSlave=kettleHaSlaveService.getById(idSlave);
		
		return getAutoView().addObject("kettleHaSlave",kettleHaSlave)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得KETTLE_HA_SLAVE明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看KETTLE_HA_SLAVE明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long idSlave=RequestUtil.getLong(request,"idSlave");
		KettleHaSlave kettleHaSlave = kettleHaSlaveService.getById(idSlave);	
		return getAutoView().addObject("kettleHaSlave", kettleHaSlave);
	}
	
}
