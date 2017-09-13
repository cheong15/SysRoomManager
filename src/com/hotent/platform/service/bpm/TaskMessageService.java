package com.hotent.platform.service.bpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.task.Task;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Service;

import com.hotent.core.bpm.util.BpmConst;
import com.hotent.core.jms.MessageProducer;
import com.hotent.core.model.InnerMessage;
import com.hotent.core.model.MailModel;
import com.hotent.core.model.SmsMobile;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.dao.bpm.BpmDefinitionDao;
import com.hotent.platform.dao.bpm.BpmNodeSetDao;
import com.hotent.platform.dao.system.SysUserDao;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.BpmNodeSet;
import com.hotent.platform.model.bpm.TaskOpinion;
import com.hotent.platform.model.system.SysTemplate;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.model.system.SystemConst;
import com.hotent.platform.service.system.SysTemplateService;
import com.hotent.platform.service.util.ServiceUtil;
@Service
public class TaskMessageService {
	@Resource
	private MessageProducer messageProducer;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private TaskUserService taskUserService;
	@Resource
	private SysTemplateService sysTemplateService;
	@Resource
	private BpmDefinitionDao bpmDefinitionDao;
	@Resource
	private BpmNodeSetService bpmNodeSetService;
	
	public void setMessageProducer(MessageProducer producer){
		this.messageProducer=producer;
	}
	

	private void pushUser(Map<SysUser, List<Task>> users, SysUser user,
			Task task) {
		if (users.containsKey(user)) {
			users.get(user).add(task);
		} else {
			List<Task> list = new ArrayList<Task>();
			list.add(task);
			users.put(user, list);
		}
	}
	
