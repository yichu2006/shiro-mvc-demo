

### 一、数据源采用 ini 的方式

> **可以实现的功能**
- 用户登录认证：进行用户名、密码校验 （都是通过ini配置文件和spring-config配置文件）
- 授权校验
    - 通过修改 spring-config.xml 配置 /menu/**=authc,roles[admin],perms[menu:*]
        - test用户登录，访问 http://localhost:8080/menu/list.html 显示权限不足
        - root用户登录，可以则可以访问

> **通过注解的方式：授权校验**
- 需要在父子容器中配置
- 在controller的方法上加权限**注解** @RequiresPermissions("menu:edit")
- 修改 spring-config.xml 配置 /menu/**=authc
- 访问效果如下
    - test用户登录，/menu/list.html 可以访问，/menu/go_edit.html 报错 org.apache.shiro.authz.UnauthorizedException: Subject does not have permission [menu:edit]
    - root用户登录，/menu/list.html ，/menu/go_edit.html 都可以访问
        - 通过查看 ini 配置文件可知: test用户**没有** menu:edit 菜单权限
        ```
        [users]
        root = 123456, admin
        guest = guest, guest
        test = 123456, guest,test
            
        [roles]
        admin=*
        guest=user:list
        test=menu:list,menu:add
        ```

 > **异常处理**
 - 编写异常处理类 AuthExceptionHandler
 - 在父容器中 包扫描排除 controller
     ```
     <!-- 父容器扫描排除 controller注解，异常处理类才能有效 -->
     <context:component-scan base-package="com.shiro">
         <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller" />
     </context:component-scan>
     ```
        
 > **页面shiro标签控制权限**
```
<shiro:hasPermission name="menu:list">
<li data-option="url:'/menu/list.html'">
    <span>菜单管理</span>
</li>
</shiro:hasPermission>
<shiro:hasRole name="admin">
<li>
    <span>角色管理</span>
</li>
</shiro:hasRole>
```
**疑问:** 前端shiro标签是怎么跟后台交互的

> **重写shiro的filter**
- 产生的原因：
    - shiro-config中 /menu/**=authc,roles[admin,test] 表示 需要同时具有admin、test角色才能访问
    - shiro 里面默认判断的是 所有的角色和权限
- 重写的filter
    - 只要满足一个 角色 即可访问
    - 编写 MyShiroFilter 继承 AuthorizationFilter
    - 修改 spring-config 配置
        ```
        <property name="filters">
            <map>
                <entry key="roles">
                   <bean class="com.shiro.filter.MyShiroFilter"/>
                </entry>
            </map>
        </property>
        ```

> **动态添加验证规则**
- 目前验证规则是在 spring-config中配置的 ShiroFilterFactoryBean  filterChainDefinitions
- 如果需要额外的验证规则，需要动态添加规则
    - 自定义 MyShiroFilterFactoryBean 继承 ShiroFilterFactoryBean
        - setFilterChainDefinitions 方法只在启动的时候有效，如果数据库动态加规则，这个方法就无效了
        - 实现不用重启，即可动态追加权限
            - update方法实现了不用重启，动态从数据库读取权限追加
    - 配置 spring-config 把 ShiroFilterFactoryBean 替换成  MyShiroFilterFactoryBean


### 二、数据源采用 realm 的方式
- 暂时没用到