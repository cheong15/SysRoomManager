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

package org.activiti.engine.impl.persistence.deploy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import com.rits.cloning.Cloner;


/**
 * @author Tom Baeyens
 * @author Falko Menge
 */
public class DeploymentCache {

  private static final Logger LOG = Logger.getLogger(DeploymentCache.class.getName());;
  //全局缓存
 // private static Map<String, ProcessDefinitionEntity> globalDefCache=new HashMap<String, ProcessDefinitionEntity>();
  //线程变量。
  protected static ThreadLocal<Map<String, ProcessDefinitionEntity>> processDefinitionCache = new ThreadLocal<Map<String,ProcessDefinitionEntity>>();
  
  
  public static void clearProcessDefinitionEntity(){
	  processDefinitionCache.remove();
  }
 
  
  /**
   * 添加到全局缓存
   * @param ent
   */
//  public static void addGlobalDefCache(ProcessDefinitionEntity ent){
//	  globalDefCache.put(ent.getId(), ent);
//  }
//  
//  /**
//   * 删除全局缓存
//   * @param actDefId
//   */
//  public static void removeGlobalDefCache(String actDefId){
//	  globalDefCache.remove(actDefId);
//	  processDefinitionCache.remove();
//  }
  
  /**
   * 从全局缓存中获取对象并克隆一个新的对象。
   * @param actDefId
   * @return
   */
//  private static ProcessDefinitionEntity getGlobalDefById(String actDefId){
//	  ProcessDefinitionEntity ent=globalDefCache.get(actDefId);
//	  if(ent==null) {
//		  LOG.info("get definition from database:"+actDefId);
//		  return null;
//	  }
//	  LOG.info("get definition from global cache:"+actDefId);
//	  ProcessDefinitionEntity entClone=(ProcessDefinitionEntity) ent.clone();
//	  
//	  return entClone;
//  }
//  
  /**
   * 设置到线程变量。
   * @param processEnt
   */
  public static void setThreadLocalDef(ProcessDefinitionEntity processEnt){
	  if(processDefinitionCache.get()==null){
		  Map<String, ProcessDefinitionEntity> map=new HashMap<String, ProcessDefinitionEntity>();
		  map.put(processEnt.getId(),processEnt);
		  processDefinitionCache.set(map);
	  }
	  else{
		  Map<String, ProcessDefinitionEntity> map=processDefinitionCache.get();
		  map.put(processEnt.getId(),processEnt);
	  }
	
  }
  
  /**
   * 1.线程变量为空。
   * 	从缓存中获取
   * 	1.如果没有获取到则返回为空。
   * 	2.如果获取到了则设置到线程变量。
   * 2.线程变量不为空。
   * 	1.线程变量中包含指定的流程定义，则返回。
   * 	2.不包含。
   * 		1.如果没有获取到则返回为空。
   * 		2.如果获取到了则设置到线程变量。
   * @param processDefinitionId
   * @return
   */
  private static ProcessDefinitionEntity getThreadLocalDef(String processDefinitionId){
	  if(processDefinitionCache.get()==null){
		  return null;
	  }
	  Map<String, ProcessDefinitionEntity> map=processDefinitionCache.get();
	  if(!map.containsKey(processDefinitionId)){
		  return null;
	  }
	  LOG.info("get definition from local thread:"+processDefinitionId);
	  return map.get(processDefinitionId);
  }
  
//  public static void clearProcessDefinitionEntity(){
//	  processDefinitionCache.remove();
//  }
  
  
  protected Map<String, Object> knowledgeBaseCache = new HashMap<String, Object>(); 
  protected List<Deployer> deployers;
  
  public void deploy(DeploymentEntity deployment) {
    for (Deployer deployer: deployers) {
      deployer.deploy(deployment);
    }
  }

  public ProcessDefinitionEntity findDeployedProcessDefinitionById(String processDefinitionId) {
    if (processDefinitionId == null) {
      throw new ActivitiException("Invalid process definition id : null");
    }
    ProcessDefinitionEntity processDefinition =  getThreadLocalDef(processDefinitionId);
    if(processDefinition==null){
    	 processDefinition = Context
    		      .getCommandContext()
    		      .getProcessDefinitionManager()
    		      .findLatestProcessDefinitionById(processDefinitionId);
    		    if(processDefinition == null) {
    		      throw new ActivitiException("no deployed process definition found with id '" + processDefinitionId + "'");
    		    }
    		    processDefinition = resolveProcessDefinition(processDefinition);
    }
   
    
    return processDefinition;
  }