	/**
	 * 获取默认的消息模板字符串
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getDefaultTemp() throws Exception {
		SysTemplate temp = sysTemplateService.getDefaultByUseType(SysTemplate.USE_TYPE_NOTIFY);
		if (temp == null)
			throw new Exception("模板中未找到内部消息的默认模板或系统模板");

		Map<String, String> map = new HashMap<String, String>();
		map.put("inner", temp.getInnerContent());
		map.put("mail", temp.getMailContent());
		map.put("shortmsg", temp.getSmsContent());
		map.put(SysTemplate.TEMPLATE_TITLE, temp.getTitle());
		return map;
	}
	
	/**
	 * 根据模板用途获取模板
	 * @param useType
	 * @return
	 * @throws Exception
	 */
	private Map<String,String> getTempByUseType(Integer useType)throws Exception{
		SysTemplate temp = sysTemplateService.getDefaultByUseType(useType);
		if (temp == null)
			throw new Exception("模板中未找到内部消息的默认模板或系统模板");

		Map<String, String> map = new HashMap<String, String>();
		map.put("inner", temp.getInnerContent());
		map.put("mail", temp.getMailContent());
		map.put("shortmsg", temp.getSmsContent());
		map.put(SysTemplate.TEMPLATE_TITLE, temp.getTitle());

		return map;
	}


	
	/**
	 * 发送 站内消息 短信 邮件 通知
	 * @param taskList 任务列表
	 * @param informTypes 通知方式
	 * @param subject	标题
	 * @param map		消息模版
	 * @param opinion	意见
	 * @throws Exception
	 */
	public void notify(List<Task> taskList, String informTypes, String subject, Map<String, String> map, String opinion) throws Exception {
		// 通知类型为空。
		if (taskList == null) 	return;
		Map<SysUser, List<Task>> users = new HashMap<SysUser, List<Task>>();
		for (Task task : taskList) {
			String actDefId=task.getProcessDefinitionId();
			String nodeId=task.getTaskDefinitionKey();
			BpmDefinition bpmDefinition=bpmDefinitionDao.getByActDefId(actDefId);
			BpmNodeSet bpmNodeSet=bpmNodeSetService.getByMyActDefIdNodeId(actDefId, nodeId);
			if(StringUtil.isEmpty(informTypes)){
				if(!BpmDefinition.STATUS_TEST.equals(bpmDefinition.getStatus())&&StringUtil.isNotEmpty(bpmNodeSet.getInformType())){
					informTypes=bpmNodeSet.getInformType();
				}else{
					informTypes=bpmDefinition.getInformType();
				}
			}
			if (StringUtil.isEmpty(informTypes)) continue;
			String assignee=task.getAssignee();
			// 存在指定的用户
			if (ServiceUtil.isAssigneeNotEmpty(assignee)) {
				SysUser user = sysUserDao.getById(Long.parseLong(assignee));
				this.pushUser(users, user, task);
			}
			// 获取该任务的候选用户列表
			else {
				Set<SysUser> cUIds = taskUserService.getCandidateUsers(task.getId());
				for (SysUser user : cUIds) {
					pushUser(users, user, task);
				}
			}
		}
		SysUser curUser = ContextUtil.getCurrentUser();
		
		String fullName = SystemConst.SYSTEMUSERNAME;
		if(BeanUtils.isNotEmpty(curUser)){
			fullName = curUser.getFullname();
		}
		for (SysUser user : users.keySet()) {
			List<Task> tasks = users.get(user);
			for (Task task : tasks) {
				//是否为代理任务(代理任务发送消息给任务所属人)
				boolean isAgentTask=TaskOpinion.STATUS_AGENT.toString().equals(task.getDescription());
				if (map == null){
					if(isAgentTask){
						map=getTempByUseType(SysTemplate.USE_TYPE_NOTIFYOWNER_AGENT);
					}else{
						map=getDefaultTemp();
					}
				}
				if(isAgentTask){
					user=sysUserDao.getById(Long.parseLong(task.getOwner()));
				}
				// 手机短信
				if (informTypes.contains(BpmConst.MESSAGE_TYPE_SMS)) {
					this.sendShortMessage(user, subject, map.get(SysTemplate.TEMPLATE_TYPE_SMS), fullName,
							opinion);
				}
				// 邮件
				if (informTypes.contains(BpmConst.MESSAGE_TYPE_MAIL)) {
					this.sendMailMessage(user, map.get(SysTemplate.TEMPLATE_TITLE), subject, map.get(SysTemplate.TEMPLATE_TYPE_MAIL),
							fullName,opinion, Long.parseLong(task.getId()), true);
				}
				// 内部消息
				if (informTypes.contains(BpmConst.MESSAGE_TYPE_INNER)) {
					// 催办 冒泡
					this.sendInnerMessage(user,
							map.get(SysTemplate.TEMPLATE_TITLE), subject,
							map.get(SysTemplate.TEMPLATE_TYPE_INNER), curUser, opinion,
							Long.parseLong(task.getId()), true);
				}
			}
		}
	}
	
