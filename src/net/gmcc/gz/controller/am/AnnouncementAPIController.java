package net.gmcc.gz.controller.am;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gmcc.gz.base.json.RequestBodyExt;
import net.gmcc.gz.model.am.Announcement;
import net.gmcc.gz.model.am.AnnouncementList;
import net.gmcc.gz.model.attach.Attachment;
import net.gmcc.gz.model.attach.AttachmentBizTypeEnum;
import net.gmcc.gz.service.am.AnnouncementService;
import net.gmcc.gz.service.attach.AttachmentService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotent.core.annotion.Action;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;

/**
 * <pre>
 * 对象功能:公告管理
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-4 下午4:22:18
 * </pre>
 */
@Controller
@RequestMapping("/front/announcement/")
public class AnnouncementAPIController extends BaseController {

	@Resource
	private AnnouncementService announcementService;
	
	@Resource
	private AttachmentService attachmentService;
	
	/**
	 * 返回json接口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("list")
	@Action(description="查看公告管理表分页列表")
	public Object list(HttpServletRequest request,HttpServletResponse response, @RequestBodyExt(AnnouncementList.class) AnnouncementList announcementList) throws Exception {
		List<Announcement> list = announcementService.getList(new QueryFilter(request, announcementList));
		return announcementService.convert(list);
	}
	
	/**
	 * 取得公告管理表明细
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("{id}")
	@Action(description="查看公告管理表明细")
	public Object get(@PathVariable("id") Long id, @RequestBodyExt(Announcement.class) Announcement announcement) throws Exception
	{
		announcement = announcementService.getById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id",String.valueOf(id));
		map.put("businessType", AttachmentBizTypeEnum.ATTACHMENT.name());
		List<Attachment> attachmentList = attachmentService.getAllAttachment(map);
		announcement.setAttachmentList(attachmentList);
		return announcement;
	}
}
