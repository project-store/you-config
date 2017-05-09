package miao.you.meng.config.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页跳转控制
 * Created by miaoyoumeng on 2017/5/8.
 */
@Controller
public class HomeController {
    @RequestMapping("/")
    public String homePageSlac(){
        return this.homePage();
    }

    @RequestMapping("")
    public String homePageNot(){
        return this.homePage();
    }


    private String homePage(){
        return "redirect:/app/info/list";
    }
}
