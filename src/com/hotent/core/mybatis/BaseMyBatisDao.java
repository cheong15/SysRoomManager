package com.hotent.core.mybatis;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;


/**
 * mybatis 操作基类，实现mybatis 基本操作。
 * @author hotent
 *
 */
public abstract class BaseMyBatisDao extends DaoSupport
{
	protected final Log log = LogFactory.getLog(getClass());
	/**
	 *  注入sqlSessionFactory。
	 */
	private SqlSessionFactory sqlSessionFactory;
	/**
	 * 注入sqlSessionTemplate。
	 */
	private SqlSessionTemplate sqlSessionTemplate;

	protected void checkDaoConfig() throws IllegalArgumentException
	{
		
		Assert.notNull(sqlSessionFactory, "sqlSessionFactory must be not null");
	}

	/**
	 * 返回SqlSessionFactory
	 * @return
	 */
	public SqlSessionFactory getSqlSessionFactory()
	{
		return sqlSessionFactory;
	}

	/**
	 * 注入sqlSessionFactory
	 * @param sqlSessionFactory
	 */
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
	{
		this.sqlSessionFactory = sqlSessionFactory;
		this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 返回sqlSessionTemplate
	 * @return
	 */
	public SqlSessionTemplate getSqlSessionTemplate()
	{
		return sqlSessionTemplate;
	}
	
	/**
	 * 根据mapper文件的Id 和参数对象获取IbatisSql对象。<br>
	 * 用于获取SQL对象。
	 * @param id
	 * @param parameterObject
	 * @return
	 */
	@SuppressWarnings("unused")
	public IbatisSql getIbatisSql(String id, Object parameterObject) {    
	    IbatisSql ibatisSql = new IbatisSql();    
	   
	    Collection<String> coll= sqlSessionFactory.getConfiguration().getMappedStatementNames();
	    MappedStatement ms = sqlSessionFactory.getConfiguration().getMappedStatement(id);    
	    BoundSql boundSql = ms.getBoundSql(parameterObject);    
	        
	    List<ResultMap> ResultMaps=ms.getResultMaps();    
	    if(ResultMaps!=null&&ResultMaps.size()>0){    
	        ResultMap ResultMap = ms.getResultMaps().get(0);    
	        ibatisSql.setResultClass(ResultMap.getType());    
	    }    
	        
	        
	    ibatisSql.setSql(boundSql.getSql());    
	   
	    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();    
	    if (parameterMappings != null) {    
	        Object[] parameterArray = new Object[parameterMappings.size()];    
	        MetaObject metaObject = parameterObject == null ? null : MetaObject.forObject(parameterObject);    
	        for (int i = 0; i < parameterMappings.size(); i++) {    
	          ParameterMapping parameterMapping = parameterMappings.get(i);    
	          if (parameterMapping.getMode() != ParameterMode.OUT) {    
	            Object value;    
	            String propertyName = parameterMapping.getProperty();    
	            PropertyTokenizer prop = new PropertyTokenizer(propertyName);    
	            if (parameterObject == null) {    
	              value = null;    
	            } else if (ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {    
	              value = parameterObject;    
	            } else if (boundSql.hasAdditionalParameter(propertyName)) {    
	              value = boundSql.getAdditionalParameter(propertyName);    
	            } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)    
	                && boundSql.hasAdditionalParameter(prop.getName())) {    
	              value = boundSql.getAdditionalParameter(prop.getName());    
	              if (value != null) {    
	                value = MetaObject.forObject(value).getValue(propertyName.substring(prop.getName().length()));    
	              }    
	            } else {    
	              value = metaObject == null ? null : metaObject.getValue(propertyName);    
	            }    
	            parameterArray[i] = value;    
	          }    
	        }    
	        ibatisSql.setParameters(parameterArray);    
	    }    
	   
	    return ibatisSql;    
	}   

	/**
	 * SqlSessionTemplate 模版类。
	 * @author hotent
	 */
	public static class SqlSessionTemplate
	{
		SqlSessionFactory sqlSessionFactory;

		/**
		 * 构建SqlSessionTemplate
		 * @param sqlSessionFactory
		 */
		public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
			this.sqlSessionFactory = sqlSessionFactory;
		}
		/**
		 * 获取sqlSessionFactory
		 * @return
		 */
		public SqlSessionFactory getSqlSessionFactory(){
			return this.sqlSessionFactory;
		}
		/**
		 * 执行操作的模版方法
		 * @param action
		 * @return
		 */
		public Object execute(SqlSessionCallback action){
			SqlSession session = null;
			try{
				session = sqlSessionFactory.openSession();
				Object result = action.doInSession(session);
				return result;
			}
			finally{
				if (session != null)
					session.close();
			}
		}

		/**
		 * 插入对象。
		 * @param statement	
		 * @param parameter
		 * @return
		 */
		public int insert(final String statement, final Object parameter){
			return (Integer) execute(new SqlSessionCallback(){
				public Object doInSession(SqlSession session){
					return session.insert(statement, parameter);
				}
			});
		}

		/**
		 * 删除对象。
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public int delete(final String statement, final Object parameter){
			return (Integer) execute(new SqlSessionCallback(){
				public Object doInSession(SqlSession session){
					return session.delete(statement, parameter);
				}
			});
		}

		/**
		 *  更新对象。
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public int update(final String statement, final Object parameter){
			return (Integer) execute(new SqlSessionCallback(){
				public Object doInSession(SqlSession session){
					return session.update(statement, parameter);
				}
			});
		}

		/**
		 * 查询单条记录。
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public Object selectOne(final String statement, final Object parameter){
			return execute(new SqlSessionCallback(){
				public Object doInSession(SqlSession session){
					return session.selectOne(statement, parameter);
				}
			});
		}
		
		/**
		 * 查询列表。
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public List selectList(final String statement, final Object parameter){
			return (List) execute(new SqlSessionCallback(){
				public Object doInSession(SqlSession session){
					return session.selectList(statement, parameter);
				}
			});
		}
		
		/**
		 * 查询列表。
		 * @param statement
		 * @return
		 */
		public List selectList(final String statement){
			return (List) execute(new SqlSessionCallback(){
				public Object doInSession(SqlSession session){
					return session.selectList(statement);
				}
			});
		}
		
		/**
		 * 分页查询列表。
		 * @param statement
		 * @param parameter
		 * @param offset
		 * @param limit
		 * @return
		 */
		public List selectList(final String statement, final Object parameter, final int offset, final int limit)
		{
			return (List) execute(new SqlSessionCallback()
			{
				public Object doInSession(SqlSession session)
				{
					return session.selectList(statement, parameter, new RowBounds(offset, limit));
				}
			});
		}
	}

	/**
	 * SqlSession 模版接口。 
	 * @author hotent
	 *
	 */
	public static interface SqlSessionCallback
	{
		public Object doInSession(SqlSession session);
	}
}
