package com.hotent.platform.job;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.activiti.engine.RuntimeService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.bpm.model.ProcessTask;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.core.jms.MessageProducer;
import com.hotent.core.model.InnerMessage;
import com.hotent.core.model.MailModel;
import com.hotent.core.model.SmsMobile;
import com.hotent.core.scheduler.BaseJob;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.TimeUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.bpm.ReminderStateDao;
import com.hotent.platform.dao.bpm.TaskDao;
import com.hotent.platform.dao.bpm.TaskReminderDao;
import com.hotent.platform.dao.system.SysUserDao;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.bpm.ReminderState;
import com.hotent.platform.model.bpm.TaskReminder;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.bpm.IBpmActService;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.platform.service.bpm.TaskUserService;
import com.hotent.platform.service.bpm.impl.BpmActService;
import com.hotent.platform.service.worktime.CalendarAssignService;

/**
 * 催办任务。
 *
 * @author ray
 *
 */
public class ReminderJob extends BaseJob {
    // 流程提醒
    private static final int Remind = 1;
    // 执行到期处理动作
    private static final int Complete = 2;
    private Log logger = LogFactory.getLog(ReminderJob.class);

    @Override
    public void executeJob(JobExecutionContext context) throws Exception {
        TaskReminderDao taskReminderDao = (TaskReminderDao) AppUtil.getBean(TaskReminderDao.class);
        ReminderStateDao reminderStateDao = (ReminderStateDao) AppUtil.getBean(ReminderStateDao.class);
        GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil.getBean(GroovyScriptEngine.class);
        RuntimeService runtimeService = (RuntimeService) AppUtil.getBean(RuntimeService.class);
        //清除任务状态数据。
        reminderStateDao.delExpiredTaskReminderState();
        TaskDao taskDao = (TaskDao) AppUtil.getBean(TaskDao.class);
        // 获取未到期的任务
        List<ProcessTask> list = taskDao.getReminderTask();
        if (list.size() == 0) {
            logger.debug("没有获取到任务!");
            return;
        }
        for (ProcessTask task : list) {
            // 取得流程定义ID
            String actDefId = task.getProcessDefinitionId();
            // 取得节点ID
            String nodeId = task.getTaskDefinitionKey();
            // 获取提醒对象
            List<TaskReminder> taskReminders = taskReminderDao.getByActDefAndNodeId(actDefId, nodeId);
            String executionId = task.getExecutionId();
            Map<String,Object> vars = runtimeService.getVariables(executionId);
            for(TaskReminder taskReminder:taskReminders){
                String conditionExp=taskReminder.getCondExp();
                //条件表达式不为空
                if(StringUtil.isNotEmpty(conditionExp)){
                    boolean  result= groovyScriptEngine.executeBoolean(conditionExp, vars);
                    if(!result){
                        logger.debug("Skip TaskReminder :"+taskReminder.getTaskDueId()+","+taskReminder.getName());
                        continue;
                    }
                }
                logger.debug("Execute TaskReminder :"+taskReminder.getTaskDueId()+","+taskReminder.getName());
                // 获取用户数据
                Set<Long> userSet = getUserByTask(task);
                //提醒次数如果为0则表示不发送消息
                if(taskReminder.getTimes()>0){
                    // 处理提醒
                    handReminder(task, taskReminder, userSet, vars);
                }
                // 到期处理
                handlerDueTask(task, taskReminder, userSet);
            }
        }
    }

