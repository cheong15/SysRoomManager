package com.hotent.platform.service.bpm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.engine.IScript;
import com.hotent.core.model.TaskExecutor;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.Dom4jUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.TimeUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.form.BpmFormHandlerDao;
import com.hotent.platform.dao.system.PositionDao;
import com.hotent.platform.dao.system.SysOrgDao;
import com.hotent.platform.dao.system.SysOrgTypeDao;
import com.hotent.platform.dao.system.SysRoleDao;
import com.hotent.platform.dao.system.SysUserDao;
import com.hotent.platform.dao.system.SysUserParamDao;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.bpm.TaskOpinion;
import com.hotent.platform.model.system.Position;
import com.hotent.platform.model.system.SysOrg;
import com.hotent.platform.model.system.SysOrgType;
import com.hotent.platform.model.system.SysRole;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.model.system.SysUserParam;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.platform.service.bpm.TaskOpinionService;
import com.hotent.platform.service.bpm.thread.TaskThreadService;
import com.hotent.platform.service.system.SysOrgService;
import com.hotent.platform.service.system.SysUserOrgService;
import com.hotent.platform.service.system.SysUserService;
import com.hotent.platform.service.system.UserUnderService;

/**
 * 实现这个接口可以在groovy脚本中直接使用。
 * 
 * @author ray。
 * 
 */
public class ScriptImpl implements IScript {
	
	private static final Log logger = LogFactory.getLog(Dom4jUtil.class);
	@Resource
	TaskOpinionService taskOpinionService;
	@Resource
	private SysRoleDao sysRoleDao;
	@Resource
	private SysUserOrgService sysUserOrgService;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private PositionDao positionDao;
	@Resource
	private UserUnderService userUnderService;
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private SysUserService sysUserService;

	@Resource
	private SysOrgTypeDao sysOrgTypeDao;

	@Resource
	private SysUserParamDao sysUserParamDao;

	@Resource
	private ProcessRunService processRunService;
	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private BpmFormHandlerDao bpmFormHandlerDao;
	//@Resource
	//private PersonInfoDao personaldao;
	
	/**
	 * 取得当前登录用户工号 。<br>
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getAccount() 
	 * </pre>
	 * @return
	 */
	public String getAccount(){
		SysUser sysUser=ContextUtil.getCurrentUser();
		if(sysUser==null) return "";
		return sysUser.getAccount();
	}
	/**
	 * 取得当前登录用户id 。<br>
	 * 
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentUserId()
	 * </pre>
	 * 
	 * @return
	 */
	public long getCurrentUserId() {
		long userId= ContextUtil.getCurrentUserId();
		return userId;
	}
	
	

	/**
	 * 取得当前登录用户名称 。<br>
	 * 
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentName()
	 * </pre>
	 * 
	 * @return
	 */
	public String getCurrentName() {
		SysUser sysUser=ContextUtil.getCurrentUser();
		if(sysUser==null) return "";
		return sysUser.getFullname();
	}

	/**
	 * 获取当前系统的用户。
	 * 
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getCurrentUser();
	 * </pre>
	 * 
	 * @return 用户对象。
	 */
	public SysUser getCurrentUser() {
		return ContextUtil.getCurrentUser();
	}

	/**
	 * 获取当前日期。
	 * 
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentDate();
	 * 返回日期类型如下：
	 * 2002-11-06
	 * </pre>
	 * 
	 * @return
	 */
	public String getCurrentDate() {
		return TimeUtil.getCurrentDate();
	}
	/**
	 * 获取当前日期。指定格式输出
	 * 
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentDate("yyyy-MM-dd HH:mm:ss");
	 * 返回日期类型如下：
	 * 2002-11-06
	 * </pre>
	 * 
	 * @return
	 */
	public String getCurrentDate(String style) {
		if(StringUtils.isEmpty(style))
			style="yyyy-MM-dd";
		return TimeUtil.getCurrentDate(style);
	}

