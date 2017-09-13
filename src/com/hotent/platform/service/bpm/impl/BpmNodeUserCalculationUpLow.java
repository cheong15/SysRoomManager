package com.hotent.platform.service.bpm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.core.model.TaskExecutor;
import com.hotent.platform.model.bpm.BpmNodeUser;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.bpm.CalcVars;
import com.hotent.platform.service.bpm.IBpmNodeUserCalculation;
import com.hotent.platform.service.system.SysUserService;

/**
 * 根据节点用户设置为“上下级”，计算执行人员。
 * 
 * @author Raise
 */
public class BpmNodeUserCalculationUpLow implements IBpmNodeUserCalculation {
	@Resource
	private SysUserService sysUserService;

	@Override
	public List<SysUser> getExecutor(BpmNodeUser bpmNodeUser, CalcVars vars) {
		Long startUserId = vars.getStartUserId();
		Long prevExecUserId = vars.getPrevExecUserId();
		if(startUserId==null || startUserId.intValue()==0) return new ArrayList<SysUser>();
		List<SysUser> users = new ArrayList<SysUser>();
		if(prevExecUserId==null || prevExecUserId.intValue()==0) {
			users = sysUserService.getByUserIdAndUplow(startUserId);
		}
		else {
			users = sysUserService.getByUserIdAndUplow(startUserId, bpmNodeUser);
		}
		return users;
	}

	@Override
	public String getTitle() {
		return "上下级";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(BpmNodeUser bpmNodeUser, CalcVars vars) {
		int extractUser=bpmNodeUser.getExtractUser();
		List<SysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
		Set<TaskExecutor> uIdSet =BpmNodeUserUtil.getExcutorsByUsers(sysUsers, extractUser);
		return uIdSet;
	}

	@Override
	public boolean supportMockModel() {
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(BpmNodeUser bpmNodeUser) {
		List< PreViewModel> list=new ArrayList<PreViewModel>();
		PreViewModel preViewModel=new PreViewModel();
		preViewModel.setType(PreViewModel.START_USER);
		
		list.add(preViewModel);
		
		return list;
	}

	@Override
	public boolean supportPreView() {
		return true;
	}

	
}
