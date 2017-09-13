package com.hotent.platform.service.jms.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;

import com.hotent.core.engine.MessageEngine;
import com.hotent.core.model.MailModel;
import com.hotent.platform.service.jms.IJmsHandler;
/**
 * 发送邮件消息的实现类
 * @author zxh
 *
 */
public class MailHandler implements IJmsHandler {

	private final Log logger = LogFactory.getLog(MailHandler.class);
	@Resource
	private MessageEngine messageEngine;

	@Override
	public void handMessage(Object model) {
		MailModel mailModel = (MailModel) model;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(mailModel.getSubject());
		message.setTo(mailModel.getTo());
		message.setCc(mailModel.getCc());
		message.setBcc(mailModel.getBcc());
		message.setText(mailModel.getContent());
		message.setSentDate(mailModel.getSendDate());
		messageEngine.sendMail(message);
		logger.debug("MailModel");
	}

}
