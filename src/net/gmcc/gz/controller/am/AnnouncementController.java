package net.gmcc.gz.controller.am;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gmcc.gz.model.am.Announcement;
import net.gmcc.gz.model.am.AnnouncementStatusEnum;
import net.gmcc.gz.service.am.AnnouncementService;
import net.gmcc.gz.service.attach.UploadService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.system.SysUser;

/**
 * <pre>
 * 对象功能:公告管理
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-4 下午4:22:18
 * </pre>
 */
@Controller
@RequestMapping("/gmcc/am/announcement/")
public class AnnouncementController extends BaseController {

	@Resource
	private AnnouncementService announcementService;
	
	@Resource
	private UploadService uploadService;
	
	/**
	 * 添加或更新公告管理表。
	 * @param request
	 * @param response
	 * @param announcement 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新公告管理表")
	public void save(MultipartHttpServletRequest request, HttpServletResponse response,Announcement announcement) throws Exception
	{
		String resultMsg=null;		
		try{
			
			SysUser user = ContextUtil.getCurrentUser();
			if(user == null || StringUtil.isEmpty(user.getAccount())){
				logger.error("current user is not login or user account is empty.");
				return;
			}
			
			if(announcement.getId()==null||announcement.getId()==0){
				announcement.setCreateTime(new Date());
				announcement.setCreatorPortalid(user.getAccount());
				announcement.setCreatorName(user.getFullname());
				//TODO 后续改为调用流程引擎
				announcement.setStatus(AnnouncementStatusEnum.DRAFT.getValue());
				announcementService.insertOne(announcement);
				resultMsg=getText("record.added","公告管理表");
			}else{
				announcement.setEditorPortalid(user.getAccount());
				announcement.setEditorName(user.getFullname());
				announcement.setEditTime(new Date());
			    announcementService.update(announcement);
				resultMsg=getText("record.updated","公告管理表");
			}
			
			List<MultipartFile> file = request.getFiles("file");
			
			//上传附件
			uploadService.upload(file, announcementService.constructAttachment(String.valueOf(announcement.getId()), user.getAccount(), user.getFullname()));
			
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得公告管理表分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看公告管理表分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<String> announcementTypeList = announcementService.getAnnouncementType();
		
		QueryFilter queryFilter = new QueryFilter(request,"announcementItem");
		
		List<Announcement> list=announcementService.getAll(queryFilter);
		ModelAndView mv=this.getAutoView().addObject("announcementList",list)
				.addObject("announcementTypeList", announcementTypeList)
				.addObject("queryFilter", queryFilter);
		
		return mv;
	}
	
	/**
	 * 删除公告管理表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除公告")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			announcementService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除公告管理表成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	@RequestMapping("checkView")
	@Action(description="审批公告页面")
	public ModelAndView checkView(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		Announcement announcement=announcementService.getById(id);
		
		return getAutoView().addObject("announcement",announcement)
				.addObject("returnUrl",returnUrl);
	}
	
	@RequestMapping("check")
	@Action(description="公告审批")
	public void check(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String resultMsg=null;		
		try{
			Long id =RequestUtil.getLong(request, "id", null);
			//TODO 后续改为调用流程引擎
			String status = announcementService.getStatusValue(RequestUtil.getString(request, "status"));
			String checkOpinion = RequestUtil.getString(request, "checkOpinion");
			if(StringUtils.isNotEmpty(status) && StringUtils.isNotEmpty(checkOpinion)){
				announcementService.operateStatusById(id, status, checkOpinion);
				resultMsg=getText("operate.success","公告管理");
			}else{
				resultMsg=getText("operate.failure","公告管理");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception ex){
			resultMsg=getText("operate.failure","公告管理");
			writeResultMessage(response.getWriter(),resultMsg+","+ex.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 公告发布&取消发布
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("publish")
	@Action(description="发布公告")
	public void publish(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			String status = announcementService.getStatusValue(RequestUtil.getString(request, "status"));
			if(StringUtils.isNotEmpty(status)){
				//TODO 后续改为调用流程引擎
				announcementService.operateStatusByIds(lAryId, status);
				message = new ResultMessage(ResultMessage.Success, "操作成功!");
			}else{
				message=new ResultMessage(ResultMessage.Fail, "操作失败!");
			}
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "操作失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑公告管理表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑公告")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		
		List<String> announcementTypeList = announcementService.getAnnouncementType();
		Announcement announcement=announcementService.getById(id);
		
		return getAutoView().addObject("announcement",announcement)
				.addObject("announcementTypeList", announcementTypeList)
				.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得公告管理表明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看公告")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		Announcement announcement = announcementService.getById(id);	
		
		return getAutoView().addObject("announcement", announcement);
	}
	
}

