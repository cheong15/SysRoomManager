package com.hotent.platform.service.bpm.impl;

import java.util.List;

import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

import com.hotent.core.bpm.BaseProcessService;
import com.hotent.platform.dao.bpm.BpmDefVarDao;
import com.hotent.platform.model.bpm.BpmDefVar;
import com.hotent.platform.service.bpm.IBpmActService;
import com.hotent.platform.service.bpm.cmd.EndProcessCmd;
import com.hotent.platform.service.bpm.cmd.GetExecutionCmd;

@Service
public class BpmActService extends BaseProcessService implements IBpmActService {
	
	@Resource
	private BpmDefVarDao dao;
	public BpmActService() {
		
	}
	
	@Override
	public List<BpmDefVar> getVarsByFlowDefId(Long defId) {
		return dao.getVarsByFlowDefId(defId);
	}
	
	
	@Override
	public ExecutionEntity getExecution(String executionId) {
		return commandExecutor.execute(new GetExecutionCmd(executionId)) ;
	}
	
	/**
	 * 根据任务ID结束流程。
	 * @param taskId
	 */
	public void endProcessByTaskId(String taskId){
		EndProcessCmd cmd=new EndProcessCmd(taskId);
		commandExecutor.execute(cmd);
	}

}
