package com.hotent.platform.controller.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hotent.platform.model.system.SysParam;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.model.system.SysUserParam;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.system.SysParamService;
import com.hotent.platform.service.system.SysUserParamService;
import com.hotent.platform.service.system.SysUserService;

/**
 * 对象功能:人员参数属性 控制器类 开发公司:广州宏天软件有限公司 开发人员:csx 创建时间:2012-02-24 10:04:50
 */
@Controller
@RequestMapping("/platform/system/sysUserParam/")
public class SysUserParamController extends BaseController {
	@Resource
	private SysUserParamService sysUserParamService;
	@Resource
	private SysParamService sysParamService;
	@Resource
	private SysUserService sysUserService;
	protected Logger logger = LoggerFactory.getLogger(SysUserParamController.class);

	/**
	 * 取得人员参数属性分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editByUserId")
	@Action(description = "修改人员参数属性分页列表")
	public ModelAndView editByUserId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		long userId = RequestUtil.getLong(request, "userId");
		SysUser user = sysUserService.getById(userId);
		List<SysParam> sysParamList = sysParamService.getUserParam();

		List<SysUserParam> list = sysUserParamService.getAll(new QueryFilter(
				request, "sysUserParamItem"));
		ModelAndView mv = this.getAutoView()
				.addObject("sysUserParamList", list)
				.addObject("sysParamList", sysParamList)
				.addObject("userId", userId).addObject("user", user)
				.addObject("returnUrl", returnUrl);
		return mv;
	}

	@RequestMapping("saveByUserId")
	@Action(description = "编辑人员参数属性")
	public void saveByUserId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		try {
			long userId = RequestUtil.getLong(request, "userId");
			String[] paramId = request.getParameterValues("paramId");
			String[] paramValue = request.getParameterValues("paramValue");
			List<SysUserParam> valueList = coverBean(userId, paramId,
					paramValue);
			sysUserParamService.add(userId, valueList);

			ResultMessage message = new ResultMessage(ResultMessage.Success,
					"编辑人员参数属性成功");
			out.print(message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,"编辑人员参数属性失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	private List<SysUserParam> coverBean(long uesrId, String[] paramId,
			String[] paramValue) throws Exception {
		List<SysUserParam> list = new ArrayList<SysUserParam>();
		if (paramId == null || paramId.length == 0)
			return list;
		for (int i = 0; i < paramId.length; i++) {
			String p = paramId[i];
			String v = paramValue[i];
			if (p == null || p.equals(""))
				continue;
			SysUserParam param = new SysUserParam();
			param.setValueId(UniqueIdUtil.genId());
			param.setParamId(Long.parseLong(p));
			param.setParamValue(v);
			param.setUserId(uesrId);

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
		Enumeration attrNames = request.getAttributeNames();
		while(attrNames.hasMoreElements()){
			logger.info(attrNames.nextElement().toString());
		}
		
		ModelAndView mv = this.getAutoView();

		List<SysParam> sysParamList = sysParamService.getUserParam();
		mv.addObject("sysParamList", sysParamList);
		return mv;
	}

	@RequestMapping("getByParamKey")
	public ModelAndView getByParamKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();
		int postFlag=RequestUtil.getInt(request, "postflag");  
		String userParam = RequestUtil.getString(request, "userParam");
		List<SysUser> userList = sysUserService.getByUserParam(userParam);
		mv.addObject("userList", userList);
		mv.addObject("postFlag",postFlag);
		return mv;
	}	
}
