package com.hotent.gzdc.controller.kettle;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.engine.MessageEngine;
import com.hotent.core.scheduler.SchedulerService;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.gzdc.model.kettle.KettleRepository;
import com.hotent.gzdc.model.kettle.QrtzTriggers;
import com.hotent.gzdc.service.kettle.KettleRepositoryService;
import com.hotent.gzdc.service.kettle.QrtzTriggersService;
import com.hotent.platform.job.KettleJob;
import com.hotent.platform.service.system.SysJobLogService;
/**
 *<pre>
 * 对象功能:QRTZ_TRIGGERS 控制器类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-07 17:37:45
 *</pre>
 */
@Controller
@RequestMapping("/gzdc/kettle/qrtzTriggers/")
public class QrtzTriggersController extends BaseController
{
	public static String KETTLE_GROUP_NAME = "KETTLE";
	private String KETTLE_JOB_CLASS = KettleJob.class.getName();
	@Resource
	private QrtzTriggersService qrtzTriggersService;
	
	@Resource
	SchedulerService schedulerService;
	
	@Resource
	SysJobLogService sysJobLogService;
	
	@Resource
	private KettleRepositoryService kditRepositoryService;
	
	
	/**
	 * 添加或更新QRTZ_TRIGGERS。
	 * @param request
	 * @param response
	 * @param qrtzTriggers 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@RequestMapping("save")
	@Action(description="添加或更新QRTZ_TRIGGERS")
	public void save(HttpServletRequest request, HttpServletResponse response,QrtzTriggers qrtzTriggers) throws Exception
	{
		String resultMsg=null;
		boolean isNew = true;
		try{
			String jobName = qrtzTriggers.getJobName();
			String triggerName = qrtzTriggers.getTriggerName();
			if (StringUtils.isNotEmpty(triggerName)) {
				isNew = false;
			}
			triggerName = jobName;
			Map jobDetailmap = new HashMap();
			qrtzTriggers.setTriggerName(jobName);
			qrtzTriggers.setJobGroup(KETTLE_GROUP_NAME);
			qrtzTriggers.setTriggerGroup(KETTLE_GROUP_NAME);
			qrtzTriggers.setNextFireTime(qrtzTriggers.getStartDate() + " " + qrtzTriggers.getStartTime());
			
			int cycle = qrtzTriggers.getCycle();
			String cycleNum = qrtzTriggers.getCycleNum();
			String haveEndDate = qrtzTriggers.getHaveEndDate();//是否永不结束0:永不结束;1:通过结束时间来确定结束
			String endDate = qrtzTriggers.getEndDate();//结束时间
			String startDate = qrtzTriggers.getStartDate();//开始时间
			String startTime = qrtzTriggers.getStartTime();//开始时间
			String dayType = qrtzTriggers.getDayType();//天数类型
			String monthType = qrtzTriggers.getMonthType();
	        String weekNum = qrtzTriggers.getWeekNum();
	        String dayNum = qrtzTriggers.getDayNum();
	        String yearType = qrtzTriggers.getYearType();
	        String monthNum = qrtzTriggers.getMonthNum();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(StringUtil.StringToDate(startDate + " " + startTime, "yyyy-MM-dd HH:mm:ss"));
			String cronString = "";
			String withDescription = "";
			ScheduleBuilder scheduleBuilder = null;
			TriggerBuilder<Trigger> triggerBuild=  TriggerBuilder.newTrigger();
			triggerBuild.startAt(calendar.getTime());//开始执行时间
			//周期
			switch (cycle) {
				case 1 ://执行一次
					qrtzTriggers.setRepeatCount(0);
					long repeatInterval = StringUtil.StringToDate(startDate + " " + startTime, "yyyy-MM-dd HH:mm:ss").getTime() - new Date().getTime();
					if (repeatInterval > 0L){
						qrtzTriggers.setRepeatInterval(repeatInterval);
					}else {
						qrtzTriggers.setRepeatInterval(-repeatInterval);
					}
					qrtzTriggers.setEndDate(null);
					withDescription = "执行一次, 执行时间:" + startDate + " " + startTime;
					break;
				case 2 ://秒
					qrtzTriggers.setRepeatInterval(Long.parseLong(cycleNum) * 1000L);
					qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
					
					scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(Integer.parseInt(cycleNum));
					withDescription = "每隔" + cycleNum + " 秒执行一次 ";
					if ("1".equals(haveEndDate)) {
						qrtzTriggers.setEndDate(endDate);
					}
					break;
				case 3 ://分钟
					qrtzTriggers.setRepeatInterval(Long.parseLong(cycleNum) * 60L * 1000L);
					qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
					
					scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(Integer.parseInt(cycleNum));
					withDescription = "每隔" + cycleNum + " 分钟执行一次 ";
					if ("1".equals(haveEndDate)) {
						qrtzTriggers.setEndDate(endDate);
					}
					break;
				case 4 ://小时
					qrtzTriggers.setRepeatInterval(Long.parseLong(cycleNum) * 60L * 60L * 1000L);
					qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
					
					scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInHours(Integer.parseInt(cycleNum));
					withDescription = "每隔" + cycleNum + " 小时执行一次 ";
					if ("1".equals(haveEndDate)) {
						qrtzTriggers.setEndDate(endDate);
					}
					break;
				case 5 ://日
					qrtzTriggers.setDayType(dayType);
					if ("0".equals(dayType)) {
						qrtzTriggers.setRepeatInterval(Long.parseLong(dayNum) * 24L * 60L * 60L * 1000L);
						scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInDays(Integer.parseInt(dayNum));
						withDescription = "每隔" + dayNum + " 天执行一次 ";
					} else if ("1".equals(dayType)) {
						cronString = calendar.get(13) + " " + calendar.get(12) + " " + calendar.get(11) + " ? * MON-FRI";
						qrtzTriggers.setCronString(cronString);
					}
					qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
					if ("1".equals(haveEndDate)) {
						qrtzTriggers.setEndDate(endDate);
					}
					break;
				case 6 ://周
					qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);

					cronString = calendar.get(13) + " " + calendar.get(12) + " " + calendar.get(11) + " ? * " + cycleNum;
					qrtzTriggers.setCronString(cronString);
					if ("1".equals(haveEndDate)) {
						qrtzTriggers.setEndDate(endDate);
					}
					break;
				case 7 ://月
				      if ("0".equals(monthType)) {
				        cronString = calendar.get(13) + " " + calendar.get(12) +  " " + calendar.get(11) + " " + cycleNum + " * ?";
				      } else if ("1".equals(monthType)) {
				        if (!"L".equals(weekNum)) {
				          weekNum = "#" + weekNum;
				        }
				        cronString = calendar.get(13) + " " + calendar.get(12) + " " + calendar.get(11) + " ? * " + dayNum + weekNum;
				      }
				      qrtzTriggers.setCronString(cronString);
				      qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
				      if ("1".equals(haveEndDate)) {
				    	  qrtzTriggers.setEndDate(endDate);
				      }
					break;
				case 8 ://年
					if ("0".equals(yearType)) {
						String[] monthAndDay = cycleNum.split("-");
						cronString = calendar.get(13) + " " + calendar.get(12) + " " + calendar.get(11) + " " + monthAndDay[1] + " " + monthAndDay[0] + " ?";
					} else if ("1".equals(yearType)) {
						if (!"L".equals(weekNum)) {
							weekNum = "#" + weekNum;
						}
						cronString = calendar.get(13) + " " + calendar.get(12) + " " + calendar.get(11) + " ? " + monthNum + " " + dayNum + weekNum;
					}
					qrtzTriggers.setCronString(cronString);
					qrtzTriggers.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
					if ("1".equals(haveEndDate)) {
						qrtzTriggers.setEndDate(endDate);
					}
					break;
			}
			
			//继续处理trigger
			if ("1".equals(haveEndDate)) {
				triggerBuild.endAt(StringUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
				withDescription += "　于" + endDate + " 23:59:59结束";
			}
			//查看描述是否为空　如果不为空设置到triggerBuild中
			if (StringUtils.isNotEmpty(withDescription)) {
				triggerBuild.withDescription(withDescription);
			}
			
			//scheduleBuilder不为空时
			if (scheduleBuilder != null) {
				triggerBuild.withSchedule(scheduleBuilder);
			}
			
			if (StringUtils.isNotEmpty(cronString)) {
				scheduleBuilder = CronScheduleBuilder.cronSchedule(cronString);
				triggerBuild.withSchedule(scheduleBuilder);
			}
			
			
			jobDetailmap.put("actionRef", qrtzTriggers.getActionRef());
		    jobDetailmap.put("actionPath", qrtzTriggers.getActionPath());
		    jobDetailmap.put("repeat-time-millisecs", qrtzTriggers.getRepeatInterval());
		    jobDetailmap.put("joGroup", qrtzTriggers.getJobGroup());
		    jobDetailmap.put("repeat-count", qrtzTriggers.getRepeatCount());
		    jobDetailmap.put("start-date-time", qrtzTriggers.getStartDate());
		    jobDetailmap.put("requestedMimeType", "text/xml");
		    jobDetailmap.put("description", qrtzTriggers.getDescription());
		    jobDetailmap.put("jobName", qrtzTriggers.getJobName());
		    jobDetailmap.put("version", qrtzTriggers.getVersion());
		    jobDetailmap.put("fileType", qrtzTriggers.getFileType());
		    jobDetailmap.put("repName", qrtzTriggers.getRepName());
		    jobDetailmap.put("startTime", qrtzTriggers.getStartTime());
		    jobDetailmap.put("startDate", qrtzTriggers.getStartDate());
		    jobDetailmap.put("haveEndDate", qrtzTriggers.getHaveEndDate());
		    jobDetailmap.put("endDate", qrtzTriggers.getEndDate());
		    jobDetailmap.put("cycle", qrtzTriggers.getCycle());
		    jobDetailmap.put("cycleNum", qrtzTriggers.getCycleNum());
		    jobDetailmap.put("dayType", qrtzTriggers.getDayType());
		    jobDetailmap.put("monthType", qrtzTriggers.getMonthType());
		    jobDetailmap.put("yearType", qrtzTriggers.getYearType());
		    jobDetailmap.put("dayNum", qrtzTriggers.getDayNum());
		    jobDetailmap.put("weekNum", qrtzTriggers.getWeekNum());
		    jobDetailmap.put("monthNum", qrtzTriggers.getMonthNum());
		    jobDetailmap.put("userId", qrtzTriggers.getUserId());
		    jobDetailmap.put("execType", qrtzTriggers.getExecType());
		    jobDetailmap.put("remoteServer", qrtzTriggers.getRemoteServer());
		    jobDetailmap.put("ha", qrtzTriggers.getHa());
		    jobDetailmap.put("errorNoticeUserId", qrtzTriggers.getErrorNoticeUserId());
		    jobDetailmap.put("errorNoticeUserName", qrtzTriggers.getErrorNoticeUserName());

		    jobDetailmap.put("background_action_name", "");
		    jobDetailmap.put("processId", QrtzTriggersController.class.getName());
		    jobDetailmap.put("background_user_name", "");
		    jobDetailmap.put("background_output_location", "background/" + StringUtil.createNumberString(16));
		    jobDetailmap.put("background_submit_time", StringUtil.DateToString(new Date(), "yyyy-MM-dd"));

		    jobDetailmap.put("backgroundExecution", "true");

		    if (isNew) {
		    	schedulerService.addJob(jobName, KETTLE_JOB_CLASS, jobDetailmap, qrtzTriggers.getDescription(), KETTLE_GROUP_NAME);
		    	resultMsg=getText("record.added","调度");
			}else{
				schedulerService.delJob(jobName, KETTLE_GROUP_NAME);
				schedulerService.addJob(jobName, KETTLE_JOB_CLASS, jobDetailmap, qrtzTriggers.getDescription(), KETTLE_GROUP_NAME);
				resultMsg=getText("record.updated","调度");
			}
		    
		    //添加计划任务
		    schedulerService.addTrigger(jobName, KETTLE_GROUP_NAME, triggerName, triggerBuild);
			
		    writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得QRTZ_TRIGGERS分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看QRTZ_TRIGGERS分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<QrtzTriggers> qrtzTriggerslList = schedulerService.getJobLisyByGroupName(KETTLE_GROUP_NAME);
		ModelAndView mv=new ModelAndView();
		mv.setViewName("/gzdc/kettle/qrtzTriggersList.jsp");
		mv.addObject("qrtzTriggersList", qrtzTriggerslList);
		return mv;
	}
	
	/**
	 * 删除QRTZ_TRIGGERS
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除QRTZ_TRIGGERS")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			String[] jobNames =RequestUtil.getStringAryByStr(request, "jobName");
			if (jobNames != null && jobNames.length > 0) {
				for (String jobName : jobNames) {
					schedulerService.delJob(jobName, KETTLE_GROUP_NAME);
				}
			}
			message=new ResultMessage(ResultMessage.Success, "删除调度成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * <p>Description: 暂停调度<p>
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("pause")
	@Action(description="暂定QRTZ_TRIGGERS")
	public void pause(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			String[] jobNames =RequestUtil.getStringAryByStr(request, "jobName");
			if (jobNames != null && jobNames.length > 0) {
				for (String jobName : jobNames) {
					schedulerService.pauseJob(jobName, KETTLE_GROUP_NAME);
				}
			}
			message=new ResultMessage(ResultMessage.Success, "暂停调度成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "暂停调度失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * <p>Description: 恢复调度<p>
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("resume")
	@Action(description="恢复QRTZ_TRIGGERS")
	public void resume(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			String[] jobNames =RequestUtil.getStringAryByStr(request, "jobName");
			if (jobNames != null && jobNames.length > 0) {
				for (String jobName : jobNames) {
					schedulerService.resumeJob(jobName, KETTLE_GROUP_NAME);
				}
			}
			message=new ResultMessage(ResultMessage.Success, "恢复调度成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "恢复调度失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	
	/**
	 * <p>Description: 运行调度<p>
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("run")
	@Action(description="动行QRTZ_TRIGGERS")
	public void run(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			String[] jobNames =RequestUtil.getStringAryByStr(request, "jobName");
			if (jobNames != null && jobNames.length > 0) {
				for (String jobName : jobNames) {
					schedulerService.executeJob(jobName, KETTLE_GROUP_NAME);
				}
			}
			message=new ResultMessage(ResultMessage.Success, "运行调度成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "动行失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑QRTZ_TRIGGERS
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑QRTZ_TRIGGERS")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		String jobName=RequestUtil.getString(request,"jobName",null);
		
		QrtzTriggers qrtzTriggers = null;
		if (StringUtils.isNotEmpty(jobName)) {
			//qrtzTriggers = qrtzTriggersService.getById(triggerName);
			JobDetail jobDetail = schedulerService.getJobDetailByJobNameAndGroupName(jobName, KETTLE_GROUP_NAME);
			if (jobDetail != null) {
				qrtzTriggers = new QrtzTriggers();
				JobDataMap dataMap = jobDetail.getJobDataMap();
				qrtzTriggers.setJobName(jobName);
				qrtzTriggers.setTriggerName(jobName);
				qrtzTriggers.setTriggerGroup(KETTLE_GROUP_NAME);
				qrtzTriggers.setJobGroup(KETTLE_GROUP_NAME);
				qrtzTriggers.setActionPath(dataMap.getString("actionPath"));
				qrtzTriggers.setActionRef(dataMap.getString("actionRef"));
				qrtzTriggers.setCronString(dataMap.getString("cronString"));
				qrtzTriggers.setCycle(dataMap.get("cycle") == null
						? 0
						: dataMap.getInt("cycle"));
				qrtzTriggers.setCycleNum(dataMap.getString("cycleNum"));
				qrtzTriggers.setDayNum(dataMap.getString("dayNum"));
				qrtzTriggers.setDayType(dataMap.getString("dayType"));
				qrtzTriggers.setDescription(dataMap.getString("description"));
				qrtzTriggers.setEndDate(dataMap.getString("endDate"));//结束日期
				qrtzTriggers.setFileType(dataMap.getString("fileType"));
				qrtzTriggers.setHaveEndDate(dataMap.getString("haveEndDate"));
				qrtzTriggers.setMonthNum(dataMap.getString("monthNum"));
				qrtzTriggers.setMonthType(dataMap.getString("monthType"));
				qrtzTriggers.setNextFireTime(dataMap.getString("nextFireTime"));
				qrtzTriggers.setPrevFireTime(dataMap.getString("prevFireTime"));
				qrtzTriggers.setRepeatCount(dataMap.get("repeatCount") == null
						? 0
						: dataMap.getInt("repeatCount"));
				qrtzTriggers
						.setRepeatInterval(dataMap.get("repeatInterval") == null
								? 0L
								: dataMap.getLong("repeatInterval"));
				qrtzTriggers.setRepName(dataMap.getString("repName"));
				qrtzTriggers.setStartDate(dataMap.getString("startDate"));//开始日期
				qrtzTriggers.setStartTime(dataMap.getString("startTime"));//开始时间
				qrtzTriggers
						.setTriggerState(dataMap.get("triggerState") == null
								? 0
								: dataMap.getInt("triggerState"));
				qrtzTriggers.setVersion(dataMap.getString("version"));
				qrtzTriggers.setWeekNum(dataMap.getString("weekNum"));
				qrtzTriggers.setYearType(dataMap.getString("yearType"));
				qrtzTriggers.setUserId(dataMap.getString("userId"));
				qrtzTriggers.setExecType(dataMap.getString("execType"));
				qrtzTriggers.setRemoteServer(dataMap.getString("remoteServer"));
				qrtzTriggers.setHa(dataMap.getString("ha"));
			}
		}
		
		ModelAndView mv=new ModelAndView();
		mv.setViewName("/gzdc/kettle/qrtzTriggersEdit.jsp");
		mv.addObject("qrtzTriggersEdit", qrtzTriggers);
		List<KettleRepository> repositoryList = kditRepositoryService.getAll();
		mv.addObject("repositoryList", repositoryList);
		return mv;
	}

	/**
	 * 取得QRTZ_TRIGGERS明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看QRTZ_TRIGGERS明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long triggerName=RequestUtil.getLong(request,"triggerName");
		QrtzTriggers qrtzTriggers = qrtzTriggersService.getById(triggerName);	
		return getAutoView().addObject("qrtzTriggers", qrtzTriggers);
	}
	
	@RequestMapping("viewSourceImage")
	@Action(description="查看当前调度配置原图")
	public ModelAndView viewSourceImage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String jobName=RequestUtil.getString(request,"jobName",null);
		JobDetail jobDetail = schedulerService.getJobDetailByJobNameAndGroupName(jobName, KETTLE_GROUP_NAME);
		//初始化必要信息
		QrtzTriggers qrtzTriggers = jobDetail2QrtzTriggers(jobDetail);
		StringBuffer dataXML = new StringBuffer();
		
		String tempXML = kditRepositoryService.getDataXML(qrtzTriggers.getRepName(), qrtzTriggers.getFileType(), qrtzTriggers.getActionPath(), qrtzTriggers.getActionRef());
		
		dataXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<config>")
				.append("<container height=\"100%\" id=\"Scroller0\" type=\"spark.components.Scroller\" width=\"100%\">")
				.append("<container height=\"100%\" id=\"Vgroup0\" paddingBottom=\"5\" paddingLeft=\"5\" paddingRight=\"5\" paddingTop=\"5\" type=\"spark.components.VGroup\" width=\"100%\">")
				.append("<container height=\"100%\" id=\"Group0\" type=\"spark.components.Group\" width=\"100%\">")
				.append("<container id=\"etl\" height=\"100%\" type=\"bi.etl.ETL\" width=\"100%\" x=\"0\" y=\"0\">")
				.append("<etl type=\"kettle\">")
				.append("<dataSource type=\"data\">").append("<data>");
		dataXML.append(tempXML);
		
		dataXML.append("</data>").append("<url/>").append("</dataSource>")
				.append("</etl>").append("</container>").append("</container>")
				.append("</container>").append("</container>")
				.append("</config>");
		
		qrtzTriggers.setDataXML(dataXML.toString());
		
		return getAutoView().addObject("qrtzTriggers", qrtzTriggers)
				.addObject("detailXML", dataXML.toString())
				.addObject("jobName", qrtzTriggers.getJobName())
				.addObject("actionRef", qrtzTriggers.getActionRef());
	}
	
	private QrtzTriggers jobDetail2QrtzTriggers(JobDetail jobDetail){
		QrtzTriggers qrtzTriggers = null;
		if (jobDetail != null) {
			qrtzTriggers = new QrtzTriggers();
			JobKey jobKey = jobDetail.getKey();
			qrtzTriggers.setJobName(jobKey.getName());
			qrtzTriggers.setJobGroup(jobKey.getGroup());
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			if (jobDataMap != null) {
				qrtzTriggers.setActionPath(jobDataMap.getString("actionPath"));
		        qrtzTriggers.setActionRef(jobDataMap.getString("actionRef"));
		        qrtzTriggers.setFileType(jobDataMap.getString("fileType"));
		        qrtzTriggers.setVersion(jobDataMap.getString("version"));
		        qrtzTriggers.setRepName(jobDataMap.getString("repName"));
		        qrtzTriggers.setDescription(jobDetail.getDescription());
			}
		}
		return qrtzTriggers;
	}
	
	
	@RequestMapping("activeDetails")
	@Action(description="获取图的节点信息")
	public void activeDetails(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String jobName=RequestUtil.getString(request,"jobName",null);
		JobDetail jobDetail = schedulerService.getJobDetailByJobNameAndGroupName(jobName, KETTLE_GROUP_NAME);
		//初始化必要信息
		QrtzTriggers qrtzTriggers = jobDetail2QrtzTriggers(jobDetail);
		String status = kditRepositoryService.getActiveDetails(qrtzTriggers.getRepName(), qrtzTriggers.getFileType(), qrtzTriggers.getActionPath(), qrtzTriggers.getActionRef());
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(status);
		response.getWriter().close();
	}
	
	
	
}
