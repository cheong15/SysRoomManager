package com.hotent.gzdc.controller.kettle;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.gzdc.model.kettle.KettleRepository;
import com.hotent.gzdc.model.kettle.RepositoryJson;
import com.hotent.gzdc.service.kettle.KettleRepositoryService;
/**
 *<pre>
 * 对象功能:KETTLE_REPOSITORY 控制器类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-10 09:31:32
 *</pre>
 */
@Controller
@RequestMapping("/gzdc/kettle/kettleRepository/")
public class KettleRepositoryController extends BaseController
{
	@Resource
	private KettleRepositoryService kettleRepositoryService;
	
	
	/**
	 * 添加或更新KETTLE_REPOSITORY。
	 * @param request
	 * @param response
	 * @param kettleRepository 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新KETTLE_REPOSITORY")
	public void save(HttpServletRequest request, HttpServletResponse response,KettleRepository kettleRepository) throws Exception
	{
		String resultMsg=null;		
		try{
			if(kettleRepository.getCRepositoryId()==null||kettleRepository.getCRepositoryId()==0){
				kettleRepository.setCRepositoryId(UniqueIdUtil.genId());
				kettleRepositoryService.createRepository(kettleRepository, false);
				kettleRepositoryService.add(kettleRepository);
				resultMsg=getText("record.added","资源库");
			}else{
				kettleRepositoryService.createRepository(kettleRepository, true);
			    kettleRepositoryService.update(kettleRepository);
				resultMsg=getText("record.updated","资源库");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			e.printStackTrace();
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得KETTLE_REPOSITORY分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看KETTLE_REPOSITORY分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<KettleRepository> list=kettleRepositoryService.getAll(new QueryFilter(request,"kettleRepositoryItem"));
		ModelAndView mv=this.getAutoView().addObject("kettleRepositoryList",list);
		
		return mv;
	}
	
	/**
	 * 删除KETTLE_REPOSITORY
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除KETTLE_REPOSITORY")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "CRepositoryId");
			kettleRepositoryService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除资源库成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑KETTLE_REPOSITORY
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑KETTLE_REPOSITORY")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long CRepositoryId=RequestUtil.getLong(request,"CRepositoryId",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		KettleRepository kettleRepository=kettleRepositoryService.getById(CRepositoryId);
		
		return getAutoView().addObject("kettleRepository",kettleRepository)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得KETTLE_REPOSITORY明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看KETTLE_REPOSITORY明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long CRepositoryId=RequestUtil.getLong(request,"CRepositoryId");
		KettleRepository kettleRepository = kettleRepositoryService.getById(CRepositoryId);	
		return getAutoView().addObject("kettleRepository", kettleRepository);
	}
	
	/**
	 * <p>Description: <p>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tree")
	@Action(description="资源对应的文件树")
	public ModelAndView tree(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String CRepositoryName=RequestUtil.getString(request,"CRepositoryName",null);
		String returnUrl=RequestUtil.getPrePage(request);
		
		return getAutoView().addObject("CRepositoryName", CRepositoryName)
							.addObject("returnUrl",returnUrl);
	}
	
	/**
	 * <p>Description: <p>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getRepositoryTreeData")
	@ResponseBody
	public List<RepositoryJson> getRepositoryTreeData(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String CRepositoryName=RequestUtil.getString(request,"CRepositoryName",null);
		String jsonTree = kettleRepositoryService.getRepTreeJSON(CRepositoryName, String.valueOf(ContextUtil.getCurrentUserId()));
		JSONArray jsonArray = JSONArray.fromObject(jsonTree);
		List<RepositoryJson> list = (List) JSONArray.toCollection(jsonArray, RepositoryJson.class);
		return list;
	}

}
