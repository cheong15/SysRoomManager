package com.hotent.platform.controller.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.system.SysOrg;
import com.hotent.platform.model.system.SysOrgParam;
import com.hotent.platform.model.system.SysParam;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.system.SysOrgParamService;
import com.hotent.platform.service.system.SysOrgService;
import com.hotent.platform.service.system.SysParamService;
import com.hotent.platform.service.system.SysUserService;

/**
 * 对象功能:组织参数属性 控制器类 开发公司:广州宏天软件有限公司 开发组织:csx 创建时间:2012-02-24 10:04:50
 */
@Controller
@RequestMapping("/platform/system/sysOrgParam/")
public class SysOrgParamController extends BaseController {
	@Resource
	private SysOrgParamService sysOrgParamService;
	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private SysParamService sysParamService;
	@Resource
	private SysUserService sysUserService;

	/**
	 * 取得组织参数属性分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editByOrgId")
	@Action(description = "修改组织参数属性分页列表")
	public ModelAndView editByOrgId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		long orgId = RequestUtil.getLong(request, "orgId");
		SysOrg sysOrg = sysOrgService.getById(orgId);
		List<SysOrgParam> list = sysOrgParamService.getAll(new QueryFilter(
				request, "sysOrgParamItem"));
		List<SysParam> sysParamList = sysParamService.getOrgParam();

		ModelAndView mv = this.getAutoView().addObject("sysOrgParamList", list)
				.addObject("sysParamList", sysParamList)
				.addObject("orgId", orgId).addObject("sysOrg", sysOrg);
		return mv;
	}

	@RequestMapping("saveByOrgId")
	@Action(description = "编辑组织参数属性")
	public void saveByOrgId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		try {
			long orgId = RequestUtil.getLong(request, "orgId");
			String[] paramId = request.getParameterValues("paramId");
			String[] paramValue = request.getParameterValues("paramValue");
			List<SysOrgParam> valueList = coverBean(orgId, paramId, paramValue);
			sysOrgParamService.add(orgId, valueList);

			resultMessage = new ResultMessage(ResultMessage.Success,
					"编辑组织参数属性成功");
			out.print(resultMessage.toString());
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,"编辑组织参数属性失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	private List<SysOrgParam> coverBean(long orgId, String[] paramId,
			String[] paramValue) throws Exception {
		List<SysOrgParam> list = new ArrayList<SysOrgParam>();
		if (paramId == null || paramId.length == 0)
			return list;
		for (int i = 0; i < paramId.length; i++) {
			String p = paramId[i];
			String v = paramValue[i];
			if (p == null || p.equals(""))
				continue;
			SysOrgParam param = new SysOrgParam();
			param.setValueId(UniqueIdUtil.genId());
			param.setParamId(Long.parseLong(p));
			param.setParamValue(v);
			param.setOrgId(orgId);

			String dataType = sysParamService.getById(Long.parseLong(p))
					.getDataType();
			if (SysParam.DATA_TYPE_MAP.get(dataType) != null
					&& SysParam.DATA_TYPE_MAP.get(dataType).equals("数字")) {
				param.setParamIntValue(Long.parseLong(v));
			} else if (SysParam.DATA_TYPE_MAP.get(dataType) != null
					&& SysParam.DATA_TYPE_MAP.get(dataType).equals("日期")) {
				param.setParamDateValue(SysParam.PARAM_DATE_FORMAT.parse(v));
			}

			list.add(param);
		}
		return list;
	}

	@RequestMapping("dialog")
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();
//		Long nodeUserId = RequestUtil.getLong(request, "nodeUserId");
//		BpmNodeUser nu = bpmNodeUserService.getById(nodeUserId);
//		if (nu != null) {
//			String cmpIds = nu.getCmpIds();
//			cmpIds = SysParamService.setParamIcon(request.getContextPath(),
//					cmpIds);
//			String cmpNames = nu.getCmpNames();
//			mv.addObject("cmpIds", cmpIds);
//			mv.addObject("cmpNames", cmpNames);
//		} else {
//			String cmpIds = RequestUtil.getString(request, "cmpIds");
//			cmpIds = SysParamService.setParamIcon(request.getContextPath(),
//					cmpIds);
//			String cmpNames = RequestUtil.getString(request, "cmpNames");
//			mv.addObject("cmpIds", cmpIds);
//			mv.addObject("cmpNames", cmpNames);
//		}

		List<SysParam> sysParamList = sysParamService.getOrgParam();

		mv.addObject("sysParamList", sysParamList).addObject("conditionUS",
				SysParam.CONDITION_US);
		return mv;
	}

	@RequestMapping("getByParamKey")
	public ModelAndView getByParamKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();
		String orgParam = RequestUtil.getString(request, "orgParam");
		int postFlag=RequestUtil.getInt(request, "postflag");  
		orgParam = new String(orgParam.getBytes("ISO8859_1"), "utf-8");
		List<SysUser> userList = sysUserService.getByOrgParam(orgParam);
		mv.addObject("userList", userList);
		mv.addObject("postFlag",postFlag);
		return mv;
	}

}
