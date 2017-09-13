package com.hotent.platform.service.jms.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hotent.core.engine.MessageEngine;
import com.hotent.core.model.SmsMobile;
import com.hotent.platform.service.jms.IJmsHandler;

/**
 * 发送手机消息的实现类
 * 
 * @author zxh
 * 
 */
public class SmsHandler implements IJmsHandler {

	private final Log logger = LogFactory.getLog(SmsHandler.class);
	@Resource
	private MessageEngine messageEngine;

	@Override
	public void handMessage(Object model) {
		SmsMobile smsModel = (SmsMobile) model;
		//System.out.println(smsModel.getUserName());
//		List<String> mobiles = new ArrayList<String>();
//		mobiles.add(smsModel.getPhoneNumber());
//		messageEngine.sendSms(mobiles, smsModel.getSmsContent());
//		logger.debug("SmsMobile");
	}

}
