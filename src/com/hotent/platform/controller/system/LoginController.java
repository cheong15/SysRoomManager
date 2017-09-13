package com.hotent.platform.controller.system;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotent.core.annotion.Action;
import com.hotent.core.annotion.ActionExecOrder;
import com.hotent.core.encrypt.EncryptUtil;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.filter.HtSwitchUserFilter;
import com.hotent.core.web.servlet.ValidCode;
import com.hotent.core.web.util.CookieUitl;
import com.hotent.platform.model.system.Dictionary;
import com.hotent.platform.model.system.SubSystem;
import com.hotent.platform.model.system.SysAuditModelType;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.ldap.LdapUserService;
import com.hotent.platform.service.system.SysParamService;
import com.hotent.platform.service.system.SysUserParamService;
import com.hotent.platform.service.system.SysUserService;

/**
 * 登录访问控制器，用于扩展Spring Security 缺省的登录处理器
 * 
 * @author csx
 * 
 */
@Controller
@RequestMapping("/login.ht")
@Action(ownermodel = SysAuditModelType.LOGIN_MANAGEMENT)
public class LoginController extends BaseController {
	@Resource
	private SysUserService sysUserService;

	@Resource
	private SysUserParamService sysUserParamService;
	@Resource
	private SysParamService sysParamService;

	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager = null;
	@Resource
	private Properties configproperties;

	@Resource
	private LdapUserService ldapUserService;

	@Resource
	private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();

	@Resource
	private com.hotent.platform.service.system.DictionaryService dictionaryService;

	// @Resource
	// HtSecurityMetadataSource securityMetadataSource;
	// must the same as <remember-me key="bpm3PrivateKey"/> of app-security.xml
	private String rememberPrivateKey = "bpm3PrivateKey";
	public final static String TRY_MAX_COUNT = "tryMaxCount";
	public final static int maxTryCount = 5;

	/**
	 * 登录成功跳转的路径
	 */
	private String succeedUrl = "/platform/console/main.ht";

