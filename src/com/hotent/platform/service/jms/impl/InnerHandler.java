package com.hotent.platform.service.jms.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hotent.core.engine.MessageEngine;
import com.hotent.core.model.InnerMessage;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.model.system.MessageSend;
import com.hotent.platform.service.jms.IJmsHandler;

/**
 * 发送内部消息的实现类
 * @author zxh
 *
 */
public class InnerHandler implements IJmsHandler {

	private final Log logger = LogFactory.getLog(InnerHandler.class);
	@Resource
	private MessageEngine messageEngine;

	@Override
	public void handMessage(Object model) {
		InnerMessage innerModel = (InnerMessage) model;
		MessageSend messageSend = new MessageSend();
		messageSend.setId(UniqueIdUtil.genId());
		messageSend.setUserName(innerModel.getFromName());
		messageSend.setUserId(Long.parseLong(innerModel.getFrom()));
		messageSend.setSendTime(new Date());
		messageSend.setMessageType(MessageSend.MESSAGETYPE_FLOWTASK);
		messageSend.setContent(innerModel.getContent());
		messageSend.setSubject(innerModel.getSubject());
		messageSend.setCreateBy(Long.parseLong(innerModel.getFrom()));
		messageSend.setRid(Long.parseLong(innerModel.getTo()));
		messageSend.setReceiverName(innerModel.getToName());
		messageSend.setCanReply(innerModel.getCanReply());
		messageSend.setCreatetime(innerModel.getSendDate());
		messageEngine.sendInnerMessage(messageSend);

		logger.debug("InnerMessage");
	}

}
