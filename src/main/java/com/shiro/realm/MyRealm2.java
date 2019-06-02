package com.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 缓存功能的realm，自带认证、授权： 重写 AuthorizingRealm
 */
public class MyRealm2 extends AuthorizingRealm {

    //这里可以  @Autowired 注入数据源，操作数据库

    /**
     * 权限验证调用  这块还有点问题
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        String username = (String)principalCollection.getPrimaryPrincipal();
        //根据用户名可以执行sql，查询出该用户拥有的角色
        if("test".equals(username)) {
            authorizationInfo.addRole("test");
        }
        else if("root".equals(username)) {
            authorizationInfo.addRole("admin");
        }

        return authorizationInfo;

        /*        authorizationInfo.addRole("role1");
        authorizationInfo.addRole("role2");
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
        authorizationInfo.addStringPermission("+user2+10");
        authorizationInfo.addStringPermission("user2:*");*/
    }

    /**
     * 登录的时候调用
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String)token.getPrincipal();  //得到用户名
        String password = new String((char[])token.getCredentials()); //得到密码  这里应该调用数据库，查询密码
        //这里 用户名、密码 是要从数据库取的，这里先写死 test 000000
        if(!"root".equals(username)) {
            throw new UnknownAccountException(); //如果用户名错误
        }
        if(!"123456".equals(password)) {
            throw new IncorrectCredentialsException(); //如果密码错误
        }


        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username, password, getName());


//        String sql = "select PASSWORD from shiro_user where USER_NAME = ?";
//        String username = (String) authenticationToken.getPrincipal();
//        String password = jdbcTemplate.queryForObject(sql, String.class, username);
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, null, getName());  //如果有加盐
//        return info;
    }
}
