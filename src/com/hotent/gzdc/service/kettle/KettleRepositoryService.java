package com.hotent.gzdc.service.kettle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.gzdc.dao.kettle.KettleRepositoryDao;
import com.hotent.gzdc.kettle.qrtz.util.KettleConstants;
import com.hotent.gzdc.model.kettle.KettleRepository;

/**
 * <pre>
 * 对象功能:KETTLE_REPOSITORY Service类
 * 开发公司:从兴技术有限公司
 * 开发人员:张宇清
 * 创建时间:2014-01-10 09:31:32
 * </pre>
 */
@Service
public class KettleRepositoryService extends BaseService<KettleRepository> {
	private static List<Object> activeTrans = new ArrayList();
	private static List<Object> activeJobs = new ArrayList();
	
	@Resource
	private KettleRepositoryDao dao;

	public KettleRepositoryService() {
	}

	@Override
	protected IEntityDao<KettleRepository, Long> getEntityDao() {
		return dao;
	}

	/**
	 * <p>Description: 创建资源库<p>
	 * @param kettleRepository
	 * @param update
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void createRepository(KettleRepository kettleRepository, boolean update)
			throws Exception {
		KettleEnvironment.init();
		List createSql = new ArrayList();
		Class databaseMetaClass = Class.forName("org.pentaho.di.core.database.DatabaseMeta");
		Constructor databaseMetaConstructor = databaseMetaClass.getConstructor(new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class});
		Object databaseMeta = databaseMetaConstructor.newInstance(new Object[]{
				kettleRepository.getCRepositoryName(),
				kettleRepository.getCDbType(), kettleRepository.getCDbAccess(),
				kettleRepository.getCDbHost(), kettleRepository.getCDbName(),
				kettleRepository.getCDbPort(), kettleRepository.getCUserName(),
				kettleRepository.getCPassword()});

		Class repositoryMetaClass = Class.forName("org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta");
		Constructor repositoryMetaConstructor = repositoryMetaClass.getConstructor(new Class[0]);
		Object repositoryMeta = repositoryMetaConstructor.newInstance(new Object[0]);

		Method setNameMethod = repositoryMeta.getClass().getDeclaredMethod("setName", new Class[]{String.class});
		Method setConnectionMethod = repositoryMeta.getClass().getDeclaredMethod("setConnection", new Class[]{Class.forName("org.pentaho.di.core.database.DatabaseMeta")});
		setNameMethod.invoke(repositoryMeta, new Object[]{kettleRepository.getCRepositoryName()});
		setConnectionMethod.invoke(repositoryMeta, new Object[]{databaseMeta});
		Class kettleDatabaseRepositoryClass = Class.forName("org.pentaho.di.repository.kdr.KettleDatabaseRepository");
		Constructor kettleDatabaseRepositoryConstructor = kettleDatabaseRepositoryClass.getConstructor(new Class[0]);
		Object kettleDatabaseRepository = kettleDatabaseRepositoryConstructor.newInstance(new Object[0]);
		Method kdbRepinit = kettleDatabaseRepository.getClass().getDeclaredMethod("init", new Class[]{Class.forName("org.pentaho.di.repository.RepositoryMeta")});
		kdbRepinit.invoke(kettleDatabaseRepository, new Object[]{repositoryMeta});

		Method getDatabase = kettleDatabaseRepository.getClass().getDeclaredMethod("getDatabase", new Class[0]);

		Object database = getDatabase.invoke(kettleDatabaseRepository, new Object[0]);
		Method connect = database.getClass().getDeclaredMethod("connect", new Class[0]);
		connect.invoke(database, new Object[0]);

		Method createRepositorySchema = kettleDatabaseRepository.getClass().getDeclaredMethod("createRepositorySchema", new Class[]{Class.forName("org.pentaho.di.core.ProgressMonitorListener"), Boolean.TYPE, List.class, Boolean.TYPE});
		createRepositorySchema.invoke(kettleDatabaseRepository, new Object[]{null, Boolean.valueOf(update), createSql, Boolean.valueOf(true)});

		String statement = "";
		if ((!update) || (!checkTableExist(kettleRepository, KettleConstants.KETTLE_USER_RESOURCE_TABLE_NAME))) {
			statement = "CREATE TABLE " + KettleConstants.KETTLE_USER_RESOURCE_TABLE_NAME + " (C_USER_ID INTEGER,C_RESOURCE_ID INTEGER,C_RESOURCE_TYPE_ID  INTEGER);";
		}
		if ((!update) || (!checkTableExist(kettleRepository, KettleConstants.KETTLE_CACHE_FILE_TABLE_NAME))) {
			statement = statement + "CREATE TABLE " + KettleConstants.KETTLE_CACHE_FILE_TABLE_NAME + " (ID_CACHE_FILE INTEGER,NAME VARCHAR(255),FILEPATH VARCHAR(255),MEMORYSIZE INTEGER,INDEXTYPE INTEGER);";
		}
		for (Object object : createSql) {
			statement = statement + object.toString() + ";";
		}
		Method executeStatement = database.getClass().getDeclaredMethod("execStatements", new Class[]{String.class});
		executeStatement.invoke(database, new Object[]{statement});
		Method disconnect = database.getClass().getDeclaredMethod("disconnect", new Class[0]);
		disconnect.invoke(database, new Object[0]);
	}

	// 增加新表格
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addNewTables(KettleRepository kettleRepository) throws Exception {
		KettleEnvironment.init();
		Class databaseMetaClass = Class.forName("org.pentaho.di.core.database.DatabaseMeta");
		Constructor databaseMetaConstructor = databaseMetaClass
				.getConstructor(new Class[]{String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, String.class});
		Object databaseMeta = databaseMetaConstructor.newInstance(new Object[]{
				kettleRepository.getCRepositoryName(),
				kettleRepository.getCDbType(), kettleRepository.getCDbAccess(),
				kettleRepository.getCDbHost(), kettleRepository.getCDbName(),
				kettleRepository.getCDbPort(), kettleRepository.getCUserName(),
				kettleRepository.getCPassword()});

		Class databaseClass = Class
				.forName("org.pentaho.di.core.database.Database");
		Constructor databaseConstructor = databaseClass
				.getConstructor(new Class[]{Class
						.forName("org.pentaho.di.core.database.DatabaseMeta")});
		Object database = databaseConstructor
				.newInstance(new Object[]{databaseMeta});
		Method connect = database.getClass().getDeclaredMethod("connect",
				new Class[0]);
		connect.invoke(database, new Object[0]);
		String statement = "";
		if (!checkTableExist(kettleRepository, KettleConstants.KETTLE_USER_RESOURCE_TABLE_NAME)) {
			statement = "CREATE TABLE " + KettleConstants.KETTLE_USER_RESOURCE_TABLE_NAME + " (C_USER_ID INTEGER,C_RESOURCE_ID INTEGER,C_RESOURCE_TYPE_ID  INTEGER);";
		}

		if (!checkTableExist(kettleRepository, KettleConstants.KETTLE_CACHE_FILE_TABLE_NAME)) {
			statement = statement
					+ "CREATE TABLE " + KettleConstants.KETTLE_CACHE_FILE_TABLE_NAME + " (ID_CACHE_FILE INTEGER,NAME VARCHAR(255),FILEPATH VARCHAR(255),MEMORYSIZE INTEGER,INDEXTYPE INTEGER);";
		}
		if (!"".equals(statement)) {
			Method executeStatement = database.getClass().getDeclaredMethod(
					"execStatements", new Class[]{String.class});
			executeStatement.invoke(database, new Object[]{statement});
		}
		Method disconnect = database.getClass().getDeclaredMethod("disconnect",
				new Class[0]);
		disconnect.invoke(database, new Object[0]);

	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private boolean checkTableExist(KettleRepository kettleRepository,
			String tableName) {
		boolean isExist = true;
		try {
			Class databaseMetaClass = Class
					.forName("org.pentaho.di.core.database.DatabaseMeta");
			Constructor databaseMetaConstructor = databaseMetaClass
					.getConstructor(new Class[]{String.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class});
			Object databaseMeta = databaseMetaConstructor
					.newInstance(new Object[]{
							kettleRepository.getCRepositoryName(),
							kettleRepository.getCDbType(),
							kettleRepository.getCDbAccess(),
							kettleRepository.getCDbHost(),
							kettleRepository.getCDbName(),
							kettleRepository.getCDbPort(),
							kettleRepository.getCUserName(),
							kettleRepository.getCPassword()});

			Class databaseClass = Class
					.forName("org.pentaho.di.core.database.Database");
			Constructor databaseConstructor = databaseClass
					.getConstructor(new Class[]{Class
							.forName("org.pentaho.di.core.database.DatabaseMeta")});
			Object database = databaseConstructor
					.newInstance(new Object[]{databaseMeta});
			Method connect = database.getClass().getDeclaredMethod("connect",
					new Class[0]);
			connect.invoke(database, new Object[0]);
			Method checkTableExist = database.getClass().getDeclaredMethod(
					"checkTableExists", new Class[]{String.class});
			isExist = ((Boolean) checkTableExist.invoke(database,
					new Object[]{tableName})).booleanValue();
			Method disconnect = database.getClass().getDeclaredMethod(
					"disconnect", new Class[0]);
			disconnect.invoke(database, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}
	
	public String getRepTreeJSON(String repName, String user_id) {
		try {
			KettleEnvironment.init();
		} catch (KettleException e2) {
			e2.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");

		Object rep = null;
		Method disconnect = null;
		boolean connected = false;
		try {
			rep = getRepositoryByName(repName);
			connected = true;

			String authResourceIDs = getAuthResourceIDs(rep, user_id);

			Object rootDirectory = getDirectory(rep, "/");

			boolean isAdmin = true;
			Long rootParentId = -1L;
			String filePath = "/";
			getRepTreeJSON(rep, rootDirectory, rootParentId, filePath, sb, authResourceIDs.split(","),
					isAdmin);

			disconnect = rep.getClass().getDeclaredMethod("disconnect",
					new Class[0]);
			disconnect.invoke(rep, new Object[0]);
			connected = false;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (connected) {
					disconnect = rep.getClass().getDeclaredMethod("disconnect",
							new Class[0]);
					disconnect.invoke(rep, new Object[0]);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (connected) {
					disconnect = rep.getClass().getDeclaredMethod("disconnect",
							new Class[0]);
					disconnect.invoke(rep, new Object[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		sb.append("]");
		return sb.toString();
	}
	
	private String getRepTreeJSON(Object rep, Object repDirectory, Long parentId, String filePath, StringBuffer sb, String[] authedResourceIDs, boolean isAdmin)
	  {
	    try
	    {
	      Class longObjectIdClass = Class.forName("org.pentaho.di.repository.LongObjectId");
	      Method getId = longObjectIdClass.getDeclaredMethod("getId", new Class[0]);
	      Method getObjectId = repDirectory.getClass().getDeclaredMethod("getObjectId", new Class[0]);
	      Object longObjectId = getObjectId.invoke(repDirectory, new Object[0]);
	      Object directoryID = getId.invoke(longObjectId, new Object[0]);
	      Method getDirectoryName = repDirectory.getClass().getDeclaredMethod("getName", new Class[0]);
	      String directoryName = (String)getDirectoryName.invoke(repDirectory, new Object[0]);
	      	//如果当前目录不是根目录
			if (!directoryName.equals("/")) {
				if (filePath.equals("/")) {
					filePath = filePath + directoryName;
				}else{
					filePath = filePath + "/" + directoryName;
				}
			}

	      Method getNrSubdirectories = repDirectory.getClass().getDeclaredMethod("getNrSubdirectories", new Class[0]);
	      int subDirCount = ((Integer)getNrSubdirectories.invoke(repDirectory, new Object[0])).intValue();

	      Method getTransformationNames = rep.getClass().getDeclaredMethod("getTransformationNames", new Class[] { 
	        Class.forName("org.pentaho.di.repository.ObjectId"), Boolean.TYPE });
	      String[] transNames = (String[])getTransformationNames.invoke(rep, new Object[] { longObjectId, Boolean.valueOf(false) });
	      Method getJobNames = rep.getClass().getDeclaredMethod("getJobNames", new Class[] { 
	        Class.forName("org.pentaho.di.repository.ObjectId"), Boolean.TYPE });
	      String[] jobNames = (String[])getJobNames.invoke(rep, new Object[] { longObjectId, Boolean.valueOf(false) });

	      if (subDirCount + transNames.length + jobNames.length > 0) {
	        StringBuffer sb_objs = new StringBuffer();

	        for (int i = 0; i < subDirCount; i++) {
	          StringBuffer sb_subs = new StringBuffer();
	          Method getSubdirectory = repDirectory.getClass().getDeclaredMethod("getSubdirectory", new Class[] { Integer.TYPE });
	          Object subDirectory = getSubdirectory.invoke(repDirectory, new Object[] { Integer.valueOf(i) });
	          getRepTreeJSON(rep, subDirectory, Long.parseLong(directoryID.toString()), filePath, sb_subs, authedResourceIDs, isAdmin);
	          if ((sb_subs.length() > 0) && (sb_objs.length() > 0))
	            sb_objs.append(",")
	              .append(sb_subs);
	          else {
	            sb_objs.append(sb_subs);
	          }
	        }
				for (int i = 0; i < transNames.length; i++) {
					Method getTransformationID = rep.getClass().getDeclaredMethod("getTransformationID", new Class[]{String.class, Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface")});
					Object transLongObjectID = getTransformationID.invoke(rep, new Object[]{transNames[i], repDirectory});
					Object transID = getId.invoke(transLongObjectID, new Object[0]);
					if (!isAdmin) {
						for (String authedResourceId : authedResourceIDs)
							if ((transID.toString() + "_trans").equals(authedResourceId)) {
								if (sb_objs.length() > 0) {
									sb_objs.append(",");
								}
								sb_objs.append("{")
										.append("id:'" + transID + "_trans"+ "'")
										.append(",text:'" + transNames[i] + "["+ "ktr" + "]'")
										.append(",leaf:true")
										.append(",parentId:")
										.append(Long.parseLong(directoryID.toString()))// 如果存在元素时　使用当前目录为父parentId
										.append(",filePath:'" + filePath + "'")
										.append(",fileName:'" + transNames[i]+ "'")
										.append(",fileType:'ktr'").append("}");
								break;
							}
					} else {
						if (sb_objs.length() > 0) {
							sb_objs.append(",");
						}
						sb_objs.append("{")
								.append("id:'" + transID + "_trans" + "'")
								.append(",text:'" + transNames[i] + "[" + "ktr"+ "]'")
								.append(",leaf:true")
								.append(",parentId:")
								.append(Long.parseLong(directoryID.toString()))// 如果存在元素时　使用当前目录为父parentId
								.append(",filePath:'" + filePath + "'")
								.append(",fileName:'" + transNames[i] + "'")
								.append(",fileType:'ktr'").append("}");
					}
				}
				for (int i = 0; i < jobNames.length; i++) {
					Method getJobId = rep.getClass().getDeclaredMethod("getJobId", new Class[]{String.class, Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface")});
					Object jobObjectID = getJobId.invoke(rep, new Object[]{jobNames[i], repDirectory});
					Object jobID = getId.invoke(jobObjectID, new Object[0]);
					if (!isAdmin) {
						for (String authedResourceId : authedResourceIDs)
							if ((jobID.toString() + "_job").equals(authedResourceId)) {
								if (sb_objs.length() > 0) {
									sb_objs.append(",");
								}
								sb_objs.append("{")
										.append("id:'" + jobID + "_job" + "'")
										.append(",text:'" + jobNames[i] + "["+ "kjb" + "]'")
										.append(",leaf:true")
										.append(",parentId:")
										.append(Long.parseLong(directoryID.toString()))// 如果存在元素时　使用当前目录为父parentId
										.append(",filePath:'" + filePath + "'")
										.append(",fileName:'" + jobNames[i]+ "'")
										.append(",fileType:'kjb'").append("}");
								break;
							}
					} else {
						if (sb_objs.length() > 0) {
							sb_objs.append(",");
						}
						sb_objs.append("{")
								.append("id:'" + jobID + "_job" + "'")
								.append(",text:'" + jobNames[i] + "[" + "kjb"+ "]'")
								.append(",leaf:true")
								.append(",parentId:")
								.append(Long.parseLong(directoryID.toString()))// 如果存在元素时　使用当前目录为父parentId
								.append(",filePath:'" + filePath + "'")
								.append(",fileName:'" + jobNames[i] + "'")
								.append(",fileType:'kjb'").append("}");
					}
				}

				if (sb_objs.length() > 0)
					sb.append("{").append("id:'" + directoryID + "'")
							.append(",text:'" + directoryName + "'")
							.append(",expanded:true,leaf:false")
							.append(",parentId:").append(parentId)
							.append(",filePath:'" + filePath + "'").append("}")
							.append(",").append(sb_objs);

			}
		}
	    catch (Exception e) {
	    	e.printStackTrace();
	    }

	    return sb.toString();
	  }
	
	public Object getRepsMetaFromDatabase()
	  {
	    Object repsMeta = new Object();
	    try
	    {
	      Class repositoriesMetaClass = Class.forName("org.pentaho.di.repository.RepositoriesMeta");
	      Class databaseMetaClass = Class.forName("org.pentaho.di.core.database.DatabaseMeta");
	      Class repositoryMetaClass = Class.forName("org.pentaho.di.repository.RepositoryMeta");
	      Class kettleDatabaseRepositoryMetaClass = Class.forName("org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta");
	      Constructor repositoriesMetaConstructor = repositoriesMetaClass.getConstructor(new Class[0]);
	      Constructor databaseMetaConstructor = databaseMetaClass.getConstructor(new Class[] { String.class, String.class, 
	        String.class, String.class, String.class, String.class, String.class, String.class });
	      Constructor kettleDatabaseRepositoryMetaConstructor = kettleDatabaseRepositoryMetaClass.getConstructor(new Class[] { String.class, 
	        String.class, String.class, databaseMetaClass });
	      repsMeta = repositoriesMetaConstructor.newInstance(new Object[0]);
	      Method addDatabase = repositoriesMetaClass.getDeclaredMethod("addDatabase", new Class[] { databaseMetaClass });
	      Method addRepository = repositoriesMetaClass.getDeclaredMethod("addRepository", new Class[] { repositoryMetaClass });

	      List<KettleRepository> list = dao.getAll();
	      for (KettleRepository kettleRepository : list) {
	    	  Object databaseMeta = databaseMetaConstructor.newInstance(new Object[] { kettleRepository.getCRepositoryName(), kettleRepository.getCDbType(), 
	    			  kettleRepository.getCDbAccess(), kettleRepository.getCDbHost(), kettleRepository.getCDbName(), kettleRepository.getCDbPort(), kettleRepository.getCUserName(), kettleRepository.getCPassword() });
	    	        Object repositoryMeta = kettleDatabaseRepositoryMetaConstructor.newInstance(new Object[] { String.valueOf(kettleRepository.getCRepositoryId()), kettleRepository.getCRepositoryName(), 
	    	        		kettleRepository.getCDbType(), databaseMeta });
	    	        addDatabase.invoke(repsMeta, new Object[] { databaseMeta });
	    	        addRepository.invoke(repsMeta, new Object[] { repositoryMeta });
		}
	      
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return repsMeta;
	  }
	
	/**
	 * <p>Description: <p>
	 * @param repName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Object getRepositoryByName(String repName) throws Exception {
		KettleEnvironment.init();
		Class repositoriesMetaClass = Class.forName("org.pentaho.di.repository.RepositoriesMeta");
		Method findRepository = repositoriesMetaClass.getDeclaredMethod("findRepository", new Class[]{String.class});

		Object repsMeta = getRepsMetaFromDatabase();

		Object repMeta = findRepository.invoke(repsMeta, new Object[]{repName});

		Class repositoryClass = Class.forName("org.pentaho.di.repository.Repository");
		
		Class pluginRegistryClass = Class.forName("org.pentaho.di.core.plugins.PluginRegistry");
		Method getInstance = pluginRegistryClass.getDeclaredMethod("getInstance", new Class[0]);
		Method loadClass = pluginRegistryClass.getDeclaredMethod("loadClass", new Class[]{Class.class, Object.class, Class.class});
		Method init = repositoryClass.getDeclaredMethod("init", new Class[]{Class.forName("org.pentaho.di.repository.RepositoryMeta")});
		Object pluginRegistry = getInstance.invoke(pluginRegistryClass, new Object[0]);

		Object rep = loadClass.invoke(pluginRegistry, new Object[]{Class.forName("org.pentaho.di.core.plugins.RepositoryPluginType"), repMeta, repositoryClass});

		init.invoke(rep, new Object[]{repMeta});

		Method connect = repositoryClass.getDeclaredMethod("connect", new Class[]{String.class, String.class});
		connect.invoke(rep, new Object[]{KettleConstants.get("LoginUser"), KettleConstants.get("LoginPassword")});
		return rep;
	}
	
	/**
	 * <p>Description: 查找目录<p>
	 * @param repository 资源库
	 * @param filePath 文件路径
	 * @return
	 * @throws Exception
	 */
	public Object getDirectory(Object repository, String filePath) throws Exception {
		Method loadRepositoryDirectoryTree = repository.getClass().getDeclaredMethod("loadRepositoryDirectoryTree", new Class[0]);
		Object directory = loadRepositoryDirectoryTree.invoke(repository, new Object[0]);

		if ((filePath != null) && (!"/".equals(filePath)) && (!"".equals(filePath))) {
			Method findDirectory = directory.getClass().getDeclaredMethod("findDirectory", new Class[]{String.class});
			directory = findDirectory.invoke(directory, new Object[]{filePath});
		}

		return directory;
	}
	
