package com.shiro.controller;

import com.shiro.filter.MyShiroFilterFactoryBean;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("index")
    public String index() {
        return "index";
    }


    //模拟数据库 更新增加 权限
    @Autowired
    private MyShiroFilterFactoryBean myShiroFilterFactoryBean;
    @RequestMapping("update.html")
    public String update() {
        myShiroFilterFactoryBean.update();
        return "index";
    }

    @RequestMapping("error.html")
    public String error() {
        return "error";
    }

    @RequestMapping("do{path}.html")
    public String page(@PathVariable(name = "path") String path, Model model) {
        model.addAttribute("path",path);
        return "test";
    }

}
















