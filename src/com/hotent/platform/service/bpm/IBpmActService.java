/**
 * 
 */
package com.hotent.platform.service.bpm;

import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.hotent.platform.model.bpm.BpmDefVar;

/**
 * 流程使用到的服务接口。
 * @author hotent
 *
 */
public interface IBpmActService {
	
	/**
	 * 根据流程定义id获取流程变量列表。
	 * @param defId
	 * @return
	 */
	public List<BpmDefVar> getVarsByFlowDefId(Long defId);
	
	/**
	 * 根据executionId获取ExecutionEntity对象。
	 * @param executionId
	 * @return
	 */
	public ExecutionEntity getExecution(String executionId);
	
	/**
	 * 结束流程
	 * @param taskId
	 */
	public void endProcessByTaskId(String taskId);

}
