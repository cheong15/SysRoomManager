package com.hotent.platform.service.bpm.listener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

import com.hotent.core.bpm.util.BpmConst;
import com.hotent.core.util.AppUtil;
import com.hotent.platform.dao.bpm.BpmProStatusDao;
import com.hotent.platform.model.bpm.TaskOpinion;
import com.hotent.platform.service.bpm.BpmProStatusService;
import com.hotent.platform.service.bpm.thread.TaskThreadService;
/**
 * 外部子流程节点启动事件监听器。
 * <pre>
 * 1.	子流程执行顺序。
 *  	1.流程流转到外部子流程节点。
 *  	2.首先执行节点开始监听器，CallSubProcessStartListener。
 *  	3.再触发子流程的开始监听事件。
 *  	4.子流程执行完毕，会触发结束监听事件。
 *  	5.触发子流程节点结束监听器。
 * 	2.起始节点事件做的操作。
 * 		1.构造外部子流程变量 【outPassVars】 ，用于外部子流程和内部子流程做数据传递。
 * 		2.清除之前的堆栈任务。
 * 		3.清除之前子流程产生的子流程流程实例id列表。
 * 		4.修改子流程节点的状态为正在运行。
 * </pre>
 * @author ray
 *
 */
public class CallSubProcessStartListener extends BaseNodeEventListener {
	
	

	@Override
	protected void execute(DelegateExecution execution, String actDefId,String nodeId) {
		Long processInstanceId=new Long( execution.getProcessInstanceId());
		Map<String,Object> flowVars=TaskThreadService.getVariables();
		if(execution.getVariable(BpmConst.PROCESS_EXT_VARNAME)==null){
			execution.setVariable(BpmConst.PROCESS_EXT_VARNAME, flowVars);
			TaskThreadService.clearVariables();
		}
		Integer completeInstance=(Integer)execution.getVariable("nrOfCompletedInstances");

		//首次调用。
		if(completeInstance==null){
			//
			BpmProStatusService bpmProStatusService=(BpmProStatusService)AppUtil.getBean(BpmProStatusService.class);
			TaskThreadService.clearNewTasks();
			TaskThreadService.cleanExtSubProcess();
			bpmProStatusService.addOrUpd(actDefId, processInstanceId, nodeId);
		}
	}
	
	@Override
	protected Integer getScriptType() {
		return BpmConst.StartScript;
	}

}
