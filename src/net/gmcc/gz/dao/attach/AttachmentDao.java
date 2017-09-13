package net.gmcc.gz.dao.attach;

import java.util.List;

import com.hotent.core.db.BaseDaoStr;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import net.gmcc.gz.model.attach.Attachment;
/**
 *<pre>
 * 对象功能:上传附件管理 Dao类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:lchh
 * 创建时间:2017-09-06 17:31:55
 *</pre>
 */
@Repository
public class AttachmentDao extends BaseDaoStr<Attachment>
{
	@Override
	public Class<?> getEntityClass()
	{
		return Attachment.class;
	}


	/**
	 * 获取文件序号
	 * @return
	 */
	public Long getSortNum(Attachment queryAttachment){
		Long sortNum = (Long) getOne("getSortNum", queryAttachment);
		return sortNum;
	}

}