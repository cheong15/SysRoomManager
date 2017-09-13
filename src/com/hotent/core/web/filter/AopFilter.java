package com.hotent.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.core.util.ContextUtil;
import com.hotent.core.web.util.RequestUtil;

/**
 * 用于拦截请求以获取HttpSevletRequest对象，以供后续Aop类使用,以获取当前用户请求的IP等信息。
 *  用于相同线程间共享Request对象 。
 * 
 * @author csx
 * 
 */
public class AopFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException{
		try{
			ContextUtil.clearAll();
			RequestUtil.setHttpServletRequest((HttpServletRequest) request);
			RequestUtil.setHttpServletResponse((HttpServletResponse)response);
			Long userId=ContextUtil.getCurrentUserId();
			if(userId!=null && userId>0){
				ContextUtil.getCurrentOrgFromSession();
			}
			chain.doFilter(request, response);
		}
		finally{
			ContextUtil.clearAll();
		}
	}
	

	@Override
	public void destroy()
	{
	}

}
