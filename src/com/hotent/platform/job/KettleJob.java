/*
 * Copyright (C), 2011-2012, Sunrise Tech. Co., Ltd.
 * SUNRISE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hotent.platform.job;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.di.core.KettleEnvironment;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.mail.SimpleMailMessage;

import com.hotent.core.engine.MessageEngine;
import com.hotent.core.scheduler.BaseJob;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.gzdc.controller.kettle.QrtzTriggersController;
import com.hotent.gzdc.model.kettle.KettleMonitor;
import com.hotent.gzdc.service.kettle.KettleRepositoryService;
import com.hotent.gzdc.service.kettle.KettleMonitorService;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.system.SysUserService;

public class KettleJob extends BaseJob{
	public static final String STATUS_RUNNING = "RUNING";

	public static final String STATUS_FINISHED = "FINISHED";

	public static final String STATUS_ERROR = "ERROR";	  
	  
	private Log logger = LogFactory.getLog(KettleJob.class);
	
	private KettleRepositoryService kditRepositoryService;
	
	private KettleMonitorService kettleMonitorService;

	private KettleMonitor kettleMonitor = null;
	
	private static List<Object> activeTrans = new ArrayList();
	private static List<Object> activeJobs = new ArrayList();

	@Override
	public void executeJob(JobExecutionContext context) throws Exception {
		String jobName = null;
		String errorNoticeUserId = null;
		try {
			KettleEnvironment.init();
			kditRepositoryService = (KettleRepositoryService) AppUtil.getBean(KettleRepositoryService.class);
			kettleMonitorService = (KettleMonitorService) AppUtil.getBean(KettleMonitorService.class);
			JobDetail jobDetail = context.getJobDetail();
		    JobDataMap data = jobDetail.getJobDataMap();
		    jobName = data.getString("jobName");
		    String actionRef = data.getString("actionRef");
		    String actionPath = data.getString("actionPath");
		    String fileType = data.getString("fileType");
		    String repositoryName = data.getString("repName");
		    errorNoticeUserId = data.getString("errorNoticeUserId");
		    int execType = 1;
		    String execTypeStr = data.getString("execType");
		    if (StringUtils.isNotEmpty(execTypeStr)) {
				execType = Integer.parseInt(execTypeStr);
			}
		    
		    String jobFile =actionPath + "/" + actionRef + "." + fileType;
		    String remoteServer = data.getString("remoteServer");
		    String ha = data.getString("ha");
		    kettleMonitor = new KettleMonitor();
		    kettleMonitor.setId(UniqueIdUtil.genId());
		    kettleMonitor.setJobname(jobName);
		    kettleMonitor.setJobgroup(QrtzTriggersController.KETTLE_GROUP_NAME);
		    kettleMonitor.setJobfile(jobFile);
		    kettleMonitorService.add(kettleMonitor);
		    
	    	execute(repositoryName, actionPath, actionRef, fileType, execType, remoteServer, ha);
		} catch (Exception e) {
			//获取错误信息更新到监控表中
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String errMsg = sw.toString();
			kettleMonitor.setErrmsg(errMsg);
			kettleMonitorService.update(kettleMonitor);
			
			if (StringUtils.isNotEmpty(errorNoticeUserId)) {
				SysUserService sysUserService = (SysUserService) AppUtil.getBean(SysUserService.class);
				String[] errorNoticeUserIds = errorNoticeUserId.split(",");
				String noticeUserMailStr = "";
				for (String userId : errorNoticeUserIds) {
					SysUser sysUser = sysUserService.getById(Long.parseLong(userId));
					String email = sysUser.getEmail();
					if (StringUtils.isNotEmpty(email)) {
						noticeUserMailStr += "," + email;
					}
				}
				if (StringUtils.isNotEmpty(noticeUserMailStr)) {
					noticeUserMailStr = noticeUserMailStr.substring(1);
					String[] sendMailArr = noticeUserMailStr.split(",");
					MessageEngine messageEngine = new MessageEngine();
					SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
					String meailTitle = "[ScheduleError][" + StringUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "][" + jobName + "]";
					simpleMailMessage.setText(errMsg);
					simpleMailMessage.setTo(sendMailArr);
					simpleMailMessage.setSubject(meailTitle);
					messageEngine.setFromUser("zhangyuqing@revenco.com");
					messageEngine.sendMail(simpleMailMessage );
				}
			}
		}
		
	}

	private synchronized boolean execute(String repositoryName, String filePath, String fileName, String fileType, int execType, String remoteServer, String ha) throws Exception {
		 boolean success = false;
		 Object repository = null;
		    Method disconnect = null;
		    boolean connected = false;
		    try {
		      repository = kditRepositoryService.getRepositoryByName(repositoryName);
		      connected = true;

		      Object directory = kditRepositoryService.getDirectory(repository, filePath);

		      if (directory != null) {
		        if (fileType.equalsIgnoreCase("ktr")){//转换
		          Method loadTransformation = repository.getClass().getDeclaredMethod("loadTransformation", new Class[] { String.class, 
		            Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface"), Class.forName("org.pentaho.di.core.ProgressMonitorListener"), 
		            Boolean.TYPE, String.class });
		          Object transMeta = loadTransformation.invoke(repository, new Object[] { fileName, directory, null, Boolean.valueOf(true), null });

		          success = executeTrans(transMeta, null, null, execType, remoteServer, ha);
		        } else if (fileType.equalsIgnoreCase("kjb")){//job
		        	Method loadJob = repository.getClass().getDeclaredMethod("loadJob", new Class[] { String.class, 
		            Class.forName("org.pentaho.di.repository.RepositoryDirectoryInterface"), 
		            Class.forName("org.pentaho.di.core.ProgressMonitorListener"), String.class });
		        	Object jobMeta = loadJob.invoke(repository, new Object[] { fileName, directory, null, null });

		          success = executeJob(jobMeta, repository, null, null, execType, remoteServer, ha);
		        }
		      }

		      disconnect = repository.getClass().getDeclaredMethod("disconnect", new Class[0]);
		      disconnect.invoke(repository, new Object[0]);
		      connected = false;
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	throw e;
		    } finally {
		      try {
		        if (connected) {
		          disconnect = repository.getClass().getDeclaredMethod("disconnect", new Class[0]);
		          disconnect.invoke(repository, new Object[0]);
		        }
		      } catch (Exception e) {
		        logger.error(e.getMessage(), e);
		      }
		    }
		 return success;
	}
	
	/**
	 * <p>Description: <p>
	 * @param transMeta
	 * @param params
	 * @param prop
	 * @param monitor_id
	 * @param execType
	 * @param remoteServer
	 * @param ha
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked", "unused"})
	private boolean executeTrans(Object transMeta, String[] params, HashMap<?, ?> prop, int execType,
			String remoteServer, String ha) throws Exception {
		boolean success = false;
		Date startDate = new Date();
		String status = STATUS_FINISHED;
		String logChannelId = "";
		Object result = null;
		Object trans = null;
		Long id_server = 0L;
		try {
			String simpleObjectId = UUID.randomUUID().toString();

		      Class transMetaClass = Class.forName("org.pentaho.di.trans.TransMeta");
		      Class transClass = Class.forName("org.pentaho.di.trans.Trans");
		      Constructor transConstructor = transClass.getConstructor(new Class[] { transMetaClass });
		      trans = transConstructor.newInstance(new Object[] { transMeta });
		
		      if (1 == execType) {
		        Class simpleLoggingObjectClass = Class.forName("org.pentaho.di.core.logging.SimpleLoggingObject");
		        Class loggingObjectTypeClass = Class.forName("org.pentaho.di.core.logging.LoggingObjectType");
		        Class loggingObjectInterfaceClass = Class.forName("org.pentaho.di.core.logging.LoggingObjectInterface");
		        Constructor simpleLoggingObjectConstructor = simpleLoggingObjectClass.getConstructor(new Class[] { String.class, loggingObjectTypeClass, loggingObjectInterfaceClass });
		
		        Field jobField = loggingObjectTypeClass.getDeclaredField("JOB");
		        Object simpleLoggingObject = simpleLoggingObjectConstructor.newInstance(new Object[] { "SPOON", jobField.get(""), null });
		        Method setContainerObjectId = simpleLoggingObjectClass.getDeclaredMethod("setContainerObjectId", new Class[] { String.class });
		        setContainerObjectId.invoke(simpleLoggingObject, new Object[] { simpleObjectId });
		
		        Method setParent = transClass.getDeclaredMethod("setParent", new Class[] { loggingObjectInterfaceClass });
		        setParent.invoke(trans, new Object[] { simpleLoggingObject });
		
		        Method getLogChannel = transClass.getDeclaredMethod("getLogChannel", new Class[0]);
		        Object logChannel = getLogChannel.invoke(trans, new Object[0]);
		        Method logMinimal = logChannel.getClass().getDeclaredMethod("logMinimal", new Class[] { String.class, Object[].class });
		
		        Method getLogChannelId = transClass.getDeclaredMethod("getLogChannelId", new Class[0]);
		        logChannelId = (String)getLogChannelId.invoke(trans, new Object[0]);
		
		        logMinimal.invoke(logChannel, new Object[] { "ETL--TRANS Start of run", new Object[0] });
		
		        Method getArguments = transMetaClass.getDeclaredMethod("getArguments", new Class[0]);
		        Method prepareExecution = transClass.getDeclaredMethod("prepareExecution", new Class[] { String[].class });
		        prepareExecution.invoke(trans, new Object[] { getArguments.invoke(transMeta, new Object[0]) });
		
		        activeTrans.add(trans);
		
		        Method startThreads = transClass.getDeclaredMethod("startThreads", new Class[0]);
		        startThreads.invoke(trans, new Object[0]);
		        Method waitUntilFinished = transClass.getDeclaredMethod("waitUntilFinished", new Class[0]);
		        waitUntilFinished.invoke(trans, new Object[0]);
		
		        activeTrans.remove(trans);
		
		        logMinimal.invoke(logChannel, new Object[] { "ETL--TRANS Finished", new Object[0] });
		        Date stop = new Date();
		        logMinimal.invoke(logChannel, new Object[] { "ETL--TRANS Start=" + StringUtil.DateToString(startDate, "yyyy/MM/dd HH:mm:ss") + ", Stop=" + StringUtil.DateToString(stop, "yyyy/MM/dd HH:mm:ss"), new Object[0] });
		        long millis = stop.getTime() - startDate.getTime();
		        logMinimal.invoke(logChannel, new Object[] { "ETL--TRANS Processing ended after " + millis / 1000L + " seconds.", new Object[0] });
		
		        Method getResult = transClass.getDeclaredMethod("getResult", new Class[0]);
		        result = getResult.invoke(trans, new Object[0]);
		
		        success = true;
		      } 
		} catch (Exception e) {
			status = STATUS_FINISHED;
			e.printStackTrace();
			throw e;
		} finally {
			monitor(startDate, status, logChannelId, result, id_server);
			if ((trans != null) && (activeTrans.contains(trans))) {
				activeTrans.remove(trans);
			}
		}
    return success;
  }
	
	/**
	 * <p>Description: <p>
	 * @param jobMeta
	 * @param rep
	 * @param params
	 * @param prop
	 * @param monitor_id
	 * @param execType
	 * @param remoteServer
	 * @param ha
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked", "unused"})
	private boolean executeJob(Object jobMeta, Object rep, String[] params, HashMap<?, ?> prop, int execType, String remoteServer, String ha) throws Exception {
    boolean success = false;
    Date startDate = new Date();
    String status = STATUS_FINISHED;
    String logChannelId = "";
    Object result = null;
    Object job = null;
    Long id_server = 0L;
    try
    {
		//EnvUtil.environmentInit();
		//JobEntryLoader.init();
		//StepLoader.init();

      Class jobClass = Class.forName("org.pentaho.di.job.Job");
      String simpleObjectId = UUID.randomUUID().toString();
      if (rep != null) {
        Class simpleLoggingObjectClass = Class.forName("org.pentaho.di.core.logging.SimpleLoggingObject");
        Class loggingObjectTypeClass = Class.forName("org.pentaho.di.core.logging.LoggingObjectType");
        Class loggingObjectInterfaceClass = Class.forName("org.pentaho.di.core.logging.LoggingObjectInterface");
        Class repositoryClass = Class.forName("org.pentaho.di.repository.Repository");
        Constructor simpleLoggingObjectConstructor = simpleLoggingObjectClass.getConstructor(new Class[] { String.class, loggingObjectTypeClass, loggingObjectInterfaceClass });

        Field jobField = loggingObjectTypeClass.getDeclaredField("JOB");
        Object simpleLoggingObject = simpleLoggingObjectConstructor.newInstance(new Object[] { "SPOON", jobField.get(""), null });
        Method setContainerObjectId = simpleLoggingObjectClass.getDeclaredMethod("setContainerObjectId", new Class[] { String.class });
        setContainerObjectId.invoke(simpleLoggingObject, new Object[] { simpleObjectId });
        Constructor jobConstructor = jobClass.getConstructor(new Class[] { repositoryClass, jobMeta.getClass(), loggingObjectInterfaceClass });
        job = jobConstructor.newInstance(new Object[] { rep, jobMeta, simpleLoggingObject });
      } else {
        Constructor jobConstructor = jobClass.getConstructor(new Class[] { String.class, String.class, Object[].class });
        Method getName = jobMeta.getClass().getDeclaredMethod("getName", new Class[0]);
        Method getFileName = jobMeta.getClass().getDeclaredMethod("getFileName", new Class[0]);
        job = jobConstructor.newInstance(new Object[] { getName.invoke(jobMeta, new Object[0]), getFileName.invoke(jobMeta, new Object[0]), params });
      }

      Method setInteractive = jobClass.getDeclaredMethod("setInteractive", new Class[] { Boolean.TYPE });
      setInteractive.invoke(job, new Object[] { Boolean.valueOf(true) });
      
      if (1 == execType)
      {
        Method getLogChannel = jobClass.getDeclaredMethod("getLogChannel", new Class[0]);
        Object logChannel = getLogChannel.invoke(job, new Object[0]);

        Method getLogChannelId = jobClass.getDeclaredMethod("getLogChannelId", new Class[0]);
        logChannelId = (String)getLogChannelId.invoke(job, new Object[0]);

        Method logMinimal = logChannel.getClass().getDeclaredMethod("logMinimal", new Class[] { String.class, Object[].class});
        logMinimal.invoke(logChannel, new Object[] { "ETL--JOB Start of run", new Object[0] });

        activeJobs.add(job);

        Method beginProcessing = jobClass.getDeclaredMethod("beginProcessing", new Class[0]);
        beginProcessing.invoke(job, new Object[0]);
        Class threadClass = Class.forName("java.lang.Thread");
        Method start_job = threadClass.getDeclaredMethod("start", new Class[0]);
        start_job.invoke(job, new Object[0]);
        Method waitUntilFinished = jobClass.getDeclaredMethod("waitUntilFinished", new Class[0]);
        waitUntilFinished.invoke(job, new Object[0]);

        activeJobs.remove(job);

        logMinimal.invoke(logChannel, new Object[] { "ETL--JOB Finished!", new Object[0] });
        Date stop = new Date();
        logMinimal.invoke(logChannel, new Object[] { "ETL--JOB Start=" + StringUtil.DateToString(startDate, "yyyy/MM/dd HH:mm:ss") + ", Stop=" + StringUtil.DateToString(stop, "yyyy/MM/dd HH:mm:ss"), new Object[0] });
        long millis = stop.getTime() - startDate.getTime();
        logMinimal.invoke(logChannel, new Object[] { "ETL--JOB Processing ended after " + millis / 1000L + " seconds.", new Object[0] });

        Method getResult = jobClass.getDeclaredMethod("getResult", new Class[0]);
        result = getResult.invoke(job, new Object[0]);
        
        success = true;
      } 
		} catch (Exception e) {
			status = STATUS_ERROR;
			e.printStackTrace();
			throw e;
		} finally {
			monitor(startDate, status, logChannelId, result, id_server);
			if ((job != null) && (activeJobs.contains(job))) {
				activeJobs.remove(job);
			}
		}

    return success;
  }

	/**
	 * <p>Description: 添加监控<p>
	 * @param startDate
	 * @param status
	 * @param logChannelId
	 * @param result
	 * @param id_server
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void monitor(Date startDate, String status, String logChannelId, Object result, Long id_server) {
		try {
			String logMessage = null;
			Method getNrErrors = null;
			Method getNrLinesInput = null;
			Method getNrLinesOutput = null;
			Method getNrLinesUpdated = null;
			Method getNrLinesRead = null;
			Method getNrLinesWritten = null;
			Method getNrLinesDeleted = null;

			long nrErrors = 0L;
			long nrLinesInput = 0L;
			long nrLinesOutput = 0L;
			long nrLinesUpdated = 0L;
			long nrLinesRead = 0L;
			long nrLinesWritten = 0L;
			long nrLinesDeleted = 0L;

			Class centralLogStoreClass = Class
					.forName("org.pentaho.di.core.logging.CentralLogStore");
			Method getAppender = centralLogStoreClass.getDeclaredMethod(
					"getAppender", new Class[0]);
			Object appender = getAppender.invoke(centralLogStoreClass,
					new Object[0]);

			Method getBuffer = appender.getClass().getDeclaredMethod(
					"getBuffer", new Class[]{String.class, Boolean.TYPE});
			StringBuffer sb = (StringBuffer) getBuffer.invoke(appender,
					new Object[]{logChannelId, Boolean.valueOf(true)});
			logMessage = sb.toString().replaceAll("\n", "<br>");

			if (result != null) {
				getNrErrors = result.getClass().getDeclaredMethod(
						"getNrErrors", new Class[0]);
				getNrLinesInput = result.getClass().getDeclaredMethod(
						"getNrLinesInput", new Class[0]);
				getNrLinesOutput = result.getClass().getDeclaredMethod(
						"getNrLinesOutput", new Class[0]);
				getNrLinesUpdated = result.getClass().getDeclaredMethod(
						"getNrLinesUpdated", new Class[0]);
				getNrLinesRead = result.getClass().getDeclaredMethod(
						"getNrLinesRead", new Class[0]);
				getNrLinesWritten = result.getClass().getDeclaredMethod(
						"getNrLinesWritten", new Class[0]);
				getNrLinesDeleted = result.getClass().getDeclaredMethod(
						"getNrLinesDeleted", new Class[0]);

				nrErrors = ((Long) getNrErrors.invoke(result, new Object[0]))
						.longValue();
				nrLinesInput = ((Long) getNrLinesInput.invoke(result,
						new Object[0])).longValue();
				nrLinesOutput = ((Long) getNrLinesOutput.invoke(result,
						new Object[0])).longValue();
				nrLinesUpdated = ((Long) getNrLinesUpdated.invoke(result,
						new Object[0])).longValue();
				nrLinesRead = ((Long) getNrLinesRead.invoke(result,
						new Object[0])).longValue();
				nrLinesWritten = ((Long) getNrLinesWritten.invoke(result,
						new Object[0])).longValue();
				nrLinesDeleted = ((Long) getNrLinesDeleted.invoke(result,
						new Object[0])).longValue();

			}

			Date endDate = new Date();
			float continuedTime = (float) (endDate.getTime() - startDate.getTime()) / 1000.0F;
			if(kettleMonitor != null){
				kettleMonitor.setStartTime(startDate);
				kettleMonitor.setEndTime(endDate);
				kettleMonitor.setJobstatus(status);
				kettleMonitor.setContinuedTime(continuedTime);
				kettleMonitor.setLogmsg(logMessage);
				kettleMonitor.setLinesError(nrErrors);
				kettleMonitor.setLinesInput(nrLinesInput);
				kettleMonitor.setLinesOutput(nrLinesOutput);
				kettleMonitor.setLinesUpdated(nrLinesUpdated);
				kettleMonitor.setLinesRead(nrLinesRead);
				kettleMonitor.setLinesWritten(nrLinesWritten);
				kettleMonitor.setLinesDeleted(nrLinesDeleted);
				kettleMonitor.setIdCluster(id_server);
				kettleMonitorService.update(kettleMonitor);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
