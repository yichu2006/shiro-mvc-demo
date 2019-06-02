package com.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * shiro 里面默认判断的是 所有的角色和权限
 * AuthorizationFilter 已经把 onAccessDenied：表示访问拒绝，提供了实现
 * 自定义filter 改写 只要满足一个权限 即可
 */
public class MyShiroFilter extends AuthorizationFilter {

    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        String[] roles = (String[])o;
        if(roles == null || roles.length == 0) {
            return true;
        }
        for(String role : roles) {
            if(subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}






