	/**
	 * 发送消息接口
	 * 
	 * @param sendUser
	 *            发送者名称
	 * @param receiverUserList
	 *            接收者用户列表
	 * @param informTypes
	 *            发送消息类型
	 * @param msgTempMap
	 *            消息模板
	 * @param subject
	 *            事项名称
	 * @param opinion
	 *            意见或原因
	 * @param taskId
	 *            任务Id
	 * @param runId
	 *            流程运行ID
	 * @throws Exception
	 */
	public void sendMessage(SysUser sendUser, List<SysUser> receiverUserList,
			String informTypes, Map<String, String> msgTempMap, String subject,
			String opinion,  Long taskId,
			Long runId) throws Exception {
		if(StringUtil.isEmpty(informTypes))return;
		if(BeanUtils.isEmpty(sendUser)){
			sendUser=new SysUser();
			sendUser.setUserId(0L);
			sendUser.setFullname("系统消息");
		}
		if (BeanUtils.isEmpty(receiverUserList))
			return;
		if (BeanUtils.isEmpty(msgTempMap))
			return;
		Long id = null;
		boolean isTask = true;
		if (BeanUtils.isNotEmpty(taskId)) {
			id = taskId;
			isTask = true;
		} else {
			id = runId;
			isTask = false;
		}
		String title = msgTempMap.get(SysTemplate.TEMPLATE_TITLE);
		
		String smsTemplate = msgTempMap.get(SysTemplate.TEMPLATE_TYPE_SMS);
		String mailTemplate = msgTempMap.get(SysTemplate.TEMPLATE_TYPE_MAIL);
		String innerTemplate = msgTempMap.get(SysTemplate.TEMPLATE_TYPE_INNER);
		smsTemplate = StringUtil.jsonUnescape(smsTemplate);
		mailTemplate = StringUtil.jsonUnescape(mailTemplate);
		
		int userSize = receiverUserList.size();
		
		for (int i=0;i<userSize;i++) {
			SysUser receiverUser = receiverUserList.get(i);
			// 邮件
			if (informTypes.contains(BpmConst.MESSAGE_TYPE_MAIL)){
				sendMailMessage(receiverUser, title, subject,mailTemplate, sendUser.getFullname(), opinion, id, isTask);
			}
			// 短信
			if (informTypes.contains(BpmConst.MESSAGE_TYPE_SMS))
				sendShortMessage(receiverUser, subject, smsTemplate,sendUser.getFullname(), opinion);

			// 内部消息
			if (informTypes.contains(BpmConst.MESSAGE_TYPE_INNER))
				sendInnerMessage(receiverUser, title, subject,
						innerTemplate, sendUser, opinion, id, isTask);

		}

	}
		
	/**
	 * 发送内部消息。
	 * 
	 * <pre>
	 * [开始,沟通 ，沟通反馈 ，驳回 ，撤销，发起流程，处理流程，催办 冒泡]
	 * </pre>
	 * 
	 * @param receiverUser
	 *            收件人
	 * @param title
	 *            消息标题
	 * @param subject
	 *            事项名称
	 * @param tempStr
	 *            消息模板
	 * @param sendUser
	 *            发送人 如果为空返回
	 * @param cause
	 *            意见(或原因)
	 * @param id
	 *            流程运行ID或任务ID（由isTask决定）
	 * @param isTask
	 *            是否是任务（taskId 是true,runId 是false）
	 * @throws Exception
	 */
	private void sendInnerMessage(SysUser receiverUser,
			String title, String subject, String tempStr, SysUser sendUser,
			 String cause, Long id, boolean isTask)
			throws Exception {
		if ((Jsoup.clean(tempStr, new Whitelist()).isEmpty()))
			return;
		if (BeanUtils.isEmpty(sendUser))
			return;
		// 链接地址

		String url="";
		if(BeanUtils.isNotEmpty(id)){
			url= ServiceUtil.getUrl(id.toString(), isTask);
		}
		if(StringUtil.isNotEmpty(url)){
			url = url.replace("http://", "");
			url = url.substring(url.indexOf("/"), url.length());
		}
		
		// 发送标题
		title = ServiceUtil.replaceTitleTag(title, receiverUser.getFullname(),
				sendUser.getFullname(), subject, cause);
		// 发送内容
		String content = ServiceUtil.replaceTemplateTag(tempStr,
				receiverUser.getFullname(), sendUser.getFullname(), subject, url, cause,
				false);

		InnerMessage innerModel=new InnerMessage();
		innerModel.setSubject(title);
		innerModel.setContent(content);
		innerModel.setSendDate(new Date());
		innerModel.setTo(receiverUser.getUserId().toString());
		innerModel.setToName(receiverUser.getFullname());
		innerModel.setFrom(sendUser.getUserId().toString());
		innerModel.setFromName(sendUser.getFullname());
		messageProducer.send(innerModel);
	}
	
