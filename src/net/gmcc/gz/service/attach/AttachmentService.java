package net.gmcc.gz.service.attach;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.gmcc.gz.dao.attach.AttachmentDao;
import net.gmcc.gz.model.attach.Attachment;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseServiceStr;

/**
 *<pre>
 * 对象功能:上传附件管理 Service类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:lchh
 * 创建时间:2017-09-06 17:31:55
 *</pre>
 */
@Service
public class AttachmentService extends BaseServiceStr<Attachment>
{
	@Resource
	private AttachmentDao dao;
	
	
	
	public AttachmentService()
	{
	}
	
	@Override
	protected IEntityDao<Attachment, String> getEntityDao()
	{
		return dao;
	}

	/**
	 * 获取文件序号
	 * @return
	 */
    public Long getSortNum(Attachment queryAttachment) {
    	return dao.getSortNum(queryAttachment);
    }


	/**
	 * 获取所有attachment
	 * @param attachment
	 * @return
	 */
	public List<Attachment> getAllAttachment(Map<String,Object> map) {
		return dao.getBySqlKey("getAllAttachment", map);
	}

}
