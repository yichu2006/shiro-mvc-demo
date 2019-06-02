package com.shiro.filter;

import org.apache.shiro.config.Ini;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    private static final String ROLE_STRING = "roles[{0}]";

    //存放spring-config中默认配置的权限
    private String filterChainDefinitions;

    //setFilterChainDefinitions 方法只在启动的时候加载
    public void setFilterChainDefinitions(String definitions) {

        //存放spring-config中默认配置的权限
        filterChainDefinitions = definitions;

        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }

        //上面这段直接，从父类中copy

        //模拟从数据库中读取的数据   重点重点重点：这里以后都是从数据库读取
        Map<String,String[]> permsMap = new HashMap<String,String[]>();
        permsMap.put("/dotest1.html", new String[]{"test"});
        permsMap.put("/dotest2.html", new String[]{"test","guest"});

        //下面这句通过模拟 数据库动态加权限生效，通过下面的update方法
        // permsMap.put("/dotest3.html", new String[]{"admin"});

        for(String url : permsMap.keySet()) {
            String[] roles = permsMap.get(url);
            StringBuilder sb = new StringBuilder();
            for(String role : roles) {
                sb.append(role).append(",");
            }
            String str = sb.substring(0, sb.length()-1);
            //shiro里面是这种格式 roles[test,guest]， 所以这里替换占位符
            section.put(url, MessageFormat.format(ROLE_STRING,str));
        }
        this.setFilterChainDefinitionMap(section);
    }

    public void update() {
        synchronized (this) {
            try{
                AbstractShiroFilter shiroFilter = (AbstractShiroFilter)this.getObject();
                PathMatchingFilterChainResolver resolver =
                        (PathMatchingFilterChainResolver)shiroFilter.getFilterChainResolver();
                DefaultFilterChainManager manager = (DefaultFilterChainManager)resolver.getFilterChainManager();

                //清楚原来的权限验证
                manager.getFilterChains().clear();
                this.getFilterChainDefinitionMap().clear();

                //动态设置权限验证: 添加默认的；
                this.setFilterChainDefinitions(filterChainDefinitions);
                /**
                 * 这里重点解释下：因为后期动态加权限都是从数据库读取，所以这里清楚完所有权限，之后只要加 spring-config中默认配置的权限
                 * setFilterChainDefinitions 这个方法中 permsMap 就是从数据库读取的最新的权限
                 */

                //这个只是模拟，实际是不成立的，只能设置一次
                //this.setFilterChainDefinitions("/dotest3.html=authc,roles[admin]");

                //从父类中copy的
                Map<String, String> chains = this.getFilterChainDefinitionMap();
                if (!CollectionUtils.isEmpty(chains)) {
                    Iterator var12 = chains.entrySet().iterator();

                    while(var12.hasNext()) {
                        Map.Entry<String, String> entry = (Map.Entry)var12.next();
                        String url = (String)entry.getKey();
                        String chainDefinition = (String)entry.getValue();
                        manager.createChain(url, chainDefinition);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