  public ProcessDefinitionEntity findDeployedLatestProcessDefinitionByKey(String processDefinitionKey) {
    ProcessDefinitionEntity processDefinition = Context
      .getCommandContext()
      .getProcessDefinitionManager()
      .findLatestProcessDefinitionByKey(processDefinitionKey);
    if (processDefinition==null) {
      throw new ActivitiException("no processes deployed with key '"+processDefinitionKey+"'");
    }
    ProcessDefinitionEntity ent=  getThreadLocalDef(processDefinition.getId());
    if(ent!=null){
    	return ent;
    }
    processDefinition = resolveProcessDefinition(processDefinition);
    return processDefinition;
  }

  public ProcessDefinitionEntity findDeployedProcessDefinitionByKeyAndVersion(String processDefinitionKey, Integer processDefinitionVersion) {
    ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) Context
      .getCommandContext()
      .getProcessDefinitionManager()
      .findProcessDefinitionByKeyAndVersion(processDefinitionKey, processDefinitionVersion);
    if (processDefinition==null) {
      throw new ActivitiException("no processes deployed with key = '" + processDefinitionKey + "' and version = '" + processDefinitionVersion + "'");
    }
    ProcessDefinitionEntity ent=  getThreadLocalDef(processDefinition.getId());
    if(ent!=null){
    	return ent;
    }
    processDefinition = resolveProcessDefinition(processDefinition);
    return processDefinition;
  }

  protected ProcessDefinitionEntity resolveProcessDefinition(ProcessDefinitionEntity processDefinition) {
    String processDefinitionId = processDefinition.getId();
    String deploymentId = processDefinition.getDeploymentId();
    processDefinition =  getThreadLocalDef(processDefinitionId);
    if (processDefinition==null) {
	      DeploymentEntity deployment = Context
	        .getCommandContext()
	        .getDeploymentManager()
	        .findDeploymentById(deploymentId);
	      deployment.setNew(false);
	      
	      deploy(deployment);
	    
	      processDefinition = getThreadLocalDef(processDefinitionId);
	     
      }
   
      
      if (processDefinition==null) {
        throw new ActivitiException("deployment '"+deploymentId+"' didn't put process definition '"+processDefinitionId+"' in the cache");
      }
    //}
    return processDefinition;
  }

//  public void addProcessDefinition(ProcessDefinitionEntity processDefinition) {
//	  setThreadLocalDef(processDefinition);
//  }
//
//  public void removeProcessDefinition(String processDefinitionId) {
//    if( processDefinitionCache.get()==null) return;
//    
//    processDefinitionCache.get().remove(processDefinitionId);
//  }

  public void addKnowledgeBase(String knowledgeBaseId, Object knowledgeBase) {
    knowledgeBaseCache.put(knowledgeBaseId, knowledgeBase);
  }

  public void removeKnowledgeBase(String knowledgeBaseId) {
    knowledgeBaseCache.remove(knowledgeBaseId);
  }
  
  public void discardProcessDefinitionCache() {
	 // globalDefCache.clear();
	  processDefinitionCache.remove();
  }

  public void discardKnowledgeBaseCache() {
    knowledgeBaseCache.clear();
  }
  // getters and setters //////////////////////////////////////////////////////

  public Map<String, ProcessDefinitionEntity> getProcessDefinitionCache() {
    return processDefinitionCache.get();
  }
  
  public void setProcessDefinitionCache(Map<String, ProcessDefinitionEntity> processDefinitionCache) {
	  processDefinitionCache.putAll(processDefinitionCache);
//    this.processDefinitionCache.set(processDefinitionCache);
  }
  
  public Map<String, Object> getKnowledgeBaseCache() {
    return knowledgeBaseCache;
  }
  
  public void setKnowledgeBaseCache(Map<String, Object> knowledgeBaseCache) {
    this.knowledgeBaseCache = knowledgeBaseCache;
  }
  
  public List<Deployer> getDeployers() {
    return deployers;
  }
  
  public void setDeployers(List<Deployer> deployers) {
    this.deployers = deployers;
  }
}
