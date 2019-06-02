package com.shiro.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
    @RequestMapping("menu")
public class MenuController {

    @RequestMapping("list.html")
    public String list() {
        return "/menu_list";
    }

    @RequestMapping("go_edit.html")
    @RequiresPermissions("menu:edit")
    //同时具备2个权限，才能访问
//    @RequiresPermissions(value = {"menu:edit","menu:add"},logical = Logical.AND)
//    @RequiresRoles()
    public String goEdit() {
        return "/menu_edit";
    }

}