    /**
     * 处理消息报警
     *
     * @param task
     * @param taskReminder
     * @param userSet
     * @param vars
     * @throws Exception
     */
    private void handReminder(ProcessTask task, TaskReminder taskReminder, Set<Long> userSet, Map<String, Object> vars) throws Exception {
        ReminderStateDao reminderStateDao = (ReminderStateDao) AppUtil.getBean(ReminderStateDao.class);
        CalendarAssignService calendarAssignService = (CalendarAssignService) AppUtil.getBean(CalendarAssignService.class);
        String taskId = task.getId();
        // 需要提醒的次数。
        int needRemindTimes = taskReminder.getTimes();
        // 离任务起始时间的分钟数
        int reminderStart = taskReminder.getReminderStart();
        // 任务距离触发时间的分钟数
        int interval = taskReminder.getReminderEnd();
        // 该节点的剩余时间
        String completeTime = taskReminder.getCompleteTime() + "分钟";
        String actDefId = task.getProcessDefinitionId();
        String actInstanceId = task.getProcessInstanceId();
        // 任务开始时间
        Date startTime = calendarAssignService.getRelativeStartTime(actInstanceId, taskReminder.getRelativeNodeId(), taskReminder.getRelativeNodeType());

        //未获取到相对节点的创建或者完成时间
        if(startTime==null){
            return;
        }

        for (Long userId : userSet) {
            // 已催办的次数
            int reminderTimes = reminderStateDao.getAmountByUserTaskId(Long.parseLong(taskId), userId, Remind);
            // 已达到指定的催办次数。
            if (reminderTimes >= needRemindTimes)
                continue;
            // 计算距离开始时间。
            int start = reminderStart + interval * reminderTimes;
            Date dueDate = null;
            //日历日
            if(new Integer(1).equals(taskReminder.getRelativeTimeType())){
                dueDate = new Date(TimeUtil.getNextTime(TimeUtil.MINUTE, start, startTime.getTime()));
            }
            //工作日
            else{
                dueDate = calendarAssignService.getTaskTimeByUser(startTime, start, userId);
            }
            Date curDate = new Date();
            // 催办时间未到
            if (dueDate.compareTo(curDate) > 0)
                continue;
            Date completeDate = null;
            //日历日
            if(new Integer(1).equals(taskReminder.getRelativeTimeType())){
                completeDate = new Date(TimeUtil.getNextTime(TimeUtil.MINUTE, taskReminder.getCompleteTime(), startTime.getTime()));
            }
            //工作日
            else{
                //任务已过期
                completeDate = calendarAssignService.getTaskTimeByUser(startTime, taskReminder.getCompleteTime(), userId);
            }
            if (completeDate.compareTo(curDate) < 0){
                continue;
            }
            // 发送催办消息
            sendMsg(userId, taskReminder, task, completeTime,vars);
            // 记录提醒次数
            saveReminderState(taskId, actDefId, actInstanceId, userId, Remind);
        }
    }

    private void sendMsg(Long userId, TaskReminder taskReminder, ProcessTask task, String time, Map<String, Object> vars) {
        MessageProducer messageProducer = (MessageProducer) AppUtil.getBean(MessageProducer.class);
        ProcessRunService processRunService = (ProcessRunService) AppUtil.getBean(ProcessRunService.class);
        SysUserDao sysUserDao = (SysUserDao) AppUtil.getBean(SysUserDao.class);
        Properties configproperties = (Properties) AppUtil.getBean("configproperties");
        SysUser sysUser = sysUserDao.getById(userId);
        String msgTemplate = taskReminder.getMsgContent();
        String mailTemplate = taskReminder.getMailContent();
        String mobileTemplate = taskReminder.getSmsContent();
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(task.getProcessInstanceId()));
        String subject = processRun.getSubject();
        String url = configproperties.get("serverUrl") + "/platform/bpm/task/toStart.ht?taskId=" + task.getId();
        if (StringUtil.isNotEmpty(mailTemplate)) {
            MailModel mailModel = getMail(sysUser, mailTemplate, subject, url, time,vars);
            if (mailModel != null)
                messageProducer.send(mailModel);
        }

        if (StringUtil.isNotEmpty(msgTemplate)) {
            InnerMessage msgModel = getMsg(sysUser, msgTemplate, subject, url, time,vars);
            if (msgModel != null)
                messageProducer.send(msgModel);
        }

