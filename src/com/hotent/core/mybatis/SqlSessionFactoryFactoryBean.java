package com.hotent.core.mybatis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;

/**
 * 功能：SqlSessionFactoryFactoryBean，获取SqlSessionFactory。
 * @author hotent
 *
 */
public class SqlSessionFactoryFactoryBean implements FactoryBean, InitializingBean
{
	Log logger = LogFactory.getLog(SqlSessionFactoryFactoryBean.class);
	private Resource configLocation;
	private Resource[] mapperLocations;
	private DataSource dataSource;
	private boolean useTransactionAwareDataSource = true;
	SqlSessionFactory sqlSessionFactory;

	/**
	 * 创建sqlSessionFactory。
	 */
	public void afterPropertiesSet() throws Exception
	{
		Assert.notNull(configLocation, "configLocation must be not null");
		sqlSessionFactory = createSqlSessionFactory();
	}

	@SuppressWarnings("unused")
	private SqlSessionFactory createSqlSessionFactory() throws IOException{
		Reader reader = new InputStreamReader(getConfigLocation().getInputStream());
		try{
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			Configuration conf = sqlSessionFactory.getConfiguration();
			// 数据源不为空
			if (dataSource != null){
				DataSource dataSourceToUse = this.dataSource;
				if (this.useTransactionAwareDataSource && !(this.dataSource instanceof TransactionAwareDataSourceProxy)){
					dataSourceToUse = new TransactionAwareDataSourceProxy(this.dataSource);
				}
				conf.setEnvironment(new Environment("development", new ManagedTransactionFactory(), dataSourceToUse));
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(conf);
			}
			// sql map文件
			if (mapperLocations != null){
				for (Resource mapperLocation : mapperLocations){
					String path="";
	                if (mapperLocation instanceof ClassPathResource) {
	                    path = ((ClassPathResource) mapperLocation).getPath();
	                } else {
	                    path = mapperLocation.toString();
	                }
	                try {
	                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
	                    		conf, path, conf.getSqlFragments());
	                    xmlMapperBuilder.parse();
	                    conf.buildAllStatements();
	                   
	                } catch (Exception e) {
	                    throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
	                } 
	                finally {
	                    ErrorContext.instance().reset();
	                }
				}
			}
			return sqlSessionFactory;
		}
		finally{
			reader.close();
		}
	}

	/**
	 * 返回sqlSessionFactory
	 */
	public Object getObject() throws Exception
	{
		return sqlSessionFactory;
	}

	public DataSource getDataSource()
	{
		return dataSource;
	}

	/**
	 * 设置数据源

	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public Class getObjectType()
	{
		return SqlSessionFactory.class;
	}

	public boolean isSingleton()
	{
		return true;
	}

	public Resource getConfigLocation()
	{
		return configLocation;
	}

	/**
	 * 配置文件
	 * @return
	 */
	public void setConfigLocation(Resource configurationFile)
	{
		this.configLocation = configurationFile;
	}

	/**
	 * maper文件
	 * @param mapperLocations
	 */
	public void setMapperLocations(Resource[] mapperLocations)
	{
		this.mapperLocations = mapperLocations;
	}
}
