package com.hotent.platform.controller.system;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.web.controller.BaseController;
import com.hotent.platform.service.system.SysUserService;

/**
 * 登录访问控制器，用于扩展Spring Security 缺省的登录处理器
 *
 * @author csx
 */
@Controller
public class SsoController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager = null;

    @Resource
    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();

    private final String systemId = "GZHR_ACCESSRIGHT";

    /**
     * 登录成功跳转的路径
     */
    private String succeedUrl = "/platform/console/main.ht";

    @RequestMapping("/sso.ht")
    private void sso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        // 添加系统日志信息 -B
        try {
            SysAuditThreadLocalHolder.putParamerter("success", false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        String token = "";
        Cookie[] cookie = request.getCookies();
        for (int i = 0; i < cookie.length; i++) {
            Cookie cook = cookie[i];
            if (cook.getName().equalsIgnoreCase("iPlanetDirectoryPro")) {
                // 获取键
                token = cook.getValue();
            }
        }
        /*if (StringUtils.isNotBlank(token)) {
            //以下是测试帐号的验证操作
            UIPService_Impl impl = new UIPService_Impl();
            UIPServiceIF uip = impl.getUIPServiceIFPort();
            try {
                AuthResult result = uip.validateToken(token, systemId);//验证portal令牌
                if (result.isAuthResult()) {//令牌验证成功
                    String account = result.getAccount();
                    String defaultPassword = "abc123!@#";
                    SysUser sysUser = sysUserService.getByAccount(account);
                    // 验证成功，通过
                    String encrptPassword = EncryptUtil.encryptSha256(defaultPassword);
                    // 修改为临时密码
                    sysUserService.updPwdEncry(sysUser.getUserId(), encrptPassword);
                    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(sysUser.getAccount(), defaultPassword);
                    authRequest.setDetails(new WebAuthenticationDetails(request));

                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    Authentication auth = authenticationManager.authenticate(authRequest);
                    securityContext.setAuthentication(auth);
                    request.getSession().setAttribute(WebAttributes.LAST_USERNAME, sysUser.getAccount());
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
                    CookieUitl.delCookie(HtSwitchUserFilter.SwitchAccount, request, response);
                    sysUserService.updPwdEncry(sysUser.getUserId(), sysUser.getPassword());
                    response.sendRedirect(request.getContextPath() + succeedUrl);
                    return;
                } else {
                    request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "令牌验证失败.因原是：" + result.getAuthMsg());
                }
            } catch (LockedException e) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "用户已被锁定!");
            } catch (DisabledException e) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "用户已被禁用!");
            } catch (AccountExpiredException e) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "用户已过期!");
            }
        }*/
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

//    @Resource
//    private SysUserService sysUserService;
//
//    @Resource
//    private SysUserParamService sysUserParamService;
//    @Resource
//    private SysParamService sysParamService;
//
//    @Resource(name = "authenticationManager")
//    private AuthenticationManager authenticationManager = null;
//    @Resource
//    private Properties configproperties;
//
//    @Resource
//    private LdapUserService ldapUserService;
//
//    @Resource
//    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
//
//    @Resource
//    private com.hotent.platform.service.system.DictionaryService dictionaryService;
//
//    // @Resource
//    // HtSecurityMetadataSource securityMetadataSource;
//    // must the same as <remember-me key="bpm3PrivateKey"/> of app-security.xml
//    private String rememberPrivateKey = "bpm3PrivateKey";
//    public final static String TRY_MAX_COUNT = "tryMaxCount";
//    public final static int maxTryCount = 5;
//
//    /**
//     * 登录成功跳转的路径
//     */
//    private String succeedUrl = "/platform/console/main.ht";
//
//    @RequestMapping
//    @Action(description = "单点登录")
//    private void sso(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String cookv = null;
//        Cookie[] cookie = request.getCookies();
//
//        for (int i = 0; i < cookie.length; i++) {
//            Cookie cook = cookie[i];
//            if (cook.getName().equalsIgnoreCase("__GZIAMS-Passport__")) {
//                // 获取键
//                cookv = cook.getValue();
//            } else if (cook.getName().equalsIgnoreCase("iPlanetDirectoryPro")) {
//                // 获取键
//                cookv = cook.getValue();
//            }
//        }
//        LoginCheck loginCheck = (LoginCheck) AppUtil
//                .getBean("portalLoginCheck");
//        if (cookv != null) {
//            String username = loginCheck.getAccount(cookv);
//            if (username != null) {
//                SysUser sysUser = sysUserService.getByAccount(username);
//                String encrptPassword = EncryptUtil.encryptSha256("abc123!@#");
//                String temppwdold = sysUser.getPassword();
//                // 修改为临时密码
//                sysUserService.updPwdEncry(sysUser.getUserId(), encrptPassword);
//                // 使用临时密码验证
//                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
//                        username, "abc123!@#");
//                authRequest.setDetails(new WebAuthenticationDetails(request));
//                SecurityContext securityContext = SecurityContextHolder
//                        .getContext();
//                Authentication auth = authenticationManager
//                        .authenticate(authRequest);
//                securityContext.setAuthentication(auth);
//
//                request.getSession().setAttribute(WebAttributes.LAST_USERNAME,
//                        username);
//                // 登陆时移除管理员标识
//                request.getSession().removeAttribute("isAdmin");
//
//                sessionStrategy.onAuthentication(auth, request, response);
//                // 从session中删除组织数据。
//                ContextUtil.removeCurrentOrg(request, response);
//                // 从session中删除当前子系统。
//                request.getSession().removeAttribute(SubSystem.CURRENT_SYSTEM);
//                // 删除cookie。
//                CookieUitl.delCookie("loginAction", request, response);
//                // 删除切换用户的cookie
//                CookieUitl.delCookie(HtSwitchUserFilter.SwitchAccount, request,
//                        response);
//                // 修改回原密码
//                sysUserService.updPwdEncry(sysUser.getUserId(), temppwdold);
//                writeRememberMeCookie(request, response, username,
//                        encrptPassword);
//
//                response.sendRedirect(request.getContextPath() + succeedUrl);
//            }
//        }
//
//        response.sendRedirect(request.getContextPath() + "/login.jsp");
//    }
//
//    /**
//     * 加上用户登录的remember me 的cookie
//     *
//     * @param request
//     * @param response
//     * @param username
//     * @param enPassword
//     */
//    private void writeRememberMeCookie(HttpServletRequest request,
//                                       HttpServletResponse response, String username, String enPassword) {
//        String rememberMe = request
//                .getParameter("_spring_security_remember_me");
//        if ("1".equals(rememberMe)) {
//            long tokenValiditySeconds = 1209600; // 14 days
//            long tokenExpiryTime = System.currentTimeMillis()
//                    + (tokenValiditySeconds * 1000);
//            String signatureValue = DigestUtils.md5Hex(username + ":"
//                    + tokenExpiryTime + ":" + enPassword + ":"
//                    + rememberPrivateKey);
//            String tokenValue = username + ":" + tokenExpiryTime + ":"
//                    + signatureValue;
//            String tokenValueBase64 = new String(Base64.encodeBase64(tokenValue
//                    .getBytes()));
//            Cookie cookie = new Cookie(
//                    TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
//                    tokenValueBase64);
//            cookie.setMaxAge(60 * 60 * 24 * 365 * 5); // 5 years
//            cookie.setPath(org.springframework.util.StringUtils
//                    .hasLength(request.getContextPath()) ? request
//                    .getContextPath() : "/");
//            response.addCookie(cookie);
//        }
//    }
//
//    private boolean ldapUserAuthentication(String userId, String password) {
//        return ldapUserService.authenticate(userId, password);
//    }
//
//    private void writePortalCookie(HttpServletRequest request,
//                                   HttpServletResponse response, String userName, String userId) {
//        String cookieValue = userId + '|' + userName;
//        LoginCheck loginCheck = (LoginCheck) AppUtil
//                .getBean("portalLoginCheck");
//        String enCookieValue = loginCheck.enCookie(cookieValue);
//        Cookie cookie = new Cookie("__GZIAMS-Passport__", enCookieValue);
//        cookie.setDomain("gmcc.net");
//        // cookie时效1小时
//        cookie.setMaxAge(3600);
//        response.addCookie(cookie);
//    }
}