	/**
	 * 获取当前组织。
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentOrg();
	 * </pre>
	 * @return
	 */
	public SysOrg getCurrentOrg() {
		SysOrg sysOrg = ContextUtil.getCurrentOrg();
		return sysOrg;
	}

	/**
	 * 获取当前用户组织的ID
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentOrgId();
	 * </pre>
	 * @return
	 */
	public Long getCurrentOrgId() throws Exception {
		SysOrg sysOrg = ContextUtil.getCurrentOrg();
		if (sysOrg == null)
			return 0L;
		return sysOrg.getOrgId();
	}

	/**
	 * 取得当前用户组织的名称。
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentOrgName();
	 * </pre>
	 * @return
	 */
	public String getCurrentOrgName() {
		SysOrg sysOrg = ContextUtil.getCurrentOrg();
		if (sysOrg == null)
			return "";
		return sysOrg.getOrgName();
	}

	/**
	 * 取得当前用户主组织名称
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentPrimaryOrgName();
	 * </pre> 
	 * @return
	 */
	public String getCurrentPrimaryOrgName() {
		Long userId = ContextUtil.getCurrentUserId();
		SysOrg sysOrg = sysOrgDao.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return "";
		return sysOrg.getOrgName();
	}
	
	/**
	 * 取得当前用户主组织的ID。
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurrentPrimaryOrgId();
	 * </pre> 
	 * @return
	 */
	public Long getCurrentPrimaryOrgId() {
		Long userId = ContextUtil.getCurrentUserId();
		SysOrg sysOrg = sysOrgDao.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return 0L;
		return sysOrg.getOrgId();
	}

	/**
	 * 取得某用户主组织的Id。
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getUserOrgId(Long userId);
	 * </pre>
	 * @return
	 */
	public Long getUserOrgId(Long userId) {
		SysOrg sysOrg= sysOrgDao.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return 0L;
		return sysOrg.getOrgId();
	}

	/**
	 * 取得某用户主组织的名称。
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getUserOrgName(Long userId);
	 * </pre>
	 * @return
	 */
	public String getUserOrgName(Long userId) {
		SysOrg sysOrg= sysOrgDao.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return "";
		return sysOrg.getOrgName();
	}

	
	
