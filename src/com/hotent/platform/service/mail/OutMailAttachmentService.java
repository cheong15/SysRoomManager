package com.hotent.platform.service.mail;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.platform.dao.mail.OutMailAttachmentDao;
import com.hotent.platform.model.mail.OutMailAttachment;

/**
 *<pre>
 * 对象功能:OUT_MAIL_ATTACHMENT Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:guojh
 * 创建时间:2014-03-04 13:49:25
 *</pre>
 */
@Service
public class OutMailAttachmentService extends BaseService<OutMailAttachment>
{
	@Resource
	private OutMailAttachmentDao dao;
	
	
	
	public OutMailAttachmentService()
	{
	}
	
	@Override
	protected IEntityDao<OutMailAttachment, Long> getEntityDao() 
	{
		return dao;
	}

	public List<OutMailAttachment> getByMailId(long mailId) {
		return dao.getByMailId(mailId);
	}
	
	
}
