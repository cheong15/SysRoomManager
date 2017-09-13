package net.gmcc.gz.dao.am;

import java.util.List;

import net.gmcc.gz.model.am.Announcement;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.core.web.query.QueryFilter;
/**
 *<pre>
 * 对象功能:公告管理表 Dao类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-09-07 18:34:39
 *</pre>
 */
@Repository
public class AnnouncementDao extends BaseDao<Announcement>
{
	@Override
	public Class<?> getEntityClass()
	{
		return Announcement.class;
	}
	
	/**
	 * 按过滤器查询记录列表
	 * @param queryFilter
	 * @return
	 */
	public List<Announcement> getList(QueryFilter queryFilter){
		return this.getBySqlKey("getList",queryFilter);
	}
	
	public int insertOne(Announcement announcement){
		String addStatement=getIbatisMapperNamespace() + ".insertOne";
		return getSqlSessionTemplate().insert(addStatement, announcement);
	}
	
	/**
	 * 公告状态调整
	 * @param id
	 * @param status
	 * @param checkOpinion
	 * @return
	 */
	public int operateStatusById(Long id, String status, String checkOpinion){
		Announcement announcement = new Announcement();
		announcement.setId(id);
		announcement.setStatus(status);
		announcement.setCheckOpinion(checkOpinion);
		return this.update("operateStatusById",announcement);
	}

}