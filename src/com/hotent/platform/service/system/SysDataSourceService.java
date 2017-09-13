package com.hotent.platform.service.system;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.db.JdbcHelper;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.platform.dao.system.SysDataSourceDao;
import com.hotent.platform.model.system.SysDataSource;

/**
 * 对象功能:系统数据源管理 Service类 
 * 开发公司:广州宏天软件有限公司 
 * 开发人员:xwy 
 * 创建时间:2011-11-16 16:34:16
 */
@Service
public class SysDataSourceService extends BaseService<SysDataSource> {
	@Resource
	private SysDataSourceDao dao;
	
	public SysDataSourceService() {
	}

	@Override
	protected IEntityDao<SysDataSource, Long> getEntityDao() {
		return dao;
	}
	
	/**
	 * 根据id集合删除数据源。
	 */
	@Override
	public void delByIds(Long[] ids){
		if(BeanUtils.isEmpty(ids)) return;
		for (Long p : ids){
			SysDataSource sysDataSource = dao.getById(p);
			//删除JdbcHelper中的数据源。
			JdbcHelper.getInstance().removeAlias(sysDataSource.getAlias());
			delById(p);
		}
	}

	/**
	 * 测试数据源是否可以连接
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> testConnectById(Long[] ids) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (long id : ids) {
			SysDataSource sysDataSource = dao.getById(id);
			result.addAll(testConnectByForm(sysDataSource));
		}

		return result;
	}

	/**
	 * 测试数据源是否可以连接
	 * 
	 * @param sysDataSource
	 * @return
	 */
	public List<Map<String, Object>> testConnectByForm(SysDataSource sysDataSource) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		// 连接信息
		Map<String, Object> connectResult = new HashMap<String, Object>();
		connectResult.put("name", sysDataSource.getName());
		try {
			Class.forName(sysDataSource.getDriverName());
			// 尝试连接
			DriverManager.getConnection(sysDataSource.getUrl(), sysDataSource.getUserName(),sysDataSource.getPassword()).close();
			// 连接成功
			connectResult.put("success", true);
		} catch (ClassNotFoundException e) {
			// 连接失败
			connectResult.put("msg", "ClassNotFoundException: " + sysDataSource.getDriverName());
			connectResult.put("success", false);
		} catch (SQLException e) {
			// 连接失败
			connectResult.put("msg", e.getMessage());
			connectResult.put("success", false);
		}
		result.add(connectResult);

		return result;
	}
	
	/**
	 * 根据别名获取数据源
	 * @param alias
	 * @return
	 */
	public SysDataSource getByAlias(String alias){
		return dao.getByAlias(alias);
	}
	
	/**
	 * 别名是否已存在
	 * @param alias
	 * @return
	 */
	public boolean isAliasExisted(String alias) {
		return dao.isAliasExisted(alias);
	}
	
	/**
	 * 更新的别名是否已存在
	 * @param sysDataSource
	 * @return
	 */
	public boolean isAliasExistedByUpdate(SysDataSource sysDataSource) {
		return dao.isAliasExistedByUpdate(sysDataSource);
	}
	
	/**
	 * 通过数据源别名获取数据源管理类
	 * @param alias
	 * @return
	 */
	public DriverManagerDataSource getDriverMangerDataSourceByAlias(String dsAlias){
		return dao.getDriverMangerDataSourceByAlias(dsAlias);
	}
	
}
