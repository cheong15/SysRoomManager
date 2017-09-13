package net.gmcc.gz.service.am;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.gmcc.gz.dao.am.AnnouncementDao;
import net.gmcc.gz.model.am.Announcement;
import net.gmcc.gz.model.am.AnnouncementList;
import net.gmcc.gz.model.am.AnnouncementStatusEnum;
import net.gmcc.gz.model.attach.Attachment;
import net.gmcc.gz.model.attach.AttachmentBizTypeEnum;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.dao.system.DictionaryDao;
import com.hotent.platform.dao.system.GlobalTypeDao;
import com.hotent.platform.model.system.Dictionary;
import com.hotent.platform.model.system.GlobalType;

/**
 *<pre>
 * 对象功能:公告管理表 Service类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-09-07 18:34:39
 *</pre>
 */
@Service
public class AnnouncementService extends BaseService<Announcement>
{
	@Resource
	private AnnouncementDao dao;
	
	@Resource
	private GlobalTypeDao globalTypeDao;
	
	@Resource
	private DictionaryDao dictionaryDao;
	
	public AnnouncementService()
	{
	}
	
	@Override
	protected IEntityDao<Announcement, Long> getEntityDao() 
	{
		return dao;
	}
	
	public int insertOne(Announcement announcement)
	{
		return dao.insertOne(announcement);
	}
	
	/**
	 * 按过滤器查询记录列表
	 * @param queryFilter
	 * @return
	 */
	public List<Announcement> getList(QueryFilter queryFilter){
		return dao.getList(queryFilter);
	}
	
	/**
	 * 获取公告类型数据字典
	 * @return
	 */
	public List<String> getAnnouncementType(){
		
		List<String> list = new ArrayList<String>();
		GlobalType globalType =globalTypeDao.getByDictNodeKey("GGLX");
		long typeId=globalType.getTypeId();
		for(Dictionary dic : dictionaryDao.getByTypeId(typeId)){
			list.add(dic.getItemName());
		}
		return list;
	}
	
	/**
	 * 根据主键操作状态
	 * @param id
	 */
	public void operateStatusById(Long id, String status, String checkOpinion)
	{
		dao.operateStatusById(id, status, checkOpinion);
	}

	/**
	 * 根据主键批量操作状态
	 * @param ids
	 */
	public void operateStatusByIds(Long[] ids, String status){
		if(BeanUtils.isEmpty(ids)) return;
		for (Long id : ids){
			operateStatusById(id, status, null);
		}
	}
	
	public static final Map<String,String> statusMap = new HashMap<String, String>();
    static{
    	statusMap.put(AnnouncementStatusEnum.DRAFT.name(), AnnouncementStatusEnum.DRAFT.getValue());
    	statusMap.put(AnnouncementStatusEnum.CHECK_FAIL.name(), AnnouncementStatusEnum.CHECK_FAIL.getValue());
    	statusMap.put(AnnouncementStatusEnum.CHECK_SUCCESS.name(), AnnouncementStatusEnum.CHECK_SUCCESS.getValue());
    	statusMap.put(AnnouncementStatusEnum.PUBLISH.name(), AnnouncementStatusEnum.PUBLISH.getValue());
    }
    public String getStatusValue(String status){
    	return statusMap.get(status);
    }
    
    /**
     * 构建附件对象
     * @param id
     * @param uploaderId
     * @param uploaderName
     * @return
     */
    public Attachment constructAttachment(String id, String uploaderId, String uploaderName){
    	
    	Attachment attachment = new Attachment();
		attachment.setBusinessId(id);
		attachment.setBusinessType(AttachmentBizTypeEnum.ATTACHMENT.name());
		attachment.setUploaderId(uploaderId);
		attachment.setUploaderName(uploaderName);
		attachment.setUploadTime(new Date());
		return attachment;
    }
    
	/**
	 * 类型转换
	 * @param list
	 * @return
	 */
	public List<AnnouncementList> convert(List<Announcement> list){
		
		List<AnnouncementList> newList = new ArrayList<AnnouncementList>();
		for(Announcement announcement : list){
			AnnouncementList announcementList = new AnnouncementList();
			BeanUtils.copyNotNullProperties(announcementList, announcement);
			newList.add(announcementList);
		}
		return newList;
	}
}