        if (StringUtil.isNotEmpty(mobileTemplate)) {
            SmsMobile smsMobile = getSms(sysUser, mobileTemplate, subject, time,vars);
            if (smsMobile != null)
                messageProducer.send(smsMobile);
        }

    }

    /**
     * 处理任务到期。
     *
     * @param task
     * @param taskReminder
     * @param userSet
     * @throws Exception
     */
    private void handlerDueTask(ProcessTask task, TaskReminder taskReminder, Set<Long> userSet) throws Exception {
        CalendarAssignService calendarAssignService = (CalendarAssignService) AppUtil.getBean(CalendarAssignService.class);
        ReminderStateDao reminderStateDao = (ReminderStateDao) AppUtil.getBean(ReminderStateDao.class);
        GroovyScriptEngine scriptEngine = (GroovyScriptEngine) AppUtil.getBean("scriptEngine");
        IBpmActService processService = (BpmActService) AppUtil.getBean("bpmActService");
        ProcessRunService processRunService = (ProcessRunService)AppUtil.getBean(ProcessRunService.class);
        String taskId = task.getId();
        String actDefId = task.getProcessDefinitionId();
        String actInstanceId = task.getProcessInstanceId();
        Date relativeStartTime = calendarAssignService.getRelativeStartTime(actInstanceId, taskReminder.getRelativeNodeId(), taskReminder.getRelativeNodeType());

        //未获取到相对节点的创建或者完成时间
        if(relativeStartTime==null){
            return;
        }

        //日历日
        if(new Integer(1).equals(taskReminder.getRelativeTimeType())){
            Date dueDate = new Date(TimeUtil.getNextTime(TimeUtil.MINUTE, taskReminder.getCompleteTime(), relativeStartTime.getTime()));
            Date curDate = new Date();
            if (dueDate.compareTo(curDate) <= 0){
                handlerAction(taskReminder,processRunService,task,processService,scriptEngine);
                // 记录提醒次数
                saveReminderState(taskId, actDefId, actInstanceId, null, Complete);
            }
        }
        //工作日
        else{
            //该任务有执行人或候选人(工作日需要考虑不同的用户工作日历、加班、请假不同的情况)
            if(userSet.size()>0){
                for (Iterator<Long> it = userSet.iterator(); it.hasNext();) {
                    Long userId = it.next();

                    int reminderTimes = reminderStateDao.getAmountByTaskId(Long.parseLong(taskId), Complete);
                    if (reminderTimes > 0)
                        continue;

                    Date dueDate = calendarAssignService.getTaskTimeByUser(relativeStartTime, taskReminder.getCompleteTime(), userId);
                    Date curDate = new Date();
                    if (dueDate.compareTo(curDate) > 0)
                        continue;
                    handlerAction(taskReminder,processRunService,task,processService,scriptEngine);

                    // 记录提醒次数
                    saveReminderState(taskId, actDefId, actInstanceId, userId, Complete);
                    break;
                }
            }
            //该任务没有执行人或候选人(没有人员时使用系统默认的工作日历)
            else{
                int reminderTimes = reminderStateDao.getAmountByTaskId(Long.parseLong(taskId), Complete);
                if (reminderTimes == 0){
                    Date dueDate = calendarAssignService.getTaskTimeByUser(relativeStartTime, taskReminder.getCompleteTime(),0);
                    Date curDate = new Date();
                    if (dueDate.compareTo(curDate) <= 0){
                        handlerAction(taskReminder,processRunService,task,processService,scriptEngine);
                        // 记录提醒次数
                        saveReminderState(taskId, actDefId, actInstanceId, null, Complete);
                    }
                }
            }
        }
    }

    /**
     * 处理任务到期动作
     * @param taskReminder
     * @param taskService
     * @param task
     * @param processService
     * @param scriptEngine
     * @throws Exception
     */
    private void handlerAction(TaskReminder taskReminder,ProcessRunService processRunService,ProcessTask task,IBpmActService processService,GroovyScriptEngine scriptEngine) throws Exception{
        Integer action = taskReminder.getAction();
        String taskId = task.getId();
        ProcessCmd processCmd = new ProcessCmd();
        processCmd.setTaskId(taskId);
        switch (action) {
            // 执行同意操作
            case 1:
                processCmd.setVoteAgree(new Short("1"));
                processRunService.nextProcess(processCmd);
                logger.debug("对该任务执行同意操作");
                break;
            //执行反对操作
            case 2:
                processCmd.setVoteAgree(new Short("2"));
                processRunService.nextProcess(processCmd);
                logger.debug("对该任务执行反对操作");
                break;
            //执行驳回操作
            case 3:
                processCmd.setVoteAgree(new Short("3"));
                processCmd.setBack(1);
                processRunService.nextProcess(processCmd);
                logger.debug("对该任务执行驳回操作");
                break;
            //执行驳回到发起人操作
            case 4:
                processCmd.setVoteAgree(new Short("3"));
                processCmd.setBack(2);
                processRunService.nextProcess(processCmd);
                logger.debug("对该任务执行驳回到发起人操作");
                break;
            //执行交办操作
            //TODO ZYG 2013-08-02
//			case 5:
//				Long userId = taskReminder.getAssignerId();
//				if (StringUtils.isNotEmpty(taskId) && userId >0) {
//					String userName = taskReminder.getAssignerName();
//					String description = "任务到期，系统将任务交办给用户【" + userName +"】来处理。";
//					processRunService.delegate(taskId, userId.toString(), description);
//					logger.debug("对该任务执行交办操作");
//				}
//				else{
//					logger.debug("任务到期执行交办操作失败，因为未能获取到接受交办的用户。");
//				}
//				break;
            // 结束流程
            case 6:
                processService.endProcessByTaskId(task.getId());
                logger.debug("结束流程");
                break;
            // 执行脚本
            case 7:
                String script = taskReminder.getScript();
                Map vars = new HashMap();
                vars.put("task", task);
                scriptEngine.execute(script, vars);
                logger.debug("执行指定的脚本");
                break;
        }
    }

    /**
     * 取得任务的人员数据。
     * @param task
     * @return
     */
    private Set<Long> getUserByTask(ProcessTask task) {
        TaskUserService taskUserService = (TaskUserService) AppUtil.getBean(TaskUserService.class);
        Set<Long> set = new HashSet<Long>();
        String assignee = task.getAssignee();
        if (StringUtil.isNotEmpty(task.getAssignee())) {
            set.add(Long.parseLong(assignee));
        }
        else {
            Set<SysUser> users = taskUserService.getCandidateUsers(task.getId());
            for(SysUser user:users){
                set.add(user.getUserId());
            }
        }
        return set;
    }

    /**
     * 设置提醒状态。
     *
     * @param reminderStateDao
     * @param taskId
     * @param actDefId
     * @throws Exception
     */
    private void saveReminderState(String taskId, String actDefId, String actInstanceId, Long userId, int remindType) throws Exception {
        ReminderStateDao reminderStateDao = (ReminderStateDao) AppUtil.getBean(ReminderStateDao.class);
        ReminderState reminderState = new ReminderState();
        reminderState.setId(UniqueIdUtil.genId());
        reminderState.setActInstanceId(actInstanceId);
        reminderState.setUserId(userId);
        reminderState.setActDefId(actDefId);
        reminderState.setTaskId(taskId);
        reminderState.setRemindType(remindType);
        reminderState.setCreatetime(new Date());
        reminderStateDao.add(reminderState);
    }

    private MailModel getMail(SysUser sysUser, String mailContent, String title, String url, String time, Map<String, Object> vars) {
        String mail = sysUser.getEmail();
        if (StringUtil.isEmpty(mail))
            return null;
        MailModel mailModel = new MailModel();
        String jumpUrl = "<a href='" + url + "'>" + title + "</a>";
        mailContent = mailContent.replace("${收件人}", sysUser.getFullname())
                .replace("${发件人}", "系统邮件")
                .replace("${跳转地址}", jumpUrl)
                .replace("${剩余时间}", time)
                .replace("${事项名称}", title);

        mailContent=replaceVars(mailContent,vars);

        String[] toUser = { mail };
        mailModel.setSubject(title);
        mailModel.setTo(toUser);
        mailModel.setContent(mailContent);
        mailModel.setSendDate(new Date());
        return mailModel;
    }

    private String replaceVars(String content,Map<String, Object> vars){
        for(Entry<String, Object> entry:vars.entrySet()){
            String hold="${"+entry.getKey()+"}";
            content=content.replace(hold, entry.getValue().toString());
        }
        return content;
    }

    /**
     * 取得短消息
     *
     * @param sysUser
     * @param smsContent
     * @param title
     * @param vars
     * @return
     */
    private SmsMobile getSms(SysUser sysUser, String smsContent, String title, String time, Map<String, Object> vars) {
        String mobile = sysUser.getMobile();
        if (StringUtil.isEmpty(mobile))
            return null;
        smsContent = smsContent.replace("${收件人}", sysUser.getFullname())
                .replace("${发件人}", "系统短信")
                .replace("${剩余时间}", time)
                .replace("${跳转地址}", "")
                .replace("${事项名称}", title);


        smsContent=replaceVars(smsContent,vars);

        SmsMobile smsModel = new SmsMobile();
        smsModel.setPhoneNumber(mobile);
        smsModel.setSmsContent(smsContent);
        smsModel.setUserName(sysUser.getFullname());
        smsModel.setSendTime(new Date());
        return smsModel;
    }

    /**
     * 取得内部消息
     *
     * @param sysUser
     * @param msgContent
     * @param title
     * @param vars
     * @return
     */
    private InnerMessage getMsg(SysUser sysUser, String msgContent, String title, String url, String time, Map<String, Object> vars) {
        String jumpUrl = "<a href='" + url + "'>" + title + "</a>";
        msgContent = msgContent.replace("${收件人}", sysUser.getFullname())
                .replace("${发件人}", "系统消息")
                .replace("${跳转地址}", jumpUrl)
                .replace("${剩余时间}", time)
                .replace("${事项名称}", title);

        msgContent=replaceVars(msgContent,vars);

        InnerMessage innerMessage = new InnerMessage();
        innerMessage.setSubject(title);
        innerMessage.setFrom("0"); //系统消息设置发送人ID为0
        innerMessage.setFromName("系统消息");
        innerMessage.setCanReply(new Short("0"));
        innerMessage.setContent(msgContent);
        innerMessage.setTo(String.valueOf(sysUser.getUserId()));
        innerMessage.setToName(String.valueOf(sysUser.getFullname()));
        innerMessage.setSendDate(new Date());
        return innerMessage;
    }

}
