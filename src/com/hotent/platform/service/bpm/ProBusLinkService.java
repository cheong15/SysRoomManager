package com.hotent.platform.service.bpm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.dao.bpm.ProBusLinkDao;
import com.hotent.platform.model.bpm.ProBusLink;

/**
 *<pre>
 * 对象功能:流程实例与业务关联表 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2013-07-17 14:01:11
 *</pre>
 */
@Service
public class ProBusLinkService extends BaseService<ProBusLink>
{
	@Resource
	private ProBusLinkDao dao;
	
	protected Logger logger = LoggerFactory.getLogger(ProBusLinkService.class);
	
	public ProBusLinkService()
	{
	}
	
	@Override
	protected IEntityDao<ProBusLink, Long> getEntityDao() 
	{
		return dao;
	}
	
	public void addProLink(ProcessCmd cmd){
		HttpServletRequest req=RequestUtil.getHttpServletRequest();
		String taskId=req.getParameter("taskId");
		logger.info(cmd.toString());
		logger.info("addProLink");
	}
	
	public void addProLink2(ProcessCmd md){
		String s="后置处理器";
		logger.info(s);
	}
}