	/**
	 * <p>Description: 获取用户对应资源库的资源ID<p>
	 * @param repository
	 * @param user_id
	 * @return
	 */
	private String getAuthResourceIDs(Object repository, String user_id) {
		StringBuffer sb = new StringBuffer(1024);
		String query_sql = "select * from " + KettleConstants.KETTLE_USER_RESOURCE_TABLE_NAME + " where C_USER_ID = " + user_id;
		try {
			Method getDatabase = repository.getClass().getDeclaredMethod("getDatabase", new Class[0]);
			Object database = getDatabase.invoke(repository, new Object[0]);
			Method getConnection = database.getClass().getDeclaredMethod("getConnection", new Class[0]);
			Connection conn = (Connection) getConnection.invoke(database, new Object[0]);
			
			try {
				Statement stt = conn.createStatement();
				ResultSet rs = stt.executeQuery(query_sql);

				int count = 0;
				while (rs.next()) {
					if (count > 0){
						sb.append(",");
					}
					int resourceID = rs.getInt("C_RESOURCE_ID");
					int resource_type_ID = rs.getInt("C_RESOURCE_TYPE_ID");
					sb.append(getResourceID(Integer.valueOf(resourceID), resource_type_ID));
					count++;
				}

				rs.close();
				stt.close();
			} catch (SQLException e) {
				System.err.println(query_sql);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	/**
	 * <p>Description: 根据资源ID和类型获取详细类型<p>
	 * @param resourceID
	 * @param resource_type
	 * @return
	 */
	private String getResourceID(Object resourceID, int resource_type) {
		switch (resource_type) {
			case 3 :
				return resourceID + "_trans";
			case 2 :
				return resourceID + "_job";
			case 1 :
				return resourceID + "_dir";
		}
		return "";
	}

	/**
	 * <p>Description: 根据资源库名称获取对象<p>
	 * @param repositoryName
	 * @return
	 */
	public KettleRepository getByName(String repositoryName) {
		KettleRepository kettleRespository = null;
		String getStatement= KettleRepository.class.getName() + ".getByName";
		kettleRespository = (KettleRepository) dao.getSqlSessionTemplate().selectOne(getStatement, repositoryName);
		return kettleRespository;
	}
	
	 /**
	 * <p>Description: 获取资源库资源数据XML<p>
	 * @param reppositoryName
	 * @param fileType
	 * @param actionPath
	 * @param actionRef
	 * @return
	 */
	public String getDataXML(String reppositoryName, String fileType, String actionPath, String actionRef)
	  {
	    String xml = "";
	    Method disconnect = null;
	    Object repository = null;
	    boolean connected = false;
	    try
	    {
	      KettleEnvironment.init();
	      repository = getRepositoryByName(reppositoryName);
	      connected = true;

	      Object directory = getDirectory(repository, actionPath);

	      //转换
	      if (fileType.equalsIgnoreCase("ktr")){
	    	  Method loadTransformation = repository.getClass().getDeclaredMethod("loadTransformation", new Class[] { String.class, Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface"), Class.forName("org.pentaho.di.core.ProgressMonitorListener"),  Boolean.TYPE, String.class });
	    	  Object transMeta = loadTransformation.invoke(repository, new Object[] { actionRef, directory, null, Boolean.valueOf(true), null });

	    	  Method getXML = transMeta.getClass().getDeclaredMethod("getXML", new Class[0]);
	    	  xml = (String)getXML.invoke(transMeta, new Object[0]);
	      }else if (fileType.equalsIgnoreCase("kjb")){//作业
	    	  Method loadJob = repository.getClass().getDeclaredMethod("loadJob", new Class[] { String.class, 
	          Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface"), 
	          Class.forName("org.pentaho.di.core.ProgressMonitorListener"), String.class });
	    	  Object jobMeta = loadJob.invoke(repository, new Object[] { actionRef, directory, null, null });

	    	  Method getXML = jobMeta.getClass().getDeclaredMethod("getXML", new Class[0]);
	    	  xml = (String)getXML.invoke(jobMeta, new Object[0]);
	      }
	      xml = xml.replaceAll("\r", "");
	      xml = xml.replaceAll("\n", "");
	      disconnect = repository.getClass().getDeclaredMethod("disconnect", new Class[0]);
	      disconnect.invoke(repository, new Object[0]);
	      connected = false;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	try{
		        if (connected) {
		          disconnect = repository.getClass().getDeclaredMethod("disconnect", new Class[0]);
		          disconnect.invoke(repository, new Object[0]);
		        }
	      } catch (Exception e2) {
	    	  e.printStackTrace();
	      }
	    } finally {
	    	try {
	    		if (connected) {
	    			disconnect = repository.getClass().getDeclaredMethod("disconnect", new Class[0]);
	    			disconnect.invoke(repository, new Object[0]);
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    return xml;
	  }

	@SuppressWarnings({"unchecked", "rawtypes"})
	public String getActiveDetails(String repositoryName, String fileType, String actionPath, String actionRef) {
		StringBuffer statusJSON = new StringBuffer();
		statusJSON.append("{objs:[");
		try {
			Iterator localIterator;
			if (fileType.equalsIgnoreCase("ktr")) {//转换
				for (localIterator = activeTrans.iterator(); localIterator.hasNext();) {
					Object trans = localIterator.next();

					Method transMetaMethod = trans.getClass().getDeclaredMethod("getTransMeta", new Class[0]);
					Object transMeta = transMetaMethod.invoke(trans, new Object[0]);
					Method repositoryMethod = transMeta.getClass().getDeclaredMethod("getRepository", new Class[0]);
					Object repository = repositoryMethod.invoke(transMeta, new Object[0]);
					Class repositoryClass = Class.forName("org.pentaho.di.repository.Repository");
					Method repNameMethod = repositoryClass.getDeclaredMethod("getName", new Class[0]);
					String trans_repName = (String) repNameMethod.invoke(repository, new Object[0]);
					if (trans_repName.equals(repositoryName)) {
						Method repositoryDirectoryMethod = transMeta.getClass().getDeclaredMethod("getRepositoryDirectory", new Class[0]);
						Object repositoryDirectory = repositoryDirectoryMethod.invoke(transMeta, new Object[0]);
						Class repositoryDirectotyClass = Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface");
						Method directotyNameMethod = repositoryDirectotyClass.getDeclaredMethod("getPath", new Class[0]);
						String trans_dictName = (String) directotyNameMethod.invoke(repositoryDirectory, new Object[0]);
						if (trans_dictName.equals(actionPath)) {
							Method transNameMethod = trans.getClass().getDeclaredMethod("getName", new Class[0]);
							String trans_name = (String) transNameMethod.invoke(trans, new Object[0]);
							if (trans_name.equals(actionRef)) {
								Method transStepExecutionStatusLookupMethod = trans.getClass().getDeclaredMethod("getTransStepExecutionStatusLookup", new Class[0]);
								Object[] statusLookup = (Object[]) transStepExecutionStatusLookupMethod.invoke(trans, new Object[0]);

								Method stepsMethod = trans.getClass().getDeclaredMethod("getSteps", new Class[0]);
								List steps = (List) stepsMethod.invoke(trans, new Object[0]);
								for (int i = 0; i < steps.size(); i++) {
									int state = 0;
									Object step = steps.get(i);
									Field stepNameField = step.getClass().getDeclaredField("stepname");
									String stepName = (String) stepNameField.get(step);

									Enum stepStatus = (Enum) statusLookup[i];
									String stateName = stepStatus.name();
									if ("STATUS_RUNNING".equals(stateName)){
										state = 1;
									} else if ("STATUS_FINISHED".equals(stateName)){
										state = 2;
									} else if ("STATUS_STOPPED".equals(stateName)){
										state = 0;
									} else {
										state = 3;
									}

									if (i > 0) {
										statusJSON.append(",");
									}

									statusJSON.append("{name:'").append(stepName).append("',state:'").append(state).append("'}");
								}
							}
						}
					}
				}
			} else if (fileType.equalsIgnoreCase("kjb")) {//作业
				for (localIterator = activeJobs.iterator(); localIterator.hasNext();) {
					Object job = localIterator.next();
					Method repositoryMethod = job.getClass().getDeclaredMethod("getRep", new Class[0]);
					Object repository = repositoryMethod.invoke(job, new Object[0]);
					Class repositoryClass = Class.forName("org.pentaho.di.repository.Repository");
					Method repositoryNameMethod = repositoryClass.getDeclaredMethod("getName", new Class[0]);
					String job_repositoryName = (String) repositoryNameMethod.invoke(repository, new Object[0]);
					if (job_repositoryName.equals(repositoryName)) {
						Method jobMetaMethod = job.getClass().getDeclaredMethod("getJobMeta", new Class[0]);
						Object jobMeta = jobMetaMethod.invoke(job, new Object[0]);
						Method repositoryDirectotyMethod = jobMeta.getClass().getDeclaredMethod("getRepositoryDirectory", new Class[0]);
						Object repositoryDirectoty = repositoryDirectotyMethod.invoke(jobMeta, new Object[0]);
						Class repositoryDirectotyClass = Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface");
						Method directotyNameMethod = repositoryDirectotyClass.getDeclaredMethod("getPath", new Class[0]);
						String job_dictName = (String) directotyNameMethod.invoke(repositoryDirectoty, new Object[0]);
						if (job_dictName.equals(actionPath)) {
							Method jobNameMethod = job.getClass().getDeclaredMethod("getJobname", new Class[0]);
							String job_name = (String) jobNameMethod.invoke(job, new Object[0]);
							if (job_name.equals(actionRef)) {
								Method activeJobEntryTransformationsMethod = job.getClass().getDeclaredMethod("getActiveJobEntryTransformations", new Class[0]);
								Map activeJobEntryTrans = (Map) activeJobEntryTransformationsMethod.invoke(job, new Object[0]);
								Method activeJobEntryJobsMethod = job.getClass().getDeclaredMethod("getActiveJobEntryJobs", new Class[0]);
								Map activeJobEntryJobs = (Map) activeJobEntryJobsMethod.invoke(job, new Object[0]);

								Method jobEntryResultsMethod = job.getClass().getDeclaredMethod("getJobEntryResults", new Class[0]);
								List jobEntryResults = (List) jobEntryResultsMethod.invoke(job, new Object[0]);

								Method jobCopiesMethod = jobMeta.getClass().getDeclaredMethod("getJobCopies", new Class[0]);
								List jobCopies = (List) jobCopiesMethod.invoke(jobMeta, new Object[0]);

								for (int i = 0; i < jobCopies.size(); i++) {
									int state = 0;
									Method entryNameMethod = jobCopies.get(i).getClass().getDeclaredMethod("getName", new Class[0]);
									String entryName = (String) entryNameMethod.invoke(jobCopies.get(i), new Object[0]);

									for (int j = 0; j < jobEntryResults.size(); j++) {
										Object jobEntryResult = jobEntryResults.get(j);
										Method jobEntryNameMethod_ = jobEntryResult.getClass().getDeclaredMethod("getJobEntryName", new Class[0]);
										Object jobEntryName_ = jobEntryNameMethod_.invoke(jobEntryResult, new Object[0]);

										if ((jobEntryName_ != null) && (jobEntryName_.equals(entryName))) {
											state = 2;
											break;
										}
									}

									if (state == 0) {
										if ((activeJobEntryJobs != null) && (activeJobEntryJobs.get(jobCopies.get(i)) != null)) {
											state = 1;
										}

										if ((activeJobEntryTrans != null) && (activeJobEntryTrans.get(jobCopies.get(i)) != null)) {
											state = 1;
										}

									}

									if (i > 0) {
										statusJSON.append(",");
									}

									statusJSON.append("{name:'").append(entryName).append("',state:'").append(state).append("'}");
								}
							}
						}
					}
				}
			}

			statusJSON.append("]}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusJSON.toString();
	}


}
