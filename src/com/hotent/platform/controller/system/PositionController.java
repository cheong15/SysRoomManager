package com.hotent.platform.controller.system;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.system.Position;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.model.system.UserPosition;
import com.hotent.platform.service.system.PositionService;
import com.hotent.platform.service.system.UserPositionService;

/**
 * 对象功能:系统岗位表 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
@Controller
@RequestMapping("/platform/system/position/")
@Action(ownermodel=SysAuditModelType.USER_MANAGEMENT)
public class PositionController extends BaseController
{
	@Resource
	private PositionService positionService;
	@Resource
	UserPositionService userPositionService;
	
	/**
	 * 组织对话框的展示
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	public ModelAndView selector(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		QueryFilter filter = new QueryFilter(request, "positionItem", true);
		Long posId = RequestUtil.getLong(request, "pid");
		Position pos = positionService.getById(posId);
		if(pos!=null){
			filter.addFilter("nodePath", pos.getNodePath());
		}
		List<Position> positionList=positionService.getAll(filter);
		if(BeanUtils.isNotEmpty(pos)){
			positionList.add(pos);
		}
		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		ModelAndView mv=this.getAutoView().addObject("positionList",positionList).addObject("isSingle", isSingle);
		return mv;
	}
	
	/**
	 * 取得系统岗位表分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	//@Action(description="查看系统岗位表分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<Position> list=positionService.getAll(new QueryFilter(request,"positionItem"));
		
		long parentId=RequestUtil.getLong(request, "parentId",0);
		Position parent=positionService.getParentPositionByParentId(parentId);
		String Q_nodePath_S=RequestUtil.getString(request, "Q_nodePath_S", "0.");
		
		ModelAndView mv=this.getAutoView()
		.addObject("positionList",list)
		.addObject("Q_nodePath_S", Q_nodePath_S)
		.addObject("parent", parent)
		.addObject("parentId", parentId);
	
		return mv;
	}
	
	/**
	 * 删除系统岗位表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除系统岗位",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除系统岗位"+
					"<#list StringUtils.split(posId,\",\") as item>" +
					"<#assign entity=positionService.getById(Long.valueOf(item))/>" +
					"【${entity.posName}】"+
					"</#list>",
			exectype="管理日志")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage message=null;
		try {
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "posId");
			delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success,"删除系统岗位成功");
		} catch (Exception e) {
			message=new ResultMessage(ResultMessage.Fail,"删除系统岗位失败");
		}
		writeResultMessage(response.getWriter(), message);
	}
	
	private void delByIds(Long[] lAryId){
		if(BeanUtils.isEmpty(lAryId)) return;
		for(Long id:lAryId){
			positionService.delById(id);
			userPositionService.delByPosId(id);
		}
	}

	@RequestMapping("edit")
	@Action(description="添加或编辑系统岗位",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if isAdd>添加系统岗位<#else>编辑系统岗位" +
					"<#assign entity=sysOrgService.getById(Long.valueOf(posId))/>" +
					"【entity.posName】</#if>",
			exectype="管理日志")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		ModelAndView mv=getAutoView();
		Long posId=RequestUtil.getLong(request,"posId");
		String returnUrl=RequestUtil.getString(request, "returnUrl", RequestUtil.getPrePage(request));
		Position position=null;
		boolean isadd=true;
		if(posId!=0){
			 position= positionService.getById(posId);
			 List<UserPosition> userPositionList= userPositionService.getByPosId(posId);
			 mv.addObject("userPositionList", userPositionList);
			 isadd=false;
		}else{
			long parentId=RequestUtil.getLong(request, "parentId",0);
			position=new Position();
			position.setParentId(parentId);
		}
		SysAuditThreadLocalHolder.putParamerter("isAdd", isadd);
		return mv.addObject("position",position).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得系统岗位表明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ModelAndView mv=this.getAutoView();
		return getView(request, response, mv, 0);
	}
	
	/**
	 * 取得系统岗位表明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getByPosId")
	public ModelAndView getByPosId(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ModelAndView mv=new ModelAndView("/platform/system/positionGet.jsp");
		return getView(request, response, mv, 1);
	}
	
	
	
	/**
	 * 取得系统岗位表明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	//@Action(description="查看系统岗位表明细")
	public ModelAndView getView(HttpServletRequest request, HttpServletResponse response,ModelAndView mv ,int isOtherLink) throws Exception
	{
		long id=RequestUtil.getLong(request,"posId");
		Position position = positionService.getById(id);	
		return mv.addObject("position", position).addObject("isOtherLink", isOtherLink);
	}
	
	/**
	 * 取得总岗位表用于显示树层次结构的分类可以分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getChildTreeData")
	//@Action(description="取得总岗位表树形数据")
	@ResponseBody
	public List<Position> getChildTreeData(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		long posId=RequestUtil.getLong(request, "posId",0);
		long parentId=RequestUtil.getLong(request, "parentId",0);
		List<Position> list= new ArrayList<Position>();
		if(parentId==0&&posId==0){
			list=positionService.getAllChildByParentId(posId);
			Position parent=positionService.getParentPositionByParentId(parentId);
			list.add(parent);
		} else {
			list=positionService.getChildByParentId(posId);
		}
		return list;
	}
	
	
	/**
	 * 排序岗位
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("sort")
	@Action(description="系统岗位排序",
			execOrder=ActionExecOrder.AFTER,
			detail="系统岗位排序",
			exectype="管理日志")
	public void sort(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage resultObj=null;
		PrintWriter out = response.getWriter();
		
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "posIds");
		if(lAryId!=null&&lAryId.length>0){
			positionService.updSn(lAryId);
		}
		
		resultObj=new ResultMessage(ResultMessage.Success,"排序岗位完成");
		out.print(resultObj);
		
	}
	
	

	/**
	 * 排序岗位
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("move")
	@Action(description="转移系统岗位",
			execOrder=ActionExecOrder.AFTER,
			detail="转移系统岗位",
			exectype="管理日志")
	public void move(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage resultObj=null;
		PrintWriter out = response.getWriter();
		long targetId=RequestUtil.getLong(request, "targetId",0);
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "originalIds");
		if(lAryId!=null&&lAryId.length>0){
			//目标节点
			Position target=null;
			if(targetId!=0){
				target=positionService.getById(targetId);
			}else{
				target=positionService.getParentPositionByParentId(0);
			}
			for(int i=0;i<lAryId.length;i++){
				
				Position orgPo=positionService.getById(lAryId[i]);
				
				if(orgPo!=null){
					//取得旧路径
					String oldNodePath=orgPo.getNodePath();
					//取得所有节点
					List<Position> childrenList=positionService.getByNodePath(oldNodePath);
					//取得旧父亲
					long oldParentId=orgPo.getParentId();
					Position parent=positionService.getById(oldParentId);
					
					//设置新父亲
					orgPo.setParentId(target.getPosId());
					orgPo.setNodePath(target.getNodePath()+orgPo.getPosId()+".");
					orgPo.setDepth(target.getDepth()+1);
					positionService.update(orgPo);
					//更新所有孩子们的路径nodePath
					positionService.updateChildrenNodePath(orgPo,childrenList);
					//维护旧父亲的isParent辽段
					if(parent!=null)
					positionService.updateIsParent(parent);
				}
			}
		}
		
		resultObj=new ResultMessage(ResultMessage.Success,"转动岗位完成");
		out.print(resultObj);
		
	}
	
	
	/**
	 * 获取岗位树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getTreeData")
	@ResponseBody
	public List<Position> getTreeData(HttpServletRequest request,HttpServletResponse response) throws Exception{	
		List<Position> posList=positionService.getAll();		
		Position pos=new Position();
		pos.setPosId(new Long(0));
		pos.setPosName("全部");
		pos.setPosDesc("岗位");
		pos.setParentId(new Long(-1));
		pos.setNodePath("0.");
		posList.add(pos);
		return posList;
						
	}

}
