/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.engine.impl.bpmn.behavior;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

import com.hotent.core.util.BeanUtils;

/**
 * Implementation of the Inclusive Gateway/OR gateway/inclusive data-based
 * gateway as defined in the BPMN specification.
 * 
 * @author Tijs Rademakers
 * @author Tom Van Buskirk
 * @author Joram Barrez
 */
public class InclusiveGatewayActivityBehavior extends GatewayActivityBehavior {

	private static Logger log = Logger
			.getLogger(InclusiveGatewayActivityBehavior.class.getName());

	public void execute(ActivityExecution execution) throws Exception {

		execution.inactivate();
		lockConcurrentRoot(execution);

		PvmActivity activity = execution.getActivity();
		//当前的execution 非激活非并发。
		if (!activeConcurrentExecutionsExist(execution)) {

			if (log.isLoggable(Level.FINE)) {
				log.fine("inclusive gateway '" + activity.getId()
						+ "' activates");
			}

			List<ActivityExecution> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
			
			//int nbrOfExecutionsToJoin = execution.getActivity().getIncomingTransitions().size();
			
			String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
			List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();

			for (PvmTransition outgoingTransition : execution.getActivity()
					.getOutgoingTransitions()) {
				if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
					Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
					if (condition == null || condition.evaluate(execution)) {
						transitionsToTake.add(outgoingTransition);
					}
				}
			}
			
			
			

			if (transitionsToTake.size() > 0) {
				execution.takeAll(transitionsToTake, joinedExecutions);

			} else {

				if (defaultSequenceFlow != null) {
					PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
					if (defaultTransition != null) {
						execution.take(defaultTransition);
					} else {
						throw new ActivitiException("Default sequence flow '"
								+ defaultSequenceFlow
								+ "' could not be not found");
					}
				} else {
					// No sequence flow could be found, not even a default one
					throw new ActivitiException(
							"No outgoing sequence flow of the inclusive gateway '"
									+ execution.getActivity().getId()
									+ "' could be selected for continuing the process");
				}
			}

		} else {
			if (log.isLoggable(Level.FINE)) {
				log.fine("Inclusive gateway '" + activity.getId()
						+ "' does not activate");
			}
		}
	}
	
	/**
	 * 根据excution判断是否有子的excution还是激活状态。
	 * @param execution
	 * @return
	 */
	private boolean isChildActive(ActivityExecution execution){
		if(execution.isActive()) return true;
		List<? extends ActivityExecution> list= execution.getExecutions();
		if(BeanUtils.isEmpty(list)){
			return false;
		}
		for(ActivityExecution tmp:list){
			if(tmp.isActive()){
				return true;
			}
			else{
				return isChildActive(execution);
			}
		}
		return false;
		
	}
	/**
	 * 判断传入的excution，是否是并发的，并且是活动的。
	 * @param execution
	 * @return
	 */
	public boolean activeConcurrentExecutionsExist(ActivityExecution execution) {
		PvmActivity activity = execution.getActivity();
		
		if (execution.isConcurrent()) {
			List<ActivityExecution> list=(List<ActivityExecution>) execution.getParent().getExecutions();
			for (ActivityExecution concurrentExecution : list) {
				boolean isActive=isChildActive(concurrentExecution);
				if(isActive) return true;
				if (concurrentExecution.isActive()	&& concurrentExecution.getActivity() != activity) {
					// TODO: when is transitionBeingTaken cleared? Should we
					// clear it?
					boolean reachable = false;
					PvmTransition pvmTransition = ((ExecutionEntity) concurrentExecution).getTransitionBeingTaken();
					if (pvmTransition != null) {
						reachable = isReachable(pvmTransition.getDestination(),activity, new HashSet<PvmActivity>());
					} else {
						reachable = isReachable(concurrentExecution.getActivity(), activity,new HashSet<PvmActivity>());
					}

					if (reachable) {
						if (log.isLoggable(Level.FINE)) {
							log.fine("an active concurrent execution found: '"
									+ concurrentExecution.getActivity());
						}
						return true;
					}
				}
			}
		} else if (execution.isActive()) { // is this ever true?
			if (log.isLoggable(Level.FINE)) {
				log.fine("an active concurrent execution found: '"
						+ execution.getActivity());
			}
			return true;
		}

		return false;
	}
	


	/**
	 * 这个方法是判定从开始节点是否可以达到目标节点。
	 * 判断条件，从开始节点开始遍历，查找目标节点，一直递归查找找到有目标节点。
	 * @param srcActivity
	 * @param targetActivity
	 * @param visitedActivities
	 * @return
	 */
	protected boolean isReachable(PvmActivity srcActivity,PvmActivity targetActivity, Set<PvmActivity> visitedActivities) {

		if (srcActivity.equals(targetActivity)) {
			return true;
		}

		// To avoid infinite looping, we must capture every node we visit
		// and check before going further in the graph if we have already
		// visitied the node.
		visitedActivities.add(srcActivity);
		//获取源节点的外出节点列表。
		List<PvmTransition> transitionList = srcActivity.getOutgoingTransitions();
		if (transitionList != null && transitionList.size() > 0) {
			for (PvmTransition pvmTransition : transitionList) {
				PvmActivity destinationActivity = pvmTransition
						.getDestination();
				if (destinationActivity != null
						&& !visitedActivities.contains(destinationActivity)) {
					boolean reachable = isReachable(destinationActivity,
							targetActivity, visitedActivities);

					// If false, we should investigate other paths, and not yet
					// return the result
					if (reachable) {
						return true;
					}

				}
			}
		}
		return false;
	}

}