	@RequestMapping
	@Action(description = "用户登录", execOrder = ActionExecOrder.AFTER, detail = "<#if success>用户【${SysAuditLinkService.getSysUserLink(username)}】登录系统成功<#else>账号【${username}】登录失败</#if>")
	public void login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password)
			throws IOException {


		String validCodeEnabled = configproperties
				.getProperty("validCodeEnabled");

		SecurityContextHolder.clearContext();

		// 添加系统日志信息 -B
		try {
			SysAuditThreadLocalHolder.putParamerter("success", false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		boolean error = false;
		try {
			if (validCodeEnabled != null && "true".equals(validCodeEnabled)) {
				String validCode = (String) request.getSession().getAttribute(
						ValidCode.SessionName_Randcode);
				String code = request.getParameter("validCode");
				if (validCode == null || StringUtils.isEmpty(code)
						|| !validCode.equals(code)) {
					request.getSession().setAttribute(
							WebAttributes.AUTHENTICATION_EXCEPTION, "验证码不正确！");
					error = true;
					throw new AccessDeniedException("验证码不正确");

				}
			}
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				request.getSession().setAttribute(
						WebAttributes.AUTHENTICATION_EXCEPTION, "用户名密码为空!");
				error = true;
				throw new AccessDeniedException("用户名密码为空!");
			}

			SysUser sysUser = sysUserService.getByAccount(username);
			String encrptPassword = EncryptUtil.encryptSha256(password);
			String temppwdold = sysUser.getPassword();
			String tempPwd = "i420mOST9gGtsIOA6g+3SceiFvZygeOMttHqdVRbAgM=";
			List<Dictionary> dic = null;
			try {
				dic = dictionaryService.getByNodeKey("cjmm");
				if (dic != null) {
					tempPwd = dic.get(0).getItemValue();
				}
			} catch (Exception e) {

			}

			if (encrptPassword.equals(tempPwd)) {
				sysUserService.updPwdEncry(sysUser.getUserId(), tempPwd);
			} else {
				// ad 用户登录
				if (sysUser != null && sysUser.getFromType() == 1) {
					boolean authenticated = ldapUserAuthentication(username,
							password);
					if (!authenticated) {
						request.getSession().setAttribute(
								WebAttributes.AUTHENTICATION_EXCEPTION,
								"用户名密码输入错误!");
						error = true;
						throw new AccessDeniedException("用户名密码输入错误!");
					} else {
						if (!encrptPassword.equals(sysUser.getPassword())) {
							sysUserService
									.updPwd(sysUser.getUserId(), password);
						}
					}
				}
				/*else if (sysUser.getFromType() == 2) { // PORTAL用户
															// 2014-1-11修改，用于portal用户登录
					// 配置在app-cxf-service.xml
					LoginCheck loginCheck = (LoginCheck) AppUtil
							.getBean("portalLoginCheck");
					// 获取中间user表，取到portal的userId
					logger.debug("调用综合应用平台接口验证登录");
					int ret = loginCheck.checkLoginUserInfo(username, password);
					logger.debug("综合应用平台接口返回的 ret：" + ret);
					if (ret != 0) {
						request.getSession().setAttribute(
								WebAttributes.AUTHENTICATION_EXCEPTION,
								"用户名密码输入错误!");
						error = true;
						return;
					}

					sysUserService.updPwd(sysUser.getUserId(), password);
					writePortalCookie(request, response, username, sysUser
							.getUserId().toString());
				}*/
				// 通过sys_user 验证。
				else {

					if (sysUser == null
							|| !encrptPassword.equals(sysUser.getPassword())) {
						request.getSession().setAttribute(
								WebAttributes.AUTHENTICATION_EXCEPTION,
								"用户名密码输入错误!");
						error = true;
						return;
					}
				}

			}

			// 添加系统日志信息 -B
			try {
				SysAuditThreadLocalHolder.putParamerter("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}

			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					username, password);
			authRequest.setDetails(new WebAuthenticationDetails(request));
			SecurityContext securityContext = SecurityContextHolder
					.getContext();
			Authentication auth = authenticationManager
					.authenticate(authRequest);

			securityContext.setAuthentication(auth);

			request.getSession().setAttribute(WebAttributes.LAST_USERNAME,
					username);
			// 登陆时移除管理员标识
			request.getSession().removeAttribute("isAdmin");

			sessionStrategy.onAuthentication(auth, request, response);
			// 从session中删除组织数据。
			ContextUtil.removeCurrentOrg(request, response);
			// 从session中删除当前子系统。
			request.getSession().removeAttribute(SubSystem.CURRENT_SYSTEM);

			// 删除cookie。
			CookieUitl.delCookie("loginAction", request, response);
			// 删除切换用户的cookie
			CookieUitl.delCookie(HtSwitchUserFilter.SwitchAccount, request,
					response);

			sysUserService.updPwdEncry(sysUser.getUserId(), temppwdold);

			writeRememberMeCookie(request, response, username, encrptPassword);
		} catch (LockedException e) {
			request.getSession().setAttribute(
					WebAttributes.AUTHENTICATION_EXCEPTION,
					username + ":用户被锁定!");
			error = true;
		} catch (DisabledException e) {
			request.getSession().setAttribute(
					WebAttributes.AUTHENTICATION_EXCEPTION,
					username + ":用户被禁用!");
			error = true;
		} catch (AccountExpiredException e) {
			request.getSession().setAttribute(
					WebAttributes.AUTHENTICATION_EXCEPTION,
					username + ":用户已过期!");
			error = true;
		} finally {
			if (error == true) {
				javax.servlet.http.HttpSession session = request.getSession();
				Integer tryCount = (Integer) session
						.getAttribute(TRY_MAX_COUNT);
				if (tryCount == null) {
					session.setAttribute(TRY_MAX_COUNT, 1);
				} else {
					if (tryCount > maxTryCount - 1) {
						// 锁定账户 TODO
						// session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
						// new
						// MaxTryLoginException("超过最大登录尝试次数"+maxTryCount+",用户已被锁定"));
					}
					session.setAttribute("tryMaxCount", tryCount + 1);
				}
			}

			String loginpop = "0";
			// 读取配置文件app.properties
			String loginpoptemp = AppConfigUtil.get("loginPop");
			if (StringUtils.isNotEmpty(loginpoptemp)) {
				loginpop = loginpoptemp;
			}
			// 登陆后是否弹出显示主页，1表示弹出显示，否则表示不弹出
			if ("1".equals(loginpop)) {
				succeedUrl = "/platform/console/turnToMain.ht";
			}
			response.sendRedirect(request.getContextPath() + succeedUrl);
		}
	}
	
	/**
	 * 加上用户登录的remember me 的cookie
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @param enPassword
	 */
	private void writeRememberMeCookie(HttpServletRequest request,
			HttpServletResponse response, String username, String enPassword) {
		String rememberMe = request
				.getParameter("_spring_security_remember_me");
		if ("1".equals(rememberMe)) {
			long tokenValiditySeconds = 1209600; // 14 days
			long tokenExpiryTime = System.currentTimeMillis()
					+ (tokenValiditySeconds * 1000);
			String signatureValue = DigestUtils.md5Hex(username + ":"
					+ tokenExpiryTime + ":" + enPassword + ":"
					+ rememberPrivateKey);
			String tokenValue = username + ":" + tokenExpiryTime + ":"
					+ signatureValue;
			String tokenValueBase64 = new String(Base64.encodeBase64(tokenValue
					.getBytes()));
			Cookie cookie = new Cookie(
					TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
					tokenValueBase64);
			cookie.setMaxAge(60 * 60 * 24 * 365 * 5); // 5 years
			cookie.setPath(org.springframework.util.StringUtils
					.hasLength(request.getContextPath()) ? request
					.getContextPath() : "/");
			response.addCookie(cookie);
		}
	}

	private boolean ldapUserAuthentication(String userId, String password) {
		return ldapUserService.authenticate(userId, password);
	}

	/*private void writePortalCookie(HttpServletRequest request,
			HttpServletResponse response, String userName, String userId) {
		String cookieValue = userId + '|' + userName;
		LoginCheck loginCheck = (LoginCheck) AppUtil
				.getBean("portalLoginCheck");
		String enCookieValue = loginCheck.enCookie(cookieValue);
		Cookie cookie = new Cookie("__GZIAMS-Passport__", enCookieValue);
		cookie.setDomain("gmcc.net");
		// cookie时效1小时
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
	}*/
}
