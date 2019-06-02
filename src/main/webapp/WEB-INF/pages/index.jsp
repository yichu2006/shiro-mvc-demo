<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
    <head>
        <title>主页面</title>
        <meta charset="UTF-8" />
        <!--引入主题样式-->
        <link rel="stylesheet" type="text/css" href="/easyui/themes/default/easyui.css" />
        <!--引入图标样式-->
        <link rel="stylesheet" type="text/css" href="/easyui/themes/icon.css" />
        <!--引入jQuery文件-->
        <script src="/easyui/jquery.min.js" type="text/javascript" charset="utf-8"></script>
        <!--引入EasyUi的js文件-->
        <script src="/easyui/jquery.easyui.min.js" type="text/javascript" charset="utf-8"></script>
    </head>
    <body class="easyui-layout">
        <div data-options="region:'north'" style="height: 30px">
            <a href="/logout.html">退出登录</a>
        </div>
        <div data-options="region:'west'" title="菜单" style="width: 150px">
            <ul class="easyui-tree" id="leftMenu">
                <li>
                    <span>系统管理</span>
                    <ul>
                        <shiro:hasPermission name="menu:list">
                            <li data-options="url:'/menu/list.html'">
                                <span>菜单管理</span>
                            </li>
                        </shiro:hasPermission>

                        <shiro:hasRole name="admin">
                            <li>
                                <span>角色管理</span>
                            </li>
                        </shiro:hasRole>

                        <li>
                            <span>用户管理</span>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <div data-options="region:'center'" title="首页">
            <div id="tabs" class="easyui-tabs"></div>
        </div>

        <script type="text/javascript">
            $(function() {
                $('#leftMenu').tree({
                    onClick: function(node) {
                        if($('#leftMenu').tree("isLeaf",node.target)) {
                            var tabs = $("#tabs");
                            var tab = tabs.tabs("getTab",node.text);
                            if(tab) {
                                tabs.tabs("select",node.text)
                            } else {
                                tabs.tabs("add",{
                                    title:node.text,
                                    href:node.url,
                                    closable:true
                                })
                            }
                        }
                    }
                })
            })
        </script>
    </body>

</html>
























