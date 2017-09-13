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

import com.hotent.gzdc.model.kettle.KettleHaCluster;
import com.hotent.gzdc.service.kettle.KettleHaClusterService;
import com.hotent.core.web.ResultMessage;
/**
 *<pre>
 * 对象功能:KETTLE_HA_CLUSTER 控制器类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-08 16:19:55
 *</pre>
 */
@Controller
@RequestMapping("/gzdc/kettle/kettleHaCluster/")
public class KettleHaClusterController extends BaseController
{
	@Resource
	private KettleHaClusterService kettleHaClusterService;
	
	
	/**
	 * 添加或更新KETTLE_HA_CLUSTER。
	 * @param request
	 * @param response
	 * @param kettleHaCluster 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新KETTLE_HA_CLUSTER")
	public void save(HttpServletRequest request, HttpServletResponse response,KettleHaCluster kettleHaCluster) throws Exception
	{
		String resultMsg=null;		
		try{
			if(kettleHaCluster.getIdCluster()==null||kettleHaCluster.getIdCluster()==0){
				kettleHaCluster.setIdCluster(UniqueIdUtil.genId());
				kettleHaClusterService.add(kettleHaCluster);
				resultMsg=getText("record.added","KETTLE_HA_CLUSTER");
			}else{
			    kettleHaClusterService.update(kettleHaCluster);
				resultMsg=getText("record.updated","KETTLE_HA_CLUSTER");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得KETTLE_HA_CLUSTER分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看KETTLE_HA_CLUSTER分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<KettleHaCluster> list=kettleHaClusterService.getAll(new QueryFilter(request,"kettleHaClusterItem"));
		ModelAndView mv=this.getAutoView().addObject("kettleHaClusterList",list);
		
		return mv;
	}
	
	/**
	 * 删除KETTLE_HA_CLUSTER
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除KETTLE_HA_CLUSTER")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "idCluster");
			kettleHaClusterService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除KETTLE_HA_CLUSTER成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑KETTLE_HA_CLUSTER
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑KETTLE_HA_CLUSTER")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long idCluster=RequestUtil.getLong(request,"idCluster",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		KettleHaCluster kettleHaCluster=kettleHaClusterService.getById(idCluster);
		
		return getAutoView().addObject("kettleHaCluster",kettleHaCluster)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得KETTLE_HA_CLUSTER明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看KETTLE_HA_CLUSTER明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long idCluster=RequestUtil.getLong(request,"idCluster");
		KettleHaCluster kettleHaCluster = kettleHaClusterService.getById(idCluster);	
		return getAutoView().addObject("kettleHaCluster", kettleHaCluster);
	}
	
}