	/**
	 * 判断当前用户是否属于该角色。
	 * 
	 * <pre>
	 * 在脚本中使用方法:
	 * scriptImpl.hasRole(alias)
	 * </pre>
	 * 
	 * @param alias
	 *            角色别名
	 * @return
	 */
	public boolean hasRole(String alias) {
		long userId = ContextUtil.getCurrentUserId();
		List<SysRole> roleList = sysRoleDao.getByUserId(userId);
		for (SysRole role : roleList) {
			if (role.getAlias().equals(alias)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前用户所属角色。
	 * 
	 * <pre>
	 * 在脚本中使用方法: scriptImpl.getCurrentUserRoles();
	 * </pre>
	 * 
	 * @return 返回角色列表。
	 */
	public List<SysRole> getCurrentUserRoles() {
		long userId = ContextUtil.getCurrentUserId();
		List<SysRole> list = sysRoleDao.getByUserId(userId);
		return list;
	}

	/**
	 * 获取发起用户所属角色。
	 * 
	 * <pre>
	 * 在脚本中使用方法: scriptImpl.getUserRoles(strUserId);
	 * </pre>
	 * 
	 * @return 返回角色列表。
	 * @param strUserId
	 *            用户ID
	 * @return
	 */
	public List<SysRole> getUserRoles(String strUserId) {
		if(StringUtil.isEmpty(strUserId)) {
			return Collections.EMPTY_LIST;
		}
		Long userId = Long.parseLong(strUserId);
		List<SysRole> list = sysRoleDao.getByUserId(userId);
		return list;
	}

	/**
	 * 判断用户是否属于某角色。
	 * 
	 * @param userId
	 *            用户id。
	 * @param role
	 *            角色别名 在脚本中使用方法: scriptImpl.isUserInRole(userId);
	 * @return
	 */
	public boolean isUserInRole(String userId, String role) {
		Long lUserId = Long.parseLong(userId);
		List<SysRole> list = sysRoleDao.getByUserId(lUserId);
		if (BeanUtils.isEmpty(list))
			return false;
		for (SysRole sysRole : list) {
			if (sysRole.getAlias().equals(role)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断用户是否属于某组织。
	 * <pre>
	 * 在脚本中使用方法: scriptImpl.isUserInOrg(String userId, String orgName);
	 * </pre>
	 * @param userId	用户ID
	 * @param org		组织名称
	 * @return
	 */
	public boolean isUserInOrg(String userId, String orgName) {
		Long lUserId = Long.parseLong(userId);
		List<SysOrg> list = sysOrgDao.getOrgsByUserId(lUserId);
		if (BeanUtils.isEmpty(list))
			return false;
		for (SysOrg sysOrg : list) {
			if (sysOrg.getOrgName().equals(orgName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前用户是否属于某组织。
	 * 
	 * @param userId
	 *            用户id。
	 * @param org
	 *            组织名 在脚本中使用方法: scriptImpl.isCurUserInOrg(String orgName);
	 * @return
	 */
	public boolean isCurUserInOrg(String orgName) {
		Long userId = ContextUtil.getCurrentUserId();
		return isUserInOrg(userId.toString(), orgName);
	}
	
	
	/**
	 * 判断用户是否属于某组织（存在多级组织）。
	 * <pre>
	 * 在脚本中使用方法: scriptImpl.isUserInOrgs(String userId, String orgName);
	 * </pre>
	 * @param userId	用户ID
	 * @param org		组织名称
	 * @return
	 */
	public boolean isUserInOrgs(String userId, String orgName) {
		Long lUserId = Long.parseLong(userId);
		SysOrg sysOrg = sysOrgDao.getOrgByOrgName(orgName);
		if(sysOrg==null)return false;
		
		String path="%"+sysOrg.getPath()+"%";
		List<SysOrg> list = sysOrgDao.getOrgByUserIdPath(lUserId,path.trim());
		
		if (BeanUtils.isEmpty(list)){
			return false;
		}
		
		return true;
	}

	/**
	 * 获取用户的主岗位名称
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getUserPos(userId)
	 * </pre>
	 * @param userId	用户ID
	 * @return 主岗位名称
	 */
	public String getUserPos(Long userId) {
		String posName = "";
		Position position = positionDao.getPosByUserId(userId);
		if (!BeanUtils.isEmpty(position)) {
			posName = position.getPosName();
		}
		return posName;
	}

	
	/**
	 * 根据用户ID获取岗位Id。
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getUserPosId(userId)
	 * </pre>
	 * @param userId
	 * @return
	 */
	public String getUserPosId(Long userId) {
		String posId = "";
		Position position = positionDao.getPosByUserId(userId);
		if (!BeanUtils.isEmpty(position)) {
			posId = position.getPosId().toString();
		}
		return posId;
	}
	
	
	/**
	 * 根据工号获取岗位ID。
	 * <pre>
	 * 脚本中使用方法:
	 * return scriptImpl.getPosIdByAccount(account);
	 * </pre>
	 * @param account
	 * @return
	 */
	public String getPosIdByAccount(String account) {
		SysUser sysUser=sysUserService.getByAccount(account);
		if(sysUser==null){
			return "";
		}
		String posName = "";
		Position position = positionDao.getPosByUserId(sysUser.getUserId());
		if (!BeanUtils.isEmpty(position)) {
			posName = position.getPosName();
		}
		return posName;
	}

	
	/**
	 * 根据工号获取岗位名称。
	 * <pre>
	 * 脚本中使用方法:
	 * return scriptImpl.getPosNameByAccount(account);
	 * </pre>
	 * @param userId
	 * @return
	 */
	public String getPosNameByAccount(String account) {
		SysUser sysUser=sysUserService.getByAccount(account);
		if(sysUser==null){
			return "";
		}
		String name = "";
		Position position = positionDao.getPosByUserId(sysUser.getUserId());
		if (!BeanUtils.isEmpty(position)) {
			name = position.getPosName();
		}
		return name;
	}

	/**
	 * 获取当前用户的主岗位名称。
	 * 
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getCurUserPos();
	 * </pre>
	 * 
	 * @return
	 */
	public String getCurUserPos() {
		long userId = ContextUtil.getCurrentUserId();
		String posName = getUserPos(userId);
		return posName;
	}
	
	/**
	 * 获取当前用户的主岗位名称。
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getCurUserPosName();
	 * </pre>
	 * @return
	 */
	public String getCurUserPosName() {
		long userId = ContextUtil.getCurrentUserId();
		String posName = getUserPos(userId);
		return posName;
	}
	
	
	/**
	 * 获取当前用户的主岗位ID。
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getCurUserPosId();
	 * </pre>
	 * @return
	 */
	public String getCurUserPosId() {
		long userId = ContextUtil.getCurrentUserId();
		String posId = getUserPosId(userId);
		return posId;
	}

	/**
	 * 获取cmd对象。
	 */
	public ProcessCmd getProcessCmd(){
		return TaskThreadService.getProcessCmd();
	}

	/**
	 * 通过用户账号设置任务执行人
	 * 
	 * @param task
	 * @param userAccout
	 */
	public void setAssigneeByAccount(TaskEntity task, String userAccout) {
		String[] aryAccount = userAccout.split(",");
		List<String> userIds = new ArrayList<String>();
		for (String str : aryAccount) {
			SysUser sysUser = sysUserService.getByAccount(str);
			userIds.add(sysUser.getUserId().toString());
		}
		if (userIds.size() == 0)
			return;
		// 传进来一个用户则直接设置为执行人
		if (userIds.size() == 1) {
			task.setAssignee(userIds.get(0));
		}
		// 传进来多个用户则添加到候选人
		else {
			task.addCandidateUsers(userIds);
		}
	}

	/**
	 * 启动某个流程。
	 * 脚本使用方法：
	 * scriptImpl.startFlow(String flowKey, String businnessKey,Map<String, Object> vars);
	 * @param flowKey
	 *            流程定义key。
	 * @param businnessKey
	 *            业务主键
	 * @param vars
	 *            流程变量。
	 * @return
	 * @throws Exception
	 */
	public ProcessRun startFlow(String flowKey, String businnessKey,
			Map<String, Object> vars) throws Exception {
		ProcessCmd processCmd = new ProcessCmd();
		processCmd.setFlowKey(flowKey);
		if(StringUtils.isEmpty(businnessKey)){
			businnessKey=Long.toString(UniqueIdUtil.genId());
		}
		processCmd.setBusinessKey(businnessKey);
		if (BeanUtils.isNotEmpty(vars)) {
			processCmd.setVariables(vars);
		}
		return processRunService.startProcess(processCmd);
	}

	/**
	 * 根据工号获取姓名。
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getFullNameByAccount(String accout);
	 * </pre>
	 * @param accout
	 * @return
	 */
	public String getFullNameByAccount(String accout){
		
		SysUser sysUser= sysUserService.getByAccount(accout);
		if(sysUser==null){
			return "";
		}
		return sysUser.getFullname();
	}
	
	/**
	 * 根据多工号获取姓名字符串
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getFullNameByAccounts(String accouts);
	 * </pre>
	 * @param accouts 工号字符串，多个以逗号隔开
	 * @return
	 */
	public String getFullNameByAccounts(String accouts){
		
		List<SysUser> sysUsers= sysUserService.getByAccounts(accouts);
		StringBuilder fullNames = new StringBuilder();
		for(SysUser s : sysUsers) {
			fullNames.append(s.getFullname());
			fullNames.append(",");
		}
		if(fullNames.length()>1) {
			fullNames.deleteCharAt(fullNames.length()-1);
		}
		return fullNames.toString();
	}
	
	/**
	 * 根据多工号获取用户ID字符串
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getUserIdsByAccounts(String accouts);
	 * </pre>
	 * @param accout
	 * @return
	 */
	public String getUserIdsByAccounts(String accouts){
		
		List<SysUser> sysUsers= sysUserService.getByAccounts(accouts);
		StringBuilder ids = new StringBuilder();
		for(SysUser s : sysUsers) {
			ids.append(s.getUserId());
			ids.append(",");
		}
		if(ids.length()>1) {
			ids.deleteCharAt(ids.length()-1);
		}
		return ids.toString();
	}
	/**
	 * 根据工号获取用户ID
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getUserIdByAccount(String accout);
	 * </pre>
	 * @param accout
	 * @return
	 */
	public String getUserIdByAccount(String accout){
		SysUser sysUser= sysUserService.getByAccount(accout);
		if(sysUser==null){
			return "";
		}
		return sysUser.getUserId().toString();
	}
	
	/**
	 * 根据工号获取组织名称。
	 * <pre>
	 * 脚本中使用方法: scriptImpl.getOrgNameByAccount(String accout);
	 * </pre>
	 * @param account
	 * @return
	 */
	public String getOrgNameByAccount(String account){
		SysOrg sysOrg=sysOrgDao.getOrgByAccount(account);
		return sysOrg.getOrgName();
	}
	
	/**
	 * 根据工号获取组织ID。
	 * <pre>
	 * 脚本中使用方法:
	 *  scriptImpl.getOrgIdByAccount(String account);
	 * </pre>
	 * @param account
	 * @return
	 */
	public String getOrgIdByAccount(String account){
		SysOrg sysOrg=sysOrgDao.getOrgByAccount(account);
		return sysOrg.getOrgId().toString();
	}
	
	/**
	 * 判断组织A是否为组织B的子组织
	 * @param sunOrgId
	 * @param parentOrgId
	 * @return
	 */
	public boolean getOrgBelongTo(String sonOrgId,Long parentOrgId){
		SysOrg sonSysOrg = sysOrgDao.getById(Long.parseLong(sonOrgId));
		SysOrg parentSysOrg = sysOrgDao.getById(parentOrgId);
		if(BeanUtils.isEmpty(sonSysOrg)||BeanUtils.isEmpty(parentSysOrg))return false;
		String sonPath = sonSysOrg.getPath();
		String parentPath = parentSysOrg.getPath();
		int result = sonPath.indexOf(parentPath); 
		return result > -1;
	}

	/**
	 * 根据组织id获取组织名称。
	 * <pre>
	 * scriptImpl.getOrgNameById(Long orgId);
	 * </pre>
	 * @param orgId
	 * @return
	 */
	public String getOrgNameById(Long orgId){
		String orgName="";
		try{
			SysOrg sysOrg= sysOrgDao.getById(orgId);
			orgName=sysOrg.getOrgName();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return orgName;
		
		
	}
	
	/**
	 * 根据组织ID获取组织ID(用于接口)
	 * <pre>
	 * scriptImpl.getOrgNameById(String orgId);
	 * </pre>
	 * @param orgId
	 * @return
	 */
	public String getOrgIdById(String orgId){
	  String rOrgId="";
	  try{
	    SysOrg sysOrg= sysOrgDao.getById(Long.valueOf(orgId));
	    rOrgId=String.valueOf(sysOrg.getOrgId());
	  }
	  catch (Exception e) {
	    e.printStackTrace();
	  }
	  return rOrgId;
	  
	  
	}
	
	/**
	 * 根据组织ID获取组织名称(用于接口传字符串类型的ID)
	 * <pre>
	 * scriptImpl.getOrgNameById(String orgId);
	 * </pre>
	 * @param orgId
	 * @return
	 */
	public String getOrgNameById(String orgId){
	  String orgName="";
	  try{
	    SysOrg sysOrg= sysOrgDao.getById(Long.valueOf(orgId));
	    orgName=sysOrg.getOrgName();
	  }
	  catch (Exception e) {
	    e.printStackTrace();
	  }
	  return orgName;
	  
	  
	}


	/**
	 * 返回当前组织的类型。
	 * 
	 * @return
	 */
	public SysOrgType getCurrentOrgType() {
		SysOrg sysOrg = ContextUtil.getCurrentOrg();
		if (sysOrg == null)
			return null;
		Long orgType = sysOrg.getOrgType();
		if (orgType == null)
			return null;
		SysOrgType sysOrgType = sysOrgTypeDao.getById(orgType);
		return sysOrgType;
	}

	/**
	 * 返回当前组织类型的名称。
	 * 
	 * @return
	 */
	public String getCurrentOrgTypeName() {
		SysOrg sysOrg = ContextUtil.getCurrentOrg();
		if (sysOrg == null)
			return "";
		Long orgType = sysOrg.getOrgType();
		if (orgType == null)
			return "";
		SysOrgType sysOrgType = sysOrgTypeDao.getById(orgType);
		return sysOrgType.getName();
	}

	/**
	 * 根据当前用户取得指定参数key的参数值。
	 * 脚本中使用方法:
	 * scriptImpl.getParaValue(String paramKey);
	 * @param paraName
	 *            参数名称
	 * @return
	 */
	public Object getParaValue(String paramKey) {
		Long currentUserId = ContextUtil.getCurrentUserId();

		return getParaValueByUser(currentUserId, paramKey);
	}

	/**
	 * 根据用户ID和参数key获取参数值。
	 * 脚本中使用方法:
	 * scriptImpl.getParaValueByUser(Long userId, String paramKey);
	 * @param userId
	 *            用户ID
	 * @param paramKey
	 *            参数键
	 * @return
	 */
	public Object getParaValueByUser(Long userId, String paramKey) {
		SysUserParam sysUserParam = sysUserParamDao.getByParaUserId(userId,
				paramKey);
		if (sysUserParam == null) {
			return null;
		}
		String dataType = sysUserParam.getDataType();
		if ("String".equals(dataType)) {
			return sysUserParam.getParamValue();
		} else if ("Integer".equals(dataType)) {
			return sysUserParam.getParamIntValue();
		} else {
			return sysUserParam.getParamDateValue();
		}
	}

	/**
	 * 获取流程当前用户直属领导的主岗位名称。
	 * 
	 * <pre>
	 * 脚本中使用方法:
	 * scriptImpl.getCurDirectLeaderPos();
	 * </pre>
	 * 
	 * @param userId
	 *            用户ID
	 * @return 主岗位名称
	 */
	public String getCurDirectLeaderPos() {
		String userId = ContextUtil.getCurrentUserId().toString();
		String posName = getDirectLeaderPosByUserId(userId);
		return posName;
	}

	/**
	 * 获取用户的组织的直属领导岗位。
	 * 
	 * <pre>
	 * 1.当前人是普通员工，则获取部门负责人，如果找不到，往上级查询负责人岗位。
	 * 2.当前人员是部门负责人，则获取上级部门负责人，如果找不到则往上级查询负责人岗位。
	 * <br>
	 * 脚本使用方法：
	 * scriptImpl.getDirectLeaderPosByUserId(String userId);
	 * </pre>
	 * 
	 * @param userId
	 * @return 如果有负责人则返回，没有返回null。
	 */
	public String getDirectLeaderPosByUserId(String userId) {
		String posName = "";
		posName = sysUserOrgService
				.getLeaderPosByUserId(Long.parseLong(userId));
		return posName;
	}


	public Set<String> getDirectLeaderByUserId(String userId) {
		Set<String> userSet = new HashSet<String>();
		List<TaskExecutor> userList = sysUserOrgService.getLeaderByUserId(Long
				.parseLong(userId));
		// 获取直属上司为空。

		if (BeanUtils.isEmpty(userList))
			return userSet;
		for (TaskExecutor user : userList) {
			userSet.add(user.getExecuteId());
		}

		return userSet;
	}
	/**
	 * 判断用户是否该部门的负责人
	 * 脚本使用方法：
	 * scriptImpl.isDepartmentLeader(String userId, String orgId) ;
	 * @param userId
	 *            用户ID
	 * @param orgId
	 *            部门ID
	 * @return
	 */
	public boolean isDepartmentLeader(String userId, String orgId) {
		// 根据部门ID获取部门负责人
		List<TaskExecutor> leaders = sysUserOrgService.getLeaderByOrgId(Long
				.parseLong(orgId));
		for (TaskExecutor leader : leaders) {
			if (userId.equals(leader.getExecuteId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取用户领导id集合。
	 * 脚本使用方法：
	 * scriptImpl.getLeaderByUserId(Long userId);
	 * @param userId
	 * @return
	 */
	public Set<String> getLeaderByUserId(Long userId) {
		Set<TaskExecutor> set = userUnderService.getMyLeader(userId);
		Set<String> userSet = new HashSet<String>();
		for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();) {
			userSet.add(it.next().getExecuteId());
		}
		return userSet;
	}
	
	/**
	 * 获取当前用户领导。
	 * @param userId
	 * @return
	 */
	public Set<String> getMyLeader() {
		Long userId=ContextUtil.getCurrentUserId();
		Set<String> userSet= getLeaderByUserId(userId);
		return userSet;
	}

	/**
	 * 获取用户下属用户ID集合
	 * 脚本使用方法：
	 * scriptImpl.getMyUnderUserId();
	 * @param userId
	 * @return
	 */
	public Set<String> getMyUnderUserId() {
		Long userId=ContextUtil.getCurrentUserId();
		return userUnderService.getMyUnderUserId(userId);
	}

	/**
	 * 通过表名更新数据
	 * @param businessKey 主键
	 * @param tableName 表名
	 * @param map 要更新的数据
	 */
	public void updateByTableName(String businessKey,String tableName,Map<String,Object> map){
		String sql = "";
		if(map.size()==0)return;
		Object[] objs = new Object[map.size()+1];
		int count = 0;
		for(Iterator<Entry<String, Object>> it=map.entrySet().iterator();it.hasNext();){
			Entry<String, Object> obj=it.next();
			sql += ", " + obj.getKey() + "=?";
			objs[count] = obj.getValue();
			count++;
		}
		objs[count] = businessKey;
		sql = sql.replaceFirst(",", "");
		sql = "update " + tableName + " set " + sql + " where ID=?";
		jdbcTemplate.update(sql, objs);
	}
	
	/**
	 * 获取该当前用户的组织责任人，临时用 
	 */
	public String getMgrByOrgIds(){
		Long userId = ContextUtil.getCurrentUserId();
		List<SysUser> sysUser=sysUserDao.getOrgMainUser(userId);
		if(BeanUtils.isEmpty(sysUser)) return "暂时没有资产负责人";
		return sysUser.get(0).getFullname();
	}
	
	/**
	 * 取得当前用户所在的组织某个角色对应的用户
	 * @param roleName 角色名称别名
	 * @return
	 */
	public SysUser getUserByCurOrgRoleAlias(String roleNameAlias){
		//Long userId = ContextUtil.getCurrentUserId();
		//SysUserOrg sysUserOrg=sysUserOrgService.getPrimaryByUserId(userId);
		SysOrg curOrg=ContextUtil.getCurrentOrg();
		SysRole sysRole=sysRoleDao.getByRoleAlias(roleNameAlias);
		if(curOrg!=null && sysRole!=null){
			List<SysUser> sysUserList=sysUserService.getUserByRoleIdOrgId(sysRole.getRoleId(),curOrg.getOrgId());
			if(BeanUtils.isNotEmpty(sysUserList)) sysUserList.get(0);
		}
		return null;
	}
	
	/**
	 * 取得当前用户所在的组织某个角色对应的用户名
	 * @param roleName 角色名称别名
	 * @return
	 */
	public String getUserFullnameByCurOrgRoleAlias(String roleNameAlias){
		SysUser sysUser=getUserByCurOrgRoleAlias(roleNameAlias);
		return sysUser==null?"":sysUser.getFullname();
	}
	
	/**
	 * 取得某个组织(包括当前组织)的上级部门包括某个岗位的人员列表
	 * @param startOrgId
	 * @param posName
	 * @return
	 */
	public Set<String> getUsersByOrgAndPos(Long startOrgId,String posName){
		Set<String> users=new HashSet<String>();
		if(startOrgId==null) return users;
		SysOrg sysOrg=sysOrgService.getById(startOrgId);
		List<Position> posList=positionDao.getByPosName(posName);
		if(BeanUtils.isEmpty(posList)){
			return users;
		}
		Long posId=posList.get(0).getPosId();
		String[]upOrgPaths=sysOrg.getPath().split("[.]");
		for(int i=upOrgPaths.length-1;i>0;i--){
			SysOrg tempOrg=sysOrgService.getById(new Long(upOrgPaths[i]));
			List<SysUser> sysUserList=sysUserDao.getByOrgIdPosId(tempOrg.getOrgId(), posId);
			for(SysUser sysUser:sysUserList){
				users.add(sysUser.getUserId().toString());
			}
		}
		return users;
	}
	
	
	

	/**
	 * 根据业务主键和表名查看子表是否有数据。
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public boolean isSubTableHasData(String tableName,Long fk){
		List<Map<String, Object>> list= bpmFormHandlerDao.getByFk(tableName, fk);
		return list.size()>0;
	}
	
	/**
	 * 根据表名和外键获取数据。
	 * @param tableName
	 * @param fk
	 * @return
	 */
	public List<Map<String, Object>> getByFk(String tableName,Long fk){
		List<Map<String, Object>> list= bpmFormHandlerDao.getByFk(tableName, fk);
		return list;
	}
	
	/**
	 * 在子流程中获取父流程某个节点的审批人。
	 * @param flowRunId		子流程的RUNID
	 * @param nodeId		主流程的节点ID
	 * @return
	 */
	public Set<String> getAuditByMainInstId(Long flowRunId,String nodeId){
		Set<String> set=new LinkedHashSet<String>();
		ProcessRun processRun= processRunService.getById(flowRunId);
		
		if(processRun==null){
			return set;
		}
		if(processRun.getParentId()!=null){
			processRun= processRunService.getById(processRun.getParentId());
			TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(Long.parseLong(processRun.getActInstId()) , nodeId);
			set.add(taskOpinion.getExeUserId().toString());
		}
		return set;
	}
	
	/**
	 * 根据员工ID获取父母信息
	 * @param parentStr表示要获取父亲还是母亲
	 * @return
	 * @throws Exception 
	 */  
	/*public String getPersonParentsInfo(String parentStr) throws Exception
	{
		 String strSQL=null;
		 String rtnStr=null;
		 strSQL="select a.name  from sm_emp_sub_family a , sm_staffinfo_main b"+
		             " WHERE a.EMPLOYEE_ID=b.id and b.LOCALSTAFFCODE='"+
				         String.valueOf(ContextUtil.getCurrentUserId())+
				      "' and relation='"+parentStr+"'";
		 List<Map<String, Object>> list = null;
		 list=personaldao.queryForList(strSQL);
		 if (list.size()>0)
		 {
			 rtnStr=list.get(0).get("name").toString();
		 }
		 return rtnStr;	 
	}	*/
}
