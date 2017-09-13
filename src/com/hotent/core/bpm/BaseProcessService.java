package com.hotent.core.bpm;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.ServiceImpl;

import org.springframework.beans.factory.InitializingBean;

/**
 * 获取命令执行对象，将其注入到当前对象。
 * @author ray
 *
 */
public class BaseProcessService extends ServiceImpl implements InitializingBean{
	
	@Resource 
	ProcessEngine processEngine;

	@Override
	public void afterPropertiesSet() throws Exception {
		 ProcessEngineImpl engine = (ProcessEngineImpl)processEngine;
		 this.setCommandExecutor(engine.getProcessEngineConfiguration().getCommandExecutorTxRequired());
	}

	

}