	/**
	 * 发送手机短信
	 * 
	 * @param receiverUser
	 *            收件人
	 * @param subject
	 *            事项名称
	 * @param tempStr
	 *            消息模板
	 * @param sendUser
	 *            发送人 如果为空为"系统消息"
	 * @param cause
	 *            意见(或原因)
	 * @throws Exception
	 */
	private void sendShortMessage(SysUser receiverUser, String subject,
			String tempStr, String sendUser, String cause) throws Exception {
		String phone = receiverUser.getMobile();
		if (phone == null)
			return;
		if (StringUtil.isEmpty(sendUser))
			sendUser = "系统消息";

		String content = ServiceUtil.replaceTemplateTag(tempStr,
				receiverUser.getFullname(), sendUser, subject, "", cause, true);
		SmsMobile smsMobile = new SmsMobile();
		smsMobile.setPhoneNumber(receiverUser.getMobile());
		smsMobile.setSmsContent(content);
		messageProducer.send(smsMobile);
	}
	
	/**
	 * 发送邮件，查看流程实例。
	 * 
	 * @param receiverUser
	 *            收件人
	 * @param title
	 *            邮件标题
	 * @param subject
	 *            事项名称
	 * @param tempStr
	 *            消息模板
	 * @param sendUser
	 *            发送人 如果为空为"系统消息"
	 * @param cause
	 *            意见(或原因)
	 * @param id
	 *            流程运行ID或任务ID（由isTask决定）
	 * @param isTask
	 *            是否是任务（taskId 是true,runId 是false）
	 * @throws Exception
	 */
	private void sendMailMessage(SysUser receiverUser, String title,
			String subject, String tempStr, String sendUser, String cause,
			Long id, boolean isTask) throws Exception {
		String email = receiverUser.getEmail();
		if (StringUtil.isEmpty(email))
			return;

		if (StringUtil.isEmpty(sendUser))
			sendUser = "系统消息";
		String url="";
		if(BeanUtils.isNotEmpty(id)){
			url= ServiceUtil.getUrl(id.toString(), isTask);
		}
		title = ServiceUtil.replaceTitleTag(title, receiverUser.getFullname(),sendUser, subject, cause);
		String content = ServiceUtil.replaceTemplateTag(tempStr,receiverUser.getFullname(), sendUser, subject, url, cause,false);
		
		MailModel mailModel = new MailModel();
		String[] sendTos = { email };
		mailModel.setTo(sendTos);
		mailModel.setSubject(title);
		mailModel.setContent(content);
		mailModel.setSendDate(new Date());
		messageProducer.send(mailModel);
	}
	/**
	 * 直接发送消息
	 * @param sendUser 发送者
	 * @param receiverUserList 接受者
	 * @param informTypes 消息方式
	 * @param title 标题
	 * @param content 内容
	 */
	public void sendMessage(SysUser sendUser,List<SysUser>receiverUserList,String informTypes,String title,String content){
		int userSize = receiverUserList.size();
		for (int i=0;i<userSize;i++) {
			SysUser receiverUser = receiverUserList.get(i);
			// 邮件
			if (informTypes.contains(BpmConst.MESSAGE_TYPE_MAIL)){
				String email = receiverUser.getEmail();
				if (StringUtil.isEmpty(email))
					return;
				MailModel mailModel = new MailModel();
				String[] sendTos = { email };
				mailModel.setTo(sendTos);
				mailModel.setSubject(title);
				mailModel.setContent(content);
				mailModel.setSendDate(new Date());
				messageProducer.send(mailModel);
			}
			// 短信
			if (informTypes.contains(BpmConst.MESSAGE_TYPE_SMS)){
				String phone = receiverUser.getMobile();
				if (phone == null)
					return;
				SmsMobile smsMobile = new SmsMobile();
				smsMobile.setPhoneNumber(receiverUser.getMobile());
				smsMobile.setSmsContent(content);
				messageProducer.send(smsMobile);
			}
			// 内部消息
			if (informTypes.contains(BpmConst.MESSAGE_TYPE_INNER)){
				InnerMessage innerModel=new InnerMessage();
				innerModel.setSubject(title);
				innerModel.setContent(content);
				innerModel.setSendDate(new Date());
				innerModel.setTo(receiverUser.getUserId().toString());
				innerModel.setToName(receiverUser.getFullname());
				innerModel.setFrom(sendUser.getUserId().toString());
				innerModel.setFromName(sendUser.getFullname());
				messageProducer.send(innerModel);
			}
		}
	}
}
