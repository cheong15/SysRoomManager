package net.gmcc.gz.dao.sm;

import java.util.List;
import java.util.Map;

import net.gmcc.gz.model.sm.OpDataLog;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
/**
 *<pre>
 * 对象功能:操作记录数据保存 Dao类
 * 开发公司:从兴技术有限公司
 * 开发人员:John
 * 创建时间:2014-04-26 18:13:32
 *</pre>
 */
@Repository
public class OpDataLogDao extends BaseDao<OpDataLog>
{
	@Override
	public Class<?> getEntityClass()
	{
		return OpDataLog.class;
	}
	
	/**
	 * 更新一条记录
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int update(String sql) throws Exception{
		int cou = this.jdbcTemplate.update(sql);
		return cou;
	}
	
	/**
	 * 以List形式返回查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public  List<Map<String,Object>>  queryForList(String sql) throws Exception{
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}


}