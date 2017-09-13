package com.hotent.platform.dao.mail;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.mail.OutMailAttachment;
/**
 *<pre>
 * 对象功能:OUT_MAIL_ATTACHMENT Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:guojh
 * 创建时间:2014-03-04 13:49:25
 *</pre>
 */
@Repository
public class OutMailAttachmentDao extends BaseDao<OutMailAttachment>
{
	@Override
	public Class<?> getEntityClass()
	{
		return OutMailAttachment.class;
	}

	public List<OutMailAttachment> getByMailId(long mailId) {
		return this.getBySqlKey("getByMailId", mailId);
	}

}