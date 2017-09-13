package com.hotent.platform.controller.bpm;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.bpm.TaskComment;
import com.hotent.platform.service.bpm.BpmService;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.platform.service.bpm.TaskCommentService;

/**
 * 对象功能:流程任务评论 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:cjj
 * 创建时间:2012-04-05 17:02:32
 */
@Controller
@RequestMapping("/platform/bpm/taskComment/")
public class TaskCommentController extends BaseController
{
	@Resource
	private TaskCommentService taskCommentService;
	@Resource
	private BpmService bpmService;
	@Resource
	private ProcessRunService processRunService;
	
	/**
	 * 取得流程任务评论分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看流程任务评论分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		Long taskId=RequestUtil.getLong(request, "taskId");
		Long runId=RequestUtil.getLong(request, "runId");
		String actDefId=RequestUtil.getString(request, "actDefId");
		
		QueryFilter query = new QueryFilter(request,"taskCommentItem", true);
		query.addFilter("Q_actDefId_S", actDefId);
		query.addFilter("Q_runId_L", runId);
		query.addFilter("Q_taskId_L", taskId);
		List<TaskComment> list=taskCommentService.getAll(query);
		
		ModelAndView mv=this.getAutoView().addObject("taskCommentList",list);
		
		return mv;
	}
	
	/**
	 * 删除流程任务评论
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除流程任务评论")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "commentId");
			taskCommentService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除流程任务评论成功!");
		}
		catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description="编辑流程任务评论")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		String taskId=request.getParameter("taskId");
		
		TaskEntity taskEntity=bpmService.getTask(taskId);
		ProcessRun processRun=processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
		
		// 任务详细信息
		TaskComment taskComment=new TaskComment();
		taskComment.setActDefId(processRun.getActDefId());
		taskComment.setRunId(processRun.getRunId());
		taskComment.setTaskId(new Long(taskId));
		taskComment.setSubject(processRun.getSubject());
		taskComment.setNodeName(taskEntity.getName());
		
		Long runId=RequestUtil.getLong(request, "runId");
		String actDefId=RequestUtil.getString(request, "actDefId");
		
		QueryFilter query = new QueryFilter(request,"taskCommentItem", true);
		query.addFilter("Q_actDefId_S", actDefId);
		query.addFilter("Q_runId_L", runId);
		query.addFilter("Q_taskId_L", taskId);
		List<TaskComment> list=taskCommentService.getAll(query);
		
		return getAutoView().addObject("taskComment",taskComment).addObject("taskCommentList", list);
	}

	/**
	 * 取得流程任务评论明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看流程任务评论明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"commentId");
		TaskComment taskComment = taskCommentService.getById(id);		
		return getAutoView().addObject("taskComment", taskComment);
	}
	
	/**
	 * 添加或更新流程任务评论
	 * @param request
	 * @param response
	 * @param taskComment 添加或更新的实体
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新流程任务评论")
	public void save(HttpServletRequest request, HttpServletResponse response, TaskComment taskComment) throws Exception
	{
		if(taskComment.getCommentId()==null){
			String taskId=request.getParameter("taskId");
			TaskEntity taskEntity=bpmService.getTask(taskId);
			taskCommentService.addTaskComment(taskComment, taskEntity);
		}
		
		PrintWriter writer =  response.getWriter();
		String result = "{\"result\":"+ResultMessage.Success
				+",\"message\":\"保存评论成功\",\"actDefId\":\""+taskComment.getActDefId()+"\""
				+",\"runId\":\""+taskComment.getRunId()+"\""+",\"taskId\":\""+taskComment.getTaskId()+"\""
				+"}";
		writer.print(result);
	}
	
}
